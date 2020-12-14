<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
    
    <style>
    #dataTables-example{
    	text-align: center;
    	margin: auto;
    }
    </style> 
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
                <a class="navbar-brand waves-effect waves-dark" href="main.mc"><img src="img/logo_name.png" width="175px" height="35px" style="display: inline;"></a>
				
		<div id="sideNav" href=""><i class="material-icons dp48">toc</i></div>
            </div>

		<ul class="nav navbar-top-links navbar-right">
			<li><a class="dropdown-button waves-effect waves-dark" href="#!" data-activates="dropdown2"><i class="fa fa-tasks fa-fw"></i> <i class="material-icons right">arrow_drop_down</i></a></li>
			<li><a class="dropdown-button waves-effect waves-dark" href="#!" data-activates="dropdown1"><i class="fa fa-user fa-fw"></i> <b>${admin.adminname }</b><i class="material-icons right">arrow_drop_down</i></a></li>
		</ul>
        </nav>
		<!-- Dropdown Structure -->
		<ul id="dropdown1" class="dropdown-content">
			<li><a href="logout.mc"><i class="fa fa-sign-out fa-fw"></i> Logout</a></li>
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
				<li><a href="dashboard.mc" class="waves-effect waves-dark"><i class="fa fa-dashboard"></i> Dashboard</a></li>
				<li><a href="chart.mc" class="waves-effect waves-dark"><i class="fa fa-bar-chart-o"></i> Charts</a></li>
				<li><a href="table.mc" class="active-menu waves-effect waves-dark"><i class="fa fa-table"></i> Tables</a></li>
			</ul>

		</div>

	</nav>
        <!-- /. NAV SIDE  -->
        <div id="page-wrapper" >
		  <div class="header"> 
                        <h1 class="page-header">
                            Tables
                        </h1>
						<ol class="breadcrumb">
					  <li><a href="#">Home</a></li>
					  <li><a href="#">Tables</a></li>
					  <li class="active">Data</li>
					</ol> 
									
		</div>
		
            <div id="page-inner"> 
               
            <div class="row">
                <div class="col-md-12">
                    <!-- Advanced Tables -->
                    <div class="card">
                        <div class="card-action"> 회원 목록</div>
                        <div class="card-content">
                            <div class="table-responsive">
                                <table class="table table-striped table-bordered table-hover" id="dataTables-example">
                                    <thead>
                                        <tr>
                                            <th>User ID</th>
                                            <th>이름</th>
                                            <th>성별</th>
                                            <th>전화번호</th>
                                            <th>생년월일</th>
                                            <th>Send Push</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach var="user" items="${usersInfo}">
                                    	<tr>
	                                    	<td>${user.userid}</td>
	                                    	<td>${user.username}</td>
	                                    	<td>${user.usersex}</td>
	                                    	<td>${user.userphone}</td>
	                                    	<td>${user.userbirth}</td>
	                                    	<td><button>Push</button></td>
                                    	</tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                            
                        </div>
                    </div>
                    <!--End Advanced Tables -->
                </div>
            </div>
                
         
               <footer><p>Shared by <i class="fa fa-love"></i><a href="https://bootstrapthemes.co">BootstrapThemes</a>
</p></footer>
    </div>
             <!-- /. PAGE INNER  -->
            </div>
         <!-- /. PAGE WRAPPER  -->
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
    <script src="view/assets/js/morris/morris.js"></script>
	
	
	<script src="view/assets/js/easypiechart.js"></script>
	<script src="view/assets/js/easypiechart-data.js"></script>
	
	 <script src="view/assets/js/Lightweight-Chart/jquery.chart.js"></script>
	 <!-- DATA TABLE SCRIPTS -->
    <script src="view/assets/js/dataTables/jquery.dataTables.js"></script>
    <script src="view/assets/js/dataTables/dataTables.bootstrap.js"></script>
        <script>
            $(document).ready(function () {
                $('#dataTables-example').dataTable();
            });
    </script>
    <!-- Custom Js -->
    <script src="view/assets/js/custom-scripts.js"></script> 
 

</body>

</html>