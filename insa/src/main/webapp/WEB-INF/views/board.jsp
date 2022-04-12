<%@ page language="java" contentType="text/html; charset=EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>JSP Board</title>

<!-- 게시글 입력창 디자인 설정 -->


<script>
var boardList= [];
$(document).ready(function(){

	
$("#Registration").click(RegistrationList)
console.log($("#Registration").click(RegistrationList));

});

	function RegistrationList(){
 		var boardList= {
  		 "Writer" : $("#nameBoard").val(),
  		 "Subject" : $("#titleBoard").val(),
  		 "content" : $("#contentBoard").val(),
  		 "passwardBoard" : $("#passwardBoard").val(),
  		 "filedBoard" : $("#filedBoard").val()		 
 		};
 		
 		var sendData = JSON.stringify(boardList);
		
 		$.ajax({
			url : "${pageContext.request.contextPath}/base/boardList.do",
			data : {
				"method" : "boardRequest",
				"sendData" : sendData
			},
			dataType : "json",
			success : function(data) {
				if (data.errorCode < 0) {
					alert("등록을 실패했습니다");
				} else {
					console.log("여기???")
					
					alert("등록되었습니다");
				}
				location.reload();
			}
				});
  	
			}


</script>
</head>
<body >
<center>
<h2> 게시글 쓰기 </h2> 
 
<form action="BoardWriteProc.jsp" method="post">
<table width = "600" border="1" bordercolor = "gray" bgcolor = "skyblue" class="table"> <!-- bordercolor는 선색깔 지정 -->
    <tr height = "40">
        <td align = "center" width = "150"> 작성자 </td> <!-- ref는 데이터베이스에서 처리하기 때문에 따로 받지 않는다. -->
        <td width = "450"> <input type = "text" name = "writer" size = "60" id="nameBoard"></td> 
    </tr>
    
    <tr height = "40">
        <td align = "center" width = "150"> 제목 </td>
        <td width = "450"> <input type = "text" name = "subject" size = "60" id="titleBoard"></td>
    </tr>
    <tr height = "40">
        <td align = "center" width = "150"> 비밀번호 </td>
        <td width = "450"> <input type = "password" name = "password" size = "61" id="passwardBoard"></td>
    </tr>
    
    <tr height = "40">
        <td align = "center" width = "150"> 글내용 </td>
        <td width = "450"><textarea rows = "10" cols = "50" name = "content" id="ontentBoard"></textarea></td>
    </tr>
    <tr height = "40">
       <td align = "center" width = "150">파일 찾기</td>
       <td width = "450"><input type="file" name="filename" size="50" maxlength="50" id="filedBoard"></td>
     </tr>
    
    <tr height = "40">
        <td align = "center" colspan = "2">
            <input type = "button" value = "글쓰기" id="Registration"> &nbsp;&nbsp;
            <input type = "reset" value = "다시작성"> &nbsp;&nbsp;
            <button onclick = "location.href='BoardList.jsp'">전체 게시글 보기</button> <!-- 클릭하면 BoardList.jsp페이지로 넘어가는 버튼-->
        </td>
    </tr>
</table>
</form>
</center>
</body>
</html>
