/**
 * 
 */
package com.drzk.pay.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.alibaba.fastjson.JSON;

/**
 * @author Xu Xiwen
 *
 */
public class RequestDataUtils {

	@SuppressWarnings("static-access")
	public static final Map<String, String> transformRequestMap(Map<String, Object> reqData) {

		Map<String, String> transData = new HashMap<String, String>();
		Set<Entry<String, Object>> set = reqData.entrySet();
		for (Entry<String, Object> entry : set) {
			if (entry.getValue() instanceof String) {
				transData.put(entry.getKey(), (String) entry.getValue());
			} else if (entry.getValue() instanceof Integer || entry.getValue() instanceof Float
					|| entry.getValue() instanceof Double) {
				transData.put(entry.getKey(), new String().valueOf(entry.getValue()));
			} else {
				transData.put(entry.getKey(), JSON.toJSONString(entry.getValue()));
			}
		}

		return transData;
	}
}
