<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator"%> <!-- 데코레이터 적용 커트텀 태그 설정 -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/animate.css"  />
<script src="https://unpkg.com/@ag-grid-enterprise/all-modules@24.1.0/dist/ag-grid-enterprise.min.js"></script>

<link href="${pageContext.request.contextPath}/vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
<%-- <script src="${pageContext.request.contextPath}/vendor/jquery-easing/jquery.easing.min.js"></script> --%>

<!-- <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.css"/> -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.23.0/moment.min.js"></script> 
<script src="https://unpkg.com/ag-grid/dist/ag-grid.min.js"></script> <!-- ag그리드 사용부분 -->
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@10"></script>  <!-- loginTest.jsp 의 로그인 실패시 alert창이 예쁘게 나오게 하기위해 링크건부분 -->

<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<jsp:include page="header.jsp" /> <!--  젤 상단 부분 -->
<title>Mynoming</title>
<decorator:head />  

<c:if test="${requestScope.errorMsg != null}">
	<script>
		alert("${requestScope.errorMsg }");
	</script>
</c:if>
<style>

.rgba-gradient {
  background: -webkit-linear-gradient(45deg, rgba(0, 0, 0, 0.7), rgba(72, 15, 144, 0.4) 100%) !important;
  background: -webkit-gradient(linear, 45deg, from(rgba(0, 0, 0, 0.7), rgba(72, 15, 144, 0.4) 100%)) !important;
  background: linear-gradient(to 45deg, rgba(0, 0, 0, 0.7), rgba(72, 15, 144, 0.4) 100%) !important;
}

</style>
</head>
<body>
<div class="mask rgba-gradient align-items-center">
	<table class="topTable" style="width: 100%; height: 1000px;">
		<tr style="height: 50px">
			<td rowspan="3">
				<jsp:include page="menu.jsp" />  <!--  로그인하지않았을떄   .hid()로 슘김상태  -->
			</td>			
			<td colspan="2">
				<jsp:include page="top.jsp" />  <!--  제일윗줄 상단정보 seoulit부터 시작 -->
			</td>
		</tr>  <!-- 제일 윗줄 끝!!! -->
		
		<tr style="height: 300px"> <!-- 2번째줄 -->
			<td valign="top" style="width: 100%; height: 800px;" id="decorator">  <!-- td valign="top" 높이지정 -->
				<center>
				<decorator:body /> <!-- 제일 처음 화면 : loginTest.jsp    web.xml의 <welcome-file>에 설정됨-->   <!--  여기만 이제 계속 화면이 바뀜.   -->
				</center>
			</td>
		</tr>
		<tr> <!-- 막줄  -->
			<td colspan="2">
				<hr>
					<center>
						<jsp:include page="bottom.jsp" />
					</center>
			</td>
		</tr>
	</table>

</div>
</body>
</html>
