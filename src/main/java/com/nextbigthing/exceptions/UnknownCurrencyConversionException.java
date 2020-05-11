package com.nextbigthing.exceptions;

public class UnknownCurrencyConversionException extends Exception {

	private static final long serialVersionUID = 1L;

	public UnknownCurrencyConversionException(String message) {
		super(message);
	}

}
