
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

	public static final String	VUE					= "/WEB-INF/admin/ajoutObjet" + ".jsp";

	private void setActiveMenu(HttpServletRequest request) {
		request.setAttribute("te4", "<span class='sr-only'>(current)</span>");
		request.setAttribute("class4", "active");
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		setActiveMenu(request);
		this.getServletContext().getRequestDispatcher(VUE).forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		setActiveMenu(request);
		AjouterForm form = new AjouterForm();
		form.validation(request);
		this.getServletContext().getRequestDispatcher(VUE).forward(request, response);
	}

}
