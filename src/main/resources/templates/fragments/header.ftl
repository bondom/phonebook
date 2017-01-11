<div class="panel-heading top-panel">
	<a href = "#" id="logo" class="btn btn-primary">
	Phonebook
	</a>
	<a href = "javascript:formSubmit()" class="btn btn-primary logout-btn" role="button"><@spring.message "logout"/></a>
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
