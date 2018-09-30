/**
 * 
 */
package com.drzk.pay.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 支付字典表
 * 
 * @author Xu Xiwen
 * @date 2018-07-18
 */
public final class OperationTypeDictUtil {

	private static Map<String, String> map = new HashMap<String, String>();

	static {
		map.put("f2fOrder", "支付宝面对面支付");
		map.put("appOrder", "支付宝APP支付");
		map.put("wapOrder", "支付移动支付");
		map.put("pageOrder", "支付宝网页支付");
		map.put("queryOrder", "支付宝查询订单");
		map.put("cancelOrder", "支付宝取消订单");
		map.put("refundOrder", "支付宝退款");
		map.put("billDownload", "支付宝下载对账单");

		map.put("unifiedOrder", "微信统一支付");
		map.put("closeOrder", "微信关闭支付订单");
		map.put("orderQuery", "微信查询订单");
		map.put("refund", "微信退款");
		map.put("refundQuery", "微信退款查询");
		map.put("downloadBill", "微信下载对账单");
		map.put("microPay", "微信刷卡支付");
		map.put("report", "微信交易保障");
	}

	public final static String getOperationTypeValue(String key) {
		return map.get(key);
	}
}
