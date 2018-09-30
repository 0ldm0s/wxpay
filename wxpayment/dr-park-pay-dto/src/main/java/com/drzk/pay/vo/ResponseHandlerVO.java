package com.drzk.pay.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 所有支付交易请求统一返回格式约定对象
 * 
 * @author Xu Xiwen
 * @date 2018-07-13
 */
@Getter
@Setter
@ToString
public class ResponseHandlerVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4368874908990756369L;
	// 状态代码
	private String code;
	// 自定义输出信息
	private String message;
	// 是否请求成功标识
	private boolean success;
	// 接口原始返回数据
	private Object resultBody;

}
