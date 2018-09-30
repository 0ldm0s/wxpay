package com.drzk.pay.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.drzk.pay.constant.PayConstants;
import com.drzk.pay.constant.PayStatusEnum;
import com.drzk.pay.mqtt.MqttSender;
import com.drzk.pay.utils.DateFormatUtil;
import com.drzk.pay.vo.StatusVO;
import com.github.wxpay.sdk.WXPayUtil;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/payment/callback")
public class PaymentCallbackController {

	Logger logger = LoggerFactory.getLogger(PaymentCallbackController.class);

	@Autowired
	MongoTemplate mongoTemplate;

	@Resource
	MqttSender mqttSender;

	@SuppressWarnings({ "static-access", "unchecked", "rawtypes" })
	@ApiOperation(value = "接收微信回调数据接口", notes = "接收微信回调数据接口")
	@PostMapping("/wxCallBack.do")
	public Object ReceiptWXPayCallBackDatas(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		InputStream input = req.getInputStream();
		String sline = "";
		StringBuffer reqData = new StringBuffer();
		BufferedReader bReader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
		while ((sline = bReader.readLine()) != null) {
			reqData.append(sline);
		}
		logger.info("Wechat call back datas :" + reqData.toString());

		Map<String, String> reqDataMap = WXPayUtil.xmlToMap(reqData.toString());
		String outTradeNo = reqDataMap.get("out_trade_no");
		String resultCode = reqDataMap.get("result_code");
		String returnCode = reqDataMap.get("return_code");
		String transactionId = reqDataMap.get("transaction_id");
		if (resultCode.equals(PayConstants.WXPayResultCode.SUCCESS.getValue())
				&& returnCode.equals(PayConstants.WXPayResultCode.SUCCESS.getValue())) {
			Query query = new Query().addCriteria(Criteria.where("out_trade_no").is(outTradeNo));
			System.out.println(outTradeNo);
			List<Map> orderList = mongoTemplate.find(query, Map.class, "park_payorder");
			Map<String, Object> orderMap = orderList.get(0);
			Map<String, Object> mqttSendDatas = new HashMap<String, Object>();
			Map<String, Object> mqttSendHead = new HashMap<String, Object>();

			Map<String, Object> body = (Map<String, Object>) orderMap.get("detail");
			String header = (String) body.get("header");

			Map<String, Object> sceneInfo = (Map<String, Object>) orderMap.get("scene_info");
			String randomSessionId = (String) sceneInfo.get("randomSessionId");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			switch (header) {
			case "MONTHLY":
				mqttSendHead.put("method", "user/monthlycarpayment");
				mqttSendHead.put("replyTopic", "park/cloudplatfrom5/payment/callback/" + randomSessionId);
				mqttSendDatas.put("head", mqttSendHead);
				
				BigDecimal amount = new BigDecimal((int) orderMap.get("total_fee")).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_EVEN);
				Map<String, Object> payData = (Map<String, Object>) body.get("payData");
				String parkId = (String) payData.get("parkingNo");
				List<Map> carPayList = (List<Map>) payData.get("carportAndCarList");
				Map<String, Object> carPayMap = carPayList.get(0);
				Map<String, Object> resultData = new HashMap<String, Object>();
				resultData.put("carNo", carPayMap.get("carNo"));
				resultData.put("carTypeId", carPayMap.get("cardTypeId"));
				resultData.put("startDate", sdf.format(new Date((long) carPayMap.get("startTime"))));
				resultData.put("endDate", sdf.format(new Date((long) carPayMap.get("endTime"))));
				resultData.put("amount",amount.doubleValue()) ;
				resultData.put("payType", 2);
				resultData.put("paymentNumber", transactionId);
				resultData.put("payDate", sdf.format(new Date()));
				mqttSendDatas.put("body", resultData);
				System.out.println(JSON.toJSONString(mqttSendDatas));
				mqttSender.send("server/data/publish/phone/" + parkId, JSON.toJSONString(mqttSendDatas));
				break;
			default:
				parkId = (String) body.get("parkId");
				mqttSendHead.put("parkId", parkId);
				mqttSendHead.put("method", "park/userpaymentcarfee");
				mqttSendHead.put("replyTopic", "park/cloudplatfrom5/payment/callback/" + randomSessionId);
				mqttSendDatas.put("head", mqttSendHead);
				body.put("paymentTnx", transactionId);
				body.put("payTime", sdf.format(new Date()));
				mqttSendDatas.put("body", body);
				mqttSender.send("server/data/publish/phone/" + parkId, JSON.toJSONString(mqttSendDatas));
				break;
			}

			StatusVO status = new StatusVO(PayStatusEnum.PAYMENT_SUCCESS.getStatusCode(),
					PayStatusEnum.PAYMENT_SUCCESS.getStatusName(), DateFormatUtil.getDateTime());

			Update update = new Update().push("transactionId", transactionId).update("status", status);
			mongoTemplate.upsert(query, update, Map.class, "park_payorder");

			logger.info("query order is:" + orderMap);

			Map<String, String> returnMessage = new HashMap<String, String>();
			returnMessage.put("result_code", "SUCCESS");
			returnMessage.put("return_code", "SUCCESS");
			returnMessage.put("return_msg", "OK");
			return WXPayUtil.mapToXml(returnMessage);
		}
		return null;
	}

	@ApiOperation(value = "接收支付宝回调数据接口", notes = "接收支付宝回调数据接口")
	@PostMapping("/alipayCallBack.do")
	public void ReceiptAlipayCallBackDatas(HttpServletRequest req, HttpServletResponse resp) {

	}
	
}
