<div class="panel-header">
	<div class="row">
		<div class="col-md-10"></div>
		<div class="col-md-2">
			<a href = "javascript:formSubmit()" class="btn btn-primary" role="button"><@spring.message "logout"/></a>
		</div>
	</div>
	<form action = "<@spring.url "/logout"/>" method = "post" id = "logoutForm">
		<input type = "hidden" 
				name = "${_csrf.parameterName}"
				value = "${_csrf.token}"/>
	</form>
	<script>
		function formSubmit() {
			document.getElementById("logoutForm").submit();
		}
	</script>
</div>
