<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style type="text/css">
section {
    padding: 20px 0;
}
section .section-title {
    text-align: center;
    color: #007b5e;
    text-transform: uppercase;
}
#tabs{
	background: #333333;
    color: #eee;
    background-color: rgba( 051,051, 051, 0.5 );
}
#tabs h6.section-title{
    color: #eee;
}

form .form-group{
	margin-bottom:2px;
	flex-wrap: nowrap;
	text-align:center;
}

.form-group .btn.btn-sm{
	margin: 0 auto;
}
</style>
<script>
var empBean={};

   $(document).ready(function(){
       $("#txt_dept").click(function(){ //부서
         getCode('CO-07',"txt_dept","deptCode");
      });
      $("#txt_posi").click(function(){ //직급
         getCode('CO-04',"txt_posi","position");
      }); 
      $("#txt_gend").click(function(){ //성별
         getCode('CO-01',"txt_gend","gender");
      });
      $("#txt_lasc").click(function(){ //최종학력
         getCode('CO-02',"txt_lasc","last_shcool");
      });
      $("#btn_crenum").click(function(){ //사원번호생성
         creatEmpCode();
      });
      $("#txt_hobong").click(function(){ //호봉생성
         getCode('CO-12',"txt_hobong","hobong")
      });
      $("#txt_occupation").click(function(){ //근무형태생성
         getCode('CO-03',"txt_occupation","occupation")
      });
      $("#txt_employment").click(function(){ //고용형태생성
         getCode('CO-05',"txt_employment","employment")
      });
      
      /* 등록시 이벤트 */
      $("#regist_btn").click(function(){  registEmp(); }); 
            
      /*  tab 구현 */  // 용도를 모르겠다
    $( "#registTab" ).tabs();
     
      /* 주소 검색 버튼 */
      $(function() { $("#addr_btn").postcodifyPopUp(); });  //https://www.poesis.org/postcodify/guide/jquery_popup
      
      
      $("#IdCheck").click(function(){ idcheck(); }) 
      
   
      /* 사진찾기*/
      $("#findPhoto").button().click(function(){  //https://blog.naver.com/kokomi20/221289080799
         console.log("사진찾기 모달 띄웠다잉");
          $("#emp_img_file").click(); //사진찾기 버튼을 누르면 숨겨진 file 태그를 클릭
      });     
      // 사진 등록 form의 ajax 부분
        $("#emp_img_form").ajaxForm({ //ajax로 파일을 업로드할때   //https://yoondeng.tistory.com/41
         dataType: "json",
         success: function(responseText, statusText, xhr, $form){
            alert(responseText.errorMsg);
            location.reload();
         }
      });      
      
   });
   
   
   
   function readURL(input){ // 사진찾기해서 사진을 넣었을때 네모난칸에 사진이 나오는게 하는 함수
      
      if (input.files && input.files[0]) {
         var reader = new FileReader(); //파일 읽어오는 자바스크립트 객체생성
         reader.onload = function (e) {
            // 이미지 Tag의 SRC속성에 읽어들인 File내용을 지정 (아래 코드에서 읽어들인 dataURL형식)
            console.log(e.target);
           
            $("#profileImg").attr("src", e.target.result);  // img src 속성에 읽어온 파일의 결과값을 넣어 이미지가 보이게한다.
         }
      reader.readAsDataURL(input.files[0]); //File내용을 읽어 dataURL형식의 문자열로 저장
      }
   }
   
   function idcheck(){
	   var newID=$("#txt_id").val().trim();
	   checkid="";
	   if(newID.length==0){
		   alert("아이디를입력해주세요")
		   return;	
	   }
		
	   $.ajax({
			url:'${pageContext.request.contextPath}/emp//empList.do',
			data:{
				"method" : "emplist"
			
			},
			dataType:"json",
			success : function(data){
				if(data.errorCode < 0){
					var str = "내부 서버 오류가 발생했습니다\n";
					str += "관리자에게 문의하세요\n";
					str += "에러 위치 : " + $(this).attr("id");
					str += "에러 메시지 : " + data.errorMsg;
					alert(str);
					return;
				}
				
				var emplist = data.list;
				
			    var empIdList=emplist.map(emp => emp.id );  // id만 담은 새로운 배열은 만든다
				    	   

				
				if(empIdList.includes(newID)){  // 사용자가 입력한 id가 있다면 true를 반환해서 존재하는아이디
					alert("존재하는아이디");
				}else{
					alert("사용가능아이디");
				}
				  
				
			      
	
				
			
			}
		});
   }
   
   
   /* 사원등록 */
   function registEmp(){      
      saveInfo();      // 호풀시  empBean객체에 정보를 다담음
      
      var sendData = JSON.stringify(empBean);   // 서버로 보내기위해 문자열로 바꿈
      alert(sendData);
       $.ajax({
          type : "POST" ,  // 밑에 사진을 보내서 post방식을 취한듯
         url : "${pageContext.request.contextPath}/emp/empRegist.do",   
         data : {
            "method" : "registEmployee",
            "sendData" : sendData
         },
         dataType : "json",
         success : function(data) {
            if(data.errorCode<0){ //오류가 있다
                 var error=/unique constraint/; //정규표현식 ㄱ객체생성
                 if(error.test(data.errorMsg)){ // test 정규표현식 받아온 errorMsg에 /unique constraint/가 있으면 아래진행 
                    alert("해당 사원번호가 있습니다"); // 즉 유닉제약 조건 위배 . 아이디는 하나만있어야한다
                    return false;  //return false 때문에 폼은 전송되지 않습니다.
                 }else{
                    var str = "내부 서버 오류가 발생했습니다\n";
                  str += "관리자에게 문의하세요\n";
                  str += "에러 위치 : " + $(this).attr("id"); //? undefined 나옴 
                  str += "에러 메시지 : " + data.errorMsg;
                  alert(str);   
                
                 }                 
              }else{  // 회원등록을 다하고 돌아서 사진읋 form.submit()로 제출하는듯  < 퍼사드에서 applicationservice로 갔다가 이리저리 많ㅇ ㅣ복잡 
                 console.log("요안들가겟찌?");
                 if($("#emp_img_file").val()!=""){
                	 console.log("사진찾기 모달 띄웠다잉");
                  $("#emp_img_empCode").val(empBean.empCode);
                  
                    $("#emp_img_form").submit(); //제출
                   
               } 
                 alert("사원을 등록했습니다");
              }
              location.reload();  //새로고침          
         }
      }); 
      
   }
   


   
