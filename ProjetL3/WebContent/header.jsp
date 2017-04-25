<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<nav class="navbar navbar-default">
    <div class="container"> 

		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed"
				data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="#">Projet L3</a>
		</div>


		<div class="collapse navbar-collapse"
			id="bs-example-navbar-collapse-1">
			<ul class="nav navbar-nav">
				<li class="${class1}"><a href="accueil">Liste ${te1}</a></li>
				 <c:if test="${!empty sessionScope.sessionUtilisateur}">
				<li class="${class2}"><a href="Reservation">Réserver ${te2}</a></li>
				<li class="${class3}"><a href="Rendre">Rendre ${te3}</a></li>
				</c:if>
				
			</ul>
			<c:choose>
 			 <c:when test="${empty sessionScope.sessionUtilisateur}">
			<form id="signin" class="navbar-form navbar-right" role="form" method="post" action="accueil">
            <div class="input-group">
					<span class="input-group-addon"><i
						class="glyphicon glyphicon-user"></i></span>
						<input type="text" class="form-control" placeholder="Login" id="nom" name="nom" value="<c:out value="${utilisateur.nom}"/>" size="20" maxlength="20" />
				</div>
				<div class="input-group">
					<span class="input-group-addon"><i
						class="glyphicon glyphicon-lock"></i></span> <input type="password" class="form-control" id="motdepasse" name="motdepasse" value="" size="20" maxlength="20" placeholder="Password"/>
					
				</div>
	            <input type="submit" value="Connexion" class="btn btn-primary" />              
	        </form>
	        </c:when>
	        <c:otherwise>
			    <ul class="nav navbar-nav navbar-right">
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        <span class="glyphicon glyphicon-user"></span>
                        <strong>${sessionScope.sessionUtilisateur.nom}</strong>
                        <span class="glyphicon glyphicon-chevron-down"></span>
                    </a>
                    <ul class="dropdown-menu">   
            			<li><a  href="logout">Sign Out <span class="glyphicon glyphicon-log-out pull-right"></span></a></li>
                    </ul>
                    
                </li>
            </ul>
			</c:otherwise>
		</c:choose>
		</div>
	</div>
</nav>
