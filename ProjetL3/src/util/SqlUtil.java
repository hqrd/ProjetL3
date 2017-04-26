package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlUtil {

	/**
	 * Méthode qui permet de se connecter à la BDD
	 * 
	 * @return Connection
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {
		try {
			Class.forName("org.hsqldb.jdbc.JDBCDriver");
		} catch (ClassNotFoundException e) {
			/* Gérer les éventuelles erreurs ici. */
		}

		/* Connexion à la base de données */
		String url = "jdbc:hsqldb:hsql://localhost/";
		String utilisateur = "SA";
		String motDePasse = "";
		return DriverManager.getConnection(url, utilisateur, motDePasse);
	}

	/**
	 * Méthode pour fermer l'objet Connection
	 * 
	 * @param connexion
	 */
	public static void close(Connection connexion) {
		try {
			connexion.close();
		} catch (Exception e) {
			/* ignored */ }
	}

	/**
	 * Méthode pour fermer l'objet ResultSet
	 * 
	 * @param resultat
	 */
	public static void close(ResultSet resultat) {
		try {
			resultat.close();
		} catch (Exception e) {
			/* ignored */ }

	}

	/**
	 * Méthode pour fermer l'objet PreparedStatement
	 * 
	 * @param resultat
	 */
	public static void close(PreparedStatement statement) {
		try {
			statement.close();
		} catch (Exception e) {
			/* ignored */ }

	}
}
