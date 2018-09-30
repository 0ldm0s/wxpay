package com.drzk.pay.constant;

public class PayConstants {

	public static enum WXPayResultCode {
		SUCCESS("SUCCESS", true), FAIL("FAIL", false);

		String value;
		boolean flag;

		WXPayResultCode(String value, boolean flag) {
			this.value = value;
			this.flag = flag;
		}

		public String getValue() {
			return this.value;
		}

		public boolean getFlag() {
			return this.flag;
		}
	}

	public static final String ALIPAY_SUCCESS = "10000"; // 成功
	public static final String ALIPAY_PAYING = "10003"; // 用户支付中
	public static final String ALIPAY_FAILED = "40004"; // 失败
	public static final String ALIPAY_ERROR = "20000"; // 系统异常
	
	public static final String WX_PAY_SUCCESS = "SUCCESS"; // 支付成功
	public static final String WX_PAY_REFUND = "REFUND";// 转入退款
	public static final String WX_PAY_NOTPAY = "NOTPAY";// 未支付
	public static final String WX_PAY_CLOSED = "CLOSED";// 已关闭
	public static final String WX_PAY_REVOKED = "REVOKED";// 已撤销（刷卡支付）
	public static final String WX_PAY_USERPAYING = "USERPAYING";//用户支付中
	public static final String WX_PAY_PAYERROR = "PAYERROR";//支付失败(其他原因，如银行返回失败)
	
	public static final String WXPAY_OUT_TRADE_NO_HEADER = "WX-PAY-"; // 微信支付订单号头部信息
	public static final String ALIPAY_OUT_TRADE_NO_HEADER = "ALIPAY-"; // 支付宝支付订单号头部信息
	
	public static final String ORDER_COLLECTION = "park_payorder";
	public static final String ORDER_REFUND_COLLECTION = "park_payorder_refund";
	
	public static final String MQTT_WECHAT_UNIFIED_ORDER = "getPayment/wechat";
	public static final String MQTT_ALIPAY_UNIFIED_ORDER = "getPayment/alipay";
	
	public static final String TEMP_CAR_PAY_MENU = "temp";
	public static final String MONTHLY_CAR_PAY_MENU = "monthly";
	public static final String SCAN_CODE_CAR_PAY_MENU = "scancode";
	
}
