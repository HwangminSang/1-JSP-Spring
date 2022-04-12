<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>재직증명서 승인 관리</title>
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
select{
	font-size:small;
}
</style>

<script>
	var certificateRequestList = [];
	//var lastId = 0;

	$(document).ready(function() {
		
		      $("input:text").button();  //없어도 상관없음 
				$(".small_Btn").button();

				$(".comment_div").css({ fontSize : "0.7em" });
				
				$("#certificateApproval_tabs").tabs().css({ margin : "10px" });

				getDatePicker($("#search_startDate"));
				getDatePicker($("#search_endDate"));

				showCertificateListGrid();  // AJAX 연결도 없는데 왜 ? 먼저 호출해서 받아오지?

				findCertificateRequestList('모든부서');  // 처음에는 모든부서로 설정하여 데이터를 받아온다.   startdate 는 오늘 날짜 enddate는 비었음

				$("#search_certificate_deptName").click(
						function() {
							getCode("CO-07", "search_certificate_deptName","search_certificate_deptCode");
						}); // 부서선택

				$("#search_startDate").change(function() {$("#search_endDate").datepicker("option", "minDate", $(this).val());}); 

				$("#search_endDate").change(function() {$("#search_startDate").datepicker("option","maxDate", $(this).val());}); // 종료일

				$("#search_certificateList_Btn").click(findCertificateRequestList); // 조회버튼  DAO감
				$("#apploval_certificate_Btn").click(applovalCertificateRequest); // 승인버튼   AG그리드 이용 DAO안감
				$("#cancel_certificate_Btn").click(cancelCertificateReqeust); // 승인취소버튼    AG그리드 이용 DAO안감
				$("#reject_certificate_Btn").click(rejectCertificateRequest); // 반려버튼   AG그리드 이용 DAO안감
				$("#update_certificate_Btn").click(modifyCertificateRequest); // 확정버튼   DAO감
			});

	/*그리드 띄우는 함수 */
	function showCertificateListGrid() {
		var columnDefs = [ {
			headerName : "부서",
			field : "deptName",
			checkboxSelection : true
		}, {
			headerName : "사원번호",
			field : "empCode"
		}, {
			headerName : "사원이름",
			field : "empName"
		}, {
			headerName : "용도",  //기타제출종 
			field : "usageName"
		}, {
			headerName : "신청일",
			field : "requestDate"
		}, {
			headerName : "사용일",
			field : "useDate"
		}, {
			headerName : "승인여부",
			field : "approvalStatus"
		}, {
			headerName : "반려사유",
			field : "rejectCause",
			editable : true
		}, {
			headerName : "비고",
			field : "etc"
		}, {
			headerName : "상태",
			field : "status",
			hide : true
		} ];
		gridOptions = {
			columnDefs : columnDefs,
			rowData : certificateRequestList,
			defaultColDef : {
				editable : false,
				width : 100
			},
			rowSelection : 'single', /* 'single' or 'multiple',*/
			enableColResize : true,
			enableSorting : true,
			enableFilter : true,
			enableRangeSelection : true,
			suppressRowClickSelection : false,
			animateRows : true,
			suppressHorizontalScroll : true,
			localeText : {  // 데이터가 없을떄 
				noRowsToShow : '조회 결과가 없습니다.'
			},
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
			onCellEditingStarted : function(event) {
				console.log('cellEditingStarted');
			},
		};
		
		$('#certificateList_grid').children().remove();
		var eGridDiv = document.querySelector('#certificateList_grid');
		new agGrid.Grid(eGridDiv, gridOptions);
	}

	/* 재직증명서 조회버튼 함수 */
	function findCertificateRequestList(allDept) {
		console.log(allDept);  // 첫시작시 '모드부서'
		
		var deptName = $("#search_certificate_deptName").val();
		var startDate = $("#search_startDate").val();
		var endDate = $("#search_endDate").val();

		
		if (allDept == "모든부서") { //넘어온값이 처음시작 
			deptName = allDept; //deptName를 모든부서로 바꾸고
			
			var today = new Date();
			var rrrr = today.getFullYear();
			var mm = today.getMonth() + 1;
			var dd = today.getDate();
			startDate = rrrr + "-" + mm + "-" + dd; //시작일을 오늘날짜로 넘긴다
		}
		console.log(endDate)

		
				$.ajax({
					url : "${pageContext.request.contextPath}/certificate/certificateApproval.do",
					data : {
						"method" : "findCertificateListByDept", 
						"deptName" : deptName,
						"startDate" : startDate,
						"endDate" : endDate,
					},
					dataType : "json",
					success : function(data) {
						if (data.errorCode < 0) {
							var str = "내부 서버 오류가 발생했습니다\n";
							str += "관리자에게 문의하세요\n";
							str += "에러 위치 : " + $(this).attr("id");
							str += "에러 메시지 : " + data.errorMsg;
							alert(str);
							return;
						}

						certificateRequestList = data.certificateList;
						showCertificateListGrid();
					}
				});
	}

	/*  확정버튼 눌렀을 때 실행되는 함수 */
	function modifyCertificateRequest() {
		var sendData = JSON.stringify(certificateRequestList);   // AG그리드에서 수정된 정보를 가지고옴   [{}]  
 
		$.ajax({
			url : "${pageContext.request.contextPath}/certificate/certificateApproval.do",
			data : {"method" : "modifyCertificateList",
					"sendData" : sendData
			},
			dataType : "json",
			success : function(data) {
				if (data.errorCode < 0) {
					var str = "내부 서버 오류가 발생했습니다\n";
					str += "관리자에게 문의하세요\n";
					str += "에러 위치 : " + $(this).attr("id");
					str += "에러 메시지 : " + data.errorMsg;
					alert(str);
				} else {
					alert("확정되었습니다");
				}
					findCertificateRequestList('모든부서');
				}
		});
	}

	/* 승인버튼 함수 */
	function applovalCertificateRequest() {
		var rowNode = gridOptions.api.getSelectedNodes();  // [{}]
		//console.log(rowNode[0].data);
		if (rowNode.length === 0) {
			alert("승인할 재직증명서 항목을 선택해 주세요");
			return;
		}
		rowNode[0].setDataValue('approvalStatus', "승인");
		rowNode[0].setDataValue('rejectCause', "");
		rowNode[0].setDataValue('status', "update");
		console.log(rowNode[0].data);
	}
	/* 승인취소버튼 함수 */
	function cancelCertificateReqeust() {
		var rowNode = gridOptions.api.getSelectedNodes();
		if (rowNode.length === 0) {
			alert("취소할 재직증명서 항목을 선택해 주세요");
			return;
		}
		rowNode[0].setDataValue('approvalStatus', "승인취소");
		rowNode[0].setDataValue('status', "update");
	}
	/* 근태외 반려버튼 함수 */
	function rejectCertificateRequest() {
		var rowNode = gridOptions.api.getSelectedNodes();
		if (rowNode.length === 0) {
			alert("반려할 재직증명서 항목을 선택해 주세요");
			return;
		}
		rowNode[0].setDataValue('approvalStatus', "반려");
		rowNode[0].setDataValue('status', "update");
	}

	/* 날짜 조회창 함수 */
	function getDatePicker($Obj, maxDate) {
		$Obj.datepicker({
			defaultDate : "d",
			changeMonth : true,
			changeYear : true,
			dateFormat : "yy-mm-dd",
			dayNamesMin : [ "일", "월", "화", "수", "목", "금", "토" ],
			monthNamesShort : [ "1", "2", "3", "4", "5", "6", "7", "8", "9",
					"10", "11", "12" ],
			yearRange : "1980:2021",
			showOptions : "up",
			maxDate : (maxDate == null ? "+100Y" : maxDate)
		});
	}

	/* 코드 선택창 띄우는 함수 */
	function getCode(code, inputText, inputCode) {
		option = "width=220; height=200px; left=300px; top=300px; titlebar=no; toolbar=no,status=no,menubar=no,resizable=no, location=no";
		window.open(
				"${pageContext.request.contextPath}/base/codeWindow.html?code="
						+ code + "&inputText=" + inputText + "&inputCode="
						+ inputCode, "newwins", option);
	}
