
package servlets;

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import beans.Objet;

public class Supprimer extends HttpServlet {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	public static final String	ATT_SESSION_USER	= "sessionUtilisateur";

	public static final String	VUE					= "/WEB-INF/accueil" + ".jsp";

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Enumeration<String> parametres = request.getParameterNames();
		while (parametres.hasMoreElements()) {
			String param = parametres.nextElement();
			
			System.out.println(param);

			String id_tmp = (request.getParameter(param));
			int id = (int) Integer.parseInt(id_tmp);
			try {
				Objet.supprimerObjet(id);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		Objet.listeObjet(request);
		this.getServletContext().getRequestDispatcher(VUE).forward(request, response);
	}

}
