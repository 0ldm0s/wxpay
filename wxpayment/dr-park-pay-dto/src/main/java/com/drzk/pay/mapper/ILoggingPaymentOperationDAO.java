/**
 * 
 */
package com.drzk.pay.mapper;

import org.springframework.stereotype.Component;

import com.drzk.pay.vo.LoggingPaymentOperationVO;

/**
 * @author Xu Xiwen
 *
 */
@Component
public interface ILoggingPaymentOperationDAO {

	public void save(LoggingPaymentOperationVO vo);

	public void selectList(LoggingPaymentOperationVO vo);
}
