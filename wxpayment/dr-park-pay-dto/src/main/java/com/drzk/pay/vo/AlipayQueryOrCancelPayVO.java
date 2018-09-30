/**
 * 
 */
package com.drzk.pay.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 支付宝支付订单状态信息查询
 * @author Xu Xiwen
 *
 */
@Getter
@Setter
@ToString
public class AlipayQueryOrCancelPayVO implements Serializable {

	/**
	 * @author Xu Xiwen
	 *
	 */
	private static final long serialVersionUID = 6093250168077395144L;

	// 支付宝支付外部订单号
	private String out_trade_no;
	// 支付宝支付内部订单号
	private String trade_no;

}
