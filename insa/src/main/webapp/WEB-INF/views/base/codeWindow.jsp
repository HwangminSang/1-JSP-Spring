<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>CODE</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/script/jquery-ui/jquery-ui.min.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/script/jqgrid/css/ui.jqgrid.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/script/jqgrid/plugins/ui.multiselect.css" />
<script
	src="${pageContext.request.contextPath}/script/jquery/jquery-3.3.1.min.js"></script>
<script
	src="${pageContext.request.contextPath}/script/jqgrid/js/i18n/grid.locale-kr.js"></script>
<script
	src="${pageContext.request.contextPath}/script/jqgrid/js/jquery.jqGrid.min.js"></script>
<script>
	$(document).ready(function() {   //http://www.trirand.com/blog/jqgrid/jqgrid.html jq그리드 관련사이트
		if ("${param.code}" == "") {  // param.code=requeset.getParameter("code") //사원번호가 있는지없는지 확인
			console.log("showCode@@")
			showCode();
		} else {
			console.log("showdept@@")
			showdept();
		}
		
		var pre = opener.document; //부모창
		
		function showdept() {  ////https://aljjabaegi.tistory.com/322 jq그리드 사용법
								////https://blog.naver.com/alsrud477/220336975437
			$('#grid').jqGrid({  
				url : '${pageContext.request.contextPath}/base/codeList.do',
				postData : {                              // ajax 의 data와 같음. 즉 보내는데이터
					code : "${param.code}", 
					method : "detailCodelist"
				},
				datatype : 'json',
				jsonReader : {root : 'detailCodeList'},  //// jsonReader는 서버에서 데이터를 받을 때 매핑하는 옵션입니다. 

				colNames : [ '코드번호', '코드명' ],  //그리드 헤더의 제목 배열,
				colModel : [   //colModel 의 name값과 데이터의 key가 같으면 해당 컬럼으로 출력이 되는
					{name : 'detailCodeNumber', width : 0,	editable : true },
					{name : 'detailCodeName', width : 300, 	editable : true },
				],
				width : '300',
				viewrecords : true, //처리속도 빠름
				onSelectRow : function(rowId) {   // row를 선택했을때 이벤트
					console.log(rowId);   // 회계팀 누르면 1  ,  인사팀은  2
					
					if ("${param.inputCode}" != "") {  
						var codeNumber = $("#grid").getCell(rowId,"detailCodeNumber"); //회계를 누르면 getCell(1,DEP000)
						console.log(codeNumber); //DEP000
						$("#${param.inputCode}",opener.document).val(codeNumber);
					}      //284번줄 <input type="hidden" id="deptCode"> 에 코드 번호가 들어간다.
						   // (자식 ,  부모) 		, opener.document 이창을 열어준창 즉 empRegist1 의 284번
                    var codeName = $("#grid").getCell(rowId,"detailCodeName");
					$("#${param.inputText}", opener.document).val(codeName); //283번 txt_dept
					window.close(); // 현재창 닫음
				},
				loadError : function(xhr) {
													//alert(xhr.readyStatus+","+xhr.status+","+xhr.statusText);
					if (xhr.status == 500) {
						alert("Internal Server Error");
					}
				}
			});
			$("#grid").jqGrid("hideCol", [ 'detailCodeNumber' ]);  //코드번호를 숨기게하기  지우면 코드번호가 나옴
		}
		

		function showCode() { // 코드번호가 없을떄   // ? 현재 안쓰임  < 사원메뉴를 누르고 근태외신청 / 조회를 눌렀을때 근태외 구분에서 사용중임
			console.log(123);
			$('#grid').jqGrid({ //https://javafactory.tistory.com/1218
				url : '${pageContext.request.contextPath}/base/codeList.do', //"DAC004","ADC003","ADC005", "selectRestAttd", "selectRestAttdCode"
					postData : { //데이터 보낼떄   key : values
						code1 : "${param.code1}",  // == request.getParameter("code1");
						code2 : "${param.code2}",
						code3 : "${param.code3}",
						method : "detailCodelistRest"
					},
					datatype : 'json',
					jsonReader : { root : 'detailCodeList'},  // 그리드에 띄울 데이터타입정의
					colNames : [ '코드번호', '코드명' ],
					colModel : [ 
						{ name : 'detailCodeNumber', width : 0, editable : true },
						{ name : 'detailCodeName', width : 300, editable : true },
					],
					width : '300',
					viewrecords : true,
					onSelectRow : function(rowId) {
						console.log("ddd22z!!@@")
						if ("${param.inputCode}" != "") {
							var codeNumber = $("#grid").getCell(rowId,"detailCodeNumber");
							$("#${param.inputCode}",opener.document).val(codeNumber);   
						}
						var codeName = $("#grid").getCell(rowId, "detailCodeName");
						$("#${param.inputText}", opener.document).val(codeName);
						window.close();
					},
					loadError : function(xhr) {
					//alert(xhr.readyStatus+","+xhr.status+","+xhr.statusText);
						if (xhr.status == 500) {
							alert("Internal Server Error");
						}
					}
				});
				$("#grid").jqGrid("hideCol", [ 'detailCodeNumber' ]);
			}
		});
</script>
</head>
<body>
	<div>
		<table id="grid"></table>
	</div>
</body>
</html>