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
 *
 */
public class ExcelReader {

	private CSVParser parser;

	/**
	 * Liest die Excel Datei ein.
	 * 
	 * @param uri
	 *            die File URI
	 * @return der CSVParser
	 * @throws IOException
	 *             bei lese Fehler
	 */
	public CSVParser read(URI uri) throws IOException {

		BufferedReader reader = Files.newBufferedReader(Paths.get(uri));

		parser = new CSVParser(reader, CSVFormat.EXCEL.withDelimiter(';').withHeader().withIgnoreHeaderCase().withTrim());

		return parser;
	}

	public void close() {
		try {
			parser.close();
		} catch (IOException e) {
			System.err.println("Konnte Parser nicht schließen. " + e.getMessage());
		}
	}

	@Override
	public String toString() {
		try {
			return "BasicCsvReader [Zeilen= " + parser.getRecords().size() + " Titel=" + parser.getHeaderMap().keySet() + "]";
		} catch (IOException e) {
			return "IOException " + e.getMessage();
		}
	}

}