function saveInfo(){  // 등록 버튼 누르면 처음에 여기도 실행  빈객체 empBean 방금 입력한 모든 정보를 넣는다.
      empBean.empCode=$("#txt_code").val(); 
      empBean.empName=$("#txt_name").val();
      empBean.id=$("#txt_id").val();
      empBean.pw=$("#txt_pw").val();
      empBean.birthdate=$("#txt_birth").val();
      empBean.gender=$("#txt_gend").val();
      empBean.mobileNumber=$("#txt_mnum").val();
      empBean.address=$("#txt_addr").val();
      empBean.detailAddress=$("#txt_daddr").val();
      empBean.postNumber=$("#txt_pnum").val();
      empBean.email=$("#txt_emain").val();
      empBean.lastSchool=$("#txt_lasc").val();            
      empBean.deptName=$("#deptCode").val();
      empBean.position=$("#position").val();
      empBean.hobong=$("#txt_hobong").val();
      empBean.occupation=$("#txt_occupation").val();
      empBean.employment=$("#txt_employment").val();
      
   }
   
   /* 부서,직급 선택창 띄우기 */
   function getCode(code,inputText,inputCode){    //https://offbyone.tistory.com/312
      option="width=220; height=200px; left=300px; top=300px; titlebar=no; toolbar=no,status=no,menubar=no,resizable=no, location=no";  // 눌렀을때 생기는 새창의 구성
      window.open("${pageContext.request.contextPath}/base/codeWindow.html?code="+code+"&inputText="+inputText+"&inputCode="+inputCode,"newwins",option);
	//window.open(url, name, specs, replace); 새창 띄움.
	// name=newwins<이름>   ,    specs=option<모달창 데코레이션>

   }
   
   /* 달력 띄우기 */
   $(function(){   //$(document).ready(function(){ }); == $(function(){ });  문서를 다읽고하기 

      $("#txt_birth").datepicker({    //달력을 띄울수있게 하는 
    	  changeMonth : true,  //원하는달 선택가능
         changeYear : true,
         dateFormat : "yy/mm/dd",   // 이게 없으면 일 부터 시작해서 나옴 . 
         dayNamesMin : [ "일", "월", "화", "수", "목", "금", "토" ],  //상단 요일 나오게
         monthNamesShort : [ "1", "2", "3", "4", "5", "6", "7", "8", "9","10", "11", "12" ],  // 달 이름 설정
         yearRange: "1900:2030", // 년도 범위
      })
      
      
      
      
      $("#txt_hiredate").datepicker({  //구현필요 입사일
         changeMonth : true,
         changeYear : true,
         showOn:"button",
         buttonImage:"${pageContext.request.contextPath}/image/cal.png",
         buttonImageOnly:true,
         dateFormat : "yy/mm/dd",
         dayNamesMin : [ "일", "월", "화", "수", "목", "금", "토" ],
         monthNamesShort : [ "1", "2", "3", "4", "5", "6", "7", "8", "9",
               "10", "11", "12" ],
         yearRange: "1900:2030",
      })
   })
   
   /* 사번생성 */
      function creatEmpCode(){
         $.ajax({
            url : "${pageContext.request.contextPath}/emp/empRegist.do",  
            data:{
               "method":"findLastEmpCode"
            },
            dataType:'json',
            success:function(data){
               if(data.errorCode < 0){
                  var str = "내부 서버 오류가 발생했습니다\n";
                  str += "관리자에게 문의하세요\n";
                  str += "에러 위치 : " + $(this).attr("id");
                  str += "에러 메시지 : " + data.errorMsg;
                  alert(str);
                  return;
               }
      
               var lastEmpCode = data.lastEmpCode;
               var lastNumber = lastEmpCode.substring(1); //인데스 1부터 다 가져옴 A490080-->490080  밑에서 넘버형으로 바꿀떄 A가 있으면 NaN 뜸
               console.log(lastNumber);
               var newCode = "A" + (Number(lastNumber)+1);  // 490081은 EMP테이블에 없는애. 문자열을 넘버형으로 바꿔준다
               $("#txt_code").val(newCode);
               $("#emp_img_empCode").val(newCode);  // 252번줄 사진을 넣었을떄 파일 저장이름에 아마도 사원번호가 들어가는듯.
            }
         });
      }
   
   /* 사진찾기 버튼 눌렀을 때 실행되는 함수 */

