<#ftl encoding="UTF-8">
<#import "/spring.ftl" as spring/>
<html>
<head>
	<link rel="stylesheet" type = "text/css" href="<@spring.url "/resources/css/bootstrap.min.css"/>"/>         
	<script src="<@spring.url "/resources/js/bootstrap.min.js"/>"></script> 
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Login Form</title>
</head>
<body>
	<div class="container">
	  	<div class="panel panel-default">
			<div class="panel-body" style = "margin: 0px">
				<#if RequestParameters.error?? && Session.SPRING_SECURITY_LAST_EXCEPTION??>
				     <div class="alert alert-danger">
						${Session.SPRING_SECURITY_LAST_EXCEPTION.message} 
					 </div>
				</#if>
				<#if RequestParameters.logout??>
					<div class="alert alert-success">
						You have been logged out successfully
					</div>
				</#if>
				<#if success??>
					<div class="alert alert-success">
						${success}
					</div>
				</#if>
				
					<div>
					<h3>Login Form</h3>
					<form action = "<@spring.url "/login_process"/>" method = "Post" role = "form">
						<div class="form-group col-xs-5" >
								<input type = "text" name = "login"
								class="form-control" placeholder = "Login" />
								<br/>
								<input type = "password" name = "password" 
								class="form-control" placeholder = "Password"/>
								<br/>

								<button type = "submit" class="btn btn-info">
									Log In
								</button>
								
								<input type="hidden"
									name="${_csrf.parameterName}"
									value="${_csrf.token}"/>
								</form>
							</div>
					</form>
					</div>
					
					<div>
					<h3>Registration</h3>
						<form action = "<@spring.url "/registration"/>" method = "Post" role = "form">
						<div class="form-group col-xs-5" >
								<#if error??>
									<div class="alert alert-danger">${error}</div>
								</#if>
	
								<@spring.bind "user.login"/>
								<input type = "text" name = "${spring.status.expression}" 
								value = "${spring.status.value!""}" class="form-control" placeholder = "Login"/>
								</br>
								<#list spring.status.errorMessages as error>
									<div class="alert alert-warning">${error}</div>
								</#list>
	
								<@spring.bind "user.password"/>
								<input type = "password" name = "${spring.status.expression}" 
								class="form-control" placeholder = "Password"/>
								<br>
								<#list spring.status.errorMessages as error>
									<div class="alert alert-warning">${error}</div>
								</#list>
	
								<@spring.bind "user.fullName"/>
								<input type = "text" name = "${spring.status.expression}" 
								value = "${spring.status.value!""}" class="form-control" placeholder = "Full Name"/>
								<br>
								<#list spring.status.errorMessages as error>
									<div class="alert alert-warning">${error}</div>
								</#list>
								
								<button type = "submit" class="btn btn-info">
									Sign Up
								</button>
								<input type="hidden"
										name="${_csrf.parameterName}"
										value="${_csrf.token}"/>
							</div>
					</form>
					</div>
				</div>
		</div>
	</div>
	
	

</body>
</html>