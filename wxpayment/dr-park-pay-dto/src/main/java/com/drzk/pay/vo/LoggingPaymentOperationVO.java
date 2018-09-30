/**
 * 
 */
package com.drzk.pay.vo;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Xu Xiwen
 *
 */
@Getter
@Setter
@ToString
public class LoggingPaymentOperationVO implements Serializable {

	/**
	 * @author Xu Xiwen
	 *
	 */
	private static final long serialVersionUID = 7529344237065421840L;
	
	private String id;
	// 支付操作请求的接口url
	private String url;
	// 交易号
	private String outTradeNo;
	// 交易类型
	private String tradeType;
	// 操作类型
	private String operationType;
	// 操作金额
	private String totalFee;
	// 支付途径 0 微信 1 支付宝
	private Integer payWay;
	// 微信或者支付宝服务器接口返回的状态
	private String returnStatus;
	// 微信或者支付宝服务器接口返回的消息
	private String returnMsg;
	// 客户端请求的IP
	private String requestIp;
	// 交易请求的原始报文
	private String requestBody;
	// 交易请求微信或支付宝返回的原始报文
	private String responseBody;
	private Date createdDate;
}
