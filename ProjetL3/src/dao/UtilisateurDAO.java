package dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import entities.Objet;
import entities.Utilisateur;
import listener.LocalEntityManagerFactory;

public class UtilisateurDAO {

	/**
	 * Retrouve un utilisateur avec son email
	 * 
	 * @param email
	 * @return
	 * @throws Exception
	 */
	public Utilisateur findByEmail(String email) throws Exception {
		EntityManager em = LocalEntityManagerFactory.createEntityManager();

		Utilisateur utilisateur = null;

		Query requete = em.createQuery("SELECT u FROM Utilisateur u WHERE u.email=:email");
		requete.setParameter("email", email);
		try {
			utilisateur = (Utilisateur) requete.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}

		return utilisateur;
	}

	/**
	 * Renvoie le nombre d'objets reservés par un utilisateur en fonction du nom de l'objet
	 * 
	 * @param idUser
	 * @param intitule
	 * @return
	 * @throws Exception
	 */
	public Integer getObjets(Utilisateur user, String intitule) throws Exception {
		EntityManager em = LocalEntityManagerFactory.createEntityManager();

		Long nbObjets = null;

		Query requete = em.createQuery("SELECT o FROM Objet o WHERE o.intitule = :intitule");
		requete.setParameter("intitule", intitule);
		Objet objet = (Objet) requete.getSingleResult();

		requete = em.createQuery(
				"SELECT SUM(e.qtiteEmprunt) as nb_res FROM Emprunt e WHERE e.user = :user AND e.objet = :objet AND e.rendu = false");

		requete.setParameter("user", user);
		requete.setParameter("objet", objet);
		try {
			nbObjets = (Long) requete.getSingleResult();
		} catch (NoResultException e) {
			return 0;
		}

		if (nbObjets != null)
			return nbObjets.intValue();
		return 0;
	}

	/**
	 * Insert un Utilisateur en BDD
	 * 
	 * @param utilisateur
	 * @throws Exception
	 */
	public void insertBDD(Utilisateur utilisateur) throws Exception {
		EntityManager em = LocalEntityManagerFactory.createEntityManager();

		try {
			em.getTransaction().begin();
			em.persist(utilisateur);
			em.getTransaction().commit();
			em.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Erreur lors de l'inscription");
		}
	}
}
