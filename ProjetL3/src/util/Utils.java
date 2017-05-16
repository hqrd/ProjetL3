package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

	public static String formatDate(String date_s, boolean shortDate) throws ParseException {

		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = dt.parse(date_s);

		if (shortDate) {
			SimpleDateFormat dt1 = new SimpleDateFormat("dd-MM-yyyy");
			return dt1.format(date);
		}

		SimpleDateFormat dt2 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		return dt2.format(date);
	}

}
