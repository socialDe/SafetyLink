<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>


<script>

$(document).ready(function (){
	logTableCreate();
});

function logTableCreate(){
	
		$.ajax({			
			url : "getFcmLog.mc",
			success:function(data){					
				for(key in data){
					var html = '';	
					html += '<tr>';
					html += '<td>'+data[key].fcmnum+'</td>';
					html += '<td>'+data[key].fcmtype+'</td>';
					html += '<td>'+data[key].carnum+'</td>';
					html += '<td>'+data[key].date+'</td>';
					html += '<td>'+data[key].time+'</td>';
					html += '</tr>';	
					
					$("#table").prepend(html);
				}
				
			},	
			error:function(){
				alert("getFcmLog Fail..");
			}
		})
	}

</script>


<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"> 
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Safety Link</title> 
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
			<button type="button" class="navbar-toggle waves-effect waves-dark" data-toggle="collapse" data-target=".sidebar-collapse">
				<span class="sr-only">Toggle navigation</span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<a class="navbar-brand waves-effect waves-dark" href="main.mc"><img src="img/logo_name.png" width="175px" height="35px" style="display: inline;"></a>

			<div id="sideNav" href="">
				<i class="material-icons dp48">toc</i>
			</div>
		</div>

		<ul class="nav navbar-top-links navbar-right">
			<li><a class="dropdown-button waves-effect waves-dark" href="#!" data-activates="dropdown2"><i class="fa fa-tasks fa-fw"></i> <i class="material-icons right">arrow_drop_down</i></a></li>
			<li><a class="dropdown-button waves-effect waves-dark" href="#!" data-activates="dropdown1"><i class="fa fa-user fa-fw"></i> <b>${admin.adminname }</b><i class="material-icons right">arrow_drop_down</i></a></li>
		</ul>
	</nav>
	<!-- Dropdown Structure -->
	<ul id="dropdown1" class="dropdown-content">
		<li><a href="logout.mc"><i class="fa fa-sign-out fa-fw"></i>Logout</a></li>
	</ul>
	<ul id="dropdown2" class="dropdown-content dropdown-tasks w250">
		<li><a href="#">
				<div>
					<p><strong>졸음 경보</strong> <span class="pull-right text-muted">${sleep } cases</span></p>
					<div class="progress progress-striped active">
						<div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="${sleep }" aria-valuemin="0" aria-valuemax="100" style="width: ${sleep }%">
							<span class="sr-only">${sleep } cases (success)</span>
						</div>
					</div>
				</div>
		</a></li>
		<li class="divider"></li>
		<li><a href="#">
				<div>
					<p><strong>영유아 경보</strong> <span class="pull-right text-muted">${baby } cases</span></p>
					<div class="progress progress-striped active">
						<div class="progress-bar progress-bar-info" role="progressbar" aria-valuenow="${baby }" aria-valuemin="0" aria-valuemax="100" style="width: ${baby }%">
							<span class="sr-only">${baby } cases</span>
						</div>
					</div>
				</div>
		</a></li>
		<li class="divider"></li>
		<li><a href="#">
				<div>
					<p><strong>적재물 낙하 경보</strong> <span class="pull-right text-muted">${freight } cases</span></p>
					<div class="progress progress-striped active">
						<div class="progress-bar progress-bar-warning" role="progressbar" aria-valuenow="${freight } " aria-valuemin="0" aria-valuemax="100" style="width: ${freight }%">
							<span class="sr-only">${freight } cases (warning)</span>
						</div>
					</div>
				</div>
		</a></li>
		<li class="divider"></li>
		<li><a href="#">
				<div>
					<p><strong>교통 사고</strong> <span class="pull-right text-muted">${accident } cases</span></p>
					<div class="progress progress-striped active">
						<div class="progress-bar progress-bar-danger" role="progressbar" aria-valuenow="${accident }" aria-valuemin="0" aria-valuemax="100" style="width: ${accident }%">
							<span class="sr-only">${accident } cases (danger)</span>
						</div>
					</div>
				</div>
		</a></li>
		<li class="divider"></li>
		<li>
	</ul>
	<!--/. NAV TOP  -->
	<nav class="navbar-default navbar-side" role="navigation">
		<div class="sidebar-collapse">
			<ul class="nav" id="main-menu">
				<li><a href="dashboard.mc" class="active-menu waves-effect waves-dark"><i class="fa fa-dashboard"></i> Dashboard</a></li>
				<li><a href="chart.mc" class="waves-effect waves-dark"><i class="fa fa-bar-chart-o"></i> Charts</a></li>
				<li><a href="table.mc" class="waves-effect waves-dark"><i class="fa fa-table"></i> Users</a></li>
			</ul>

		</div>

	</nav>
	<!-- /. NAV SIDE  -->

	<div id="page-wrapper">
		<div class="header">
			<h1 class="page-header">Dashboard</h1>
			<ol class="breadcrumb">
				<li><a href="#">Home</a></li>
				<li><a href="#">Dashboard</a></li>
				<li class="active">Data</li>
			</ol>

		</div>
		<div id="page-inner">

			<div class="dashboard-cards">
				<div class="row">
					<div class="col-xs-12 col-sm-6 col-md-3">

						<div class="card horizontal cardIcon waves-effect waves-dark">
							<div class="card-image red">
								<i class="material-icons dp48">drive_eta</i>
							</div>
							<div class="card-stacked red">
								<div class="card-content">
									<h3>${drive }</h3>
								</div>
								<div class="card-action">
									<strong>운행중</strong>
								</div>
							</div>
						</div>

					</div>
					<div class="col-xs-12 col-sm-6 col-md-3">

						<div class="card horizontal cardIcon waves-effect waves-dark">
							<div class="card-image orange">
								<i class="material-icons dp48">notifications_active</i>
							</div>
							<div class="card-stacked orange">
								<div class="card-content">
									<h3>${sleep }</h3>
								</div>
								<div class="card-action">
									<strong>졸음 경보</strong>
								</div>
							</div>
						</div>
					</div>
					<div class="col-xs-12 col-sm-6 col-md-3">

						<div class="card horizontal cardIcon waves-effect waves-dark">
							<div class="card-image blue">
								<i class="material-icons dp48">local_shipping</i>
							</div>
							<div class="card-stacked blue">
								<div class="card-content">
									<h3>${freight }</h3>
								</div>
								<div class="card-action">
									<strong>적재물 경보</strong>
								</div>
							</div>
						</div>

					</div>
					<div class="col-xs-12 col-sm-6 col-md-3">

						<div class="card horizontal cardIcon waves-effect waves-dark">
							<div class="card-image green">
								<i class="material-icons dp48">warning</i>
							</div>
							<div class="card-stacked green">
								<div class="card-content">
									<h3>${accident }</h3>
								</div>
								<div class="card-action">
									<strong>일간 교통 사고</strong>
								</div>
							</div>
						</div>

					</div>
				</div>
			</div>
			<!-- /. ROW  -->
			<div class="row">
				<div class="col-xs-12 col-sm-12 col-md-7">
					<div class="cirStats">
						<div class="row">
							<div class="col-xs-12 col-sm-6 col-md-6">
								<div class="card-panel text-center">
									<h4>일간 주행유저</h4>
									<div class="easypiechart" id="easypiechart-blue" data-percent="${driver }">
										<span class="percent">${driver }%</span>
									</div>
								</div>
							</div>
							<div class="col-xs-12 col-sm-6 col-md-6">
								<div class="card-panel text-center">
									<h4>일간 경보비율</h4>
									<div class="easypiechart" id="easypiechart-red" data-percent="${alarmRate }">
										<span class="percent">${alarmRate }%</span>
									</div>
								</div>
							</div>
							<div class="col-xs-12 col-sm-6 col-md-6">
								<div class="card-panel text-center">
									<h4>전월대비 경보 증감률</h4>
									<div class="easypiechart" id="easypiechart-teal" data-percent="${monthAlarmRate }">
										<span class="percent">${monthAlarmRate }%</span>
									</div>
								</div>
							</div>
							<div class="col-xs-12 col-sm-6 col-md-6">
								<div class="card-panel text-center">
									<h4>전일대비 경보 증감률</h4>
									<div class="easypiechart" id="easypiechart-orange" data-percent="${dayAlarmRate }">
										<span class="percent">${dayAlarmRate }%</span>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!--/.row-->
				<div class="col-xs-12 col-sm-12 col-md-5">
					<div class="row">
						<div class="col-xs-12">
							<div class="card">
								<div class="card-image donutpad">
									<div id="morris-donut-chart"></div>
								</div>
								<div class="card-action">
									<b>Donut Chart Example</b>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!--/.row-->


				<!-- /. PAGE INNER  -->
			</div>
				
				<div class="card-panel text-center">
					<h3 style="font-weight:bolder;">최신 경보</h3><br>
					<!-- LOG TABLE 생성 -->
					<table class="table log" style="text-align: center;">
						<tr>
							<th>번호</th>
							<th>경보 유형</th>
							<th>차량 번호</th>
							<th>날짜</th>
							<th>시각</th>
						</tr>
						<tbody id="table" style="text-align: center;">
							<!-- <tr><th>1</th><th>적재물 낙하 경보</th><th>12가1234</th><th>2020-12-10</th><th>21:23:15</th></tr> -->
						</tbody>
					</table>
				</div>
			
			<!-- /. PAGE WRAPPER  -->

			</div>



			<!-- /. ROW  -->
			<div class="fixed-action-btn horizontal click-to-toggle">
				<a class="btn-floating btn-large red"> <i class="material-icons">menu</i>
				</a>
				<ul>
					<li><a class="btn-floating red"><i class="material-icons">track_changes</i></a></li>
					<li><a class="btn-floating yellow darken-1"><i class="material-icons">format_quote</i></a></li>
					<li><a class="btn-floating green"><i class="material-icons">publish</i></a></li>
					<li><a class="btn-floating blue"><i class="material-icons">attach_file</i></a></li>
				</ul>
			</div>

			<footer>
				<p>
					Shared by <i class="fa fa-love"></i><a
						href="https://bootstrapthemes.co">BootstrapThemes</a>
				</p>


			</footer>
		</div>
		<!-- /. WRAPPER  -->
	</div>
</div>
<!-- /. WRAPPER  -->

	<!-- JS Scripts-->
    <!-- jQuery Js -->
    <script src="view/assets/js/jquery-1.10.2.js"></script>
	
	<!-- Bootstrap Js -->
    <script src="view/assets/js/bootstrap.min.js"></script>
	
	<script src="view/assets/materialize/js/materialize.min.js"></script>
	
    <!-- Metis Menu Js -->
    <script src="view/assets/js/jquery.metisMenu.js"></script>
    <!-- Morris Chart Js -->
    <script src="view/assets/js/morris/raphael-2.1.0.min.js"></script>
    <script src="view/assets/js/morris/morris.js??v=<%=System.currentTimeMillis() %>"></script>
	
	
	<script src="view/assets/js/easypiechart.js"></script>
	<script src="view/assets/js/easypiechart-data.js"></script>
	
	 <script src="view/assets/js/Lightweight-Chart/jquery.chart.js"></script>
	
    <!-- Custom Js -->
    <script src="view/assets/js/dashboardscripts.js"></script> 
 

</body>

</html>