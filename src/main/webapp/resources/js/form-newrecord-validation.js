$(function() {
  var $newrecordform = $("form[name='newrecord-form']"); 
  $newrecordform.submit(function(event){
	 event.preventDefault(); 
  }).validate({
    rules: {
      firstName: {
	      required: true,
	      minlength: 4
      },
      lastName: {
	      required: true,
	      minlength: 4
      },
      patronymic: {
    	  required: true,
	      minlength: 4
	  },
      mobilePhone: {
    	  required: true,
    	  regexmob:true
	  },
	  homePhone: {
		  regexhome:true
	  },
      email:{
    	  email:true
      }
    
    },
    messages: {
      firstName: {
          required: "This field is required",
          minlength: "First name must be at least 4 characters long"
      },    
      lastName: {
    	  required: "This field is required",
    	  minlength: "Last name be at least 4 characters long"
      },
      patronymic: {
          required: "This field is required",
          minlength: "Patronymic must be at least 4 characters long"
      },
      mobilePhone: {
          required: "This field is required",
      },
      email: {
    	  email: "Please input valid email"
      }
    },
    submitHandler: function(form) {
    	var firstName=$(form).find("input[name=firstName]").val(),
			lastName=$(form).find("input[name=lastName]").val(),
			patronymic=$(form).find("input[name=patronymic]").val(),
			mobilePhone=$(form).find("input[name=mobilePhone]").val(),
			homePhone=$(form).find("input[name=homePhone]").val(),
			street=$(form).find("input[name=street]").val(),
			email=$(form).find("input[name=email]").val();
		var header="X-CSRF-TOKEN",
			token = $(form).find("input[name=_csrf]").val();
		$( document ).ajaxSend(function( event, jqxhr, settings ) {
			jqxhr.setRequestHeader(header,token);
		});
		$.post($(form).attr("action")+"ajax",{
				"firstName":firstName,
				"lastName":lastName,
				"patronymic":patronymic,
				"mobilePhone":mobilePhone,
				"homePhone":homePhone,
				"street":street,
				"email":email
				},
				function(result){
				  display(result);
				})
		.fail(function(jqXHR, textStatus, errorThrown){
			console.log("Error:"+jqXHR.responseText);
		});
		return false;
    },
    errorPlacement: function(error,element){
      var errortext=error.text();
      console.log("Form new-record error:"+errortext);
      element.parent(".form-group").children(".errorlabel").remove();
      var $div=$("<div>",{class:"errorlabel"}).append(error);
      $div.appendTo( element.parent(".form-group"));
    },
    success: function(label){
    	console.log("Delete html code after passing validation:"+label.parent(".errorlabel").html());
    	label.parent(".errorlabel")[0].remove();
    }
  });
  
  
  
  var $homephonefield = $newrecordform.find(".home-phone");
  /*this method is needed for deleting triangle(::before error label) 
  when user erase all symbols in homePhone field*/
  $homephonefield.keyup(function(e){
		var key = e.keyCode;
		//console.log("Key with code="+key+" is pressed");
		if((key==8 || key==46) && 
			$homephonefield.val().length==0){
			console.log("homePhone is empty");
			if(typeof $homephonefield.nextAll(".errorlabel")[0]!=="undefined"){
				//console.log("Error label of homePhone field is not undefined");
				$homephonefield.nextAll(".errorlabel")[0].remove();
			}
		}
	});

  
  $.validator.addMethod("regexmob",function(value,element){
	 return this.optional(element) ||  new RegExp("^[+]380[(](50|63|6[6-8]|70|73|9\\d)[)]\\d{7}$").test(value);
  },"Please input valid phone number");
  $.validator.addMethod("regexhome",function(value,element){
	 return this.optional(element) ||  new RegExp("^([+]380[(](\\d{2,4})[)]\\d{7}$)").test(value);
  },"Please input valid phone number");
  
  function display(result) {
		console.log("Result from ajax request to add new record:"+result);
		$div=$("<div>",{class:"alert alert-success alert-dismissible"})
				.html('<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>')
				.append(result);
		$div.appendTo($(".resultdiv > .resultmsg"));
		$newrecordform.find("input").val("");
		$(".back-button").trigger("click");
	}
});