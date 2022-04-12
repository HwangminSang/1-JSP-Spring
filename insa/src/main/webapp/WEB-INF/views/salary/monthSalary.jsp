<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>개인급여조회</title>
<style>
	/* Tabs*/
section {
    padding: 10px 0;
}

#tabs{
	background: #333333;
    color: #eee;
    background-color: rgba( 051,051, 051, 0.8 );
}
#tabs h6.section-title{
    color: #eee;
}

#tabs .nav-tabs .nav-item.show .nav-link, .nav-tabs .nav-link.active{
    color: #f3f3f3;
    background-color: transparent;
    border-color: transparent transparent #f3f3f3;
    border-bottom: 4px solid !important;
    font-size: 10px;
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
var monthSalary = [];
var yearSalary = [];
var monthSalaryGridData = [];
var empCode = "${sessionScope.code}";

	$(document).ready(function(){
	    showYearSalaryGrid();
	    showMonthDeductionListGrid();
		showMonthExtSalListGrid();
		showMonthSalaryGrid();

		$("#request_Btn").click(printWorkInfoReport); // 발급 클릭시 근데 지금 제대로 작동안함 
		
		$("#yearSalary_col").click(function(){ //연급여조회 탭을 누르면
			$("#deduction_tab").hide(); //공제, 초과수당 grid를 숨긴다
		})

		$("#monthSalary_col").click(function(){ //월급여조회 탭을 누르면
			$("#deduction_tab").show(); //공제, 초과수당 grid를 보여준다
		})
		
		
		// input:text에 버튼 형식의 css 씌움
		
      $("input:text").button().css({ width:"50px",height:"10px"});

	
		
		
		 var year = 2020; 
		// (new Date()).getFullYear();
		for(var y=2010; y<=2021; y++)
			$("#searchYear1").append($("<option />").val(y).text(y+"년 "));
		
		$("#searchYear1").selectmenu();

		// 적용연월 셀렉터
		//var year = 2020; //(new Date()).getFullYear();
		for(var i=1; i<=12; i++)
			$("#searchYearMonth").append($("<option />").val(i).text(i+"월"));
		
		$("#searchYearMonth").selectmenu();
		
		$("#searchYear").selectmenu();
		for(var i=-10; i<11; i++) // 현재 연도에 -10~+10년까지 조회 가능하게함
			$("#searchYear").append($("<option />").val(year+i).text(year+i+"년"));
		
		// 월급여 조회하기 버튼
		$("#monthSearch_Btn").click(findMonthSalary);

		// 연급여 조회하기 버튼
		$("#yearSearch_Btn").click(findYearSalary);
		
		// 마감 버튼
		$("#finalize_Btn").click(finalizeMonthSalary);

		// 마감취소 버튼
		$("#cancel_Btn").click(cancelMonthSalary);

	});
	
	
	/* 급여명세서 */  // 보고서로 감
	function printWorkInfoReport(){
		applyMonth = $("#searchYear1").val()+"-"+$("#searchYearMonth").val();  // 2021-10
		
	    window.open( // 새로운창 띄움 
	            "${pageContext.request.contextPath }/base/empReport.do?method=requestMonthSalary&empCode="+empCode+"&applyMonth="+applyMonth,
	            "급여명세서", //새창이름
	            "width=1280, height=1024"); // 옵션 즉 새창의 크기 
	}


	/* 월급여 조회하기 */
	function findMonthSalary(){
		if($("#searchYearMonth").val()==""){ //1 
			alert("적용 연월을 선택해 주세요");
			return; // 이게 뜰 이유가 없음 . 
		}
		
		console.log("선택한 월자 : ");
		console.log($("#searchYearMonth").val());
		
		$.ajax({
			url:"${pageContext.request.contextPath}/salary/monthSalary.do",
			data:{
				"method" : "findMonthSalary",
				"applyYearMonth" : $("#searchYear1").val()+"-"+$("#searchYearMonth").val(),  //2021-1 
				"empCode" : empCode
			},
			dataType:"json",
			success : function(data){
				if(data.errorCode < 0){
					/* alert()
					var str = "내부 서버 오류가 발생했습니다\n";
					str += "관리자에게 문의하세요\n";
					str += "에러 위치 : " + $(this).attr("id");
					str += "에러 메시지 : " + data.errorMsg; */
					Swal.fire({ icon: 'error',
				  		title: 'Oops...',
				  		text:  data.errorMsg
					})
					return;
				}
				
 				monthSalary = data.monthSalary;

 				/* 가져온 데이터들중에 금액과 관련된 데이터에 콤마,'원'을 붙임 */
 				
 				/* 공제내역들 = [국민연금, 건강보험, 고용보험, 장기요양보험, 운전보험] */
				$.each(monthSalary.monthDeductionList, function(i, elt) {
					monthSalary.monthDeductionList[i].price = numberWithCommas(monthSalary.monthDeductionList[i].price)+"원";
				});
				
 				/* 초과근무들 = [연장근무, 심야근무, 휴일근무]*/
				$.each(monthSalary.monthExtSalList, function(i, elt) {
					monthSalary.monthExtSalList[i].price = numberWithCommas(monthSalary.monthExtSalList[i].price)+"원";
				});
				
 
				monthSalary.realSalary = numberWithCommas(monthSalary.realSalary)+"원";
				monthSalary.salary = numberWithCommas(monthSalary.salary)+"원";
				monthSalary.totalDeduction = numberWithCommas(monthSalary.totalDeduction)+"원";
				monthSalary.totalExtSal = numberWithCommas(monthSalary.totalExtSal)+"원";
				monthSalary.totalPayment = numberWithCommas(monthSalary.totalPayment)+"원";
				monthSalary.cost         =  numberWithCommas(monthSalary.cost)+"원";            
				
				/* monthSalary 통째로 그리드에 넣으면 인식이 안됨 그래서 따로 빼서 객체화시켜 집어넣음 */ // 왜냐하면 일대 다 관계라서  monthSalary 안에 또  .monthDeductionList  공제내역들  ,monthExtSalList  초과근무배열이 있다.
				monthSalaryGridData.push({ // 그래서 위에서 빈배열에 push 해준다. 
					"empCode":monthSalary.empCode, 
				    "applyYearMonth":monthSalary.applyYearMonth,
				    "salary":monthSalary.salary,
				    "totalExtSal":monthSalary.totalExtSal,
				    "totalPayment":monthSalary.totalPayment,
				    "totalDeduction":monthSalary.totalDeduction,
				    "realSalary":monthSalary.realSalary,
				    "cost":monthSalary.cost,
				    "unusedDaySalary":monthSalary.unusedDaySalary,
				    "finalizeStatus":monthSalary.finalizeStatus
				});

				/* 데이터를 가져와서 일련의 작업 후 바로 그리드 재호출 */
				showMonthSalaryGrid();  // 월급여 
				showMonthDeductionListGrid(); // 공제내역
				showMonthExtSalListGrid();  //초과수당 
				
				/* Data를 담는 배열들은 초기화 해줌 */
				monthSalary = {};
				yearSalary = {};
				monthSalaryGridData = [];
			}
		});
		
	}

	/* 연급여 조회하기 */
	function findYearSalary(){
		if($("#searchYear").val()==""){
			alert("적용 연도를 선택해 주세요");
			return;
		}
		$.ajax({
			url:"${pageContext.request.contextPath}/salary/monthSalary.do",
			data:{
				"method" : "findYearSalary",
				"applyYear" : $("#searchYear").val(),  // DAO에서 2021% 로 처리하기떄문에 2021만 날려도된다 
				"empCode" : empCode
			},
			dataType:"json",
			success : function(data){
				if(data.errorCode < 0){
					var str = "내부 서버 오류가 발생했습니다\n";
					str += "관리자에게 문의하세요\n";
					str += "에러 위치 : " + $(this).attr("id");
					str += "에러 메시지 : " + data.errorMsg;
					Swal.fire({
						  icon: 'error',
						  title: 'Oops...',
						  text: str
						}).
					return;
				}
				
				if(data.yearSalary==""){  // 값이 업을경우 
					Swal.fire({ icon: 'error',
				  		title: 'Oops...',
				  		text:  "해당 연도의 마감 완료된 급여 정보가\n존재하지 않습니다"
					})
				}else{  //값이 있을경우 

					yearSalary = data.yearSalary; // [] 
					
					$.each(yearSalary, function(i, elt) {
					    yearSalary[i].realSalary = numberWithCommas(yearSalary[i].realSalary)+"원";
					    yearSalary[i].salary = numberWithCommas(yearSalary[i].salary)+"원";
					    yearSalary[i].totalDeduction = numberWithCommas(yearSalary[i].totalDeduction)+"원";
					    yearSalary[i].totalExtSal = numberWithCommas(yearSalary[i].totalExtSal)+"원";
					    yearSalary[i].totalPayment = numberWithCommas(yearSalary[i].totalPayment)+"원";
					});
					
					showYearSalaryGrid();
					monthSalary = {}; // 새로비워둔다 그래서 마감처리시 
					yearSalary = {};
					monthSalaryGridData = [];
				}
			}
		});
	}

	/* 숫자 3자리마다 콤마를 찍는 정규표현식 */
	function numberWithCommas(won) {
	    return won.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	}

    /* 마감처리 함수 */
    function finalizeMonthSalary(){
		monthSalary.finalizeStatus = "Y";   // 월급조회후 다시 다 비워둔놈에 새롭게 넣는다.
		monthSalary.empCode=empCode;
		monthSalary.applyYearMonth=$("#searchYear1").val()+"-"+$("#searchYearMonth").val(); // 2021-1
	
		var sendData = JSON.stringify(monthSalary);  //{"finalizeStatus":"Y","empCode":"A490079","applyYearMonth":"2021-10"}
		 
		
		$.ajax({
			url : "${pageContext.request.contextPath}/salary/monthSalary.do",
			data : {
				"method" : "modifyMonthSalary",
				"sendData" : sendData
			},
			dataType : "json",
			success : function(data) {
				if(data.errorCode < 0){
					Swal.fire({
						  icon: 'error',
						  title: 'Oops...',
						  text: '마감이 실패하였습니다',
						  confirmButtonText: '확인'
						}).then((result) => {
							if(result.isConfirmed) {
								location.reload();
							}
						})
				} else {
					Swal.fire({
						  icon: 'success',
						  title: 'Complete !',
						  text: '마감이 성공적으로 처리 되었습니다 !',
						  confirmButtonText: '확인'
						}).then((result) => {
							if(result.isConfirmed) {
								location.reload();
							}
						})
				}
				//location.reload();  // 위쪽에 then 사용하여 그쪽에서 처리 
			}
		});
	}
    
    /* 마감취소 처리 함수 */
    function cancelMonthSalary(){
		monthSalary.finalizeStatus = "N";  // 아무것도 없는 객체에 키값을 설정하고 넣는다 .
		monthSalary.empCode=empCode;
		monthSalary.applyYearMonth=$("#searchYear1").val()+"-"+$("#searchYearMonth").val();
		
		var sendData = JSON.stringify(monthSalary);
		
		$.ajax({
			url : "${pageContext.request.contextPath}/salary/monthSalary.do",
			data : {
				"method" : "modifyMonthSalary",
				"sendData" : sendData
			},
			dataType : "json",
			success : function(data) {
				if(data.errorCode < 0){
					Swal.fire({
						  icon: 'error',
						  title: 'Oops...',
						  text: '마감 취소가 실패하였습니다',
						  confirmButtonText: '확인'
						}).then((result) => {
							if(result.isConfirmed) {
								location.reload();
							}
						})
				} else {
					Swal.fire({
						  icon: 'success',
						  title: 'Complete !',
						  text: '마감이 성공적으로 처리 되었습니다',
						  confirmButtonText: '확인'
						}).then((result) => {
							if(result.isConfirmed) {
								location.reload();
							}
						})
				}
				//location.reload();
			}
		});
	}
    
    /* 월급여조회 그리드 */
	function showMonthSalaryGrid(){
		var columnDefs = [
		      {headerName: "사원코드", field: "empCode",hide:true },
		      {headerName: "적용연월", field: "applyYearMonth",hide:true},
 		      {headerName: "본 급여", field: "salary" },
		      {headerName: "초과수당합계", field: "totalExtSal"},
		      {headerName: "총 급여", field: "totalPayment"},
		      {headerName: "공제금액합계", field: "totalDeduction"},
		      {headerName: "차인지급액", field: "realSalary"},
		      {headerName: "경비지급액", field: "cost"},
		      {headerName: "연차미사용수당", field: "unusedDaySalary"},
		      {headerName: "마감여부", field: "finalizeStatus"}
		];      
		gridOptions = {
			columnDefs: columnDefs,
			rowData: monthSalaryGridData, // 조회하기 에서 빈배열에 넣었다 .
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
		    },
		    onCellEditingStarted: function (event) {
		        console.log('cellEditingStarted');
		    }, 
		};   
		$('#monthSalary_grid').children().remove();	 
		var eGridDiv = document.querySelector('#monthSalary_grid');
		new agGrid.Grid(eGridDiv, gridOptions);	
	}

	/* 연급여조회 그리드 */
    function showYearSalaryGrid() {
		var columnDefs = [
		      {headerName: "사원코드", field: "empCode",hide:true },
		      {headerName: "적용연월", field: "applyYearMonth",hide:true},
		      {headerName: "본 급여", field: "salary" },
		      {headerName: "초과수당합계", field: "totalExtSal"},
		      {headerName: "총 급여", field: "totalPayment"},
		      {headerName: "공제금액합계", field: "totalDeduction"},
		      {headerName: "차인지급액", field: "realSalary"}
		]; 
		
		gridOptions = {
			columnDefs: columnDefs,
			rowData: yearSalary,
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
		    },
		    onCellEditingStarted: function (event) {
		        console.log('cellEditingStarted');
		    }, 
	};   
	$('#yearSalary_grid').children().remove();	 
	var eGridDiv = document.querySelector('#yearSalary_grid');
	new agGrid.Grid(eGridDiv, gridOptions);	
}

    
	/* 공제내역 그리드 */
	function showMonthDeductionListGrid(){
		var columnDefs = [
		      {headerName: "사원코드", field: "empCode",hide:true },
		      {headerName: "적용연월", field: "applyYearMonth"},
		      {headerName: "공제항목코드", field: "deductionCode" },
		      {headerName: "공제항목명", field: "deductionName"},
		      {headerName: "공제금액", field: "price"},
		];      
	gridOptions = {
			columnDefs: columnDefs,
			rowData: monthSalary.monthDeductionList, //일 대 다 관계
			defaultColDef: { editable: false, width: 100 },
			rowSelection: 'single', 	/* 'single' or 'multiple',*/
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
		    },
		    onCellEditingStarted: function (event) {
		        console.log('cellEditingStarted');
		    }, 
	};   
	$('#monthDeductionList_grid').children().remove();	 
	var eGridDiv = document.querySelector('#monthDeductionList_grid');
	new agGrid.Grid(eGridDiv, gridOptions);	
}

	/* 초과수당내역 그리드 */
	function showMonthExtSalListGrid(){
		var columnDefs = [
		      {headerName: "사원코드", field: "empCode",hide:true },
		      {headerName: "적용연월", field: "applyYearMonth"},
		      {headerName: "초과수당코드", field: "extSalCode" },
		      {headerName: "초과수당명", field: "deductionName"},
		      {headerName: "금액", field: "price"},
		];      
	gridOptions = {
			columnDefs: columnDefs,
			rowData: monthSalary.monthExtSalList,  // 일 대 다 배열에 담아와서 
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
		    },
		    onCellEditingStarted: function (event) {
		        console.log('cellEditingStarted');
		    }, 
	};   
	$('#monthExtSalList_grid').children().remove();	 
	var eGridDiv = document.querySelector('#monthExtSalList_grid');
	new agGrid.Grid(eGridDiv, gridOptions);	
}
</script>
</head>
<body>
	<section id="tabs" class="wow fadeInDown">
	<div class="container">
		<nav>
			<div class="nav nav-tabs" id="nav-tab" role="tablist">
				<a class="nav-item nav-link active" id="monthSalary_col" data-toggle="tab" href="#monthSalary_tab" role="tab" aria-controls="nav-home" aria-selected="true">월급여조회</a>
				<a class="nav-item nav-link" id="yearSalary_col" data-toggle="tab" href="#yearSalary_tab" role="tab" aria-controls="nav-profile" aria-selected="false">연급여조회</a>
			</div>
		</nav>
		</div>	
			<div class="tab-content py-3 px-3 px-sm-0" id="nav-tabContent">
				<div class="tab-pane fade show active" id="monthSalary_tab" role="tabpanel" aria-labelledby="nav-home-tab">
					  조회 년 <select id="searchYear1"></select> 조회 월 <select id="searchYearMonth"></select>
						<input type="button" class="ui-button ui-widget ui-corner-all" id="monthSearch_Btn" value="조회하기"/><br /><br />
						<input type="button" class="ui-button ui-widget ui-corner-all" id="finalize_Btn" value="마감"/>
						<input type="button" class="ui-button ui-widget ui-corner-all" id="cancel_Btn" value="마감취소"/>
						<input type="button" class="ui-button ui-widget ui-corner-all" id="request_Btn" value="발급"/><br /><br />
						<div id="monthSalary_grid" style="height: 230px; width:1200px" class="ag-theme-balham"></div>
				</div>
				
				<div class="tab-pane fade" id="yearSalary_tab" role="tabpanel" aria-labelledby="nav-profile-tab">
					조회 연도 <select id="searchYear"></select><input type="button" class="ui-button ui-widget ui-corner-all" id="yearSearch_Btn" value="조회하기"/>
			<div>마감 완료된 월의 급여 정보만 출력됩니다</div>
			<br>
			<br>
			<div id="yearSalary_grid" style="height: 250px; width:1200px" class="ag-theme-balham"></div>
				</div>
			</div>
