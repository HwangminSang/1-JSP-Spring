<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
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

<script type="text/javascript">
//var dustList = [];


$(document).ready(function(){
		
		

	
		$.ajax({
			url : "${pageContext.request.contextPath}/busanFood.do",
			dataType : "json",
			data : {
				method : "show"
			},

			success : function(data) {
				console.log(typeof data.api);
				
				api = JSON.parse(data.api)  //문자열을 객체로 변환
				
			var foodList=api.getFoodKr.item
				var columnDefs = [
					{headerName : "식당이름",field : "MAIN_TITLE"}, 
					{headerName : "운영 및 시간",field : "USAGE_DAY_WEEK_AND_TIME"}, 
					{headerName : "주소",field : "ADDR1"}, 
					{headerName : "대표메뉴",field : "RPRSNTV_MENU"},
					{headerName : "제목",field : "TITLE",hide:true},
					{headerName : "전화번호",field : "CNTCT_TEL"},
					{headerName : "소개",field : "ITEMCNTNTS" ,hide:true}
					
					
					];
				
				var gridOptions = {
						columnDefs : columnDefs,
						rowData : foodList,
						defaultColDef : {
							editable : false,
							width : 100,
							
						},
						enableColResize : true,
						enableSorting : true,
						enableFilter : true,
						enableRangeSelection : true,
						suppressRowClickSelection : false,
						animateRows : true,
						suppressHorizontalScroll : true,
						localeText : { noRowsToShow : '조회 결과가 없습니다.' },
						onGridReady : function(event) { event.api.sizeColumnsToFit(); },
						onGridSizeChanged : function(event) { event.api.sizeColumnsToFit(); },
						onRowClicked : function(node) {
							console.log(node)
							Swal.fire({   //https://wooncloud.tistory.com/12 
								  icon: 'info', 
								  title: '음식점소개',
								  text: node.data.ITEMCNTNTS,
								  showCancelButton: true,  // 취소버튼을 보아게 하려면 이게 필요
								  confirmButtonColor: '#3085d6', 
								  cancelButtonColor: '#d33', 
								  confirmButtonText: '사진보기', 
								  cancelButtonText : '취소',
								  allowOutsideClick: false  //이렇게하면 밖에 눌러도 안꺼진다 . 버튼클릭후 꺼짐
								  

								}).then(result=>{
								  //사진보기 누르면 true / 취소는 false
									if(result.value){  //https://rocabilly.tistory.com/84
										option="width=417px; height=320px; left=150px;  titlebar=no; toolbar=no,status=no,menubar=no,resizable=no, location=no";  // 눌렀을때 생기는 새창의 구성
										
									window.open(node.data.MAIN_IMG_THUMB,"img",option); //새창 띄워서 보여주기
										
									}
								})
					         
					      },
						

					};
				//gridOptions.rowStyle = {textAlign: "center"};
				var eGridDiv = document.querySelector('#FoodGrid');
				new agGrid.Grid(eGridDiv, gridOptions);
				gridOptions.api.sizeColumnsToFit();
			}

		});
	});
</script>
</head>
<body>
<br>
<br>
<br>
<br>
<section id="tabs" style="width:1480px; height:800px;" class="wow fadeInDown">
		<h6 class="section-title h3">부산맛집정보</h6>
		<div class="container">
		<hr style="background-color:white; height: 1px;">
		</div>
		<div id="FoodGrid" style="height: 600px; width: 1440px" class="ag-theme-balham"></div>
</section>
</body>
</html>
	