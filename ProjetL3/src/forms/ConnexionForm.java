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
import beans.Utilisateur;
import util.SqlUtil;

public final class ConnexionForm {

	private static final String			CHAMP_PASS	= "motdepasse";
	private static final String			CHAMP_EMAIL	= "email";

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

	public Utilisateur connecterUtilisateur(HttpServletRequest request) {
		resetErrors();

		String motDePasse = getValeurChamp(request, CHAMP_PASS);
		String email = getValeurChamp(request, CHAMP_EMAIL);

		Utilisateur utilisateur = new Utilisateur();
		utilisateur.setMotDePasse(motDePasse);

		try {
			validationEmail(email);
		} catch (Exception e) {
			setErreur(CHAMP_EMAIL, e.getMessage());
		}
		utilisateur.setEmail(email);
		try {
			Connexion(utilisateur);
		} catch (Exception e) {
			//
		}
		return utilisateur;
	}

	private void validationEmail(String email) throws Exception {
		if (!email.matches("([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)")) {
			throw new Exception("Merci de saisir une adresse mail valide.");
		}
	}

	/*
	 * Ajoute un message correspondant au champ spécifié à la map des erreurs.
	 */
	private static void setErreur(String champ, String message) {
		erreurs.put(champ, message);
	}

	/*
	 * Méthode utilitaire qui retourne null si un champ est vide, et son contenu
	 * sinon.
	 */
	private static String getValeurChamp(HttpServletRequest request, String nomChamp) {
		String valeur = request.getParameter(nomChamp);
		if (valeur == null || valeur.trim().length() == 0) {
			return null;
		} else {
			return valeur.trim();
		}
	}

	private static void Connexion(Utilisateur user) throws Exception {

		Connection connexion = null;
		ResultSet resultat = null;
		PreparedStatement statement = null;
		try {
			connexion = SqlUtil.getConnection();

			String pswdEnc = encode(user.getMotDePasse());

			statement = connexion.prepareStatement("SELECT * FROM UTILISATEUR WHERE EMAIL = ? AND PSWDENC = ?;");
			statement.setString(1, user.getEmail());
			statement.setString(2, pswdEnc);

			resultat = statement.executeQuery();

			if (!resultat.next()) {
				statement.close();
				resultat.close();
				statement = connexion.prepareStatement("SELECT * FROM UTILISATEUR WHERE EMAIL = ?;");
				statement.setString(1, user.getEmail());
				resultat = statement.executeQuery();

				if (resultat.next()) {
					setErreur(CHAMP_PASS, "Le mot de passe est incorrect");
					throw new Exception();
				} else {
					setErreur(CHAMP_EMAIL, "L'email est invalide");
					throw new Exception();
				}
			} else {
				user.setNom(resultat.getString("nom"));
				user.setPrenom(resultat.getString("prenom"));
			}

		} catch (SQLException e) {
			throw new Exception("erreur : " + e.getMessage());
		} finally {
			SqlUtil.close(resultat);
			SqlUtil.close(statement);
			SqlUtil.close(connexion);
		}
	}

	/*
	 * Coder les mots de passe
	 */
	private static String encode(String password) {
		if (password == null)
			return null;
		if (password == "")
			return "";
		byte[] uniqueKey = password.getBytes();
		byte[] hash = null;
		try {
			hash = MessageDigest.getInstance("MD5").digest(uniqueKey);
		} catch (NoSuchAlgorithmException e) {
			throw new Error("No MD5 support in this VM.");
		}

		StringBuilder hashString = new StringBuilder();
		for (int i = 0 ; i < hash.length ; i++) {
			String hex = Integer.toHexString(hash[i]);
			if (hex.length() == 1) {
				hashString.append('0');
				hashString.append(hex.charAt(hex.length() - 1));
			} else
				hashString.append(hex.substring(hex.length() - 2));
		}
		return hashString.toString();
	}
}