package com.nextbigthing.currency;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nextbigthing.exceptions.SystemConfigurationException;
import com.nextbigthing.exceptions.UnknownCurrencyConversionException;

public class CurrencyConverterFactory {

	private final static String CONVERSION_CONFIG_FILE = "currencyConvertersConfig.json";

	private final static CurrencyConverterFactory INSTANCE = new CurrencyConverterFactory();

	private boolean initialized = false;

	private CurrencyFactory currencyFactory;

	private Map<CurrencyPair, CurrencyConverter> currencyPairToConverterMap = new HashMap<>();

	private CurrencyConverterFactory() {}
	
	private void init() throws SystemConfigurationException {
		ObjectMapper objectMapper = new ObjectMapper();

		try (InputStream input = CurrencyConverterFactory.class.getClassLoader()
				.getResourceAsStream(CONVERSION_CONFIG_FILE)) {

			currencyFactory = CurrencyFactory.getInstance();

			List<CurrencyConversionConfig> conversionConfigs = objectMapper.readValue(input,
					new TypeReference<List<CurrencyConversionConfig>>() {
					});

			initialized = isConfigValid(conversionConfigs, currencyFactory.getAllCurrencyCodes());

			if (initialized) {

				conversionConfigs.forEach(config -> {

					Currency srcCurrency = currencyFactory.getCurrencyByCode(config.getSrcCurrency());
					Currency targetCurrency = currencyFactory.getCurrencyByCode(config.getTargetCurrency());

					CurrencyPair currencyPair = new CurrencyPair(srcCurrency, targetCurrency);

					float exchangeRate = config.getExchangeRate();
					currencyPairToConverterMap.put(currencyPair, new CurrencyConverterImpl(targetCurrency, exchangeRate));

					// Configurations specify conversions from currency A to B but let's also
					// initialize B to A
					CurrencyPair inversePair = new CurrencyPair(targetCurrency, srcCurrency);
					float inverseExchangeRate = 1 / config.getExchangeRate();
					currencyPairToConverterMap.put(inversePair,
							new CurrencyConverterImpl(srcCurrency, inverseExchangeRate));
				});
			} else {
				throw new SystemConfigurationException("Unable to configure the currency converters");
			}

		} catch (IOException e) {
			initialized = false;
			throw new SystemConfigurationException("Unable to configure the currency converters");
		}
	}

	private boolean isConfigValid(List<CurrencyConversionConfig> conversionConfigs, Set<String> currencyCodes) {

		if (currencyCodes == null || currencyCodes.size() == 0) {
			System.out.println("The currency codes must be initialized");
			return false;
		}

		if (conversionConfigs == null || conversionConfigs.size() == 0) {
			System.out.println("The " + CONVERSION_CONFIG_FILE + " must be initialized");
			return false;
		}

		boolean missingConfig = conversionConfigs.stream()
				.anyMatch(config -> config.getExchangeRate() == null || config.getSrcCurrency() == null
						|| config.getTargetCurrency() == null || config.getSrcCurrency().length() == 0
						|| config.getTargetCurrency().length() == 0);
		
		if (missingConfig) {
			System.out.println("Each property of the " + CONVERSION_CONFIG_FILE + " (srcCurrency, targetCurrency and exchangeRate) must be initialized");
			return false;
		}
		
		boolean invalidCurrency = !conversionConfigs.stream()
							.allMatch(config -> currencyCodes.contains(config.getSrcCurrency()) && currencyCodes.contains(config.getTargetCurrency()));
		if (invalidCurrency) {
			System.out.println("Some currency codes in " + CONVERSION_CONFIG_FILE + " have not been configured in the currencies configuration");
			return false;
		}
		
		boolean sameCurrencyRemap = conversionConfigs.stream()
				.anyMatch(config -> config.getSrcCurrency().equals(config.getTargetCurrency()));

		if (sameCurrencyRemap) {
			System.out.println("Entries in" + CONVERSION_CONFIG_FILE + " cannot have the same currency for srcCurrency and targetCurrency");
			return false;
		}		

		return true;
	}

	public final static CurrencyConverterFactory getInstance() throws SystemConfigurationException {

		if (!INSTANCE.isInitialized()) {
			INSTANCE.init();

		}

		return INSTANCE;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public CurrencyConverter getConverter(Currency sourceCurrency, Currency targetCurrency)
			throws UnknownCurrencyConversionException {

		CurrencyPair lookupKey = new CurrencyPair(sourceCurrency, targetCurrency);
		CurrencyConverter converter = currencyPairToConverterMap.get(lookupKey);

		if (converter == null) {
			throw new UnknownCurrencyConversionException("Currency conversion between " + sourceCurrency.getName()
					+ " and " + targetCurrency.getName() + " is not supported");
		}

		return converter;
	}

	// Could have used equals in CurrencyConversionConfig but this is more explicit
	private final static class CurrencyPair {

		private Currency sourceCurrency;
		private Currency targetCurrency;

		public CurrencyPair(Currency sourceCurrency, Currency targetCurrency) {
			super();
			this.sourceCurrency = sourceCurrency;
			this.targetCurrency = targetCurrency;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((sourceCurrency == null) ? 0 : sourceCurrency.hashCode());
			result = prime * result + ((targetCurrency == null) ? 0 : targetCurrency.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CurrencyPair other = (CurrencyPair) obj;
			if (sourceCurrency == null) {
				if (other.sourceCurrency != null)
					return false;
			} else if (!sourceCurrency.equals(other.sourceCurrency))
				return false;
			if (targetCurrency == null) {
				if (other.targetCurrency != null)
					return false;
			} else if (!targetCurrency.equals(other.targetCurrency))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "CurrencyPair [sourceCurrency=" + sourceCurrency + ", targetCurrency=" + targetCurrency + "]";
		}
	}

}
