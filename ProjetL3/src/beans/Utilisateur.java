package beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import util.SqlUtil;

public class Utilisateur {
	private String	nom;
	private String	prenom;
	private String	email;
	private String	motDePasse;

	public Utilisateur(String nom, String prenom, String email, String motDePasse) {
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.motDePasse = motDePasse;
	}

	public Utilisateur() {
	}

	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse;
	}

	public String getMotDePasse() {
		return motDePasse;
	}

	public String getPrenom() {
		return this.prenom;
	}

	public String getEmail() {
		return this.email;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getNom() {
		return this.nom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String toString() {
		return "Nom : " + nom + " PSWD : " + motDePasse;
	}

	public static Utilisateur findByEmail(String email) throws Exception {
		Connection connexion = null;
		ResultSet resultat = null;
		PreparedStatement statement = null;
		try {
			connexion = SqlUtil.getConnection();

			statement = connexion.prepareStatement("SELECT * FROM utilisateur WHERE email = ?");
			statement.setString(1, email);
			resultat = statement.executeQuery();

			if (resultat.next()) {
				Utilisateur utilisateur = new Utilisateur(resultat.getString("NOM"), resultat.getString("prenom"),
						resultat.getString("email"), resultat.getString("pswdenc"));

				return utilisateur;
			} else
				return null;
		} catch (SQLException e) {
			throw new Exception("erreur : " + e.getMessage());
		} finally {
			SqlUtil.close(resultat);
			SqlUtil.close(statement);
			SqlUtil.close(connexion);
		}
	}
}