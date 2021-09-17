/*
Template Name: STUDIO - Responsive Bootstrap 4 Admin Template
Version: 2.0.0
Author: Sean Ngu
Website: http://www.seantheme.com/studio/
*/

var handleRenderSummernote = function() {
	var totalHeight = ($(window).width() >= 767) ? $(window).height() - $('.summernote').offset().top - 63 : 400;
	$('.summernote').summernote({
		height: totalHeight
	});
};

var handleEmailTagsInput = function() {
	$('#email-to').tagit({
		availableTags: ["admin2@studio.com", "admin3@studio.com", "admin4@studio.com", "admin5@studio.com", "admin6@studio.com", "admin7@studio.com", "admin8@studio.com"]
	});
	$('#email-cc').tagit({
		availableTags: ["admin2@studio.com", "admin3@studio.com", "admin4@studio.com", "admin5@studio.com", "admin6@studio.com", "admin7@studio.com", "admin8@studio.com"]
	});
	$('#email-bcc').tagit({
		availableTags: ["admin2@studio.com", "admin3@studio.com", "admin4@studio.com", "admin5@studio.com", "admin6@studio.com", "admin7@studio.com", "admin8@studio.com"]
	});
};


/* Controller
------------------------------------------------ */
$(document).ready(function() {
	handleRenderSummernote();
	handleEmailTagsInput();
});