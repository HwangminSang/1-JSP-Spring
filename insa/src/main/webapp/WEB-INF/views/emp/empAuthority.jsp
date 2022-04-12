<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
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

#nav-home td{
	margin-right:7px;
	margin-left:7px;
}
</style>
<script>
	var AuthorityGridOptions={};
	var EmpListGridOptions={};
	$(document).ready(function(){
		empInfoList = [];
		adminCodeList = [];
		findEmpInfo();
		findAdimCode();
		$("#authority_btn").click(modifyAuthority);
	});
	
	function findEmpInfo(){
		$.ajax({
			url:'${pageContext.request.contextPath}/emp/empList.do',
			data:{
				"method" : "emplist",

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
				empInfoList = data.list;
				console.log(empInfoList);
				showEmpListGrid();		
			}
		});
	}
	
	function showEmpListGrid() {
		var columnDefs = [ { headerName : "사원코드", field : "empCode" , checkboxSelection: true}, 
						   { headerName : "사원명", field : "empName" }, 
			               { headerName : "직급", field : "position" },
			               { headerName : "부서", field : "deptName" }
		];
		EmpListGridOptions = {
			columnDefs : columnDefs,
			rowData : empInfoList,
			defaultColDef : { editable : false, width : 100 },
			rowSelection : 'single', 
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
			
			onGridReady : function(event) {
				event.api.sizeColumnsToFit();
			},
			
			onGridSizeChanged : function(event) {
				event.api.sizeColumnsToFit();
			}
		};
		
		var eGridDiv = document.querySelector('#myGrid1');
		new agGrid.Grid(eGridDiv, EmpListGridOptions);
	}
	
	function findAdimCode(){
		$.ajax({
			url:'${pageContext.request.contextPath}/base/adminCodeList.do',
			data:{
				"method" : "adminCodeList"
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
				
				adminCodeList = data.adminCodeList;
				console.log(data.adminCodeList);
				showAuthorityListGrid();		
			}
		});
	}
	
	
	function showAuthorityListGrid() {
		var columnDefs = [ { headerName : "권한명", field : "admin_authority" }, 
						   { headerName : "권한여부",  checkboxSelection: true },
						   { headerName : "체크여부",  field:"status" }
		];
		
		AuthorityGridOptions = {
			columnDefs : columnDefs,
			rowData : adminCodeList,
			defaultColDef : {
				editable : false,
				width : 100
			},
			rowSelection : 'single',
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
					return { 'font-weight' : 'bold', background : '#dddddd' };
				}
				
				return { 'text-align' : 'center' };
			},
			getRowHeight : function(param) {
				if (param.node.rowPinned) {
					return 30;
				}
				return 24;
			},
			onGridReady : function(event) { event.api.sizeColumnsToFit(); },
			onGridSizeChanged : function(event) { event.api.sizeColumnsToFit(); }
		}
		$('#myGrid2').children().remove();
		
		var eGridDiv = document.querySelector('#myGrid2');
		new agGrid.Grid(eGridDiv, AuthorityGridOptions);
	}
	
	
	
	function modifyAuthority(){  //권한부여 버튼
		authorityList = AuthorityGridOptions.api.getSelectedRows();  //오른쪽창   []
		empList = EmpListGridOptions.api.getSelectedRows();  //왼쪽
		
		console.log(authorityList);
		
		if(authorityList.length == 0 | empList.length == 0){
			Swal.fire({
				icon:'warning',
				title:'(o´〰`o)',
				text:'사원 혹은 권한여부를 모두 선택하여 주세요'
			});
			return;
		}
		
		admin_code = authorityList[0].admin_code;    //권한코드 
		empCode = empList[0].empCode // 직원번호
		
		$.ajax({
			url:'${pageContext.request.contextPath}/base/adminCodeList.do',
			data:{
				"method" : "modifyAuthority",
				"empCode" : empCode , 
				"adminCode": admin_code
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
				alert("수정되었습니다");
				console.log("처리 상태 : "+data.errorMsg);
			}
		}); 
		
		
	}
</script>
</head>
<body>
		<section id="tabs" style="width:1000px; height:630px; display: inline-block;" class="wow fadeInDown">
		
			<h6 class="section-title h3" style="text-align=left;">사원권한관리</h6>
			<div class="container">
				<hr style="background-color:white; height: 1px;">
			</div>
			
			
				<div class="tab-pane fade show active" id="nav-home" role="tabpanel" aria-labelledby="nav-home-tab">
					<table>
					<tr>
						<td>
						<div id="divPart1" style="display:inline-block;">
							<div id="myGrid1" style="height: 450px; width: 450px; display:inline-block;" class="ag-theme-balham"></div>
						</div> 
						</td>
						
						<td>
						<div id="divPart2" style="display:inline-block;">
							<div id="myGrid2" style="height: 450px; width: 450px; display:inline-block;" class="ag-theme-balham"></div>
						</div>					
						</td>
					</tr>
					</table>
					<input type="button" id = "authority_btn" value="권한부여" class="btn btn-light">
					<input type="button" id = "authority_btn" value="권한해지" class="btn btn-light">
				</div>	
	</section>

</body>
</html>