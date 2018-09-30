package com.drzk.pay.service;

import java.util.Map;

import com.drzk.pay.vo.ResponseHandlerVO;

public interface IWXPayService {

	public ResponseHandlerVO unifiedOrder(final Map<String, Object> reqData) throws Exception;

	public ResponseHandlerVO closeOrder(final Map<String, Object> reqData) throws Exception;

	public ResponseHandlerVO orderQuery(final Map<String, Object> reqData) throws Exception;

	public ResponseHandlerVO refund(final Map<String, Object> reqData) throws Exception;

	public ResponseHandlerVO refundQuery(final Map<String, Object> reqData) throws Exception;

	public ResponseHandlerVO downloadBill(final Map<String, Object> reqData) throws Exception;

	public ResponseHandlerVO microPay(final Map<String, Object> reqData) throws Exception;

	public ResponseHandlerVO report(final Map<String, Object> reqData) throws Exception;

}
