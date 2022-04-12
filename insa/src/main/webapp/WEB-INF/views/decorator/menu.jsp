<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link href="${pageContext.request.contextPath}/vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
<link href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i" rel="stylesheet">

<!-- Custom styles for this template-->
<link href="${pageContext.request.contextPath}/css/sb-admin-2.min.css" rel="stylesheet">
<script src="${pageContext.request.contextPath}/vendor/chart.js/Chart.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@10"></script>

<c:set var="dept" value="" />

<c:if test="${ not empty sessionScope.id}">			<%--값이 null이 아닐경우 --%>
	<c:set var="dept" value="${sessionScope.dept}" />
</c:if>

<c:set var="position" value="" />
<c:if test="${ not empty sessionScope.id}">
	<c:set var="position" value="${sessionScope.position}" />
</c:if>

<c:set var="name" value="Guest" />
<c:if test="${ not empty sessionScope.id}">
	<c:set var="name" value="${sessionScope.id}" />
</c:if>

<script>
var MenuList = [];

	$(document).ready(
		function() {
			var id = "${sessionScope.id}";
			var position = "${sessionScope.position}";
			var dept = "${sessionScope.dept}";
			var authority = "${sessionScope.authority}"  //AD_03
					
			console.log("권한 레벨 : "+authority);
			//authoritySeparator(authority);
			$('nav li').hover(function() {  //nav 태그 안의 모든 li태그
				$('ul', this).stop().toggle();  // ul 태그와  this== nav 태그 안의 모든 li태그
			}); //어떤 역할을 하는지 정확히 모르겠음.
					
			if(id == ""){  //즉 로그인하지않았을떄는 
				$("#accordionSidebar").hide();  // 왼쪽 메뉴바 나오는곳을 숨김으로 표시함
			}

			findMenuList();  
	});
				     
			     
	function findMenuList(){
		$.ajax({
			url : "${pageContext.request.contextPath}/base/menuList.do",   
			data : { "method" : "findMenuList" }, 
			dataType : "json",
			success : function(data) {
					
			var headMenu="";
			var subMenu="";
			var num=0;
			
			if (data.errorCode < 0) {
				var str = "내부 서버 오류가 발생했습니다\n";
					str += "관리자에게 문의하세요\n";
					str += "에러 위치 : " + $(this).attr("id");
					str += "에러 메시지 : " + data.errorMsg;
					alert(str);
					return;  //return은 함수 실행을 종료하고 함수를 호출한 곳으로 실행 흐름을 옮긴다.

			}
					
			MenuList = data.MenuList;
			console.log(MenuList);
					
			$.each(MenuList , function(index,menuOption){
						
			if(menuOption.is_collapce == "Y"){ // 즉 super_menu_code 가 null  level 1의 최상위    
				num++;
							
				headMenu = '<li class="nav-item">'
	        		+'<a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapseUtilities'+num+'" aria-expanded="true" aria-controls="collapseTwo">'  // 인사관리를 눌렀을때 토글되는 이유 
	          		+'<i class="fas fa-fw fa-wrench"></i>'
	          		+'<span> '+menuOption.menu_name+'</span>'
	        		+'</a>'
	        		+'<div id="collapseUtilities'+num+'" class="collapse" aria-labelledby="headingTwo" data-parent="#accordionSidebar">'
	          		+'<div class="bg-white py-2 collapse-inner rounded" id="subTarget'+num+'">' // 아래 subMenu태그가 여기에 추가된다.
	            	+'<h6 class="collapse-header">Custom Utilities:</h6>'
	          		+'</div>'
	        		+'</div>'
	      			+'</li>';
	      			console.log(num);
	      			
					$("#target").append(headMenu);   
				}else{
					subMenu = '<a class="collapse-item" href="${pageContext.request.contextPath}'+menuOption.menu_url+'">'+menuOption.menu_name+'</a>';  //메뉴를 추가하고싶으면 테이블에 넣고 가만히 있으면 여기서처리
					$("#subTarget"+num+"").append(subMenu); 
					
				}
	
 			}); 

			}
		});
	}
	

	
</script>
<section class="wow bounceInLeft">  <!-- 그냥 div태그로 생각하면됨 -->
	<!--  메뉴  Body -->
    <ul class="navbar-nav bg-gradient-dark sidebar sidebar-dark accordion" id="accordionSidebar">  <!-- 메뉴가 나오는곳 -->
    
    <!--  메뉴 Head -->
      <a class="sidebar-brand d-flex align-items-center justify-content-center" href="${pageContext.request.contextPath}/home.html">
        <div class="sidebar-brand-icon rotate-n-15">
          <i class="fas fa-chalkboard-teacher"></i>
        </div>
        <div class="sidebar-brand-text mx-3">insa<sup>Inc.</sup></div>
      </a>

     
      <hr class="sidebar-divider my-0">

      
      <li class="nav-item">
        <a class="nav-link" href="${pageContext.request.contextPath}/home.html">  <!-- 컨트롤러 null 나옴 -->
          <i class="fas fa-fw fa-tachometer-alt"></i>
          <span>Dashboard</span></a>
      </li>

      
      <hr class="sidebar-divider">
      <div class="sidebar-heading">
        Interface
      </div>
      <!--  DB 연동하는 데이터 들아가는 Content 부분 -->
      <div id="target"></div>
      
    
      <hr class="sidebar-divider">

     
      <div class="sidebar-heading"></div>

  
     
      <li class="nav-item">
        <a class="nav-link" href="${pageContext.request.contextPath}/busanInformation.html">
        
          <i class="fas fa-fw fa-chart-area"></i>
          <span>부산맛집소개</span></a>
      </li>
      
       <li class="nav-item">
        <a class="nav-link" href="${pageContext.request.contextPath}/affair/empAuthority.html">
        
          <i class="fas fa-fw fa-chart-area"></i>
          <span>사원권한관리</span></a>
      </li>
      
      <li class="nav-item">
		<a class="nav-link" href="${pageContext.request.contextPath}/base/listBoard1.html">
   		<i class="fas fa-fw fa-chart-area"></i>
          <span>게시판(page)</span></a>
      </li>
    
      <hr class="sidebar-divider d-none d-md-block">

     
      <div class="text-center d-none d-md-inline">
        <button class="rounded-circle border-0" id="sidebarToggle"></button>
      </div>

    </ul>
   
    </section>
</body>
<hr>
</html>

