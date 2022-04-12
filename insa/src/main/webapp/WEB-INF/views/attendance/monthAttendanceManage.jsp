<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html style="width: 1579px; ">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>월근태관리</title>
<style>
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
select{
	font-size:small;
}
</style>


<script>
var monthAttdMgtList = [];
var DayFinalizeCheck=[];
	$(document).ready(function(){

	    showMonthAttdMgtListGrid();
	    
		$("input:text").button();
		$(".small_Btn").button();

		$("#search_monthAttdMgtList_Btn").click(findMonthAttdMgtList); 	// 조회버튼 
		$("#finalize_monthAttdMgt_Btn").click(finalizeMonthAttdMgt); 	// 마감버튼 
		$("#cancel_monthAttdMgt_Btn").click(cancelMonthAttdMgt); 		// 마감취소버튼 
		
		
		var year = (new Date()).getFullYear(); // 2021 얻어옴 
		
		for(var i=0; i<=12; i++){
			if(i===0){
			$("#searchYearMonth").append($("<option />").val("").text("선택해주세요"));
			}else{
			$("#searchYearMonth").append($("<option />").val(year+"-"+i).text(year+"년 "+i+"월"));	 // 부모.append(자식 )  <option value=2021-1>2021년1월</option>
			}
			}
		
		
		$("#searchYearMonth").change(daycheck);  // 해당월의 일근태를 가져와 마감되지 않은 일들을 검사한다.
		});
	
	
	function daycheck(){
		
		
		var newDate=new Date($(this).val());
		var rrrr = newDate.getFullYear()
		var mm = newDate.getMonth() + 1;
		
		var applyDay=rrrr+"-"+mm;

		$.ajax({
				url : "${pageContext.request.contextPath}/attendance/dayAttendanceManage.do",
				data : {
						"method" : "findDayAttdMgtCheckList",   
						"applyDay" : applyDay 		  
					
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
						}else{
							
							DayFinalizeCheck=data.dayAttdMgtList;
							}
						}

		
	})
	};

	/* 월근태관리 목록 조회버튼 함수 */
	function findMonthAttdMgtList(){
		
		
		
		if($("#searchYearMonth").val()==""){  // option의 value값을 가져온다
			alert("조회연월을 선택해 주세요");
			return;
		}
		

		$.ajax({
			url:"${pageContext.request.contextPath}/attendance/monthAttendanceManage.do",  // 서비스 퍼사드 까지는 findMonthAttdMgtList 가다가 마지막 applicationServiece에서 batchMonthAttdMgtProcess 로 바뀌네?
			data:{
				"method" : "findMonthAttdMgtList",
				"applyYearMonth" : $("#searchYearMonth").val()  // option의 value값  2021-1   2021-10
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

				monthAttdMgtList = data.monthAttdMgtList; // 새로 만들어진 값을 리턴
				console.log(monthAttdMgtList);
				
 				for(var index in monthAttdMgtList){
 					monthAttdMgtList[index].workHour = getRealTime(monthAttdMgtList[index].workHour);
 					monthAttdMgtList[index].overWorkHour = getRealTime(monthAttdMgtList[index].overWorkHour);
 					monthAttdMgtList[index].nightWorkHour = getRealTime(monthAttdMgtList[index].nightWorkHour);
 					monthAttdMgtList[index].holidayWorkHour = getRealTime(monthAttdMgtList[index].holidayWorkHour);
 					monthAttdMgtList[index].holidayOverWorkHour = getRealTime(monthAttdMgtList[index].holidayOverWorkHour);
 					monthAttdMgtList[index].holidayNightWorkHour = getRealTime(monthAttdMgtList[index].holidayNightWorkHour);
				}
				
				showMonthAttdMgtListGrid();
			}
		});
	}

  
    /* 마감처리 함수 */
    function finalizeMonthAttdMgt(){
    	var info="";
    	var FsCheck=DayFinalizeCheck.map(emp=> emp.finalizeStatus )
		var DayCheck=DayFinalizeCheck.filter(emp=> emp.finalizeStatus=='N');

    	if(FsCheck.includes('N')){  // 배열의 요소중 N이 있으면 false를 반환
			
			DayCheck.forEach(member=>info+=member.empName+"님의"+member.applyDays.split(" ")[0]+"일근태를 마감해주세요 <br> ")
				
				                  /// swal fire에서 줄바꿈을 하기위해  <br> 사용   // spilt 사용이유는 날짜만 보여주기 위해서 
		              Swal.fire({
								  icon: 'warning',
								  title: '일근태마감필요',
							      html: info    //text 말고 html로 해야 br 태그가먹어서 예쁘게 줄바꿈이된다
								});
			return;
		
		}
		
	
		
		
    	
		for(var index in monthAttdMgtList){
			monthAttdMgtList[index].finalizeStatus = "Y"; //마감하려면 상태를 Y 해줘야 프로시저에서 승인상태를 보고 계산.
			monthAttdMgtList[index].status = "update";  //그리고 업데이트로 설정  -- 퍼사드에서 관리 
		}

		var sendData = JSON.stringify(monthAttdMgtList);

		$.ajax({
			url : "${pageContext.request.contextPath}/attendance/monthAttendanceManage.do",
			data : {
				"method" : "modifyMonthAttdList",
				"sendData" : sendData
			},
			dataType : "json",
			success : function(data) {
				if(data.errorCode < 0){
					alert("마감을 실패했습니다");
				} else {
					 Swal.fire({
						  icon: 'success',
						  title: '마감확인'
					     
						});
				}
				setTimeout(function(){location.reload()},2000);
				
			}
		});
	}

    /* 마감취소 함수 */
    function cancelMonthAttdMgt(){
		for(var index in monthAttdMgtList){
			monthAttdMgtList[index].finalizeStatus = "N";
			monthAttdMgtList[index].status = "update";
		}

		var sendData = JSON.stringify(monthAttdMgtList);

		$.ajax({
			url : "${pageContext.request.contextPath}/attendance/monthAttendanceManage.do",
			data : {
				"method" : "modifyMonthAttdList",
				"sendData" : sendData
			},
			dataType : "json",
			success : function(data) {
				if(data.errorCode < 0){
					alert("마감취소를 실패했습니다");
				} else {
					alert("마감취소처리 되었습니다");
				}
				
				location.reload()
			}
		});
	}
    
    

	/* 0000단위인 시간을 (00시간00분) 형식으로 변환하는 함수 */
	function getRealTime(time){
		var hour = Math.floor(time/100); 
		if(hour==25) hour=1; //데이터 베이스에 넘길때는 25시로 받고나서 grid에 표시하는건 1시로
		var min = time-(Math.floor(time/100)*100);
		if(min.toString().length==1) min="0"+min; //분이 1자리라면 앞에0을 붙임
		if(min==0) min="00";
		return hour + ":" + min;
	}
	
	  /* 월근태관리 목록 그리드 띄우는 함수 */
    function showMonthAttdMgtListGrid(){
		var columnDefs = [
		      {headerName: "사원코드", field: "empCode" },
		      {headerName: "사원명", field: "empName"},
		      {headerName: "적용연월", field: "applyYearMonth" },
		      {headerName: "기준근무일수", field: "basicWorkDays"},
		      {headerName: "평일근무일수", field: "weekdayWorkDays"},
		      {headerName: "기준근무시간", field: "basicWorkHour"},
		      {headerName: "실제근무시간", field: "workHour"},
		      {headerName: "연장근무시간", field: "overWorkHour"},
		      {headerName: "심야근무시간", field: "nightWorkHour"},
		      {headerName: "휴일근무일수", field: "holidayWorkDays"},
		      {headerName: "휴일근무시간", field: "holidayWorkHour"},
		      {headerName: "지각일수", field: "lateDays"},
		      {headerName: "결근일수", field: "absentDays"},
		      {headerName: "반차일수", field: "halfHolidays"},
		      {headerName: "휴가일수", field: "holidays"},
		      {headerName: "마감여부", field: "finalizeStatus"},
		      {headerName: "상태", field: "status",hide:true}
		];      
	gridOptions = {
			columnDefs: columnDefs,
			rowData: monthAttdMgtList,
			defaultColDef: { editable: false, width: 100 },
			rowSelection: 'single', /* 'single' or 'multiple',*/
			enableColResize: true,
			enableSorting: true,
			enableFilter: true,
			enableRangeSelection: true,
			suppressRowClickSelection: false,
			animateRows: true,
			suppressHorizontalScroll: true,
			localeText: {noRowsToShow: '조회 결과가 없습니다.'},
			getRowStyle: function (param) {
		        if (param.node.rowPinned) {
		            return {'font-weight': 'bold', background: '#dddddd'};
		        }
		        return {'text-align': 'center'};
		    },
		    getRowHeight: function(param) {
		        if (param.node.rowPinned) {
		            return 30;
		        }
		        return 24;
		    },
		    // GRID READY 이벤트, 사이즈 자동조정 
		    onGridReady: function (event) {
		        event.api.sizeColumnsToFit();
		    },
		    // 창 크기 변경 되었을 때 이벤트 
		    onGridSizeChanged: function(event) {
		        event.api.sizeColumnsToFit();
		    }
	};   
	
	$('#monthAttdMgtList_grid').children().remove();	 
	var eGridDiv = document.querySelector('#monthAttdMgtList_grid');
	new agGrid.Grid(eGridDiv, gridOptions);	
}

</script>
</head>
<body>
<br>
<br>
<br>
<br>
<section id="tabs" style="width:1480px; height:530px;" class="wow fadeInDown">
		<h6 class="section-title h3">월근태관리</h6>
		<div class="container">
		<hr style="background-color:white; height: 1px;">
		</div>
		<h6 style="display:inline-block;">조회월</h6>   <div class="col col-md-4" style="display:inline-block; width:150px;">
                         <select class="form-control" id="searchYearMonth"></select>
                     </div>
			
		<input type= "button" value="조회하기" class="btn btn-light btn-sm" id="search_monthAttdMgtList_Btn">
		<br/> 
		<br/>
		<input type= "button" value="전체마감하기" class="btn btn-light btn-sm" id="finalize_monthAttdMgt_Btn">
		<input type= "button" value="전체마감취소" class="btn btn-light btn-sm" id="cancel_monthAttdMgt_Btn">
		<div id="monthAttdMgtList_grid" style="height: 300px; width: 1440px" class="ag-theme-balham"></div>
</section>
</body>

</html>