</script>
</head>
<body>
	<br>
	<br>
	<br>
	<br>
		<section id="tabs" style="width:1350px; height:530px;" class="wow fadeInDown">
		<h6 class="section-title h3">재직증명서 관리</h6>
		<div class="container">
		<hr style="background-color:white; height: 1px;">
		</div>
		<h6 style="display:inline-block;">조회부서</h6> 
              	<input type=text id="search_certificate_deptName" style="width:100px;" readonly>
              	<input type=hidden id="search_certificate_deptCode">
              	
       <h6 style="display:inline-block;">  조회일자</h6>  
       		<input type=text id="search_startDate" style="width:100px;" readonly> ~ <input type=text id="search_endDate" style="width:100px;" readonly>
       
		<input type= "button" value="조회하기" class="btn btn-light btn-sm" id="search_certificateList_Btn">
		<br/> 
		<br/>
		<input type= "button" value="승인" class="btn btn-light btn-sm" id="apploval_certificate_Btn">
		<input type= "button" value="승인취소" class="btn btn-light btn-sm" id="cancel_certificate_Btn">
		<input type= "button" value="반려" class="btn btn-light btn-sm" id="reject_certificate_Btn">
		<input type= "button" value="확정" class="btn btn-light btn-sm" id="update_certificate_Btn">
		<div id="certificateList_grid" style="height: 300px; width: 1300px" class="ag-theme-balham"></div>
</section>
</body>
</html>