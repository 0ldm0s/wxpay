/**
 * 
 */
package com.drzk.pay.vo;

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
public final class AlipayWapTradePayVO extends AlipayTradePayBaseVO {

	/**
	 * @author Xu Xiwen
	 *
	 */
	private static final long serialVersionUID = -6101400450926642744L;
	private String time_expire; 	// 否 32 绝对超时时间，格式为yyyy-MM-dd HH:mm。
									// 注：1）以支付宝系统时间为准；2）如果和timeout_express参数同时传入，以time_expire为准。 2016-12-31 10:05
	private String auth_token; 		// 否 40 针对用户授权接口，获取用户相关数据时，用于标识用户授权关系注：若不属于支付宝业务经理提供签约服务的商户，暂不对外提供该功能，该参数使用无效。
									// appopenBb64d181d0146481ab6a762c00714cC27
	private String product_code; 	// 是 64 销售产品码，商家和支付宝签约的产品码。该产品请填写固定值：QUICK_WAP_WAY QUICK_WAP_WAY
	private String goods_type; 		// 否 2 商品主类型：0—虚拟类商品，1—实物类商品注：虚拟类商品不支持使用花呗渠道 0
	private String passback_params; // 否 512
									// 公用回传参数，如果请求时传递了该参数，则返回给商户时会回传该参数。支付宝会在异步通知时将该参数原样返回。本参数必须进行UrlEncode之后才可以发送给支付宝
									// merchantBizType%3d3C%26merchantBizNo%3d2016010101111
	private String promo_params; 	// 否 512 优惠参数注：仅与支付宝协商后可用 {“storeIdType”:“1”}
	private ExtendParams extend_params;	// 否 业务扩展参数，详见下面的“业务扩展参数说明” {“sys_service_provider_id”:“2088511833207846”}
	private String enable_pay_channels; // 否 128 可用渠道，用户只能在指定渠道范围内支付当有多个渠道时用“,”分隔注：与disable_pay_channels互斥
										// pcredit,moneyFund,debitCardExpress
	private String disable_pay_channels; // 否 128 禁用渠道，用户不可用指定渠道支付当有多个渠道时用“,”分隔注：与enable_pay_channels互斥
									// pcredit,moneyFund,debitCardExpress
	private String store_id; 		// 否 32 商户门店编号。该参数用于请求参数中以区分各门店，非必传项。 NJ_001
	private String quit_url; 		// 400
									// 添加该参数后在h5支付收银台会出现返回按钮，可用于用户付款中途退出并返回到该参数指定的商户网站地址。注：该参数对支付宝钱包标准收银台下的跳转不生效。
									// http://www.taobao.com/product/113714.html
	private ExtUserInfo ext_user_info; // 否		外部指定买家

}

