
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<nav class="navbar navbar-default">
	<div class="container">

		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed" data-toggle="collapse"
				data-target="#bs-example-navbar-collapse-1">
				<span class="sr-only">Toggle navigation</span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="<%=request.getContextPath()%>/accueil">Projet L3</a>
		</div>


		<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
			<ul class="nav navbar-nav">
				<li class="${class1}"><a href="<%=request.getContextPath()%>/accueil">Liste ${te1}</a></li>
				<c:if test="${!empty sessionScope.sessionUtilisateur}">
					<c:if test="${sessionScope.sessionUtilisateur.getRole() == 'admin'}">
						<li class="${class4}"><a href="<%=request.getContextPath()%>/admin/Ajouter-objet">Ajouter ${te4}</a></li>
					</c:if>
					<li class="${class2}"><a href="<%=request.getContextPath()%>/Reservation">Réserver ${te2}</a></li>
					<li class="${class3}"><a href="<%=request.getContextPath()%>/Rendre">Rendre ${te3}</a></li>
				</c:if>

			</ul>
			<c:choose>
				<c:when test="${empty sessionScope.sessionUtilisateur}">
					<ul class="nav navbar-nav navbar-right">
						<li><a href="<%=request.getContextPath()%>/inscription">S'inscrire</a></li>
					</ul>
					<form id="signin" class="navbar-form navbar-right" role="form" method="post"
						action="<%=request.getContextPath()%>/accueil">
						<div class="input-group
						<c:if test="${!empty form.erreurs['email']}">
								has-error
						</c:if>">
							<span class="input-group-addon">
								<i class="glyphicon glyphicon-user"></i>
							</span>
							<input type="text" class="form-control" placeholder="E-mail" id="email" name="email"
								value="<c:out value="${utilisateur.email}"/>" size="20" maxlength="20" />
						</div>

						<div
							class="input-group 
						<c:if test="${!empty form.erreurs['motdepasse']}">
								has-error
						</c:if>">
							<span class="input-group-addon">
								<i class="glyphicon glyphicon-lock"></i>
							</span>
							<input type="password" class="form-control" id="motdepasse" name="motdepasse" value="" size="20" maxlength="20"
								placeholder="Mot de passe" />
						</div>

						<span class="help-block-navbar help-block">${form.erreurs['email']} ${form.erreurs['motdepasse']}</span>

						<input type="submit" value="Connexion" class="btn btn-primary" />
					</form>
				</c:when>
				<c:otherwise>
					<ul class="nav navbar-nav navbar-right">
						<li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown"> <span
									class="glyphicon glyphicon-user"></span> <strong>${sessionScope.sessionUtilisateur.email}</strong> <span
									class="glyphicon glyphicon-chevron-down"></span>
						</a>
							<ul class="dropdown-menu">
								<li><a href="<%=request.getContextPath()%>/logout">Sign Out <span
											class="glyphicon glyphicon-log-out pull-right"></span></a></li>
							</ul></li>
					</ul>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</nav>

<link href="<c:url value="/resources/bootstrap/css/bootstrap.css" />" rel="stylesheet" type="text/css" />
<link href="<c:url value="/resources/main.css" />" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<c:url value="/resources/jquery.js" />"></script>
<script type="text/javascript" src="<c:url value="/resources/bootstrap/js/bootstrap.min.js" />"></script>
