package dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
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
		System.out.println(utilisateur.getEmail());
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
	public int getObjets(Utilisateur user, String intitule) throws Exception {
		EntityManager em = LocalEntityManagerFactory.createEntityManager();

		int nbObjets = 0;

		Query requete = em.createQuery("SELECT id FROM Objet o WHERE o.intitule = :intitule");
		requete.setParameter("intitule", intitule);
		int idObjet = (int) requete.getSingleResult();

		requete = em.createQuery(
				"SELECT SUM(qtite_emprunt) as nb_res FROM emprunt e WHERE e.ID_USER = :idUser AND e.ID_OBJET = :idObjet AND e.rendu = false");

		requete.setParameter("idUser", user.getId());
		requete.setParameter("idObjet", idObjet);

		nbObjets = (int) requete.getSingleResult();

		return nbObjets;
	}
}
