/*
Template Name: STUDIO - Responsive Bootstrap 5 Admin Template
Version: 2.3.0
Author: Sean Ngu
Website: http://www.seantheme.com/studio/
*/

function newDate(days) {
	return moment().add(days, 'd').format('D MMM');
}

function newDateString(days) {
	return moment().add(days, 'd').format();
}

Chart.defaults.font.family = FONT_FAMILY;
Chart.defaults.color = COLOR_GRAY_900;
Chart.defaults.plugins.legend.display = false;
Chart.defaults.plugins.tooltip.padding = 8;
Chart.defaults.plugins.tooltip.backgroundColor = hexToRgba(COLOR_GRAY_900, .95);
Chart.defaults.plugins.tooltip.titleFont.family = FONT_FAMILY;
Chart.defaults.plugins.tooltip.titleFont.weight = 600;
Chart.defaults.plugins.tooltip.footerFont.family = FONT_FAMILY;
Chart.defaults.scale.grid.color = COLOR_GRAY_200;
Chart.defaults.scale.beginAtZero = true;

var options = {
	maintainAspectRatio: false,
	elements: {
		line: {
			tension: 0.000001
		}
	},
	legend: {
		display: false
	},
	tooltips: {
		mode: 'nearest',
		callbacks: {
			label: function(tooltipItem, data) {
				var label = data.datasets[tooltipItem.datasetIndex].label || '';

				if (label) {
				label += ': ';
				}
				label += Math.round(tooltipItem.yLabel * 100) / 100;
				label = '$' + label;
				return label;
			},
			labelColor: function(tooltipItem, chart) {
				console.log(tooltipItem.datasetIndex);
				console.log(chart);
				return {
					borderColor: hexToRgba(COLOR_WHITE, .75),
					backgroundColor: chart.data.datasets[tooltipItem.datasetIndex].color
				};
			},
			labelTextColor: function(tooltipItem, chart) {
				return COLOR_WHITE;
			}
		}
	}
};

