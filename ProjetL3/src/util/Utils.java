package util;

import java.text.ParseException;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpSession;
import entities.Utilisateur;

public class Utils {

	public static final String ATT_SESSION_USER = "sessionUtilisateur";

	/**
	 * @param date_s
	 * @param shortDate
	 * @return
	 * @throws ParseException
	 */
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

	/**
	 * Renvoie l'utilisateur en session
	 * 
	 * @param request
	 * @return
	 */
	public static Utilisateur getSessionUser(HttpServletRequest request) {
		HttpSession session = request.getSession();
		entities.Utilisateur user = (entities.Utilisateur) session.getAttribute(ATT_SESSION_USER);
		return user;
	}

	/**
	 * Met en session un utilisateur
	 * 
	 * @param request
	 * @param user
	 */
	public static void setSessionUser(HttpServletRequest request, Utilisateur user) {
		HttpSession session = request.getSession();
		session.setAttribute(ATT_SESSION_USER, user);
	}

}
