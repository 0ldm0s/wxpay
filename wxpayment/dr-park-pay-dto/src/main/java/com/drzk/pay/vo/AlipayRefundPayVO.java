/**
 * 
 */
package com.drzk.pay.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 支付宝支付交易退款VO
 * 
 * @author Xu Xiwen
 *
 */
@Getter
@Setter
@ToString
public class AlipayRefundPayVO extends AlipayQueryOrCancelPayVO {

	/**
	 * @author Xu Xiwen
	 *
	 */
	private static final long serialVersionUID = 5284004637271213862L;

	private String out_request_no; // 本次退款请求流水号，部分退款时必传
	private String refund_amount; // 本次退款金额

}