var handleRenderChart = function() {
	// #chart1
	options.scales = {
		yAxes: [{
			ticks: {
				beginAtZero: true,
				min: 0,
				max: 750,
				stepSize: 250
			}
		}]
	};
	var ctx = document.getElementById('chart1').getContext('2d');
	var chart1 = new Chart(ctx, {
			type: 'line',
			data: {
				labels: ['', '4am', '8am', '12pm', '4pm', '8pm', newDate(1)],
					datasets: [{
						color: COLOR_BLUE,
						backgroundColor: 'transparent',
						borderColor: COLOR_BLUE,
						borderWidth: 2,
						pointBackgroundColor: COLOR_WHITE,
						pointBorderWidth: 2,
						pointRadius: 4,
						pointHoverBackgroundColor: COLOR_WHITE,
						pointHoverBorderColor: COLOR_BLUE,
						pointHoverRadius: 6,
						pointHoverBorderWidth: 2,
						data: [0, 0, 0, 601.5, 220]
					},{
						color: COLOR_GRAY_300,
						backgroundColor: hexToRgba(COLOR_GRAY_300, .2),
						borderColor: COLOR_GRAY_300,
						borderWidth: 2,
						pointBackgroundColor: COLOR_WHITE,
						pointBorderWidth: 2,
						pointRadius: 4,
						pointHoverBackgroundColor: COLOR_WHITE,
						pointHoverBorderColor: COLOR_GRAY_300,
						pointHoverRadius: 6,
						pointHoverBorderWidth: 2,
						data: [0, 0, 0, 500, 120, 0, 0, 0]
					}]
			}, options
	});
	
	// #chart2
	options.scales = {
		yAxes: [{
			ticks: {
				beginAtZero: true,
				min: 0,
				max: 150,
				stepSize: 50
			}
		}]
	};
	var ctx2 = document.getElementById('chart2').getContext('2d');
	var chart2 = new Chart(ctx2, {
		type: 'line',
		data: {
			labels: ['', '4am', '8am', '12pm', '4pm', '8pm', newDate(1)],
				datasets: [{
					color: COLOR_BLUE,
					backgroundColor: 'transparent',
					borderColor: COLOR_BLUE,
					borderWidth: 2,
					pointBackgroundColor: COLOR_WHITE,
					pointBorderWidth: 2,
					pointRadius: 4,
					pointHoverBackgroundColor: COLOR_WHITE,
					pointHoverBorderColor: COLOR_BLUE,
					pointHoverRadius: 6,
					pointHoverBorderWidth: 2,
					data: [0, 20, 50, 100, 120]
				},{
					color: COLOR_GRAY_300,
					backgroundColor: hexToRgba(COLOR_GRAY_300, .2),
					borderColor: COLOR_GRAY_300,
					borderWidth: 2,
					pointBackgroundColor: COLOR_WHITE,
					pointBorderWidth: 2,
					pointRadius: 4,
					pointHoverBackgroundColor: COLOR_WHITE,
					pointHoverBorderColor: COLOR_GRAY_300,
					pointHoverRadius: 6,
					pointHoverBorderWidth: 2,
					data: [0, 30, 44, 130, 34, 15, 43, 22]
				}]
		}, options
});

	// #chart3
	options.scales = {
		yAxes: [{
			ticks: {
				beginAtZero: true,
				min: 0,
				max: 30,
				stepSize: 10
			}
		}]
	};
	var ctx3 = document.getElementById('chart3').getContext('2d');
	var chart3 = new Chart(ctx3, {
		type: 'line',
		data: {
			labels: ['', '4am', '8am', '12pm', '4pm', '8pm', newDate(1)],
				datasets: [{
					color: COLOR_INDIGO,
					backgroundColor: 'transparent',
					borderColor: COLOR_INDIGO,
					borderWidth: 2,
					pointBackgroundColor: COLOR_WHITE,
					pointBorderWidth: 2,
					pointRadius: 4,
					pointHoverBackgroundColor: COLOR_WHITE,
					pointHoverBorderColor: COLOR_INDIGO,
					pointHoverRadius: 6,
					pointHoverBorderWidth: 2,
					data: [0, 0, 5, 18, 9]
				},{
					color: COLOR_TEAL,
					backgroundColor: hexToRgba(COLOR_TEAL, .2),
					borderColor: COLOR_TEAL,
					borderWidth: 2,
					pointBackgroundColor: COLOR_WHITE,
					pointBorderWidth: 2,
					pointRadius: 4,
					pointHoverBackgroundColor: COLOR_WHITE,
					pointHoverBorderColor: COLOR_TEAL,
					pointHoverRadius: 6,
					pointHoverBorderWidth: 2,
					data: [0, 0, 10, 26, 13]
				}]
		}, options
	});

	// #chart4
	options.scales = {
		yAxes: [{
			ticks: {
				beginAtZero: true,
				min: 0,
				max:  60,
				stepSize: 20
			}
		}]
	};
	var ctx4 = document.getElementById('chart4').getContext('2d');
	var chart4 = new Chart(ctx4, {
		type: 'line',
		data: {
			labels: ['', '4am', '8am', '12pm', '4pm', '8pm', newDate(1)],
				datasets: [{
					color: COLOR_BLUE,
					backgroundColor: 'transparent',
					borderColor: COLOR_BLUE,
					borderWidth: 2,
					pointBackgroundColor: COLOR_WHITE,
					pointBorderWidth: 2,
					pointRadius: 4,
					pointHoverBackgroundColor: COLOR_WHITE,
					pointHoverBorderColor: COLOR_BLUE,
					pointHoverRadius: 6,
					pointHoverBorderWidth: 2,
					data: [0, 0, 0, 24, 39]
				},{
					color: COLOR_GRAY_300,
					backgroundColor: hexToRgba(COLOR_GRAY_300, .2),
					borderColor: COLOR_GRAY_300,
					borderWidth: 2,
					pointBackgroundColor: COLOR_WHITE,
					pointBorderWidth: 2,
					pointRadius: 4,
					pointHoverBackgroundColor: COLOR_WHITE,
					pointHoverBorderColor: COLOR_GRAY_300,
					pointHoverRadius: 6,
					pointHoverBorderWidth: 2,
					data: [0, 0, 0, 28, 35, 23, 0, 0]
				}]
		}, options
	});
	
	// #chart5
	options.scales = {
		yAxes: [{
			ticks: {
				beginAtZero: true,
				min: 0,
				max:  15,
				stepSize: 5
			}
		}]
	};
	var ctx5 = document.getElementById('chart5').getContext('2d');
	var chart5 = new Chart(ctx5, {
		type: 'line',
		data: {
			labels: ['', '4am', '8am', '12pm', '4pm', '8pm', newDate(1)],
				datasets: [{
					color: COLOR_BLUE,
					backgroundColor: 'transparent',
					borderColor: COLOR_BLUE,
					borderWidth: 2,
					pointBackgroundColor: COLOR_WHITE,
					pointBorderWidth: 2,
					pointRadius: 4,
					pointHoverBackgroundColor: COLOR_WHITE,
					pointHoverBorderColor: COLOR_BLUE,
					pointHoverRadius: 6,
					pointHoverBorderWidth: 2,
					data: [0, 0, 0, 12, 5]
				},{
					color: COLOR_GRAY_300,
					backgroundColor: hexToRgba(COLOR_GRAY_300, .2),
					borderColor: COLOR_GRAY_300,
					borderWidth: 2,
					pointBackgroundColor: COLOR_WHITE,
					pointBorderWidth: 2,
					pointRadius: 4,
					pointHoverBackgroundColor: COLOR_WHITE,
					pointHoverBorderColor: COLOR_GRAY_300,
					pointHoverRadius: 6,
					pointHoverBorderWidth: 2,
					data: [0, 0, 0, 10, 4, 2, 0, 0]
				}]
		}, options
	});
}

var handleDaterangepicker = function() {
	$('[data-id="prev-date"]').html(moment().add(-1, 'd').format('D MMM YYYY'));
	$('[data-id="today-date"]').html(moment().format('D MMM YYYY'));
	
	$('#daterangepicker').daterangepicker({
    opens: 'right',
    format: 'MM/DD/YYYY',
    separator: ' to ',
    startDate: moment(),
    endDate: moment(),
    maxDate: moment()
  });
};


/* Controller
------------------------------------------------ */
$(document).ready(function() {
	handleRenderChart();
	handleDaterangepicker();
});