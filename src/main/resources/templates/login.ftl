<#ftl encoding="UTF-8">
<#import "/spring.ftl" as spring/>
<html>
<head>
	<#include "/general.ftl">
	<title><@spring.message "title.login"/></title>
</head>
<body>
	<div class="container">
	  	<div class="panel panel-primary">
	  		<#include "/fragments/header-login.ftl">
			<div class="panel-body" style = "margin: 0px">
				<div class="row">
				  <div class="col-md-4"></div>
				  <div class="col-md-4">
				  	<#if RequestParameters.error?? && Session.SPRING_SECURITY_LAST_EXCEPTION??>
					     <div class="alert alert-danger">
							<@spring.message "login.failure"/>
						 </div>
					</#if>
					<#if RequestParameters.logout??>
						<div class="alert alert-success">
							<@spring.message "logout.success"/>
						</div>
					</#if>
					<#if success??>
						<div class="alert alert-success">
							${success}
						</div>
					</#if>
				  </div>
				  <div class="col-md-4"></div>
				</div>
				
				
				<div class="row">
					<div class="col-lg-4 col-md-3 col-sm-3"></div>
					<div class="col-lg-4 col-md-6 col-sm-6">
						<form action = "<@spring.url "/login_process"/>" method = "Post" 
							  role = "form" name="login-form">
							<div class="form-group col-lg-12 col-md-12 col-sm-12 col-xs-12" >
								<h3 class="text-center"><@spring.message "form.login.name"/></h3>
								<table class="login-table">
									<tr class="form-row">
										<td>
											<input type = "text" name = "login"
											class="form-control" id = "login"
											placeholder = "<@spring.message "form.login.placeholder.login"/>" />
										</td><td class="errorlabel"></td>
									</tr>	
									<tr class="form-row">
										<td>
											<input type = "password" id = "password" name = "password" 
											class="form-control" placeholder = "<@spring.message "form.login.placeholder.password"/>"/>
										</td><td class="errorlabel"></td>
									</tr>
									<tr>
										<td>
										<button type = "submit" class="btn btn-info btn-block">
											<@spring.message "form.login.button"/>
										</button>
									
										<input type="hidden"
											name="${_csrf.parameterName}"
											value="${_csrf.token}"/>
										</td>
									</tr>
								</table>
							</div>
						</form>
					</div>
					<div class="col-lg-4 col-md-3 col-sm-3"></div>
				</div>
					
				<div class="row">
					<div class="col-lg-4 col-md-3 col-sm-3"></div>
					<div class="col-lg-4 col-md-6 col-sm-6">
						<form action = "<@spring.url "/registration"/>" method = "Post" role = "form">
						<div class="form-group col-lg-12" >
							<h3 class="text-center"><@spring.message "form.registration.name"/></h3>
							<#if error??>
								<div class="alert alert-danger">
									${error}
								</div>
							</#if>

							<@spring.bind "user.login"/>
							<input type = "text" name = "${spring.status.expression}" 
							value = "${spring.status.value!""}" class="form-control" 
							placeholder = "<@spring.message "form.registration.placeholder.login"/>"/>
							</br>
							<#list spring.status.errorMessages as error>
								<div class="alert alert-danger">${error}</div>
							</#list>

							<@spring.bind "user.password"/>
							<input type = "password" name = "${spring.status.expression}" 
							class="form-control" placeholder = "<@spring.message "form.registration.placeholder.password"/>"/>
							<br>
							<#list spring.status.errorMessages as error>
								<div class="alert alert-danger">${error}</div>
							</#list>

							<@spring.bind "user.fullName"/>
							<input type = "text" name = "${spring.status.expression}" 
							value = "${spring.status.value!""}" class="form-control" 
							placeholder = "<@spring.message "form.registration.placeholder.fullName"/>"/>
							<br>
							<#list spring.status.errorMessages as error>
								<div class="alert alert-danger">${error}</div>
							</#list>
							
							<button type = "submit" class="btn btn-info btn-block">
								<@spring.message "form.registration.button"/>
							</button>
							<input type="hidden"
									name="${_csrf.parameterName}"
									value="${_csrf.token}"/>
						</div>
						</form>
					</div>
					<div class="col-lg-4 col-md-3 col-sm-3"></div>
				</div>
		</div>
	</div>
	
	

</body>
</html>