/**
 * 
 */
package com.drzk.pay.service.impl;

import javax.annotation.Resource;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import com.drzk.pay.mqtt.MqttBuilder;

/**
 * @author Xu Xiwen
 *
 */
//@Service
public class MqttSubsricueTopicServiceImpl implements ApplicationRunner {

	@Resource
	MqttBuilder mqttBuilder;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.boot.ApplicationRunner#run(org.springframework.boot.
	 * ApplicationArguments)
	 */
	@Override
	public void run(ApplicationArguments args) throws Exception {
		String[] topices = { "getPayment/+", "getPayment/wechat", "getPayment/alipay" };
		mqttBuilder.mqttPahoMessageDrivenChannelAdapter().addTopic(topices);
	}
}
