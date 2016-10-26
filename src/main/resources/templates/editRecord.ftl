<#ftl encoding="UTF-8">
<#import "/spring.ftl" as spring>
<html>
<head>
	<#include "/general.ftl">
	<title><@spring.message "title.edit"/></title>
</head>
<body>
	<div class="container">
		<div class="panel panel-default">
			<#include "/fragments/top.ftl">
			<#include "/fragments/header.ftl">
			<div class="panel-body" style = "margin: 0px">
				<h3><@spring.message "form.edit.name"/>:</h3>
				<form action = "<@spring.url "/phonebook/updateRecord"/>" method = "Post" role = "form">
						<div class="form-group col-xs-5" >
	
								<@spring.bind "record.firstName"/>
								<input type = "text" name = "${spring.status.expression}" 
								value = "${spring.status.value!""}" class="form-control" 
								placeholder = "<@spring.message "phonebookrecord.placeholder.firstName"/>"/>
								</br>
								<#list spring.status.errorMessages as error>
									<div class="alert alert-warning">${error}</div>
								</#list>
	
								<@spring.bind "record.lastName"/>
								<input type = "text" name = "${spring.status.expression}" 
								value = "${spring.status.value!""}" class="form-control" 
								placeholder = "<@spring.message "phonebookrecord.placeholder.lastName"/>"/>
								<br>
								<#list spring.status.errorMessages as error>
									<div class="alert alert-warning">${error}</div>
								</#list>
	
								<@spring.bind "record.patronymic"/>
								<input type = "text" name = "${spring.status.expression}" 
								value = "${spring.status.value!""}" class="form-control" 
								placeholder = "<@spring.message "phonebookrecord.placeholder.patronymic"/>"/>
								<br>
								<#list spring.status.errorMessages as error>
									<div class="alert alert-warning">${error}</div>
								</#list>
								
								<@spring.bind "record.mobilePhone"/>
								<input type = "text" name = "${spring.status.expression}" 
								value = "${spring.status.value!""}" class="form-control" 
								placeholder = "<@spring.message "phonebookrecord.placeholder.mobilePhone"/>"/>
								<br>
								<#list spring.status.errorMessages as error>
									<div class="alert alert-warning">${error}</div>
								</#list>
								
								<@spring.bind "record.homePhone"/>
								<input type = "text" name = "${spring.status.expression}" 
								value = "${spring.status.value!""}" class="form-control" 
								placeholder = "<@spring.message "phonebookrecord.placeholder.homePhone"/>"/>
								<br>
								<#list spring.status.errorMessages as error>
									<div class="alert alert-warning">${error}</div>
								</#list>

								<@spring.bind "record.street"/>
								<input type = "text" name = "${spring.status.expression}" 
								value = "${spring.status.value!""}" class="form-control" 
								placeholder = "<@spring.message "phonebookrecord.placeholder.street"/>"/>
								<br>
								<#list spring.status.errorMessages as error>
									<div class="alert alert-warning">${error}</div>
								</#list>
								
								<@spring.bind "record.email"/>
								<input type = "text" name = "${spring.status.expression}" 
								value = "${spring.status.value!""}" class="form-control" 
								placeholder = "<@spring.message "phonebookrecord.placeholder.email"/>"/>
								<br>
								<#list spring.status.errorMessages as error>
									<div class="alert alert-warning">${error}</div>
								</#list>
								
								<@spring.bind "record.id"/>
								<input type="hidden" name = "${spring.status.expression}" value="${spring.status.value}"/>
								<button type = "submit" class="btn btn-info">
									<@spring.message "form.edit.button"/>
								</button>
								<input type="hidden"
										name="${_csrf.parameterName}"
										value="${_csrf.token}"/>
								<a href = "<@spring.url "/phonebook"/>"><@spring.message "cancel"/></a>
							</div>
					</form>
			</div>
		</div>
	</div>
</body>
</html>