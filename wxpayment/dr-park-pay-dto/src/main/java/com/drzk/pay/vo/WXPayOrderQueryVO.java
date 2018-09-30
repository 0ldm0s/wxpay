package com.drzk.pay.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 查询微信订单信息 
 * 
 * @author Xu Xiwen
 * @date 2018-07-13
 */
@Getter
@Setter
@ToString
public class WXPayOrderQueryVO extends WXPayBaseVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4949249008328531950L;
	/**
	 * 查询条件二选一,都传默认使用transaction_id查询
	 */
	// 微信订单号 建议使用 
	private String transaction_id;
	// 商户订单号
	private String out_trade_no;
}
