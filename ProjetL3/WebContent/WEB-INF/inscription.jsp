<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>Gestion de stocks</title>
<link type="text/css" rel="stylesheet" href="form.css" />
</head>
<body>
	<div class="container">
		<form method="post" action="inscription">
			<fieldset>
				<legend>Inscription</legend>
				<p>Vous pouvez vous inscrire via ce formulaire.</p>

				<div class="form-group row">
					<label for="email" class="col-sm-3 col-form-label">
						Adresse email <span class="requis">*</span>
					</label>
					<div class="col-sm-5">
						<input class="form-control" type="email" id="email" name="email" value="<c:out value="${utilisateur.email}"/>"
							size="20" maxlength="60" />
						<span class="erreur">${form.erreurs['email']}</span> <br />
					</div>
				</div>

				<div class="form-group row">
					<label for="motdepasse" class="col-sm-3 col-form-label">
						Mot de passe <span class="requis">*</span>
					</label>
					<div class="col-sm-5">
						<input class="form-control" type="password" id="motdepasse" name="motdepasse" value="" size="20" maxlength="20" />
						<span class="erreur">${form.erreurs['motdepasse']}</span> <br />
					</div>
				</div>

				<div class="form-group row">
					<label for="confirmation" class="col-sm-3 col-form-label">
						Confirmation du mot de passe <span class="requis">*</span>
					</label>
					<div class="col-sm-5">
						<input class="form-control" type="password" id="confirmation" name="confirmation" value="" size="20"
							maxlength="20" />
						<span class="erreur">${form.erreurs['confirmation']}</span> <br />
					</div>
				</div>

				<div class="form-group row">
					<label for="nom" class="col-sm-3 col-form-label">Nom d'utilisateur</label>
					<div class="col-sm-5">
						<input class="form-control" type="text" id="nom" name="nom" value="<c:out value="${utilisateur.nom}"/>" size="20"
							maxlength="20" />
						<span class="erreur">${form.erreurs['nom']}</span> <br />
					</div>
				</div>
				
				<div class="col-sm-offset-3 col-sm-9">
					<input class="btn btn-primary" type="submit" value="Inscription" class="sansLabel" />
				</div>
				<br />

				<p class="${empty form.erreurs ? 'succes' : 'erreur'}">${form.resultat}</p>
			</fieldset>
		</form>
	</div>
</body>
</html>