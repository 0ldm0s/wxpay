package com.drzk.pay.mqtt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * mqtt配置参数
 * 
 * @author Administrator
 * @since 2018-06-07
 */
@Component
@ConfigurationProperties(prefix = "drzk-mqtt")
public class MqttConfig {

	private String mqttServer;
	private String mqttUserName;
	private String mqttPwd;
	private String pubTopics;
	private String subTopics;
	private int qos;
	/**
	 * 发送客户ID
	 */
	private String sendClientId;
	/**
	 * 接收客户ID
	 * 
	 * @return
	 */
	private String receiveClintId;

	public String getSendClientId() {
		return sendClientId;
	}

	public void setSendClientId(String sendClientId) {
		this.sendClientId = sendClientId;
	}

	public String getReceiveClintId() {
		return receiveClintId;
	}

	public void setReceiveClintId(String receiveClintId) {
		this.receiveClintId = receiveClintId;
	}

	public int getQos() {
		return qos;
	}

	public void setQos(int qos) {
		this.qos = qos;
	}

	public String getMqttServer() {
		return mqttServer;
	}

	public void setMqttServer(String mqttServer) {
		this.mqttServer = mqttServer;
	}

	public String getMqttUserName() {
		return mqttUserName;
	}

	public void setMqttUserName(String mqttUserName) {
		this.mqttUserName = mqttUserName;
	}

	public String getMqttPwd() {
		return mqttPwd;
	}

	public void setMqttPwd(String mqttPwd) {
		this.mqttPwd = mqttPwd;
	}

	public String getPubTopics() {
		return pubTopics;
	}

	public void setPubTopics(String pubTopics) {
		this.pubTopics = pubTopics;
	}

	public String getSubTopics() {
		return subTopics;
	}

	public void setSubTopics(String subTopics) {
		this.subTopics = subTopics;
	}

}
