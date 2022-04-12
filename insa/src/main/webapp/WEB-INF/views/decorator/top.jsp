<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
 <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
 <meta name="description" content="">
 <meta name="author" content="">

<script
	src="https://unpkg.com/ag-grid-community/dist/ag-grid-community.min.noStyle.js"></script>
<link rel="stylesheet"
	href="https://unpkg.com/ag-grid-community/dist/styles/ag-grid.css">
<link rel="stylesheet"
	href="https://unpkg.com/ag-grid-community/dist/styles/ag-theme-balham.css">
<link rel="stylesheet"
	href="https://unpkg.com/ag-grid-community/dist/styles/ag-theme-fresh.css">
<link rel="stylesheet"
	href="https://unpkg.com/ag-grid-community/dist/styles/ag-theme-material.css">
<link rel="stylesheet"
	href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i" >

<!-- Material Design Bootstrap -->
<link href="https://cdnjs.cloudflare.com/ajax/libs/mdbootstrap/4.19.1/css/mdb.min.css" rel="stylesheet">
<!-- Bootstrap tooltips -->
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.4/umd/popper.min.js"></script>
<!-- Bootstrap core JavaScript -->
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.5.0/js/bootstrap.min.js"></script>
<!-- MDB core JavaScript -->
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/mdbootstrap/4.19.1/js/mdb.min.js"></script>


<script type="text/javascript" src="${pageContext.request.contextPath}/js/wow.js"></script>   <!-- 제일 밑에 init()메서드  똑같이 적용-->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/animate.css"  />   <!-- 제일 밑에 init()메서드  로 시작하여 이 링크를 적용-->

<style>
#divTag1 {
	margin-left: 50px;
}

#divTag3 {
	margin-left: 800px;
}

font {
	font-family: '견고딕체'
}

html,
body,
header,
.view {
  height: 100% !important;
}

@media (max-width: 740px) {
  html,
  body,
  header,
  .view {
    height: 1000px !important;
  }
}
@media (min-width: 800px) and (max-width: 850px) {
  html,
  body,
  header,
  .view {
    height: 650px !important;
  }
}

.top-nav-collapse {
  background-color: #000000 !important;
}

.navbar:not(.top-nav-collapse) {
  background: transparent !important;
}

@media (max-width: 991px) {
  .navbar:not(.top-nav-collapse) {
    background: #3f51b5 !important;
  }
}

.rgba-gradient {
  background: -webkit-linear-gradient(45deg, rgba(72, 15, 144, 0.2), rgba(0, 0, 0, 0.9) 100%) !important;
  background: -webkit-gradient(linear, 45deg, from(rgba(72, 15, 144, 0.2), rgba(0, 0, 0, 0.9) 100%)) !important;
  background: linear-gradient(to 45deg, rgba(72, 15, 144, 0.2), rgba(0, 0, 0, 0.9) 100%) !important;
}

.card {
  background-color: rgba(126, 123, 215, 0.1) !important;
}

.md-form label {
  color: #ffffff !important;
}

h6 {
  line-height: 1.7;
}
</style>


