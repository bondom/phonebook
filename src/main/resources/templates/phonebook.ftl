<#ftl encoding="UTF-8">
<#setting url_escaping_charset='utf-8'> 
<#import "/spring.ftl" as spring>
<html>
<head>
	<#include "/general.ftl">
	<title><@spring.message "title.phonebook"/></title>
</head>
<body id="bootstrap-overrides">
	<div class="container">
		<div class="panel panel-primary">
			<#include "/fragments/header.ftl">
			<div class="panel-body">
				
				
				<div class="row resultdiv">
					<div class="col-md-2"></div>
					<div class="col-md-8 resultmsg">
						<#if success??>
							<div class="alert alert-success alert-dismissible">
								<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
								${success}
							</div>
						</#if>
					</div>
					<div class="col-md-2"></div>
				</div>
				
				<div class="row add-rec">
				  	<div class="col-md-12">
	                	<form action = "<@spring.url "/phonebook/addRecord"/>" method = "Post" 
	                	      role = "form" name="newrecord-form" id="newrecord-form">
							<div class="form-group">
								<h4><@spring.message "form.addRecord.name"/>:</h4>
							</div>
							<div class="form-group">
								<@spring.bind "record.firstName"/>
								<input type = "text" name = "${spring.status.expression}" 
								value = "${spring.status.value!""}" class="form-control add-input first-field" 
								placeholder = "<@spring.message "phonebookrecord.placeholder.firstName"/>">
								
								<#list spring.status.errorMessages as error>
									<div class="alert alert-warning">${error}</div>
								</#list>
							</div>
							<div class="form-group">
								<@spring.bind "record.lastName"/>
								<input type = "text" name = "${spring.status.expression}" 
								value = "${spring.status.value!""}" class="form-control add-input" 
								placeholder = "<@spring.message "phonebookrecord.placeholder.lastName"/>"/>
								<#list spring.status.errorMessages as error>
									<div class="alert alert-warning">${error}</div>
								</#list>
							</div>
							<div class="form-group">
								<@spring.bind "record.patronymic"/>
								<input type = "text" name = "${spring.status.expression}" 
								value = "${spring.status.value!""}" class="form-control add-input" 
								placeholder = "<@spring.message "phonebookrecord.placeholder.patronymic"/>"/>
								<#list spring.status.errorMessages as error>
									<div class="alert alert-warning">${error}</div>
								</#list>
							</div>
							<div class="form-group">	
								<@spring.bind "record.mobilePhone"/>
								<input type = "text" name = "${spring.status.expression}" 
								value = "${spring.status.value!""}" class="form-control add-input mobile-phone"
								placeholder = "<@spring.message "phonebookrecord.placeholder.mobilePhone"/>"/>
								<#list spring.status.errorMessages as error>
									<div class="alert alert-warning">${error}</div>
								</#list>
							</div>
							<div class="form-group">	
								<@spring.bind "record.homePhone"/>
								<input type = "text" name = "${spring.status.expression}" 
								value = "${spring.status.value!""}" class="form-control add-input home-phone" 
								placeholder = "<@spring.message "phonebookrecord.placeholder.homePhone"/>"/>
								<#list spring.status.errorMessages as error>
									<div class="alert alert-warning">${error}</div>
								</#list>
							</div>
							<div class="form-group">
								<@spring.bind "record.street"/>
								<input type = "text" name = "${spring.status.expression}" 
								value = "${spring.status.value!""}" class="form-control add-input" 
								placeholder = "<@spring.message "phonebookrecord.placeholder.street"/>"/>
								<#list spring.status.errorMessages as error>
									<div class="alert alert-warning">${error}</div>
								</#list>
							</div>
							<div class="form-group">	
								<@spring.bind "record.email"/>
								<input type = "text" name = "${spring.status.expression}" 
								value = "${spring.status.value!""}" class="form-control add-input" 
								placeholder = "<@spring.message "phonebookrecord.placeholder.email"/>"/>
								<#list spring.status.errorMessages as error>
									<div class="alert alert-warning">${error}</div>
								</#list>
							</div>
							<div class="form-group">
								<button type = "submit" class="btn btn-info">
									<@spring.message "form.addRecord.button"/>
								</button>
								<input type="hidden"
									name="${_csrf.parameterName}"
									value="${_csrf.token}"/>
							</div>
					</form>
	                </div>
	                <div class="back">
	                	<button class="back-button">
						</button>
					</div>
	            </div>
					
					
				
				<div class="row">
					<div class="col-md-2">
						<button class="new-button">
							New
						</button>
					</div>
					<div class="col-md-10 search-col">
						<div class="search-panel">
							<div class="search-form-wrapper">
								<form action = "<@spring.url "/phonebook"/>" method = "Get" 
									class="form-inline search-form">
									<div class="form-group">
										<@spring.bind "filter.firstName"/>
										<input type = "text" name = "${spring.status.expression}" 
										value = "${spring.status.value!""}" class="form-control first-field"
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
										value = "${spring.status.value!""}" class="form-control mobile-phone" 
										placeholder = "<@spring.message "phonebookrecord.search.placeholder.mobilePhone"/>"/>
									</div>
									<button type = "submit" class="btn btn-info">
										<@spring.message "form.search.button"/>
									</button>
									<a href = "<@spring.url "/phonebook"/>">Reset</a>
								</form>
							</div>
							<div style="float:left;">
			                	<input type="button" class="top-search-button">
							</div>
						</div>
				</div>
				<div class="row record-table">
					<div class="col-md-12 table-wrapper">
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
									</form>
									<a href = "<@spring.url "/phonebook/editRecord?id=${phoneBookRecord.id?c}"/>">
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
			</div>
		</div>
	</div>
</body>
</html>