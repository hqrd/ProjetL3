
package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import forms.AjouterForm;

@WebServlet(name = "Ajout", urlPatterns = "/admin/Ajouter-objet")
public class AjoutObjet extends HttpServlet {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	public static final String	ATT_FORM			= "form";
	public static final String	ATT_SESSION_USER	= "sessionUtilisateur";

	public static final String	VUE					= "/WEB-INF/admin/ajoutObjet" + ".jsp";

	private void setActiveMenu(HttpServletRequest request) {

		request.setAttribute("te1", "");
		request.setAttribute("class1", "");
		request.setAttribute("te2", "");
		request.setAttribute("class2", "");
		request.setAttribute("te3", "");
		request.setAttribute("class3", "");

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
		request.setAttribute( ATT_FORM, form );
		this.getServletContext().getRequestDispatcher(VUE).forward(request, response);
	}

}
