package com.zlb.ecp.pojo;

import java.io.Serializable;

public class DataCommDto implements Serializable {

	private static final long serialVersionUID = -1962061902522566532L;

	private String valueOne;
	private String valueTwo;
	private String valueThr;
	private String valueFou;
	private String valueFiv;
	private String valueSix;
	private String valueSev;
	private String valueEng;

	public DataCommDto() {
		this.valueOne = "0";
		this.valueTwo = "0";
		this.valueThr = "0";
		this.valueFou = "0";
		this.valueFiv = "0";
		this.valueSix = "0";
		this.valueSev = "0";
		this.valueEng = "0";
	}

	public String getValueOne() {
		return valueOne;
	}

	public void setValueOne(String valueOne) {
		this.valueOne = valueOne;
	}

	public String getValueTwo() {
		return valueTwo;
	}

	public void setValueTwo(String valueTwo) {
		this.valueTwo = valueTwo;
	}

	public String getValueThr() {
		return valueThr;
	}

	public void setValueThr(String valueThr) {
		this.valueThr = valueThr;
	}

	public String getValueFou() {
		return valueFou;
	}

	public void setValueFou(String valueFou) {
		this.valueFou = valueFou;
	}

	public String getValueFiv() {
		return valueFiv;
	}

	public void setValueFiv(String valueFiv) {
		this.valueFiv = valueFiv;
	}

	public String getValueSix() {
		return valueSix;
	}

	public void setValueSix(String valueSix) {
		this.valueSix = valueSix;
	}

	public String getValueSev() {
		return valueSev;
	}

	public void setValueSev(String valueSev) {
		this.valueSev = valueSev;
	}

	public String getValueEng() {
		return valueEng;
	}

	public void setValueEng(String valueEng) {
		this.valueEng = valueEng;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
