package beans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import util.SqlUtil;

public class Objet {

	public static final String ATT_SESSION_USER = "sessionUtilisateur";

	public Objet() {
	}

	public void listeObjet(HttpServletRequest request) {
		Connection connexion = null;
		try {
			connexion = SqlUtil.getConnection();
			Statement statement = connexion.createStatement();
			ResultSet resultat;
			resultat = statement.executeQuery("select intitule,qtiterest from objet");
			String message = "<table class='table table-hover'>	<thead><tr><th>Nom</th><th>Quantit&eacute;</th></tr></thead><tbody>";
			while (resultat.next()) {
				String nom = resultat.getString("intitule");
				String qtite = resultat.getString("qtiterest");
				message += "<tr><td>" + nom + "</td><td>" + qtite + "</td><td>";
			}
			message += "</tbody>";
			message += "</table>";

			request.setAttribute("tab", message);

		} catch (SQLException e) {
			/* Gérer les éventuelles erreurs ici */
		} finally {
			SqlUtil.close(connexion);
		}
	}

	public void rendreOjet(int id) throws Exception {
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
		try {
			connexion = DriverManager.getConnection(url, utilisateur, motDePasse);

			Statement statement = connexion.createStatement();
			@SuppressWarnings("unused")
			ResultSet resultat;
			try {
				String sqlToObjet = "update objet set qtiteRest = qtiteRest + (select qtite_emprunt from emprunt where id = "
						+ id + ") where id = (select id_objet from emprunt where id = " + id + ");";
				String sqlToEmprunt = "update emprunt set rendu = true where id = " + id + ";";
				resultat = statement.executeQuery(sqlToObjet);
				resultat = statement.executeQuery(sqlToEmprunt);

			} catch (Exception e) {
				throw new Exception("Erreur lors la restitution");
			}
		} catch (SQLException e) {
			/* Gérer les éventuelles erreurs ici */
		} finally {
			if (connexion != null)
				try {
					/* Fermeture de la connexion */
					connexion.close();
				} catch (SQLException ignore) {
					/* Si une erreur survient lors de la fermeture, il suffit de l'ignorer. */
				}
		}
	}
	
	public void emprunterObjet(HttpServletRequest request,int id,int qtite) throws Exception
	{
    	if (id == 0) throw new Exception("Veuillez choisir un objet");
    	else if (qtite<=0) throw new Exception("Veuillez saisir une quantit&eacute; valable");
		HttpSession session = request.getSession();
		Utilisateur user = (Utilisateur) session.getAttribute(ATT_SESSION_USER);
		String username = user.getNom();
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
		try {
			connexion = DriverManager.getConnection(url, utilisateur, motDePasse);
			@SuppressWarnings("unused")
			ResultSet resultat;
			Statement statement = connexion.createStatement();


			try {

				resultat = statement
						.executeQuery("update objet set qtiteRest = qtiteRest -" + qtite + " where id = " + id + ";");
				resultat = statement
						.executeQuery("insert into emprunt " + "(nom_user,id_objet,qtite_emprunt,rendu) values " + "('"
								+ username + "'," + id + "," + qtite + ",false);");
			} catch (Exception e) {
				throw new Exception("Erreur lors l'emprunt, Il n'y en pas assez");
			}

		} catch (SQLException e) {
			/* Gérer les éventuelles erreurs ici */
		} finally {
			if (connexion != null)
				try {
					/* Fermeture de la connexion */
					connexion.close();
				} catch (SQLException ignore) {
					/* Si une erreur survient lors de la fermeture, il suffit de l'ignorer. */
				}
		}
	}

	public void listeEmprunt(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Utilisateur user = (Utilisateur) session.getAttribute(ATT_SESSION_USER);
		String username = user.getNom();
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
		try {
			connexion = DriverManager.getConnection(url, utilisateur, motDePasse);

			Statement statement = connexion.createStatement();
			ResultSet resultat;
			/////////////////// A rendre ////////////////////////////
			resultat = statement
					.executeQuery("select emprunt.id as id,objet.intitule as nom, qtite_emprunt, rendu from "
							+ "emprunt join objet on id_objet = objet.id where nom_User = '" + username
							+ "' and rendu = false");
			// resultat = statement.executeQuery( "select nom,test from user" );

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

			//////////////////// Historique ////////////////////////////////
			resultat = statement.executeQuery("select emprunt.id, objet.intitule as nom, qtite_emprunt, rendu from "
					+ "emprunt join objet on id_objet = objet.id where nom_User = '" + username + "' and rendu = true");

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
			if (connexion != null)
				try {
					/* Fermeture de la connexion */
					connexion.close();
				} catch (SQLException ignore) {
					/* Si une erreur survient lors de la fermeture, il suffit de l'ignorer. */
				}
		}
	}

	public void selectObj(HttpServletRequest request) {
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
		try {
			connexion = DriverManager.getConnection(url, utilisateur, motDePasse);

			Statement statement = connexion.createStatement();
			ResultSet resultat;

			/////////////////// Liste select ////////////////////////////
			resultat = statement.executeQuery("select id, intitule from objet where qtiteRest > 0;");

			String message = "<select class='form-control' name='objet'><option selected value ='0'>Choisir</option>";

			while (resultat.next()) {

				int id = resultat.getInt("id");
				String intitule = resultat.getString("intitule");
				message += "<option value='" + id + "'>" + intitule + "</option>";
				// System.out.println(nom+" "+qtite);
			}
			message += "</select><label for='objet'>Objet</label>";

			request.setAttribute("tab", message);
		} catch (SQLException e) {
			/* Gérer les éventuelles erreurs ici */
		} finally {
			if (connexion != null)
				try {
					/* Fermeture de la connexion */
					connexion.close();
				} catch (SQLException ignore) {
					/* Si une erreur survient lors de la fermeture, il suffit de l'ignorer. */
				}
		}
	}
}
