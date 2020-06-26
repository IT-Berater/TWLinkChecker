package de.wenzlaff.linkchecker;

import org.junit.jupiter.api.Test;

/**
 * Testklasse für den URL Checker.
 * 
 * @author Thomas Wenzlaff
 *
 */
public class CheckExcelUrlsTest {

	private static final String EXCEL_LISTE = "src/test/resources/de/wenzlaff/linkchecker/test.xlsx";

	@Test
	public void testNegMainKeineParam() throws Exception {

		String[] argv = {};

		CheckExcelUrls.main(argv);
	}

	@Test
	public void testNegMainEineParam() throws Exception {

		String[] argv = { "25" };

		CheckExcelUrls.main(argv);
	}

	@Test
	public void testNegMainDreiParam() throws Exception {

		String[] argv = { "25", EXCEL_LISTE, "42" };

		CheckExcelUrls.main(argv);
	}

	@Test
	public void testMain() throws Exception {

		String[] argv = { "25", EXCEL_LISTE };

		CheckExcelUrls.main(argv);
	}

	@Test
	public void testMainOhne() throws Exception {

		String[] argv = { "2", EXCEL_LISTE };

		CheckExcelUrls.main(argv);
	}

}
