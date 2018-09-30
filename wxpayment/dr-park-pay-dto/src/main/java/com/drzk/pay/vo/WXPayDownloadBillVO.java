package com.drzk.pay.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 微信支付下载对账单信息VO
 * 
 * @author Xu Xiwen
 * @date 2018-07-13
 */
@Getter
@Setter
@ToString
public class WXPayDownloadBillVO extends WXPayBaseVO{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3291852891784110977L;
	
	// 下载日期 格式20180713
	private String bill_date;
	/**
	 * 下载对账单类型 ALL，返回当日所有订单信息，默认值 
	 * SUCCESS，返回当日成功支付的订单 
	 * REFUND，返回当日退款订单
	 * RECHARGE_REFUND，返回当日充值退款订单
	 */
	private String bill_type;
	// 非必传参数，固定值：GZIP，返回格式为.gzip的压缩包账单。不传则默认为数据流形式。
	private String tar_type;

}
