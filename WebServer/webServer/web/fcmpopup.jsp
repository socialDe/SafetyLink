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
    
    <!-- Image Preview Start // jquery 이 버전이 있어야 사진 미리보기 가능 -->
    <script type="text/javascript" src="http://code.jquery.com/jquery-2.1.0.min.js"></script>
    <script type="text/javascript">
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
	// Image Preview End
    </script>
    
</head>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
<body>
			 	<div class="card">
    <div class="page-inner">
  	  	<div class="row">
		<!-- FCM Page Start -->
			<div class="col-lg-12">
                    <div class="card-action text-center" style="font-size:20px;'">
						FCM 전송
                    </div>
                    <div class="card-content">
    					<form class="col s12" action="fcmsendpopup.mc" method="post" role="form" enctype="multipart/form-data">
      						<div class="row">
        						<div class="input-field col s2"></div>
        						<div class="input-field col s8">
         							<input placeholder="" id="title" type="text" class="validate" name="title">
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
					</div>
 	</div>
    		<!-- FCM Page End -->
             <!-- /. PAGE INNER  -->
 	</div>
         <!-- /. PAGE WRAPPER  -->
         </div>
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