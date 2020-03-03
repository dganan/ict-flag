$( document ).ready(function() {

	$("span.has-error").prev("div").addClass("has-error");	
	
	$(".has-error > input").first().focus();
	
	$('.select2me').select2({
        placeholder: "Select",
        allowClear: true
    });
});
