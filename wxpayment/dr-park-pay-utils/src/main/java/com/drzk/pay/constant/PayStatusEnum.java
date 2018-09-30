/**
 * 
 */
package com.drzk.pay.constant;

/**
 * 支付状态枚举类
 * 
 * @author Xu Xiwen
 * @date 2018-07-23
 */
public enum PayStatusEnum {

	WAITING_PAYMENT(0, "等待支付"),

	PAYMENT_SUCCESS(1, "支付成功"),

	WAITTING_REFUND(2, "等待退款"),

	REFUND_SUCCESS(3, "退款完成"),

	CLOSED(4, "支付关闭"),

	NOTPAY(5, "未支付"),

	REVOKED(6, "已撤销（刷卡支付）"), // 已撤销（刷卡支付）

	USERPAYING(7, "用户支付中"), // 用户支付中

	PAYERROR(8, "支付失败");

	private Integer statusCode;

	private String statusName;

	private PayStatusEnum(Integer statusCode, String statusName) {
		this.statusCode = statusCode;
		this.statusName = statusName;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

}
