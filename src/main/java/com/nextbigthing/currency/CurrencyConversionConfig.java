package com.nextbigthing.currency;

public class CurrencyConversionConfig {

	private String srcCurrency;
	private String targetCurrency;
	
	private Float exchangeRate;
	
	public String getSrcCurrency() {
		return srcCurrency;
	}
	public void setSrcCurrency(String srcCurrency) {
		this.srcCurrency = srcCurrency;
	}
	public String getTargetCurrency() {
		return targetCurrency;
	}
	public void setTargetCurrency(String targetCurrency) {
		this.targetCurrency = targetCurrency;
	}
	public Float getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(Float exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

}
