package com.jeeplus.modules.tp.gpsrealtime.service;

import com.jeeplus.common.utils.SpringContextHolder;
import com.jeeplus.modules.monitor.entity.Task;
import org.quartz.DisallowConcurrentExecution;

@DisallowConcurrentExecution
public class DoSomethingTask extends Task {

    @Override
    public void run() {
        SpringContextHolder.getBean(TpDoSomethingService.class).runTask();
    }

}
