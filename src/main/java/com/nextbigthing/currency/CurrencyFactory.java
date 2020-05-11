package com.nextbigthing.currency;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nextbigthing.exceptions.SystemConfigurationException;

public class CurrencyFactory {

	private final static String CURRENCIES_FILE = "currencies.json";

	private final static CurrencyFactory INSTANCE = new CurrencyFactory();

	private boolean initialized = false;
	private Map<String, Currency> iso4217CodeToCurrencyMap = Collections.emptyMap();
	
	private ObjectMapper objectMapper = new ObjectMapper();

	private CurrencyFactory() {

		try (InputStream input = CurrencyConverterFactory.class.getClassLoader().getResourceAsStream(CURRENCIES_FILE)) {
			List<Currency> currencyCfgs = objectMapper.readValue(input, new TypeReference<List<Currency>>() {
			});

			initialized = isConfigValid(currencyCfgs);

			iso4217CodeToCurrencyMap = currencyCfgs.stream()
					.collect(Collectors.toMap(Currency::getSymbol, Function.identity()));

			System.out.println(iso4217CodeToCurrencyMap);
		} catch (IOException e) {
			initialized = false;
		}

	}

	private boolean isConfigValid(List<Currency> currencies) {

		if (currencies == null || currencies.size() == 0) {
			System.out.println(
					"The currencies configuration file " + CURRENCIES_FILE + " should contain at least one currency");
			return false;
		}

		boolean missingConfig = currencies.stream().anyMatch(config -> config.getName() == null
				|| config.getSymbol() == null || config.getName().length() == 0 || config.getSymbol().length() == 0);
		if (missingConfig) {
			System.out.println("Each property of the " + CURRENCIES_FILE + " (name, symbol) must be initialized");
			return false;
		}

		return true;
	}

	public final static CurrencyFactory getInstance() throws SystemConfigurationException {

		if (!INSTANCE.initialized) {
			throw new SystemConfigurationException("Unable to initialize the currencies configuration");	
		}
		return INSTANCE;
	}

	/**
	 * Locate currency by its ISO-4217 code
	 */
	public Currency getCurrencyByCode(String code) {

		return iso4217CodeToCurrencyMap.get(code);
	}

	/**
	 * Return all configured ISO-4217 currency codes
	 */	
	public Set<String> getAllCurrencyCodes() {

		return iso4217CodeToCurrencyMap.keySet();
	}

}
