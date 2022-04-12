<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>휴일목록</title>
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

#tabs .nav-tabs .nav-item.show .nav-link, .nav-tabs .nav-link.active {
    color: #f3f3f3;
    background-color: transparent;
    border-color: transparent transparent #f3f3f3;
    border-bottom: 4px solid !important;
    font-size: 20px;
    font-weight: bold;
}
#tabs .nav-tabs .nav-link {
    border: 1px solid transparent;
    border-top-left-radius: .25rem;
    border-top-right-radius: .25rem;
    color: #eee;
    font-size: 20px;
}
</style>

<script>
	// 휴일목록을 담을 배열 
	var holidayList = [];
	var emptyInfo = {};
	var addrowData;  //addrowData 추가 삭제 이벤트발생후  담는다.  // 저장 버튼 클릭시 사용
	
	$(document).ready(function() {
		$("#add_Btn").click(addGridRow); 	   // 추가버튼 
		$("#remove_Btn").click(removeGirdRow); // 삭제버튼 
		$("#save_Btn").click(saveGridRow); 	   // 저장버튼 

		
		$.ajax({
			url : "${pageContext.request.contextPath}/base/holidayList.do",
			dataType : "json",
			data : { method : "findHolidayList" },
			success : function(data) {
				holidayList = data.holidayList;
				emptyInfo = data.emptyHoilday; // 텅빈애를 넣느다 
				
				var columnDefs = [ { headerName : "일자", field : "applyDay" }, 
								   { headerName : "휴일명", field : "holidayName" }, 
								   { headerName : "비고", field : "note" }, 
								   { headerName : "상태", field : "status" },
								   { headerName : "일련번호", field : "seq"  ,hide : true}  // 새로 추가하여. 업데이트 시 사용. 즉 날짜도 바꿀수있다.
								   ];
				gridOptions = {
					columnDefs : columnDefs, // def 속성 
					rowData : holidayList,
					defaultColDef : { editable : true, width : 100 }, //수정가능
					rowSelection : 'single', //하나만선택
					enableColResize : true,
					enableSorting : true,
					enableFilter : true,
					enableRangeSelection : true,
					suppressRowClickSelection : false,
					animateRows : true,
					suppressHorizontalScroll : true,
					localeText : { noRowsToShow : '조회 결과가 없습니다.' },
					getRowStyle : function(param) {
						if (param.node.rowPinned) {
							return {
								'font-weight' : 'bold',
								background : '#dddddd'
							};
						}
						return {
							'text-align' : 'center'
						};
					},
					getRowHeight : function(param) {
						if (param.node.rowPinned) {
							return 30;
						}
						return 24;
					},
					// GRID READY 이벤트, 사이즈 자동조정 
					onGridReady : function(event) {
						event.api.sizeColumnsToFit();
					},
					// 창 크기 변경 되었을 때 이벤트 
					onGridSizeChanged : function(event) {
						event.api.sizeColumnsToFit();
					},
					onCellEditingStopped : function(event) {  // 셀클릭시 	
						console.log(event.data.status);
						console.log(event.data);
						if (event.data.status === "normal" || event.data.status === "delete") { 
							event.data.status = "update" }  // 셀 수정후 상태를 수정으로
				
						gridOptions.api.updateRowData({ update : [ event.data ] }); // 그리고 바로 적용하여 렌더링한다.
					},
				};

				var eGridDiv = document.querySelector('#holidayListGrid');
				new agGrid.Grid(eGridDiv, gridOptions);
			}
		});
	});

	// 그리드에 행 추가하는 함수
	function createNewRowData() {

		var newData=Object.assign({},emptyInfo)  // assign 메서드 이용 복사해주기. 1첫번째 인자에 두번재 인자값을 병합하여 새로운 객체를 만든다
		newData.status="insert"
		
		return newData;
	}

	function addGridRow() { //추가 눌렀을때  객체를 생성하여 그리드에 보여지고 있는 배열에 넣는다 
		var newItem = createNewRowData();
		gridOptions.api.updateRowData({ add : [ newItem ] });  // [ {}객체 필요 ]
		getRowData();
	}

	function getRowData() {  // 그런후 새로운 객체가 추가된 배열을 돌려 빈배열에 넣는다.
		addrowData = [];
		gridOptions.api.forEachNode(function(node) {   // 모든 행을 들고온다.
			addrowData.push(node.data); }); //빈배열에 넣는다 
		console.log('Row Data:');
		console.log(addrowData);
	}

	/* 그리드에 행 삭제 (주석은 행 추가하는거 참조)*/
	function removeGirdRow() {
		var selectedData = gridOptions.api.getSelectedRows();  // 선택한 행을 객체로 변환후 []에 담아서 리터한다.
		console.log(selectedData);
		var selectedData0 = selectedData[0]; //한개씩만 선택가능했다.
		if (selectedData0.status === "normal" || selectedData0.status === "update") {
			selectedData0.status = 'delete' }
		
		gridOptions.api.updateRowData({ update : selectedData }); //업데이트 및 삭제  [{}]
		
		getRowData();
	}

	/* 저장 버튼을 눌렀을 때 실행되는 함수 */
	function saveGridRow() {
		if (addrowData != null) {  // 즉  추가나 삭제를 하였을때 null이 아님 .
			var sendData = JSON.stringify(addrowData);
			alert(sendData);
		} else {
			var sendData = JSON.stringify(holidayList);  // 처음 그리드 화면에 띄울때 ajax를 통해 받아왔던 데이터를 할당한 배열객체 
			alert(sendData);
		}

	
		
		$.ajax({
			url : "${pageContext.request.contextPath}/base/holidayList.do",
			data : {
				"method" : "batchHoilyDayProcess",
				"sendData" : sendData
			},
			dataType : "json",
			success : function(sendData){
				
				console.log(sendData.errorCode);
				
				if (sendData.errorCode < 0) {
					alert("저장에 실패했습니다");
				} else {
					alert("저장되었습니다");
				}
		
				location.reload();
			}
		});
	}
	
</script>
</head>
<body>
	<section id="tabs" style="width:550px; height:800px;" class="wow fadeInDown">
	<div class="container">
		<h6 class="section-title h3" style="text-align=left;">휴일조회</h6>
		<hr style="background-color:white; height: 1px;">
		<input type="button" class="btn btn-light btn-sm" value="추가" id= "add_Btn">
		<input type="button" class="btn btn-light btn-sm" value="삭제" id="remove_Btn">
		<input type="button" class="btn btn-light btn-sm" value="저장" id="save_Btn">
		<div class="row">
			<div class="col-md-6">
				<div class="tab-content py-3 px-3 px-sm-0" id="nav-tabContent">
					<div class="tab-pane fade show active" id="nav-home" role="tabpanel" aria-labelledby="nav-home-tab">
						<div id="holidayListGrid" style="height: 600px; width: 500px" class="ag-theme-balham"></div>
					</div>
				</div>
			
			</div>
		</div>
	</div>
</section>
</body>
</html>