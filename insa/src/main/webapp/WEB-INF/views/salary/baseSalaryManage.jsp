<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <script src="https://unpkg.com/ag-grid-community/dist/ag-grid-community.min.noStyle.js"></script>
  <link rel="stylesheet" href="https://unpkg.com/ag-grid-community/dist/styles/ag-grid.css">
  <link rel="stylesheet" href="https://unpkg.com/ag-grid-community/dist/styles/ag-theme-balham.css">
  <link rel="stylesheet" href="https://unpkg.com/ag-grid-community/dist/styles/ag-theme-fresh.css">
  <link rel="stylesheet" href="https://unpkg.com/ag-grid-community/dist/styles/ag-theme-material.css">

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>급여기준관리</title>
<style type="text/css">
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
    background-color: rgba( 051,051, 051, 0.7 );
}
#tabs h6.section-title{
    color: #eee;
}
</style>
<script>
	var updatedSalaryBean = []; // 전역변수 선언 
	var gridOptions;
	
	$(document).ready(function(){	
	    getBaseSalaryListFunc();  //첫화면에 나오는이유/
		$("#submit_Btn").click(modifyBaseSalaryList);
	});
	

	function getBaseSalaryListFunc(){
			$.ajax({
				url:"${pageContext.request.contextPath}/salary/baseSalaryManage.do",
				data:{
					"method" : "findBaseSalaryList"
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
					 
					updatedSalaryBean = $.extend(true, [], data.baseSalaryList); // 변경된 내용이 들어갈 공간에 딥카피 // https://blueshw.github.io/2016/01/20/shallow-copy-deep-copy/
					console.log(updatedSalaryBean);  //어느 한쪽을 수정한다고 해서 다른 한쪽이 영향 받는 일은 없게되겠죠.
					
					showBaseSalaryListGrid();
				}
			});

		}

	
	/*변경확정 버튼 눌렀을때 */
	function modifyBaseSalaryList(){
		var sendData = JSON.stringify(updatedSalaryBean); // 문자열로 변환 
		
		$('#baseSalaryList_grid').children().remove();
		
		$.ajax({
			url : "${pageContext.request.contextPath}/salary/baseSalaryManage.do",
			data : {
				"method" : "modifyBaseSalaryList",
				"sendData" : sendData
			},
			dataType : "json",
			success : function(data) {
				if(data.errorCode < 0){
					alert("저장에 실패했습니다");
				} else {
					alert("저장되었습니다");
				}
				
				console.log(sendData);
				//location.reload();
			}
		});
	}
 
	 /* 급여기준목록 그리드 띄우는 함수 aggrid*/
	 function showBaseSalaryListGrid(){
		 var columnDefs = [
		      {headerName: "직급코드", field: "positionCode", hide: true  },
		      {headerName: "직급", field: "position", cellClass: 'rag-amber' },
		      {headerName: "기본급", field: "baseSalary" },
		      {headerName: "호봉인상율", field: "hobongRatio"},
		      {headerName: "상태", field: "status"}
		 ];
		 
		 gridOptions = {
		    	columnDefs: columnDefs,
		    	rowData: updatedSalaryBean,
		    	defaultColDef: { editable: true },
	    		onCellEditingStopped: function (event) {
					if (event.data.status == "normal"){  // event안의 속성 data가 {}  객체 그래서 . status로 값변경
						event.data.status = "update"}
						
					gridOptions.api.updateRowData({ update : [event.data] }); // 변경된정보 반영
	    		    }
		    	};
			var eGridDiv = document.querySelector('#baseSalaryList_grid');
			new agGrid.Grid(eGridDiv, gridOptions);	
			gridOptions.api.sizeColumnsToFit();
	}
	 
</script>	


</head>
<body>
	<section id="tabs" style="width:550px; height:800px;" class="wow fadeInDown">
	<div class="container">
		<h6 class="section-title h3" style="text-align=left;">급여기준관리</h6>
		<hr style="background-color:white; height: 1px;">
		<input type="button" class="btn btn-light" value="변경확정" id= "submit_Btn">
		<div class="row">
			<div class="col-md-6">
				<div class="tab-content py-3 px-3 px-sm-0" id="nav-tabContent">
					<div class="tab-pane fade show active" id="nav-home" role="tabpanel" aria-labelledby="nav-home-tab">
						<div id="baseSalaryList_grid" style="height: 600px; width: 500px" class="ag-theme-balham"></div>
					</div>
				</div>
			
			</div>
		</div>
	</div>
</section>
</body>
</html>