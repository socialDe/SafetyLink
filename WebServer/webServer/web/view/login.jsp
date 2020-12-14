	<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
    <!-- Login Styles-->
    <link href="view/assets/css/login-style.css" rel="stylesheet" />
    <!-- Google Fonts-->
    <link href='http://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css' />
    <link rel="stylesheet" href="view/assets/js/Lightweight-Chart/cssCharts.css"> 
</head>
<body>
	<div id="wrapper">
		<div class="navbar-header">
				<a class="navbar-brand waves-effect waves-dark" href="main.mc"><img src="img/logo_name.png" width="175px" height="35px" style="display: inline;"></a>
		</div>
		<!-- /. NAV SIDE  -->
			<div class="header">
				<div class="page-header">
					<h1 style="margin-top: 30px;">Safety Link</h1>
					<h4>Management System</h4>
				</div>
			</div>

			<div id="page-inner">
				<div id="logincenter">
					<div class="card">
						<div class="card-action" style="font-size: 2em; margin-left: 20px;">Sign in</div>
						<div class="card-content">
							<!-- 로그인 폼 시작 -->
							<div class="col s12" style="margin: 0 40px;">
							<form id="loginform" action="loginimpl.mc" method="post">
								<div class="row">
									<div class="input-field col s12">
										<input id="id" name="id" type="text" class="validate" value="aid01">
										<label for="Administrator ID">Administrator ID</label>
									</div>
								</div>
								<div class="row">
									<div class="input-field col s12">
										<input id="password" name="password" type="password" class="validate" value="pwd01">
										<label for="Administrator ID">Administrator Password</label>
									</div>
								</div>
								<input type="submit" class="waves-effect waves-light btn" value="Sign in" style="float: right;">
							</form>
							</div>
							<!-- 로그인 폼 종료 -->

							<div class="clearBoth">
								<br />
							</div>
						</div>
					</div>
					<footer>
						<p>
							Shared by <i class="fa fa-love"></i><a
								href="https://bootstrapthemes.co">BootstrapThemes</a>
						</p>
					</footer>
				</div>
				<!-- /. PAGE INNER  -->
				<!-- /. PAGE WRAPPER  -->
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
			<!-- Custom Js -->
			<script src="view/assets/js/custom-scripts.js"></script>
</body>

</html>