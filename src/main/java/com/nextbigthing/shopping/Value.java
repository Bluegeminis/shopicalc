package com.nextbigthing.shopping;

import java.math.BigDecimal;

import com.nextbigthing.currency.Currency;
import com.nextbigthing.exceptions.CurrencyMismatchException;

public class Value {

	private Currency currency;
	private BigDecimal amount;

	public Value(Currency currency, BigDecimal amount) {
		super();
		this.currency = currency;
		if (amount != null) {
			this.amount = amount;
		}
	}

	public Value(Currency currency, int amount) {
		super();
		this.currency = currency;
		this.amount = BigDecimal.valueOf(amount);
	}

	public Currency getCurrency() {
		return this.currency;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public synchronized void add(Value other) throws CurrencyMismatchException {
		if (other != null) {
			if (this.currency.getSymbol() != other.getCurrency().getSymbol()) {
				throw new CurrencyMismatchException("Cannot sum different currencies");
			}
			this.amount = this.amount.add(other.getAmount());
		}
	}
}
