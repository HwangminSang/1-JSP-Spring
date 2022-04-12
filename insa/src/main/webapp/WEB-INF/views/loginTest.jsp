<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<% 
	Cookie[] array=request.getCookies();
	String id="";
	if(array!=null){
		for(Cookie coo: array){
			if(coo.getName().equals("id")){
				id=coo.getValue();
			}
		}
	}
%> 
<script>
	$(document).ready(function(){  
		$("#loginB").click(login);  // 94 번줄 누르면 시작
		$(document).keydown(  // 이페이지 전체에 이벤트를 걸어둠
				function(event){ 
					if(event.keyCode == 13){ login() }   // 초기 페이지에서 엔터키를 누르면 바로 로그인 함수가 호출되서 41번줄이 실행됨 .   13번은 enter  
				});
	});

	function login(){
	  console.log($("#empId").val())
	  console.log($("#empPw").val())
		$.ajax({
			url : "${pageContext.request.contextPath}/login.do",  //   /insa/login.do  mvc패턴과 같음
			data:{
				"id":$("#empId").val(),  //아이디
				"pw":$("#empPw").val(),  //비밀번호
				"box":$("#customCheck").is(":checked") // 쿠키를 서버에 남길건지 안 남길건지 체크 
			},
			dataType : "json",
			success : function(data){
				
				if(data.me=="enter"){  // 컨트롤러에서 로그인 성공시 map.put("me","enter") 를 해뒀음
					location.href="${pageContext.request.contextPath}/home.html"
				}else{		
					Swal.fire({   //https://wooncloud.tistory.com/12   alert창 예쁘게 나오게 하기위해 decorator.jsp 13번줄 cdn 방식으로 링크를 걸어둠 확인!!
						  icon: 'error', 
						  title: '로그인실패',
						  text: data.errorMsg,
						  confirmButtonText: '확인'
						})
				}
			}
		});	  
	}

	//	new WOW().init(); 없어도 됨 어차피 css 적용안함. 
</script>


</head>
<body>
	 <div class="container">
                <!--Grid row-->
                <div class="row mt-5">
                  <!--Grid column-->
                  <div class="col-md-6 mb-5 mt-md-0 mt-5 white-text text-center text-md-left">
                    <h1 class="h1-responsive font-weight-bold wow fadeInLeft" data-wow-delay="0.3s">Human Resource Project </h1>
                    <hr class="hr-light wow fadeInLeft" data-wow-delay="0.3s">
                    <h6 class="mb-3 wow fadeInLeft" data-wow-delay="0.3s">
                    	Let's get started now!
                    </h6>
                    <a class="btn btn-outline-white wow fadeInLeft" data-wow-delay="0.3s">Learn more</a>
                  </div>
                  <div class="col-md-6 col-xl-5 mb-4">
                    <div class="card wow fadeInRight" data-wow-delay="0.3s">
                      <div class="card-body">
                        <div class="text-center">
                          <h3 class="white-text">
                            <i class="fas fa-user white-text"></i>&nbsp Login</h3>
                          <hr class="hr-light">
                        </div>
                        <div class="md-form">
                          
                          <input type="text" id="empId" class="white-text form-control" value="<%=id%>">
                          <label for="empId" class="active">enter Your email...</label>
                        </div>
                        <div class="md-form">
                          
                          <input type="password" id="empPw" class="white-text form-control">  
                          <label for="empPw">enter Your password...</label>  <!--  위에 input과 label for로 이어짐 -->
                        </div>
                        <div class="custom-control custom-checkbox white-text text-center text-md-left">
	                        <input type="checkbox" class="white-text custom-control-input " id="customCheck" name="box">
	                        <label class="custom-control-label" for="customCheck">Remember Me</label>
                      	</div>
                        <div class="text-center mt-4">
                          <button class="btn btn-outline-white wow fadeInLeft" id="loginB">Login</button>  <!-- 로그인버트부분 -->
                          <hr class="hr-light mb-3 mt-4">
                          <div class="inline-ul text-center">
                            <a class="p-2 m-3 tw-ic" href="https://twitter.com/?lang=ko">
                              <i class="fab fa-twitter white-text">트위터</i>
                            </a>
                            <a class="p-2 m-3 li-ic" href="https://www.google.co.kr/">
                              <i class="fab fa-google white-text">구글</i>
                            </a>
                            <a class="p-2 m-3 ins-ic" herf="https://www.instagram.com/?hl=ko">
                              <i class="fab fa-instagram white-text">인스타</i>
                            </a>
                          </div>
                        </div>
                      </div>
                    </div>                    
                  </div>
                </div>
              </div>
</body>
</html>