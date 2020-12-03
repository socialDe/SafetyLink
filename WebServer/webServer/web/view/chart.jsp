<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Target Material Design Bootstrap Admin Template</title>
    	<!-- Bootstrap Js -->
    <script src="view/assets/js/bootstrap.min.js"></script>
	
	<script src="view/assets/materialize/js/materialize.min.js"></script>
	

	
    <!-- Custom Js -->
    <script src="view/assets/js/custom-scripts.js"></script> 
    
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

.ui-datepicker-calendar {
    display: none;
}

#submitButton:focus {
    outline: none;
    background-color: #2bbbad;
    color: #fff;
}

#submitButton {
	float: right;
}
#submitDiv {
	height: 30px;
	
    
}

</style>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>

<!-- datepicker를 사용하기 위한 import -->
<script src="https://code.jquery.com/jquery-1.11.3.js"></script>
<script src="https://code.jquery.com/ui/1.11.3/jquery-ui.js"></script>
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">

<script>
$(document).ready(function (){
	displayChart();
	
	var mform = document.getElementById('filter_form');
	var div1 = document.getElementById('region_list');
	var div2 = document.getElementById('period_list');
	var div3 = document.getElementById('time_list');
	
	// Region으로 Deafault SearchBasis Setting
	div1.style.display= 'block';
	
	// SearchBasis가 클릭될 때마다 다른 Filter div를 display 
	for(var i=0; i<mform.searchBasis.length;i++){
		mform.searchBasis[i].onclick=function(){
			
			if(this.value=='Region'){
				div1.style.display = 'block';
				$('#regionLists').display = 'inline-block';
				div2.style.display = 'none';
				div3.style.display = 'none';
			}else if(this.value=='Period'){
				div1.style.display = 'none';
				div2.style.display = 'block';
				div3.style.display = 'none';
			}else if(this.value=='Time'){
				div1.style.display = 'none';
				div2.style.display = 'none';
				div3.style.display = 'block';
			};
		};
	}
	
	// Gender Filter 체크박스 All은 다른 항목과 함께 사용 불가
	$('#gender_all').change(function(){
		if($('#gender_all').is(':checked')){
			if($('#gender_man').is(':checked')){
				alert('All은 다른 선택과 중복으로 사용할 수 없습니다.');
				$('#gender_man').trigger('click');	
			}
			if($('#gender_woman').is(':checked')){
				alert('All은 다른 선택과 중복으로 사용할 수 없습니다.');
				$('#gender_woman').trigger('click');
			}
		}
	});
	
	// Vehicle Type Filter 체크박스 All은 다른 항목과 함께 사용 불가
	$('#vehicle_all').change(function(){
		if($('#vehicle_all').is(':checked')){
			if($('#sedan').is(':checked')){
				alert('All은 다른 선택과 중복으로 사용할 수 없습니다.');
				$('#sedan').trigger('click');	
			}
			if($('#van').is(':checked')){
				alert('All은 다른 선택과 중복으로 사용할 수 없습니다.');
				$('#van').trigger('click');
			}
			if($('#truck').is(':checked')){
				alert('All은 다른 선택과 중복으로 사용할 수 없습니다.');
				$('#truck').trigger('click');
			}
		}
	});

	
	// 월 단위 datepicker
	$.datepicker.setDefaults({
 		dateFormat: 'yy-mm', // Input Date Format 설정
	    showMonthAfterYear:true, // 년도 먼저 나오고, 뒤에 월 표시
	    showButtonPanel: true,
	    onClose: function(dateText, inst) {
	        function isDonePressed() {
	          return ($('#ui-datepicker-div').html().indexOf('ui-datepicker-close ui-state-default ui-priority-primary ui-corner-all ui-state-hover') > -1);
	        }

	        if (isDonePressed()) {
	          var month = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
	          var year = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
	          $(this).datepicker('setDate', new Date(year, month, 1)).trigger('change');

	          $('.date-picker').focusout() //Added to remove focus from datepicker input box on selecting date
	        }
	      },
	      beforeShow: function(input, inst) {

	        inst.dpDiv.addClass('month_year_datepicker')

	        if ((datestr = $(this).val()).length > 0) {
	          year = datestr.substring(datestr.length - 4, datestr.length);
	          month = datestr.substring(0, 2);
	          $(this).datepicker('option', 'defaultDate', new Date(year, month - 1, 1));
	          $(this).datepicker('setDate', new Date(year, month - 1, 1));
	          $(".ui-datepicker-calendar").hide();
	        }
	      }
	});
	
	// 두 날짜 input을 datepicker로 설정
	$('#startDate').datepicker();
	$('#endDate').datepicker();
	
	//start, end 날짜 Default 설정
	if(div2.style.display == 'block'){
	 	$('#startDate').datepicker('setDate', '-6M');
		$('#endDate').datepicker('setDate', '-1M')
	}
	
	
	
	
	// Gender Filter 체크박스 All은 다른 항목과 함께 사용 불가
	$('#gender_man').change(function(){
		alert("clicked");
	});
	
});
/*
 *  End $(document).ready(function (){}
 */

