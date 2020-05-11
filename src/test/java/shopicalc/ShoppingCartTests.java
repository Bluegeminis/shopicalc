package shopicalc;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.nextbigthing.currency.Currency;
import com.nextbigthing.currency.CurrencyFactory;
import com.nextbigthing.exceptions.CurrencyMismatchException;
import com.nextbigthing.exceptions.SystemConfigurationException;
import com.nextbigthing.exceptions.UnidentifiedCurrencyException;
import com.nextbigthing.exceptions.UnknownCurrencyConversionException;
import com.nextbigthing.shopping.Product;
import com.nextbigthing.shopping.ShoppingCart;
import com.nextbigthing.shopping.Value;

public class ShoppingCartTests {

	private static Currency CURRENCY_USD;
	private static Currency CURRENCY_SWEDISH_KRONA;

	private final static float EXPECTED_KRONA_TO_USD_EXCHANGE_RATE = 1 / 8.86f;
	private final static float EXPECTED_USD_TO_KRONA_EXCHANGE_RATE = 8.86f;

	@BeforeClass
	public static void init() throws SystemConfigurationException {
		CurrencyFactory currencyFactory = CurrencyFactory.getInstance();
		CURRENCY_USD = currencyFactory.getCurrencyByCode("USD");
		CURRENCY_SWEDISH_KRONA = currencyFactory.getCurrencyByCode("SEK");
	}

	@Test
	public void testAddingUsdAndKronaWithUsdTarget() throws Exception {

		try {
			ShoppingCart cart = new ShoppingCart();

			int currencylessUnits = 5;
			float expectedTotal = currencylessUnits + currencylessUnits * EXPECTED_KRONA_TO_USD_EXCHANGE_RATE;

			cart.addItem(new Product("Hand Cream", new Value(CURRENCY_USD, currencylessUnits)));
			cart.addItem(new Product("Soap", new Value(CURRENCY_SWEDISH_KRONA, currencylessUnits)));

			Value sum = cart.sumCost(CURRENCY_USD);

			assertEquals(expectedTotal, sum.getAmount().floatValue(), 0.5);
		} catch (UnidentifiedCurrencyException | CurrencyMismatchException | UnknownCurrencyConversionException
				| SystemConfigurationException e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Test
	public void testAddingUsdAndUsdWithUsdTarget() throws Exception {

		try {
			ShoppingCart cart = new ShoppingCart();

			int currencylessUnits = 5;
			float expectedTotal = currencylessUnits + currencylessUnits;

			cart.addItem(new Product("Hand Cream", new Value(CURRENCY_USD, currencylessUnits)));
			cart.addItem(new Product("Soap", new Value(CURRENCY_USD, currencylessUnits)));

			Value sum = cart.sumCost(CURRENCY_USD);

			assertEquals(expectedTotal, sum.getAmount().floatValue(), 0.5);
		} catch (UnidentifiedCurrencyException | CurrencyMismatchException | UnknownCurrencyConversionException
				| SystemConfigurationException e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Test
	public void testSubstractingKronaFromUsdWithUsdTarget() throws Exception {

		try {
			ShoppingCart cart = new ShoppingCart();

			int currencylessUnits = 5;
			float expectedTotal = currencylessUnits - (currencylessUnits * EXPECTED_KRONA_TO_USD_EXCHANGE_RATE);

			cart.addItem(new Product("Hand Cream", new Value(CURRENCY_USD, currencylessUnits)));
			cart.addItem(new Product("Soap", new Value(CURRENCY_SWEDISH_KRONA, -currencylessUnits)));

			Value sum = cart.sumCost(CURRENCY_USD);

			assertEquals(expectedTotal, sum.getAmount().floatValue(), 0.5);
		} catch (UnidentifiedCurrencyException | CurrencyMismatchException | UnknownCurrencyConversionException
				| SystemConfigurationException e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Test
	public void testAddingUsdAndKronaWithKronaTarget() throws Exception {

		try {
			ShoppingCart cart = new ShoppingCart();

			int currencylessUnits = 5;
			float expectedTotalInKrona = (currencylessUnits * EXPECTED_USD_TO_KRONA_EXCHANGE_RATE) + currencylessUnits;

			cart.addItem(new Product("Hand Cream", new Value(CURRENCY_USD, currencylessUnits)));
			cart.addItem(new Product("Soap", new Value(CURRENCY_SWEDISH_KRONA, currencylessUnits)));

			Value sum = cart.sumCost(CURRENCY_SWEDISH_KRONA);

			assertEquals(expectedTotalInKrona, sum.getAmount().floatValue(), 0.5);
		} catch (UnidentifiedCurrencyException | CurrencyMismatchException | UnknownCurrencyConversionException
				| SystemConfigurationException e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Test
	public void testAddingSingleUsdItemWithKronaTarget() throws Exception {

		try {
			ShoppingCart cart = new ShoppingCart();

			int currencylessUnits = 5;
			float expectedTotalInKrona = currencylessUnits * EXPECTED_USD_TO_KRONA_EXCHANGE_RATE;

			cart.addItem(new Product("Hand Cream", new Value(CURRENCY_USD, currencylessUnits)));

			Value sum = cart.sumCost(CURRENCY_SWEDISH_KRONA);

			assertEquals(expectedTotalInKrona, sum.getAmount().floatValue(), 0.5);
		} catch (UnidentifiedCurrencyException | CurrencyMismatchException | UnknownCurrencyConversionException
				| SystemConfigurationException e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Test
	public void testAddingSingleKronaItemWithUsdTarget() throws Exception {

		try {
			ShoppingCart cart = new ShoppingCart();

			int currencylessUnits = 5;
			float expectedTotalInUsd = currencylessUnits * EXPECTED_KRONA_TO_USD_EXCHANGE_RATE;

			cart.addItem(new Product("Hand Cream", new Value(CURRENCY_SWEDISH_KRONA, currencylessUnits)));

			Value sum = cart.sumCost(CURRENCY_USD);

			assertEquals(expectedTotalInUsd, sum.getAmount().floatValue(), 0.5);
		} catch (UnidentifiedCurrencyException | CurrencyMismatchException | UnknownCurrencyConversionException
				| SystemConfigurationException e) {
			e.printStackTrace();
			throw e;
		}
	}

}
