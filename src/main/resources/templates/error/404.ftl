<#ftl encoding="UTF-8">
<#setting url_escaping_charset='utf-8'> 
<#import "/spring.ftl" as spring>
<html>
<head>
	<#include "/general.ftl">
	<title><@spring.message "title.notfound"/></title>
</head>
<body>
	<div class="container">
		<div class="panel panel-default">
			<#include "/fragments/top.ftl">
			<#include "/fragments/header.ftl">
			<div class="panel-body">
				<div class="alert alert-danger">
					<h1>404 - Page Not Found</h1>
					<h2>Back to <a href = "<@spring.url "/phonebook"/>">main</a> page</h2>
				</div>
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