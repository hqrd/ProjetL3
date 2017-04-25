package beans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Statement;

public class Connexion {

	
	public static void test()
	{
		/* Chargement du driver JDBC pour MySQL */
		try {
		    Class.forName( "org.hsqldb.jdbc.JDBCDriver" );
		} catch ( ClassNotFoundException e ) {
		    /* Gérer les éventuelles erreurs ici. */
		}
		
		/* Connexion à la base de données */
		String url = "jdbc:hsqldb:hsql://localhost/";
		String utilisateur = "SA";
		String motDePasse = "";
		Connection connexion = null;
		try {
		    connexion = DriverManager.getConnection( url, utilisateur, motDePasse );

		    Statement statement = connexion.createStatement();
			ResultSet resultat;
			System.out.println("TEST !!!!!!!!!!!!");
			resultat = statement.executeQuery( "select nom,test from user" );
		

		while ( resultat.next() ) {
		    
		    String nom = resultat.getString( "nom" );
		    String test = resultat.getString( "test" );
		    
		    System.out.println(nom+" "+test);
		}

		} catch ( SQLException e ) {
		    /* Gérer les éventuelles erreurs ici */
		} finally {
		    if ( connexion != null )
		        try {
		            /* Fermeture de la connexion */
		            connexion.close();
		        } catch ( SQLException ignore ) {
		            /* Si une erreur survient lors de la fermeture, il suffit de l'ignorer. */
		        }
		}
	}
	
	public static void main(String [] argv)
	{
		test();
	}
}
