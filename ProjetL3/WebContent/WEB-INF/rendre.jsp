<%@ page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta charset="utf-8" />
<title>Test</title>
<link type="text/css" rel="stylesheet" href="form.css" />

</head>
<body>

	<div class="container">
		<span class="erreur">${form.erreurs['nom']}</span>
		 <span class="erreur">${form.erreurs['motdepasse']}</span>
		<p class="${empty form.erreurs ? 'succes' : 'erreur'}">${form.resultat}</p>
                
        <c:if test="${!empty sessionScope.sessionUtilisateur}">
        <%-- Si l'utilisateur existe en session, alors on affiche son adresse email. --%>
        <p class="succes">Vous êtes connecté(e) avec le nom d'utilisateur : ${sessionScope.sessionUtilisateur.nom}</p>
        </c:if>
		
		<h2>Liste des objets</h2>
	${tab}
	</div>
	
	
	
</body>
<link href="<c:url value="/resources/bootstrap/css/bootstrap.css" />" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<c:url value="/resources/jquery.js" />" ></script>
<script type="text/javascript" src="<c:url value="/resources/bootstrap/js/bootstrap.min.js" />" ></script>
<script>
	$(document).ready(function() {

	});
	
</script>
</html>