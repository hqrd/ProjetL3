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
		<form method="post" action="<%=request.getContextPath()%>/admin/Ajouter-objet">
			<fieldset>
				<legend>Ajouter un objet</legend>
				<p>Vous pouvez ajouter un objet via ce formulaire.</p>

				<div class="form-group row">
					<label for="intitule" class="col-sm-3 col-form-label">Intitulé de l'objet <span class="requis">*</span>
					</label>
					<div class="col-sm-5">
						<input class="form-control" type="text" id="intitule" name="intitule"
							value="<c:out value="${utilisateur.email}"/>" size="20" maxlength="60" />
						<br />
					</div>
				</div>

				<div class="form-group row">
					<label for="quantite" class="col-sm-3 col-form-label"> Quantité de l'objet <span class="requis">*</span>
					</label>
					<div class="col-sm-5">
						<input class="form-control" type="number" id="quantite" name="quantite" value="" size="20" maxlength="20" />
						</br>
					</div>
				</div>



				<div class="col-sm-offset-3 col-sm-9">
					<input class="btn btn-primary" type="submit" value="Ajouter" class="sansLabel" />
				</div>
				<br />

			</fieldset>
		</form>
	</div>
</body>
</html>