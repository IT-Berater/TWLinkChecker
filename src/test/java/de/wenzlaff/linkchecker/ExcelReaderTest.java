package de.wenzlaff.linkchecker;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;

/**
 * Testklasse für den Excelreader.
 * 
 * @author Thomas Wenzlaff
 */
public class ExcelReaderTest {

	@Test
	public void testRead() throws Exception {

		ExcelReader reader = new ExcelReader();

		File f = new File("src/test/resources/de/wenzlaff/linkchecker/test.csv");

		CSVParser csvParser = reader.read(f.toURI());

		System.out.println("Parser: " + csvParser);

		printRecords(csvParser);

		System.out.println("Parser: " + reader);
	}

	private void printRecords(CSVParser csvParser) {
		for (CSVRecord csvRecord : csvParser) {

			// Zugriff über Index
			String id = csvRecord.get(0);

			// Zugriff über Titel Namen
			String name = csvRecord.get("Name");

			// Test Name über Titel und Index
			assertEquals(name, csvRecord.get(2));

			// Printing the record
			System.out.println("Record Number - " + csvRecord.getRecordNumber());
			System.out.println("Name : " + name);
			System.out.println("Nr.  : " + id);
			System.out.println("\n");
			System.out.println(csvRecord);
		}
	}
}