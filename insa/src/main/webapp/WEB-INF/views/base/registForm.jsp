<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>

<script src="https://code.jquery.com/jquery-3.5.0.js"></script>
<script src="http://malsup.github.com/jquery.form.js"></script> 
<script>
   $(document).ready(function() {
      
      var todayTime = new Date();         
      var rrrr = todayTime.getFullYear();
      var mm = todayTime.getMonth()+1;
      var dd = todayTime.getDate();
      var today = rrrr+"-"+addZeros(mm,2)+"-"+addZeros(dd,2);
      console.log(today);
      $("#date").val(today);
      
      $("#registBoard").button().click(function(){
         $("#regist_board").submit();
         location.href='${pageContext.request.contextPath}/base/listBoard1.html';
      });
      
      /* $("#regist_board").ajaxForm({
         dataType: "json",
         success: function(data){ 
            alert(data.errorMsg);
            location.href = "${pageContext.request.contextPath}/base/listBoard1.html";
         }
      });    */
   });

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
</script>
</head>
<body>
   <h3><small>&nbsp;</small></h3>
   <h3><small>&nbsp;</small></h3>
   <center>
      <form id="regist_board" target="iframe" action="${pageContext.request.contextPath}/base/registBoard.do" method="post" enctype="multipart/form-data">
         글등록
         <iframe src="#" name="iframe"  style="width:1px; height:1px; border:0; visibility:hidden;"></iframe> <!--  화면전환막는거  -->

         <fieldset>
         <table class="c1">            
            <tr>   
               <td>이름
               <input type="text" name="name" value="${sessionScope.id}">
            </tr>
            <tr>   
               <td>제목
               <input type="text" name="title" maxlength="20">
               </td>
            </tr>
            <tr>   
               <td>작성일자
               <input type="text" name="reg_date" style="width:137px;" id="date" value="${today}">
               </td>
            </tr>
            <tr>
               <td colspan="4">
                  <textarea cols="25" rows="4" name="content"></textarea>
               </td>
            </tr>   
            <tr>
               <td colspan="4">
                  <input type="file" name="f1">
               </td>
            </tr>
            <tr>
               <td colspan="4">
                  <input type="file" name="f2">
               </td>
            </tr>
            <tr>
               <td colspan="4">
                  <input type="file" name="f3">
               </td>
            </tr>
            <tr>   
               <td colspan="4">
                  <input type="button" value="등록" id="registBoard" />
                  <input type="button" value="취소" onclick="location.href='${pageContext.request.contextPath}/base/listBoard1.html'" />
                  
               </td>
            </tr>
         </table>
         <input type="hidden" name="board_seq" value="${param.board_seq}">
         </fieldset>
      </form>
   </center>
</body>
</html>