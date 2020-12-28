package de.wenzlaff.linkchecker;

import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * Klasse zum testen der URLs einer Excel-Datei auf Gültigkeit.
 * 
 * Es werden alle falschen URLs ausgegeben.
 * 
 * @author Thomas Wenzlaff
 *
 */
@Command(name = "CheckExcelUrls", mixinStandardHelpOptions = true, version = "CheckExcelUrls 1.0", description = "Untersucht eine Exceldatei auf gültige URLs in einer Spalte", footer = {
		"@|fg(green) Thomas Wenzlaff|@", "@|fg(red),bold http://www.wenzlaff.info|@" })
public class CheckExcelUrls implements Callable<Integer> {

	private static final Logger LOG = LogManager.getLogger(CheckExcelUrls.class);

	private static final int TIMEOUT_IN_MILLISEKUNDEN = 5000;
	private static final String TRENNZEICHEN = ", ";
	private static final int SPALTE_ID = 0;
	private static final String STATUS_ERROR = "ERROR";

	private static List<Zeile> zeilen;

	@Option(names = { "-s", "--spaltennummer" }, description = "die Spalte Nummer die verwendet werden soll", defaultValue = "28")
	private int spaltenNumme;

	@Option(names = { "-f", "--execldateiname" }, description = "der Dateiname der Exceldatei die untersucht werden soll", defaultValue = "exceldatei.xlsx")
	private String excelDateiName;

	/**
	 * Start des Url Checker.
	 * 
	 * @param args Spalte die Überprüft werden soll und der Dateiname Aufruf z.B.:
	 * 
	 *             de.wenzlaff.linkchecker.CheckExcelUrls -s 28 -f
	 *             /Users/TWLinkChecker/exceldatei.xlsx
	 * 
	 * @throws Exception alle Fehler
	 */
	public static void main(String[] args) throws Exception {

		int exitCode = new CommandLine(new CheckExcelUrls()).execute(args);
		System.exit(exitCode);
	}

	@Override
	public Integer call() throws Exception {
		zeilen = new ArrayList<>();

		LOG.info("Lese alle Zeilen aus der Excel Datei " + excelDateiName);
		LOG.info("Validiere die " + spaltenNumme + ". Spalte in der Excel-Datei mit Namen: " + CellReference.convertNumToColString(spaltenNumme));

		try (FileInputStream inputStream = new FileInputStream(new File(excelDateiName))) {

			try (Workbook workbook = new XSSFWorkbook(inputStream)) {
				Sheet firstSheet = workbook.getSheetAt(0);
				Iterator<Row> iterator = firstSheet.iterator();

				int maxSpalten = firstSheet.getRow(0).getLastCellNum();
				LOG.info("Anzahl der Spalten der Tabelle: " + maxSpalten);
				String sheetName = firstSheet.getSheetName();
				LOG.info("Verwende Blatt " + sheetName);

				while (iterator.hasNext()) { // über alle Zeilen
					Row nextRow = iterator.next();
					Iterator<Cell> cellIterator = nextRow.cellIterator();

					Zeile zeile = new Zeile();

					while (cellIterator.hasNext()) {
						Cell nextCell = cellIterator.next();
						int columnIndex = nextCell.getColumnIndex();

						if (columnIndex == SPALTE_ID) { // Spaltennummer muss vorhanden sein
							// Entferne .0 da Spalte evl. als Zahl mit Nachkomma formatiert
							Object wert = getCellValue(nextCell);
							if (wert != null) {
								String replace = wert.toString().replace(".0", "");
								zeile.setId(replace);
							} else {
								zeile.setId("");
							}
						} else if (columnIndex == spaltenNumme) {
							zeile.setUrl((String) getCellValue(nextCell));
						}
					}
					try {
						if (isTitelzeile(zeile)) {
							zeilen.add(zeile);
							LOG.info("Eingelesen " + zeile);
						}
					} catch (Exception e) {
						LOG.error("Fehler in Zeile: " + zeile + " Exception:" + e);
					}
				}
			}
		}

		LOG.info(zeilen.size() + " gelesene Zeilen aus der Tabelle " + excelDateiName);
		LOG.info("Checke nun den Online Status aller URLs ...");

		checkOnlineStatus();

		LOG.info("Online Check abgeschlossen.");

		return 0;
	}

	private static boolean isTitelzeile(Zeile zeile) {
		// keine Titelzeile einlesen, das heisst überprüfe auf Nr in erster Spalte!
		return zeile.getId() != null && !zeile.getId().equals("Nr");
	}

	synchronized private static void checkOnlineStatus() {

		int fehlerNr = 1;

		for (Iterator<Zeile> zeilenIterator = zeilen.iterator(); zeilenIterator.hasNext();) {
			Zeile zeile = zeilenIterator.next();

			URL webseite = null;

			try {
				webseite = new URL(zeile.getUrl());

				if (getStatus(webseite.toString()).contains(STATUS_ERROR)) {
					LOG.error("Fehler Nr. " + fehlerNr + " ZeilenId: " + zeile.getId() + "\t" + getStatus(webseite.toString()));
					fehlerNr++;
				}
			} catch (Exception e) {
				LOG.error("Fehler Nr. " + fehlerNr + " Fehler " + e.getMessage() + " in Zeile: " + zeile + " mit URL: " + webseite);
				fehlerNr++;
			}
		}
	}

	/**
	 * Gibt den Status in der Form:
	 * 
	 * <pre>
	 	50	 ERROR,    , www.kleinhirn
		147	 ERROR, 400, http://www.klein hirn.eu
	 * </pre>
	 * 
	 * @param url die zu testende URL
	 * @return der Status
	 */
	private static String getStatus(String url) {

		String result = "";
		try {
			URL siteURL = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) siteURL.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(TIMEOUT_IN_MILLISEKUNDEN);
			connection.connect();

			int code = connection.getResponseCode();
			if (code == HttpURLConnection.HTTP_OK || code <= HttpURLConnection.HTTP_USE_PROXY) {
				result = " OK, " + code + TRENNZEICHEN;
			} else if (code >= HttpURLConnection.HTTP_BAD_REQUEST || code <= HttpURLConnection.HTTP_VERSION) {
				result = " " + STATUS_ERROR + ", " + code + TRENNZEICHEN + url;
			} else {
				result = TRENNZEICHEN + code + TRENNZEICHEN;
			}
		} catch (Exception e) {
			result = " " + STATUS_ERROR + ",    , " + e.getMessage();
		}
		return result;
	}

	private static Object getCellValue(Cell cell) {

		if (cell.getCellTypeEnum() == CellType.STRING) {
			return cell.getStringCellValue();
		} else if (cell.getCellTypeEnum() == CellType.BOOLEAN) {
			return cell.getBooleanCellValue();
		} else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
			return cell.getNumericCellValue();
		}
		return null;
	}
}