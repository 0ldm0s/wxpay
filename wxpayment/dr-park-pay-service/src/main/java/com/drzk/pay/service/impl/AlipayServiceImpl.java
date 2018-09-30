package com.drzk.pay.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayDataDataserviceBillDownloadurlQueryRequest;
import com.alipay.api.request.AlipayEcoMycarParkingConfigQueryRequest;
import com.alipay.api.request.AlipayEcoMycarParkingConfigSetRequest;
import com.alipay.api.request.AlipayEcoMycarParkingEnterinfoSyncRequest;
import com.alipay.api.request.AlipayEcoMycarParkingExitinfoSyncRequest;
import com.alipay.api.request.AlipayEcoMycarParkingOrderSyncRequest;
import com.alipay.api.request.AlipayEcoMycarParkingOrderUpdateRequest;
import com.alipay.api.request.AlipayEcoMycarParkingParkinglotinfoCreateRequest;
import com.alipay.api.request.AlipayEcoMycarParkingParkinglotinfoUpdateRequest;
import com.alipay.api.request.AlipayEcoMycarParkingVehicleQueryRequest;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeCancelRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.alipay.api.response.AlipayDataDataserviceBillDownloadurlQueryResponse;
import com.alipay.api.response.AlipayEcoMycarParkingConfigQueryResponse;
import com.alipay.api.response.AlipayEcoMycarParkingConfigSetResponse;
import com.alipay.api.response.AlipayEcoMycarParkingEnterinfoSyncResponse;
import com.alipay.api.response.AlipayEcoMycarParkingExitinfoSyncResponse;
import com.alipay.api.response.AlipayEcoMycarParkingOrderSyncResponse;
import com.alipay.api.response.AlipayEcoMycarParkingOrderUpdateResponse;
import com.alipay.api.response.AlipayEcoMycarParkingParkinglotinfoCreateResponse;
import com.alipay.api.response.AlipayEcoMycarParkingParkinglotinfoUpdateResponse;
import com.alipay.api.response.AlipayEcoMycarParkingVehicleQueryResponse;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.drzk.pay.constant.AlipayStatusEnum;
import com.drzk.pay.constant.PayConstants;
import com.drzk.pay.constant.PayStatusEnum;
import com.drzk.pay.constant.PayWayEnum;
import com.drzk.pay.handler.PayConfigHandler;
import com.drzk.pay.service.IAlipayService;
import com.drzk.pay.utils.DateFormatUtil;
import com.drzk.pay.utils.OutTradeNOUtil;
import com.drzk.pay.utils.ResponseHandlerUtil;
import com.drzk.pay.vo.ResponseHandlerVO;
import com.drzk.pay.vo.StatusVO;

/**
 * 支付宝支付实现类
 * 
 * @author Xu Xiwen
 * @date 2018-07-14
 *
 */
@Service
public class AlipayServiceImpl implements IAlipayService {

	Logger logger = LoggerFactory.getLogger(AlipayServiceImpl.class);

	@Autowired
	MongoTemplate mongoTemplate;

