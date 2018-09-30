/**
 * 
 */
package com.drzk.pay.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConfig;
import com.github.wxpay.sdk.WXPayConstants.SignType;

/**
 * @author Xu Xiwen
 *
 */
public class PayConfigHandler {

	public final static String alipayServerUrl = "https://openapi.alipaydev.com/gateway.do";
	public final static String alipayDatafmt = "json";
	public final static String alipayCharset = "UTF-8";
	public final static String aliSignType = "RSA";

	public final static AlipayClient alipayClientConfigFactory(final Map<String, Object> reqData)
			throws RuntimeException {
		String alipayAppId = (String) reqData.get("app_id");
		String alipayPrivateKey = (String) reqData.get("private_key");
		String alipayPublicKey = (String) reqData.get("public_key");

		return new DefaultAlipayClient(alipayServerUrl, alipayAppId, alipayPrivateKey, alipayDatafmt, alipayCharset,
				alipayPublicKey, aliSignType);
	}

	public final static int wxConnTimeoutMs = 30000;
	public final static int wxReadTimeoutMs = 30000;
	// private final static String certPath = "";

	public final static WXPay wxPayFactory(final Map<String, Object> reqData) throws RuntimeException {
		String wxAppId = (String) reqData.get("appid");
		String wxMcrId = (String) reqData.get("mch_id");
		String wxAppKey = (String) reqData.get("appKey");
		String signType = "md5";

		WXPayConfigInit wxPayConfig = new WXPayConfigInit(wxAppId, wxMcrId, wxAppKey, wxConnTimeoutMs, wxReadTimeoutMs,
				signType);
		reqData.remove("appKey");

		WXPay wxPay = null;
		// 微信支付沙箱环境属性处理, useSandBoxStatus : true 接入测试环境 ? false 接入正式环境
		boolean useSandBoxStatus = false;
		// 微信支付签名算法配置, 只支持MD5和HMACSHA256两种算法, 默认是MD5
		if (StringUtils.isNotBlank(signType)) {
			switch (signType.trim().toLowerCase()) {
			case "md5":
				wxPay = new WXPay(wxPayConfig, SignType.MD5, useSandBoxStatus);
				break;
			case "hmacsha256":
				wxPay = new WXPay(wxPayConfig, SignType.HMACSHA256, useSandBoxStatus);
				break;
			default:
				wxPay = new WXPay(wxPayConfig, SignType.MD5, useSandBoxStatus);
				break;
			}
		} else {
			wxPay = new WXPay(wxPayConfig, SignType.MD5, useSandBoxStatus);
		}
		return wxPay;
	}

	/**
	 * 微信支付属性初始化
	 * 
	 * @author Xu Xiwen
	 *
	 */
	private static class WXPayConfigInit implements WXPayConfig {

		private String wxAppId;
		private String wxMcrId;
		private String wxKey;
		private Integer wxConnTimeoutMs;
		private Integer wxReadTimeoutMs;
		private String wxCert;
		private InputStream in;

		@Override
		public String getAppID() {
			return wxAppId;
		}

		@Override
		public String getMchID() {
			return wxMcrId;
		}

		@Override
		public String getKey() {
			return wxKey;
		}

		@SuppressWarnings("unused")
		public String getWxCert() {
			return wxCert;
		}

		public void setCertStream(String wxCert) {
			try {
				this.in = new FileInputStream(new File(wxCert));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		@Override
		public InputStream getCertStream() {
			return this.in;
		}

		@Override
		public int getHttpConnectTimeoutMs() {
			return wxConnTimeoutMs;
		}

		@Override
		public int getHttpReadTimeoutMs() {
			return wxReadTimeoutMs;
		}

		public WXPayConfigInit(String wxAppId, String wxMcrId, String wxKey, Integer wxConnTimeoutMs,
				Integer wxReadTimeoutMs, String wxCert) {
			super();
			this.wxAppId = wxAppId;
			this.wxMcrId = wxMcrId;
			this.wxKey = wxKey;
			this.wxConnTimeoutMs = wxConnTimeoutMs;
			this.wxReadTimeoutMs = wxReadTimeoutMs;
			this.wxCert = wxCert;
			if (StringUtils.isNotBlank(wxCert))
				this.setCertStream(wxCert);
		}

	}
}