/*
 *  Chart Display Function Start
 */
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
		      text: '차량 수'
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
		  },{
		    name: 'Busan',
		    data: [7.0, 3.9, 9.5, 14.5, 18.4, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6]
		  }, {
		    name: 'Pohang',
		    data: [3.9, 2.2, 5.0, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8]
		  }, {
		    name: 'Bejing',
		    data: [3.3, 2.2, 5.2, 6.5, 17.9, 13.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8]
		  }]
	});
}
 /*
  *   End Chart Display Function
  */ 

  
  

/*
 *  Start Ajax(Chart Display)
 */

function chartSearch(){
	 var formData = $('#filter_form').serialize();
	 
	 // gender checkbox 선택 결과를 확인(선택되지 않은 항목 포함)
	 var gender_check = 0;
	 $('input:checkbox[name=gender]:checked').each(function(idx,elem){
		gender_check += Number($(elem).val()); 
	 });
	 // man: 22, woman: 33, gender_all: 44
	 // man+woman: 55
	 if(gender_check == 22){
		 gender_check = 'Man';
	 }else if(gender_check == 33){
		 gender_check = 'Woman';
	 }else if(gender_check == 44){
		 gender_check = 'Gender_all';
	 }else if(gender_check == 55){
		 gender_check = 'ManWoman';
	 }
	 
	 formData += '&gender_check='+gender_check;
	 
	 // vehecleType checkbox 선택 결과를 확인(선택되지 않은 항목 포함)
	 var type_check = 0;
	 $('input:checkbox[name=vehicleType]:checked').each(function(idx,elem){
		 type_check += Number($(elem).val()); 
		 console.log('elem:'+type_check);
	 });
	 // sedan: 2, van: 5, truck:8, vehicle_all: 11
	 if(type_check == 2){
		 type_check = 'Sedan';
	 }else if(type_check == 5){
		 type_check = 'Van';
	 }else if(type_check == 7){
		 type_check = 'SedanVan';
	 }else if(type_check == 8){
		 type_check = 'Truck';
	 }else if(type_check == 10){
		 type_check = 'SedanTruck';
	 }else if(type_check == 11){
		 type_check = 'Vehicle_all';
	 }else if(type_check == 13){
		 type_check = 'VanTruck';
	 }else if(type_check == 15){
		 type_check = 'SedanVanTruck';
	 }
	 
	 formData += '&type_check='+type_check;
	 console.log(String(formData));
	 
	 $.ajax({
		 cache:false,
		 url: 'chartSearch.mc',
		 data: formData,
		 success: function(data){
			 console.log("success");
			 console.log(data);
			 console.log(typeof(data));
		 },
		 error : function(request,status,error) {
            	console.log("error");
            	console.log(request.responseText);
            	console.log(" error = "+error)
            	alert("code= "+request.status+" message = "+ request.responseText +" error = "+error);
		 }
	 });
}
 
 
/*
 *  Search Form 체크(공백으로 submit할 때)
 */
