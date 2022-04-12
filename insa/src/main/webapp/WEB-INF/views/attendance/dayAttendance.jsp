	<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>일근태기록/조회</title>
<style type="text/css">
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

input[type=text]:not (#timePicker ) {
	width: 180px;
	height: 10px;
}


#clock {
	margin: auto;
	font-family: 'Digital Dismay';
    src: url('Digital Dismay.otf');
    font-weight: normal;
    font-style: normal;
	color: #ffffff; 	
	color: #daf6ff; 
	text-shadow: 0 0 20px rgba(255, 255, 255, 1), 0 0 20px rgba(153, 153, 153, 0);
}
</style>

<script>
var empCode = "${sessionScope.code}"; // 로그인 후 존재하는 세션값 ( 세션이 말소 되면 얘도 같이 사라짐 )
var currentHours = 0; 				  // 시계의 시간
var currentMinute = 0;			      // 시계의 분
var amPm;    						  // 시계의 AM,PM
var conversionDate = 0; 		      // timePicker의 :를 제거하고 자정 이후의 시간을 변환한 값	
var dayAttdList = []; 				  // 근태목록
var holidayList = []; 				  // 휴일목록
var isHoliday=false; 				  // 휴일여부
var isEarlyOut=false; 				  // 조퇴여부
var restTypeCode=[]; 				  // 근태외코드	
var earlyOutTime = 0; 			      // 조퇴시간
var leaveTime=[];				      // 외출


	
$(document).ready(function(){	
	var today=$.datepicker.formatDate($.datepicker.ATOM,new Date()); // 바로 오늘 날짜 뜨게      
	$("#applyDay").val(today); // 적용일자 
	
	printClock(); 				// 시계표시함수 호출
	findDayAttdList("today"); 	// 시작하자마자 오늘 날짜의 근태목록을 조회목록 grid에 띄움

  	$("#timePicker").button().css({width : "105px",height : "30px"});  //"시간선택"버튼
	$(".small_Btn").button(); // button 화 시킵니다 
  
  	getDatePicker($("#applyDay")); 			// 적용일자

	$("#attdTypeName").click(function(){ 	// 근태구분 입력창
   		getCode("CO-09","attdTypeName","attdTypeCode");
	});

	$("#registDayAttd_Btn").click(registDayAttd); 			// 기록하기 버튼  
	$("#delete_dayAttd_Btn").click(removeDayAttdList); 		// 삭제 버튼 
	
	$("#timePicker").click(function(){  //시각 옆 텍스트박스 눌렀을때 
		$(this).timepicker({
    						step: 5,            		//시간간격 : 5분
    						timeFormat: "H:i",   		//시간:분 으로표시
    						minTime: "00:00am",
    						maxTime: "23:55pm" 
   		});
  	})

  	$("#timeCheck_Btn").click(function(){ 						            // 현시각기록 버튼 눌렀을때 시계기록 눌렀을 때 시계시간, clock 넘기기  16:45:40 PM
		var checkHours = $("#clock").text().split(" ")[0].substring(0,2); 	// 16:45:40 PM 일때 --> 16:45:40   즉 PM을 잘라버림  16 추출
		var checkMinute = $("#clock").text().split(" ")[0].substring(3,5);	// 16:45:40 PM 일때 45 추출
		registDayAttd("Clock",checkHours+checkMinute); // 1645   // 기록하기 눌렀을때 호출 되는 함수. 즉 바로 호출된다 
	})
		
  	$("#applyDay").change(function(){		// 적용일자가 변경되는 이벤트가 발생했을 때 , 그 해당일자에 맞는 일근태 목록을 가지고 옴
  		findDayAttdList("applyDay");
  	});
 });

	



