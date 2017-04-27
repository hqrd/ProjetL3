package forms;

import java.util.HashMap;
import java.util.Map;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import beans.Objet;
import beans.Utilisateur;
import util.SqlUtil;

public final class AjouterForm {

	private static final String			CHAMP_ERREUR	= "erreur";

	private String						resultat;
	private static Map<String, String>	erreurs		= new HashMap<String, String>();

	public String getResultat() {
		return resultat;
	}

	public Map<String, String> getErreurs() {
		return erreurs;
	}

	public void resetErrors() {
		erreurs.clear();
	}

	public void validation(HttpServletRequest request)
	{
		Objet o = new Objet();
		
		try
		{
			Objet.ajouterBDD(request);
		}
		catch(SQLException e)
		{
			setErreur(CHAMP_ERREUR, e.getMessage());
		}
		
	}
	/*
	 * Ajoute un message correspondant au champ spécifié à la map des erreurs.
	 */
	private static void setErreur(String champ, String message) {
		erreurs.put(champ, message);
	}

	




}