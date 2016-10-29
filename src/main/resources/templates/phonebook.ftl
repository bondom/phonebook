<#ftl encoding="UTF-8">
<#setting url_escaping_charset='utf-8'> 
<#import "/spring.ftl" as spring>
<html>
<head>
	<#include "/general.ftl">
	<title><@spring.message "title.phonebook"/></title>
</head>
<body>
	<div class="container">
		<div class="panel panel-default">
			<#include "/fragments/top.ftl">
			<#include "/fragments/header.ftl">
			<div class="panel-body">
				<div class="row">
					<div class="col-md-2"></div>
					<div class="col-md-8">
						<#if success??>
							<div class="alert alert-success">
								${success}
							</div>
						</#if>
					</div>
					<div class="col-md-2"></div>
				</div>
				
				<div class="row">
					<div class="col-md-2"></div>
					<div class="col-md-10">
						<form action = "<@spring.url "/phonebook"/>" method = "Get" class="form-inline">
							<div class="form-group">
								<@spring.bind "filter.firstName"/>
								<input type = "text" name = "${spring.status.expression}" 
								value = "${spring.status.value!""}" class="form-control" 
								placeholder = "<@spring.message "phonebookrecord.placeholder.firstName"/>"/>
							</div>
							<div class="form-group">
								<@spring.bind "filter.lastName"/>
								<input type = "text" name = "${spring.status.expression}" 
								value = "${spring.status.value!""}" class="form-control" 
								placeholder = "<@spring.message "phonebookrecord.placeholder.lastName"/>"/>
							</div>
							<div class="form-group">
								<@spring.bind "filter.mobilePhone"/>
								<input type = "text" name = "${spring.status.expression}" 
								value = "${spring.status.value!""}" class="form-control" 
								placeholder = "<@spring.message "phonebookrecord.search.placeholder.mobilePhone"/>"/>
							</div>
							<button type = "submit" class="btn btn-info">
								<@spring.message "form.search.button"/>
							</button>
							<a href = "<@spring.url "/phonebook"/>">Reset</a>
			
						</form>
					</div>
				</div>
				
				<div class="row">
					<div class="col-md-12">
					<table class="table table-striped">
	                    <#list phoneBook>
	                    	 <#assign counter=initialId>
	                    	 <thead>
		                        <tr>
		                            <td>#</td>
		                            <td><@spring.message "phonebookrecord.table.firstName"/></td>
		                            <td><@spring.message "phonebookrecord.table.lastName"/></td>
		                            <td><@spring.message "phonebookrecord.table.patronymic"/></td>
		                            <td><@spring.message "phonebookrecord.table.mobilePhone"/></td>    
		                            <td><@spring.message "phonebookrecord.table.homePhone"/></td>
		                            <td><@spring.message "phonebookrecord.table.street"/></td>
		                            <td><@spring.message "phonebookrecord.table.email"/></td> 
		                            <td></td> 
		                            <td></td>                             
		                        </tr>
		                    </thead>
	                    	<#items as phoneBookRecord>
								<tr>
									<td>${counter}</td>
									<td>${phoneBookRecord.firstName}</td>
									<td>${phoneBookRecord.lastName}	</td>					
									<td>${phoneBookRecord.patronymic}</td>
									<td>${phoneBookRecord.mobilePhone}</td>
									<td>${phoneBookRecord.homePhone!""}</td>
									<td>${phoneBookRecord.street!""}</td>
									<td>${phoneBookRecord.email!""}</td>
									<td><form action = "<@spring.url "/phonebook/deleteRecord"/>" method = "Post" role = "form">
										<input name="id" type="hidden" value="${phoneBookRecord.id?c}"/>
										<button type = "submit" class="btn btn-info">
											<@spring.message "form.delete.button"/>
										</button>
										<input type="hidden"
												name="${_csrf.parameterName}"
												value="${_csrf.token}"/>
									</form></td>
									<td><a href = "<@spring.url "/phonebook/editRecord?id=${phoneBookRecord.id?c}"/>">
										<@spring.message "form.edit.button"/></a>
									</td>
								</tr>
								<#assign counter++>
							</#items>
						<#else>
							<#if filtering==true>
								<@spring.message "phonebook.filter.empty"/>
							<#else>
								<@spring.message "phonebook.empty"/>
							</#if>
						</#list>
	                </table>  
	                <#if totalPages gt 1>
						<ul class="pagination">
							<#list 1..totalPages as page>
								<#if filtering==true>
									<#assign pageUrl="/phonebook?page=${page}&firstName=${filter.firstName!''}&lastName=${filter.lastName!''}&mobilePhone=${(filter.mobilePhone!'')?url}">
								<#else>
									<#assign pageUrl="/phonebook?page=${page}">
								</#if>
								<#if page==activePage>
									<li class="active"><a href="<@spring.url pageUrl/>">${page}</a></li>
								<#else>
									<li><a href="<@spring.url pageUrl/>">${page}</a></li>
								</#if>
							</#list>
						</ul>
					</#if>
	                </div>
	              </div>
				  <div class="row">
				  	<div class="col-md-4">
	                	<form action = "<@spring.url "/phonebook/addRecord"/>" method = "Post" role = "form">
						<div class="form-group col-md-12" >
							<h3 class="text-center"><@spring.message "form.addRecord.name"/>:</h3>
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
	
							<button type = "submit" class="btn btn-info">
								<@spring.message "form.addRecord.button"/>
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
	</div>
</body>
</html>