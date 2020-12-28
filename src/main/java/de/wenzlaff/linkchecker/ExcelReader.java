package de.wenzlaff.linkchecker;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

/**
 * Basis Excel Reader für CSV Dateien.
 * 
 * 
 * @author Thomas Wenzlaff
 */
public class ExcelReader {

	private static final char TRENNZEICHEN = ';';

	/**
	 * Liest die Excel Datei ein.
	 * 
	 * @param dateiUri die File URI
	 * @return der CSVParser
	 * @throws IOException bei lese Fehler
	 */
	public CSVParser read(URI dateiUri) throws IOException {

		BufferedReader reader = Files.newBufferedReader(Paths.get(dateiUri));

		try (CSVParser parser = new CSVParser(reader, CSVFormat.EXCEL.withDelimiter(TRENNZEICHEN).withHeader().withIgnoreHeaderCase().withTrim())) {
			return parser;
		}
	}
}