/**
 * 
 */
package com.drzk.pay.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.drzk.pay.constant.AuthTypeConstant;
import com.drzk.pay.handler.PayConfigHandler;
import com.drzk.pay.service.IAuthService;

/**
 * @author Xu Xiwen
 *
 */
@Service
public class AuthServiceImpl implements IAuthService {

	Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

//	private Integer wxConnTimeoutMs;
	// 微信支付读取超时时间
//	private Integer wxReadTimeoutMs;
	// 支付宝支付appid
//	private String alipayAppId;
	// 支付宝支付网关
//	private String alipayServerUrl;
	// 支付宝支付私有key
//	private String alipayPrivateKey;
	// 支付宝支付公有key
//	private String alipayPublicKey;
	// 支付宝支付加密
//	private String aliSignType;
	// 支付宝支付数据格式 默认JSON
//	private String alipayDatafmt;
	// 支付宝支付请求字符集 默认GBK
//	private String alipayCharset;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.drzk.pay.service.IAuthService#getWechatOpenID(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public String wechatRedirect(String wxAppId, String redirectUri, String scope) throws Exception {
		String state = UUID.randomUUID().toString().replaceAll("-", "");
		String strUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxAppId + "&redirect_uri="
				+ redirectUri + "&response_type=code&scope=" + scope + "&state=" + state + "#wechat_redirect";
		return strUrl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.drzk.pay.service.IAuthService#getWechatOpenid(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object getWechatOpenid(String wxAppId, String wxAppSecret, String code) throws Exception {
		String strUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + wxAppId + "&secret=" + wxAppSecret
				+ "&code=" + code + "&grant_type=authorization_code";
		URL httpUrl = new URL(strUrl);
		HttpURLConnection httpURLConnection = (HttpURLConnection) httpUrl.openConnection();
		httpURLConnection.setDoOutput(true);
		httpURLConnection.setRequestMethod("GET");
		httpURLConnection.setConnectTimeout(PayConfigHandler.wxConnTimeoutMs);
		httpURLConnection.setReadTimeout(PayConfigHandler.wxReadTimeoutMs);
		httpURLConnection.connect();
		OutputStream outputStream = httpURLConnection.getOutputStream();
		if (outputStream != null) {
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// if (httpURLConnection.getResponseCode()!= 200) {
		// throw new Exception(String.format("HTTP response code is %d, not 200",
		// httpURLConnection.getResponseCode()));
		// }

		// 获取内容
		InputStream inputStream = httpURLConnection.getInputStream();
		String resp = getResult(inputStream);
		// if (httpURLConnection!=null) {
		// httpURLConnection.disconnect();
		// }
		// access_token 网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
		// expires_in access_token接口调用凭证超时时间，单位（秒）
		// refresh_token 用户刷新access_token
		// openid 用户唯一标识，请注意，在未关注公众号时，用户访问公众号的网页，也会产生一个用户和公众号唯一的OpenID
		// scope 用户授权的作用域，使用逗号（,）分隔
		Map<String, Object> respMap = JSON.parseObject(resp, Map.class);
		if (null != respMap.get("openid")) {
			Map<String, Object> openidMap = new HashMap<String, Object>();
			Set<Entry<String, Object>> entrySet = respMap.entrySet();
			entrySet.forEach(n -> {
				if (n.getKey().equals("openid")) {
					openidMap.put("openid", n.getValue());
				}
			});
			return openidMap;
		}
		return respMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.drzk.pay.service.IAuthService#getWechatUserInfo(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object getWechatUserInfo(String wxAppId, String wxAppSecret, String code) throws Exception {
		String strUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + wxAppId + "&secret=" + wxAppSecret
				+ "&code=" + code + "&grant_type=authorization_code";
		URL httpUrl = new URL(strUrl);
		HttpURLConnection httpURLConnection = (HttpURLConnection) httpUrl.openConnection();
		httpURLConnection.setDoOutput(true);
		httpURLConnection.setRequestMethod("GET");
		httpURLConnection.setConnectTimeout(PayConfigHandler.wxConnTimeoutMs);
		httpURLConnection.setReadTimeout(PayConfigHandler.wxReadTimeoutMs);
		httpURLConnection.connect();
		OutputStream outputStream = httpURLConnection.getOutputStream();
		if (outputStream != null) {
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// if (httpURLConnection.getResponseCode()!= 200) {
		// throw new Exception(String.format("HTTP response code is %d, not 200",
		// httpURLConnection.getResponseCode()));
		// }

		// 获取内容
		InputStream inputStream = httpURLConnection.getInputStream();
		String resp = getResult(inputStream);
		// if (httpURLConnection!=null) {
		// httpURLConnection.disconnect();
		// }
		// access_token 网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
		// expires_in access_token接口调用凭证超时时间，单位（秒）
		// refresh_token 用户刷新access_token
		// openid 用户唯一标识，请注意，在未关注公众号时，用户访问公众号的网页，也会产生一个用户和公众号唯一的OpenID
		// scope 用户授权的作用域，使用逗号（,）分隔
		Map<String, Object> respMap = JSON.parseObject(resp, Map.class);
		String accessToken = (String) respMap.get("access_token");
		String openid = (String) respMap.get("openid");
		if (StringUtils.isBlank(openid)) {
			return respMap;
		}

		// TODO Auto-generated method stub
		String strUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid="
				+ openid + "&lang=zh_CN";
		URL userInfoHttpUrl = new URL(strUserInfoUrl);
		httpURLConnection = (HttpURLConnection) userInfoHttpUrl.openConnection();
		httpURLConnection.setDoOutput(true);
		httpURLConnection.setRequestMethod("GET");
		httpURLConnection.setConnectTimeout(PayConfigHandler.wxConnTimeoutMs);
		httpURLConnection.setReadTimeout(PayConfigHandler.wxReadTimeoutMs);
		httpURLConnection.connect();
		// if (httpURLConnection.getResponseCode()!= 200) {
		// throw new Exception(String.format("HTTP response code is %d, not 200",
		// httpURLConnection.getResponseCode()));
		// }

		// 获取内容
		InputStream userInfoInputStream = httpURLConnection.getInputStream();
		String userInfo = getResult(userInfoInputStream);
		Map<String, Object> userInfoMap = JSON.parseObject(userInfo, Map.class);
		return userInfoMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.drzk.pay.service.IAuthService#getAlipayUserID()
	 */
	@Override
	public String alipayRedirect(String alipayAppId, String redirectUri, String scope) throws Exception {
		long state = System.currentTimeMillis();
		String strUri = "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=" + alipayAppId + "&scope="
				+ AuthTypeConstant.AUTH_BASE + "&redirect_uri=" + redirectUri + "&state=" + state;
		return strUri;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.drzk.pay.service.IAuthService#getAlipayUserInfo()
	 */
	@Override
	public AlipaySystemOauthTokenResponse getAlipayUserInfo(String alipayAppId,String alipayPrivateKey, String alipayPublicKey, String authCode) throws Exception {
		AlipayClient alipayClient = new DefaultAlipayClient(PayConfigHandler.alipayServerUrl, alipayAppId, alipayPrivateKey, "json",
				PayConfigHandler.alipayCharset, alipayPublicKey, "RSA2");
		AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
		request.setCode("2e4248c2f50b4653bf18ecee3466UC18");
		request.setGrantType("authorization_code");
		AlipaySystemOauthTokenResponse oauthTokenResponse = null;
		try {
			oauthTokenResponse = alipayClient.execute(request);
			System.out.println(JSON.toJSONString(oauthTokenResponse));
		} catch (AlipayApiException e) {
			// 处理异常
			e.printStackTrace();
		}
		return oauthTokenResponse;
	}

	private String getResult(InputStream inputStream) throws Exception {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
		final StringBuffer stringBuffer = new StringBuffer();
		String line = null;
		while ((line = bufferedReader.readLine()) != null) {
			stringBuffer.append(line);
		}
		String resp = stringBuffer.toString();
		if (stringBuffer != null) {
			try {
				bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resp;
	}

}
