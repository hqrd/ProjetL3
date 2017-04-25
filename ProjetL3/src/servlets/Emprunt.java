

package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import beans.Objet;

public class Emprunt extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String VUE = "/WEB-INF/emprunt.jsp";

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Objet o = new Objet();
		o.selectObj(request);
		request.setAttribute("te2", "<span class='sr-only'>(current)</span>");
		request.setAttribute("class2", "active");
		request.setAttribute("te3", "");
		request.setAttribute("class3", "");
		request.setAttribute("te1", "");
		request.setAttribute("class1", "");
		
		this.getServletContext().getRequestDispatcher(VUE).forward(request, response);

	}
	
	public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
		request.getParameter("objet");
		String[] tmp = request.getParameterValues("objet");
		int id = Integer.parseInt(tmp[0]);
		String qtite_tmp = request.getParameter("nb");
		int qtite = Integer.parseInt(qtite_tmp);
		Objet o = new Objet();
		try {
			o.emprunterObjet(request,id,qtite);
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
			request.setAttribute("err_emprunt", e.getMessage());
		}
		o.selectObj(request);
	    this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
	    }
    
	
}
