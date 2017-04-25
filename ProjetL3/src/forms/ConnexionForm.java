package forms;

import java.util.HashMap;
import java.util.Map;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



import beans.Utilisateur;

public final class ConnexionForm {


    private static final String CHAMP_PASS   = "motdepasse";
    private static final String CHAMP_NOM    = "nom";    

    private String resultat;
    private Map<String, String> erreurs = new HashMap<String, String>();

    public String getResultat() {
        return resultat;
    }

    public Map<String, String> getErreurs() {
        return erreurs;
    }
    
    public Utilisateur connecterUtilisateur( HttpServletRequest request ) {
        String motDePasse = getValeurChamp( request, CHAMP_PASS );
        String nom = getValeurChamp( request, CHAMP_NOM );

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setMotDePasse( motDePasse );

        try {
            validationNom( nom );
        } catch ( Exception e ) {
            setErreur( CHAMP_NOM, e.getMessage() );
        }
        utilisateur.setNom( nom );
    	try {
			Connexion(utilisateur);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			setErreur( CHAMP_NOM, e.getMessage() );
		}
        if ( erreurs.isEmpty() ) {
        	
            resultat = "Succès de la connexion.";
        } else {
            resultat = "Échec de la connexion.";
        }
        return utilisateur;
    }
    private void validationNom( String nom ) throws Exception {
        if ( nom != null && nom.length() < 3 ) {
            throw new Exception( "Le nom d'utilisateur doit contenir au moins 3 caractères." );
        }
    }
    /*
     * Ajoute un message correspondant au champ spécifié à la map des erreurs.
     */
    private void setErreur( String champ, String message ) {
        erreurs.put( champ, message );
    }

    /*
     * Méthode utilitaire qui retourne null si un champ est vide, et son contenu
     * sinon.
     */
    private static String getValeurChamp( HttpServletRequest request, String nomChamp ) {
        String valeur = request.getParameter( nomChamp );
        if ( valeur == null || valeur.trim().length() == 0 ) {
            return null;
        } else {
            return valeur.trim();
        }
    }  
    private static void Connexion(Utilisateur user)  throws Exception
    {
    	//System.out.println("testtestetetsterts");
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
		ResultSet resultat = null;
		String pswdEnc = encode(user.getMotDePasse());
		String sql = "select pswdenc from utilisateur where nom = '"+user.getNom()+"';" ;
		System.out.println(sql);
		resultat = statement.executeQuery( sql);
		System.out.println("testtestetsetsetr - 3");
	
		boolean flag = false;
	while ( resultat.next() ) {
	    
	    String pswd = resultat.getString( "pswdEnc" );
	    if (pswd.equals(pswdEnc))
	    {
	    	flag = true;
	    }
	    else
	    	{
	    	System.out.println("mauvais mot de passe");
	    	throw new Exception( "Le mot de passe est incorrect" );	    
	    	}
	   // System.out.println(nom+" "+test);
	}
	if (flag){}
    else
    	{
    	System.out.println("Aucun resultat");
    	throw new Exception( "Le nom d'utilisateur est invalide" );
    	}
	

	} catch ( SQLException e ) {
	    /* Gérer les éventuelles erreurs ici */
		throw new Exception("erreur : "+e.getMessage());
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
    ////////////  Coder les mots de passe ///////////////
    private static String encode(String password)
    {
        byte[] uniqueKey = password.getBytes();
        byte[] hash      = null;
        try
        {
            hash = MessageDigest.getInstance("MD5").digest(uniqueKey);
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new Error("No MD5 support in this VM.");
        }

        StringBuilder hashString = new StringBuilder();
        for (int i = 0; i < hash.length; i++)
        {
            String hex = Integer.toHexString(hash[i]);
            if (hex.length() == 1)
            {
                hashString.append('0');
                hashString.append(hex.charAt(hex.length() - 1));
            }
            else
                hashString.append(hex.substring(hex.length() - 2));
        }
        return hashString.toString();
    }
}