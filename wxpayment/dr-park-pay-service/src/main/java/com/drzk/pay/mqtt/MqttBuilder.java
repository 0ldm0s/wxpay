package com.drzk.pay.mqtt;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * mqtt构建器
 *
 * @author Administrator
 * @since 2018-06-07
 */
@Component
@Configuration
public class MqttBuilder {

	@Resource
	MqttConfig config;

	@Resource
	MqttReceiveService mqttMessageReceive;

	public static final Integer KEEP_ALIVE_TIME = 20;

	public static final Integer COMPLIE_TIMEOUT_P = 10000;

	/**
	 * 初始化订阅配置
	 *
	 * @return
	 */
	@Bean
	public MqttPahoClientFactory mqttClientFactory() {
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		// 和服务器断开后 重新连接接受断开断开期间服务器接收到的消息，
		factory.setCleanSession(false);
		factory.setServerURIs(config.getMqttServer());
		factory.setKeepAliveInterval(KEEP_ALIVE_TIME);
		factory.setUserName(config.getMqttUserName());
		factory.setPassword(config.getMqttPwd());

		return factory;
	}

	/**
	 * MQTT订阅通道
	 *
	 * @return
	 */
	@Bean
	public MessageChannel mqttInputChannel() {
		if (config.getSubTopics() == null || config.getSubTopics().length() == 0) {
			return null;
		}
		return new DirectChannel();
	}

	/**
	 * MQTT订阅程序
	 *
	 * @return
	 */
	@Bean
	public MessageProducerSupport inbound() {
		return mqttPahoMessageDrivenChannelAdapter();
	}

	@Bean
	public MqttPahoMessageDrivenChannelAdapter mqttPahoMessageDrivenChannelAdapter() {
		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
				config.getReceiveClintId(), mqttClientFactory());
		// 设置操作的完成超时 Default 30000 milliseconds.
		adapter.setCompletionTimeout(COMPLIE_TIMEOUT_P);
		// 设置断开重连时间 默认是10秒
		// adapter.setRecoveryInterval( 5 );
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setOutputChannel(mqttInputChannel());
		addSubTopics(adapter);
		adapter.setQos(config.getQos());
		return adapter;
	}

	/**
	 * 添加订阅主题，支持多主题
	 *
	 * @param adapter
	 */
	private void addSubTopics(MqttPahoMessageDrivenChannelAdapter adapter) {
		if (config.getSubTopics() == null || config.getSubTopics().length() == 0) {
			return;
		}
		String topics = config.getSubTopics();
		if (topics != null) {
			String topicArray[] = topics.split(",");
			for (String topic : topicArray) {
				adapter.addTopic(topic);
			}
		}
	}

	/**
	 * mqtt订阅消息读取 监听句柄，有消息 会回调此方法
	 *
	 * @return
	 */
	@Bean
	@ServiceActivator(inputChannel = "mqttInputChannel")
	public MessageHandler handler() {
		if (config.getSubTopics() == null || config.getSubTopics().length() == 0) {
			return null;
		}
		return new MessageHandler() {
			@Override
			public void handleMessage(Message<?> message) {
				String topicName = (String) message.getHeaders().get(MqttHeaders.TOPIC);
				if (StringUtils.isBlank(topicName))
					topicName = (String) message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC);
				mqttMessageReceive.receiveMessage(topicName, String.valueOf(message.getPayload()));
			}
		};
	}

	/**
	 * mqtt发布主题通道
	 *
	 * @return
	 */
	@Bean
	@ServiceActivator(inputChannel = "mqttOutboundChannel")
	public MessageHandler mqttOutBound() {
		MqttPahoMessageHandler msgHandler = new MqttPahoMessageHandler(config.getSendClientId(), mqttClientFactory());
		msgHandler.setAsync(true);
		return msgHandler;
	}

	/**
	 * mqtt发布通道
	 *
	 * @return
	 */
	@Bean
	public MessageChannel mqttOutboundChannel() {
		return new DirectChannel();
	}

	/**
	 * mqtt发消息网关路由
	 *
	 * @author Administrator
	 */
	@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
	public interface MyGateWay {
		void sendToMqtt(String data, @Header(MqttHeaders.TOPIC) String topic);
	}

}
