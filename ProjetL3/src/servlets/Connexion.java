
package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import beans.Objet;
import beans.Utilisateur;
import forms.ConnexionForm;

public class Connexion extends HttpServlet {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	public static final String	ATT_USER			= "utilisateur";
	public static final String	ATT_FORM			= "form";
	public static final String	ATT_SESSION_USER	= "sessionUtilisateur";

	public static final String	VUE					= "/WEB-INF/accueil" + ".jsp";

	private void setActiveMenu(HttpServletRequest request) {
		request.setAttribute("te1", "<span class='sr-only'>(current)</span>");
		request.setAttribute("class1", "active");
		request.setAttribute("te2", "");
		request.setAttribute("class2", "");
		request.setAttribute("te3", "");
		request.setAttribute("class3", "");
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Objet o = new Objet();
		o.listeObjet(request);
		
		setActiveMenu(request);
		
		this.getServletContext().getRequestDispatcher(VUE).forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/* Préparation de l'objet formulaire */
		ConnexionForm form = new ConnexionForm();
		Objet o = new Objet();
		o.listeObjet(request);
		
		setActiveMenu(request);
		

		/* Appel au traitement et à la validation de la requête, et récupération du bean en résultant */
		Utilisateur utilisateur1 = null;
		try {
			utilisateur1 = form.connecterUtilisateur(request);
			HttpSession session = request.getSession();

			/* Stockage du formulaire et du bean dans l'objet request */
			request.setAttribute(ATT_FORM, form);
			request.setAttribute(ATT_USER, utilisateur1);

			if (form.getErreurs().isEmpty()) {
				session.setAttribute(ATT_SESSION_USER, utilisateur1);
			} else {
				session.setAttribute(ATT_SESSION_USER, null);
			}

			/* Stockage du formulaire et du bean dans l'objet request */

		} catch (Exception e) {
			System.out.println("erreur : " + e.getMessage());
			this.getServletContext().getRequestDispatcher(VUE).forward(request, response);

		}

		this.getServletContext().getRequestDispatcher(VUE).forward(request, response);
	}

}
