package beans;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import util.SqlUtil;

public class Objet {

	private Integer				id;
	private String				intitule;
	private Integer				qtiterest;

	public static final String	ATT_SESSION_USER	= "sessionUtilisateur";

	public Objet(String intitule, Integer qtiterest) {
		this.intitule = intitule;
		this.qtiterest = qtiterest;
	}

	public Objet() {
	}

	public String getIntitule() {
		return intitule;
	}

	public void setIntitule(String intitule) {
		this.intitule = intitule;
	}

	public Integer getQtiterest() {
		return qtiterest;
	}

	public void setQtiterest(Integer qtiterest) {
		this.qtiterest = qtiterest;
	}

	public static void listeObjet(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Utilisateur user = (Utilisateur) session.getAttribute(ATT_SESSION_USER);

		Connection connexion = null;
		ResultSet resultat = null;
		PreparedStatement statement = null;
		try {
			connexion = SqlUtil.getConnection();

			statement = connexion.prepareStatement("select * from objet");

			resultat = statement.executeQuery();

			String message = "<table class='table table-hover'>	<thead><tr><th>Nom</th><th>Quantité</th><th>Actions	 </th></tr></thead><tbody>";
			while (resultat.next()) {
				String nom = resultat.getString("intitule");
				Integer qtite = resultat.getInt("qtiterest");
				Integer id = resultat.getInt("id");
				message += "<tr><td>" + nom + "</td><td>" + qtite + "</td><td><div class='row'><div class='col-sm-6'>";

				if (qtite > 0)
					message += "<a href='Reservation'><button type='button' class='btn btn-default btn-sm'>Réserver</button></a>";

				if (user != null) {
					try {
						if (user.getObjets(nom) > 0)
							message += "<a href='Rendre'><button type='button' class='btn btn-default btn-sm'>Rendre</button></a>";
						if (user.isAdmin()) {
							message += "</div><div class='col-sm-6'><form id='supprimer' role='form' method='post' action='admin/Supprimer'>"
									+ "<button type='submit' id='" + id + "' name='id_delete' value='" + id
									+ "' type='button' class='btn btn-danger btn-sm'>Supprimer tout les objets</button></form>";
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				message += "</div></div></td>";
			}
			message += "</tbody>";
			message += "</table>";

			request.setAttribute("tab", message);

		} catch (SQLException e) {
			/* Gérer les éventuelles erreurs ici */
		} finally {
			SqlUtil.close(resultat);
			SqlUtil.close(statement);
			SqlUtil.close(connexion);
		}
	}

	public static void rendreOjet(int id) throws Exception {
		Connection connexion = null;
		PreparedStatement statement = null;
		try {
			connexion = SqlUtil.getConnection();

			statement = connexion.prepareStatement(
					"update objet set qtiteRest = qtiteRest + (select qtite_emprunt from emprunt where id = ?)"
							+ " where id = (select id_objet from emprunt where id = ?);");

			statement.setInt(1, id);
			statement.setInt(2, id);

			statement.executeUpdate();

			SqlUtil.close(statement);

			statement = connexion.prepareStatement("update emprunt set rendu = true, date_rendu = ? where id = ?;");

			statement.setDate(1, new Date(Calendar.getInstance().getTimeInMillis()));
			statement.setInt(2, id);

			statement.executeUpdate();

		} catch (SQLException e) {
			/* Gérer les éventuelles erreurs ici */
		} finally {
			SqlUtil.close(statement);
			SqlUtil.close(connexion);
		}
	}

	public static void emprunterObjet(Utilisateur user, Integer id, Integer qtite) throws Exception {
		if (id == 0)
			throw new Exception("Veuillez choisir un objet");
		else if (qtite <= 0)
			throw new Exception("Veuillez saisir une quantité valable");

		Connection connexion = null;
		PreparedStatement statement = null;
		try {
			connexion = SqlUtil.getConnection();

			statement = connexion.prepareStatement("update objet set qtiteRest = qtiteRest - ? where id = ?;");

			statement.setInt(1, qtite);
			statement.setInt(2, id);

			statement.executeUpdate();

			SqlUtil.close(statement);

			statement = connexion.prepareStatement(
					"insert into emprunt (id_user,id_objet,qtite_emprunt,rendu) values (?,?,?,false);");

			statement.setInt(1, user.getId());
			statement.setInt(2, id);
			statement.setInt(3, qtite);

			statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("Erreur lors l'emprunt, Il n'y en pas assez");
		} finally {
			SqlUtil.close(statement);
			SqlUtil.close(connexion);
		}
	}

	public static void listeEmprunt(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Utilisateur user = (Utilisateur) session.getAttribute(ATT_SESSION_USER);

		Connection connexion = null;
		PreparedStatement statement = null;
		ResultSet resultat = null;
		try {
			connexion = SqlUtil.getConnection();

			/////////////////// A rendre ////////////////////////////
			statement = connexion
					.prepareStatement("select emprunt.id as id,objet.intitule as nom, qtite_emprunt, rendu from "
							+ "emprunt join objet on id_objet = objet.id where id_User = ? and rendu = false");

			statement.setInt(1, user.getId());

			resultat = statement.executeQuery();

			String message = "<table class='table table-hover'>	<thead><tr><th>Nom</th><th>Quantit&eacute;</th><th>Rendre</th></tr></thead><tbody>";

			while (resultat.next()) {

				String nom = resultat.getString("nom");
				String qtite = resultat.getString("qtite_emprunt");
				int id = resultat.getInt("id");
				message += "<tr><td>" + nom + "</td><td>" + qtite + "</td><td>"
						+ "<form id='signin' role='form' method='post' action='Rendre'>" + "<button id='" + id
						+ "' name='id_rendre' value='" + id
						+ "' class='btn btn-primary'>Rendre</button></form></td></tr>";
			}
			message += "</tbody>";
			message += "</table></br></br>";

			SqlUtil.close(statement);
			SqlUtil.close(resultat);

			//////////////////// Historique ////////////////////////////////
			statement = connexion
					.prepareStatement("select emprunt.id, objet.intitule as nom, qtite_emprunt, rendu from "
							+ "emprunt join objet on id_objet = objet.id where id_user = ? and rendu = true");
			statement.setInt(1, user.getId());

			resultat = statement.executeQuery();

			message += "<h2>Historique</h2><table class='table table-hover'>	"
					+ "<thead><tr><th>Nom</th><th>Quantit&eacute;</th><th>Rendu</th></tr></thead><tbody>";
			while (resultat.next()) {
				String id = resultat.getString("nom");
				String qtite = resultat.getString("qtite_emprunt");
				String re = "Rendu";
				message += "<tr><td>" + id + "</td><td>" + qtite + "</td><td>" + re + "</td></tr>";
			}
			message += "</tbody>";
			message += "</table></br></br>";
			request.setAttribute("tab", message);

		} catch (SQLException e) {
			/* Gérer les éventuelles erreurs ici */
		} finally {
			SqlUtil.close(resultat);
			SqlUtil.close(statement);
			SqlUtil.close(connexion);
		}
	}

	public static void selectObj(HttpServletRequest request) {
		Connection connexion = null;
		PreparedStatement statement = null;
		ResultSet resultat = null;
		try {
			connexion = SqlUtil.getConnection();

			/////////////////// Liste select ////////////////////////////
			statement = connexion.prepareStatement("select id, intitule from objet where qtiteRest > 0;");

			resultat = statement.executeQuery();

			String message = "<select class='form-control' name='objet'><option selected value ='0'>Choisir</option>";

			while (resultat.next()) {
				int id = resultat.getInt("id");
				String intitule = resultat.getString("intitule");
				message += "<option value='" + id + "'>" + intitule + "</option>";
			}

			message += "</select><label for='objet'>Objet</label>";

			request.setAttribute("tab", message);
		} catch (SQLException e) {
			/* Gérer les éventuelles erreurs ici */
		} finally {
			SqlUtil.close(resultat);
			SqlUtil.close(statement);
			SqlUtil.close(connexion);
		}
	}

	public static Objet findByIntitule(String intitule) throws Exception {
		Connection connexion = null;
		ResultSet resultat = null;
		PreparedStatement statement = null;
		try {
			connexion = SqlUtil.getConnection();

			statement = connexion.prepareStatement("SELECT * FROM objet WHERE intitule = ?");
			statement.setString(1, intitule);
			resultat = statement.executeQuery();

			if (resultat.next()) {
				Objet objet = new Objet(resultat.getString("intitule"), resultat.getInt("qtiterest"));

				return objet;
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
	 * Récupère l'ID d'un objet en BDD si il n'est pas dans l'Objet
	 * 
	 * @return l'id d'un objet, ou null si pas trouvé
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

				statement = connexion.prepareStatement("SELECT * FROM Objet WHERE intitule = ?");
				statement.setString(1, this.getIntitule());
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

	public static void ajouterBDD(String intitule, int qtite) throws SQLException {

		Connection connexion = null;
		PreparedStatement statement = null;
		try {
			connexion = SqlUtil.getConnection();

			statement = connexion.prepareStatement("Insert into Objet (intitule, qtiterest) values" + "(?,?);");

			statement.setString(1, intitule);
			statement.setInt(2, qtite);
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new SQLException("Impossible de créer cette objet, il existe déjà");
		} finally {
			SqlUtil.close(statement);
			SqlUtil.close(connexion);
		}
	}

	public static void supprimerObjet(int id) throws SQLException {
		Connection connexion = null;
		PreparedStatement statement = null;
		try {
			connexion = SqlUtil.getConnection();

			statement = connexion.prepareStatement("DELETE FROM Objet WHERE id=?");

			statement.setInt(1, id);
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new SQLException("Erreur lors de la suppression");
		} finally {
			SqlUtil.close(statement);
			SqlUtil.close(connexion);
		}

	}

}