function searchFormCheck(){
	chartSearch();
}
</script>
	
	<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
	<link rel="stylesheet" href="view/assets/materialize/css/materialize.min.css" media="screen,projection" />
    <!-- Bootstrap Styles-->
    <link href="view/assets/css/bootstrap.css" rel="stylesheet" />
    <!-- FontAwesome Styles-->
    <link href="view/assets/css/font-awesome.css" rel="stylesheet" />
    <!-- Morris Chart Styles-->
    <link href="view/assets/js/morris/morris-0.4.3.min.css" rel="stylesheet" />
    <!-- Custom Styles-->
    <link href="view/assets/css/custom-styles.css" rel="stylesheet" />
    <!-- Google Fonts-->
    <link href='http://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css' />
    <link rel="stylesheet" href="view/assets/js/Lightweight-Chart/cssCharts.css"> 
</head>
<body>
    <div id="wrapper">
        <nav class="navbar navbar-default top-navbar" role="navigation">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".sidebar-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand waves-effect waves-dark" href="index.html"><i class="large material-icons">track_changes</i> <strong>target</strong></a>
				
		<div id="sideNav" class="waves-effect waves-dark" href=""><i class="material-icons dp48">toc</i></div>
            </div>

            <ul class="nav navbar-top-links navbar-right"> 
				<li><a class="dropdown-button waves-effect waves-dark" href="#!" data-activates="dropdown4"><i class="fa fa-envelope fa-fw"></i> <i class="material-icons right">arrow_drop_down</i></a></li>				
				<li><a class="dropdown-button waves-effect waves-dark" href="#!" data-activates="dropdown3"><i class="fa fa-tasks fa-fw"></i> <i class="material-icons right">arrow_drop_down</i></a></li>
				<li><a class="dropdown-button waves-effect waves-dark" href="#!" data-activates="dropdown2"><i class="fa fa-bell fa-fw"></i> <i class="material-icons right">arrow_drop_down</i></a></li>
				  <li><a class="dropdown-button waves-effect waves-dark" href="#!" data-activates="dropdown1"><i class="fa fa-user fa-fw"></i> <b>John Doe</b> <i class="material-icons right">arrow_drop_down</i></a></li>
            </ul>
        </nav>
		<!-- Dropdown Structure -->
<ul id="dropdown1" class="dropdown-content">
<li><a href="#"><i class="fa fa-user fa-fw"></i> My Profile</a>
</li>
<li><a href="#"><i class="fa fa-gear fa-fw"></i> Settings</a>
</li> 
<li><a href="#"><i class="fa fa-sign-out fa-fw"></i> Logout</a>
</li>
</ul>
<ul id="dropdown2" class="dropdown-content w250">
  <li>
                            <a href="#">
                                <div>
                                    <i class="fa fa-comment fa-fw"></i> New Comment
                                    <span class="pull-right text-muted small">4 min</span>
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a href="#">
                                <div>
                                    <i class="fa fa-twitter fa-fw"></i> 3 New Followers
                                    <span class="pull-right text-muted small">12 min</span>
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a href="#">
                                <div>
                                    <i class="fa fa-envelope fa-fw"></i> Message Sent
                                    <span class="pull-right text-muted small">4 min</span>
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a href="#">
                                <div>
                                    <i class="fa fa-tasks fa-fw"></i> New Task
                                    <span class="pull-right text-muted small">4 min</span>
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a href="#">
                                <div>
                                    <i class="fa fa-upload fa-fw"></i> Server Rebooted
                                    <span class="pull-right text-muted small">4 min</span>
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a class="text-center" href="#">
                                <strong>See All Alerts</strong>
                                <i class="fa fa-angle-right"></i>
                            </a>
                        </li>
