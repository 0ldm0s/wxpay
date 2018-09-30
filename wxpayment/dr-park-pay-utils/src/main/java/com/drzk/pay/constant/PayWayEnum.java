/**
 * 
 */
package com.drzk.pay.constant;

/**
 * 支付方式
 * @author Xu Xiwen
 *
 */
public enum PayWayEnum {
	WX(0, "/wx/"), ALIPAY(1, "/alipay/");
	
	Integer type;
	String urlHeader;

	private PayWayEnum(Integer type, String urlHeader) {
		this.type = type;
		this.urlHeader = urlHeader;
	}
	
	public Integer getType() {
		return this.type;
	}
	
	public String getUrlHeader() {
		return this.urlHeader;
	}
}
