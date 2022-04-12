<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <script src="https://unpkg.com/ag-grid-community/dist/ag-grid-community.min.noStyle.js"></script>
  <link rel="stylesheet" href="https://unpkg.com/ag-grid-community/dist/styles/ag-grid.css">
  <link rel="stylesheet" href="https://unpkg.com/ag-grid-community/dist/styles/ag-theme-balham.css">


<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>공제기준관리</title>
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
    background-color: rgba( 051,051, 051, 0.8 );
}
#tabs h6.section-title{
    color: #eee;
}
</style>
<script>
	var baseDeductionList = [];
	var emptyBean= [];
	var lastId = 0;
	var gridOptions;
	var addrowData; // 수정 삭제 추가 시 여기에 다 담아서 저장시 보냄 []

	$(document).ready(function(){
		$("#add_baseDeduction_Btn").click(addListGridRow); 	// 추가 버튼  
		$("#del_baseDeduction_Btn").click(delListGridRow); 	// 삭제 버튼 
		$("#submit_Btn").click(batchBaseDeductionProcess); 	// 저장 버튼

		
		$.ajax({
			url:"${pageContext.request.contextPath}/salary/baseDeductionManage.do",
			data:{
				"method" : "findBaseDeductionList"
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

				baseDeductionList = data.baseDeductionList;  // 공제정보들 다들고옴 
				emptyBean = data.emptyBean;  // 빈껍데를가져온다.  status를 insert로 컨트로럴에서 설정해둠  배열로 설정해뒀네? 컨트롤러에서는 객체를 던졌다. 즉 [{}] 이상태
				showBaseDeductionListGrid();
			}
		});
	});

	//변경확정 저장
	function batchBaseDeductionProcess(){
		if(addrowData != null){  // 추가 업데이트 삭제를 안했을때는 null
			var sendData = JSON.stringify(addrowData);
			alert(sendData);
		}else{  // 아무변화도 없을떄는 화면나올때 받아왔던 현재 그리드에 보이는 놈을 할당
			var sendData = JSON.stringify(baseDeductionList);
			alert(sendData);
		}
		
		$.ajax({
			url : "${pageContext.request.contextPath}/salary/baseDeductionManage.do",
			data : {
				"method" : "batchBaseDeductionProcess",
				"sendData" : sendData
			},
			dataType : "json",
			success : function(data) {
				if(data.errorCode < 0){
					alert("저장에 실패했습니다");
				} else {
					alert("저장되었습니다");
				}
				var eGridDiv = document.querySelector('#baseDeductionList_grid');
				new agGrid.Grid(eGridDiv, gridOptions);	
				location.reload();
			}
		});
	}

	/* 공제기준목록 그리드 띄우는 함수 aggrid*/
	 function showBaseDeductionListGrid(){
		 var columnDefs = [
		      {headerName: "공제항목코드", field: "deductionCode" },
		      {headerName: "공제항목명", field: "deductionName" },
		      {headerName: "공제율", field: "ratio" },
		      {headerName: "상태", field: "status" }
		 ];  
		 
  		gridOptions = {
    		columnDefs: columnDefs,
    		rowData: baseDeductionList,
    		defaultColDef: { editable: true }, // 수정가능
    		rowSelection: 'single',  
		    onCellEditingStopped: function (event) {  //셀클릭후 수정한후 이벤트발샐
		      	console.log("select Data : ");
		        console.log(event.data);
				if (event.data.status == "normal"){
					event.data.status = "update"}
				
				gridOptions.api.updateRowData({ update : [event.data] }); // 업데이트시
		  		
				console.log(event.data.status);	
		    },  
    	};
			var eGridDiv = document.querySelector('#baseDeductionList_grid');
			new agGrid.Grid(eGridDiv, gridOptions);	
			gridOptions.api.sizeColumnsToFit();
	 }

	/* 그리드에 행 추가하는 함수 */
	function createNewRowData() {
		var newData = {
				deductionCode : "DED",
				deductionName : "deductionName",
				ratio  : "",
			 	status   : "insert"
		};
	    return newData;
	}	
		
	function addListGridRow() {
	    var newItem = createNewRowData();
	    gridOptions.api.updateRowData({add: [newItem]}); // 추가시 
	    getRowData(); // 추가를 하고 호출
	}
	
	function getRowData() {
		addrowData = [];
	    gridOptions.api.forEachNode( function(node) { // 그리드 모든행의 정보를 가져온다. 방근 추가한 애도 가져옴 
	        addrowData.push(node.data); // 빈배열에 그리드 한행한행 모든 담는다.
	    });
	    console.log('Row Data : ');
	    console.log(addrowData);
	}

	/* 그리드에 행 삭제(멀티셀렉트 미적용 함수라서 1개씩만 삭제됨) switch 로 바꾸어서 처리 완료 */
	function delListGridRow() {
	    var selectedData = gridOptions.api.getSelectedRows(); // 선택한 행을 배열에 담아서 객체로 던져줌 
	    console.log(selectedData);
		var selectedData0 = selectedData[0]; //선택한놈
		console.log(selectedData0.status); 
	  
	    switch(selectedData0.status){
	    	case "normal" : selectedData0.status = 'delete';  break;
	    	case "delete" : selectedData0.status = 'normal';  break;
	    }
	    
	    gridOptions.api.updateRowData({ update : [selectedData0] }); // [ 객체 ]
	    
		console.log('delete Data:');
	    getRowData();
	}
	
</script>
</head>
<body>
<section id="tabs" style="width:550px; height:800px;" class="wow fadeInDown">
	<div class="container">
		<h6 class="section-title h3" style="text-align=left;">공제기준관리</h6>
		<hr style="background-color:white; height: 1px;">
		<input type="button" class="btn btn-light btn-sm" value="추가" id= "add_baseDeduction_Btn">
		<input type="button" class="btn btn-light btn-sm" value="삭제/삭제취소" id="del_baseDeduction_Btn">
		<input type="button" class="btn btn-light btn-sm" value="저장" id="submit_Btn">
		<div class="row">
			<div class="col-md-6">
				<div class="tab-content py-3 px-3 px-sm-0" id="nav-tabContent">
					<div class="tab-pane fade show active" id="nav-home" role="tabpanel" aria-labelledby="nav-home-tab">
						<div id="baseDeductionList_grid" style="height: 600px; width: 500px" class="ag-theme-balham"></div>
					</div>
				</div>
			
			</div>
		</div>
	</div>
</section>
</body>
</html>