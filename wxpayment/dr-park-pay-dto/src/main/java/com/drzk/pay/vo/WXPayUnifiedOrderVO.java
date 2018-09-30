package com.drzk.pay.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 微信支付统一下单接口VO
 * 
 * @author Xu Xiwen
 * @date 2018-07-13
 */
@Getter
@Setter
@ToString
public class WXPayUnifiedOrderVO extends WXPayBaseVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3495702169861777616L;
	/**
	 * 微信支付统一订单必填字段 start
	 */
	// 微信支付订单通知回调地址 例如:https://192.168.1.1/pay/callback.do
	private String notify_url;
	// 商品描述 例如:腾讯充值中心-QQ会员充值
	private String body;
	// 商户订单号 例如:WX_20150806125346
	private String out_trade_no;
	// 标价金额 例如:8888,以分为单位
	private String total_fee;
	// 终端IP 例如:192.168.1.1
	private String spbill_create_ip;
	// 交易类型 例如:JSAPI 公众号支付 / NATIVE 扫码支付 / APP APP支付
	private String trade_type;
	/**
	 * 微信支付统一订单必填字段 end
	 */
	// 设备号
	private String device_info;
	// 商品描述
	private String detail;
	// 商品详情
	private String attach;
	// 标价币种 例如:CNY,USD
	private String fee_type;
	// 交易发起时间 例如:20091225091010 订单生成时间，格式为yyyyMMddHHmmss
	private String time_start;
	// 交易结束时间
	private String time_expire;
	// 订单优惠标记
	private String goods_tag;
	// 商品ID
	private String product_id;
	// 指定支付方式 no_credit属性可以使用户不能使用信用卡支付
	private String limit_pay;
	// 微信公共号支付时,获取用户的openid
	private String openid;
	// 门店信息
	private String scene_info;
}

/**
 * 门店信息VO
 * 
 * @see com.drzk.pay.vo.WXPayUnifiedOrderVO#scene_info
 * @author Xu Xiwen
 *
 */
@Getter
@Setter
@ToString
class SceneInfoVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3016609628573085318L;
	// 门店id
	private String id;
	// 门店名称
	private String name;
	// 门店地址代码 例如:518000
	private String area_code;
	// 门店地址 例如:科技园中一路腾讯大厦
	private String address;
}