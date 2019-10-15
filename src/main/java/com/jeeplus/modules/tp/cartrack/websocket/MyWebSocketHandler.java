package com.jeeplus.modules.tp.cartrack.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class MyWebSocketHandler implements WebSocketHandler {

    private Logger logger = LoggerFactory.getLogger(MyWebSocketHandler.class);

    //保存用户链接
    private static ConcurrentHashMap<String, WebSocketSession> users = new ConcurrentHashMap<String, WebSocketSession>();

    // 连接 就绪时
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        users.put(session.getId(), session);
    }

    // 处理信息
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        System.err.println(session + "---->" + message + ":" + message.getPayload().toString());
    }

    // 处理传输时异常
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    // 关闭链接时
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {

        logger.debug("用户： " + session.getRemoteAddress() + " is leaving, because:" + closeStatus);
        users.remove(session.getId());
    }

    //是否支持分包
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    public void sendMessage(String value) {
        TextMessage message = new TextMessage(value);
        for (WebSocketSession user : users.values()) {
            try {
                if (user.isOpen()) {
                    user.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
