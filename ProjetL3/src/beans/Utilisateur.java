package beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import util.SqlUtil;

/**
 * @deprecated Utilisation de JPA
 */
@Deprecated
public class Utilisateur {
	private Integer	id;
	private String	nom;
	private String	prenom;
	private String	email;
	private String	motDePasse;
	private String	role;

	public Utilisateur(String nom, String prenom, String email, String motDePasse, String role) {
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.motDePasse = motDePasse;
		this.role = role;
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

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String toString() {
		return "Email : " + email + " nom : " + nom + " prenom : " + prenom;
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
						resultat.getString("email"), resultat.getString("pswdenc"), resultat.getString("role"));

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

	/**
	 * Récupère l'ID d'un utilisateur en BDD si il n'est pas dans l'Objet
	 * 
	 * @return l'id d'un utilisateur, ou null si pas trouvé
	 * @throws Exception
	 */
	public Integer getId() {
		if (this.id != null) {
			return this.id;
		} else {
			Connection connexion = null;
			ResultSet resultat = null;
			PreparedStatement statement = null;
			try {
				connexion = SqlUtil.getConnection();

				statement = connexion.prepareStatement("SELECT * FROM Utilisateur WHERE email = ?");
				statement.setString(1, this.getEmail());
				resultat = statement.executeQuery();

				if (resultat.next()) {
					this.id = resultat.getInt("ID");
					return this.id;
				} else
					return null;
			} catch (SQLException e) {
				try {
					throw new Exception("erreur : " + e.getMessage());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			} finally {
				SqlUtil.close(resultat);
				SqlUtil.close(statement);
				SqlUtil.close(connexion);
			}
		}
		return id;
	}

	/**
	 * Renvoie le nombre d'objets reservés par un utilisateur en fonction du nom de l'objet
	 * 
	 * @param intitule
	 * @return le nombre d'objets reservés
	 * @throws Exception
	 */
	public int getObjets(String intitule) throws Exception {
		Connection connexion = null;
		ResultSet resultat = null;
		PreparedStatement statement = null;
		try {
			connexion = SqlUtil.getConnection();

			statement = connexion.prepareStatement(
					"SELECT SUM(qtite_emprunt) as nb_res FROM emprunt WHERE ID_USER = ? AND ID_OBJET = ? AND rendu = false");
			statement.setInt(1, this.getId());
			statement.setInt(2, Objet.findByIntitule(intitule).getId());
			resultat = statement.executeQuery();

			if (resultat.next()) {
				return resultat.getInt("nb_res");
			} else
				return 0;
		} catch (SQLException e) {
			throw new Exception("erreur : " + e.getMessage());
		} finally {
			SqlUtil.close(resultat);
			SqlUtil.close(statement);
			SqlUtil.close(connexion);
		}
	}

	public boolean isAdmin() {
		return (this.getRole().equals("admin"));
	}

}