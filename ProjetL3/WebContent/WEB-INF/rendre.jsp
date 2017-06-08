<%@ page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta charset="utf-8" />
<title>Gestion de stocks</title>


</head>
<body>

	<div class="container">
		<span class="erreur">${form.erreurs['nom']}</span> <span class="erreur">${form.erreurs['motdepasse']}</span>
		<p class="${empty form.erreurs ? 'succes' : 'erreur'}">${form.resultat}</p>

		<h2>Liste des objets</h2>
		${tab}
	</div>



</body>
<script>
    $(document).ready(function() {
	$('#histo').DataTable({
	    "aaSorting" : []
	});
    });
</script>
</html>