package com.drzk.pay.utils;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.drzk.pay.constant.AlipayStatusEnum;
import com.drzk.pay.constant.PayConstants;
import com.drzk.pay.vo.ResponseHandlerVO;

public final class ResponseHandlerUtil {

	public final static ResponseHandlerVO loadWXResponse(Map<String, String> respData) {
		ResponseHandlerVO respVO = new ResponseHandlerVO();

		String returnCode = respData.get("return_code");
		String returnMsg = respData.get("return_msg");
		if (returnCode.equals(PayConstants.WXPayResultCode.SUCCESS.getValue())) {
			respVO.setCode(returnCode);
			respVO.setSuccess(PayConstants.WXPayResultCode.SUCCESS.getFlag());
			respVO.setMessage(returnMsg);
			respVO.setResultBody(respData);
		} else if (returnCode.equals(PayConstants.WXPayResultCode.FAIL.getValue())) {
			respVO.setCode(returnCode);
			respVO.setSuccess(PayConstants.WXPayResultCode.FAIL.getFlag());
			respVO.setMessage(returnMsg);
			respVO.setResultBody(respData);
		}

		return respVO;
	}

	@SuppressWarnings("unchecked")
	public final static ResponseHandlerVO loadAlipayResponse(String respData, AlipayStatusEnum status, Exception e) {
		ResponseHandlerVO respVO = new ResponseHandlerVO();
		if (status == AlipayStatusEnum.SUCCESS) {
			Map<String, Object> map = JSONObject.parseObject(respData, Map.class);
			String resultCode = (String) map.get("code");
			if(resultCode.equals(PayConstants.ALIPAY_SUCCESS)) {
				respVO.setCode(resultCode);
				respVO.setMessage((String) map.get("msg"));
				respVO.setSuccess(AlipayStatusEnum.SUCCESS.isFlag());
			}else {
				respVO.setCode(resultCode);
				respVO.setMessage((String) map.get("sub_msg"));
				respVO.setSuccess(AlipayStatusEnum.EXCEPTION.isFlag());
			}
			respVO.setSuccess(AlipayStatusEnum.SUCCESS.isFlag());
			respVO.setResultBody(map.get("body"));
		} else if (status == AlipayStatusEnum.EXCEPTION) {
			respVO.setCode(PayConstants.ALIPAY_ERROR);
			respVO.setMessage(e.getMessage());
			respVO.setSuccess(AlipayStatusEnum.EXCEPTION.isFlag());
			respVO.setResultBody(null);
		}

		return respVO;
	}
	
	public final static ResponseHandlerVO loadMessageResponse(final String message) {
		ResponseHandlerVO respVO = new ResponseHandlerVO();
		respVO.setCode(PayConstants.WXPayResultCode.FAIL.getValue());
		respVO.setSuccess(PayConstants.WXPayResultCode.FAIL.getFlag());
		respVO.setMessage(message);
		respVO.setResultBody(message);
		return respVO;
	}

}
