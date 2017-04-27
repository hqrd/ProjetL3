package beans;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import util.SqlUtil;

public class Objet {

	public static final String ATT_SESSION_USER = "sessionUtilisateur";

	public Objet() {
	}

	public void listeObjet(HttpServletRequest request) {
		Connection connexion = null;
		ResultSet resultat = null;
		PreparedStatement statement = null;
		try {
			connexion = SqlUtil.getConnection();

			statement = connexion.prepareStatement("select intitule,qtiterest from objet");

			resultat = statement.executeQuery();

			String message = "<table class='table table-hover'>	<thead><tr><th>Nom</th><th>Quantité</th><th>Actions	 </th></tr></thead><tbody>";
			while (resultat.next()) {
				String nom = resultat.getString("intitule");
				String qtite = resultat.getString("qtiterest");
				message += "<tr><td>" + nom + "</td><td>" + qtite + "</td><td>"
						+ "<button type='button' class='btn btn-default btn-sm'>Réserver</button>"
						+ "<button type='button' class='btn btn-default btn-sm'>Rendre</button>" + "</td>";
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

	public void rendreOjet(int id) throws Exception {
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

			statement = connexion.prepareStatement("update emprunt set rendu = true where id = ?;");

			statement.setInt(1, id);

			statement.executeUpdate();

		} catch (SQLException e) {
			/* Gérer les éventuelles erreurs ici */
		} finally {
			SqlUtil.close(statement);
			SqlUtil.close(connexion);
		}
	}

	public void emprunterObjet(HttpServletRequest request, int id, int qtite) throws Exception {
		if (id == 0)
			throw new Exception("Veuillez choisir un objet");
		else if (qtite <= 0)
			throw new Exception("Veuillez saisir une quantité valable");

		HttpSession session = request.getSession();
		Utilisateur user = (Utilisateur) session.getAttribute(ATT_SESSION_USER);
		String username = user.getNom();
		int iduser = user.getId();

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

			statement.setInt(1, iduser);
			statement.setInt(2, id);
			statement.setInt(3, qtite);

			statement.executeQuery();

		} catch (SQLException e) {
			throw new Exception("Erreur lors l'emprunt, Il n'y en pas assez");
		} finally {
			SqlUtil.close(statement);
			SqlUtil.close(connexion);
		}
	}

	public void listeEmprunt(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Utilisateur user = (Utilisateur) session.getAttribute(ATT_SESSION_USER);
		String username = user.getNom();
		int iduser = user.getId();

		Connection connexion = null;
		PreparedStatement statement = null;
		ResultSet resultat = null;
		try {
			connexion = SqlUtil.getConnection();

			/////////////////// A rendre ////////////////////////////
			statement = connexion
					.prepareStatement("select emprunt.id as id,objet.intitule as nom, qtite_emprunt, rendu from "
							+ "emprunt join objet on id_objet = objet.id where id_User = ? and rendu = false");

			statement.setInt(1, iduser);

			resultat = statement.executeQuery();

			String message = "<table class='table table-hover'>	<thead><tr><th>Nom</th><th>Quantit&eacute;</th><th>Rendre</th></tr></thead><tbody>";

			while (resultat.next()) {

				String nom = resultat.getString("nom");
				String qtite = resultat.getString("qtite_emprunt");
				int id = resultat.getInt("id");
				message += "<tr><td>" + nom + "</td><td>" + qtite + "</td><td>"
						+ "<form id='signin' class='navbar-form navbar' role='form' method='post' action='Rendre'>"
						+ "<button id='" + id + "' name='" + id + "' value='" + id
						+ "' class='btn btn-primary'>Rendre</button></form></td></tr>";
			}
			message += "</tbody>";
			message += "</table></br></br>";

			SqlUtil.close(statement);
			SqlUtil.close(resultat);

			//////////////////// Historique ////////////////////////////////
			statement = connexion
					.prepareStatement("select emprunt.id, objet.intitule as nom, qtite_emprunt, rendu from "
							+ "emprunt join objet on id_objet = objet.id where id_User = ? and rendu = true");
			statement.setInt(1, iduser);

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

	public void selectObj(HttpServletRequest request) {
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

	public void ajouterBDD(HttpServletRequest request) {
		Connection connexion = null;
		PreparedStatement statement = null;
		try {
			connexion = SqlUtil.getConnection();
			String intitule = request.getParameter("intitule");
			String qtite_tmp = request.getParameter("quantite");
			int qtite = (int) Integer.parseInt(qtite_tmp);

			System.out.println("intitule : " + intitule + "\nqtite = " + qtite);
			statement = connexion.prepareStatement("Insert into Objet (intitule, qtiterest) values" + "(?,?);");

			statement.setString(1, intitule);
			statement.setInt(2, qtite);
			statement.executeUpdate();
		} catch (Exception e) {

		} finally {
			SqlUtil.close(statement);
			SqlUtil.close(connexion);
		}
	}

}
