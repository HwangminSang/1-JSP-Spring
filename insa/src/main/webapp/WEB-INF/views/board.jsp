<%@ page language="java" contentType="text/html; charset=EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>JSP Board</title>

<!-- �Խñ� �Է�â ������ ���� -->


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
					alert("����� �����߽��ϴ�");
				} else {
					console.log("����???")
					
					alert("��ϵǾ����ϴ�");
				}
				location.reload();
			}
				});
  	
			}


</script>
</head>
<body >
<center>
<h2> �Խñ� ���� </h2> 
 
<form action="BoardWriteProc.jsp" method="post">
<table width = "600" border="1" bordercolor = "gray" bgcolor = "skyblue" class="table"> <!-- bordercolor�� ������ ���� -->
    <tr height = "40">
        <td align = "center" width = "150"> �ۼ��� </td> <!-- ref�� �����ͺ��̽����� ó���ϱ� ������ ���� ���� �ʴ´�. -->
        <td width = "450"> <input type = "text" name = "writer" size = "60" id="nameBoard"></td> 
    </tr>
    
    <tr height = "40">
        <td align = "center" width = "150"> ���� </td>
        <td width = "450"> <input type = "text" name = "subject" size = "60" id="titleBoard"></td>
    </tr>
    <tr height = "40">
        <td align = "center" width = "150"> ��й�ȣ </td>
        <td width = "450"> <input type = "password" name = "password" size = "61" id="passwardBoard"></td>
    </tr>
    
    <tr height = "40">
        <td align = "center" width = "150"> �۳��� </td>
        <td width = "450"><textarea rows = "10" cols = "50" name = "content" id="ontentBoard"></textarea></td>
    </tr>
    <tr height = "40">
       <td align = "center" width = "150">���� ã��</td>
       <td width = "450"><input type="file" name="filename" size="50" maxlength="50" id="filedBoard"></td>
     </tr>
    
    <tr height = "40">
        <td align = "center" colspan = "2">
            <input type = "button" value = "�۾���" id="Registration"> &nbsp;&nbsp;
            <input type = "reset" value = "�ٽ��ۼ�"> &nbsp;&nbsp;
            <button onclick = "location.href='BoardList.jsp'">��ü �Խñ� ����</button> <!-- Ŭ���ϸ� BoardList.jsp�������� �Ѿ�� ��ư-->
        </td>
    </tr>
</table>
</form>
</center>
</body>
</html>