function printClock() {    
	var clock = $("#clock");           		// 출력할 장소 선택
    var currentDate = new Date();      		// 현재시간
    var calendar = currentDate.getFullYear() + "-" + (currentDate.getMonth()+1) + "-" + currentDate.getDate();
    amPm = 'aM'; 							// 초기값 AM
    currentHours = addZeros(currentDate.getHours(),2);  //	0에서 23사이의 정수 시간을 나타냅니다.
    currentMinute = addZeros(currentDate.getMinutes() ,2);  //0에서 59까지의 정수 분을 나타냅니다.
    var currentSeconds = addZeros(currentDate.getSeconds(),2);
     
    if(currentHours >= 12){ 				// 시간이 12보다 클 때 PM으로 세팅
    	amPm = 'pM';
    	  currentHours = addZeros(currentDate.getHours(),2); 
    	  currentMinute = addZeros(currentDate.getMinutes() ,2); 
    	  var currentSeconds = addZeros(currentDate.getSeconds(),2);
    }
     
    currentSeconds = '<span style="font-size:30px">'+currentSeconds+'</span>'; 

    clock.html(currentHours+":"+currentMinute+":"+currentSeconds +" <span style='font-size:30px;'>"+ amPm+"</span>"); 	//시간를 출력해 줌
    setTimeout(printClock, 1000); // 시간이 계속 변경되게 1초에 한번씩 자기자신호출
}


function addZeros(num, digit) { 			// 시계 자릿수 맞춰주기 // 9시 일경우 09시로 만들어줌 
	var zero = '';
    num = num.toString();
    if (num.length < digit) {
    	for (i = 0; i < digit - num.length; i++) {
        zero += '0';
    	}
    }
    return zero + num;
}


/* 일근태목록 조회 함수 */ 
function findDayAttdList(check){ //check 는 적용일자의 날짜 .   // 처음 시작시 check에 today를 할당. 날짜를 변경하면 applyday를 적용
	
	var searchDay = "";
	
	if(check==="today"){ 	//처음 시작시 today				// 해당 함수를 부를때에 today라는 글자가 변수로 넘어오면 오늘 날짜를 searchDay변수에 담아 ajax실행
		var today = new Date();			
	    var rrrr = today.getFullYear();
	    var mm = today.getMonth()+1;  //왜냐하면 컴퓨터는 달이 0부터 시작 
	    var dd = today.getDate();
	    searchDay = rrrr+"-"+mm+"-"+dd;
	    
	}else if(check==="applyDay"){			// #applyDay의 값이 바뀔때 #applyDay의 해당 날짜를 받아와서 searchDay변수에 담는다. // 새로 추가함
		searchDay = $("#applyDay").val();
	}
	console.log(searchDay);
	
	$.ajax({
   			url:"${pageContext.request.contextPath}/attendance/dayAttendance.do",
   			data:{
    			"method" 	: "findDayAttdList",
    			"empCode" 	: empCode,  //로그인후들어오는 화면이라서 session에 저장해둔 id를 화면로딩과 동시에 정의해두었다.
    			"applyDay"	: searchDay
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
				
						console.log(data);
						
    					dayAttdList = data.dayAttdList;   // []
    					
    					for(var index in dayAttdList){  // 데이터 베이스에서 받아온 데이터들의 시간을표기하는곳  []
    			
     						dayAttdList[index].applyDay = getRealDay(dayAttdList[index].time, dayAttdList[index].applyDay);
     						dayAttdList[index].time = getRealTime(dayAttdList[index].time);
    					}
    					
    					showDayAttdListGrid();
   			}
  	});

}


/* 0000단위인 시간을 00:00(실제시간)으로 변환하는 함수 */
function getRealTime(time){
  	var hour = Math.floor(time/100);  //2530 /100 하고 25.3 floor함수를 써서  25로바꿈
  	if(hour>=24) {   //6
  		hour -= 24; 						// 데이터 베이스에 넘길때는 25시로 받고나서 grid에 표시하는건 1시로
  	}
  	var min = time-(Math.floor(time/100)*100); // 2530-2500
  	if(min.toString().length==1) 
  		min="0"+min; 					// 분이 1자리라면 앞에 0을 붙임
  	if(min==0) 
  		min="00";
  	return hour + ":" + min;  // 1:30
}

/* 2400시 이상일때 날짜를 다음날로 출력해줌*/
function getRealDay(time, applyDay){ // 만약에 다음날 새벽 6시 퇴근일경우. 3000 , 근데 데이터베이스는 당일 퇴근으로 되어있음 
	var hour = Math.floor(time/100);	 //console.log(Math.floor(5.95))   expected output: 5
	var date = new Date(applyDay+'/00:00:00');  // 받아온 날짜를 시간으로 바꿈 . 2021-10-10
  	 console.log(date);
	if(hour>=24) { // 24시로 찍었을떄
		date.setDate(date.getDate()+1) //setDate 하여 현재 날짜에 +1을 해줌 . 즉 10일경우  +1 하여 11일 
		applyDay = date.getFullYear()+'/' + ('0'+(date.getMonth()+1)).slice(-2) + '/' + ('0' + date.getDate()).slice(-2);	 // 뒤에서 2번쨰부터 다 가져오기 왜냐하면 012월이면 12 , 024일 이면 24 이렇게 
  	}
  	return applyDay;
}


