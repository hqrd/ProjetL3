package forms;

import java.util.HashMap;
import java.util.Map;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import beans.Utilisateur;

public final class ConnexionForm {

	private static final String	CHAMP_PASS	= "motdepasse";
	private static final String	CHAMP_NOM	= "nom";

	private String				resultat;
	private Map<String, String>	erreurs		= new HashMap<String, String>();

	public String getResultat() {
		return resultat;
	}

	public Map<String, String> getErreurs() {
		return erreurs;
	}

	public Utilisateur connecterUtilisateur(HttpServletRequest request) {
		String motDePasse = getValeurChamp(request, CHAMP_PASS);
		String nom = getValeurChamp(request, CHAMP_NOM);

		Utilisateur utilisateur = new Utilisateur();
		utilisateur.setMotDePasse(motDePasse);

		try {
			validationNom(nom);
		} catch (Exception e) {
			setErreur(CHAMP_NOM, e.getMessage());
		}
		utilisateur.setNom(nom);
		try {
			Connexion(utilisateur);
		} catch (Exception e) {
			setErreur(CHAMP_NOM, e.getMessage());
		}
		if (erreurs.isEmpty()) {

			resultat = "Succès de la connexion.";
		} else {
			resultat = "Echec de la connexion.";
		}
		return utilisateur;
	}

	private void validationNom(String nom) throws Exception {
		if (nom != null && nom.length() < 3) {
			throw new Exception("Le nom d'utilisateur doit contenir au moins 3 caractères.");
		}
	}

	/*
	 * Ajoute un message correspondant au champ spécifié à la map des erreurs.
	 */
	private void setErreur(String champ, String message) {
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
		try {
			Class.forName("org.hsqldb.jdbc.JDBCDriver");
		} catch (ClassNotFoundException e) {
			/* Gérer les éventuelles erreurs ici. */
		}

		/* Connexion à la base de données */
		String url = "jdbc:hsqldb:hsql://localhost/";
		String utilisateur = "SA";
		String motDePasse = "";
		Connection connexion = null;
		ResultSet resultat = null;
		PreparedStatement statement = null;
		try {
			connexion = DriverManager.getConnection(url, utilisateur, motDePasse);

			// Statement statement = connexion.createStatement();

			String pswdEnc = encode(user.getMotDePasse());

			statement = connexion.prepareStatement("SELECT * FROM UTILISATEUR WHERE NOM = ? AND PSWDENC = ?;");
			statement.setString(1, user.getNom());
			statement.setString(2, pswdEnc);
			resultat = statement.executeQuery();

			statement.close();
			resultat.close();

			// resultat = statement.executeQuery(sql);

			// boolean flag = false;
			if (!resultat.next()) {
				statement = connexion.prepareStatement("SELECT * FROM UTILISATEUR WHERE NOM = ?;");
				statement.setString(1, user.getNom());
				resultat = statement.executeQuery();

				if (resultat.next()) {
					throw new Exception("Le mot de passe est incorrect");
				} else {
					throw new Exception("Le nom d'utilisateur est invalide");
				}
			}

			// while (resultat.next()) {
			// System.out.println("Found=" + resultat.getString("PSWDENC"));
			//
			// String pswd = resultat.getString("PSWDENC");
			// if (pswd.equals(pswdEnc)) {
			// flag = true;
			// } else {
			// System.out.println("mauvais mot de passe");
			// throw new Exception("Le mot de passe est incorrect");
			// }
			// // System.out.println(nom+" "+test);
			// }
			// if (flag) {
			// } else {
			// System.out.println("Aucun resultat");
			// throw new Exception("Le nom d'utilisateur est invalide");
			// }

		} catch (SQLException e) {
			throw new Exception("erreur : " + e.getMessage());
		} finally {
			if (resultat != null) {
				try {
					/* Fermeture de l'objet ResultSet */
					resultat.close();
				} catch (SQLException ignore) {
				}
			}
			if (statement != null) {
				try {
					/* Fermeture de l'objet Statement */
					statement.close();
				} catch (SQLException ignore) {
				}
			}
			if (connexion != null)
				try {
					/* Fermeture de la connexion */
					connexion.close();
				} catch (SQLException ignore) {
					/* Si une erreur survient lors de la fermeture, il suffit de l'ignorer. */
				}
		}
	}

	/*
	 * Coder les mots de passe
	 */
	private static String encode(String password) {
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