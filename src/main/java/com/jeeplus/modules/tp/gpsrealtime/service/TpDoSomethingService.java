/**
 * Copyright &copy; 2018-2020 <a href="http://www.yinbin.ink/">青岛前途软件技术</a> All rights reserved.
 */
package com.jeeplus.modules.tp.gpsrealtime.service;

import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.time.DateUtil;
import com.jeeplus.modules.monitor.entity.ScheduleJob;
import com.jeeplus.modules.monitor.service.ScheduleJobService;
import com.jeeplus.modules.tp.car.entity.TpCar;
import com.jeeplus.modules.tp.car.service.TpCarService;
import com.jeeplus.modules.tp.cartrack.entity.TpCarTrack;
import com.jeeplus.modules.tp.cartrack.service.TpCarTrackService;
import com.jeeplus.modules.tp.gpshistory.entity.TpGpsHistory;
import com.jeeplus.modules.tp.gpshistory.service.TpGpsHistoryService;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

                    Calendar c = Calendar.getInstance();
                    if (split.length == 4) { // 自定义时间
                        Date customDate = new Date();
                        String toStartTime = split[3];
                        customDate = DateUtils.parseDate(toStartTime);
                        c.setTime(customDate);
                    }
                    Date timeBegin1 = new Date();
                    timeBegin1 = DateUtil.setDays(timeBegin1, c.get(Calendar.DAY_OF_MONTH));
                    timeBegin1 = DateUtil.setMonths(timeBegin1, c.get(Calendar.MONTH));
                    timeBegin1 = DateUtil.setYears(timeBegin1, c.get(Calendar.YEAR));
                    timeBegin1 = DateUtil.setHours(timeBegin1, timeBegin.getHours());
                    timeBegin1 = DateUtil.setMinutes(timeBegin1, timeBegin.getMinutes());
                    timeBegin1 = DateUtil.setSeconds(timeBegin1, timeBegin.getSeconds());
                    /*int d1 = c.get(Calendar.HOUR_OF_DAY);
                    if (d1 != 0) {
                        timeBegin1 = DateUtils.setHours(timeBegin1, d1);
                    } else {
                        timeBegin1 = DateUtils.setHours(timeBegin1, timeBegin.getHours());
                    }
                    int m1 = c.get(Calendar.MINUTE);
                    if (m1 != 0) {
                        timeBegin1 = DateUtil.setMinutes(timeBegin1, m1);
                    } else {
                        timeBegin1 = DateUtil.setMinutes(timeBegin1, timeBegin.getMinutes());
                    }
                    int s1 = c.get(Calendar.SECOND);
                    if (s1 != 0) {
                        timeBegin1 = DateUtil.setSeconds(timeBegin1, s1);
                    } else {
                        timeBegin1 = DateUtil.setSeconds(timeBegin1, timeBegin.getSeconds());
                    }*/
                    timeBegin1 = DateUtils.addSeconds(timeBegin1, RandomUtils.nextInt(100, 500));
                    tpCarTrack.setTimeBegin(timeBegin1);

                    Date timeEnd = tpCarTrack.getTimeEnd();
                    Date timeEnd1 = new Date();
                    timeEnd1 = DateUtil.setDays(timeEnd1, c.get(Calendar.DAY_OF_MONTH));
                    timeEnd1 = DateUtil.setMonths(timeEnd1, c.get(Calendar.MONTH));
                    timeEnd1 = DateUtil.setYears(timeEnd1, c.get(Calendar.YEAR));
                    timeEnd1 = DateUtil.setHours(timeEnd1, timeEnd.getHours());
                    timeEnd1 = DateUtil.setMinutes(timeEnd1, timeEnd.getMinutes());
                    timeEnd1 = DateUtil.setSeconds(timeEnd1, timeEnd.getSeconds());


                    /*int d2 = c.get(Calendar.HOUR_OF_DAY);
                    if (d2 != 0) {
                        timeEnd1 = DateUtils.setHours(timeEnd1, d2);
                    } else {
                        timeEnd1 = DateUtils.setHours(timeEnd1, timeEnd.getHours());
                    }
                    int m2 = c.get(Calendar.MINUTE);
                    if (m2 != 0) {
                        timeEnd1 = DateUtil.setMinutes(timeEnd1, m2);
                    } else {
                        timeEnd1 = DateUtil.setMinutes(timeEnd1, timeEnd.getMinutes());
                    }
                    int s2 = c.get(Calendar.SECOND);
                    if (s2 != 0) {
                        timeEnd1 = DateUtil.setSeconds(timeEnd1, s2);
                    } else {
                        timeEnd1 = DateUtil.setSeconds(timeEnd1, timeEnd.getSeconds());
                    }*/
                    timeEnd1 = DateUtils.addSeconds(timeEnd1, RandomUtils.nextInt(100, 500));
                    tpCarTrack.setTimeEnd(timeEnd1);

                    String nameEnd = tpCarTrack.getNameEnd();
                    String s = StringUtils.replacePattern(nameEnd, "\\d{1}米", RandomUtils.nextInt(1, 9) + "米");
                    tpCarTrack.setNameEnd(s);

                    Double km = tpCarTrack.getKm();
                    km += RandomUtils.nextDouble(0.10, 0.80);
                    tpCarTrack.setKm((double) (Math.round(km * 100) / 100.0));

                    // 星期几
                    tpCarTrack.setWhatDay(String.valueOf(DateUtil.getDayOfWeek(tpCarTrack.getTimeBegin())));

                    tpCarTrack.setRemarks("-");

                    TpCar carParam = new TpCar();
                    carParam.setName(toCarName);


                    TpCar tpCar = tpCarService.findUniqueByProperty("name", toCarName);
                    if (tpCar == null && StringUtils.isBlank(tpCar.getId())) {
                        continue; // 默认忽略，复制的目标车辆不存在的那些数据
                    }
                    tpCarTrack.setCar(tpCar);

                    tpCarTrackService.save(tpCarTrack);

                    List<TpGpsHistory> tpGpsHistories = tpGpsHistoryService.findListByCarTrackId(trackId);
                    for (TpGpsHistory tpGpsHistory : tpGpsHistories) {
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

    private Date setDate(Date date, int type, int day) {
        Calendar c = Calendar.getInstance();
        c.setLenient(true);
        c.setTime(date);
        c.set(type, day);
        return c.getTime();
    }

}