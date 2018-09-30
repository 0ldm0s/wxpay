package com.drzk.pay.aop;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.drzk.pay.constant.PayConstants;
import com.drzk.pay.constant.PayWayEnum;
import com.drzk.pay.mapper.ILoggingPaymentOperationDAO;
import com.drzk.pay.utils.OperationTypeDictUtil;
import com.drzk.pay.utils.OutTradeNOUtil;
import com.drzk.pay.utils.WXPayReqUtil;
import com.drzk.pay.vo.LoggingPaymentOperationVO;
import com.drzk.pay.vo.ResponseHandlerVO;
import com.github.wxpay.sdk.WXPayConstants.SignType;
import com.github.wxpay.sdk.WXPayUtil;

@Aspect
@Component
public class PayAopManager {

	Logger logger = LoggerFactory.getLogger(PayAopManager.class);

	private final static WXPayUtil wxPayUtil = new WXPayUtil();

	@Value("${wx.pay.expire.time}")
	volatile int expireTime;

	@Value("${wx.pay.signtype}")
	volatile String signType;

	@Autowired
	ILoggingPaymentOperationDAO iLoggingPaymentOperationDAO;

	@Pointcut("execution(* com.drzk.pay.service.impl.WXPayServiceImpl.*(..))")
	public void wxPayPointcut() {
	}

	@SuppressWarnings({ "unchecked", "static-access" })
	@Around(value = "wxPayPointcut() && args(java.util.Map)")
	public Object processedWXPayReqData(final ProceedingJoinPoint pjp) throws Throwable {
		logger.info("Wechat pay proceed request data start");

		Object[] args = pjp.getArgs();

		for (int i = 0; i < args.length; i++) {
			Object object = args[i];
			if (object instanceof Map) {

				Map<String, Object> map = (Map<String, Object>) object;
				map.put("out_trade_no", OutTradeNOUtil.getOutTradeNO(PayWayEnum.WX)); // 微信支付生成外部订单号
				map.put("nonce_str", wxPayUtil.generateNonceStr()); // 微信支付生成随机字符串
				String wxAppKey = (String) map.get("appKey"); // 微信支付生成签名的APPKEY
				WXPayReqUtil.orderExpireTime(map, expireTime);

				Map<String, String> signatureMap = new HashMap<String, String>();

				Set<Entry<String, Object>> entrys = map.entrySet();
				entrys.forEach(entry -> {
					if (!(entry.getValue() instanceof String)) {
						signatureMap.put(entry.getKey(), JSON.toJSONString(entry.getValue()));
					} else {
						signatureMap.put(entry.getKey(), (String) entry.getValue());
					}
				});

				map.remove("appKey");// 先删除appKey
				String signature = null;// 微信支付生成签名
				if (StringUtils.isBlank(signType)) {
					signature = wxPayUtil.generateSignature(signatureMap, wxAppKey, SignType.MD5);
				} else {
					if (signType.trim().toLowerCase().equals("md5"))
						signature = wxPayUtil.generateSignature(signatureMap, wxAppKey, SignType.MD5);
					else
						signature = wxPayUtil.generateSignature(signatureMap, wxAppKey, SignType.HMACSHA256);
				}
				map.put("sign", signature);
				map.put("appKey", wxAppKey);// 先删除appKey
				logger.info("Wechat pay processing data " + map);
			}
		}

		Object proceedObj = pjp.proceed(args);
		logger.info("Wechat pay proceed request data end");
		return proceedObj;
	}

	@Pointcut("execution(* com.drzk.pay.service.impl.AlipayServiceImpl.*(..))")
	public void alipayPointcut() {
	}

	@SuppressWarnings({ "unchecked" })
	@Around(value = "alipayPointcut() && args(java.util.Map)")
	public Object processedAlipayReqData(final ProceedingJoinPoint pjp) throws Throwable {
		logger.info("Alipay proceed request data start");
		Object[] args = pjp.getArgs();
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		for (int i = 0; i < args.length; i++) {
			Object object = args[i];
			if (object instanceof Map) {

				Map<String, Object> map = (Map<String, Object>) object;
				map.put("version", "1.0");
				((Map<String, Object>) map.get("biz_content")).put("out_trade_no",
						OutTradeNOUtil.getOutTradeNO(PayWayEnum.ALIPAY)); // 支付宝支付生成外部订单号
				((Map<String, Object>) map.get("biz_content")).put("timestamp", sdf.format(new Date())); // 支付宝支付生成外部订单号
				((Map<String, Object>) map.get("biz_content")).put("format", "JSON");
				((Map<String, Object>) map.get("biz_content")).put("charset", "utf-8");
				((Map<String, Object>) map.get("biz_content")).put("sign_type", "RSA");
				logger.info("Alipay pay processing data " + map);
			}
		}

		Object proceedObj = pjp.proceed(args);
		logger.info("Alipay proceed request data end");
		return proceedObj;
	}

	@Pointcut("execution(* com.drzk.pay.service.impl.*.*(..))")
	public void loggingPaymentPointcut() {
	}

	@SuppressWarnings("unchecked")
	@AfterReturning(returning = "returnValue", pointcut = "loggingPaymentPointcut()")
	public void loggingPayment(JoinPoint point, Object returnValue) {
		Object[] objArgs = point.getArgs();
		if (!(returnValue instanceof Map))
			return;
		if (!(objArgs[0] instanceof Map))
			return;
		logger.info("Join point loggingPayment start");

		ResponseHandlerVO responseHandler = new ResponseHandlerVO();

		Map<String, String> map = (Map<String, String>) objArgs[0];
		String outTradeNo = map.get("out_trade_no");
		String requestBody = JSON.toJSONString(map);
		String responseBody = JSON.toJSONString(responseHandler.getResultBody());

		Signature sig = point.getSignature();
		String methodName = sig.getName();

		LoggingPaymentOperationVO logVO = new LoggingPaymentOperationVO();
		logVO.setOutTradeNo(outTradeNo);
		logVO.setTradeType(methodName);
		logVO.setReturnStatus(responseHandler.getCode());
		logVO.setReturnMsg(responseHandler.getMessage());
		logVO.setTotalFee(map.get("total_fee"));
		logVO.setRequestIp(map.get("request_ip"));
		logVO.setOperationType(OperationTypeDictUtil.getOperationTypeValue(methodName));
		logVO.setRequestBody(requestBody);
		logVO.setResponseBody(responseBody);

		// 处理支付渠道记录
		if (StringUtils.isNotBlank(outTradeNo)) {
			if (outTradeNo.indexOf(PayConstants.WXPAY_OUT_TRADE_NO_HEADER) >= 0) {
				logVO.setPayWay(PayWayEnum.WX.getType());
				logVO.setUrl(PayWayEnum.WX.getUrlHeader() + methodName);
			} else if (outTradeNo.indexOf(PayConstants.ALIPAY_OUT_TRADE_NO_HEADER) >= 0) {
				logVO.setPayWay(PayWayEnum.ALIPAY.getType());
				logVO.setUrl(PayWayEnum.ALIPAY.getUrlHeader() + methodName);
			}
		} else {
			if (methodName.equals("downloadBill")) {
				logVO.setPayWay(PayWayEnum.WX.getType());
				logVO.setUrl(PayWayEnum.WX.getUrlHeader() + methodName);
			} else if (methodName.equals("billDownload")) {
				logVO.setPayWay(PayWayEnum.ALIPAY.getType());
				logVO.setUrl(PayWayEnum.ALIPAY.getUrlHeader() + methodName);
			}
		}

		iLoggingPaymentOperationDAO.save(logVO);
		logger.info("Join point loggingPayment end");
	}
}
