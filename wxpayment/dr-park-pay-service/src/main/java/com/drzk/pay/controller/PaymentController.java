/**
 * 
 */
package com.drzk.pay.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.drzk.pay.service.IAlipayService;
import com.drzk.pay.service.IWXPayService;
import com.drzk.pay.utils.ResponseHandlerUtil;
import com.drzk.pay.vo.ResponseHandlerVO;

/**
 * 统一支付接口入口
 * 
 * @author Xu Xiwen
 * @date 2018-07-24
 */
@RestController
@RequestMapping("/payment")
public class PaymentController {

	Logger logger = LoggerFactory.getLogger(PaymentController.class);

	@Autowired
	IWXPayService iWXPayService;

	@Autowired
	IAlipayService iAlipayService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/{payType}/{payMethod}")
	public ResponseHandlerVO execPayMethod(HttpServletRequest req, @PathVariable("payType") String payType,
			@PathVariable("payMethod") String payMethod, @RequestBody Map<String, String> reqData) {
		ResponseHandlerVO responseHandler = new ResponseHandlerVO();

		Class clz;
		try {

			if (payType.equalsIgnoreCase("wx")) {
				clz = Class.forName("com.drzk.pay.service.impl.WXPayServiceImpl");
				responseHandler = (ResponseHandlerVO) clz.getMethod(payMethod, Map.class).invoke(iWXPayService,
						reqData);
			} else if (payType.equalsIgnoreCase("alipay")) {
				clz = Class.forName("com.drzk.pay.service.impl.AlipayServiceImpl");
				responseHandler = (ResponseHandlerVO) clz.getMethod(payMethod, Map.class).invoke(iAlipayService,
						reqData);
			}

		} catch (ClassNotFoundException e) {
			logger.error("支付异常出错:" + e.getMessage());
			e.printStackTrace();
			return ResponseHandlerUtil.loadMessageResponse(e.getMessage());
		} catch (IllegalAccessException e) {
			logger.error("支付异常出错:" + e.getMessage());
			e.printStackTrace();
			return ResponseHandlerUtil.loadMessageResponse(e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("支付异常出错:" + e.getMessage());
			e.printStackTrace();
			return ResponseHandlerUtil.loadMessageResponse(e.getMessage());
		} catch (InvocationTargetException e) {
			logger.error("支付异常出错:" + e.getMessage());
			e.printStackTrace();
			return ResponseHandlerUtil.loadMessageResponse(e.getMessage());
		} catch (NoSuchMethodException e) {
			logger.error("支付异常出错:" + e.getMessage());
			e.printStackTrace();
			return ResponseHandlerUtil.loadMessageResponse(e.getMessage());
		} catch (SecurityException e) {
			logger.error("支付异常出错:" + e.getMessage());
			e.printStackTrace();
			return ResponseHandlerUtil.loadMessageResponse(e.getMessage());
		}

		return responseHandler;
	}

}
