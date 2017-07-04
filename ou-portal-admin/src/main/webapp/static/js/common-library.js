function autoHide(element_selector, mili_second){
	setTimeout(function(){
		$(element_selector).fadeOut();
	},mili_second);
}