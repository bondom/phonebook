$(document).ready(function(){
    $('.mobile-phone').tooltip({title:"e.g. +380(XX)XXXXXXX",trigger:"focus"}); 
    $('.home-phone').tooltip({title:"e.g. +380(code)XXXXXXX",trigger:"focus"}); 
    
    $('.top-search-button').click(function(){
    	console.log("Top search button is clicked");
    	$(".search-form-wrapper").css({
    		"border":"1px solid #337ab7",
    		"border-right":"none"
    	});
    	
    	$(".search-form-wrapper").animate({
    		"width":"700px"
    	},"slow",function(){
    		$(".search-form").css("display","block");
    		$(".top-search-button").css("background-color","#337ab7");
    		$(".search-form .first-field").focus();
    	});
    });
    
    $("body").click(function(evt){
    	/*hide search panel if it was focused out*/
    	if($(evt.target).parents(".search-panel").length==0){
    		$(".search-form-wrapper").animate({
        		"width":"0px"
        	},"slow",function(){
        		$(".search-form").css("display","none");
        		$(".search-form-wrapper").css("border","none");
        		$(".top-search-button").css("background-color","white");
        	});
    	}
    	
    });
    
    $('.new-button').mouseover(
    	function(){
    		console.log("New button is mouse overed");
    		$(this).animate({
    			"border-top-right-radius":"20px",
    			"border-bottom-right-radius":"20px",
    			"padding-left":"40px"
			},200,function(){$(this).css("background-color","#337ab7")});
    	}
    );
    $('.new-button').mouseout(
    	function(){
    		console.log("New button is mouse out");
    		$(this).animate({
    			"border-radius":"0px",
    			"padding-left":"20px"
			},"fast",function(){$(this).css("background-color","white")});
    	}
     );
    $('.new-button').click(function(){
    	console.log("New button is clicked");
    	$('.new-button').css("background-color","#337ab7");
		$(".add-rec").animate({
			width:"480px",
		},700,function(){
			$(".add-rec .first-field").focus();
			$(".add-rec").css("overflow","visible");
		});
		$(".add-rec").css({
			"border":"1px solid #337ab7",
			"border-left":"none",
		});
    });
    $('.back-button').mouseover(
    	function(){
    		console.log("Back button is mouse overed");
    		$(this).css("background-color","#337ab7");
    	}
    );
    $('.back-button').mouseout(
    	function(){
    		console.log("Back button is mouse out");
    		$(this).css("background-color","#BEDBF5");	
    	}
     );
    $('.back-button').click(function(){
    	console.log("Back button is clicked");
    	$(".add-rec").css("overflow","hidden");
		$(".add-rec").animate({
			width:"0px"
		},700,function(){
			$('.new-button').css("background-color","white");
			$(".add-rec").css({"border":"none"});
		});
    });
});