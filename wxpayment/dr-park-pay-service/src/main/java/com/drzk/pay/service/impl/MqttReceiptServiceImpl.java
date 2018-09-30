/**
 * 
 */
package com.drzk.pay.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.drzk.pay.constant.PayConstants;
import com.drzk.pay.mqtt.MqttReceiveService;
import com.drzk.pay.mqtt.MqttSender;
import com.drzk.pay.service.IAlipayService;
import com.drzk.pay.service.IWXPayService;
import com.drzk.pay.vo.ResponseHandlerVO;

/**
 * @author Xu Xiwen
 *
 */
@Service
public class MqttReceiptServiceImpl implements MqttReceiveService {

	Logger logger = LoggerFactory.getLogger(MqttReceiptServiceImpl.class);

	@Resource
	IAlipayService alipayService;

	@Resource
	IWXPayService wxPayService;
	
	@Resource
	MqttSender mqttSender;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.drzk.mqtt.mqtt.MqttReceiveService#receiveMessage(java.lang.String,
	 * java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void receiveMessage(String topic, String message) {
		logger.info("The Payment System recepting info by topic " + topic + ", message " + message);
		final Map<String, Object> reqData = JSON.parseObject(message, Map.class);
		final Map<String, Object> head = (Map<String, Object>) reqData.get("head");
		final Map<String, Object> body = (Map<String, Object>) reqData.get("body");
		String replyTopic = (String) head.get("mqtt_reply_topic");
		if (topic.equals(PayConstants.MQTT_WECHAT_UNIFIED_ORDER)) {// 来自微信支付MQTT消息
			try {
				ResponseHandlerVO response = wxPayService.unifiedOrder(body);
				logger.info("发送微信支付订单数据到mqtt:" + replyTopic);
				Map<String, Object> mqttMessageMap = new HashMap<String, Object>();
				Map<String, Object> mqttMessageHead = new HashMap<String, Object>();
				Map<String, Object> mqttMessageBody = new HashMap<String, Object>();
				if(response.isSuccess()) {
					mqttMessageHead.put("status", 1);
				}else {
					mqttMessageHead.put("status", 0);
				}
				mqttMessageMap.put("head", mqttMessageHead);
				mqttMessageBody.put("order", body);
				mqttMessageBody.put("result", response.getResultBody());
				mqttMessageMap.put("body", mqttMessageBody);
				mqttSender.send(replyTopic, JSON.toJSONString(mqttMessageMap));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (topic.equals(PayConstants.MQTT_ALIPAY_UNIFIED_ORDER)) {// 来自支付宝支付MQTT消息

		}

	}

}
