<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Target Material Design Bootstrap Admin Template</title>
	
	<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
	<link rel="stylesheet" href="assets/materialize/css/materialize.min.css" media="screen,projection" />
    <!-- Bootstrap Styles-->
    <link href="assets/css/bootstrap.css" rel="stylesheet" />
    <!-- FontAwesome Styles-->
    <link href="assets/css/font-awesome.css" rel="stylesheet" />
    <!-- Morris Chart Styles-->
    <link href="assets/js/morris/morris-0.4.3.min.css" rel="stylesheet" />
    <!-- Custom Styles-->
    <link href="assets/css/custom-styles.css" rel="stylesheet" />
    <!-- Google Fonts-->
    <link href='http://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css' />
    <link rel="stylesheet" href="assets/js/Lightweight-Chart/cssCharts.css">
    
		<style>
			/*Popup CSS*/
			.mkpopup {
				display: none; /* Hidden by default */
				position: fixed; /* Stay in place */
				z-index: 99999999; /* Sit on top */
				padding-top: 100px; /* Location of the box */
				left: 0;
				top: 0;
				width: 100%; /* Full width */
				height: 100%; /* Full height */
				overflow: auto; /* Enable scroll if needed */
				background-color: rgb(0,0,0); /* Fallback color */
				background-color: rgba(0,0,0,0.4); /* Black w/ opacity */
			}
			/* Modal Content */
			.mkpopup-content {
				background-color: #fefefe;
				margin: auto;
				padding: 20px;
				border: 1px solid #888;
				width: 100%;
				max-width:700px;
			}
			/* The Close Button */
			.mkpopupclose {
				color: #aaaaaa;
				float: right;
				font-size: 28px;
				font-weight: bold;
			}
			.mkpopupclose:hover, .mkpopupclose:focus {
				color: #000;
				text-decoration: none;
				cursor: pointer;
			}
			input[type="text"], textarea{width:100%; border:1px solid #cccccc; margin-bottom:10px; padding:5px;}
		</style>
		<script>
			function openmkpopup(){
				document.getElementById("mkpopup").style.display = "block";
				$("#token").text(document.getElementById('id').innerHTML);
				// open할 때 DB정보 넣어주자
			}
			function closmkpopup(){
				document.getElementById("mkpopup").style.display = "none";
				// close할 때 DB정보를 flush?? 같이 불러온 정보 비우는 기능 사용
			}
		</script>    
    
    <script type="text/javascript" src="http://code.jquery.com/jquery-2.1.0.min.js"></script>
    <script type="text/javascript">
   
    /* Image Preview Start // 이 버전의 jquery가 있어야 사진 미리보기 가능 */
    $(function() {
        $("#mf").on('change', function(){
            readURL(this);
        });
    });
    function readURL(input) {
        if (input.files && input.files[0]) {
        var reader = new FileReader();
        reader.onload = function (e) {
                $('#fcmImage').attr('src', e.target.result);
            }
          reader.readAsDataURL(input.files[0]);
        }
    }
	/* Image Preview End */ 
	
	/* popup button Start */
	/* $(document).ready(function(){
	$("#fcmtomobile").click(function(){
		var url = 'fcmpopup.mc';
		var name = 'target';
		var option = 'width=300px, height=300px, left=400px, top=400px, menubar=0, location=0, status=0';
		window.open(url, name, option)
	});
	}) */
	/* popup button End */
    </script>
</head>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
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
				
		<div id="sideNav" href=""><i class="material-icons dp48">toc</i></div>
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
        <!--/. NAV SIDE  -->
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
                        <a href="chart.html" class="waves-effect waves-dark"><i class="fa fa-bar-chart-o"></i> Charts</a>
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
                        <a href="fcm.mc" class="active-menu waves-effect waves-dark"><i class="fa fa-edit"></i> Fcm </a>
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
        <!-- /. NAV SIDE 끝 -->
        
<!-- The Popup Modal -->
<div id="mkpopup" class="mkpopup">
<!-- Modal content DB 연동 문제로 추후 작업 예정 -->
	<div class="mkpopup-content">
		<span class="mkpopupclose" onclick="closmkpopup();">&times;</span>
			<h3>Send FCM to **</h3> <!-- **에 사용자의 이름 or 아이디 -->
			<div class="input-field col s2"></div>
				<form method="post" name="popupcontact" onsubmit="popupvalidation();" action="fcmpopupsend.mc" enctype="multipart/form-data">
							<div class="row">
        						<div class="input-field col s2"></div>
        						<div class="input-field col s8">
         							<textarea placeholder="" id="token" type="text" class="materialize-textarea" name="token"></textarea>
          								<label for="first_name">token</label> <!-- db에서 토큰을 받아옴 -->
          						</div>
        						<div class="input-field col s2"></div>
      						</div>
							<div class="row">
        						<div class="input-field col s2"></div>
        						<div class="input-field col s8">
         							<textarea placeholder="" id="title" type="text" class="materialize-textarea" name="title"></textarea>
          								<label for="first_name">Title</label>
          						</div>
        						<div class="input-field col s2"></div>
      						</div>
      						<div class="row">
								<div class="input-field col s2"></div>
								<div class="input-field col s8">
									<textarea id="contents" class="materialize-textarea" name="contents"></textarea>
										<label for="textarea1">Contents</label>
								</div>
								<div class="input-field col s2"></div>
	  						</div>
	  						<div class="row">
	  							<div class="input-field col s2"></div>
	  							<div class="input-field col s8">
	  								<input type="file" name="mf" id="mf"> <!-- 파일 첨부하는 버튼, 보안상의 이유로 value를 넣을 수 없다.. -->
	  							</div>
	  							<div class="input-field col s2"></div>
	  						</div>
	  						<div class="row">
	  							<div class="input-field col s2"></div>
	  							<div class="input-field col s8">
	  								<img src="view/img/logo.png" width="210px" height="160px" id="fcmImage" alt="your image"> <!-- 기본 이미지 설정 -->
	  							</div>
	  							<div class="input-field col s2"></div>
	  						</div>
	                  		<div class="row">
	  						<div class="input-field col s7"></div>
	                 		<div class="input-field col s1">
                 					<input type="submit" value="Send Push" class="waves-effect waves-light btn-primary btn-large">
	                 		</div>
	                 		<div class="input-field col s3"></div>
	                  		</div>
				</form>
		<div class="clearfix"></div>
	</div>
	<div class="clearfix"></div>
</div>
<!-- Modal Popup End -->
        
        <!-- 페이지 본문 -->
   <div id="page-wrapper" >
	<div class="header"> 
     <h1 class="page-header">
      Form Inputs Page
     </h1>
		<ol class="breadcrumb">
			<li><a href="#">Home</a></li>
			<li><a href="#">Forms</a></li>
			<li class="active">Data</li>
		</ol> 							
	</div>
		
    <div id="page-inner">
		<div class="row">
		
		
		                    <!-- Advanced Tables -->
		         <div class="col-lg-12">
                    <div class="card">
                        <div class="card-action">
                             Advanced Tables
                        </div>
                        <div class="card-content">
                            <div class="table-responsive">
                                <table class="table table-striped table-bordered table-hover" id="dataTables-example">
                                    <thead>
                                        <tr>
                                            <th>아이디</th>
                                            <th>이름</th>
                                            <th>휴대폰</th>
                                            <th>차종</th>
                                            <th>토큰</th>
                                            <th>버튼</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr class="odd gradeX">
                                            <td id="id">uid1</td>
                                            <td>고양이</td>
                                            <td>010-1111-1111</td>
                                            <td class="center">자동차1</td>
                                            <td class="center">token1</td>
                                            <td>
												<input type="button" value="Send FCM" onclick="openmkpopup();">
                                            </td>
                                        </tr>
                                        <tr class="even gradeC">
                                            <td id="id">uid2</td>
                                            <td>강아지</td>
                                            <td>010-2222-2222</td>
                                            <td class="center">자동차2</td>
                                            <td class="center">token2</td>
                                            <td>
                                            	<input type="button" value="Send FCM" onclick="openmkpopup();">
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                            
                        </div>
                    </div>
                </div>
                    <!--End Advanced Tables -->
		
		<!-- FCM Page Start -->
			<div class="col-lg-12">
			 	<div class="card">
                    <div class="card-action text-center" style="font-size:40px;'">
						FCM 전송
                    </div>
                    <div class="card-content">
    					<form class="col s12" action="fcmsendall.mc" method="post" role="form" enctype="multipart/form-data">
      						<div class="row">
        						<div class="input-field col s2"></div>
        						<div class="input-field col s8">
         							<textarea placeholder="" id="title" type="text" class="materialize-textarea" name="title"></textarea>
          								<label for="first_name">Title</label>
          						</div>
        						<div class="input-field col s2"></div>
      						</div>
      						<div class="row">
								<div class="input-field col s2"></div>
								<div class="input-field col s8">
									<textarea id="contents" class="materialize-textarea" name="contents"></textarea>
										<label for="textarea1">Contents</label>
								</div>
								<div class="input-field col s2"></div>
	  						</div>
	  						<div class="row">
	  							<div class="input-field col s2"></div>
	  							<div class="input-field col s8">
	  								<input type="file" name="mf" id="mf"> <!-- 파일 첨부하는 버튼, 보안상의 이유로 value를 넣을 수 없다.. -->
	  							</div>
	  							<div class="input-field col s2"></div>
	  						</div>
	  						<div class="row">
	  							<div class="input-field col s2"></div>
	  							<div class="input-field col s8">
	  								<img src="view/img/logo.png" width="210px" height="160px" id="fcmImage" alt="your image"> <!-- 기본 이미지 설정 -->
	  							</div>
	  							<div class="input-field col s2"></div>
	  						</div>
	                  		<div class="input-field col s8"></div>
	                 		<div class="input-field col s1">
                 					<input type="submit" value="Send Push" class="waves-effect waves-light btn-primary btn-large">
	                 		</div>
	                 		<div class="input-field col s3"></div>
	                 		
    					</form>
    <!-- FCM Page End -->
    
						<div class="clearBoth">
						</div>
  					</div>
    			</div>
 			</div>	
		</div>		
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
    <script src="assets/js/jquery-1.10.2.js"></script>
	<!-- Bootstrap Js -->
    <script src="assets/js/bootstrap.min.js"></script>
	<script src="assets/materialize/js/materialize.min.js"></script>
    <!-- Metis Menu Js -->
    <script src="assets/js/jquery.metisMenu.js"></script>
    <!-- Morris Chart Js -->
    <script src="assets/js/morris/raphael-2.1.0.min.js"></script>
    <script src="assets/js/morris/morris.js"></script>
	<script src="assets/js/easypiechart.js"></script>
	<script src="assets/js/easypiechart-data.js"></script>
	 <script src="assets/js/Lightweight-Chart/jquery.chart.js"></script>
    <!-- Custom Js -->
    <script src="assets/js/custom-scripts.js"></script>
</body>
</html>