/* 일근태목록 정보를 그리드에 뿌려주는 함수 */
function showDayAttdListGrid(){
	var columnDefs = [
		{headerName: "사원코드", field: "empCode", hide:true },
	    {headerName: "일련번호", field: "dayAttdCode", hide:true },
	    {headerName: "적용일", field: "applyDay", checkboxSelection: true },
	    {headerName: "근태구분코드", field: "attdTypeCode",hide:true},
	    {headerName: "근태구분명", field: "attdTypeName"},
	    {headerName: "시간", field: "time"}
	];
	gridOptions = {
		columnDefs: columnDefs,
		rowData: dayAttdList,
		defaultColDef: { 
			editable: false, 
			width: 100,
			sortable : true,  // 헤드부분 클릭했을떄 정렬되게
			resizable:true,  
			filter:true   //헤드눌렀을떄 필트기능
		},
		rowSelection: 'multiple',	/* 'single' or 'multiple',*/  // 여러줄 선택가능 여러명 정보가져올수있음
		rowHeight:24,
		enableRangeSelection: true,
		suppressRowClickSelection: false,
		animateRows: true,
		suppressHorizontalScroll: true,
		localeText: {noRowsToShow: '조회 결과가 없습니다.'},	// 데이터가 없을 때 뿌려지는 글	
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
	$('#dayAttdList_grid').children().remove();	 
	var eGridDiv = document.querySelector('#dayAttdList_grid');
	new agGrid.Grid(eGridDiv, gridOptions);	
 }	 


/* timePicker시간을 변경하는 함수 */
function convertTimePicker(){
	conversionDate = $("#timePicker").val().replace(":","");      // 시각 옆에 텍스트박스  14:30 ---> 1430 
		
	if(conversionDate.indexOf("00")==0){ // 없으면 -1 리턴  00 시 일떄   즉 새벽 
		conversionDate = $("#timePicker").val().replace(":","").replace("00","24");  // 다음날 0010 톼근이여도 2410으로 변환
	}
}

/* 삭제버튼 눌렀을 때 실행되는 함수 */
function removeDayAttdList(){
	var selectedRowData=gridOptions.api.getSelectedRows(); // 배열을 가져옴 선택한줄은 {}   //rowSelection: 'multiple' 그리드옵션스에 넣어줘야 된다. 
	var sendData = JSON.stringify(selectedRowData);
	
	if(selectedRowData == ""){
		alert("삭제할 항목을 선택하세요.");
	} else {
		$.ajax({
			url : "${pageContext.request.contextPath}/attendance/dayAttendance.do",
			data : {
					"method" : "removeDayAttdList",
					"sendData" : sendData
			},
			dataType : "json",
			success : function(data) {
				if(data.errorCode < 0){
					alert("정상적으로 삭제되지 않았습니다");
				} else {
	 				alert("삭제되었습니다");
				}
					location.reload();
			}
		});		
	}
}


/* 기록버튼 눌렀을 때 실행되는 함수 */ 					
function registDayAttd(clock,clockTime){	
	var clockCheck = clock; 					 // timepicker로 기록할것인지 시계로 기록할것인지 여부를 가릴 변수     
	var today = new Date($("#applyDay").val());  // 오늘 요일을 가져오기위한 Date객체
	var dayAttd = {}; 						 	 // 데이터를 날릴 json 을 담을 json 객체 
  
	if($("#applyDay").val().trim()==""){
		alert("적용일을 입력해 주세요");
		return ;
  	}  
	
	if($("#attdTypeName").val().trim()==""){
		alert("근태구분을 입력해 주세요");
		return ;
	}
	
	convertTimePicker(); 						// 전역변수 conversionDate에 timePicker선택한 시간을 변환   00:10 분 하더라도 24:10분으로 반환	  
	console.log(conversionDate);
	
	/* 날릴 데이터 셋팅 */
	if(clockCheck=="Clock"){					// 현시각기록 버튼 클릭이었으면 dayAttd 세팅
		dayAttd ={
			"empCode" : empCode,
			applyDay : $("#applyDay").val(), 			// 적용일자 
			attdTypeCode : $("#attdTypeCode").val(),	// 근태코드 
    		attdTypeName : $("#attdTypeName").val(),    // 근태명 
    		time : clockTime 						    // 현시각 기록버튼을 눌렀을 때 clock 섹션에서 가져온 시간 값
   		};
	
  	}else{ 		// 기록하기 버튼 클릭시 dayAttd 세팅
  		
		if(conversionDate==""){
			alert("시간을 입력해 주세요");
			return; 
		}
  	
  		
  		dayAttd ={
			"empCode" : empCode,
    		applyDay : $("#applyDay").val(),
    		attdTypeCode : $("#attdTypeCode").val(),
    		attdTypeName : $("#attdTypeName").val(),
    		time : conversionDate 		// 전역변수 conversionDate 사용
   		};
  	}

	var sendData = JSON.stringify(dayAttd); // 나온 데이터 들을 문자열로 변형 시킵니다 
	
		$.ajax({
			url : "${pageContext.request.contextPath}/attendance/dayAttendance.do",
			data : {
				"method" : "registDayAttd",
				"sendData" : sendData
			},
			dataType : "json",
			success : function(data) {
	 					if(data.errorCode < 0){
		 					alert(data.errorMsg);
						}else {
							alert("기록되었습니다");
						}
	 			
	 					findDayAttdList("applyDay");  //등록하게 새롭게 보여주기위해서
	 			
						}
		}); 	  	
}


/* 날짜 조회창 함수 */
function getDatePicker($Obj, maxDate) {
	$Obj.datepicker({
    	changeMonth : true,
    	changeYear : true,
    	showMonthAfterYear: true,
    	dateFormat : "yy-mm-dd",
    	dayNamesMin : [ "일", "월", "화", "수", "목", "금", "토" ],
    	monthNamesShort : [ "1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월" ],
    	yearRange: "1980:2021",
    	maxDate : (maxDate!=null ? "+100Y" : maxDate) // ?
   	});
}

//코드가져오는 함수
function getCode(code,inputText,inputCode){ // "CO-09","attdTypeName","attdTypeCode"
	option="width=220; height=200px; left=300px; top=300px; titlebar=no; toolbar=no,status=no,menubar=no,resizable=no, location=no";
	window.open("${pageContext.request.contextPath}/base/codeWindow.html?code="+code+"&inputText="+inputText+"&inputCode="+inputCode,"newwins",option); // 서이트 메시 적용안된 화면나온다
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
		<h6 class="section-title h3" style="text-align=left;">일근태기록</h6>
		<hr style="background-color:white; height: 1px;">
			<div
				style="width: 300px; height: 60px; font-size: 30px; text-align: center;" id="clock"></div> <!--시간 나오는 부분 -->
			<table style="margin:auto;">
				<tr>
					<td>적용일자</td>
					<td><input type=text id="applyDay" readonly></td>
				</tr>
				<tr>
					<td>근태구분</td>
					<td><input type=text id="attdTypeName" readonly> <input type=hidden id="attdTypeCode"></td>
				</tr>
				<tr>
					<td>시각</td>
					<td>
					<input type="text" name="timePicker" placeholder="시간선택" id="timePicker" size="10"> 
					<input type="button" id="registDayAttd_Btn" class="small_Btn" value="기록하기" />
					</td>
				</tr>
				<tr>
					<td></td>
					<td><input type="button" id="timeCheck_Btn" class="small_Btn" value="현시각기록" /></td>
				</tr>
			</table>
			<br />
	</div>
</section>
	</td>
	
	<td>
		<section id="tabs" style="width:600px; height:450px; display: inline-block;" class="wow fadeInDown">
	<div class="container">
		<h6 class="section-title h3" style="text-align=left;">일근태조회</h6>
		<hr style="background-color:white; height: 1px;">
		<center>
		<button class="small_Btn" id="delete_dayAttd_Btn" style="align:center;">삭제하기</button>
		</center>
			<br /> 
			<div id="dayAttdList_grid" style="height: 280px; width:420px align:center;" class="ag-theme-balham"></div>
	</div>
	
</section>
	</td>
</tr>
</table>
</body>
</html>
