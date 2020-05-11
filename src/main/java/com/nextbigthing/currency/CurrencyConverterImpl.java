package com.nextbigthing.currency;

import java.math.BigDecimal;

import com.nextbigthing.shopping.Value;

public class CurrencyConverterImpl implements CurrencyConverter{

	private BigDecimal exchangeRate;
	private Currency newCurrency;
	
	@SuppressWarnings("unused")
	private CurrencyConverterImpl() {
	}
	
	public CurrencyConverterImpl(Currency newCurrency, float exchangeRate) {
		super();
		this.newCurrency = newCurrency;
		this.exchangeRate = new BigDecimal(exchangeRate);
	}

	public Value convert(Value value) {
		
		Value converted = new Value(newCurrency, BigDecimal.ZERO);
		if (value != null) {
			BigDecimal newAmount = value.getAmount().multiply(exchangeRate);
			converted = new Value(newCurrency, newAmount);
		}
		return converted;
	} 
	
}
