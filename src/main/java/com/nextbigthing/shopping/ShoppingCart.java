package com.nextbigthing.shopping;

import java.util.ArrayList;
import java.util.List;

import com.nextbigthing.currency.Currency;
import com.nextbigthing.exceptions.CurrencyMismatchException;
import com.nextbigthing.exceptions.SystemConfigurationException;
import com.nextbigthing.exceptions.UnidentifiedCurrencyException;
import com.nextbigthing.exceptions.UnknownCurrencyConversionException;

public class ShoppingCart {

	private List<Product> items = new ArrayList<>();
	private Calculator calculator;

	public ShoppingCart() throws SystemConfigurationException {
		this.calculator = new Calculator();
	}

	public void addItem(Product product) {
		items.add(product);
	}

	public Value sumCost(Currency desiredCurrency)
			throws UnidentifiedCurrencyException, CurrencyMismatchException, UnknownCurrencyConversionException {
		return calculator.sumProductCostForCurrency(desiredCurrency, items);
	}

}
