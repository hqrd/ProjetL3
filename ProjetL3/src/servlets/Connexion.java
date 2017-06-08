
package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import dao.ObjetDAO;
import forms.ConnexionForm;
import util.Utils;

@WebServlet("/accueil")
public class Connexion extends HttpServlet {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	public static final String	ATT_USER			= "utilisateur";
	public static final String	ATT_FORM			= "form";

	public static final String	VUE					= "/WEB-INF/accueil" + ".jsp";

	private void setActiveMenu(HttpServletRequest request) {
		request.setAttribute("te1", "<span class='sr-only'>(current)</span>");
		request.setAttribute("class1", "active");
		request.setAttribute("te2", "");
		request.setAttribute("class2", "");
		request.setAttribute("te3", "");
		request.setAttribute("class3", "");
		request.setAttribute("te4", "");
		request.setAttribute("class4", "");
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		entities.Utilisateur user = Utils.getSessionUser(request);
		ObjetDAO objetDAO = new ObjetDAO();

		request.setAttribute("tab", objetDAO.listeObjet(user));
		setActiveMenu(request);

		this.getServletContext().getRequestDispatcher(VUE).forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/* Préparation de l'objet formulaire */
		ConnexionForm form = new ConnexionForm();

		/* Appel au traitement et à la validation de la requête, et récupération du bean en résultant */
		entities.Utilisateur utilisateur1 = null;
		try {
			utilisateur1 = form.connecterUtilisateur(request);

			/* Stockage du formulaire et du bean dans l'objet request */
			request.setAttribute(ATT_FORM, form);
			request.setAttribute(ATT_USER, utilisateur1);

			if (form.getErreurs().isEmpty()) {
				Utils.setSessionUser(request, utilisateur1);
			} else {
				Utils.setSessionUser(request, null);
			}

		} catch (Exception e) {
			e.printStackTrace();
			this.getServletContext().getRequestDispatcher(VUE).forward(request, response);

		}

		this.doGet(request, response);
	}

}
