/**
 * 
 */
package com.drzk.pay.vo;

import java.math.BigDecimal;

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
public final class AlipayF2FTradePayVO extends AlipayTradePayBaseVO {

	/**
	 * @author Xu Xiwen
	 *
	 */
	private static final long serialVersionUID = 4459568396959206103L;

	private final String scene = "bar_code"; // 条码支付固定传入bar_code
	/**
	 * total_amount - 商户出资的优惠金额 = receipt_amount；
	 * receipt_amount - 支付宝出资的优惠金额 = buyer_pay_amount；
	 * buyer_pay_amount - 用户自由的营销工具（目前只有集分宝）= invoice_amount;
	 */
	private BigDecimal receipt_amount;		// 实收金额，商户实际入账的金额（扣手续费之前）
	private BigDecimal buyer_pay_amount;	// 用户实付金额，建议打印在小票上避免退款时出现纠纷
	private BigDecimal invoice_pay_amount;	// 开票金额，快速告知商户应该给用户开多少钱发票

}
