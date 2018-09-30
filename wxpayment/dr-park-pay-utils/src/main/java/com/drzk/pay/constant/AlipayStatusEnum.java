package com.drzk.pay.constant;

public enum AlipayStatusEnum {
	SUCCESS(true), EXCEPTION(false);

	boolean flag;

	AlipayStatusEnum(boolean flag) {
		this.flag = flag;
	}

	public boolean isFlag() {
		return flag;
	}

}
