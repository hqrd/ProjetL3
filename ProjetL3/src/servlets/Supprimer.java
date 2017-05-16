
package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import beans.Objet;

@WebServlet("/admin/Supprimer")
public class Supprimer extends HttpServlet {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	public static final String	ATT_SESSION_USER	= "sessionUtilisateur";

	public static final String	VUE					= "/WEB-INF/accueil" + ".jsp";

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String id_tmp = (request.getParameter("id_delete"));
		int id = (int) Integer.parseInt(id_tmp);
		try {
			Objet.supprimerObjet(id);
			request.setAttribute("success_message", Objet.getIntituleFromId(id) + " rendu");
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("warning_message", "Erreur : " + e.getMessage());
		}

		response.sendRedirect(request.getContextPath() + "/accueil");
	}

}
