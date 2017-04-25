<%@ page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta charset="utf-8" />
<title>Test</title>

</head>
<!--<c:import url="header.jsp" />-->
<nav class="navbar navbar-default">
	<div class="container-fluid">

		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
				<span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="#">Projet L3</a>
		</div>


		<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
			<ul class="nav navbar-nav">
				<li class="active"><a href="#">Liste <span class="sr-only">(current)</span></a></li>
				<li><a href="#">Réserver</a></li>
				<li><a href="#">Actions récentes</a></li>
			</ul>
			<form method="post" action="connexion">
				<fieldset>
					<legend>Connexion</legend>
					<p>Vous pouvez vous connecter via ce formulaire.</p>

					<label for="nom">Nom d'utilisateur</label> <input type="text" id="nom" name="nom" value="<c:out value="${utilisateur.nom}"/>" size="20" maxlength="20" /> <span class="erreur">${form.erreurs['nom']}</span> <br /> <label for="motdepasse">Mot de passe <span class="requis">*</span></label> <input type="password" id="motdepasse" name="motdepasse" value="" size="20" maxlength="20" /> <span class="erreur">${form.erreurs['motdepasse']}</span> <br /> <input type="submit" value="Connexion"
						class="sansLabel" /> <br />

					<p class="${empty form.erreurs ? 'succes' : 'erreur'}">${form.resultat}</p>

					<c:if test="${!empty sessionScope.sessionUtilisateur}">
						<%-- Si l'utilisateur existe en session, alors on affiche son adresse email. --%>
						<p class="succes">Vous êtes connecté(e) avec le nom d'utilisateur : ${sessionScope.sessionUtilisateur.nom}</p>
					</c:if>
				</fieldset>
			</form>
		</div>
	</div>
</nav>
<body>


	<div class="container">


		<c:if test="${!empty sessionScope.sessionUtilisateur}">
			<%-- Si l'utilisateur existe en session, alors on affiche son adresse email. --%>
			<p class="succes">Vous êtes connecté(e) avec le nom d'utilisateur : ${sessionScope.sessionUtilisateur.nom}</p>
		</c:if>


	</div>
</body>
<script>
	$(document).ready(function() {

	});
</script>
</html>