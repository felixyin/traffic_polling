/**
 * Copyright &copy; 2018-2020 <a href="http://www.yinbin.ink/">青岛前途软件技术</a> All rights reserved.
 */
package com.jeeplus.modules.tp.gpsrealtime.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;

import com.jeeplus.common.config.Global;
import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.common.utils.JsonUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.time.DateUtil;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.tp.car.entity.TpCar;
import com.jeeplus.modules.tp.car.service.TpCarService;
import com.jeeplus.modules.tp.cartrack.entity.TpCarTrack;
import com.jeeplus.modules.tp.cartrack.service.TpCarTrackService;
import com.jeeplus.modules.tp.cartrack.websocket.MyWebSocketHandler;
import com.jeeplus.modules.tp.gpshistory.entity.TpGpsHistory;
import com.jeeplus.modules.tp.gpshistory.service.TpGpsHistoryService;
import com.jeeplus.modules.tp.gpsrealtime.gdbean.DistanceRootBean;
import com.jeeplus.modules.tp.gpsrealtime.gdbean.PoiRootBean;
import com.jeeplus.modules.tp.gpsrealtime.gdbean.Results;
import com.jeeplus.modules.tp.gpsrealtime.gdbean.Roadinters;
import com.jeeplus.modules.tp.gpsrealtime.util.ConvertLocationUtil;
import com.jeeplus.modules.tp.gpsrealtime.util.HttpUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.tp.gpsrealtime.entity.TpGpsRealtime;
import com.jeeplus.modules.tp.gpsrealtime.mapper.TpGpsRealtimeMapper;

import javax.annotation.Resource;

import static org.apache.commons.beanutils.BeanUtils.copyProperties;

/**
 * 实时轨迹Service
 *
 * @author 尹彬
 * @version 2019-01-05
 */
@Service
@Transactional(readOnly = true)
public class TpGpsRealtimeService extends CrudService<TpGpsRealtimeMapper, TpGpsRealtime> {

    private Logger logger = LoggerFactory.getLogger(TpGpsRealtimeService.class);


    private static String GD_POI_URL = "http://restapi.amap.com/v3/geocode/regeo?key=806cfc91a232e5be93e358b5af52f1c9&poitype=路口名|道路名&radius=1000&extensions=all&batch=false&roadlevel=0&location=";
    private static String GD_DISTANCE_URL = "http://restapi.amap.com/v3/distance?key=806cfc91a232e5be93e358b5af52f1c9&type=1";

    @Resource
    private TpGpsRealtimeMapper gpsRealtimeMapper;

    @Resource
    private TpCarService carService;

    @Resource
    private TpCarTrackService carTrackService;

    @Resource
    private TpGpsHistoryService gpsHistoryService;

    @Resource
    private TpGpsRealtimeService gpsRealtimeService;

    private MyWebSocketHandler webSocketHandler = new MyWebSocketHandler();

    public TpGpsRealtime get(String id) {
        return super.get(id);
    }

    public List<TpGpsRealtime> findList(TpGpsRealtime tpGpsRealtime) {
        return super.findList(tpGpsRealtime);
    }

    public List<TpGpsRealtime> findGpsList(String deviceId) {
        return gpsRealtimeMapper.findGpsList(deviceId);
    }

    public Page<TpGpsRealtime> findPage(Page<TpGpsRealtime> page, TpGpsRealtime tpGpsRealtime) {
        return super.findPage(page, tpGpsRealtime);
    }

    @Transactional(readOnly = false)
    public void save(TpGpsRealtime tpGpsRealtime) {
        super.save(tpGpsRealtime);
    }

    @Transactional(readOnly = false)
    public void delete(TpGpsRealtime tpGpsRealtime) {
        super.delete(tpGpsRealtime);
    }


    @Autowired
    private TpCarService tpCarService;


