package com.jeeplus.modules.tp.mqtt.service;

import com.jeeplus.modules.tp.mqtt.util.ConvertLocationUtil;
import com.jeeplus.modules.tp.mqtt.util.GpsBean;
import org.springframework.stereotype.Service;

@Service
public class MqttService {

    public void startCase(String gpsMsg) {

        System.out.println("gps上传信息：" + gpsMsg);
        GpsBean gpsBean = ConvertLocationUtil.convert(gpsMsg);
        System.out.println("解析处理后的信息：" + gpsBean.toString());
    }
}


