<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page import="kr.co.yooooon.base.to.BoardTO" %>
<%@ page import="java.util.ArrayList" %>
<c:set var="list" value="${requestScope.boardlist}" />
<html>
<head>
<style type="text/css">
	td { font-size:0.8em; }

	.card1 {
  		background-color: #FFFFFF !important;
  		border-radius: 10px;
	}
	th{
	 	font-weight: bold;
	}
	hr {
  		height: 20px;
 		background-color: #5D5D5D;
	}
	h3  {
    	font-weight: bold;
  	}
</style>

<!-- Bootstrap CSS -->
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/css/bootstrap.min.css" integrity="sha384-GJzZqFGwb1QTTN6wy59ffF1BuGJpLSa9DkKMp0DgiMDm4iYMj70gZWKYbI706tWS" crossorigin="anonymous">

<script type="text/javascript">
var boardList=[];
$(document).ready(function () {
	findBoardList();
});


function findBoardList(){
	var html="";
	$.ajax({
   		url:"${pageContext.request.contextPath}/base/listBoard.do",
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
			for (var index in data.boardlist){
				var str="";
				html += "<tr>";
				html += "<td>"+data.boardlist[index].board_seq+"</td>";
				if(data.boardlist[index].reply_level >1){					
					for(var i=1;i<data.boardlist[index].reply_level;i++){
						str+="-";
					}
					str=str+"&gt";
				}
				html += "<td>"+"<a href='${pageContext.request.contextPath}/base/detailBoard.do?board_seq="+data.boardlist[index].board_seq+"'>"+str+data.boardlist[index].title+"</a>"+"</td>";
				
				html += "<td>"+data.boardlist[index].name+"</td>";
				html += "<td>"+data.boardlist[index].reg_date+"</td>";
				html += "<td>"+data.boardlist[index].hit+"</td>";
				html += "</tr>";
			}
			$("#tbody").empty();
			$("#tbody").append(html);
   		}
  	});
}
</script>


</head>
<body>

<h3><small>&nbsp;</small></h3>
<div class="container">
	<div class="card1">
		<div class="card-body">
			<h3 class="display-10 text-dark font-weight-bold">게시판목록[${sessionScope.id}]</h3>
			<h3><small>&nbsp;</small></h3>
			<div class="table-responsive">
				<table class="table text-center table-striped table-bordered table-sm" id="dtBasicExample">
					<thead>
					<tr>
						<th class="th-sm font-weight-bold text-dark" width="15%">번호</th>
						<th class="th-sm font-weight-bold text-dark" width="40%">제목</th>
						<th class="th-sm font-weight-bold text-dark" width="15%">작성자</th>
						<th class="th-sm font-weight-bold text-dark" width="15%">작성일자</th>			
						<th class="th-sm font-weight-bold text-dark" width="15%">조회수</th>					
					</tr>
					</thead>
					<tbody id="tbody">
					<c:forEach items="${boardList}" var="board">
						<tr style="height: 24px;">
							<td>${board.board_seq}</td>
							<td>
								<c:if test="${board.reply_level>1}">
									<c:forEach begin="1" end="${board.reply_level-1}">
										-
									</c:forEach>
									&gt;
								</c:if>
								<a href="${pageContext.request.contextPath}/base/detailBoard.do?board_seq=${board.board_seq}">${board.title}</a>
							</td>
							<td>${board.name}</td>
							<td>${board.reg_date}</td>
							<td>${board.hit}</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
			<hr/>
			<div class="col text-right">
				<form action="${pageContext.request.contextPath}/base/registForm.html">
					<input type="hidden" name="board_seq" value="0">
					<input class="btn btn-outline dark-text font-weight-bold text-dark" type="submit" value="글쓰기">
					
				</form>
			</div>
		</div>
	</div>
</div>
</body>
</html>