    /**
     * 实时轨迹第一步：初始化所有车辆定位，和车辆轨迹
     *
     * @return
     */
    public Map<String, Map<String, Object>> loadRealtimeGpsMap() {
        Map<String, Map<String, Object>> realtimeGpsMap = new HashMap<>();
        /*
         a.deviceid AS "deviceId",
		a.name AS "name",
        a.location AS "location",
        a.update_date AS "updateDate",
		office.name AS "officeName"
         */
//        1、先获取所有的车辆列表
        List<Map> allList = tpCarService.findAllLocation();

//        2、查询缓存中存储的每个车辆gps信息
        for (Map carMap : allList) {
            String deviceId = carMap.get("deviceId").toString();

            Object carTackNeedObj = CacheUtils.get("carTackNeed");
            if (null != carTackNeedObj) {
                Map<String, Map<String, Object>> carTrackMap = (HashMap<String, Map<String, Object>>) carTackNeedObj;
                if (carTrackMap.containsKey(deviceId)) {
                    Map<String, Object> valueMap = carTrackMap.get(deviceId);
                    carMap.putAll(valueMap);
                }
            }

            List<TpGpsRealtime> gpsRealtimes = gpsRealtimeService.findGpsList(deviceId);
            carMap.put("gpsRealtimes",gpsRealtimes);

            realtimeGpsMap.put(deviceId, carMap); // 记录开始时间和结束时间
        }

//        构建map，返回显示轨迹
        return realtimeGpsMap;
    }


    /**
     * 处理gps的核心逻辑
     *
     * @param gpsMsg
     */
    @Transactional(readOnly = false)
    public void handleGPS(String gpsMsg) {

        logger.info("gps上传信息：" + gpsMsg);
        TpGpsRealtime gpsRealtime = ConvertLocationUtil.convert(gpsMsg);
        if (null == gpsRealtime) return;// 没有获取到gps信号，不再继续执行

        logger.info("解析处理后的信息：" + gpsRealtime.toString());

//        获取deviceId，判断缓存中是否存在
        String deviceId = gpsRealtime.getDeviceId();

//        CacheUtils.remove(deviceId);
//        Object carObj = CacheUtils.get(deviceId);
        TpCar car = carService.findByDeviceId(deviceId);

        if (null != car) {
//        如果存在，获取carId
            gpsRealtime.setCar(car);
            car.setLocation(gpsRealtime.getLonGD() + "," + gpsRealtime.getLatGD());
            carService.save(car);
            CacheUtils.put(deviceId, car);
        } else {
//        如果不存在，存储car表，获取carId
            TpCar car2 = new TpCar();
            car2.setDeviceId(deviceId);
            car2.setLocation(gpsRealtime.getLonGD() + "," + gpsRealtime.getLatGD());
            carService.save(car2);
            CacheUtils.put(deviceId, car2);
            gpsRealtime.setCar(car2);
        }

//        存储gps_realtime表
//        if ((gpsRealtime.getLonGD() + "," + gpsRealtime.getLatGD()).equals(car.getLocation())) { // 判断车辆是否停止不动 113行
        this.save(gpsRealtime);
//        }

//        记录时间到缓存：key为deviceId；value为一个map，startUpTime为第一次gps信号接收时间，lastUpTime为最后一次gps信号接收时间
        setCache(gpsRealtime, deviceId);

//        实时轨迹第二步：
//        websocket推送到所有连接的客户端地图中 TODO
//        webSocketHandler.sendMessage(gpsRealtime.getCar().getId() + "," + gpsRealtime.getLonGD() + "," + gpsRealtime.getLatGD());

    }

    private void setCache(TpGpsRealtime gpsRealtime, String deviceId) {
        Object carTackNeedObj = CacheUtils.get("carTackNeed");
        if (null != carTackNeedObj) {
            Map<String, Map<String, Object>> carTrackMap = (HashMap<String, Map<String, Object>>) carTackNeedObj;

            if (carTrackMap.containsKey(deviceId)) { // 已经记录过

                Map<String, Object> valueMap = carTrackMap.get(deviceId);
                valueMap.put("lastCarTrack", gpsRealtime);
                carTrackMap.put(deviceId, valueMap);

            } else { //第一次记录

                Map<String, Object> valueMap = new HashMap<>();
                valueMap.put("startCarTrack", gpsRealtime);
                valueMap.put("lastCarTrack", gpsRealtime);
                carTrackMap.put(deviceId, valueMap);

            }

            CacheUtils.put("carTackNeed", carTrackMap);

        } else {

            Map<String, Object> valueMap = new HashMap<>();
            valueMap.put("startCarTrack", gpsRealtime);
            valueMap.put("lastCarTrack", gpsRealtime);

            Map<String, Map<String, Object>> carTrackMap = new HashMap<>();
            carTrackMap.put(deviceId, valueMap);

            CacheUtils.put("carTackNeed", carTrackMap);
        }
    }

    private int calLastedTime(Date startDate) {
        long a = new Date().getTime();
        long b = startDate.getTime();
        int c = (int) ((a - b) / 1000);
        return c;
    }

