$(function() {
  $("form[name='login-form']").validate({
    rules: {
      login: {
        required: true,
        minlength: 3
      },
      password: {
        required: true,
        minlength: 5
      }
    },
    messages: {
      login: {
        required: "Please provide a login",
        minlength: "Your login must be at least 3 characters long"
      },    
      password: {
        required: "Please provide a password",
        minlength: "Your password must be at least 5 characters long"
      },
    },
    submitHandler: function(form) {
      form.submit();
    },
    errorPlacement: function(error,element){
      error.appendTo( element.parent("td").next("td") );
    }
  });
});