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

import com.drzk.pay.service.IWXPayService;
import com.drzk.pay.utils.WXPayReqUtil;
import com.drzk.pay.vo.ResponseHandlerVO;
import com.drzk.pay.vo.WXPayDownloadBillVO;
import com.drzk.pay.vo.WXPayOrderQueryVO;
import com.drzk.pay.vo.WXPayRefundVO;
import com.drzk.pay.vo.WXPayUnifiedOrderVO;

import io.swagger.annotations.ApiOperation;

/**
 * 微信支付
 * 
 * @author Xu Xiwen
 * @date 2018-07-14
 */
@RestController
@RequestMapping("/wxPay")
public class WXPayController {

	@Autowired
	IWXPayService iWXPayService;

	/**
	 * 微信支付统一下单接口
	 * 
	 * @param wxPayUnifiedOrderVO
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "微信支付统一下单接口", notes = "微信支付统一下单接口:https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_1")
	@PostMapping("/unifiedOrder")
	public ResponseHandlerVO unifiedOrder(@RequestBody Map<String, Object> wxPayUnifiedOrderVO) throws Exception {
//		Map<String, Object> reqData = WXPayReqUtil.obj2Map(wxPayUnifiedOrderVO);
		return iWXPayService.unifiedOrder(wxPayUnifiedOrderVO);
	}

	/**
	 * 微信支付查询订单接口
	 * 
	 * @param wxPayOrderQueryVO
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "微信支付查询订单接口", notes = "微信支付查询订单接口:https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_2")
	@PostMapping("/orderQuery")
	public ResponseHandlerVO orderQuery(@RequestBody WXPayOrderQueryVO wxPayOrderQueryVO) throws Exception {
		Map<String, Object> reqData = WXPayReqUtil.obj2Map(wxPayOrderQueryVO);
		return iWXPayService.orderQuery(reqData);
	}

	/**
	 * 微信支付关闭订单接口
	 * 
	 * @param outTradeNo
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "微信支付关闭订单接口", notes = "微信支付关闭订单接口:https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_3")
	@GetMapping("/closeOrder/{outTradeNo}")
	public ResponseHandlerVO closeOrder(@RequestParam("outTradeNo") String outTradeNo) throws Exception {
		Map<String, Object> reqData = new HashMap<String, Object>();
		reqData.put("out_trade_no", outTradeNo);
		return iWXPayService.closeOrder(reqData);
	}

	/**
	 * 微信支付申请退款接口
	 * 
	 * @param wxPayRefundVO
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "微信支付申请退款接口", notes = "微信支付申请退款接口:https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_4")
	@PostMapping("/refund")
	public ResponseHandlerVO refund(@RequestBody WXPayRefundVO wxPayRefundVO) throws Exception {
		Map<String, Object> reqData = WXPayReqUtil.obj2Map(wxPayRefundVO);
		return iWXPayService.closeOrder(reqData);
	}

	/**
	 * 微信支付申请退款查询接口
	 * 
	 * @param wxPayRefundVO
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "微信支付申请退款查询接口", notes = "微信支付申请退款查询接口:https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_5")
	@PostMapping("/refundQuery")
	public ResponseHandlerVO refundQuery(@RequestBody WXPayRefundVO wxPayRefundVO) throws Exception {
		Map<String, Object> reqData = WXPayReqUtil.obj2Map(wxPayRefundVO);
		return iWXPayService.refundQuery(reqData);
	}

	/**
	 * 微信支付对账单查询接口
	 * 
	 * @param wxPayDownloadBillVO
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "微信支付对账单查询接口", notes = "微信支付对账单查询接口:https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_6")
	@PostMapping("/downloadBill")
	public ResponseHandlerVO downloadBill(@RequestBody WXPayDownloadBillVO wxPayDownloadBillVO) throws Exception {
		Map<String, Object> reqData = WXPayReqUtil.obj2Map(wxPayDownloadBillVO);
		return iWXPayService.downloadBill(reqData);
	}
}
