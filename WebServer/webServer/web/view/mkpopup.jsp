<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
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
    	//console.log($(this).data('id'));
    	//$("input[name='id']").val($(this).data('id'));
    	//console.log($(this).data('token'));
    	//$("input[name='token']").val($(this).data('token'));
		//document.getElementById("mkpopup").style.display = "block";
		// $("#token").text(document.getElementById('id').innerHTML);
		// open할 때 DB정보 넣어주자
	}
	
	function closmkpopup(){
		//document.getElementById("mkpopup").style.display = "none";
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
    </script> 
</head>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
<body>
                <!-- The Popup Modal -->
<div id="mkpopup" class="mkpopup" name="mkpopup" tabindex="-1" role="dialog" aria-labelledby="exampleModalLongTitle" aria-hidden="true">
	<div class="mkpopup-content">
		<span class="mkpopupclose" onclick="closmkpopup();" aria-hidden="true" data-dismiss="modal">&times;</span>
			<input name="id" id="id" type="hidden"> <!-- **에 사용자의 이름 or 아이디 -->
			<div class="input-field col s2"></div>
				<form method="post" name="popupcontact" onsubmit="popupvalidation();" action="fcmpopupsend.mc" enctype="multipart/form-data">
							<div class="row">
        						<div class="input-field col s2"></div>
        						<div class="input-field col s8">
         							<input placeholder="" id="token" class="materialize-textarea" name="token" type="hidden">
          								<label for="first_name"></label> <!-- db에서 토큰을 받아옴 -->
          						</div>
        						<div class="input-field col s2"></div>
      						</div>
      						<!-- <textarea name="name" id="name" style="border:none"></textarea> -->
      						<input type="text" name="name" value="" id="name" style="border:none" size="100">
							<div class="row">
        						<div class="input-field col s2"></div>
        						<div class="input-field col s8">
         							<textarea placeholder="" id="title" class="materialize-textarea" name="title"></textarea>
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
	  								<img src="img/img1.jpg" width="210px" height="160px" id="fcmImage" alt="your image"> <!-- 기본 이미지 설정 -->
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
        <script type="text/javascript">
            $(document).ready(function () {
                $('#dataTables-example').dataTable();
            });
    </script>
    <!-- Custom Js -->
    <script src="view/assets/js/custom-scripts.js"></script> 
 
 
</body>

</html>