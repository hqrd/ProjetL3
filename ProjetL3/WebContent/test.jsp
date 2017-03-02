<%@ page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta charset="utf-8" />
<title>Test</title>
</head>
<c:import url="header.jsp" />
<body>
	<div class="container">
		<div class="row">
			<div class="col-sm-6">
				<c:set var="locale" scope="session">
					<c:out value="${param.lang}" default="FR" />
				</c:set>
				<p>
					<c:set var="message" value="Salut les zéros !" scope="request" />
					<c:out value="${requestScope.message}" />
					(
					<c:out value="${sessionScope.locale}" />
					)

					<c:remove var="locale" scope="session" />
					<c:out value="${sessionScope.locale}" />

					<c:set target="${coyote}" property="prenom" value="${null}" />
				</p>
				<p>Ceci est une page générée depuis une JSP.</p>
				<p>
					<c:out value="${ requestScope.test }" />
				</p>
				<p>
					Récupération du bean :
					<c:out value="${ coyote.prenom } ${ coyote.nom }" />
				</p>
				<p>

					Récupération de la liste :
					<c:forEach var="item" items="${ liste }">
						<c:out value="${ item }" default="null" />
					</c:forEach>
				</p>
				<p>
					Récupération du jour du mois :
					<c:choose>
						<c:when test="${ jour % 2 == 0 }">
						 Jour pair : <c:out value="${jour}" default="null" />
						</c:when>
						<c:otherwise>
						 Jour impair : <c:out value="${jour}" default="null" />
						</c:otherwise>
					</c:choose>
				</p>
				<%@ page import="java.util.*"%>
				<%
					/* Création de la liste et des données */
					List<Map<String, String>> maListe = new ArrayList<Map<String, String>>();
					Map<String, String> news = new HashMap<String, String>();
					news.put("titre", "Titre de ma première news");
					news.put("contenu", "corps de ma première news");
					maListe.add(news);
					news = new HashMap<String, String>();
					news.put("titre", "Titre de ma seconde news");
					news.put("contenu", "corps de ma seconde news");
					maListe.add(news);
					pageContext.setAttribute("maListe", maListe);
				%>

				<c:forEach items="${maListe}" var="news">
					<div class="news">
						<div class="titreNews">
							<c:out value="${news['titre']}" />
						</div>
						<div class="corpsNews">
							<c:out value="${news['contenu']}" />
						</div>
					</div>
				</c:forEach>


			</div>
			<div class="col-sm-6">
				<p>
					<c:out value="${ param.auteur }" />
				</p>
				<a href="<c:url value="toto" />">lien</a>

			</div>
		</div>
		<h2>Gestion de matériels</h2>
		<p>Voici les stocks disponibles :</p>
		<table class="table table-hover">
			<thead>
				<tr>
					<th>Nom</th>
					<th>Stock</th>
					<th>Dispo</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>Table</td>
					<td>12</td>
					<td><span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
					</td>
				</tr>
				<tr>
					<td>Chaise</td>
					<td>0</td>
					<td><span class="glyphicon glyphicon-remove"
						aria-hidden="true"></span>
				</tr>
				<tr>
					<td>Armoire</td>
					<td>1</td>
					<td><span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
				</tr>
			</tbody>
		</table>
	</div>
</body>
<link href="<c:url value="/resources/bootstrap/css/bootstrap.css" />" rel="stylesheet"
	type="text/css" />
<script type="text/javascript" src="<c:url value="/resources/jquery.js" />" />
<script type="text/javascript"
	src="<c:url value="/resources/bootstrap/js/bootstrap.min.js" />" />
<script>
	$(document).ready(function() {

	});
</script>
</html>