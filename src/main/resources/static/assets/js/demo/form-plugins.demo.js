/*
Template Name: STUDIO - Responsive Bootstrap 5 Admin Template
Version: 2.3.0
Author: Sean Ngu
Website: http://www.seantheme.com/studio/
*/

var handleRenderDatepicker = function() {
	$('#datepicker-default').datepicker({
		autoclose: true
	});
	$('#datepicker-component').datepicker({
		autoclose: true
	});
	$('#datepicker-range').datepicker({
		autoclose: true
	});
	$('#datepicker-inline').datepicker({
		autoclose: true
	});
};

var handleDateRangePicker = function() {
	$('#default-daterange').daterangepicker({
		opens: 'right',
		format: 'MM/DD/YYYY',
		separator: ' to ',
		startDate: moment().subtract('days', 29),
		endDate: moment(),
		minDate: '01/01/2012',
		maxDate: '12/31/2018',
	}, function (start, end) {
		$('#default-daterange input').val(start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY'));
	});

	$('#advance-daterange span').html(moment().subtract('days', 29).format('MMMM D, YYYY') + ' - ' + moment().format('MMMM D, YYYY'));

	$('#advance-daterange').daterangepicker({
		format: 'MM/DD/YYYY',
		startDate: moment().subtract(29, 'days'),
		endDate: moment(),
		minDate: '01/01/2012',
		maxDate: '12/31/2015',
		dateLimit: { days: 60 },
		showDropdowns: true,
		showWeekNumbers: true,
		timePicker: false,
		timePickerIncrement: 1,
		timePicker12Hour: true,
		ranges: {
			'Today': [moment(), moment()],
			'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
			'Last 7 Days': [moment().subtract(6, 'days'), moment()],
			'Last 30 Days': [moment().subtract(29, 'days'), moment()],
			'This Month': [moment().startOf('month'), moment().endOf('month')],
			'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
		},
		opens: 'right',
		drops: 'down',
		buttonClasses: ['btn', 'btn-sm'],
		applyClass: 'btn-primary',
		cancelClass: 'btn-default',
		separator: ' to ',
		locale: {
			applyLabel: 'Submit',
			cancelLabel: 'Cancel',
			fromLabel: 'From',
			toLabel: 'To',
			customRangeLabel: 'Custom',
			daysOfWeek: ['Su', 'Mo', 'Tu', 'We', 'Th', 'Fr','Sa'],
			monthNames: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'],
			firstDay: 1
		}
	}, function(start, end, label) {
		$('#advance-daterange span').html(start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY'));
	});
};

var handleRenderTimepicker = function() {
	$('#timepicker-default').timepicker();
	$('#timepicker-seconds').timepicker({
		minuteStep: 1,
		appendWidgetTo: 'body',
		showSeconds: true,
		showMeridian: false,
		defaultTime: false,
		template: false
	});
};

var handleRenderColorpicker = function() {
	$('#colorpicker-default').spectrum({
    showInput: true
	});
	$('#colorpicker-component').spectrum({
    showInput: true
	});
};

var handleRenderTypeahead = function() {
	$('#typeahead-default').typeahead({
		source: [
			{ id: '1', name: 'ActionScript' },
			{ id: '2', name: 'AppleScript' },
			{ id: '3', name: 'Asp' },
			{ id: '4', name: 'BASIC' },
			{ id: '5', name: 'C' },
			{ id: '6', name: 'C++' },
			{ id: '7', name: 'Clojure' },
			{ id: '8', name: 'COBOL' },
			{ id: '9', name: 'ColdFusion' },
			{ id: '10', name: 'Erlang' },
			{ id: '11', name: 'Fortran' },
			{ id: '12', name: 'Groovy' },
			{ id: '13', name: 'Haskell' },
			{ id: '14', name: 'Java' },
			{ id: '15', name: 'JavaScript' },
			{ id: '16', name: 'Lisp' },
			{ id: '17', name: 'Perl' },
			{ id: '18', name: 'PHP' },
			{ id: '19', name: 'Python' },
			{ id: '20', name: 'Ruby' },
			{ id: '21', name: 'Scala' },
			{ id: '22', name: 'Scheme' }
		],
		autoSelect: true
	});
};

var handleRenderTagsInput = function() {
	var elt = '#jquery-tagit';
	
	$(elt).tagit({
		fieldName: 'tags',
		availableTags: ['c++', 'java', 'php', 'javascript', 'ruby', 'python', 'c'],
		autocomplete: {
			delay: 0, 
			minLength: 2
		}
	});
};

var handleRenderBootstrapSlider = function() {
	$('#slider-default').bootstrapSlider();
	$('#slider-range').bootstrapSlider();
	$('#slider-tooltip').bootstrapSlider({
		tooltip: 'always'
	});
	$('#slider-vertical').bootstrapSlider({
		reversed: true
	});
	$('#slider-disabled').bootstrapSlider();
};

var handleRenderMaskedInput = function() {
	$('#masked-input-date').mask('99/99/9999');
	$('#masked-input-phone').mask('(999) 999-9999');
};

var handleRenderSummernote = function() {
	$('.summernote').summernote({
		height: 300
	});
};

var handleRenderjQueryFileUpload = function() {
	$('#fileupload').fileupload({
		previewMaxHeight: 80,
		previewMaxWidth: 120,
		url: 'http://jquery-file-upload.appspot.com/',
		disableImageResize: /Android(?!.*Chrome)|Opera/.test(window.navigator.userAgent),
		maxFileSize: 999000,
		acceptFileTypes: /(\.|\/)(gif|jpe?g|png)$/i
	});
	$('#fileupload').on('fileuploadchange', function (e, data) {
		$('#fileupload .empty-row').hide();
	});
	$('#fileupload').on('fileuploadfail', function(e, data){
		if (data.errorThrown === 'abort') {
			if ($('#fileupload .files tr').not('.empty-row').length == 1) {
				$('#fileupload .empty-row').show();
			}
		}
	});
	
	if ($.support.cors) {
		$.ajax({
			url: 'http://jquery-file-upload.appspot.com/',
			type: 'HEAD'
		}).fail(function () {
			var alert = '<div class="alert alert-danger m-b-0 m-t-15">Upload server currently unavailable - ' + new Date() + '</div>';
			$('#fileupload #error-msg').removeClass('d-none').html(alert);
		});
	}
};

var handleRenderSelectPicker = function() {
	$('#ex-basic').picker();
	$('#ex-multiselect').picker();
	$('#ex-search').picker({ search: true });
};


/* Controller
------------------------------------------------ */
$(document).ready(function() {
	handleRenderDatepicker();
	handleDateRangePicker();
	handleRenderTimepicker();
	handleRenderColorpicker();
	handleRenderTypeahead();
	handleRenderTagsInput();
	handleRenderBootstrapSlider();
	handleRenderMaskedInput();
	handleRenderSummernote();
	handleRenderjQueryFileUpload();
	handleRenderSelectPicker();
});