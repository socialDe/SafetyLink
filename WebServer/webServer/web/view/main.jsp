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
</head>

<body>

	<!-- 로그인 실패 대화창 -->
	<c:choose>
		<c:when test="${loginfail != null }">
			<script type="text/javascript">
				alert("아이디 또는 비밀번호가 틀립니다.");
			</script>
		</c:when>
	</c:choose>

	<c:choose>
		<c:when test="${admin == null }">
			<!-- 로그인 정보가 없으면 로그인 페이지로 이동 -->
			<script>
				location.href = 'login.mc';
			</script>
		</c:when>
		<c:when test="${admin != null }">
			<!-- 로그인시 메인 페이지 설정 -->
			<c:choose>
				<c:when test="${centerpage == null }">
					<!-- 로그인 했으나 센터 페이지 정보가 없을 경우 로그인 페이지로 이동 -->
					<script>location.href = 'login.mc';</script>
				</c:when>
				<c:otherwise>
					<!-- 센터 페이지 설정 -->
					<jsp:include page="${centerpage}.jsp"></jsp:include>
				</c:otherwise>
			</c:choose>
		</c:when>
	</c:choose>
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
	
    <!-- Custom Js -->
    <script src="view/assets/js/custom-scripts.js"></script> 
 

</body>

</html>
