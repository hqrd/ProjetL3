
package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import dao.ObjetDAO;
import util.Utils;

@WebServlet("/admin/Supprimer")
public class Supprimer extends HttpServlet {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	public static final String	ATT_SESSION_USER	= "sessionUtilisateur";

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

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		ObjetDAO objetDAO = new ObjetDAO();
		entities.Utilisateur user = Utils.getSessionUser(request);
		
		String id_tmp = (request.getParameter("id_delete"));
		int id = (int) Integer.parseInt(id_tmp);
		try {
			String name = objetDAO.supprimerObjet(id);
			request.setAttribute("success_message", name + " rendu");
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("warning_message", "Erreur : " + e.getMessage());
		}

		request.setAttribute("tab", objetDAO.listeObjet(user));
		setActiveMenu(request);

		this.getServletContext().getRequestDispatcher(VUE).forward(request, response);
	}

}
