/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tp.gpsrealtime.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;

import com.jeeplus.common.config.Global;
import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.common.utils.JsonUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.tp.car.entity.TpCar;
import com.jeeplus.modules.tp.car.service.TpCarService;
import com.jeeplus.modules.tp.cartrack.entity.TpCarTrack;
import com.jeeplus.modules.tp.cartrack.service.TpCarTrackService;
import com.jeeplus.modules.tp.gpshistory.entity.TpGpsHistory;
import com.jeeplus.modules.tp.gpshistory.service.TpGpsHistoryService;
import com.jeeplus.modules.tp.gpsrealtime.gdbean.DistanceRootBean;
import com.jeeplus.modules.tp.gpsrealtime.gdbean.PoiRootBean;
import com.jeeplus.modules.tp.gpsrealtime.gdbean.Results;
import com.jeeplus.modules.tp.gpsrealtime.gdbean.Roadinters;
import com.jeeplus.modules.tp.gpsrealtime.util.ConvertLocationUtil;
import com.jeeplus.modules.tp.gpsrealtime.util.HttpUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.tp.gpsrealtime.entity.TpGpsRealtime;
import com.jeeplus.modules.tp.gpsrealtime.mapper.TpGpsRealtimeMapper;

import javax.annotation.Resource;

/**
 * 实时轨迹Service
 *
 * @author 尹彬
 * @version 2019-01-05
 */
@Service
@Transactional(readOnly = true)
public class TpGpsRealtimeService extends CrudService<TpGpsRealtimeMapper, TpGpsRealtime> {

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

    @Transactional(readOnly = false)
    public void handleGPS(String gpsMsg) {

        System.out.println("gps上传信息：" + gpsMsg);
        TpGpsRealtime gpsRealtime = ConvertLocationUtil.convert(gpsMsg);
        if (null == gpsRealtime) return;// 没有获取到gps信号，不再继续执行

        System.out.println("解析处理后的信息：" + gpsRealtime.toString());

//        获取deviceId，判断缓存中是否存在
        String deviceId = gpsRealtime.getDeviceId();

//        CacheUtils.remove(deviceId);
//        Object carObj = CacheUtils.get(deviceId);
        TpCar car = carService.findUniqueByProperty("deviceId", deviceId);

        if (null != car) {
//        如果存在，获取carId
            gpsRealtime.setCar(car);
        } else {
//        如果不存在，存储car表，获取carId
            TpCar car2 = new TpCar();
            car2.setDeviceId(deviceId);
            carService.save(car2);
            CacheUtils.put(deviceId, car2);
            gpsRealtime.setCar(car2);
        }

//        存储gps_realtime表
        this.save(gpsRealtime);

//        记录时间到缓存：key为deviceId；value为一个map，startUpTime为第一次gps信号接收时间，lastUpTime为最后一次gps信号接收时间
        setCache(gpsRealtime, deviceId);

//        websocket推送到所有连接的客户端地图中 TODO
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
//                        long excludeTime = lastGpsRealtime.getUpTime().getTime() - startGpsRealtime.getUpTime().getTime();
//                        if (excludeTime < carTackExcludeTime) {
////                            清理垃圾数据
//                            clearRealtime(devideId);
////                            清除缓存
//                            clearCache(carTrackMap, devideId);
//                            return;
//                        }

                        System.out.println("==================================================================================================================");

                        TpCarTrack carTrack = new TpCarTrack();
                        TpCar car = setCar(devideId, carTrack);
                        carTrack.setLocationBegin(startGpsRealtime.getLonGD() + "," + lastGpsRealtime.getLatGD());
                        carTrack.setLocationEnd(lastGpsRealtime.getLonGD() + "," + lastGpsRealtime.getLatGD());
//                         计算高德POI
                        setNameBegin(carTrack);
//                         计算高德POI
                        setNameEnd(carTrack);
                        carTrack.setTimeBegin(startGpsRealtime.getUpTime());
                        carTrack.setTimeEnd(lastGpsRealtime.getUpTime());
//                         计算行驶距离
                        double km = getKm(carTrack);
//                        if (km < carTackExcludeKm) { // 如果行驶距离小于阈值则不处理（小哥可能在兜圈子）。
////                            清理垃圾数据
//                            clearRealtime(devideId);
////                            清除缓存
//                            clearCache(carTrackMap, devideId);
//                            return;
//                        }
//                        保存car_track表
                        carTrackService.save(carTrack);
//                        移动gps_realtime表对应数据，到gps_history表中
                        moveToHistory(devideId, carTrack);
//                        统计保存car表
                        updateCar(carTrack, car);
//                        清除缓存
                        clearCache(carTrackMap, devideId);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearCache(Map<String, Map<String, Object>> carTrackMap, String devideId) {
        carTrackMap.remove(devideId);
        CacheUtils.put("carTackNeed", carTrackMap);
//                        CacheUtils.remove(devideId);
        System.out.println("=================> 处理完毕，清理缓存：" + devideId);
    }

    @Transactional(readOnly = false)
    public void updateCar(TpCarTrack carTrack, TpCar car) {
        if (null != car) {
            car.setLocation(carTrack.getLocationEnd());
            car.setLocationName(carTrack.getNameEnd());
            Integer sumTime = car.getSumTime();
            if (null == sumTime) {
                long time = carTrack.getTimeEnd().getTime() - carTrack.getTimeBegin().getTime();
                car.setSumTime(Math.round(time / 1000 / 60));
            } else {
                long time = carTrack.getTimeEnd().getTime() - carTrack.getTimeBegin().getTime();
                car.setSumTime(sumTime + Math.round(time / 1000 / 60));
            }
            Double sumKm = car.getSumKm();
            if (null == sumKm) {
                car.setSumKm(carTrack.getKm());
            } else {
                car.setSumKm(sumKm + carTrack.getKm());
            }
            carService.save(car);
            System.out.println("=================> car数据已更新：" + car);
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
                BeanUtils.copyProperties(gpsHistory, gpsRealtime);
                gpsHistory.setCar(carTrack.getCar());
                gpsHistory.setCarTrack(carTrack);
                gpsHistory.setId(null);
                gpsHistoryService.save(gpsHistory);
                gpsRealtimeService.delete(gpsRealtime);
                System.out.println("=================> 迁移gps轨迹为history:" + gpsHistory);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private TpCar setCar(String deviceId, TpCarTrack carTrack) {
        TpCar car = carService.findUniqueByProperty("deviceId", deviceId);
        if (null != car) {
//                              如果存在，获取carId
            carTrack.setCar(car);
        }
        return car;
    }

    private double getKm(TpCarTrack carTrack) {
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
                double km = new BigDecimal(Double.parseDouble(results1.getDistance()) / 1000D)
                        .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                carTrack.setKm(km);
                return km;
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
                System.out.println(nameEnd);
                carTrack.setNameEnd(nameEnd);
            }
        }
    }

    private void setNameBegin(TpCarTrack carTrack) {
        String jsonBegin = HttpUtil.get(GD_POI_URL + carTrack.getLocationBegin());
        if (StringUtils.isNotBlank(jsonBegin)) {
            PoiRootBean poiRootBean = JsonUtils.jsonToObject(jsonBegin, PoiRootBean.class);
            String nameBegin = poiRootBean.getRegeocode().getFormattedAddress();
            System.out.println(nameBegin);
            carTrack.setNameBegin(nameBegin);
        }
    }

}