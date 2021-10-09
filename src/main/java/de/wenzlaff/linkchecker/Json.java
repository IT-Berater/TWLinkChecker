package de.wenzlaff.linkchecker;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.json.JSONObject;

/**
 * Verwaltung des Json Exports.
 * 
 * @author Thomas Wenzlaff
 *
 */
public class Json {

	private static final Logger LOG = LogManager.getLogger(Json.class);

	/**
	 * Die Reihenfolge Spalten der Excel Datei
	 * 
	 * <pre>
	 * // 0 Nr
	 * //		1		Titel
	 * //		2		Name
	 * //		3		Vorname
	 * //		4		Funktion
	 * //  5			Fachgebiet
	 * //	6			weiteres Fachgebiet 1
	 * //	7			weiteres Fachgebiet 2
	 * //	8			weiteres Fachgebiet 3
	 * //	     9		Erwachsene Plan
	 * //		10		Erwachsene Not
	 * //		11		Kinder Plan
	 * //		12		Kinder Not
	 * //		13		Kleinkinder Plan
	 * //		14		Kleinkinder Not
	 * //	15			Seit wann koop.
	 * //	16			letzter Kontakt
	 * //	17			Klinik
	 * //	18			Strasse + Nr.
	 * //	19			PLZ
	 * //	20			Ort
	 * //	21			Tel Nr
	 * //	22			Notfalltelefon
	 * //	23			Anästh. kooperativ 
	 * //	24			Klinik 2
	 * //	25			RAB
	 * //	26			Stand RAB
	 * //	27			Belegarzt (Ja/Nein)
	 * // 28			Link Klinikseite
	 * // 29			Kommentar
	 * // 30			Spezielle Kompetenz
	 * // 31			Suchbegriffe
	 * // 32				Zuständiger Betreuer
	 * // 33				Focus
	 * </pre>
	 * 
	 * @param iterator        Zeilen iterator
	 * @param exportDateiname der Dateiname
	 */
	public static void writeJson(Iterator<Row> iterator, String exportDateiname) {

		List<JSONObject> aerzte = new ArrayList<>();

		Set<String> checkNr = new HashSet<>();

		while (iterator.hasNext()) { // über alle Zeilen
			Row nextRow = iterator.next();
			Iterator<Cell> cellIterator = nextRow.cellIterator();

			JSONObject json = getSortJSONObject();

			while (cellIterator.hasNext()) {
				Cell nextCell = cellIterator.next();

				int columnIndex = nextCell.getColumnIndex();

				if (columnIndex == 0) {
					Object wert = getCellValue(nextCell);
					if (wert != null) {
						String replace = wert.toString().replace(".0", "");
						json.put("arztNr", replace);

						boolean isVorhanden = checkNr.add(wert.toString());
						if (!isVorhanden) {
							String nachricht = "Wert schon vorhanden mit ID: " + replace;

							LOG.error(nachricht);
							throw new IllegalArgumentException(nachricht);
						}
					} else {
						json.put("arztNr", "");
					}
				}
				if (columnIndex == 1) {
					json.put("arztTitel", nextCell.getStringCellValue());
				}
				if (columnIndex == 2) {
					json.put("arztName", nextCell.getStringCellValue());
				}
				if (columnIndex == 3) {
					json.put("arztVorname", nextCell.getStringCellValue());
				}
				if (columnIndex == 4) {
					json.put("arztFunktion", nextCell.getStringCellValue());
				}
				if (columnIndex == 5) {
					json.put("fachgebiet", nextCell.getStringCellValue());
				}
				if (columnIndex == 6) {
					json.put("fachgebiet1", nextCell.getStringCellValue());
				}
				if (columnIndex == 7) {
					json.put("fachgebiet2", nextCell.getStringCellValue());
				}
				if (columnIndex == 8) {
					json.put("fachgebiet3", nextCell.getStringCellValue());
				}
				if (columnIndex == 9) {
					json.put("erwPlan", nextCell.getStringCellValue());
				}
				if (columnIndex == 10) {
					json.put("erwNot", nextCell.getStringCellValue());
				}
				if (columnIndex == 11) {
					json.put("kindPlan", nextCell.getStringCellValue());
				}
				if (columnIndex == 12) {
					json.put("kindNot", nextCell.getStringCellValue());
				}
				if (columnIndex == 13) {
					json.put("kleinKindPlan", nextCell.getStringCellValue());
				}
				if (columnIndex == 14) {
					json.put("kleinKindNot", nextCell.getStringCellValue());
				}
				if (columnIndex == 15) {
					json.put("arztKoSeit", "" + getCellValue(nextCell));
				}
				if (columnIndex == 16) {
					json.put("letzterKontakt", "" + getCellValue(nextCell));
				}
				if (columnIndex == 17) {
					json.put("krhsName", nextCell.getStringCellValue());
				}
				if (columnIndex == 18) {
					json.put("krhsStrNr", nextCell.getStringCellValue());
				}
				if (columnIndex == 19) {
					Object wert = getCellValue(nextCell);
					if (wert != null) {
						String replace = wert.toString().replace(".0", "");
						json.put("krhsPlz", replace);
					} else {
						json.put("krhsPlz", "");
					}
				}
				if (columnIndex == 20) {
					json.put("krhsOrt", nextCell.getStringCellValue());
				}
				if (columnIndex == 21) {
					json.put("krhsTelNr", nextCell.getStringCellValue());
				}
				if (columnIndex == 22) {
					json.put("arztNotTel", nextCell.getStringCellValue());
				}
				if (columnIndex == 23) {
					json.put("anästhesieKooperativ", nextCell.getStringCellValue());
				}
				if (columnIndex == 24) {
					json.put("klinik2", nextCell.getStringCellValue());
				}
				if (columnIndex == 25) {
					json.put("rab", nextCell.getStringCellValue());
				}
				if (columnIndex == 26) {
					json.put("rabStand", nextCell.getStringCellValue());
				}
				if (columnIndex == 27) {
					json.put("belegArzt", nextCell.getStringCellValue());
				}
				if (columnIndex == 28) {
					json.put("krhsUrl", nextCell.getStringCellValue());
				}
				if (columnIndex == 29) {
					json.put("kommentar", nextCell.getStringCellValue());
				}
				if (columnIndex == 30) {
					json.put("arztSpezielleKompetenz", nextCell.getStringCellValue());
				}
				if (columnIndex == 31) {
					json.put("arztSuchbegriffe", nextCell.getStringCellValue());
				}
				if (columnIndex == 32) {
					json.put("betreuer", nextCell.getStringCellValue());
				}
				if (columnIndex == 33) {
					json.put("focus", getCellValue(nextCell));
				}
			}
			if (isZeileValid(json)) {
				aerzte.add(json);
			}
		}
		try (FileWriter file = new FileWriter(exportDateiname)) {
			file.write(aerzte.toString());
			file.flush();
		} catch (IOException e) {
			LOG.error(e.getLocalizedMessage());
		}

	}

	/**
	 * Nur Sätzte mit arztNr übernehmen, kann vorkommen wenn die Tabelle noch evl.
	 * leere Zeilen enthält.
	 * 
	 * @param json
	 * @return
	 */
	private static boolean isZeileValid(JSONObject json) {
		return json.has("arztNr");
	}

	private static JSONObject getSortJSONObject() {
		JSONObject json = new JSONObject();
		// magic zum sortieren der Parameter, so werden die Felder so
		// geschrieben wie hinzugefügt also sortiert
		try {
			// das Feld ist in der JSONObject Klasse so def.:
			// private final Map<String, Object> map;
			// wir machen ein neues Objekt
			Field neueMap = json.getClass().getDeclaredField("map");
			// beschreibbar machen da private
			neueMap.setAccessible(true);
			// nun mit LinkedHashMap anstatt HashMap<String, Object>();
			neueMap.set(json, new LinkedHashMap<>());
			// Zugriff wieder unbeschreibbar, sicher ist sicher
			neueMap.setAccessible(false);
		} catch (IllegalAccessException | NoSuchFieldException e) {
			LOG.error("Error: " + e.getLocalizedMessage());
		}
		return json;
	}

	public static Object getCellValue(Cell cell) {

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