	/**
	 * 面对面支付实现方法
	 * 
	 * @author Xu Xiwen
	 * @param String
	 * @return ResponseHandlerVO
	 */
	@Override
	public ResponseHandlerVO f2fOrder(final Map<String, Object> jsonReqData) {
		jsonReqData.put("out_trade_no", OutTradeNOUtil.getOutTradeNO(PayWayEnum.ALIPAY));
		AlipayTradePayRequest request = new AlipayTradePayRequest(); // 创建API对应的request类
		request.setBizContent(JSON.toJSONString(jsonReqData)); // 设置业务参数
		AlipayTradePayResponse response = null;
		try {
			AlipayClient alipayClient = PayConfigHandler.alipayClientConfigFactory(jsonReqData);
			response = alipayClient.execute(request);
			String code = response.getCode();
			if (code.equals(PayConstants.ALIPAY_SUCCESS)) {
				String tradeNo = response.getTradeNo();
				jsonReqData.put("trade_no", tradeNo);
				StatusVO status = new StatusVO(PayStatusEnum.WAITING_PAYMENT.getStatusCode(),
						PayStatusEnum.WAITING_PAYMENT.getStatusName(), DateFormatUtil.getDateTime());
				jsonReqData.put("status", JSON.toJSONString(status));
				mongoTemplate.insert(jsonReqData, PayConstants.ORDER_COLLECTION);
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
			return ResponseHandlerUtil.loadAlipayResponse(null, AlipayStatusEnum.EXCEPTION, e);
		}
		return ResponseHandlerUtil.loadAlipayResponse(JSON.toJSONString(response), AlipayStatusEnum.SUCCESS, null);
	}

	/**
	 * 移动端手机,PAD网页支付实现方法
	 * 
	 * @author Xu Xiwen
	 * @param String
	 * @return ResponseHandlerVO
	 */
	@Override
	public ResponseHandlerVO wapOrder(final Map<String, Object> jsonReqData) {
		AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest(); // 创建API对应的request类
		request.setBizContent(JSON.toJSONString(jsonReqData)); // 设置业务参数
		AlipayTradeWapPayResponse response = null;
		try {
			AlipayClient alipayClient = PayConfigHandler.alipayClientConfigFactory(jsonReqData);
			response = alipayClient.execute(request);
			String code = response.getCode();
			if (code.equals(PayConstants.ALIPAY_SUCCESS)) {
				String tradeNo = response.getTradeNo();
				jsonReqData.put("trade_no", tradeNo);
				StatusVO status = new StatusVO(PayStatusEnum.WAITING_PAYMENT.getStatusCode(),
						PayStatusEnum.WAITING_PAYMENT.getStatusName(), DateFormatUtil.getDateTime());
				jsonReqData.put("status", JSON.toJSONString(status));
				mongoTemplate.insert(jsonReqData, PayConstants.ORDER_COLLECTION);
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
			return ResponseHandlerUtil.loadAlipayResponse(null, AlipayStatusEnum.EXCEPTION, e);
		}
		return ResponseHandlerUtil.loadAlipayResponse(JSON.toJSONString(response), AlipayStatusEnum.SUCCESS, null);
	}

	/**
	 * app应用跳转支付实现方法
	 * 
	 * @author Xu Xiwen
	 * @param String
	 * @return ResponseHandlerVO
	 */
	@Override
	public ResponseHandlerVO appOrder(final Map<String, Object> jsonReqData) {
		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest(); // 创建API对应的request类
		request.setBizContent(JSON.toJSONString(jsonReqData)); // 设置业务参数
		AlipayTradeAppPayResponse response = null;
		try {
			AlipayClient alipayClient = PayConfigHandler.alipayClientConfigFactory(jsonReqData);
			response = alipayClient.execute(request);
		} catch (AlipayApiException e) {
			e.printStackTrace();
			return ResponseHandlerUtil.loadAlipayResponse(null, AlipayStatusEnum.EXCEPTION, e);
		}
		return ResponseHandlerUtil.loadAlipayResponse(JSON.toJSONString(response), AlipayStatusEnum.SUCCESS, null);
	}

	/**
	 * PC端电脑版网页支付实现方法
	 * 
	 * @author Xu Xiwen
	 * @param String
	 * @return ResponseHandlerVO
	 */
	@Override
	public ResponseHandlerVO pageOrder(final Map<String, Object> jsonReqData) {
		// 创建API对应的request类
		AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
		request.setBizContent(JSON.toJSONString(jsonReqData)); // 设置业务参数
		AlipayTradePagePayResponse response = null;
		try {
			AlipayClient alipayClient = PayConfigHandler.alipayClientConfigFactory(jsonReqData);
			response = alipayClient.execute(request);
		} catch (AlipayApiException e) {
			e.printStackTrace();
			return ResponseHandlerUtil.loadAlipayResponse(null, AlipayStatusEnum.EXCEPTION, e);
		}
		return ResponseHandlerUtil.loadAlipayResponse(JSON.toJSONString(response), AlipayStatusEnum.SUCCESS, null);
	}

	/**
	 * 交易查询实现方法
	 * 
	 * @author Xu Xiwen
	 * @param String
	 * @return ResponseHandlerVO
	 */
	@Override
	public ResponseHandlerVO queryOrder(final Map<String, Object> jsonReqData) {
		AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
		request.setBizContent(JSON.toJSONString(jsonReqData)); // 设置业务参数
		AlipayTradeQueryResponse response = null;
		try {
			AlipayClient alipayClient = PayConfigHandler.alipayClientConfigFactory(jsonReqData);
			response = alipayClient.execute(request);
		} catch (AlipayApiException e) {
			e.printStackTrace();
			return ResponseHandlerUtil.loadAlipayResponse(null, AlipayStatusEnum.EXCEPTION, e);
		}
		return ResponseHandlerUtil.loadAlipayResponse(JSON.toJSONString(response), AlipayStatusEnum.SUCCESS, null);
	}

	/**
	 * 取消交易实现方法
	 * 
	 * @author Xu Xiwen
	 * @param String
	 * @return ResponseHandlerVO
	 */
	@Override
	public ResponseHandlerVO cancelOrder(final Map<String, Object> jsonReqData) {
		AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
		request.setBizContent(JSON.toJSONString(jsonReqData)); // 设置业务参数
		AlipayTradeCancelResponse response = null;
		try {
			AlipayClient alipayClient = PayConfigHandler.alipayClientConfigFactory(jsonReqData);
			response = alipayClient.execute(request);
		} catch (AlipayApiException e) {
			e.printStackTrace();
			return ResponseHandlerUtil.loadAlipayResponse(null, AlipayStatusEnum.EXCEPTION, e);
		}
		return ResponseHandlerUtil.loadAlipayResponse(JSON.toJSONString(response), AlipayStatusEnum.SUCCESS, null);
	}

	/**
	 * 交易退款支付实现方法
	 * 
	 * @author Xu Xiwen
	 * @param String
	 * @return ResponseHandlerVO
	 */
	@Override
	public ResponseHandlerVO refundOrder(final Map<String, Object> jsonReqData) {
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
		request.setBizContent(JSON.toJSONString(jsonReqData)); // 设置业务参数
		AlipayTradeRefundResponse response = null;
		try {
			AlipayClient alipayClient = PayConfigHandler.alipayClientConfigFactory(jsonReqData);
			response = alipayClient.execute(request);
		} catch (AlipayApiException e) {
			e.printStackTrace();
			return ResponseHandlerUtil.loadAlipayResponse(null, AlipayStatusEnum.EXCEPTION, e);
		}
		return ResponseHandlerUtil.loadAlipayResponse(JSON.toJSONString(response), AlipayStatusEnum.SUCCESS, null);
	}

	/**
	 * 下载支付包对账单实现方法(下载url只有30秒有效期且只能查询当日交易账单)
	 * 
	 * @author Xu Xiwen
	 * @param String
	 * @return ResponseHandlerVO
	 */
	@Override
	public ResponseHandlerVO billDownload(final Map<String, Object> reqData) {
		AlipayDataDataserviceBillDownloadurlQueryRequest request = new AlipayDataDataserviceBillDownloadurlQueryRequest();

		request.setBizContent(JSON.toJSONString(reqData)); // 设置业务参数
		AlipayDataDataserviceBillDownloadurlQueryResponse response = null;
		try {
			AlipayClient alipayClient = PayConfigHandler.alipayClientConfigFactory(reqData);
			response = alipayClient.execute(request);
		} catch (AlipayApiException e) {
			e.printStackTrace();
			return ResponseHandlerUtil.loadAlipayResponse(null, AlipayStatusEnum.EXCEPTION, e);
		}
		return ResponseHandlerUtil.loadAlipayResponse(JSON.toJSONString(response), AlipayStatusEnum.SUCCESS, null);
	}

	/*
	 * (non-Javadoc) 支付宝无感支付-车辆入场
	 * 
	 * @see com.drzk.pay.service.IAlipayService#myCarEnter(java.util.Map)
	 */
	@Override
	public ResponseHandlerVO myCarEnter(final Map<String, Object> data) {
		AlipayClient alipayClient = PayConfigHandler.alipayClientConfigFactory(data);
		AlipayEcoMycarParkingEnterinfoSyncRequest request = new AlipayEcoMycarParkingEnterinfoSyncRequest();
		request.setBizContent(JSON.toJSONString(data));
		AlipayEcoMycarParkingEnterinfoSyncResponse response = null;
		try {
			response = alipayClient.execute(request);
		} catch (AlipayApiException e) {
			e.printStackTrace();
			return ResponseHandlerUtil.loadAlipayResponse(null, AlipayStatusEnum.EXCEPTION, e);
		}
		return ResponseHandlerUtil.loadAlipayResponse(JSON.toJSONString(response), AlipayStatusEnum.SUCCESS, null);
	}

	/*
	 * (non-Javadoc) 支付宝无感支付-车辆出场
	 * 
	 * @see com.drzk.pay.service.IAlipayService#myCarOut(java.util.Map)
	 */
	@Override
	public ResponseHandlerVO myCarOut(final Map<String, Object> data) {
		AlipayClient alipayClient = PayConfigHandler.alipayClientConfigFactory(data);
		AlipayEcoMycarParkingExitinfoSyncRequest  request = new AlipayEcoMycarParkingExitinfoSyncRequest ();
		request.setBizContent(JSON.toJSONString(data));
		AlipayEcoMycarParkingExitinfoSyncResponse response = null;
		try {
			response = alipayClient.execute(request);
		} catch (AlipayApiException e) {
			e.printStackTrace();
			return ResponseHandlerUtil.loadAlipayResponse(null, AlipayStatusEnum.EXCEPTION, e);
		}
		return ResponseHandlerUtil.loadAlipayResponse(JSON.toJSONString(response), AlipayStatusEnum.SUCCESS, null);
	}

	/*
	 * (non-Javadoc) 支付宝无感支付-订单同步
	 * 
	 * @see com.drzk.pay.service.IAlipayService#myCarOrderSync(java.util.Map)
	 */
	@Override
	public ResponseHandlerVO myCarOrderSync(final Map<String, Object> data) {
		AlipayClient alipayClient = PayConfigHandler.alipayClientConfigFactory(data);
		AlipayEcoMycarParkingOrderSyncRequest  request = new AlipayEcoMycarParkingOrderSyncRequest ();
		request.setBizContent(JSON.toJSONString(data));
		AlipayEcoMycarParkingOrderSyncResponse response = null;
		try {
			response = alipayClient.execute(request);
		} catch (AlipayApiException e) {
			e.printStackTrace();
			return ResponseHandlerUtil.loadAlipayResponse(null, AlipayStatusEnum.EXCEPTION, e);
		}
		return ResponseHandlerUtil.loadAlipayResponse(JSON.toJSONString(response), AlipayStatusEnum.SUCCESS, null);
	}

	/*
	 * (non-Javadoc) 支付宝无感支付-订单更新
	 * 
	 * @see com.drzk.pay.service.IAlipayService#myCarOrderUpdate(java.util.Map)
	 */
	@Override
	public ResponseHandlerVO myCarOrderUpdate(final Map<String, Object> data) {
		AlipayClient alipayClient = PayConfigHandler.alipayClientConfigFactory(data);
		AlipayEcoMycarParkingOrderUpdateRequest  request = new AlipayEcoMycarParkingOrderUpdateRequest ();
		request.setBizContent(JSON.toJSONString(data));
		AlipayEcoMycarParkingOrderUpdateResponse response = null;
		try {
			response = alipayClient.execute(request);
		} catch (AlipayApiException e) {
			e.printStackTrace();
			return ResponseHandlerUtil.loadAlipayResponse(null, AlipayStatusEnum.EXCEPTION, e);
		}
		return ResponseHandlerUtil.loadAlipayResponse(JSON.toJSONString(response), AlipayStatusEnum.SUCCESS, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.drzk.pay.service.IAlipayService#myCarNoQuery(java.util.Map)
	 */
	@Override
	public ResponseHandlerVO vehicleNoQuery(final Map<String, Object> data) {
		AlipayClient alipayClient = PayConfigHandler.alipayClientConfigFactory(data);
		AlipayEcoMycarParkingVehicleQueryRequest request = new AlipayEcoMycarParkingVehicleQueryRequest   ();
		request.setBizContent(JSON.toJSONString(data));
		AlipayEcoMycarParkingVehicleQueryResponse response = null;
		try {
			response = alipayClient.execute(request);
		} catch (AlipayApiException e) {
			e.printStackTrace();
			return ResponseHandlerUtil.loadAlipayResponse(null, AlipayStatusEnum.EXCEPTION, e);
		}
		return ResponseHandlerUtil.loadAlipayResponse(JSON.toJSONString(response), AlipayStatusEnum.SUCCESS, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.drzk.pay.service.IAlipayService#queryISVSystemConfig(java.util.Map)
	 */
	@Override
	public ResponseHandlerVO queryISVSystemConfig(final Map<String, Object> data) {
		AlipayClient alipayClient = PayConfigHandler.alipayClientConfigFactory(data);
		AlipayEcoMycarParkingConfigQueryRequest request = new AlipayEcoMycarParkingConfigQueryRequest  ();
		request.setBizContent(JSON.toJSONString(data));
		AlipayEcoMycarParkingConfigQueryResponse response = null;
		try {
			response = alipayClient.execute(request);
		} catch (AlipayApiException e) {
			e.printStackTrace();
			return ResponseHandlerUtil.loadAlipayResponse(null, AlipayStatusEnum.EXCEPTION, e);
		}
		return ResponseHandlerUtil.loadAlipayResponse(JSON.toJSONString(response), AlipayStatusEnum.SUCCESS, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.drzk.pay.service.IAlipayService#setISVSystemConfig(java.util.Map)
	 */
	@Override
	public ResponseHandlerVO setISVSystemConfig(final Map<String, Object> data) {
		AlipayClient alipayClient = PayConfigHandler.alipayClientConfigFactory(data);
		AlipayEcoMycarParkingConfigSetRequest request = new AlipayEcoMycarParkingConfigSetRequest();
		request.setBizContent(JSON.toJSONString(data));
		AlipayEcoMycarParkingConfigSetResponse response = null;
		try {
			response = alipayClient.execute(request);
		} catch (AlipayApiException e) {
			e.printStackTrace();
			return ResponseHandlerUtil.loadAlipayResponse(null, AlipayStatusEnum.EXCEPTION, e);
		}
		return ResponseHandlerUtil.loadAlipayResponse(JSON.toJSONString(response), AlipayStatusEnum.SUCCESS, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.drzk.pay.service.IAlipayService#createParkingLotInfo(java.util.Map)
	 */
	@Override
	public ResponseHandlerVO createParkingLotInfo(final Map<String, Object> data) {
		AlipayClient alipayClient = PayConfigHandler.alipayClientConfigFactory(data);
		AlipayEcoMycarParkingParkinglotinfoCreateRequest request = new AlipayEcoMycarParkingParkinglotinfoCreateRequest();
		request.setBizContent(JSON.toJSONString(data));
		AlipayEcoMycarParkingParkinglotinfoCreateResponse response = null;
		try {
			response = alipayClient.execute(request);
		} catch (AlipayApiException e) {
			e.printStackTrace();
			return ResponseHandlerUtil.loadAlipayResponse(null, AlipayStatusEnum.EXCEPTION, e);
		}
		return ResponseHandlerUtil.loadAlipayResponse(JSON.toJSONString(response), AlipayStatusEnum.SUCCESS, null); 
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.drzk.pay.service.IAlipayService#updateParkingLotInfo(java.util.Map)
	 */
	@Override
	public ResponseHandlerVO updateParkingLotInfo(final Map<String, Object> data) {
		AlipayClient alipayClient = PayConfigHandler.alipayClientConfigFactory(data);
		AlipayEcoMycarParkingParkinglotinfoUpdateRequest request = new AlipayEcoMycarParkingParkinglotinfoUpdateRequest();
		request.setBizContent(JSON.toJSONString(data));
		AlipayEcoMycarParkingParkinglotinfoUpdateResponse response = null;
		try {
			response = alipayClient.execute(request);
		} catch (AlipayApiException e) {
			e.printStackTrace();
			return ResponseHandlerUtil.loadAlipayResponse(null, AlipayStatusEnum.EXCEPTION, e);
		}
		return ResponseHandlerUtil.loadAlipayResponse(JSON.toJSONString(response), AlipayStatusEnum.SUCCESS, null); 
	}
}
