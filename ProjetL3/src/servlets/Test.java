package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import beans.Objet;

public class Test extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//String message = "Transmission de variables : OK !";

//		Utilisateur premierBean = new Utilisateur();
//		premierBean.setNom( "Coyote" );
//		premierBean.setPrenom( "Wile E." );
//		
//		List<Integer> premiereListe = new ArrayList<Integer>();
//		premiereListe.add( 27 );
//		premiereListe.add( 12 );
//		premiereListe.add( 138 );
//		premiereListe.add( 6 );
//		
//		DateTime dt = new DateTime();
//		Integer jourDuMois = dt.getDayOfMonth();
//			
//		request.setAttribute( "liste", premiereListe );
//		request.setAttribute( "jour", jourDuMois );
//		//request.setAttribute( "test", message );
//		request.setAttribute( "coyote", premierBean );
		Objet o = new Objet();
		o.listeObjet(request);
		this.getServletContext().getRequestDispatcher("/WEB-INF/test.jsp").forward(request, response);
	}
}
