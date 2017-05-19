package forms;

import java.util.HashMap;
import java.util.Map;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import beans.Utilisateur;
import listener.LocalEntityManagerFactory;
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

	public entities.Utilisateur connecterUtilisateur(HttpServletRequest request) {
		resetErrors();

		String motDePasse = getValeurChamp(request, CHAMP_PASS);
		String email = getValeurChamp(request, CHAMP_EMAIL);

		try {
			validationEmail(email);
		} catch (Exception e) {
			setErreur(CHAMP_EMAIL, e.getMessage());
		}
		entities.Utilisateur utilisateur = null;
		try {
			utilisateur = Connexion(email, motDePasse);
		} catch (Exception e) {
			e.printStackTrace();
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

	private static entities.Utilisateur Connexion(String email, String motDePasse) throws Exception {

		EntityManager em = LocalEntityManagerFactory.createEntityManager();

		String pswdEnc = encode(motDePasse);

		Query requete = em
				.createQuery("SELECT u FROM Utilisateur u WHERE u.email=:email AND u.motDePasse=:motDePasse ");
		requete.setParameter("email", email);
		requete.setParameter("motDePasse", pswdEnc);
		entities.Utilisateur utilisateur = null;
		boolean found = true;
		try {
			utilisateur = (entities.Utilisateur) requete.getSingleResult();
		} catch (NoResultException e) {
			found = false;
		}
		
		if (found)
			return utilisateur;

		// si pas de résultat on vérifie si l'email existe
		requete = em.createQuery("SELECT * FROM Utilisateur u WHERE u.email=:email");
		requete.setParameter("email", email);
		boolean foundEmail = true;
		try {
			utilisateur = (entities.Utilisateur) requete.getSingleResult();
		} catch (NoResultException e) {
			foundEmail = false;
		}
		if (foundEmail)
			setErreur(CHAMP_PASS, "Le mot de passe est incorrect");
		else
			setErreur(CHAMP_EMAIL, "L'email est invalide");

		return null;
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