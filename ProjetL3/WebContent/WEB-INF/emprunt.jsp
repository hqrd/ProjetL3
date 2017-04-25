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
	<div style="padding: 3rem 1rem">
  		<form class="well center-block" style="max-width: 35rem" method="post" action="Reservation">
    		<legend class="text-center">Emprunt</legend>
   			<div class="form-group has-float-label">
     			${tab}
   			</div>
   			<br/>
      			<div class="col-md-4">
 					<div class="input-group spinner">
   					<input type="text" class="form-control" id="nb" name="nb" value="1" min="0" max="40">
   						<div class="input-group-btn-vertical">
     							<button class="btn btn-default" type="button"><span class="glyphicon glyphicon-chevron-up" aria-hidden="true"></span></button>
						    <button class="btn btn-default" type="button"><span class="glyphicon glyphicon-chevron-down" aria-hidden="true"></span></button>
						</div>
				</div>
 					<p class="help-block">Min 0 - Max 40.</p>
 				</div>
   			<br/>
   			<div>
		      <button class="btn btn-block btn-primary" type="submit">Emprunter</button>
		    </div>
   			<span class="erreur">${err_emprunt}</span>
  		</form>
	</div>
</body>
<link href="<c:url value="/resources/bootstrap/css/bootstrap.css" />" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<c:url value="/resources/jquery.js" />" ></script>
<script type="text/javascript" src="<c:url value="/resources/bootstrap/js/bootstrap.min.js" />" ></script>
<script>
	$(document).ready(function() {

	});
	$(function(){

	    $('.spinner .btn:first-of-type').on('click', function() {
	      var btn = $(this);
	      var input = btn.closest('.spinner').find('input');
	      if (input.attr('max') == undefined || parseInt(input.val()) < parseInt(input.attr('max'))) {    
	        input.val(parseInt(input.val(), 10) + 1);
	      } else {
	        btn.next("disabled", true);
	      }
	    });
	    $('.spinner .btn:last-of-type').on('click', function() {
	      var btn = $(this);
	      var input = btn.closest('.spinner').find('input');
	      if (input.attr('min') == undefined || parseInt(input.val()) > parseInt(input.attr('min'))) {    
	        input.val(parseInt(input.val(), 10) - 1);
	      } else {
	        btn.prev("disabled", true);
	      }
	    });

	})
</script>
</html>