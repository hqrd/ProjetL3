
package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import beans.Objet;
import beans.Utilisateur;

@WebServlet("/Reservation")
public class Emprunt extends HttpServlet {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	public static final String	VUE					= "/WEB-INF/emprunt.jsp";
	public static final String	ATT_SESSION_USER	= "sessionUtilisateur";

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Objet.selectObj(request);
		request.setAttribute("te2", "<span class='sr-only'>(current)</span>");
		request.setAttribute("class2", "active");
		request.setAttribute("te3", "");
		request.setAttribute("class3", "");
		request.setAttribute("te1", "");
		request.setAttribute("class1", "");

		request.setAttribute("te4", "");
		request.setAttribute("class4", "");

		this.getServletContext().getRequestDispatcher(VUE).forward(request, response);

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getParameter("objet");
		String[] tmp = request.getParameterValues("objet");
		Integer id = Integer.parseInt(tmp[0]);
		String qtite_tmp = request.getParameter("nb");
		Integer qtite = -1;
		try {
			qtite = Integer.parseInt(qtite_tmp);
			HttpSession session = request.getSession();
			Utilisateur user = (Utilisateur) session.getAttribute(ATT_SESSION_USER);
			Objet.emprunterObjet(user, id, qtite);
			request.setAttribute("success_message", "Emprunt réussi : " + qtite + " " + Objet.getIntituleFromId(id));
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("warning_message", "Erreur : " + e.getMessage());
		}

		this.doGet(request, response);
	}

}
