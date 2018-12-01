(function ($) {
	var addImage = $("#addImage"),
		inputImage = $("#imgHouse"),
		imagePrevew = $("#imageHouse"),
		cancelImage = $("#cancelImage");
	
	var addImage1 = $("#addImage1"),
		inputImage1 = $("#imgSlider"),
		imagePrevew1 = $("#imagePreview1"),
		cancelImage1 = $("#cancelImage1");
	
	var addImage2 = $("#addImage2"),
		inputImage2 = $("#imageStaff"),
		imagePrevew2 = $("#imagePreview2"),
		cancelImage2 = $("#cancelImage2");
	
	initImagePreview(addImage, inputImage, imagePrevew, cancelImage);
	initImagePreview(addImage1, inputImage1, imagePrevew1, cancelImage1);
	initImagePreview(addImage2, inputImage2, imagePrevew2, cancelImage2);
	
	//:: Init image review
	function initImagePreview(addImage, inputImage, imagePrevew, cancelImage){
		addImage.click(function(){
			inputImage.click();
		});
		
		inputImage.on('change', function () {
//			readURLSingle(this, imagePrevew);
			readURLMultiple(this, imagePrevew);
	    });
	    cancelImage.on('click',function(){
	    	imagePrevew.find('.previewAreaLoad').remove();
	    	imagePrevew.find('.previewArea').remove();
	    	inputImage.val('');
	    });
	}
	//:: Get image review
	function readURLSingle(input, imagePrevew) { 
		var countImg = input.files.length;
		var strAppend = "";
		
	    if (countImg > 0) {
	    	// :: Single
	    	imagePrevew.find('.previewArea').remove();
	    	var reader = new FileReader();
	    	reader.onload = function (e) {
	    		strAppend = '<div class="previewArea"><img class="previewImage" src="'+ e.target.result +'"/></div>';
	    		imagePrevew.append(strAppend);
	    		strAppend = "";
	        };
	        reader.readAsDataURL(input.files[0]);
	    }
	}
	
	//:: Get image review
	function readURLMultiple(input, imagePrevew, colMd) { 
		var countImg = input.files.length;
		var strAppend = "", i = 0;
		
	    if (countImg > 0) {
	    	// :: Multiple
	    	imagePrevew.find('.previewArea').remove();
	    	
	    	for (i; i < countImg; i++){
	    		var reader = new FileReader();
		    	reader.onload = function (e) {
		    		strAppend = '<div class="previewArea col-md-3"><img class="previewImage" src="'+ e.target.result +'"/></div>';
		    		imagePrevew.append(strAppend);
		    		strAppend = "";
		        };
		        reader.readAsDataURL(input.files[i]);
	    	}
	    }
	}
	
})(jQuery);


