<#ftl encoding="UTF-8">
<#import "/spring.ftl" as spring>
<html>
<head>
	<link rel="stylesheet" type = "text/css" href="<@spring.url "/resources/css/bootstrap.min.css"/>"/>         
	<script src="<@spring.url "/resources/js/bootstrap.min.js"/>"></script> 
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
	<div class="container">
		<div class="panel panel-default">
			<div class="panel-body" style = "margin: 0px">
				<a href = "javascript:formSubmit()" class="btn btn-info" role="button">Log out</a>

				<form action = "<@spring.url "/logout"/>" method = "post" id = "logoutForm">
					<input type = "hidden" 
							name = "${_csrf.parameterName}"
							value = "${_csrf.token}"/>
				</form>
				<br><br>

				<h3>Update record:</h3>
				<form action = "<@spring.url "/phonebook/updateRecord"/>" method = "Post" role = "form">
						<div class="form-group col-xs-5" >
	
								<@spring.bind "record.firstName"/>
								<input type = "text" name = "${spring.status.expression}" 
								value = "${spring.status.value!""}" class="form-control" placeholder = "First Name"/>
								</br>
								<#list spring.status.errorMessages as error>
									<div class="alert alert-warning">${error}</div>
								</#list>
	
								<@spring.bind "record.lastName"/>
								<input type = "text" name = "${spring.status.expression}" 
								value = "${spring.status.value!""}"
								class="form-control" placeholder = "Last Name"/>
								<br>
								<#list spring.status.errorMessages as error>
									<div class="alert alert-warning">${error}</div>
								</#list>
	
								<@spring.bind "record.patronymic"/>
								<input type = "text" name = "${spring.status.expression}" 
								value = "${spring.status.value!""}" class="form-control" placeholder = "Patronymic"/>
								<br>
								<#list spring.status.errorMessages as error>
									<div class="alert alert-warning">${error}</div>
								</#list>
								
								<@spring.bind "record.mobilePhone"/>
								<input type = "text" name = "${spring.status.expression}" 
								value = "${spring.status.value!""}" class="form-control" placeholder = "Mobile Phone e.g: +380(XX)XXXXXXX"/>
								<br>
								<#list spring.status.errorMessages as error>
									<div class="alert alert-warning">${error}</div>
								</#list>
								
								<@spring.bind "record.homePhone"/>
								<input type = "text" name = "${spring.status.expression}" 
								value = "${spring.status.value!""}" class="form-control" placeholder = "Home phone"/>
								<br>
								<#list spring.status.errorMessages as error>
									<div class="alert alert-warning">${error}</div>
								</#list>

								<@spring.bind "record.street"/>
								<input type = "text" name = "${spring.status.expression}" 
								value = "${spring.status.value!""}" class="form-control" placeholder = "Street"/>
								<br>
								<#list spring.status.errorMessages as error>
									<div class="alert alert-warning">${error}</div>
								</#list>
								
								<@spring.bind "record.email"/>
								<input type = "text" name = "${spring.status.expression}" 
								value = "${spring.status.value!""}" class="form-control" placeholder = "Email"/>
								<br>
								<#list spring.status.errorMessages as error>
									<div class="alert alert-warning">${error}</div>
								</#list>
								
								<@spring.bind "record.id"/>
								<input type="hidden" name = "${spring.status.expression}" value="${spring.status.value}"/>
								<button type = "submit" class="btn btn-info">
									Update Record
								</button>
								<input type="hidden"
										name="${_csrf.parameterName}"
										value="${_csrf.token}"/>
							</div>
					</form>
				
			</div>
		</div>
	</div>
	
	<script>
		function formSubmit() {
			document.getElementById("logoutForm").submit();
		}
	</script>
</body>
</html>