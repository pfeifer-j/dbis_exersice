package de.dis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Kleine Helferklasse zum Einlesen von Formulardaten
 */
public class FormUtil {
	/**
	 * Liest einen String vom standard input ein
	 * 
	 * @param label Zeile, die vor der Eingabe gezeigt wird
	 * @return eingelesene Zeile
	 */
	public static String readString(String label) {
		String ret = null;
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

		try {
			System.out.print(label + ": ");
			ret = stdin.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ret;
	}

	/**
	 * Liest einen Integer vom standard input ein
	 * 
	 * @param label Zeile, die vor der Eingabe gezeigt wird
	 * @return eingelesener Integer
	 */
	public static int readInt(String label) {
		int ret = 0;
		boolean finished = false;

		while (!finished) {
			String line = readString(label);

			try {
				ret = Integer.parseInt(line);
				finished = true;
			} catch (NumberFormatException e) {
				System.err.println("Ungültige Eingabe: Bitte geben Sie eine Zahl an!");
			}
		}

		return ret;
	}

	/**
	 * Liest eine Dezimalzahl vom standard input ein
	 * 
	 * @param label Zeile, die vor der Eingabe gezeigt wird
	 * @return eingelesene Dezimalzahl
	 */
	public static double readDouble(String label) {
		double ret = 0.0;
		boolean finished = false;

		while (!finished) {
			String line = readString(label);

			try {
				ret = Double.parseDouble(line);
				finished = true;
			} catch (NumberFormatException e) {
				System.err.println("Ungültige Eingabe: Bitte geben Sie eine Dezimalzahl an!");
			}
		}

		return ret;
	}

	/**
	 * Liest einen Boolean-Wert vom standard input ein
	 * 
	 * @param label Zeile, die vor der Eingabe gezeigt wird
	 * @return eingelesener Boolean-Wert
	 */
	public static boolean readBoolean(String label) {
		boolean ret = false;
		boolean finished = false;

		while (!finished) {
			String line = readString(label).toLowerCase(); // Convert input to lowercase for case-insensitivity

			if (line.equals("true") || line.equals("false")) {
				ret = Boolean.parseBoolean(line);
				finished = true;
			} else {
				System.err.println("Ungültige Eingabe: Bitte geben Sie 'true' oder 'false' an!");
			}
		}

		return ret;
	}

	/**
	 * Liest ein Datum vom standard input ein
	 *
	 * @param label Zeile, die vor der Eingabe gezeigt wird
	 * @return eingelesenes Datum
	 */
	public static Date readDate(String label) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date ret = null;
		boolean finished = false;

		while (!finished) {
			String line = readString(label);

			try {
				ret = new Date(dateFormat.parse(line).getTime());
				finished = true;
			} catch (ParseException e) {
				System.err.println("Ungültige Eingabe: Bitte geben Sie das Datum im Format 'yyyy-MM-dd' an!");
			}
		}

		return ret;
	}
}
