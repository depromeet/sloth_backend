/*
Template Name: STUDIO - Responsive Bootstrap 4 Admin Template
Version: 2.0.0
Author: Sean Ngu
Website: http://www.seantheme.com/studio/
*/

var handleRenderTags = function() {
	$('#jquery-tagit').tagit({
		fieldName: 'tags',
		availableTags: ['c++', 'java', 'php', 'javascript', 'ruby', 'python', 'c'],
		autocomplete: {
			delay: 0, 
			minLength: 2
		}
	});
};


/* Controller
------------------------------------------------ */
$(document).ready(function() {
	handleRenderTags();
});