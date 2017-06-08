
package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import dao.ObjetDAO;
import util.Utils;

@WebServlet("/logout")
public class Deconnexion extends HttpServlet {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	public static final String	VUE					= "/WEB-INF/accueil.jsp";

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getSession().invalidate();

		entities.Utilisateur user = Utils.getSessionUser(request);
		ObjetDAO objetDAO = new ObjetDAO();

		request.setAttribute("tab", objetDAO.listeObjet(user));
		
		request.setAttribute("te1", "<span class='sr-only'>(current)</span>");
		request.setAttribute("class1", "active");
		request.setAttribute("te2", "");
		request.setAttribute("class2", "");
		request.setAttribute("te3", "");
		request.setAttribute("class3", "");
		request.setAttribute("te4", "");
		request.setAttribute("class4", "");
		response.sendRedirect(request.getContextPath() + "/accueil");

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		this.getServletContext().getRequestDispatcher(VUE).forward(request, response);
	}

}
