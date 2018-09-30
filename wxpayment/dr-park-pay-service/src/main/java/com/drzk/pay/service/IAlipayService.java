package com.drzk.pay.service;

import java.util.Map;

import com.drzk.pay.vo.ResponseHandlerVO;

public interface IAlipayService {
	
	public ResponseHandlerVO f2fOrder(final Map<String, Object> jsonReqData);
	
	public ResponseHandlerVO appOrder(final Map<String, Object> jsonReqData);
	
	public ResponseHandlerVO wapOrder(final Map<String, Object> jsonReqData);
	
	public ResponseHandlerVO pageOrder(final Map<String, Object> jsonReqData);
	
	public ResponseHandlerVO queryOrder(final Map<String, Object> jsonReqData);
	
	public ResponseHandlerVO cancelOrder(final Map<String, Object> jsonReqData);
	
	public ResponseHandlerVO refundOrder(final Map<String, Object> jsonReqData);
	
	public ResponseHandlerVO billDownload(final Map<String, Object> data);
	
	public ResponseHandlerVO myCarEnter(final Map<String, Object> data);
	
	public ResponseHandlerVO myCarOut(final Map<String, Object> data);
	
	public ResponseHandlerVO myCarOrderSync(final Map<String, Object> data);
	
	public ResponseHandlerVO myCarOrderUpdate(final Map<String, Object> data);
	
	public ResponseHandlerVO vehicleNoQuery(final Map<String, Object> data);
	
	public ResponseHandlerVO queryISVSystemConfig(final Map<String, Object> data);
	
	public ResponseHandlerVO setISVSystemConfig(final Map<String, Object> data);
	
	public ResponseHandlerVO createParkingLotInfo(final Map<String, Object> data);
	
	public ResponseHandlerVO updateParkingLotInfo(final Map<String, Object> data);
	
}
