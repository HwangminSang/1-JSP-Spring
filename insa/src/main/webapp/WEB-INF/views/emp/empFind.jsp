<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<style>
	/* Tabs*/
section {
    padding: 30px 0;
}

section .section-title {
    text-align: center;
    color: #007b5e;
    text-transform: uppercase;
}
#tabs{
	background: #333333;
    color: #eee;
    background-color: rgba( 051,051, 051, 0.8 );
}
#tabs h6.section-title{
    color: #eee;
}
font{
	width:80px;
	
}
</style>

<script type="text/javascript">
	var emplist = []; 
	
	$(document).ready(function(){
		$("#dept").selectmenu();	//자동길이맞춤		// 디자인
		$("#detail_tab").tabs();	
		$("#findtab").tabs();		
		
		$("#find").click(function(){		
			findgrid($("#dept").val());		//전체부서, 회계팀, 인사팀, 전산팀, 보안팀   SELECT안의 OPTION 값을 가져온다.
		})
		
		showEmpListGrid();
	});
	
		  // 조회버튼 눌렀을때 //
		function findgrid(dept){  
			  console.log(dept)
			$.ajax({
				url:'${pageContext.request.contextPath}/emp/empList.do',
				data:{
					"method" : "emplist",
					"value"	: dept // select 에서 선택한 부서명
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
					emplist = data.list;
					showEmpListGrid();		
				}
			});
		}
		
		
		function showEmpListGrid(){
			var columnDefs = [				// 가져온 데이터 ag그리드로 뿌려줌
				{headerName: "사원코드", field: "empCode"},
				{headerName: "사원명", field: "empName" },
				{headerName: "부서", field: "deptName" },
				{headerName: "직급", field: "position" },
				{headerName: "성별", field: "gender",hide: true },
				{headerName: "전화번호", field: "mobileNumber",hide: true },
				{headerName: "이메일", field: "email" },
				{headerName: "거주지", field: "address",hide: true },
				{headerName: "최종학력", field: "lastSchool",hide: true },
				{headerName: "사진", field: "imgExtend",hide: true },   // EMP 테이블 가서 확인
				{headerName: "생년월일", field: "birthdate",hide: true }
			];
		
			gridOptions = {
					columnDefs: columnDefs,
					rowData: emplist,
	 				onCellClicked : function(node) {			// 클릭하면 옆에 div에 뿌려줌 즉 검색후  그 행을 클리하면 옆에 상세정보에 나옴 
	 					console.log(node.data);
	 					$("#fC").html(node.data.empCode);
	 					$("#fI").html(node.data.id);
	 					$("#fN").html(node.data.empName);		
	 					$("#fD").html(node.data.deptName);
	 					$("#fP").html(node.data.position);
	 					$("#fE").html(node.data.email);
	 					$("#fM").html(node.data.mobileNumber);
	 					$("#fB").html(node.data.birthdate);
	 					$("#fA").html(node.data.address);
	 					$("#fS").html(node.data.lastSchool);	
 					
					var empCode = node.data.empCode;
					var profile = node.data.imgExtend;
					console.log(empCode+"."+profile);
					document.getElementById('profileImg').setAttribute("src","${pageContext.request.contextPath}/profile/"+empCode+"."+profile);//사진갖고오는기능  profile-->jpg 이렇게 저장됨
				}
			}
			
			$('#findgrid').children().remove();
			
			var eGridDiv = document.querySelector('#findgrid');
			new agGrid.Grid(eGridDiv, gridOptions);
			
			gridOptions.api.sizeColumnsToFit();
		}
</script>
</head>

<body>
<br>
<br>
<br>
<br>
<br>
<table>
<tr>
	<td >	
	<section id="tabs" style="width:600px; height:450px; display: inline-block;" class="wow fadeInDown">
	<div class="container">
		<h6 class="section-title h3" style="text-align=left;">사원검색</h6>
		<hr style="background-color:white; height: 1px;">
		<select id="dept">
					<option value="전체부서">전체부서</option>
					<option value="회계팀">회계팀</option>
					<option value="인사팀">인사팀</option>
					<option value="전산팀">전산팀</option>
					<option value="보안팀">보안팀</option>
					<option value="개발팀">개발팀</option>
		</select>
				<input type="button" class="ui-button ui-widget ui-corner-all" value="조회" id="find">
				<hr>
				<div id="findgrid" style="height: 260px; width:550px" class="ag-theme-balham"></div>
		
	</div>
</section>
	</td>
	<td>
		<section id="tabs" style="width:600px; height:450px; display: inline-block;" class="wow fadeInDown">
	<div class="container">
		<h6 class="section-title h3" style="text-align=left;">상세정보</h6>
		<hr style="background-color:white; height: 1px;">
		<div style="display: inline-block; float:left">
					 <img id="profileImg" src="${pageContext.request.contextPath}/profile/profile.png" width="200px" height="250px">
				</div>
				<div style=" display: inline-block; position:absolute; margin-left: 50px; margin-top: 30px;" >
					<font>사원코드 : </font> <font id="fC"></font><br/>
					<font>ID    : </font> <font id="fI"></font><br/>
					<font>이름 : </font> <font id="fN"></font><br/><br/>
					<font>부서 : </font> <font id="fD"></font><br/>
					<font>직급 : </font> <font id="fP"></font><br/><br/> 
					<font>e-mail : </font> <font id="fE"></font><br/>
					<font>휴대전화 : </font> <font id="fM"></font><br/>
					<font>생년월일 : </font> <font id="fB"></font><br/>
					<font>거주지 : </font> <font id="fA"></font><br/>
					<font>최종학력 : </font> <font id="fS"></font><br/>
				</div>
		
	</div>
	
</section>
	</td>
</tr>
</table>
</body>
</html>