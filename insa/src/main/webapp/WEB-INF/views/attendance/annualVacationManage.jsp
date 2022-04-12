<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>연차관리</title>

<!-- Load D3.js -->
<script src="https://d3js.org/d3.v4.min.js"></script>
<!-- Load billboard.js with style -->
<%-- <script src="${pageContext.request.contextPath}/js/billboard.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/billboard.css"> --%>

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

</style>
<script>
   flag = true;
   var annualVacationData = [];
   var annualVacationMgtList =[];
   var empCode = "${sessionScope.code}";
   
   $(document).ready(function() {
      showAnnualVacationGrid();
      $("#searchYear").change(function(){document.getElementById("searchButton").disabled = false;});  //따라서 이 속성을 사용하면 특정 조건이 충족될 때까지 사용자가 버튼을 클릭할 수 없도록 설정하고, 특정 조건이 충족되면 자바스크립트 등으로 disabled 속성값을 삭제하여 사용자가 버튼을 다시 사용할 수 있도록 조절할 수 있습니다.
      $("#searchMonth").change(function(){document.getElementById("searchButton").disabled = false;});  //http://tcpschool.com/examples/tryit/tryhtml.php?filename=html_ref_tag_button_disabled_01
                                                                                      // 날짜를 바꿔야만 false로 줘서 조회하기 버튼이 활성하된다
      /* 적용년 셀렉터 */
      nowDate = new Date();
      
      for(var y=2010; y<=nowDate.getFullYear()+1; y++){
         $("#searchYear").append($("<option />").val(y).text(y+"년"));
      }
      
      /* 적용월 셀렉터 */
      //$("#searchMonth").selectmenu();
      for(var i=1; i<=12; i++){
         $("#searchMonth").append($("<option />").val(i).text(i+"월"));
      }
      
      /* 현황조회, 마감처리, 마감취소 버튼 */
      $("#searchButton").click(findAnnualVacationMgtList);
      $("#finalizeButton").click(finalizeAnnualVacationMgt);
      $("#cancelButton").click(cancelAnnualVacationMgt);
      $("#CreateButton").click(createAnnualVacation);
         
                                             
   });
   
   function createAnnualVacation(){  //연차생성
	   const today = new Date();  //버튼클릭시 오늘 날짜를 얻어온다
	   const year=today.getFullYear(); //년도만 따로 뽑아서 만든다.
	 
	   console.log(year);
	   
	  var Check=confirm(year+1+"년도 모든직원의 연차를 생성하시겠습니까?");
	  
	  if(Check){
	   
	   $.ajax({
	         url:"${pageContext.request.contextPath}/attendance/annualVacationManage.do",
	         data:{
	            "method" : "createVacation",
	            "applyYear" : year
	         },
	         dataType:"json",
	         success : function(data){
	             
	            if(data.errorCode < 0){
	               var str = "내부 서버 오류가 발생했습니다\n";
	               str += "관리자에게 문의하세요\n";
	               
	               str += "에러 메시지 : " + data.errorMsg;
	               alert(str);
	               return;
	            }else{
	            	
	            	alert((year+1)+'년의 연차가 생성되었습니다.')
	            }
	            
	            setTimeout(location.reload(),2000);
	   
	           
	         }
	      });
	   
	  }
   }
   
   
   /* 연차현황 조회버튼 함수 */
   function findAnnualVacationMgtList(){
	   
      if($("#searchMonth").val()==""){
         alert("적용연월을 선택해 주세요");
         return;
      }
      
      document.getElementById("searchButton").disabled = true;  //클릭후 버튼을 비활성하 한다
      
       var year=$("#searchYear").val(); //2021
       var month=addZeros($("#searchMonth").val(),2);   // 9, 2 ---> 09로 바꿔주는 함수 addZeros
       var date=year+month;  // 202109
       console.log("Request Date : "+date);
       
      $.ajax({
         url:"${pageContext.request.contextPath}/attendance/annualVacationManage.do",
         data:{
            "method" : "findAnnualVacationMgtList",
            "applyYearMonth" : date
         },
         dataType:"json",
         success : function(data){
            flag=false; 
            if(data.errorCode < 0){
            	 console.log(data);
            	var error=data.errorMsg.trim(); 
            	 
             	if(error.includes('이전달')){
             		alert(data.errorMsg)
             	    return;
             	 }
             	
               var str = "내부 서버 오류가 발생했습니다\n";
               str += "관리자에게 문의하세요\n";
               str += "에러 위치 : " + $(this).attr("id") + "\n";
               str += "에러 메시지 : " + data.errorMsg;
               alert(str);
               return;
            }
   
            annualVacationMgtList = data.annualVacationMgtList;
            showAnnualVacationGrid();
         }
      });
   }
   
   /* 날짜 자리수 맞춰주는 함수 */
   function addZeros(num, digit) {           
      var zero = '';
       num = num.toString();
       if (num.length < digit) {
          for (i = 0; i < digit - num.length; i++) {
           zero += '0';
          }
       }
       return zero + num;
   }
   
   
   /* 근태현황조회 그리드 */
   function showAnnualVacationGrid(){
      var columnDefs = [
            {headerName: "사원코드", field: "empCode"},
            {headerName: "직원이름", field: "empName"},
            {headerName: "적용년월", field: "applyYearMonth"},
            {headerName: "반차사용개수", field: "afternoonOff"},
            {headerName: "연차사용개수", field: "monthlyLeave"},
            {headerName: "남은연차", field: "remainingHoliday"},
            {headerName: "마감여부", field: "finalizeStatus"}
      ];      
      gridOptions = {
         columnDefs: columnDefs,
         rowData: annualVacationMgtList,
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
      $('#annualVacation_grid').children().remove();    
      var eGridDiv = document.querySelector('#annualVacation_grid');
      new agGrid.Grid(eGridDiv, gridOptions);   
   }
   
   
   /* 마감처리 함수 */
   function finalizeAnnualVacationMgt(){
      for(var index in annualVacationMgtList){  // 데이터에서 받아온상태 배열  FOR IN은 INDEX 나옴 
         annualVacationMgtList[index].finalizeStatus = "Y"; // 모든 사람들의 마감여부를 Y로 수정
         annualVacationMgtList[index].status = "update"; //상태 
      }
   
      var sendData = JSON.stringify(annualVacationMgtList); //서버로 보내기위해 문자열로
   
      alert(sendData);
      
      $.ajax({
         url : "${pageContext.request.contextPath}/attendance/annualVacationManage.do",
         data : {
            "method" : "modifyAnnualVacationMgtList",
            "sendData" : sendData
         },
         dataType : "json",
         success : function(data) {
            flag = true;
            if(data.errorCode < 0){
               alert("마감을 실패했습니다");
            } else {
               alert("마감처리 되었습니다");
            }
            location.reload();
         }
      });
   }
   
   /* 마감취소 함수 */
   function cancelAnnualVacationMgt(){
      for(var index in annualVacationMgtList){
         annualVacationMgtList[index].finalizeStatus = "N";
         annualVacationMgtList[index].status = "update";
      }
   
      var sendData = JSON.stringify(annualVacationMgtList);
   
      $.ajax({
         url : "${pageContext.request.contextPath}/attendance/annualVacationManage.do",
         data : {
            "method" : "cancelAnnualVacationMgtList",
            "sendData" : sendData
         },
         dataType : "json",
         success : function(data) {
            if(data.errorCode < 0){
               alert("마감취소를 실패했습니다");
            } else {
               alert("마감취소처리 되었습니다");
            }
            location.reload();
         }
      });
   }
</script>
</head>

<body class="hm-gradient">
<br>
<br>
<br>
<br>
<br>
<br>
	<section id="tabs" style="width:950px; height:530px;" class="wow fadeInDown">
		<h6 class="section-title h3">연차관리</h6>
		<div class="container">
		<hr style="background-color:white; height: 1px;">
		</div>
		조회 년/월   <div class="col col-md-4" style="display:inline-block; width:150px;">
                         <select class="form-control" id="searchYear"></select>
                     </div>
                      <div class="col col-md-4" style="display:inline-block; width:150px;">
                           <select class="form-control" id="searchMonth"></select>
                       </div>
		<input type= "button" value="조회하기" class="btn btn-light btn-sm" id="searchButton">
		<br />  
		<br/>
		<input type= "button" value="연차생성" class="btn btn-light btn-sm" id="CreateButton">
		<input type= "button" value="마감처리" class="btn btn-light btn-sm" id="finalizeButton">
		<input type= "button" value="마감취소" class="btn btn-light btn-sm" id="cancelButton">
		<div id="annualVacation_grid" style="height: 300px; width: 900px" class="ag-theme-balham"></div>
	</section>
</body>
</html>