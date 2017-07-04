$(function() {

    $('#side-menu').metisMenu();

});

//Loads the correct sidebar on window load,
//collapses the sidebar on window resize.
// Sets the min-height of #page-wrapper to window size
$(function() {
    $(window).bind("load resize", function() {
        topOffset = 110;
        width = (this.window.innerWidth > 0) ? this.window.innerWidth : this.screen.width;
        if (width < 768) {
        	$('.sidebar_switch').hide();
            $('div.navbar-collapse').addClass('collapse');
            topOffset = 100; // 2-row-menu
        } else {
        	$('.sidebar_switch').show();
           	$('div.navbar-collapse').removeClass('collapse');
        }

        height = ((this.window.innerHeight > 0) ? this.window.innerHeight : this.screen.height) - 1;
        height = height - topOffset;
        if (height < 1) height = 1;
        if (height > topOffset) {
            $("#page-wrapper").css("min-height", (height) + "px");
        }
    });



		//toggle sidebar based on the window size
		if($(window).width() > 769){
			$('body').removeClass('sidebar_hidden');
			$('.sidebar_switch').removeClass('off_switch').addClass('on_switch');
		} else {
			$('body').addClass('sidebar_hidden');
			$('.sidebar_switch').removeClass('on_switch').addClass('off_switch');
		}			 
	 
	 	//* sidebar visibility switch
        $('.sidebar_switch').click(function(){
            $('.sidebar_switch').removeClass('on_switch off_switch');
            if( $('body').hasClass('sidebar_hidden') ) {
                $('body').removeClass('sidebar_hidden');
                $('.sidebar_switch').addClass('on_switch').show();
                $('.sidebar_switch').attr( 'title', "Hide Sidebar" );
            } else {
                $('body').addClass('sidebar_hidden');
                $('.sidebar_switch').addClass('off_switch');
                $('.sidebar_switch').attr( 'title', "Show Sidebar" );
            };
			$(window).resize();
        });

    var url = window.location;
    var element = $('ul.nav a').filter(function() {
        return this.href == url || url.href.indexOf(this.href) == 0;
    }).addClass('active').parent().parent().addClass('in').parent();
    if (element.is('li')) {
        element.addClass('active');
    }
});



$(document).on('blur','.search-by',function() {
	var n = 0;
	$('.search-by :selected').each(function(i, sel){ 
	    n++; 
	});
	
	for(var i=1; i<n; i++) {
		//add textbox
	    var tBox	=	$("<input type='text' class='form-control btm-mrgn-sm txtbox-extra' name='searchKeywords[]'>");
	    
	    $(".search-txtbox-group").append(tBox);
	}
});


$(document).on('click','.btn-reset',function() {
	$('.search-by option').removeAttr('selected');
	$('.txtbox-extra').remove();
});	