</section>

<br>
	<div id = "deduction_tab" class="wow fadeInDown">
	<section id="tabs">
	<div class="container" id="deduction_tab">
		<nav>
			<div class="nav nav-tabs" id="nav-tab" role="tablist">
				<a class="nav-item nav-link active" id="nav-home-tab" data-toggle="tab" href="#deduction_tabs" role="tab" aria-controls="nav-home" aria-selected="true">공제내역</a>
				<a class="nav-item nav-link" id="nav-profile-tab" data-toggle="tab" href="#ext_sal_tabs" role="tab" aria-controls="nav-profile" aria-selected="false">초과수당내역</a>
			</div>
		</nav>
		</div>	
			<div class="tab-content py-3 px-3 px-sm-0" id="nav-tabContent">
				<div class="tab-pane fade show active" id="deduction_tabs" role="tabpanel" aria-labelledby="nav-home-tab">
					<div id="monthDeductionList_grid" style="height: 230px; width:1200px" class="ag-theme-balham"></div>
					<div id="monthDedcutionList_pager"></div>
				</div>
				<div class="tab-pane fade" id="ext_sal_tabs" role="tabpanel" aria-labelledby="nav-profile-tab">
					<div id="monthExtSalList_grid" style="height: 230px; width:1200px" class="ag-theme-balham"></div>
					<div id="monthExtSalList_pager"></div>
				</div>
			</div>	
</section>
</div>
</body>
</html>