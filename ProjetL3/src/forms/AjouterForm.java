package forms;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import entities.Objet;
import listener.LocalEntityManagerFactory;

public final class AjouterForm {

	private static final String			CHAMP_INITULE	= "intitule";
	private static final String			CHAMP_QTITE		= "quantite";

	private String						resultat;
	private static Map<String, String>	erreurs			= new HashMap<String, String>();

	public String getResultat() {
		return resultat;
	}

	public Map<String, String> getErreurs() {
		return erreurs;
	}

	public void resetErrors() {
		erreurs.clear();
	}

	public void validation(HttpServletRequest request) {
		resetErrors();

		EntityManager em = LocalEntityManagerFactory.createEntityManager();

		String intitule = request.getParameter(CHAMP_INITULE);
		String qtite_tmp = request.getParameter(CHAMP_QTITE);
		int qtite = 0;
		try {
			qtite = (int) Integer.parseInt(qtite_tmp);
		} catch (Exception e) {
			setErreur(CHAMP_QTITE, "Veuillez saisir un nombre");
		}

		if (intitule == "")
			setErreur(CHAMP_INITULE, "Veuillez saisir un intitule d'objet");

		if (qtite < 0)
			setErreur(CHAMP_QTITE, "\nVeuilliez saisir une quantité valide (supérieur ou égale à 0)");
		if (erreurs.isEmpty()) {
			Objet objet = new Objet();
			objet.setIntitule(intitule);
			objet.setQtiterest(qtite);
			em.getTransaction().begin();
			em.persist(objet);
			em.getTransaction().commit();
		}
		if (erreurs.isEmpty()) {
			request.setAttribute("success_message", qtite + " objet(s) " + intitule + " ajouté");
			resultat = "Succès de l'insertion";
		} else {
			resultat = "Echec de l'insertion";

			request.setAttribute("warning_message", "Erreur : " + resultat);

		}

	}

	/*
	 * Ajoute un message correspondant au champ spécifié à la map des erreurs.
	 */
	private static void setErreur(String champ, String message) {
		erreurs.put(champ, message);
	}

}