/**
 * 
 */
package com.drzk.pay.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.drzk.pay.service.IAlipayService;
import com.drzk.pay.vo.AlipayAppTradePayVO;
import com.drzk.pay.vo.AlipayF2FTradePayVO;
import com.drzk.pay.vo.AlipayPageTradePayVO;
import com.drzk.pay.vo.AlipayQueryOrCancelPayVO;
import com.drzk.pay.vo.AlipayRefundPayVO;
import com.drzk.pay.vo.AlipayWapTradePayVO;
import com.drzk.pay.vo.ResponseHandlerVO;

import io.swagger.annotations.ApiOperation;

/**
 * 支付宝支付
 * 
 * @author Xu Xiwen
 * @date 2018-07-14
 */
@RestController
@RequestMapping("/alipay")
public class AlipayController {

	@Autowired
	IAlipayService iAlipayService;

	/**
	 * 支付宝面对面支付
	 * 
	 * @param reqData
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "支付宝面对面支付", notes = "支付宝面对面支付:https://docs.open.alipay.com/194/105170/")
	@PostMapping("/f2fOrder")
	public ResponseHandlerVO alipayF2FTradeOrder(@RequestBody AlipayF2FTradePayVO reqData) {
		String jsonReqData = JSON.toJSONString(reqData);
		Map<String, Object> reqDataMap = JSONObject.parseObject(jsonReqData, Map.class);
		return iAlipayService.f2fOrder(reqDataMap);
	}

	/**
	 * 支付宝APP支付
	 * 
	 * @param reqData
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "支付宝APP支付", notes = "支付宝APP支付:https://docs.open.alipay.com/204/105465/")
	@PostMapping("/appOrder")
	public ResponseHandlerVO alipayAppTradeOrder(@RequestBody AlipayAppTradePayVO reqData) {
		String jsonReqData = JSON.toJSONString(reqData);
		Map<String, Object> reqDataMap = JSONObject.parseObject(jsonReqData, Map.class);
		return iAlipayService.appOrder(reqDataMap);
	}

	/**
	 * 支付宝Wap支付
	 * 
	 * @param reqData
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "支付宝Wap支付", notes = "支付宝Wap支付:https://docs.open.alipay.com/203/107090/")
	@PostMapping("/wapOrder")
	public ResponseHandlerVO alipayWapTradeOrder(@RequestBody AlipayWapTradePayVO reqData) {
		String jsonReqData = JSON.toJSONString(reqData);
		Map<String, Object> reqDataMap = JSONObject.parseObject(jsonReqData, Map.class);
		return iAlipayService.wapOrder(reqDataMap);
	}

	/**
	 * 支付宝PC端网站支付
	 * 
	 * @param reqData
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "支付宝PC支付", notes = "支付宝PC支付:https://docs.open.alipay.com/270/105899/")
	@PostMapping("/pageOrder")
	public ResponseHandlerVO alipayPageTradeOrder(@RequestBody AlipayPageTradePayVO reqData) {
		String jsonReqData = JSON.toJSONString(reqData);
		Map<String, Object> reqDataMap = JSONObject.parseObject(jsonReqData, Map.class);
		return iAlipayService.pageOrder(reqDataMap);
	}

	/**
	 * 支付宝交易支付查询
	 * 
	 * @param reqData
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "支付宝交易支付查询", notes = "支付宝交易支付查询:https://docs.open.alipay.com/194/105170/")
	@PostMapping("/queryOrder")
	public ResponseHandlerVO alipayTradeQuery(@RequestBody AlipayQueryOrCancelPayVO reqData) {
		String jsonReqData = JSON.toJSONString(reqData);
		Map<String, Object> reqDataMap = JSONObject.parseObject(jsonReqData, Map.class);
		return iAlipayService.queryOrder(reqDataMap);
	}

	/**
	 * 支付宝交易支付取消
	 * 
	 * @param reqData
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "支付宝交易支付取消", notes = "支付宝交易支付取消:https://docs.open.alipay.com/194/105170/")
	@PostMapping("/cancelOrder")
	public ResponseHandlerVO alipayTradeCancel(@RequestBody AlipayQueryOrCancelPayVO reqData) {
		String jsonReqData = JSON.toJSONString(reqData);
		Map<String, Object> reqDataMap = JSONObject.parseObject(jsonReqData, Map.class);
		return iAlipayService.cancelOrder(reqDataMap);
	}

	/**
	 * 支付宝交易支付退款
	 * 
	 * @param reqData
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "支付宝交易支付退款", notes = "支付宝交易支付退款:https://docs.open.alipay.com/194/105170/")
	@PostMapping("/refundOrder")
	public ResponseHandlerVO alipayTradeRefund(@RequestBody AlipayRefundPayVO reqData) {
		String jsonReqData = JSON.toJSONString(reqData);
		Map<String, Object> reqDataMap = JSONObject.parseObject(jsonReqData, Map.class);
		return iAlipayService.refundOrder(reqDataMap);
	}

	/**
	 * 支付宝交易支付下载
	 * 
	 * @param reqData
	 * @return
	 */
	@ApiOperation(value = "支付宝交易账单下载", notes = "支付宝交易账单下载:https://docs.open.alipay.com/194/105170/")
	@GetMapping("/billDownload/{date}")
	public ResponseHandlerVO alipayTradeDownloadBillUrl(@RequestParam("date") String date) {

		Map<String, Object> reqData = new HashMap<String, Object>();
		reqData.put("bill_type", "trade");
		reqData.put("bill_date", date);

		return iAlipayService.billDownload(reqData);
	}
}
