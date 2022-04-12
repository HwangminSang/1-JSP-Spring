<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>일근태관리</title>
<style type="text/css">
section {
    padding: 35px 0;
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
#container{
	margin:auto 0;
	padding-left:5px;
	padding-right:5px;
}
</style>

<script>
	var dayAttdMgtList = []; // 일근태 정보를 담을 배열 

	$(document).ready(function() {
		$(".small_Btn").button();	
		getDatePicker($("#searchDay")); //텍스트 박스에 DatePicker 설정하여 달력을 볼수있음 .
		
		$("#search_dayAttdMgtList_Btn").click(function(){ findDayAttdMgtList("searchDay"); }); // 조회일자 버튼 
		$("#finalize_dayAttdMgt_Btn").click(finalizeDayAttdMgt); // 마감처리 함수 
		$("#cancel_dayAttdMgt_Btn").click(cancelDayAttdMgt); // 마감취소 
		
		findDayAttdMgtList("today"); // 실행 시 바로 오늘자 그리드 내역을 띄우기 위함 
		//showDayAttdMgtListGrid();
	});

	
	
	/* 일근태관리 목록 조회버튼 함수 */
	function findDayAttdMgtList(check) { //check =today 
		var searchDay = $("#searchDay").val(); // 조회일자 텍스트박스 
		
		if (check == "today") { //해당 함수를 부를때에 today라는 글자가 변수로 넘어오면 오늘 날짜를 searchDay변수에 담아 ajax실행 초기 페이지 로드시 한 번만 실행됨
			var today = new Date();
			var rrrr = today.getFullYear();
			var mm = today.getMonth() + 1;
			var dd = today.getDate();
			searchDay = rrrr + "-" + mm + "-" + dd;
		}
		
		console.log("조회일자 : "+searchDay);

		$.ajax({
				url : "${pageContext.request.contextPath}/attendance/dayAttendanceManage.do",
				data : {
						"method" : "findDayAttdMgtList",   // 실행될 메서드 (MultiAction Controller)
						"applyDay" : searchDay 		   // 조회일자 
					
				},
				dataType : "json",
				success : function(data) {
					if (data.errorCode < 0) {
						var str = "내부 서버 오류가 발생했습니다\n";
							str += "관리자에게 문의하세요\n";
							str += "에러 위치 : " + $(this).attr("id");
							str += "에러 메시지 : " + data.errorMsg;
							
							Swal.fire({
								  icon: 'error',
								  title: 'Oops...',
								  text: str
								})
							return;
						}

						dayAttdMgtList = data.dayAttdMgtList; //받아온  서치 날짜 근태리스트를 넣어준다.
						console.log(dayAttdMgtList);
						
						for ( var index in dayAttdMgtList) { //인덱스를 뽑는다. <시간 뱐경이 필요한애들은 따로 한번 처리>
							dayAttdMgtList[index].attendTime = getRealTime(dayAttdMgtList[index].attendTime); 			    // 출근시간   왜냐하면 db에는 오후 8시면 2000 이렇게 입력되어있기때문에 고치는과정필요
							dayAttdMgtList[index].quitTime = getRealTime(dayAttdMgtList[index].quitTime); 				    // 퇴근시간 
							dayAttdMgtList[index].lateHour = getRealKrTime(dayAttdMgtList[index].lateHour); 			    // 지각여부   // 없으면 빈공백으로 나옴
							dayAttdMgtList[index].leaveHour = getRealKrTime(dayAttdMgtList[index].leaveHour); 			    // 총외출시간
							dayAttdMgtList[index].workHour = getRealKrTime(dayAttdMgtList[index].workHour); 			    // 근무시간 
							dayAttdMgtList[index].overWorkHour = getRealKrTime(dayAttdMgtList[index].overWorkHour); 	    // 연장근무 
							dayAttdMgtList[index].nightWorkHour = getRealKrTime(dayAttdMgtList[index].nightWorkHour); 	    // 심야근무 
							dayAttdMgtList[index].publicLeaveHour = getRealKrTime(dayAttdMgtList[index].publicLeaveHour);   //공외출 
							dayAttdMgtList[index].privateLeaveHour = getRealKrTime(dayAttdMgtList[index].privateLeaveHour); // 사외출
						}
						
						showDayAttdMgtListGrid();
					}
				});
	}
	
	/* 일근태관리 목록 그리드 띄우는 함수 */
	function showDayAttdMgtListGrid() {
		var columnDefs = [ 
			{headerName : "사원코드",field : "empCode"}, 
			{headerName : "사원명",field : "empName"}, 
			{headerName : "적용일",field : "applyDays"}, 
			{headerName : "일근태구분코드",field : "dayAttdCode"}, 
			{headerName : "일근태구분명",field : "dayAttdName"}, 
			{headerName : "출근시각",field : "attendTime"}, 
			{headerName : "퇴근시각",field : "quitTime"}, 
			{headerName : "지각여부",field : "lateWhether"},   //안나옴
			{headerName : "총외출시간",field : "leaveHour"}, 
			{headerName : "공외출시간",field : "publicLeaveHour"}, 
			{headerName : "사외출시간",field : "privateLeaveHour"}, 
			{headerName : "정상근무시간",field : "workHour"}, 
			{headerName : "연장근무시간",field : "overWorkHour"}, 
			{headerName : "심야근무시간",field : "nightWorkHour"}, 
			{headerName : "마감여부",field : "finalizeStatus"}, 
			{headerName : "상태",field : "status",hide : true} 
		];
		
		gridOptions = {
			columnDefs : columnDefs,
			rowData : dayAttdMgtList,  //받아온 데이터 배열
			defaultColDef : { editable : false,  width : 100 }, // 수정 안 된다 요놈아 ! 
			rowSelection : 'single', 
			enableColResize : true,
			enableSorting : true,
			enableFilter : true,
			enableRangeSelection : true,
			suppressRowClickSelection : false,
			animateRows : true,
			suppressHorizontalScroll : true,
			localeText : { noRowsToShow : '조회 결과가 없습니다.' },
			getRowStyle : function(param){
				console.log(param);
				
				if (param.node.rowPinned) {
					return { 'font-weight' : 'bold', background : '#333333' };
				}
				return { 'text-align' : 'center' };
			},
			getRowHeight : function(param) {
				if (param.node.rowPinned) { return 30; }
				return 24;
			},
			// GRID READY 이벤트, 사이즈 자동조정 
			onGridReady : function(event) {
				event.api.sizeColumnsToFit();
			},
			// 창 크기 변경 되었을 때 이벤트 
			onGridSizeChanged : function(event) {
				event.api.sizeColumnsToFit();
			}
		};
		$('#dayAttdMgtList_grid').children().remove(); // 기존에 있던 그리드를 날린다 (이유는 잘 모르겠다)
		var eGridDiv = document.querySelector('#dayAttdMgtList_grid');
		new agGrid.Grid(eGridDiv, gridOptions); 		// 재생성 
	}

	/* 마감처리 함수 */
	function finalizeDayAttdMgt() {
		var dayAttdMonthData = 0; 						// 월근태 데이터를 가져오기 위해 날짜를 담을 변수
		var monthAttdFinalizeStatus = 0;    			// 해당 월의 월근태 마감여부
		
		gridOptions.api.forEachNode(function(rowNode, index) { // 그리드에 있는 각 행의 정보를 가져온다 arraylike 객체로
					var dateData = rowNode.data.applyDays;// 일근태 날짜를 가지고 옴 		  2021-10-12
		              
					dayAttdMonthData = dateData.substring(0, dateData.lastIndexOf("-")); //YYYY-MM  2021-10
					
					if (dateData.endsWith('0',6)){ 		// 월에 0이 들어가 있다면   ex]2018-09						
						
						 const newData=dateData.slice(0,5)+dateData.slice(6,7);  // 2018-9로 바꿔준다.
						 dayAttdMonthData=newData;
						  
					}	  // 0을 제거한다  ex]2018-8 //YYYY-M     
				});
		

		$.ajax({ 	// 월근태 목록을 가져온다
					url : "${pageContext.request.contextPath}/attendance/monthAttendanceManage.do",
					data : {
						"method" : "findMonthAttdMgtList",
						"applyYearMonth" : dayAttdMonthData // 일근태 관리의 적용일을 잘라낸 데이터   2021-08 -- > 2021-8 로 바꾸고 왔다 . 177번줄
					},
					
					dataType : "json",
					async : false,  //<동기>
					// 동기처리를 하지 않으면 전역 변수 monthAttdFinalizeStatus에 값이 할당되기 전에 취소 작업이 일어남
					// TRUE가 default 값으로 소스처리를 기다리지 않고 동시다발적으로 처리하는 방식 <비동기>
					success : function(data) {
						if (data.errorCode < 0) {
							var str = "내부 서버 오류가 발생했습니다\n";
							str += "관리자에게 문의하세요\n";
							str += "에러 위치 : " + $(this).attr("id");
							str += "에러 메시지 : " + data.errorMsg;
							
							Swal.fire({
								  icon: 'error',
								  title: 'Oops...',
								  text: str
								})
							return;
						}

						$.each(data.monthAttdMgtList,function(i, elt){ // i 는 index   elt는 [] 안의 각각의 월근태정보
							console.log(i);
							console.log(elt);
							
							monthAttdFinalizeStatus = elt.finalizeStatus;
							return false;
						})

					}
				});

		if(monthAttdFinalizeStatus == "N") { // 월근태 마감 여부가 되지 않았다면 일근태 마감을 진행한다 
										 
			for ( var index in dayAttdMgtList) { // 조회시 받아와서 변수에 담아뒀다 . < 전체 마감 > 이기떄문에 하나씩 돌면서 설정 
				dayAttdMgtList[index].finalizeStatus = "Y"; 
				dayAttdMgtList[index].status = "update";  // DAO 들어가기전 APPLIACTIONSERVICE에서 확인
			} 
		    //일근태 마감여부를 Y 로 바꿈 

			var sendData = JSON.stringify(dayAttdMgtList);

			$.ajax({ url : "${pageContext.request.contextPath}/attendance/dayAttendanceManage.do",
					 data : {
						"method" : "modifyDayAttdList",
						"sendData" : sendData
					 },	
					dataType : "json",
					
					success : function(data) {
						if (data.errorCode < 0) {
							Swal.fire({
								icon: 'error',
								title: 'Oops...',
								text: '마감 취소를 실패하였습니다',
								confirmButtonText: '확인'
							}).then((result) => {  // 확인을 누른후 실행되게하는 동기적 THEN 
								if(result.isConfirmed) { 
									location.reload(); } })
							
						}else{
							
							Swal.fire({
								icon: 'success',
								title: 'Complete !',
								text: '마감이 성공적으로 처리 되었습니다',
								confirmButtonText: '확인'
							}).then((result) => {
								if(result.isConfirmed) { location.reload(); } })
						}
							
						}
					});
			
		} 
		else if (monthAttdFinalizeStatus == "Y") {  // 월근태가 마감되었다면 
			Swal.fire({
				  icon: 'warning',
				  title: 'Wait ...',
				  text: '해당 월의 근태관리가 마감되었습니다 \n 데이터의 마감을 하시고자 하는 경우에는\n해당 월의 근태관리 마감을 취소하시고 시도해주세요',
				})
		}
	}

	
	/* 마감취소 함수 */
	function cancelDayAttdMgt() {
		var dayAttdMonthData = 0; 		 // 일근태관리의 월 데이터 
		var monthAttdFinalizeStatus = 0; // 해당 일근태 관리의 월에 따른 월근태관리의 마감 여부  <마감여부를 확인하고 일근태를 다시 업데이트 하는작업떄문에!!> 
		
		gridOptions.api.forEachNode(function(rowNode, index) {  //각각의 줄에 접근함
			console.log(rowNode);
					var dateData = rowNode.data.applyDays;  //rowNode.data 하면 각줄의 정보에 접근  //2021-10-12
					
					dayAttdMonthData = dateData.substring(0, dateData.lastIndexOf("-")); 	// YYYY-MM  ,2021-10  <먼저 월근태 확인을 위해 > 
					
					if (dateData.endsWith('0',6)){ 		// 월에 0이 들어가 있다면   ex]2018-09						
					
						 const newData=dateData.slice(0,5)+dateData.slice(6,7);  // 2018-9로 바꿔준다.
						 dayAttdMonthData=newData;
						  
					}	  // 0을 제거한다  ex]2018-8 //YYYY-M     
					                      
		})

		$.ajax({ 	// 월근태 목록을 가져온다
					url : "${pageContext.request.contextPath}/attendance/monthAttendanceManage.do",
					data : {
						"method" : "findMonthAttdMgtList",
						"applyYearMonth" : dayAttdMonthData
					},
					dataType : "json",
					async : false,  //동기처리를 하지 않으면 전역 변수 monthAttdFinalizeStatus에 값이 할당되기 전에 취소 작업이 일어남
					success : function(data) {
						if (data.errorCode < 0) {
							var str = "내부 서버 오류가 발생했습니다\n";
							str += "관리자에게 문의하세요\n";
							str += "에러 위치 : " + $(this).attr("id");
							str += "에러 메시지 : " + data.errorMsg;
							Swal.fire({
								  icon: 'error',
								  title: 'Oops...',
								  text: str
								})
							return;
						}

						$.each(data.monthAttdMgtList, function(i, elt) { // i 는  index ,  elt 월근태에서 받아온 값들 
							monthAttdFinalizeStatus = elt.finalizeStatus;
							return false; // ? function 내에 들어있는 each 문은 또다른 function 입니다.	each 에서 return false 는 break 과 같은 역할을 합니
										  //return true는 continue
						
						})

					}
				});

		if (monthAttdFinalizeStatus == "N") { //월근태 관리의 마감 상태가 N과 같다면 일근태 마감 취소 작업을 한다 
			for ( var index in dayAttdMgtList) {
				dayAttdMgtList[index].finalizeStatus = "N";
				dayAttdMgtList[index].status = "update";  // 나중에 application 퍼사드에서 상태 검사를 한다.
			}

			var sendData = JSON.stringify(dayAttdMgtList);

			$.ajax({
					url : "${pageContext.request.contextPath}/attendance/dayAttendanceManage.do",
					data : {
						"method" : "modifyDayAttdList",
						"sendData" : sendData
					},
					dataType : "json",
					success : function(data) {
						if (data.errorCode < 0) {
							Swal.fire({
								icon: 'error',
								title: 'Oops...',
								text: '마감 취소를 실패하였습니다',
								confirmButtonText: '확인'
							}).then((result) => { if(result.isConfirmed) {location.reload(); } })  //완료 콜백  //적당한 오류 코드 와 오류 메세지를 response 해준다면, 어떨까?그렇다면, $.ajax 는 이를 성공으로 간주하게 되어, 성공(done) 에서 논리적인 오류인지를 체크해야 한다.
													//확인을 누르면 then 함수실행  true가 리턴 그리고 새로고침

							
							} else {
								Swal.fire({
									  icon: 'success',
									  title: 'Complete !',
									  text: '마감이 성공적으로 처리 되었습니다',
									  confirmButtonText: '확인' 
								}).then((result) => { if(result.isConfirmed) { location.reload(); } }) //
							}
						}
					});
			
		} else if (monthAttdFinalizeStatus == "Y") {
			Swal.fire({
				  icon: 'warning',
				  title: 'Wait ...',
				  text: '해당 월의 근태관리가 마감되었습니다 \n 데이터의 마감을 하시고자 하는 경우에는\n해당 월의 근태관리 마감을 취소하시고 시도해주세요',
				})
		}
	}
	
	
	

	/* 0000단위인 시간을 00:00(실제시간)으로 변환하는 함수 */
	function getRealTime(time) {
		var hour = Math.floor(time / 100);
		if (hour >= 24){
			var hTime=hour-24;
			hour = Math.floor(hTime); //데이터 베이스에 넘길때는 25시로 받고나서 grid에 표시하는건 1시로
		}
		
		var min = time - (Math.floor(time / 100) * 100);
		
		if (min.toString().length == 1)
			min = "0" + min; //분이 1자리라면 앞에0을 붙임
		if (min == 0)
				min = "00";
			
		return hour + ":" + min;
	}

	/* 0000단위인 시간을 (00시간00분) 형식으로 변환하는 함수 */
	function getRealKrTime(time) {
		var hour = Math.floor(time / 100);
		if (hour >= 24){
			var hTime=hour-24;
			hour = Math.floor(hTime); //데이터 베이스에 넘길때는 25시로 받고나서 grid에 표시하는건 1시로
		}
		var min = time - (Math.floor(time / 100) * 100);
		if (min == 0)
			min = "00";
		return hour + "시간" + min + "분";
	}

	/* 날짜 조회창 함수 */
	function getDatePicker($Obj, maxDate) {
		$Obj.datepicker({
			changeMonth : true,
			changeYear : true,
			dateFormat : "yy-mm-dd",
			dayNamesMin : [ "일", "월", "화", "수", "목", "금", "토" ],
			monthNamesShort : [ "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" ],
			yearRange : "1980:2021",
			maxDate : (maxDate == null ? "+100Y" : maxDate)
		});
	}

	
</script>
</head>
<body>
<br>
<br>
<br>
<br>
<br>
	<section id="tabs" style="width:1480px; height:510px;" class="wow fadeInDown">
		<h6 class="section-title h3">일근태관리</h6>
		<div class="container">
		<hr style="background-color:white; height: 1px;">
		</div>
		조회일자 <input type=text id="searchDay" readonly>
		<input type= "button" value="조회하기" class="btn btn-light btn-sm" id="search_dayAttdMgtList_Btn">
		<br /> 
		<input type= "button" value="전체마감하기" class="btn btn-light btn-sm" id="finalize_dayAttdMgt_Btn">
		<input type= "button" value="전체마감취소" class="btn btn-light btn-sm" id="cancel_dayAttdMgt_Btn">
		<div id="dayAttdMgtList_grid" style="height: 300px; width: 1440px" class="ag-theme-balham"></div>
</section>
</body>
</html>