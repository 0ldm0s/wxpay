/**
 * 
 */
package com.drzk.pay.service;

import com.alipay.api.response.AlipaySystemOauthTokenResponse;

/**
 * @author Xu Xiwen
 *
 */
public interface IAuthService {

	public String wechatRedirect(String wxAppId, String redirectUri, String scope) throws Exception;

	public Object getWechatOpenid(String wxAppId, String wxAppSecret, String code) throws Exception;

	public Object getWechatUserInfo(String wxAppId, String wxAppSecret, String code) throws Exception;

	/**
	 * @param alipayAppId
	 * @param alipayPrivateKey
	 * @param alipayPublicKey
	 * @param authCode
	 * @return
	 * @throws Exception
	 */
	AlipaySystemOauthTokenResponse getAlipayUserInfo(String alipayAppId, String alipayPrivateKey,
			String alipayPublicKey, String authCode) throws Exception;

	/**
	 * @param alipayAppId
	 * @param redirectUri
	 * @param scope
	 * @return
	 * @throws Exception
	 */
	String alipayRedirect(String alipayAppId, String redirectUri, String scope) throws Exception;

}