</ul>
<ul id="dropdown3" class="dropdown-content dropdown-tasks w250">
<li>
		<a href="#">
			<div>
				<p>
					<strong>Task 1</strong>
					<span class="pull-right text-muted">60% Complete</span>
				</p>
				<div class="progress progress-striped active">
					<div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: 60%">
						<span class="sr-only">60% Complete (success)</span>
					</div>
				</div>
			</div>
		</a>
	</li>
	<li class="divider"></li>
	<li>
		<a href="#">
			<div>
				<p>
					<strong>Task 2</strong>
					<span class="pull-right text-muted">28% Complete</span>
				</p>
				<div class="progress progress-striped active">
					<div class="progress-bar progress-bar-info" role="progressbar" aria-valuenow="28" aria-valuemin="0" aria-valuemax="100" style="width: 28%">
						<span class="sr-only">28% Complete</span>
					</div>
				</div>
			</div>
		</a>
	</li>
	<li class="divider"></li>
	<li>
		<a href="#">
			<div>
				<p>
					<strong>Task 3</strong>
					<span class="pull-right text-muted">60% Complete</span>
				</p>
				<div class="progress progress-striped active">
					<div class="progress-bar progress-bar-warning" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: 60%">
						<span class="sr-only">60% Complete (warning)</span>
					</div>
				</div>
			</div>
		</a>
	</li>
	<li class="divider"></li>
	<li>
		<a href="#">
			<div>
				<p>
					<strong>Task 4</strong>
					<span class="pull-right text-muted">85% Complete</span>
				</p>
				<div class="progress progress-striped active">
					<div class="progress-bar progress-bar-danger" role="progressbar" aria-valuenow="85" aria-valuemin="0" aria-valuemax="100" style="width: 85%">
						<span class="sr-only">85% Complete (danger)</span>
					</div>
				</div>
			</div>
		</a>
	</li>
	<li class="divider"></li>
	<li>
</ul>   
<ul id="dropdown4" class="dropdown-content dropdown-tasks w250 taskList">
  <li>
                                <div>
                                    <strong>John Doe</strong>
                                    <span class="pull-right text-muted">
                                        <em>Today</em>
                                    </span>
                                </div>
                                <p>Lorem Ipsum has been the industry's standard dummy text ever since the 1500s...</p>
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                                <div>
                                    <strong>John Smith</strong>
                                    <span class="pull-right text-muted">
                                        <em>Yesterday</em>
                                    </span>
                                </div>
                                <p>Lorem Ipsum has been the industry's standard dummy text ever since an kwilnw...</p>
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a href="#">
                                <div>
                                    <strong>John Smith</strong>
                                    <span class="pull-right text-muted">
                                        <em>Yesterday</em>
                                    </span>
                                </div>
                                <p>Lorem Ipsum has been the industry's standard dummy text ever since the...</p>
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a class="text-center" href="#">
                                <strong>Read All Messages</strong>
                                <i class="fa fa-angle-right"></i>
                            </a>
                        </li>
