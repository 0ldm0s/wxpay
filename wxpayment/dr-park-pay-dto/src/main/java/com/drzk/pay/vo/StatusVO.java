/**
 * 
 */
package com.drzk.pay.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 支付订单状态
 * @author Xu Xiwen
 * @date 2018-07-23
 */
@Getter
@Setter
@ToString
public class StatusVO implements Serializable {

	/**
	 * @author Xu Xiwen
	 *
	 */
	private static final long serialVersionUID = -8640590708049543930L;
	
	private Integer statusCode;
	private String statusName;
	private String createdDate;
	
	public StatusVO(Integer statusCode, String statusName, String createdDate) {
		super();
		this.statusCode = statusCode;
		this.statusName = statusName;
		this.createdDate = createdDate;
	}
	

}
