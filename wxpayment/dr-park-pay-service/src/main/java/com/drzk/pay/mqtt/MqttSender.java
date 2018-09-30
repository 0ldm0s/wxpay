package com.drzk.pay.mqtt;

import com.drzk.pay.mqtt.MqttBuilder.MyGateWay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MqttSender {

    private static final Logger log = LoggerFactory.getLogger(MqttSender.class);

    @Resource
    MyGateWay way;

    public static MqttSender instance = new MqttSender();

    public static MqttSender getInstance() {
        return instance;
    }
    @Async
    public void send(String topic,String message) {
        try {
            way.sendToMqtt( message, topic);
            log.info( "{}",message );
        } catch (Exception e) {
            log.error( "sent message error:{}",e.getMessage(),e );
        }
    }
}
