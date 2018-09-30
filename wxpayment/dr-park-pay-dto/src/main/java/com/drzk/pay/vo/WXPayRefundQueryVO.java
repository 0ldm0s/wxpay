package com.drzk.pay.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 查询微信订单退款信息 
 * 
 * @author Xu Xiwen
 * @date 2018-07-13
 */
@Getter
@Setter
@ToString
public class WXPayRefundQueryVO extends WXPayBaseVO{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3401936619062346854L;
	
	/**
	 * 退款条件四选一,都传默认使用transaction_id查询 start
	 */
	// 微信订单号 建议使用
	private String transaction_id;
	// 商户订单号
	private String out_trade_no;
	// 商户退款单号
	private String out_refund_no;
	// 微信退款单号
	private String refund_id;
	/**
	 * 退款条件四选一,都传默认使用transaction_id查询 end
	 */
	// 偏移量,可选,相当于获取多少次以后的数据
	private Integer offset;
}
