package com.jeeplus.modules.tp.gpsrealtime.service;

import com.jeeplus.common.utils.SpringContextHolder;
import com.jeeplus.modules.monitor.entity.Task;
import org.quartz.DisallowConcurrentExecution;

@DisallowConcurrentExecution
public class GpsHistoryTask extends Task {

    @Override
    public void run() {
        SpringContextHolder.getBean(TpGpsRealtimeService.class).runTask();
    }

}
