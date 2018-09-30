/**
 * 
 */
package com.drzk.pay.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import com.drzk.pay.constant.PayConstants;
import com.drzk.pay.constant.PayWayEnum;

/**
 * 订单号生成Util
 * 
 * @author Xu Xiwen
 * @date 2018-07-14
 */
public final class OutTradeNOUtil {

	@SuppressWarnings("static-access")
	public final static String getOutTradeNO(final PayWayEnum payWay) {
		StringBuffer sb = new StringBuffer();
		final Integer random = new Random().nextInt();
		final Date date = new Date();
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		if (payWay == PayWayEnum.WX) {// 32位长度
			sb.append(PayConstants.WXPAY_OUT_TRADE_NO_HEADER);
			sb.append(sdf.format(date));
			sb.append(new String().format("%011d", random));
		} else if (payWay == PayWayEnum.ALIPAY) {// 28位长度
			sb.append(PayConstants.ALIPAY_OUT_TRADE_NO_HEADER);
			sb.append(sdf.format(date));
			sb.append(new String().format("%07d", random));
		}
		return sb.toString();
	}
}
