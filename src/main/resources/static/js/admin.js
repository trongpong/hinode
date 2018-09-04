(function ($) {
    'use strict';
    
    // :: PreventDefault a Click
    $("a[href='#']").on('click', function ($) {
        $.preventDefault();
    });
    
    // :: ScrollUp Active Code
    if ($.fn.scrollUp) {
        $.scrollUp({
            scrollSpeed: 1000,
            easingType: 'easeInOutQuart',
            scrollText: '<i class="fa fa-angle-up" aria-hidden="true"></i>'
        });
    }
    
    // :: Nav Active Code
    if ($.fn.classyNav) {
        $('#southNav').classyNav({
            theme: 'dark'
        });
    }
    
    // :: Table
    $('.column100').on('mouseover',function(){
		var table1 = $(this).parent().parent().parent();
		var table2 = $(this).parent().parent();
		var verTable = $(table1).data('vertable')+"";
		var column = $(this).data('column') + ""; 

		$(table2).find("."+column).addClass('hov-column-'+ verTable);
		$(table1).find(".row100.head ."+column).addClass('hov-column-head-'+ verTable);
	});

	$('.column100').on('mouseout',function(){
		var table1 = $(this).parent().parent().parent();
		var table2 = $(this).parent().parent();
		var verTable = $(table1).data('vertable')+"";
		var column = $(this).data('column') + ""; 

		$(table2).find("."+column).removeClass('hov-column-'+ verTable);
		$(table1).find(".row100.head ."+column).removeClass('hov-column-head-'+ verTable);
	});
	
	// :: Image upload
	var notifyOptions = {
	        iconButtons: {
	            className: 'fa fa-question about',
	            method: function (e, modal) {
	                ssi_modal.closeAll('notify');
	                var btn = $(this).addClass('disabled');
	                ssi_modal.dialog({
	                    onClose: function () {
	                        btn.removeClass('disabled')
	                    },
	                    onShow: function () {
	                    },
	                    okBtn: {className: 'btn btn-primary btn-sm'},
	                    title: 'ssi-modal',
	                    content: 'ssi-modal is an open source modal window plugin that only depends on jquery. It has many options and it\'s super flexible, maybe the most flexible modal out there... For more details click <a class="sss" href="http://ssbeefeater.github.io/#ssi-modal" target="_blank">here</a>',
	                    sizeClass: 'small',
	                    animation: true
	                });
	            }
	        }
	    };

	    // option 1
	    $('#ssi-upload').ssi_uploader({
	        url: 'http://localhost:3300/upload',
	        inForm:true
	    });
	    
})(jQuery);

//:: Date picker
$('#moveabledate').datepicker({
	inline: true,
	dateFormat: 'dd/mm/yy'
});
$('#buildfrom').datepicker({
	inline: true,
	dateFormat: 'dd/mm/yy'
});