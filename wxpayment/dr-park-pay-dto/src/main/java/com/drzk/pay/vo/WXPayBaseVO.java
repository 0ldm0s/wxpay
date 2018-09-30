/**
 * 
 */
package com.drzk.pay.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Xu Xiwen
 *
 */
@Getter
@Setter
@ToString
public class WXPayBaseVO implements Serializable {

	/**
	 * @author Xu Xiwen
	 *
	 */
	private static final long serialVersionUID = -2367635917542500892L;

	private String appid;
	private String mch_id;

}
