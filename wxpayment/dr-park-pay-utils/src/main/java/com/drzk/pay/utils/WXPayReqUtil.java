package com.drzk.pay.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class WXPayReqUtil {
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> obj2Map(Object obj){
		Map<String, Object> result = new HashMap<String, Object>();
		String reqJson = JSON.toJSONString(obj);
		result = JSONObject.parseObject(reqJson, Map.class);
		return result;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object map2Obj(String json, Class clz){
		Object obj = JSONObject.parseObject(json, clz);
		return obj;
	}
	
	public final static void orderExpireTime(Map<String, Object> reqData, Integer minute) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		
		Date date = Calendar.getInstance().getTime();
		long start = date.getTime();
		String time_start = sdf.format(date);
		
		long expire = start + minute * 60 * 1000;
		Date expireDate = new Date(expire);
		String time_expire = sdf.format(expireDate);
		
		reqData.put("time_start", time_start);
		reqData.put("time_expire", time_expire);
	}

}
