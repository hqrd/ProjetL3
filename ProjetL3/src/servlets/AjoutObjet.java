
package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import beans.Objet;
import beans.Utilisateur;
import forms.AjouterForm;
import forms.ConnexionForm;

public class AjoutObjet extends HttpServlet {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	public static final String	ATT_USER			= "utilisateur";
	public static final String	ATT_FORM			= "form";
	public static final String	ATT_SESSION_USER	= "sessionUtilisateur";

	public static final String VUE = "/WEB-INF/ajoutObjet" + ".jsp";

	private void setActiveMenu(HttpServletRequest request) {
		request.setAttribute("te1", "<span class='sr-only'>(current)</span>");
		request.setAttribute("class1", "active");
		request.setAttribute("te2", "");
		request.setAttribute("class2", "");
		request.setAttribute("te3", "");
		request.setAttribute("class3", "");
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		this.getServletContext().getRequestDispatcher(VUE).forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		AjouterForm form = new AjouterForm();
		form.validation(request);
		this.getServletContext().getRequestDispatcher(VUE).forward(request, response);
	}

}
