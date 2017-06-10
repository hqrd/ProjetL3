package filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import entities.Utilisateur;

@WebFilter(urlPatterns = "/admin/*")
public class RestrictionAdminFilter implements Filter {
	public static final String	ACCES_CONNEXION		= "/accueil";
	public static final String	ACCES_ACCUEIL		= "/accueil";
	public static final String	ATT_SESSION_USER	= "sessionUtilisateur";

	public void init(FilterConfig config) throws ServletException {
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		/* Cast des objets request et response */
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		/* Non-filtrage des ressources statiques */
		// String chemin = request.getRequestURI().substring(request.getContextPath().length());

		/* Récupération de la session depuis la requête */
		HttpSession session = request.getSession();

		/**
		 * Si l'objet utilisateur n'existe pas dans la session en cours, alors
		 * l'utilisateur n'est pas connecté.
		 */
		if (session.getAttribute(ATT_SESSION_USER) == null) {
			/* Redirection vers la page publique */
			request.getRequestDispatcher(ACCES_CONNEXION).forward(request, response);
		} else {
			Utilisateur user = (Utilisateur) session.getAttribute(ATT_SESSION_USER);
			if (user.isAdmin()) {
				chain.doFilter(request, response);
			} else {
				request.getRequestDispatcher(ACCES_ACCUEIL).forward(request, response);
			}
		}
	}

	public void destroy() {
	}
}