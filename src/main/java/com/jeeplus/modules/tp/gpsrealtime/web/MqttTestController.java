package com.jeeplus.modules.tp.gpsrealtime.web;

import com.jeeplus.core.web.BaseController;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.integration.mqtt.support.MqttUtils;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

//@Controller
@RequestMapping("${adminPath}/tp/mqtt")
public class MqttTestController extends BaseController {

//    @Resource
    private MqttPahoMessageHandler mqttHandler;

//    @Resource
    private MqttPahoMessageDrivenChannelAdapter mqttAdapter;

    public void send(String topic, String content) {
        // 构建消息
        Message<String> messages = MessageBuilder.withPayload(content).setHeader(MqttHeaders.TOPIC, topic).build();
        // 发送消息
        mqttHandler.handleMessage(messages);
    }

    @RequestMapping("testSend")
    @ResponseBody
    public String testSend() {
        send("test/topic", "test from java... ");
        return "ok";
    }

    @RequestMapping("testReceive")
    @ResponseBody
    public String testReceive() {
        mqttAdapter.setCompletionTimeout(5000);
        mqttAdapter.setConverter(new DefaultPahoMessageConverter());
        mqttAdapter.setQos(1);
        mqttAdapter.setApplicationEventPublisher(new ApplicationEventPublisher() {
            @Override
            public void publishEvent(ApplicationEvent applicationEvent) {
                System.out.println(applicationEvent);
            }

            @Override
            public void publishEvent(Object o) {
                System.out.println(o);
            }
        });
        try {
            mqttAdapter.messageArrived("",new MqttMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ok";
    }

}
