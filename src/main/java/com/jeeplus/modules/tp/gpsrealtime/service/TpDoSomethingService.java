/**
 * Copyright &copy; 2018-2020 <a href="http://www.yinbin.ink/">青岛前途软件技术</a> All rights reserved.
 */
package com.jeeplus.modules.tp.gpsrealtime.service;

import com.jeeplus.common.config.Global;
import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.JsonUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.number.NumberUtil;
import com.jeeplus.common.utils.time.DateUtil;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.monitor.entity.ScheduleJob;
import com.jeeplus.modules.monitor.service.ScheduleJobService;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.tp.car.entity.TpCar;
import com.jeeplus.modules.tp.car.mapper.TpCarMapper;
import com.jeeplus.modules.tp.car.service.TpCarService;
import com.jeeplus.modules.tp.cartrack.entity.TpCarTrack;
import com.jeeplus.modules.tp.cartrack.service.TpCarTrackService;
import com.jeeplus.modules.tp.cartrack.websocket.MyWebSocketHandler;
import com.jeeplus.modules.tp.gpshistory.entity.TpGpsHistory;
import com.jeeplus.modules.tp.gpshistory.service.TpGpsHistoryService;
import com.jeeplus.modules.tp.gpsrealtime.entity.TpGpsRealtime;
import com.jeeplus.modules.tp.gpsrealtime.gdbean.DistanceRootBean;
import com.jeeplus.modules.tp.gpsrealtime.gdbean.PoiRootBean;
import com.jeeplus.modules.tp.gpsrealtime.gdbean.Results;
import com.jeeplus.modules.tp.gpsrealtime.gdbean.Roadinters;
import com.jeeplus.modules.tp.gpsrealtime.mapper.TpGpsRealtimeMapper;
import com.jeeplus.modules.tp.gpsrealtime.util.ConvertLocationUtil;
import com.jeeplus.modules.tp.gpsrealtime.util.HttpUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.PatternMatchUtils;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;

/**
 * 轨迹加工Service
 *
 * @author 尹彬
 * @version 2019-03-24
 */
@Service
@Transactional(readOnly = true)
public class TpDoSomethingService {

    private Logger logger = LoggerFactory.getLogger(TpDoSomethingService.class);

    @Resource
    private ScheduleJobService scheduleJobService;

    @Resource
    private TpCarTrackService tpCarTrackService;

    @Resource
    private TpGpsHistoryService tpGpsHistoryService;

    @Resource
    private TpCarService tpCarService;

    @Transactional(readOnly = false)
    public void runTask() {
        try {
            ScheduleJob scheduleJob = scheduleJobService.findUniqueByProperty("classname", "com.jeeplus.modules.tp.gpsrealtime.service.DoSomethingTask");
            String remarks = scheduleJob.getDescription();
            Date now = new Date();
//        Calendar c = Calendar.getInstance();

            String[] jobs = remarks.split(";");
            for (String job : jobs) {
                String[] split = job.split(",");
                String carName = split[0];
                String startTime = split[1];
                String toCarName = split[2];

                TpCarTrack tpCarTrack = tpCarTrackService.loadCarTrack(carName, startTime);

                if (null != tpCarTrack) {
                    String trackId = tpCarTrack.getId();

                    tpCarTrack.setId(null);

                    Date timeBegin = tpCarTrack.getTimeBegin();
                    Date timeBegin1 = new Date();
                    timeBegin1 = DateUtils.setHours(timeBegin1, timeBegin.getHours());
                    timeBegin1 = DateUtil.setMinutes(timeBegin1, timeBegin.getMinutes());
                    timeBegin1 = DateUtil.setSeconds(timeBegin1, timeBegin.getSeconds());
                    timeBegin1 = DateUtils.addSeconds(timeBegin1, RandomUtils.nextInt(100, 500));
                    tpCarTrack.setTimeBegin(timeBegin1);

                    Date timeEnd = tpCarTrack.getTimeEnd();
                    Date timeEnd1 = new Date();
                    timeEnd1 = DateUtils.setHours(timeEnd1, timeEnd.getHours());
                    timeEnd1 = DateUtil.setMinutes(timeEnd1, timeEnd.getMinutes());
                    timeEnd1 = DateUtil.setSeconds(timeEnd1, timeEnd.getSeconds());
                    timeEnd1 = DateUtils.addSeconds(timeEnd1, RandomUtils.nextInt(100, 500));
                    tpCarTrack.setTimeEnd(timeEnd1);

                    String nameEnd = tpCarTrack.getNameEnd();
                    String s = StringUtils.replacePattern(nameEnd, "\\d{1}米", RandomUtils.nextInt(1, 9) + "米");
                    tpCarTrack.setNameEnd(s);

                    Double km = tpCarTrack.getKm();
                    km += RandomUtils.nextDouble(0.10, 0.80);
                    tpCarTrack.setKm((double) (Math.round(km * 100) / 100.0));

                    tpCarTrack.setRemarks("-");

                    TpCar carParam = new TpCar();
                    carParam.setName(toCarName);

                    TpCar tpCar = tpCarService.findUniqueByProperty("name", toCarName);
                    tpCarTrack.setCar(tpCar);

                    tpCarTrackService.save(tpCarTrack);

                    List<TpGpsHistory> tpGpsHistories = tpGpsHistoryService.findListByCarTrackId(trackId);
                    for (TpGpsHistory tpGpsHistory : tpGpsHistories) {
                        System.out.println(tpGpsHistory);
                        tpGpsHistory.setId(null);
                        tpGpsHistory.setCarTrack(tpCarTrack);
                        tpGpsHistory.setCar(tpCarTrack.getCar());
                        tpGpsHistory.setRemarks("-");
                        tpGpsHistoryService.save(tpGpsHistory);
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}