</ul>  
	   <!--/. NAV TOP  -->
        <!--/. NAV TOP  -->
        <nav class="navbar-default navbar-side" role="navigation">
            <div class="sidebar-collapse">
                <ul class="nav" id="main-menu">

                    <li>
                        <a href="index.html" class="waves-effect waves-dark"><i class="fa fa-dashboard"></i> Dashboard</a>
                    </li>
                    <li>
                        <a href="ui-elements.html" class="waves-effect waves-dark"><i class="fa fa-desktop"></i> UI Elements</a>
                    </li>
					<li>
                        <a href="chart.html" class="active-menu waves-effect waves-dark"><i class="fa fa-bar-chart-o"></i> Charts</a>
                    </li>
                    <li>
                        <a href="tab-panel.html" class="waves-effect waves-dark"><i class="fa fa-qrcode"></i> Tabs & Panels</a>
                    </li>
                    
                    <li>
                        <a href="table.html" class="waves-effect waves-dark"><i class="fa fa-table"></i> Responsive Tables</a>
                    </li>
                    <li>
                        <a href="form.html" class="waves-effect waves-dark"><i class="fa fa-edit"></i> Forms </a>
                    </li>


                    <li>
                        <a href="#" class="waves-effect waves-dark"><i class="fa fa-sitemap"></i> Multi-Level Dropdown<span class="fa arrow"></span></a>
                        <ul class="nav nav-second-level">
                            <li>
                                <a href="#">Second Level Link</a>
                            </li>
                            <li>
                                <a href="#">Second Level Link</a>
                            </li>
                            <li>
                                <a href="#" class="waves-effect waves-dark">Second Level Link<span class="fa arrow"></span></a>
                                <ul class="nav nav-third-level">
                                    <li>
                                        <a href="#">Third Level Link</a>
                                    </li>
                                    <li>
                                        <a href="#">Third Level Link</a>
                                    </li>
                                    <li>
                                        <a href="#">Third Level Link</a>
                                    </li>

                                </ul>

                            </li>
                        </ul>
                    </li>
                    <li>
                        <a href="empty.html" class="waves-effect waves-dark"><i class="fa fa-fw fa-file"></i> Empty Page</a>
                    </li>
                </ul>

            </div>

        </nav>
        <!-- /. NAV SIDE  -->
        <div id="page-wrapper">
		  <div class="header"> 
                        <h1 class="page-header">
                             Charts 
                        </h1>
						<ol class="breadcrumb">
					  <li><a href="#">Home</a></li>
					  <li><a href="#">Charts</a></li>
					  <li class="active">Data</li>
					</ol> 
									
		</div>
            <div id="page-inner"> 
             
           <div class="row"> 
                  <div class="col-md-6 col-sm-12 col-xs-12">                     
                    <div class="card">
                        <div class="card-action">
                            Chart Filter
                        </div>
                        <div class="card-content">
                            <div id="chart-filter">
                            	 <form id="filter_form" name="filter_form">
                            	 <p>Gender</p>
									
								      <input type="checkbox" id="gender_man" name="gender" checked="checked" class="gender" value="22"/>
								      <label for="gender_man">Man</label>&nbsp;&nbsp;&nbsp;
								    
								    
								      <input type="checkbox" id="gender_woman" name="gender" checked="checked" class="gender" value="33"/>
								      <label for="gender_woman">Woman</label>&nbsp;&nbsp;&nbsp;
								    
								    
								      <input type="checkbox" id="gender_all" name="gender" class="gender" value="44"/>
								      <label for="gender_all">All</label>
								    <br>
								    <p>Vehicle Type</p>
								    
								      <input type="checkbox" id="sedan" name="vehicleType" value="2"/>
								      <label for="sedan">Sedan</label>&nbsp;&nbsp;
								    
								      <input type="checkbox" id="van" name="vehicleType" value="5"/>
								      <label for="van">Van</label>&nbsp;&nbsp;
								    
								      <input type="checkbox" id="truck" name="vehicleType" value="8"/>
								      <label for="truck">Truck</label>&nbsp;&nbsp;
								    
								      <input type="checkbox" id="vehicle_all" name="vehicleType" checked="checked" value="11"/>
								      <label for="vehicle_all">All</label>
								    <br>
								    <p>Search Basis</p>
								      <input type="radio" id="region_basis" name="searchBasis" value="Region" checked="checked"/>
								      <label for="region_basis">Region</label>&nbsp;&nbsp;&nbsp;
								      <input type="radio" id="period_basis" name="searchBasis" value="Period"/>
								      <label for="period_basis">Period</label>&nbsp;&nbsp;&nbsp;
								      <input type="radio" id="time_basis" name="searchBasis" value="Time"/>
								      <label for="time_basis">Time</label>
								    <br>
								    
								    <div id = region_list style="display:none">
								    	<p>Region</p>
										<div class="btn-group">
										<select data-toggle="dropdown" class="btn btn-primary dropdown-toggle" name='regionLists' id ="regionLists">
										<option class="dropdown-menu">
											<option value = 'none' selected>지역선택</option>
										  	<option value = 'seoul'>서울특별시</option>
										  	<option value = 'incheon'>인천광역시</option>
										  	<option value = 'daejeon'>대전광역시</option>
										  	<option value = 'daegu'>대구광역시</option>
										  	<option value = 'ulsan'>울산광역시</option>
										  	<option value = 'gwangju'>광주광역시</option>
										  	<option value = 'sejong'>세종특별자치시</option>
										  	<option value = 'kyeongki'>경기도</option>
										  	<option value = 'kangwon'>강원도</option>
										  	<option value = 'chungbook'>충청북도</option>
										  	<option value = 'chungnam'>충청남도</option>
										  	<option value = 'jeonbook'>전라북도</option>
										  	<option value = 'jeonnam'>전라남도</option>
										  	<option value = 'kyungbook'>경상북도</option>
										  	<option value = 'kyungnam'>경상남도</option>
										  	<option value = 'jeju'>제주특별자치도</option>
								     	</select>
								    	</div>
								    </div>
								    	
								    <div id = period_list style="display:none">
								    	<p>Period</p>
								    	<p><input type="text" id="startDate" name="startPeriod" data-toggle="datepicker" value="none">~<input type="text" id="endDate" name="endPeriod" data-toggle="datepicker" value="none"></p>
								    </div>
								    
								    <div id = time_list style="display:none">
								    	<p>Time</p>
								    	<div style=float:left;width:50%;padding:10px;>
										<select data-toggle="dropdown" class="btn btn-primary dropdown-toggle" name='timeLists1' id ="timeLists1">
										<option class="dropdown-menu">
											<option value = 'none' selected>시작 시간 선택</option>
											<option value = '0'>0 시</option>
										  	<option value = '1'>1 시</option>
										  	<option value = '2'>2 시</option>
										  	<option value = '3'>3 시</option>
										  	<option value = '4'>4 시</option>
										  	<option value = '5'>5 시</option>
										  	<option value = '6'>6 시</option>
										  	<option value = '7'>7 시</option>
										  	<option value = '8'>8 시</option>
										  	<option value = '9'>9 시</option>
										  	<option value = '10'>10 시</option>
										  	<option value = '11'>11 시</option>
										  	<option value = '12'>12 시</option>
										  	<option value = '13'>13 시</option>
										  	<option value = '14'>14 시</option>
										  	<option value = '15'>15 시</option>
										  	<option value = '16'>16 시</option>
										  	<option value = '17'>17 시</option>
										  	<option value = '18'>18 시</option>
										  	<option value = '19'>19 시</option>
										  	<option value = '20'>20 시</option>
										  	<option value = '21'>21 시</option>
										  	<option value = '22'>22 시</option>
										  	<option value = '23'>23 시</option>
								     	</select>
								     	</div>
								     	
								     	<div style=float:left;width:50%;padding:10px;>
								     	<select data-toggle="dropdown" class="btn btn-primary dropdown-toggle" name='timeLists2' id ="timeLists2">
										<option class="dropdown-menu">
											<option value = 'none' selected>종료 시간 선택</option>
										  	<option value = '1'>1 시</option>
										  	<option value = '2'>2 시</option>
										  	<option value = '3'>3 시</option>
										  	<option value = '4'>4 시</option>
										  	<option value = '5'>5 시</option>
										  	<option value = '6'>6 시</option>
										  	<option value = '7'>7 시</option>
										  	<option value = '8'>8 시</option>
										  	<option value = '9'>9 시</option>
										  	<option value = '10'>10 시</option>
										  	<option value = '11'>11 시</option>
										  	<option value = '12'>12 시</option>
										  	<option value = '13'>13 시</option>
										  	<option value = '14'>14 시</option>
										  	<option value = '15'>15 시</option>
										  	<option value = '16'>16 시</option>
										  	<option value = '17'>17 시</option>
										  	<option value = '18'>18 시</option>
										  	<option value = '19'>19 시</option>
										  	<option value = '20'>20 시</option>
										  	<option value = '21'>21 시</option>
										  	<option value = '22'>22 시</option>
										  	<option value = '23'>23 시</option>
										  	<option value = '24'>24 시</option>
								     	</select>
								    	</div>
								    </div>
								    <div>
								    <br><br><br>
								    </div>
								    <div id="submitDiv">
									 	<button id="submitButton" type="button" class="waves-effect waves-light btn" onclick="searchFormCheck();"> Search! </button>
								    </div>
                            	 </form>
                            </div>
                        </div>
                    </div>            
                </div>
                      <div class="col-md-6 col-sm-12 col-xs-12">                     
                    <div class="card">
                        <div class="card-action">
                            Chart
                        </div>
                        <div class="card-content">
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
                        </div>
                    </div>            
                </div> 
                
           </div>
                 <!-- /. ROW  -->
				 <footer><p>Shared by <i class="fa fa-love"></i><a href="https://bootstrapthemes.co">BootstrapThemes</a>
</p></footer>
				</div>
             <!-- /. PAGE INNER  -->
            </div>
         <!-- /. PAGE WRAPPER  -->
        </div>
     <!-- /. WRAPPER  -->
    <!-- JS Scripts-->
    <!-- jQuery Js -->
    <!-- 
    <script src="view/assets/js/jquery-1.10.2.js"></script>
     -->
	
 

</body>

</html>

