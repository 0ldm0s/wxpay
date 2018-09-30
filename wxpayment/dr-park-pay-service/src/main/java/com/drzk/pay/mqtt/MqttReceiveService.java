package com.drzk.pay.mqtt;

import org.springframework.stereotype.Component;

/**
 * 消息订阅后，这里可以接收
 * mqtt消息处理器
 *
 * @author Administrator
 * @since 2018-06-07
 */
@Component
public interface MqttReceiveService {
    /**
     * 消息接收处理回调方法
     * 所有接收消息在此统一处理
     * 统一落地
     *
     * @param topic   主题
     * @param message 消息
     */
    void receiveMessage(String topic, String message);
}
