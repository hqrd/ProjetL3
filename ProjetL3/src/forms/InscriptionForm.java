package forms;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import dao.UtilisateurDAO;
import entities.Utilisateur;

public final class InscriptionForm {
	private static final String	CHAMP_EMAIL		= "email";
	private static final String	CHAMP_PASS		= "motdepasse";
	private static final String	CHAMP_CONF		= "confirmation";
	private static final String	CHAMP_NOM		= "nom";
	private static final String	CHAMP_PRENOM	= "prenom";

	private String				resultat;
	private Map<String, String>	erreurs			= new HashMap<String, String>();

	public String getResultat() {
		return resultat;
	}

	public Map<String, String> getErreurs() {
		return erreurs;
	}

	public Utilisateur inscrireUtilisateur(HttpServletRequest request) {
		String email = getValeurChamp(request, CHAMP_EMAIL);
		String motDePasse = getValeurChamp(request, CHAMP_PASS);
		String confirmation = getValeurChamp(request, CHAMP_CONF);
		String nom = getValeurChamp(request, CHAMP_NOM);
		String prenom = getValeurChamp(request, CHAMP_PRENOM);

		Utilisateur utilisateur = new Utilisateur();
		UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

		try {
			validationEmail(email);
		} catch (Exception e) {
			setErreur(CHAMP_EMAIL, e.getMessage());
		}
		utilisateur.setEmail(email);

		try {
			validationMotsDePasse(motDePasse, confirmation);
		} catch (Exception e) {
			setErreur(CHAMP_PASS, e.getMessage());
			setErreur(CHAMP_CONF, null);
		}
		utilisateur.setMotDePasse(motDePasse);

		utilisateur.setNom(nom);
		utilisateur.setPrenom(prenom);

		if (erreurs.isEmpty()) {
			try {
				utilisateurDAO.insertBDD(utilisateur);
			} catch (Exception e) {
				setErreur(CHAMP_NOM, e.getMessage());
			}
		}
		if (erreurs.isEmpty()) {
			resultat = "Inscription réussie";
			request.setAttribute("success_message", resultat);
		} else {
			resultat = "Échec de l'inscription.";
			request.setAttribute("warning_message", "Erreur : " + resultat);
		}

		return utilisateur;
	}

	private void validationEmail(String email) throws Exception {
		if (email != null) {
			if (!email.matches("([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)")) {
				throw new Exception("Merci de saisir une adresse mail valide.");
			}

			UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

			if (utilisateurDAO.findByEmail(email) != null) {
				throw new Exception("Email déjà utilisé");
			}
		} else {
			throw new Exception("Merci de saisir une adresse mail.");
		}
	}

	private void validationMotsDePasse(String motDePasse, String confirmation) throws Exception {
		if (motDePasse != null && confirmation != null) {
			if (!motDePasse.equals(confirmation)) {
				throw new Exception("Les mots de passe entrés sont différents, merci de les saisir à nouveau.");
			} else if (motDePasse.length() < 3) {
				throw new Exception("Les mots de passe doivent contenir au moins 3 caractères.");
			}
		} else {
			throw new Exception("Merci de saisir et confirmer votre mot de passe.");
		}
	}

	/**
	 * Ajoute un message correspondant au champ spécifié à la map des erreurs.
	 */
	private void setErreur(String champ, String message) {
		erreurs.put(champ, message);
	}

	/**
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

}