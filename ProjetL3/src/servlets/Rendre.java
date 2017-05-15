
package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import beans.Objet;

public class Rendre extends HttpServlet {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	public static final String	VUE					= "/WEB-INF/rendre.jsp";

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Objet.listeEmprunt(request);

		request.setAttribute("te3", "<span class='sr-only'>(current)</span>");
		request.setAttribute("class3", "active");
		request.setAttribute("te2", "");
		request.setAttribute("class2", "");
		request.setAttribute("te1", "");
		request.setAttribute("class1", "");
		request.setAttribute("te4", "");
		request.setAttribute("class4", "");
		this.getServletContext().getRequestDispatcher(VUE).forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String id_tmp = (request.getParameter("id_rendre"));
		int id = (int) Integer.parseInt(id_tmp);
		try {
			Objet.rendreOjet(id);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Objet.listeEmprunt(request);
		this.getServletContext().getRequestDispatcher(VUE).forward(request, response);
	}

}
