package de.wenzlaff.linkchecker;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import picocli.CommandLine;

/**
 * Testklasse für den URL Checker.
 * 
 * @author Thomas Wenzlaff
 */
public class CheckExcelUrlsTest {

	private static final String EXCEL_LISTE = "src/test/resources/de/wenzlaff/linkchecker/test.xlsx";

	@Test
	public void testHilfeText() throws Exception {

		CheckExcelUrls app = new CheckExcelUrls();
		CommandLine cmd = new CommandLine(app);

		int exitCode = cmd.execute("--Help");
		assertEquals(2, exitCode);
	}

	@Test
	public void testVersionText() throws Exception {

		CheckExcelUrls app = new CheckExcelUrls();
		CommandLine cmd = new CommandLine(app);

		int exitCode = cmd.execute("--Version");
		assertEquals(2, exitCode);
	}

	@Test
	public void testMainPositivTest() throws Exception {

		CheckExcelUrls app = new CheckExcelUrls();
		CommandLine cmd = new CommandLine(app);

		int exitCode = cmd.execute("-s", "25", "-f", EXCEL_LISTE);
		assertEquals(0, exitCode);
	}

	@Test
	public void testMainDefaultValue() throws Exception {

		CheckExcelUrls app = new CheckExcelUrls();
		CommandLine cmd = new CommandLine(app);

		int exitCode = cmd.execute();
		assertEquals(1, exitCode);
	}
}