    @Transactional(readOnly = false)
    public void runTask() {
        //        获取超时时间，或采用配置文件中配置的gps超时时间
        String carTackTimeoutStr = Global.getConfig("cartrack.timeout");
        String carTrackExcludeTimeStr = Global.getConfig("cartrack.exclude.time");
        String carTrackExcludeKmStr = Global.getConfig("cartrack.exclude.km");

        int carTackTimeout = Integer.parseInt(carTackTimeoutStr);
        int carTackExcludeTime = Integer.parseInt(carTrackExcludeTimeStr);
        int carTackExcludeKm = Integer.parseInt(carTrackExcludeKmStr);

        try {
            Object carTackNeedObj = CacheUtils.get("carTackNeed");
            if (null != carTackNeedObj) {
                Map<String, Map<String, Object>> carTrackMap = (HashMap<String, Map<String, Object>>) carTackNeedObj;
                Set<String> deviceIds = carTrackMap.keySet();

//                    循环判断每个设备是否超时
                for (String devideId : deviceIds) {

//                        判断时间是否超时
                    Map<String, Object> stringDateMap = carTrackMap.get(devideId);
                    TpGpsRealtime startGpsRealtime = (TpGpsRealtime) stringDateMap.get("startCarTrack");
                    TpGpsRealtime lastGpsRealtime = (TpGpsRealtime) stringDateMap.get("lastCarTrack");

//                        如果超时，则认为此次出车任务结束，则计算cartrack信息，单位秒
                    if (calLastedTime(lastGpsRealtime.getUpTime()) > carTackTimeout) {

//                        小于 carTackExcludeTime 的时间的行程，不做记录
                        long excludeTime = Math.round((lastGpsRealtime.getUpTime().getTime() - startGpsRealtime.getUpTime().getTime()) / 1000);
                        if (excludeTime < carTackExcludeTime) {
//                            清理垃圾数据
                            clearRealtime(devideId);
//                            清除缓存
                            clearCache(carTrackMap, devideId);

                            logger.info("excludeTime:" + excludeTime + " < carTackExcludeTime:" + carTackExcludeTime + "，因时间不足清理数据。");
                            return;
                        }


                        TpCarTrack carTrack = new TpCarTrack();
                        TpCar car = setCar(devideId, carTrack);
                        carTrack.setLocationBegin(startGpsRealtime.getLonGD() + "," + startGpsRealtime.getLatGD());
                        carTrack.setLocationEnd(lastGpsRealtime.getLonGD() + "," + lastGpsRealtime.getLatGD());
//                         计算高德POI
                        setNameBegin(carTrack);
//                         计算高德POI
                        setNameEnd(carTrack);
                        carTrack.setTimeBegin(startGpsRealtime.getUpTime());
                        carTrack.setTimeEnd(lastGpsRealtime.getUpTime());
//                         计算行驶距离
                        double km = setKmAndGetM(carTrack);
                        if (km < carTackExcludeKm) { // 如果行驶距离小于阈值则不处理（小哥可能在兜圈子）。
//                            清理垃圾数据
                            clearRealtime(devideId);
//                            清除缓存
                            clearCache(carTrackMap, devideId);
                            logger.info("km:" + km + " < carTackExcludeKm:" + carTackExcludeKm + "，因距离不足清理数据。");
                            return;
                        }
                        logger.info("==================================================================================================================");
//                        计算星期几
                        carTrack.setWhatDay(String.valueOf(DateUtil.getDayOfWeek(carTrack.getTimeBegin())));
//                        主驾驶人，默认为车辆负责人
                        User user = car.getUser();
                        if (null != user) {
                            carTrack.setUser(user);
                        }
//                        保存car_track表
                        carTrackService.save(carTrack);
//                        移动gps_realtime表对应数据，到gps_history表中
                        moveToHistory(devideId, carTrack);
//                        统计保存car表
                        updateCar(carTrack, car);
//                        清除缓存
                        clearCache(carTrackMap, devideId);

//                        实时轨迹第三步：清理实时轨迹 TODO
                        clearRealtime(devideId);

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("发生了严重错误： ----------------2 ");
            logger.error(e.getMessage());
        }
    }

    private void clearCache(Map<String, Map<String, Object>> carTrackMap, String devideId) {
        carTrackMap.remove(devideId);
        CacheUtils.put("carTackNeed", carTrackMap);
        CacheUtils.remove(devideId);
        logger.info("=================> 处理完毕，清理缓存：" + devideId);
    }

    @Transactional(readOnly = false)
    public void updateCar(TpCarTrack carTrack, TpCar car) {
        if (null != car) {
            car.setLocation(carTrack.getLocationEnd());
            car.setLocationName(carTrack.getNameEnd());
            Long sumTime = car.getSumTime();
            if (null == sumTime) {
                long time = carTrack.getTimeEnd().getTime() - carTrack.getTimeBegin().getTime();
                car.setSumTime((long) (0L + Math.ceil(time / 1000 / 60)));
            } else {
                long time = carTrack.getTimeEnd().getTime() - carTrack.getTimeBegin().getTime();
                car.setSumTime((long) (sumTime + Math.ceil(time / 1000 / 60)));
            }
            Double sumKm = car.getSumKm();
            if (null == sumKm) {
                car.setSumKm(carTrack.getKm());
            } else {
                car.setSumKm(sumKm + Math.ceil(carTrack.getKm()));
            }
            carService.save(car);
            logger.info("=================> car数据已更新：" + car);
        }
    }

    @Transactional(readOnly = false)
    public void clearRealtime(String devideId) {
        List<TpGpsRealtime> gpsRealtimes = gpsRealtimeService.findGpsList(devideId);
        for (TpGpsRealtime gpsRealtime : gpsRealtimes) {
            gpsRealtimeService.delete(gpsRealtime);
        }
    }

    @Transactional(readOnly = false)
    public void moveToHistory(String devideId, TpCarTrack carTrack) {
        List<TpGpsRealtime> gpsRealtimes = gpsRealtimeService.findGpsList(devideId);
        for (TpGpsRealtime gpsRealtime : gpsRealtimes) {
            TpGpsHistory gpsHistory = new TpGpsHistory();
            try {
                copyProperties(gpsHistory, gpsRealtime);
                gpsHistoryService.save(gpsHistory);
                gpsRealtimeService.delete(gpsRealtime);
                logger.info("=================> 迁移gps轨迹为history:" + gpsHistory);
            } catch (Exception e) {
                gpsRealtimeService.delete(gpsRealtime);
                e.printStackTrace();
                logger.error("发生了严重错误： ----------------1 ");
                logger.error(e.getMessage());
            }
        }
    }

    private TpCar setCar(String deviceId, TpCarTrack carTrack) {
        TpCar car = carService.findByDeviceId(deviceId);
        if (null != car) {
//                              如果存在，获取carId
            carTrack.setCar(car);
        }
        return car;
    }

    private double setKmAndGetM(TpCarTrack carTrack) {
        String jsonKm = HttpUtil.get(GD_DISTANCE_URL +
                "&origins=" + carTrack.getLocationBegin() + "&destination=" + carTrack.getLocationEnd());
        if (StringUtils.isNotBlank(jsonKm)) {
            DistanceRootBean distanceRootBean = JsonUtils.jsonToObject(jsonKm, DistanceRootBean.class);
            List<Results> results = null;
            if (distanceRootBean != null) {
                results = distanceRootBean.getResults();
            }
            if (null != results && results.size() > 0) {
                Results results1 = results.get(0);
                double m = Double.parseDouble(results1.getDistance());
                double km = new BigDecimal(m / 1000D).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                carTrack.setKm(km);
                return m;
            }
        }
        return 0D;
    }

    private void setNameEnd(TpCarTrack carTrack) {
        String jsonEnd = HttpUtil.get(GD_POI_URL + carTrack.getLocationEnd());
        if (StringUtils.isNotBlank(jsonEnd)) {
            PoiRootBean poiRootBean = JsonUtils.jsonToObject(jsonEnd, PoiRootBean.class);
            List<Roadinters> roadinters = poiRootBean.getRegeocode().getRoadinters();
            if (null != roadinters && roadinters.size() > 0) {
                Roadinters roadinters1 = roadinters.get(0);
                String nameEnd = roadinters1.getFirstName() + "与" + roadinters1.getSecondName() +
                        "交叉口" + roadinters1.getDirection() + StringUtils.substringBefore(roadinters1.getDistance(), ".") + "米";
                logger.info(nameEnd);
                carTrack.setNameEnd(nameEnd);
            }
        }
    }

    private void setNameBegin(TpCarTrack carTrack) {
        String jsonBegin = HttpUtil.get(GD_POI_URL + carTrack.getLocationBegin());
        if (StringUtils.isNotBlank(jsonBegin)) {
            PoiRootBean poiRootBean = JsonUtils.jsonToObject(jsonBegin, PoiRootBean.class);
            String nameBegin = poiRootBean.getRegeocode().getFormattedAddress();
            logger.info(nameBegin);
            carTrack.setNameBegin(nameBegin);
        }
    }

}