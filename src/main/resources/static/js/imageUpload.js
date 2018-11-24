(function ($) {
	var addImage1 = $("#addImage1"),
		inputImage1 = $("#imgSlider"),
		imagePrevew1 = $("#imagePreview1"),
		cancelImage1 = $("#cancelImage1");
	
	var addImage2 = $("#addImage2"),
		inputImage2 = $("#imageStaff"),
		imagePrevew2 = $("#imagePreview2"),
		cancelImage2 = $("#cancelImage2");
	
	initImagePreview(addImage1, inputImage1, imagePrevew1, cancelImage1);
	initImagePreview(addImage2, inputImage2, imagePrevew2, cancelImage2);
	
})(jQuery);

//:: Init image review
function initImagePreview(addImage, inputImage, imagePrevew, cancelImage){
	addImage.click(function(){
		inputImage.click();
	});
	
	inputImage.on('change', function () {
		readURL(this, imagePrevew);
    });
    cancelImage.on('click',function(){
    	imagePrevew.find('.previewArea').remove();
    	var strAppend = '<div class="previewArea"><img class="previewImage" src="img/bg-img/noimage256.png"/></div>'
		imagePrevew.append(strAppend);
    	inputImage.val('');
    });
}
//:: Get image review
function readURL(input, imagePrevew) {
	imagePrevew.find('.previewArea').remove();
    if (input.files) {
    	var strAppend = "";
    	
    	var reader = new FileReader();
    	
    	reader.onload = function (e) {
    		strAppend = '<div class="previewArea"><img class="previewImage" src="'+ e.target.result +'"/></div>'
    		imagePrevew.append(strAppend);
    		strAppend = "";
        };
        
        reader.readAsDataURL(input.files[0]);
    }
}
