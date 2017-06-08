package dao;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import entities.Emprunt;
import entities.Objet;
import entities.Utilisateur;
import listener.LocalEntityManagerFactory;
import util.Utils;

public class ObjetDAO {

	/**
	 * Renvoie le code html de la liste d'objets
	 * 
	 * @param user
	 * @return
	 */
	public String listeObjet(Utilisateur user) {
		EntityManager em = LocalEntityManagerFactory.createEntityManager();

		String message = null;
		Query requete = em.createQuery("SELECT o FROM Objet o");

		@SuppressWarnings("unchecked")
		List<Objet> listeObjets = requete.getResultList();

		message = "<table class='table table-hover'>	<thead><tr><th>Nom</th><th>Quantité</th><th>Actions	 </th></tr></thead><tbody>";
		for (Objet objet : listeObjets) {
			String nom = objet.getIntitule();
			Integer qtite = objet.getQtiterest();
			Integer id = objet.getId();
			message += "<tr><td>" + nom + "</td><td>" + qtite + "</td><td><div class='row'><div class='col-sm-6'>";

			if (qtite > 0)
				message += "<a href='Reservation'><button type='button' class='btn btn-default btn-sm'>Réserver</button></a>";

			if (user != null) {
				try {
					UtilisateurDAO utilisateurDao = new UtilisateurDAO();
					if (utilisateurDao.getObjets(user, nom) > 0)
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

		return message;
	}

	/**
	 * Renvoie le code html des objets à selectionner
	 * 
	 * @TODO renvoyer la liste des objets trouvés et faire la boucle dans le template
	 * @param request
	 * @return
	 */
	public String selectObj(HttpServletRequest request) {

		EntityManager em = LocalEntityManagerFactory.createEntityManager();

		Query requete = em.createQuery("select o from Objet o where o.qtiterest > 0");
		@SuppressWarnings("unchecked")
		List<Objet> listeObjets = requete.getResultList();

		String message = "<label for='objet'>Objet</label>";
		message += "<select class='form-control' name='objet' ><option selected value ='0'>Choisir</option>";
		for (Objet objet : listeObjets) {
			String intitule = objet.getIntitule();
			Integer id = objet.getId();
			message += "<option value='" + id + "'>" + intitule + "</option>";
		}
		message += "</select>";
		return message;
	}

	/**
	 * Ajoute un emprunt d'une certaine quantité lié à un utilisateur et un objet
	 * 
	 * @param user
	 * @param id
	 * @param qtite
	 * @throws Exception
	 */
	public void emprunterObjet(Utilisateur user, Integer idObjet, Integer qtite) throws Exception {
		if (idObjet == 0)
			throw new Exception("Veuillez choisir un objet");
		else if (qtite <= 0)
			throw new Exception("Veuillez saisir une quantité valable");

		EntityManager em = LocalEntityManagerFactory.createEntityManager();
		try {
			Objet objet = em.find(Objet.class, idObjet);

			if (objet.getQtiterest() < qtite)
				throw new Exception("Erreur lors l'emprunt, Il n'y en pas assez");

			// Utilisation des transactions pour éviter les appel concurentiels
			EntityTransaction etx = em.getTransaction();
			etx.begin();
			Integer rest = objet.getQtiterest();
			objet.setQtiterest(rest - qtite);

			Emprunt emprunt = new Emprunt();
			emprunt.setUser(user);
			emprunt.setObjet(em.find(Objet.class, idObjet));
			emprunt.setQtiteEmprunt(qtite);
			emprunt.setRendu(false);
			emprunt.setDateEmprunt(new Timestamp(System.currentTimeMillis()));
			em.persist(emprunt);
			etx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Erreur lors l'emprunt, Il n'y en pas assez");
		}
	}

	/**
	 * Renvoie le code html de la liste des emprunts et de l'historique
	 * 
	 * @param user
	 * @return
	 */
	public String listeEmprunt(Utilisateur user) {
		String message = "";
		try {

			EntityManager em = LocalEntityManagerFactory.createEntityManager();
			// Selectionne les emprunts de l'utilisateur non rendu
			Query requete = em.createQuery("select e from Emprunt e where e.user = :user and e.rendu = false");
			requete.setParameter("user", user);

			@SuppressWarnings("unchecked")
			List<Emprunt> listeEmprunts = requete.getResultList();

			message = "<table class='table table-hover'>	<thead><tr><th>Nom</th><th>Quantit&eacute;</th><th>Emprunté le</th><th>Rendre</th></tr></thead><tbody>";

			for (Emprunt emprunt : listeEmprunts) {

				String nom = emprunt.getObjet().getIntitule();
				Integer qtite = emprunt.getQtiteEmprunt();
				int id = emprunt.getId();

				String date_emprunt = null;
				try {
					date_emprunt = Utils.formatDate(emprunt.getDateEmprunt().toString(), false);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				message += "<tr><td>" + nom + "</td><td>" + qtite + "</td><td>" + date_emprunt + "</td><td>"
						+ "<form id='signin' role='form' method='post' action='Rendre'>" + "<button id='" + id
						+ "' name='id_rendre' value='" + id
						+ "' class='btn btn-primary'>Rendre</button></form></td></tr>";
			}
			message += "</tbody>";
			message += "</table></br></br>";

			// Historique
			requete = em.createQuery(
					"select e from Emprunt e where e.user = :user and e.rendu = true ORDER BY e.dateEmprunt DESC");
			requete.setParameter("user", user);

			@SuppressWarnings("unchecked")
			List<Emprunt> listeEmpruntsRendus = requete.getResultList();

			//////////////////// Historique ////////////////////////////////

			message += "<h2>Historique</h2><table id='histo' class='table table-hover'>	"
					+ "<thead><tr><th>Nom</th><th>Quantit&eacute;</th><th>Emprunté le</th><th>Rendu le</th></tr></thead><tbody>";
			for (Emprunt emprunt : listeEmpruntsRendus) {
				String intitule = emprunt.getObjet().getIntitule();
				Integer qtite = emprunt.getQtiteEmprunt();
				String date_rendu = null, date_emprunt = null;
				try {
					date_emprunt = Utils.formatDate(emprunt.getDateEmprunt().toString(), false);
					date_rendu = Utils.formatDate(emprunt.getDateRendu().toString(), false);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				message += "<tr><td>" + intitule + "</td><td>" + qtite + "</td><td>" + date_emprunt + "</td><td>"
						+ date_rendu + "</td></tr>";
			}
			message += "</tbody>";
			message += "</table></br></br>";

		} catch (Exception e) {
			e.printStackTrace();
		}
		return message;
	}

	/**
	 * Fonction pour rendre un objet (màj la BDD)
	 * 
	 * @param id
	 */
	public void rendreOjet(int idEmprunt) {
		EntityManager em = LocalEntityManagerFactory.createEntityManager();
		Emprunt emprunt = em.find(Emprunt.class, idEmprunt);
		Objet objet = em.find(Objet.class, emprunt.getObjet().getId());

		em.getTransaction().begin();

		objet.setQtiterest(objet.getQtiterest() + emprunt.getQtiteEmprunt());

		emprunt.setRendu(true);
		emprunt.setDateRendu(new Timestamp(System.currentTimeMillis()));

		em.getTransaction().commit();
	}

	/**
	 * Supprime un objet
	 * 
	 * @param idObjet
	 */
	public String supprimerObjet(int idObjet) {
		EntityManager em = LocalEntityManagerFactory.createEntityManager();

		Objet objet = em.find(Objet.class, idObjet);
		String name = objet.getIntitule();

		em.getTransaction().begin();
		em.remove(objet);
		em.getTransaction().commit();

		return name;
	}
}
