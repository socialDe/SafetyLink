<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
.highcharts-figure, .highcharts-data-table table {
  min-width: 360px; 
  max-width: 800px;
  margin: 1em auto;
}

.highcharts-data-table table {
	font-family: Verdana, sans-serif;
	border-collapse: collapse;
	border: 1px solid #EBEBEB;
	margin: 10px auto;
	text-align: center;
	width: 100%;
	max-width: 500px;
}
.highcharts-data-table caption {
  padding: 1em 0;
  font-size: 1.2em;
  color: #555;
}
.highcharts-data-table th {
	font-weight: 600;
  padding: 0.5em;
}
.highcharts-data-table td, .highcharts-data-table th, .highcharts-data-table caption {
  padding: 0.5em;
}
.highcharts-data-table thead tr, .highcharts-data-table tr:nth-child(even) {
  background: #f8f8f8;
}
.highcharts-data-table tr:hover {
  background: #f1f7ff;
}
</style>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script>
$(document).ready(function (){
	displayChart();
});
	
	
	function displayChart(){
		$('#gra1').highcharts({
			  chart: {
			    type: 'line'
			  },
			  title: {
			    text: 'Monthly Average Temperature'
			  },
			  subtitle: {
			    text: 'Source: WorldClimate.com'
			  },
			  xAxis: {
			    categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
			  },
			  yAxis: {
			    title: {
			      text: 'Temperature (°C)'
			    }
			  },
			  plotOptions: {
			    line: {
			      dataLabels: {
			        enabled: true
			      },
			      enableMouseTracking: false
			    }
			  },
			  series: [{
			    name: 'Tokyo',
			    data: [7.0, 6.9, 9.5, 14.5, 18.4, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6]
			  }, {
			    name: 'London',
			    data: [3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8]
			  }, {
			    name: 'Seoul',
			    data: [3.3, 2.2, 5.7, 6.5, 17.9, 13.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8]
			  }]
			});
	}
</script>
</head>
<body>
<script src="https://code.highcharts.com/highcharts.js"></script>
<script src="https://code.highcharts.com/modules/exporting.js"></script>
<script src="https://code.highcharts.com/modules/export-data.js"></script>
<script src="https://code.highcharts.com/modules/accessibility.js"></script>

<figure class="highcharts-figure">
  <div id="gra1"></div>
  <p class="highcharts-description">
    This chart shows how data labels can be added to the data series. This
    can increase readability and comprehension for small datasets.
  </p>
</figure>
</body>
</html>