<!-- Navbar -->
            <nav class="navbar navbar-expand-lg navbar-dark fixed-top scrolling-navbar">
	            <div class="container">
		            <a class="navbar-brand" href="#">
		            	<strong>SeoulIt</strong>  
		            </a>
	                <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent-7" aria-controls="navbarSupportedContent-7" aria-expanded="false" aria-label="Toggle navigation">
	                	<span class="navbar-toggler-icon"></span>
	                </button>
	                <div class="collapse navbar-collapse" id="navbarSupportedContent-7">
		                <ul class="navbar-nav mr-auto">
		                	<li class="nav-item active">
			                    <a class="nav-link" href="${pageContext.request.contextPath}/home.html">Home
			                      <span class="sr-only">(current)</span>
			                    </a>
		                    </li>
		                    <li class="nav-item">
		                    	<a class="nav-link" href="#">Link</a>
		                    </li>
		                </ul>
		                <div class="collapse navbar-collapse" id="navbarSupportedContent-4">
    						<ul class="navbar-nav ml-auto">
      							<li class="nav-item active">
        							<a class="nav-link dropdown-toggle" href="#" id="messagesDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
	                					<i class="fas fa-envelope fa-fw"></i>
	                					<!-- Counter - Messages -->
	                					<span class="badge badge-danger badge-counter">5+</span>
              						</a>
              						<!-- Dropdown - Alerts -->
              						<!-- Dropdown - Messages -->
					              	<div class="dropdown-list dropdown-menu dropdown-menu-right shadow animated--grow-in" aria-labelledby="messagesDropdown">
						                <h6 class="dropdown-header">
						                  Message Center
						                </h6>
						                <a class="dropdown-item d-flex align-items-center" href="#">
						                  <div class="dropdown-list-image mr-3">
						                    
						                    <div class="status-indicator bg-success"></div>
						                  </div>
						                  <div class="font-weight-bold">
						                    <span class="font-weight-bold">Hi there! I am wondering if you can help me with a problem I've been having.</span>
						                    <div class="small text-gray-500">Emily Fowler · 58m</div>
						                  </div>
						                </a>
						                <a class="dropdown-item d-flex align-items-center" href="#">
						                  <div class="dropdown-list-image mr-3">
						                  	<div class="status-indicator"></div>
						                  </div>
						                  <div>
						                    <span class="font-weight-bold">I have the photos that you ordered last month, how would you like them sent to you?</span>
						                    <div class="small text-gray-500">Jae Chun · 1d</div>
						                  </div>
						                </a>
						                <a class="dropdown-item d-flex align-items-center" href="#">
						                  <div class="dropdown-list-image mr-3">
						                    
						                    <div class="status-indicator bg-warning"></div>
						                  </div>
						                  <div>
						                    <span class="font-weight-bold">Last month's report looks great, I am very happy with the progress so far, keep up the good work!</span>
						                    <div class="small text-gray-500">Morgan Alvarez · 2d</div>
						                  </div>
						                </a>
						                <a class="dropdown-item d-flex align-items-center" href="#">
						                  <div class="dropdown-list-image mr-3">
						                    
						                    <div class="status-indicator bg-success"></div>
						                  </div>
						                  <div>
						                    <span class="font-weight-bold">Am I a good boy? The reason I ask is because someone told me that people say this to all dogs, even if they aren't good...</span>
						                    <div class="small text-gray-500">Chicken the Dog · 2w</div>
						                  </div>
						                </a>
						                <a class="dropdown-item text-center small text-gray-500" href="#">Read More Messages</a>
					              	</div>
      							</li>
      							<li class="nav-item">
        							<a class="nav-link dropdown-toggle" href="#" id="showModal" data-target="#noticeModal" data-toggle="modal" aria-haspopup="true" aria-expanded="false">
	                					<i class="fas fa-bell fa-fw"></i>
	                					<!-- Counter - Alerts -->
	                					<span class="badge badge-danger badge-counter">3+</span>
              						</a>
              						<div aria-labelledby="alertsDropdown">
              						</div>
      							</li>
      							<li class="nav-item dropdown">
        							<a class="nav-link dropdown-toggle" id="navbarDropdownMenuLink-4" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                    	<i class="fas fa-user"></i> Profile </a>
	        						<div class="dropdown-menu dropdown-menu-right dropdown-info" aria-labelledby="navbarDropdownMenuLink-4">
	          							<a class="dropdown-item" href="${pageContext.request.contextPath}/emp/empFind.html">
                							<i class="fas fa-user fa-sm fa-fw mr-2 text-gray-400"></i>
                   							My Account
                						</a>
                						<div class="dropdown-divider"></div>
                						<a class="dropdown-item" href="#">  <!-- 링크 주기 -->
                							<i class="fas fa-cogs fa-sm fa-fw mr-2 text-gray-400"></i>
                    						Settings
                						</a>
                						<div class="dropdown-divider"></div>
	          							<c:if test="${empty sessionScope.id}">  <!-- !-- sessionScopre.id가 없으면 true를 반환 --> 
                							<a class="dropdown-item" href="${pageContext.request.contextPath}/loginTest.html" >
                    						<i class="fas fa-sign-out-alt fa-sm fa-fw mr-2 text-gray-400"></i>
               									Log-in
                							</a>	
                						</c:if>
                						<c:if test="${not empty sessionScope.id}">  <!-- 로그인을 안했다면 -->
                							<a class="dropdown-item" href="${pageContext.request.contextPath}/logout.do">
                							<i class="fas fa-sign-out-alt fa-sm fa-fw mr-2 text-gray-400"></i>
               									Log-out
                							</a>
                						</c:if>
                						<div class="dropdown-divider"></div>
	        						</div>
      							</li>
    						</ul>
  						</div>
  						
		                <form class="form-inline my-1">
			                <div class="md-form form-sm my-0">
			                	<input class="form-control form-control-sm mr-sm-2 mb-0" type="text" placeholder="Search" aria-label="Search"> <!-- 프로필 바로옆부분 -->
			                </div>
                      		<button class="btn btn-link btn-sm my-0" type="button">
                        		<i class="fas fa-search white-text fa-sm"></i>
                      		</button>
                      	</form>
	                </div>
	            </div>
            </nav>
<script>
new WOW().init();  //아래 한줄을 통해 이벤트를 등록한다.  // https://gahyun-web-diary.tistory.com/178  위쪽링크들을 적용한다는뜻
</script>

