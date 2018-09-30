/**
 * 
 */
package com.drzk.pay.utils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Xu Xiwen
 *
 */
public final class DateFormatUtil implements Serializable {

	/**
	 * @author Xu Xiwen
	 *
	 */
	private static final long serialVersionUID = 1331809738164506204L;

	public final static String getDateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		return sdf.format(date);
	}

}
