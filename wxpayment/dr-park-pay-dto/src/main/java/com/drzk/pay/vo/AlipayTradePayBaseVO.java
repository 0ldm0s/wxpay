/**
 * 
 */
package com.drzk.pay.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 支付宝支付共同属性提取,作为父类
 * 
 * @author Xu Xiwen
 * @date 2018-07-14
 */
@Getter
@Setter
@ToString
public class AlipayTradePayBaseVO implements Serializable {

	/**
	 * @author Xu Xiwen
	 *
	 */
	private static final long serialVersionUID = -9220039230760243469L;

	private String out_trade_no;	// 商户订单号，需要保证不重复
	private String auth_code;		// 用户付款码，25~30开头的长度为16~24位的数字，实际字符串长度以开发者获取的付款码长度为准
	private String subject;			// 订单标题
	private String store_id;		// 商户门店编号
	private String total_amount;	// 订单金额
	private String timeout_express;	// 交易超时时间
	private String timestamp;		// 时间戳,必须输入 yyyy-MM-dd HH:mm:ss
	
	private final String version = "1.0";// 支付宝版本号 固定1.0
	
	private String notify_url;		// 回调通知url地址
	
}

@Getter
@Setter
@ToString
class ExtendParams implements Serializable {
	/**
	 * @author Xu Xiwen
	 *
	 */
	private static final long serialVersionUID = -1150313262349211584L;
	private String sys_service_provider_id; // 否 64
											// 系统商编号，该参数作为系统商返佣数据提取的依据，请填写系统商签约协议的PID。注：若不属于支付宝业务经理提供签约服务的商户，暂不对外提供该功能，该参数使用无效。
											// 2088511833207846
	private String needBuyerRealnamed; 		// 否 1 是否发起实名校验T：发起F：不发起 T
	private String TRANS_MEMO; 				// 否 128 账务备注注：该字段显示在离线账单的账务备注中 促销
	private String hb_fq_num; 				// 否 5 花呗分期数（目前仅支持3、6、12）注：使用该参数需要仔细阅读“花呗分期接入文档” 3
	private String hb_fq_seller_percent; 	// 否 3
											// 卖家承担收费比例，商家承担手续费传入100，用户承担手续费传入0，仅支持传入100、0两种，其他比例暂不支持注：使用该参数需要仔细阅读“花呗分期接入文档”
}

@Getter
@Setter
@ToString
class ExtUserInfo implements Serializable{
	/**
	 * @author Xu Xiwen
	 *
	 */
	private static final long serialVersionUID = -996105259467854775L;
	
	private String name; 			// 否 16 李明 姓名 注： need_check_info=T时该参数才有效
	private String mobile;			// 否 20 16587658765 手机号 注：该参数暂不校验
	private String cert_type;  		// 否 32 IDENTITY_CARD 身份证：IDENTITY_CARD、护照：PASSPORT、军官证：OFFICER_CARD、士兵证：SOLDIER_CARD、户口本：HOKOU等。如有其它类型需要支持，请与蚂蚁金服工作人员联系。 注： need_check_info=T时该参数才有效
	private String cert_no;  		// 否 64 362334768769238881 证件号 注：need_check_info=T时该参数才有效
	private String min_age;  		// 否 3 18 允许的最小买家年龄，买家年龄必须大于等于所传数值 注： 1. need_check_info=T时该参数才有效 2. min_age为整数，必须大于等于0
	private String fix_buyer;  		// 否 8 F 是否强制校验付款人身份信息 T:强制校验，F：不强制
	private String need_check_info; // 否 1 F 是否强制校验身份信息 T:强制校验，F：不强制

}

