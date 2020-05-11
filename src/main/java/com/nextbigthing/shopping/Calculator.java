package com.nextbigthing.shopping;

import java.math.BigDecimal;
import java.util.List;

import com.nextbigthing.currency.Currency;
import com.nextbigthing.currency.CurrencyConverter;
import com.nextbigthing.currency.CurrencyConverterFactory;
import com.nextbigthing.exceptions.CurrencyMismatchException;
import com.nextbigthing.exceptions.SystemConfigurationException;
import com.nextbigthing.exceptions.UnidentifiedCurrencyException;
import com.nextbigthing.exceptions.UnknownCurrencyConversionException;

public class Calculator {
	
	private CurrencyConverterFactory currencyConverterFactory;
	
	public Calculator() throws SystemConfigurationException {
		currencyConverterFactory = CurrencyConverterFactory.getInstance();
	}

	public Value sumProductCostForCurrency(Currency desiredCurrency, List<Product> items)
			throws UnidentifiedCurrencyException, CurrencyMismatchException, UnknownCurrencyConversionException {

		if (desiredCurrency == null || desiredCurrency.getSymbol() == null) {
			throw new UnidentifiedCurrencyException();
		}

		Value sum = new Value(desiredCurrency, BigDecimal.ZERO);

		if (items != null && items.size() > 0) {

			for (Product item : items) {
				Value cost = item.getCost();
				Currency itemCurrency = cost.getCurrency();
				if (itemCurrency.equals(sum.getCurrency())) {
					sum.add(cost);
				} else { // need to convert currency
					CurrencyConverter currencyConverter = currencyConverterFactory.getConverter(itemCurrency, desiredCurrency);
					Value costInDesiredCurrency = currencyConverter.convert(cost);
					sum.add(costInDesiredCurrency);
				}
			}
		}

		return sum;
	}
}