</script>

<script> 
   
</script>
</head>
<body>
	<br>
	<br>
	<section id="tabs" style="width:560px; height:1100px;" class="wow fadeInDown">
	<div class="container">
		<h6 class="section-title h3" style="text-align=left;">인사등록</h6>
		<hr style="background-color:white; height: 3px;"> <!-- 인사등록및 줄  -->
		  <div id="registTab1">
         <!-- 사진박스 -->
         <div id="divImg" style="display:inline-block;"> 
            <img id="profileImg" src="${pageContext.request.contextPath}/profile/profile.png" width="180px" height="200px"><br>  <!--  src 인사등록 사진 그림 나오는곳 -->
            <form id="emp_img_form" action="${pageContext.request.contextPath }/base/empImg.do" enctype="multipart/form-data" method="post"> <!--  이미지 등록시 가는 url -->
               <input type="hidden" name="empCode" id="emp_img_empCode">
               <input type="file" name="empImgFile" style="display: none;" id="emp_img_file" onChange="readURL(this)">
               <button type="button" style="width:150px" class="btn btn-light btn-sm btn-outline-dark active" id="findPhoto">사진찾기</button> <br>
            </form>
            <br /> 
         </div>
   
         <!-- 사원정보 -->
         <form id="regist_form" action="<%=request.getContextPath()%>/emp/empRegist.do" method="post">    <!--  입력 끝나면 여기서 날라감 -->
            <input type="hidden" name="method" value="registEmployee"> <!-- 명령어로 날라가는부분 -->
    
            <hr style="background-color:white;">
           		<div class="form-group row">
                   <label for="full_name" class="col-md-3 col-form-label text-md-right">사원번호</label>
                   		<div class="col-md-6">
                        	<input type="text" id="txt_code" class="form-control" name="emp_code">
                        </div>
                         <input type="button" class="btn btn-light btn-sm" id='btn_crenum' value="사번생성"> <!--  사번생성 버튼부분 -->
                </div>
                
                <div class="form-group row">
                   <label for="full_name" class="col-md-3 col-form-label text-md-right">이름</label>
                     <div class="col-md-6">
                        	<input type="text" id="txt_name" class="form-control" name="emp_name">
                    </div>
                </div>
                <div class="form-group row">
                   <label for="full_name" class="col-md-3 col-form-label text-md-right">ID</label>
                   		<div class="col-md-6">
                        	<input type="text" id="txt_id" class="form-control" name="txt_id" >
                        	
                        </div>
                         <input type="button" class="btn btn-light btn-sm" id='IdCheck' value="아이디중복체크">
                </div>
                <div class="form-group row">
                   <label for="full_name" class="col-md-3 col-form-label text-md-right">PW</label>
                   		<div class="col-md-6">
                        	<input type="text" id="txt_pw" class="form-control" name="txt_pw" >
                        	
                        </div>
                </div>
                <div class="form-group row">
                   <label for="full_name" class="col-md-3 col-form-label text-md-right">부서</label>
                   		<div class="col-md-6">
                        	<input type="text" id="txt_dept" class="form-control" name="dept_name" readonly>
                        	<input type="hidden" id="deptCode">
                        </div>
                </div>
                <div class="form-group row">
                   <label for="full_name" class="col-md-3 col-form-label text-md-right">직급</label>
                   		<div class="col-md-6">
                        	<input type="text" id="txt_posi" class="form-control" name="position" readonly>
                        	<input type="hidden" id="position">
                        </div>
                </div>
                <div class="form-group row">
                   <label for="full_name" class="col-md-3 col-form-label text-md-right">성별</label>
                   		<div class="col-md-6">
                        	<input type="text" id="txt_gend" class="form-control" name="gender" readonly>
                        </div>
                </div>
                <div class="form-group row">
                   <label for="full_name" class="col-md-3 col-form-label text-md-right">생년월일</label>
                   		<div class="col-md-6">
                        	<input type="text" id="txt_birth" class="form-control" name="birthday" readonly>
                        </div>
                </div>
                
                <div class="form-group row">
                   <label for="full_name" class="col-md-3 col-form-label text-md-right">주소</label>
                   		<div class="col-md-6">
                        	<input type="text" id="txt_addr" class="form-control postcodify_address" name="address">  <!-- //postcodify_address -->
                        </div>
                         <input type="button" class="btn btn-light btn-sm" id="addr_btn" value="검색" >
                </div>
                <div class="form-group row">
                   <label for="full_name" class="col-md-3 col-form-label text-md-right">상세주소</label>
                   		<div class="col-md-6">
                        	<input type="text" id="txt_daddr" class="form-control" name="detail_address">
                        </div>
                </div>
                <div class="form-group row">
                   <label for="full_name" class="col-md-3 col-form-label text-md-right">우편번호</label>
                   		<div class="col-md-6">
                        	<input type="text" id="txt_pnum" class="form-control postcodify_postcode5" name="post_numbe"> <!-- postcodify_postcode -->
                        </div>
                </div>
                <div class="form-group row">
                   <label for="full_name" class="col-md-3 col-form-label text-md-right">전화번호</label>
                   		<div class="col-md-6">
                        	<input type="text" id="txt_mnum" class="form-control" name="mobile_number">
                        </div>
                </div>
                <div class="form-group row">
                   <label for="full_name" class="col-md-3 col-form-label text-md-right">이메일</label>
                   		<div class="col-md-6">
                        	<input type="text" id="txt_emain" class="form-control" name="email">
                        </div>
                </div>
                <div class="form-group row">
                   <label for="full_name" class="col-md-3 col-form-label text-md-right">최종학력</label>
                   		<div class="col-md-6">
                        	<input type="text" id="txt_lasc" class="form-control" name="last_school">
                        </div>
                </div>
                <div class="form-group row">
                   <label for="full_name" class="col-md-3 col-form-label text-md-right">호봉</label>
                   		<div class="col-md-6">
                        	<input type="text" id="txt_hobong" class="form-control" name="hobong" readonly>
                        </div>
                </div>
                <div class="form-group row">
                   <label for="full_name" class="col-md-3 col-form-label text-md-right">근무형태</label>
                   		<div class="col-md-6">
                        	<input type="text" id="txt_occupation" class="form-control" name="occupation" readonly>
                        </div>
                </div>
                <div class="form-group row">
                   <label for="full_name" class="col-md-3 col-form-label text-md-right">고용형태</label>
                   		<div class="col-md-6">
                        	<input type="text" id="txt_employment" class="form-control" name="employment" readonly>
                        </div>
                </div>
                <input type="hidden" name="img_extend" id="img_extend">
                  <input type="button" id="regist_btn" class="btn btn-light btn-sm" value="등록">
            <input type="reset"  class="btn btn-light btn-sm" value="취소">
         </form>
      </div>   
   </div>	
</section>
</body>
</html>