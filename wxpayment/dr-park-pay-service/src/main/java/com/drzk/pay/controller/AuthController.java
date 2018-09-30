/**
 * 
 */
package com.drzk.pay.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.drzk.pay.constant.AuthTypeConstant;
import com.drzk.pay.constant.PayConstants;
import com.drzk.pay.service.IAuthService;

/**
 * @author Xu Xiwen
 *
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

	@Resource
	IAuthService iAuthService;

	@Resource
	MongoTemplate mongoTemplate;

	@SuppressWarnings("rawtypes")
	@Resource
	RedisTemplate redisTemplate;

	@SuppressWarnings({ "unchecked" })
	@GetMapping("/getauthcode/{menu}/{scope}")
	public void getAuthCode(@PathVariable("menu") String menu, @PathVariable("scope") String scope,
			HttpServletRequest req, HttpServletResponse resp) throws Exception {
		String parkId = req.getParameter("parkId");
		String dsn = req.getParameter("dsn");
		StringBuffer params = new StringBuffer("?");
		if (StringUtils.isNotBlank(parkId))
			if (params.toString().indexOf("=") >= 0)
				params.append("%26parkId=" + parkId);
			else
				params.append("parkId=" + parkId);
		if (StringUtils.isNotBlank(dsn))
			if (params.toString().indexOf("=") >= 0)
				params.append("%26dsn=" + dsn);
			else
				params.append("dsn=" + dsn);

		String redirectUri = "";
		if (PayConstants.MONTHLY_CAR_PAY_MENU.equals(menu)) {
			if (params.length() > 1)
				redirectUri = "http://payment-dev.drzk.cn/page/monthRecharge.html" + params.toString();
			else
				redirectUri = "http://payment-dev.drzk.cn/page/monthRecharge.html";
		} else if (PayConstants.TEMP_CAR_PAY_MENU.equals(menu)) {
			if (params.length() > 1)
				redirectUri = "http://payment-dev.drzk.cn/page/tempPark.html" + params.toString();
			else
				redirectUri = "http://payment-dev.drzk.cn/page/tempPark.html";
		} else if (PayConstants.SCAN_CODE_CAR_PAY_MENU.equals(menu)) {
			if (params.length() > 1)
				redirectUri = "http://payment-dev.drzk.cn/page/scanCode.html" + params.toString();
			else
				redirectUri = "http://payment-dev.drzk.cn/page/scanCode.html";
		}

		String clientType = req.getHeader("user-agent");
		Query query = null;
		Map<String, Object> parkingPayment = null;
		if (clientType.contains("MicroMessenger")) {
			parkingPayment = (Map<String, Object>) redisTemplate.boundValueOps("wx_auth_parking_payment_" + parkId)
					.get();
			if (null == parkingPayment) {
				query = new Query().addCriteria(new Criteria("projectNo").is(parkId).and("payType").is(1));
				parkingPayment = mongoTemplate.findOne(query, Map.class, "parking_payment");
				if (null != parkingPayment) {
					redisTemplate.boundValueOps("wx_auth_parking_payment_" + parkId).set(parkingPayment);
				}
			}
		} else {
			parkingPayment = (Map<String, Object>) redisTemplate.boundValueOps("alipay_auth_parking_payment_" + parkId)
					.get();
//			if (null == parkingPayment) {
				query = new Query().addCriteria(new Criteria("projectNo").is(parkId).and("payType").is(2));
				parkingPayment = mongoTemplate.findOne(query, Map.class, "parking_payment");
				if (null != parkingPayment) {
					redisTemplate.boundValueOps("alipay_auth_parking_payment_" + parkId).set(parkingPayment);
				}
//			}
		}

		String location = null;
		if (clientType.contains("MicroMessenger")) {
			String wxAppId = (String) parkingPayment.get("appId");

			if (scope.equals(AuthTypeConstant.OPEN_ID)) {
				location = iAuthService.wechatRedirect(wxAppId, redirectUri, AuthTypeConstant.SNSAPI_BASE);
			} else if (scope.equals(AuthTypeConstant.USER_INFO)) {
				location = iAuthService.wechatRedirect(wxAppId, redirectUri, AuthTypeConstant.SNSAPI_USERINFO);
			}
			resp.encodeRedirectURL("UTF-8");
			resp.sendRedirect(location);
		} else
			//if (clientType.contains("AlipayClient")) 
			{
			String alipayAppId = (String) parkingPayment.get("appId");
			if (scope.equals(AuthTypeConstant.OPEN_ID)) {
				location = iAuthService.alipayRedirect(alipayAppId, redirectUri, AuthTypeConstant.AUTH_BASE);
			} else if (scope.equals(AuthTypeConstant.USER_INFO)) {
				location = iAuthService.alipayRedirect(alipayAppId, redirectUri, AuthTypeConstant.AUTH_USER);
			}
			resp.encodeRedirectURL("UTF-8");
			resp.sendRedirect(location);
		}

	}

	@SuppressWarnings("unchecked")
	@GetMapping("/getwechatopenid/{parkId}/{authCode}")
	public Object getWechatOpenid(@PathVariable("parkId") String parkId, @PathVariable("authCode") String authCode)
			throws Exception {
		Map<String, Object> parkingPayment = (Map<String, Object>) redisTemplate
				.boundValueOps("wx_auth_parking_payment_" + parkId).get();
		String wxAppId = (String) parkingPayment.get("appId");
		String wxAppSecret = (String) parkingPayment.get("appSecret");
		return iAuthService.getWechatOpenid(wxAppId, wxAppSecret, authCode);
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/getwechatuserinfo/{parkId}/{authCode}")
	public Object getWechatUserInfo(@PathVariable("parkId") String parkId, @PathVariable("authCode") String authCode)
			throws Exception {
		Map<String, Object> parkingPayment = (Map<String, Object>) redisTemplate
				.boundValueOps("wx_auth_parking_payment_" + parkId).get();
		String wxAppId = (String) parkingPayment.get("appId");
		String wxAppSecret = (String) parkingPayment.get("appSecret");
		return iAuthService.getWechatUserInfo(wxAppId, wxAppSecret, authCode);
	}

	@SuppressWarnings("unchecked")
	@ResponseBody
	@GetMapping("/getalipayuserid/{parkId}/{authCode}")
	public String getAlipayUserId(@PathVariable("parkId") String parkId, @PathVariable("authCode") String authCode)
			throws Exception {
		Map<String, Object> parkingPayment = (Map<String, Object>) redisTemplate
				.boundValueOps("alipay_auth_parking_payment_" + parkId).get();
		String alipayAppId = (String) parkingPayment.get("appId");
		String privateKey = (String) parkingPayment.get("privateKey");
		String publicKey = (String) parkingPayment.get("publicKey");
		AlipaySystemOauthTokenResponse response = iAuthService.getAlipayUserInfo(alipayAppId, privateKey, publicKey,
				authCode);
		return response.getUserId();
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/getalipayuserinfo/{parkId}/{authCode}")
	public Object getAlipayUserInfo(@PathVariable("parkId") String parkId, @PathVariable("authCode") String authCode)
			throws Exception {
		Map<String, Object> parkingPayment = (Map<String, Object>) redisTemplate
				.boundValueOps("alipay_auth_parking_payment_" + parkId).get();
		String alipayAppId = (String) parkingPayment.get("appId");
		String privateKey = (String) parkingPayment.get("privateKey");
		String publicKey = (String) parkingPayment.get("publicKey");
		return iAuthService.getAlipayUserInfo(alipayAppId, privateKey, publicKey, authCode);
	}
}
