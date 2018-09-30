package com.drzk.pay.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.drzk.pay.constant.PayConstants;
import com.drzk.pay.constant.PayStatusEnum;
import com.drzk.pay.constant.PayWayEnum;
import com.drzk.pay.handler.PayConfigHandler;
import com.drzk.pay.service.IWXPayService;
import com.drzk.pay.utils.DateFormatUtil;
import com.drzk.pay.utils.OutTradeNOUtil;
import com.drzk.pay.utils.RequestDataUtils;
import com.drzk.pay.utils.ResponseHandlerUtil;
import com.drzk.pay.vo.ResponseHandlerVO;
import com.drzk.pay.vo.StatusVO;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;

/**
 * 微信支付实现类
 * 
 * @author Xu Xiwen
 * @date 2018年7月12日
 *
 */
@Service
public class WXPayServiceImpl implements IWXPayService {

	Logger logger = LoggerFactory.getLogger(WXPayServiceImpl.class);

	@Autowired
	MongoTemplate mongoTemplate;

	/**
	 * 作用：统一下单 场景：公共号支付、扫码支付、APP支付
	 * 
	 * @param reqData
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	@Override
	public ResponseHandlerVO unifiedOrder(final Map<String, Object> reqData) throws Exception {
		logger.info("Request data :" + reqData);
		final WXPay wxPay = PayConfigHandler.wxPayFactory(reqData);
		Map<String, String> respData = wxPay.unifiedOrder(RequestDataUtils.transformRequestMap(reqData));
		System.out.println("unifiedOrder data : " + JSON.toJSONString(reqData));
		String resultCode = respData.get("result_code") == null ? "FAIL" : respData.get("result_code");
		String returnCode = respData.get("return_code") == null ? "FAIL" : respData.get("return_code");

		Map<String, String> payOrderData = new HashMap<String, String>();
		// 根据返回报文判断提交微信订单是否成功, 成功则创建订单信息, 否则只通过切面记录操作日志
		if (returnCode.equals(PayConstants.WXPayResultCode.SUCCESS.getValue())
				&& resultCode.equals(PayConstants.WXPayResultCode.SUCCESS.getValue())) {
			StatusVO status = new StatusVO(PayStatusEnum.WAITING_PAYMENT.getStatusCode(),
					PayStatusEnum.WAITING_PAYMENT.getStatusName(), DateFormatUtil.getDateTime());
			reqData.put("status", status);
			reqData.put("prepay_id", respData.get("prepay_id"));
			reqData.put("timeStamp", new Date().getTime());

			payOrderData.put("appId", respData.get("appid"));
			payOrderData.put("timeStamp", new BigDecimal(new Date().getTime() / 1000).intValue() + "");
			payOrderData.put("nonceStr", WXPayUtil.generateNonceStr());
			payOrderData.put("package", "prepay_id=" + respData.get("prepay_id"));
			payOrderData.put("signType", "MD5");
			String paySign = WXPayUtil.generateSignature(payOrderData, "SEmd1iL57k0YcatMkHWnZ0iboLwJDVs9");
			payOrderData.put("paySign", paySign);

			mongoTemplate.insert(reqData, PayConstants.ORDER_COLLECTION);
			logger.info("保存微信支付订单数据.");

			respData.put("payOrder", JSON.toJSONString(payOrderData));
		}
		logger.info("Response data :" + respData);
		return ResponseHandlerUtil.loadWXResponse(respData);
	}

	/**
	 * 作用：关闭订单 场景：公共号支付、扫码支付、APP支付
	 * 
	 * @param reqData
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	@Override
	public ResponseHandlerVO closeOrder(final Map<String, Object> reqData) throws Exception {
		final WXPay wxPay = PayConfigHandler.wxPayFactory(reqData);
		Map<String, String> respData = wxPay.closeOrder(RequestDataUtils.transformRequestMap(reqData));
		String returnCode = respData.get("returnCode") == null ? "FAIL" : respData.get("returnCode");
		String resultCode = respData.get("resultCode") == null ? "FAIL" : respData.get("resultCode");
		// 根据返回报文判断提交微信订单是否成功, 成功则创建订单信息, 否则只通过切面记录操作日志
		if (returnCode.equals(PayConstants.WXPayResultCode.SUCCESS.getValue())
				&& resultCode.equals(PayConstants.WXPayResultCode.SUCCESS.getValue())) {
			StatusVO status = new StatusVO(PayStatusEnum.CLOSED.getStatusCode(), PayStatusEnum.CLOSED.getStatusName(),
					DateFormatUtil.getDateTime());
			Query query = new Query();
			query.addCriteria(Criteria.where("out_trade_no").is(reqData.get("out_trade_no")));
			Update update = Update.update("status", JSON.toJSONString(status));
			mongoTemplate.updateFirst(query, update, Map.class, PayConstants.ORDER_COLLECTION);
		}
		return ResponseHandlerUtil.loadWXResponse(respData);
	}

	/**
	 * 作用：查询订单 场景：刷卡支付、公共号支付、扫码支付、APP支付
	 * 
	 * @param reqData
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	@Override
	public ResponseHandlerVO orderQuery(final Map<String, Object> reqData) throws Exception {
		final WXPay wxPay = PayConfigHandler.wxPayFactory(reqData);
		Map<String, String> respData = wxPay.orderQuery(RequestDataUtils.transformRequestMap(reqData));
		String returnCode = respData.get("returnCode") == null ? "FAIL" : respData.get("returnCode");
		String resultCode = respData.get("resultCode") == null ? "FAIL" : respData.get("resultCode");
		// 根据返回报文判断查询微信订单是否成功, 成功则更新订单状态信息, 否则只通过切面记录操作日志
		if (returnCode.equals(PayConstants.WXPayResultCode.SUCCESS.getValue())
				&& resultCode.equals(PayConstants.WXPayResultCode.SUCCESS.getValue())) {
			String tradeState = (String) respData.get("trade_state");
			StatusVO status = null;
			switch (tradeState) {
			case PayConstants.WX_PAY_SUCCESS:// 支付成功
				status = new StatusVO(PayStatusEnum.PAYMENT_SUCCESS.getStatusCode(),
						PayStatusEnum.PAYMENT_SUCCESS.getStatusName(), DateFormatUtil.getDateTime());
				break;
			case PayConstants.WX_PAY_CLOSED: // 交易关闭
				status = new StatusVO(PayStatusEnum.CLOSED.getStatusCode(), PayStatusEnum.CLOSED.getStatusName(),
						DateFormatUtil.getDateTime());
				break;
			case PayConstants.WX_PAY_NOTPAY:// 未支付
				status = new StatusVO(PayStatusEnum.NOTPAY.getStatusCode(), PayStatusEnum.NOTPAY.getStatusName(),
						DateFormatUtil.getDateTime());
				break;
			case PayConstants.WX_PAY_REFUND:// 申请退款
				status = new StatusVO(PayStatusEnum.WAITTING_REFUND.getStatusCode(),
						PayStatusEnum.WAITTING_REFUND.getStatusName(), DateFormatUtil.getDateTime());
				break;
			case PayConstants.WX_PAY_REVOKED:
				status = new StatusVO(PayStatusEnum.REVOKED.getStatusCode(), PayStatusEnum.REVOKED.getStatusName(),
						DateFormatUtil.getDateTime());
				break;
			case PayConstants.WX_PAY_PAYERROR:// 支付错误
				status = new StatusVO(PayStatusEnum.PAYERROR.getStatusCode(), PayStatusEnum.PAYERROR.getStatusName(),
						DateFormatUtil.getDateTime());
				break;
			case PayConstants.WX_PAY_USERPAYING:// 正在支付
				status = new StatusVO(PayStatusEnum.USERPAYING.getStatusCode(),
						PayStatusEnum.USERPAYING.getStatusName(), DateFormatUtil.getDateTime());
				break;
			}

			Query query = new Query();
			query.addCriteria(Criteria.where("out_trade_no").is(reqData.get("out_trade_no")));
			Update update = Update.update("status", JSON.toJSONString(status));
			mongoTemplate.updateFirst(query, update, Map.class, PayConstants.ORDER_COLLECTION);
		}
		return ResponseHandlerUtil.loadWXResponse(respData);
	}

	/**
	 * 作用：申请退款 场景：刷卡支付、公共号支付、扫码支付、APP支付
	 * 
	 * @param reqData
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	@Override
	public ResponseHandlerVO refund(final Map<String, Object> reqData) throws Exception {

		reqData.put("out_refund_no", OutTradeNOUtil.getOutTradeNO(PayWayEnum.WX));
		final WXPay wxPay = PayConfigHandler.wxPayFactory(reqData);
		Map<String, String> respData = wxPay.refund(RequestDataUtils.transformRequestMap(reqData));

		String returnCode = respData.get("returnCode") == null ? "FAIL" : respData.get("returnCode");
		String resultCode = respData.get("resultCode") == null ? "FAIL" : respData.get("resultCode");
		// 根据返回报文判断查询微信订单是否成功, 成功则更新订单状态信息, 否则只通过切面记录操作日志
		if (returnCode.equals(PayConstants.WXPayResultCode.SUCCESS.getValue())
				&& resultCode.equals(PayConstants.WXPayResultCode.SUCCESS.getValue())) {
			StatusVO status = new StatusVO(PayStatusEnum.WAITTING_REFUND.getStatusCode(),
					PayStatusEnum.WAITTING_REFUND.getStatusName(), DateFormatUtil.getDateTime());
			mongoTemplate.save(reqData, PayConstants.ORDER_REFUND_COLLECTION);
			Query query = new Query();
			query.addCriteria(Criteria.where("out_trade_no").is(reqData.get("out_trade_no")));
			Update update = Update.update("status", JSON.toJSONString(status));
			mongoTemplate.updateMulti(query, update, Map.class);
		}

		return ResponseHandlerUtil.loadWXResponse(respData);
	}

	/**
	 * 作用：对账单下载（成功时返回对账单数据，失败时返回XML格式数据） 场景：刷卡支付、公共号支付、扫码支付、APP支付
	 * 
	 * @param reqData
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	@Override
	public ResponseHandlerVO refundQuery(final Map<String, Object> reqData) throws Exception {
		final WXPay wxPay = PayConfigHandler.wxPayFactory(reqData);
		Map<String, String> respData = wxPay.refundQuery(RequestDataUtils.transformRequestMap(reqData));
		return ResponseHandlerUtil.loadWXResponse(respData);
	}

	/**
	 * 作用：对账单下载（成功时返回对账单数据，失败时返回XML格式数据） 场景：刷卡支付、公共号支付、扫码支付、APP支付
	 * 
	 * @param reqData
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	@Override
	public ResponseHandlerVO downloadBill(final Map<String, Object> reqData) throws Exception {
		final WXPay wxPay = PayConfigHandler.wxPayFactory(reqData);
		Map<String, String> respData = wxPay.downloadBill(RequestDataUtils.transformRequestMap(reqData));
		return ResponseHandlerUtil.loadWXResponse(respData);
	}

	/**
	 * 作用：提交刷卡支付 场景：刷卡支付
	 * 
	 * @param reqData
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	@Override
	public ResponseHandlerVO microPay(final Map<String, Object> reqData) throws Exception {
		final WXPay wxPay = PayConfigHandler.wxPayFactory(reqData);
		Map<String, String> respData = wxPay.microPay(RequestDataUtils.transformRequestMap(reqData));
		return ResponseHandlerUtil.loadWXResponse(respData);
	}

	/**
	 * 作用：交易保障 场景：刷卡支付、公共号支付、扫码支付、APP支付
	 * 
	 * @param reqData
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	@Override
	public ResponseHandlerVO report(final Map<String, Object> reqData) throws Exception {
		final WXPay wxPay = PayConfigHandler.wxPayFactory(reqData);
		Map<String, String> respData = wxPay.report(RequestDataUtils.transformRequestMap(reqData));
		return ResponseHandlerUtil.loadWXResponse(respData);
	}

}
