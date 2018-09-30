package com.drzk.pay.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 微信申请退款接口
 * 
 * @author Xu Xiwen
 * @date 2018-07-13
 */
@Getter
@Setter
@ToString
public class WXPayRefundVO extends WXPayBaseVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4215968676830816315L;
	/**
	 * 退款条件二选一,都传默认使用transaction_id查询 start
	 */
	// 微信订单号 建议使用
	private String transaction_id;
	// 商户订单号
	private String out_trade_no;
	/**
	 * 退款条件二选一,都传默认使用transaction_id查询 end
	 */
	// 商户退款单号
	private String out_refund_no;
	// 订单金额 订单总金额，订单总金额，单位为分，只能为整数
	private String total_fee;
	// 退款金额 退款总金额，订单总金额，单位为分，只能为整数
	private String refund_fee;

	// 退款货币种类
	private String refund_fee_type;
	// 退款原因
	private String refund_desc;
	/**
	 * 退款资金来源 REFUND_SOURCE_RECHARGE_FUNDS:仅针对老资金流商户使用
	 * REFUND_SOURCE_UNSETTLED_FUNDS:未结算资金退款（默认使用未结算资金退款）
	 * REFUND_SOURCE_RECHARGE_FUNDS:可用余额退款
	 */
	private String refund_account;
	/**
	 * 退款结果通知url 例如:https://weixin.qq.com/notify/
	 * 异步接收微信支付退款结果通知的回调地址，通知URL必须为外网可访问的url，不允许带参数
	 * 如果参数中传了notify_url，则商户平台上配置的回调地址将不会生效。
	 */
	private String notify_url;
}
