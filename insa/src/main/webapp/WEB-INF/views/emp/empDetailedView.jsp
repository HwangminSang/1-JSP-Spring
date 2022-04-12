<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<!doctype html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>jQuery UI Tabs - Default functionality</title>

<style>
#divImg { display: inline-block; }
#divEmpinfo { display: inline-block; }
#divEmpDinfo { display: inline-block; }
</style> 
<script>
var empList = [];
var empDList = []; //empDetailList
var selectedEmpBean, updatedEmpBean = {}; // 사원의 상세정보를 저장하는 객체들 

empName = "";
emptyWorkInfoBean = {}; // 그리드에 새 행을 추가하기 위한 비어있는 객체들
emptyCareerInfoBean = {};
emptyEducationInfoBean = {};
emptyLicenseInfoBean = {};
emptyFamilyInfoBean = {};
var lastId = 0; // 마지막으로 선택한 그리드의 행 id (다른 행을 더블클릭 하였을 때, 해당 행을 닫기 상태로 만들기 위해 저장함) 
var workgridOptions={};
var familygridOptions={};
var careergridOptions={};
var educationgridOptions={};
var licensegridOptions={};
var addrowworkData = [];
var addrowcareerData=[];
var addroweducationData=[];
var addrowlicenseInfoData=[];
var addrowfamilyData=[];
var today;

$(document).ready(function() {
   /* 부서명 검색/사원명 검색 탭 이동 */
   $("#tabs").tabs();  //눌렀을때 tab기능 사용하려면 필요
   
   $("#sel_dept").selectmenu(); //부서 검색하면 menu 나옴  현재는 안되는듯
   $("#tabs1").tabs();  //눌렀을때 tab기능 사용하려면 필요
   
   /* 부서명 검색, 이름 검색 이벤트 등록 */
   $("#btn_name_search").click(function() {
      makeEmpList("name", $("#txt_name").val()); //이름
   })
   $("#btn_dept_search").click(function() {
      makeEmpList("dept", $("#sel_dept").val()); //부서명 < sel_dept > 는 sel_dept는 select 태그이고 선택한 opiton의 value값을 가지고온다.
   })

      /* 검색 함수 */
   function makeEmpList(grid, value) {
      
      $.ajax({
         url : "${pageContext.request.contextPath}/emp/empList.do",
         data : {
            "method" : "emplist",
            "value" : value            //전체부서/회계팀/인사팀/전산팀
         },
         dataType : "json",
         success : function(data) {
            if (data.errorCode < 0) { //erroe code는 컨트롤러에서 날려보냄
               var str = "내부 서버 오류가 발생했습니다\n";
               str += "관리자에게 문의하세요\n";
               str += "에러 위치 : " + $(this).attr("id");
               str += "에러 메시지 : " + data.errorMsg;
               alert(str);
               return; //function 빠져나감
            }
            
            empList = data.list; //만들어놓은 빈 배열에 list담음
            
            if (grid == "dept")
               showEmpListDeptGrid();
            else {
               showEmpListNameGrid();
            }
         },
         error : function(a, b, c) {
            alert(b);
         }
      });
   }

   
/* 부서별 사원 정보 그리드에 뿌리는 함수 */
function showEmpListDeptGrid() {
   var columnDefs = [ 
      { headerName : "사원코드", field : "empCode", hide : true },
      { headerName : "사원명", field : "empName", checkboxSelection : true },
      { headerName : "부서", field : "deptName"}, 
      { headerName : "직급", field : "position" }, 
      { headerName : "성별", field : "gender", hide : true }, 
      { headerName : "전화번호", field : "mobileNumber", hide : true }, 
      { headerName : "이메일", field : "email" }, 
      { headerName : "거주지", field : "address", hide : true }, 
      { headerName : "최종학력", field : "lastSchool", hide : true }, 
      { headerName : "사진", field : "imgExtend", hide : true }, 
      { headerName : "생년월일", field : "birthdate", hide : true } 
   ];
   gridOptions = {
      columnDefs : columnDefs,
      rowData : empList,
      onRowClicked : function(node) {
         var empCode = node.data.empCode;
         var profile = node.data.imgExtend;
         document.getElementById('profileImg').setAttribute("src","${pageContext.request.contextPath}/profile/" + empCode + "." + profile);
         $.ajax({
            url : "${pageContext.request.contextPath}/emp/empDetail.do",
            data : {
               "method" : "findAllEmployeeInfo",
               "empCode" : empCode
            },
            dataType : "json",
            success : function(data) {
               if (data.errorCode < 0) {   // 결과를 에러코들 먼저 건들고 진행
                  var str = "내부 서버 오류가 발생했습니다\n";
                  str += "관리자에게 문의하세요\n";
                  str += "에러 위치 : "+ $(this).attr("id");
                  str += "에러 메시지 : " + data.errorMsg;
                  alert(str);
                  return;
               }
               
               empDList = data.empBean;   // --> [{}] 이런형식으로 담김
                  
               //initField(); // 전역변수 초기화
               clearEmpInfo(); // 상세정보, 재직정보 칸 초기화
               setAllEmptyBean(data);
               selectedEmpBean = $.extend(true, {}, data.empBean); // 취소버튼을 위한 임시 저장공간에 딥카피
               updatedEmpBean = $.extend(true, {}, data.empBean); // 변경된 내용이 들어갈 공간에 딥카피
               // 객체를 딥카피 하는 이유는 객체 내에 저장된 주소타입의 변수들이 제대로 복사되지 않기 때문임
   
               /* 회원정보를 불러와야 기타정보들의 객체에 제대로 값이 들어가기 때문에 이곳에서 부름 */
               showDetailInfo();
               showWorkInfoListGrid();
               showCareerInfoListGrid();
               showEducationInfoListGrid();
               showLicenseInfoListGrid();
               showFamilyInfoListGrid();
            }
         });
      }
   }
   $('#deptfindgrid').children().remove();
   var eGridDiv = document.querySelector('#deptfindgrid');
   new agGrid.Grid(eGridDiv, gridOptions);
   gridOptions.api.sizeColumnsToFit();
}

/* 이름으로 검색결과 그리드에 뿌리는 함수 */
function showEmpListNameGrid() {
   var columnDefs = [ 
      { headerName : "사원코드", field : "empCode", hide : true }, 
      { headerName : "사원명", field : "empName"  ,checkboxSelection : true}, 
      { headerName : "부서", field : "deptName" }, 
      { headerName : "직급", field : "position" }, 
      { headerName : "성별", field : "gender", hide : true }, 
      { headerName : "전화번호", field : "mobileNumber", hide : true }, 
      { headerName : "이메일", field : "email" }, 
      { headerName : "거주지", field : "address", hide : true }, 
      { headerName : "최종학력", field : "lastSchool", hide : true }, 
      { headerName : "사진", field : "imgExtend", hide : true }, 
      { headerName : "생년월일", field : "birthdate", hide : true } ,
      
   ];
   gridOptions = {
      columnDefs : columnDefs,
      rowData : empList,
      onCellClicked : function(node) {
         var empCode = node.data.empCode;
         var profile = node.data.imgExtend;
         document.getElementById('profileImg').setAttribute("src", "${pageContext.request.contextPath}/profile/" + empCode + "." + profile);
         $.ajax({
            url : "${pageContext.request.contextPath}/emp/empDetail.do",
            data : {
               "method" : "findAllEmployeeInfo",
               "empCode" : empCode
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
               empDList = data.empBean;
               //initField(); // 전역변수 초기화
               clearEmpInfo(); // 상세정보, 재직정보 칸 초기화
               setAllEmptyBean(data);
               selectedEmpBean = $.extend(true, {}, data.empBean); // 취소버튼을 위한 임시 저장공간에 딥카피
               updatedEmpBean = $.extend(true, {}, data.empBean); // 변경된 내용이 들어갈 공간에 딥카피
               // 객체를 딥카피 하는 이유는 객체 내에 저장된 주소타입의 변수들이 제대로 복사되지 않기 때문임
               /* 회원정보를 불러와야 기타정보들의 객체에 제대로 값이 들어가기 때문에 이곳에서 부름 */
               showDetailInfo();
               showWorkInfoListGrid();
               showCareerInfoListGrid();
               showEducationInfoListGrid();
               showLicenseInfoListGrid();
               showFamilyInfoListGrid(); 
            }
         });
      }
   }
   $('#namefindgrid').children().remove();
   var eGridDiv = document.querySelector('#namefindgrid');
   new agGrid.Grid(eGridDiv, gridOptions);
   gridOptions.api.sizeColumnsToFit();
}

$("#address").postcodifyPopUp();

/* 사진찾기*/
$("#findPhoto").button().click(function() {
   $("#emp_img_file").click(); //사진찾기 버튼을 누르면 숨겨진 file 태그를 클릭
});

// 사진 등록 form의 ajax 부분
$("#emp_img_form").ajaxForm({ //사진 업로드
   dataType : "json",
   success : function(responseText,statusText, xhr, $form) {
      alert(responseText.errorMsg);
      location.reload();
   }
});

/* 코드 선택창 띄우기 */
function getCode(code, inputText, inputCode) {
   option = "width=220; height=200px; left=300px; top=300px; titlebar=no; toolbar=no,status=no,menubar=no,resizable=no, location=no";
   window.open(
         "${pageContext.request.contextPath}/base/codeWindow.html?code="+ code + "&inputText=" + inputText+ "&inputCode=" + inputCode,"newwins", option);
}                  

/* 전역변수 초기화 함수 */
function initField() {
   selectedEmpBean, updatedEmpBean = {};
   emptyFamilyInfoBean = {};
   emptyCareerInfoBean = {};
   emptyWorkInfoBean = {};
   emptyEducationInfoBean = {};
   emptyLicenseInfoBean = {};
   lastId = 0;
}                  

/* 현재 표시된 모든 정보를 비우는 함수 */
function clearEmpInfo() {
   // 찾았던 사진을 기본 사진으로 되돌린다
   $("#profileImg").attr("src","${pageContext.request.contextPath }/profile/profile.png");  // 기본사진으로 변경
   $("input:text").each(function() {
      $(this).val("") //모든 input text타입의 value값을 비운다
   })
}                  


/* 새로운 정보들을 추가하기 위한 빈 객체 세팅 */
function setAllEmptyBean(data) {
   var todayTime = new Date();         
   var rrrr = todayTime.getFullYear();
   var mm = todayTime.getMonth()+1;
   var dd = todayTime.getDate();
   today = rrrr+"-"+addZeros(mm,2)+"-"+addZeros(dd,2);
   console.log(today);
   realTime=parseInt(today.replace(/\-/g,''))-20000000;  // >> -를 없애준다 . /  /  --> 정규표현식. 정규식 리터럴(슬래쉬"/"로 감싸는 패턴)을 사용하는 방법은 다음과 같습니다.  ,   \-  --> 을 사용하면   -만찾아낸다.   ,  g=전역 검색
   emptyFamilyInfoListBean = data.emptyFamilyInfoBean;  // 빈객체를 넣어줌 
   emptyCareerInfoListBean = data.emptyCareerInfoBean;
   emptyWorkInfoListBean = data.emptyWorkInfoBean;
   emptyEducationInfoListBean = data.emptyEducationInfoBean;
   emptyLicenseInfoListBean = data.emptyLicenseInfoBean;

   emptyFamilyInfoListBean.status = "insert";
   emptyFamilyInfoListBean.empCode = data.empBean.empCode;
   emptyCareerInfoListBean.status = "insert";
   emptyCareerInfoListBean.empCode = data.empBean.empCode;
   emptyWorkInfoListBean.status = "insert";
   emptyWorkInfoListBean.empCode = data.empBean.empCode;
   emptyWorkInfoListBean.deptName = data.empBean.deptName;
   emptyWorkInfoListBean.position = data.empBean.position;
   emptyWorkInfoListBean.empName = data.empBean.empName;
   emptyWorkInfoListBean.workInfoDays=realTime;
   emptyEducationInfoListBean.status = "insert";
   emptyEducationInfoListBean.empCode = data.empBean.empCode;
   emptyLicenseInfoListBean.status = "insert";
   emptyLicenseInfoListBean.empCode = data.empBean.empCode;
}                  

/* DatePicker 달력함수 */
function getDatePicker() {
   // function to act as a class
    function Datepicker() {}

   // gets called once before the renderer is used
   Datepicker.prototype.init = function(params) {
      // create the cell
      this.eInput = document.createElement('input');
      this.eInput.value = params.value;

      // https://jqueryui.com/datepicker/
      $(this.eInput).datepicker({
         dateFormat : 'yy/mm/dd'
      });
   };

   // gets called once when grid ready to insert the element
   Datepicker.prototype.getGui = function() {
      return this.eInput;
   };

   // focus and select can be done after the gui is attached
   Datepicker.prototype.afterGuiAttached = function() {
      this.eInput.focus();
      this.eInput.select();
   };

   // returns the new value after editing
   Datepicker.prototype.getValue = function() {
      return this.eInput.value;
   };

   // any cleanup we need to be done here
   Datepicker.prototype.destroy = function() {
      // but this example is simple, no cleanup, we could
      // even leave this method out as it's optional
   };

   // if true, then this editor will appear in a popup
   Datepicker.prototype.isPopup = function() {
      // and we could leave this method out also, false is the default
      return false;
   };
   return Datepicker;
}                  

function getDatePickerNumber($CellId) {
   // function to act as a class
    function Datepicker() {
                     }

   // gets called once before the renderer is used
   Datepicker.prototype.init = function(params) {
      // create the cell
      this.eInput = document.createElement('input');
      this.eInput.value = params.value;

      // https://jqueryui.com/datepicker/
      $(this.eInput).datepicker({
         dateFormat : 'yy/mm/dd'
      });
   };

   // gets called once when grid ready to insert the element
   Datepicker.prototype.getGui = function() {
      return this.eInput;
   };

   // focus and select can be done after the gui is attached
   Datepicker.prototype.afterGuiAttached = function() {
      this.eInput.focus();
      this.eInput.select();
   };

   // returns the new value after editing
   Datepicker.prototype.getValue = function() {
      return this.eInput.value;
   };

   // any cleanup we need to be done here
   Datepicker.prototype.destroy = function() {
      // but this example is simple, no cleanup, we could
      // even leave this method out as it's optional
   };

   // if true, then this editor will appear in a popup
   Datepicker.prototype.isPopup = function() {
      // and we could leave this method out also, false is the default
      return false;
   };
   return Datepicker;
}                  

                  
////////////////////////////////////////////////////그리드이벤트////////////////////////////////////////////////////////     
/* 그리드에 행 추가하는 함수 */
function addListGridRow() {
   var key = $(this).attr("id").split("_")[1]; //  버튼의 아이디를 가져와서 _ 배열로 나눠서 첫번째를 뽑아낸다 add_work_btn -> work 
   
   var emptyBean = {
      "family" : emptyFamilyInfoListBean,
      "career" : emptyCareerInfoListBean,
      "work" : emptyWorkInfoListBean,
      "education" : emptyEducationInfoListBean,
      "license" : emptyLicenseInfoListBean
   }
   /* 
    * addRowData가 아닌 addRow로 cell을 추가하면 굉장히 편하지만 이 방법을 사용하는 이유는
    * addRow로 cell을 추가하면 editable에 따라 textbox가 자동으로 생성된 상태로 cell이 추가됨
    * 이걸 일일이 처리하는것보다 빈 객체를 가져와 처리하는게 편하기 때문에 addRowData를 사용
    */
   
   

   var newData=Object.assign({},emptyBean[key]); // key가 work 일때 emptyWorkInfoListBean 반환   emptyBean.key 하면 오류난다.
   console.log(newData);   //참조값이 다르게하여 객체가 몇개가 추가되든 새로운 참조값이 다른 객체를 만들어낸다.
   
   if(key=="work"){
      workgridOptions.api.updateRowData({add : [newData]});                     
   }else if(key=="family"){
      familygridOptions.api.updateRowData({add : [newData]});
   }else if(key=="career"){
      careergridOptions.api.updateRowData({add : [newData]});
   }else if(key=="education"){
      educationgridOptions.api.updateRowData({add : [newData]});
   }else if(key=="license"){
      licensegridOptions.api.updateRowData({add : [newData]});
   }
}                  
                  

/* 그리드에 행 삭제 */            //https://4urdev.tistory.com/50
function delListGridRow() {  //https://mine-it-record.tistory.com/285
   var key = $(this).attr("id").split("_")[1];
    
   if(key=="work"){
	   var rowNode=workgridOptions.api.getSelectedNodes()
	   rowNode[0].setDataValue('status', "delete"); 
	      
	    	
	     
	                     
	   }else if(key=="family"){
		   var rowNode=familygridOptions.api.getSelectedNodes()
		       rowNode[0].setDataValue('status', "delete"); 
		       
		    	   
		      
	   }else if(key=="career"){
		   var rowNode=careergridOptions.api.getSelectedNodes()
		   rowNode[0].setDataValue('status', "delete"); 
		      
		    	  
		     
	   }else if(key=="education"){
		   var rowNode=educationgridOptions.api.getSelectedNodes()
		   rowNode[0].setDataValue('status', "delete"); 
		      
		    	  
		      
	   }else if(key=="license"){
		   var rowNode=licensegridOptions.api.getSelectedNodes()
		   rowNode[0].setDataValue('status', "delete"); 
		      
		    	  
		       }  
	 
}    
   
   

              


/* 상세정보 탭의 저장 버튼 */
$("#modifyEmp_Btn").click(function() {
   if (updatedEmpBean == null) {  // 각각의 업데이트 빈에 넣어둠 
      alert("저장할 내용이 없습니다");
   } else {
      var flag = confirm("변경한 내용을 서버에 저장하시겠습니까?");
      
      if(flag){ modifyEmp(); }
   }
});                  

                  
/* 저장 */
function modifyEmp() { //수정하다
   saveEmpInfo(); // updatedEmpBean객체에
   console.log(updatedEmpBean)
   var sendData = JSON.stringify(updatedEmpBean);
   console.log(updatedEmpBean)

   $.ajax({
      url : "${pageContext.request.contextPath}/emp/empDetail.do",
      data : {
         "method" : "modifyEmployee",
         "sendData" : sendData
      },
      dataType : "json",
      success : function(data) {
         if (data.errorCode < 0) {  //!
            alert("저장에 실패했습니다"+data.errorMsg);
         
         } else {
            /* 선택한 사진이 있다면 저장 버튼을 눌렀을때에 사진 저장도 같이 되게함 */
            if ($("#emp_img_file").val() != "") {
               $("#emp_img_form").submit();
            }
            alert("저장되었습니다");
         }

     location.reload();
      }
   });
}
                  
/* 변경된 정보들을 저장하는 함수 */  // 현재 재직정보로만 그리드가 돌아가고 있어서 안됨
function saveEmpInfo() {
  
   workgridOptions.api.forEachNode(function(node) {
	
	   addrowworkData.push(node.data);
   });
   
   careergridOptions.api.forEachNode(function(node){
	  
	   addrowcareerData.push(node.data);
   });
   educationgridOptions.api.forEachNode(function(node){
	 
	   addroweducationData.push(node.data);
   });
   licensegridOptions.api.forEachNode(function(node){

	   addrowlicenseInfoData.push(node.data);
	   
   });
   familygridOptions.api.forEachNode(function(node){

	   addrowfamilyData.push(node.data);
	   
   });
   updatedEmpBean.status = "update";
   updatedEmpBean.empCode = $("#empCode").val();
   updatedEmpBean.empName = $("#empName").val();
   updatedEmpBean.birthdate = $("#birthdate").val();
   updatedEmpBean.deptName = $("#deptName").val();
   updatedEmpBean.position = $("#position").val();
   updatedEmpBean.email = $("#email").val();
   updatedEmpBean.gender = $("#gender").val();
   updatedEmpBean.mobileNumber = $("#mobileNumber").val();
   updatedEmpBean.lastSchool = $("#lastSchool").val();
   updatedEmpBean.address = $("#address").val();
   updatedEmpBean.detailAddress = $("#detailAddress").val();
   updatedEmpBean.postNumber = $("#postNumber").val();
   updatedEmpBean.workInfoList=addrowworkData;
   updatedEmpBean.careerInfoList=addrowcareerData;
   updatedEmpBean.educationInfoList=addroweducationData;
   updatedEmpBean.licenseInfoList=addrowlicenseInfoData;
   updatedEmpBean.familyInfoList=addrowfamilyData;
   console.log(updatedEmpBean);
   /* 사진, 생년월일  추가해줘야함*/
}
                  
                  
/* 상세정보 탭의 취소 버튼 */
$("#can_work_btn").click(function() {
   var flag = confirm("취소하시겠습니까?");
   if (flag)
      rollBackEmpInfo();
});
                  
function rollBackEmpInfo() {
   clearEmpInfo(); // 모든 정보를 지운 후

   // 서버에서 불러온 미리 저장한 EmpBean의 정보를 수정하던 정보에 엎어씌움
   updatedEmpBean = $.extend(true, {}, selectedEmpBean);
   // 딥카피하는 이유는 주소타입의 변수가 제대로 카피되지 않기 때문임

   //상제정보와 사진을 다시 띄운다
   showDetailInfo();
   showEmpImg();
}

/* 사진 찾기 버튼 */
$("#findImg_btn").click(function() {
   if (updatedEmpBean == null) {
      alert("저장할 내용이 없습니다");
   } else {
      var flag = confirm("변경한 내용을 서버에 저장하시겠습니까?");
      if (flag)
         modifyEmp();
   }
});
                  
/* 저장된 사진 불러오는 함수 */
function showEmpImg() {
   var path = "${pageContext.request.contextPath }/profile/profile.png";

   if (selectedEmpBean.imgExtend != null) {
      if (selectedEmpBean.detailInfo.imgExtend != null) {
         path = "${pageContext.request.contextPath}/profile/";
         path += selectedEmpBean.empCode;
         path += "." + selectedEmpBean.imgExtend;
      }
   }
   $("#emp_img").attr("src", path);
}
                  
/* 사원 삭제 버튼 눌렀을 때 실행되는 함수 */  // 부서별 사원별 거기에는 gridOptions 
function removeEmpListD() { //수정 
   var selectedRowIds = gridOptions.api.getSelectedRows();  // [{}] 를 리ㅓㄴ
   console.log(selectedRowIds);
   var sendData = JSON.stringify(selectedRowIds);
   $.ajax({
      url : "${pageContext.request.contextPath}/emp/empDetail.do",
      data : {
         "method" : "removeEmployeeList",
         "sendData" : sendData
      },
      dataType : "json",
      success : function(data) {
         if (data.errorCode < 0) {
            alert("정상적으로 삭제되지 않았습니다");
         } else {
            alert("삭제되었습니다");
         }
         location.reload();
      }
   });
}
                  

function removeEmpListN() { //수정 
	var selectedRowIds = gridOptions.api.getSelectedRows();  // [{}] 를 리ㅓㄴ
	   console.log(selectedRowIds);
	   if (selectedRowIds.length == 0) {
		      alert("삭제할 사원을 선택해 주세요");
		      return;
		   }
	   var sendData = JSON.stringify(selectedRowIds);
	   $.ajax({
	      url : "${pageContext.request.contextPath}/emp/empDetail.do",
	      data : {
	         "method" : "removeEmployeeList",
	         "sendData" : sendData
	      },
	      dataType : "json",
	      success : function(data) {
	         if (data.errorCode < 0) {
	            alert("정상적으로 삭제되지 않았습니다");
	         } else {
	            alert("삭제되었습니다");
	         }
	         location.reload();
	      }
	   });
}                  
/*//////////////////////////////////////// 세부상세정보 그리드 띄우기 이건 그리드가 아니잖아 이 미친년아 //////////////////////////////////////////////*/

function showDetailInfo() {
   $("#empCode").val(selectedEmpBean.empCode);
   $("#empName").val(selectedEmpBean.empName);
   $("#deptName").val(selectedEmpBean.deptName);
   $("#position").val(selectedEmpBean.position);
   $("#id").val(selectedEmpBean.id);
   $("#pw").val(selectedEmpBean.pw);
   $("#birthdate").val(selectedEmpBean.birthdate);
   $("#gender").val(selectedEmpBean.gender);
   $("#mobileNumber").val(selectedEmpBean.mobileNumber);
   $("#address").val(selectedEmpBean.address);
   $("#detailAddress").val(selectedEmpBean.detailAddress);
   $("#postNumber").val(selectedEmpBean.postNumber);
   $("#email").val(selectedEmpBean.email);
   $("#lastSchool").val(selectedEmpBean.lastSchool);
   $("#profileImg").attr("src","${pageContext.request.contextPath}/profile/"+ selectedEmpBean.empCode + "."+ selectedEmpBean.imgExtend);
}
 
function showWorkInfoListGrid() {
   var columnDefs = [ 
      {headerName : "사원코드", field : "empCode",},
      {headerName : "적용일", field : "workInfoDays", editable : false
        
      },
      {headerName : "입사일", field : "hiredate", editable : true, 
         cellRenderer: function(params) {
             return  '<i class="fa fa-calendar-o" aria-hidden="true"></i>'+params.value;
           },
           //cellEditor: DateEditor        
      },
      {headerName : "퇴사일", field : "retireDate", editable : true, 
         cellRenderer: function(params) {
             return  '<i class="fa fa-calendar-o" aria-hidden="true"></i>'+params.value;
           },
           //cellEditor: DateEditor
      },
      {headerName : "직종", field : "occupation", editable : true, cellEditor: 'agSelectCellEditor', cellEditorParams: {values: ['생산직', '사무직']}},
      {headerName : "고용형태", field : "employmentType",editable : true, cellEditor: 'agSelectCellEditor', cellEditorParams: {values: ['정규직', '계약직','일용직','파견직','재택근로자','단시간근로자']} },
      {headerName : "호봉", field : "hobong", editable : true, cellEditor: 'agSelectCellEditor',
         cellEditorParams: {values:
            ['1호봉','2호봉','3호봉','4호봉','5호봉','6호봉','7호봉','8호봉','9호봉','10호봉','11호봉','12호봉','13호봉','14호봉','15호봉','16호봉','17호봉','18호봉','19호봉','20호봉','21호봉','22호봉','23호봉','24호봉','25호봉']
         },
      },
      {headerName : "직급", field : "position"},
      {headerName : "부서", field : "deptName"},
      {headerName : "상태",   field : "status"}
   ];
   
   workgridOptions = {
      columnDefs : columnDefs,
      rowData : updatedEmpBean.workInfoList,
      defaultColDef : {
         editable : false,
         width : 100
      },
      components : {
         datePicker : getDatePicker(),
         datePicker1 : getDatePickerNumber(),

      },
      rowSelection : 'single', /* 'single' or 'multiple',*/
      enableColResize : true,
      enableSorting : true,
      enableFilter : true,
      enableRangeSelection : true,
      suppressRowClickSelection : false,
      animateRows : true,
      suppressHorizontalScroll : true,
      defaultColDef : {resizable : true},
      localeText : {
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
  	onCellEditingStopped : function(event) { // 하나의 cell 이 수정이 끝났을때 status를 update로 바꿈
		if (event.data.status === "normal" || event.data.status == "delete") {
			event.data.status = "update" }
		
		workgridOptions.api.updateRowData({ update : [event.data] });
	},
   };
   $('#workInfoListGrid').children().remove();
   var eGridDiv = document.querySelector('#workInfoListGrid');
   new agGrid.Grid(eGridDiv, workgridOptions);
   //gridOptions.api.setRowData([{empCode:"A490070"}]);
}                  

//가족정보 그리드 호출
function showFamilyInfoListGrid() {
   var columnDefs = [ 
      {headerName : "사원코드",field : "empCode",editable : false},
      {headerName : "일련번호",field : "familyCode", hide : true}, 
      {headerName : "성명",field : "familyName",editable : true},
      {headerName : "관계",field : "relation",editable : true},
      {headerName : "생년월일",field : "birthdate",editable : true, cellEditor : 'datePicker'},
      {headerName : "동거여부",field : "liveTogether" ,editable : true, cellEditor: 'agSelectCellEditor',cellEditorParams: {values: ['Y', 'N']}},  // cellEditor  ,cellEditorParams 두개가 같이 있어야 선택가능!
      {headerName : "상태",field : "status"}
   ];
   familygridOptions = {
      columnDefs : columnDefs,
      rowData : updatedEmpBean.familyInfoList,
      defaultColDef : {
         editable : false,
         width : 100
      },
      components : {
         datePicker : getDatePicker(),
         datePicker1 : getDatePickerNumber()
      },
      rowSelection : 'single', /* 'single' or 'multiple',*/
      enableColResize : true,
      enableSorting : true,
      enableFilter : true,
      enableRangeSelection : true,
      suppressRowClickSelection : false,
      animateRows : true,
      suppressHorizontalScroll : true,
      localeText : {
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
      onCellEditingStopped : function(event) { // 하나의 cell 이 수정이 끝났을때 status를 update로 바꿈
  		if (event.data.status === "normal" || event.data.status == "delete") {
  			
  			event.data.status = "update" 
  			}
  		
  		familygridOptions.api.updateRowData({ update : [event.data] });
  	},
   };
   $('#familyInfoListGrid').children().remove();
   var eGridDiv = document.querySelector('#familyInfoListGrid');
   new agGrid.Grid(eGridDiv, familygridOptions);
}                  

//경력정보 그리드 호출
function showCareerInfoListGrid() {
   var columnDefs = [ 
      {headerName : "사원코드",field : "empCode",editable : false},
      {headerName : "일련번호",field : "careerCode",editable : true , hide : true},
      {headerName : "회사명",field : "companyName",editable : true},
      {headerName : "직종",field : "occupation",editable : true},
      {headerName : "담당업무",field : "assignmentTask",editable : true},
      {headerName : "입사일",field : "exHiredate",editable : true, cellEditor : 'datePicker'},
      {headerName : "퇴사일",field : "exRetirementDate",editable : true, cellEditor : 'datePicker'},
      {headerName : "상태",field : "status"}
   ];
   careergridOptions = {
      columnDefs : columnDefs,
      rowData : updatedEmpBean.careerInfoList,
      defaultColDef : {
         editable : false,
         width : 100
      },
      components : {
         datePicker : getDatePicker(),
         datePicker1 : getDatePickerNumber()
      },
      rowSelection : 'single', /* 'single' or 'multiple',*/
      enableColResize : true,
      enableSorting : true,
      enableFilter : true,
      enableRangeSelection : true,
      suppressRowClickSelection : false,
      animateRows : true,
      suppressHorizontalScroll : true,
      localeText : {
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
  	onCellEditingStopped : function(event) { // 하나의 cell 이 수정이 끝났을때 status를 update로 바꿈
		if (event.data.status === "normal" || event.data.status == "delete") {
			event.data.status = "update" }
		
		careergridOptions.api.updateRowData({ update : [event.data] });
	},
   };
   $('#careerInfoListGrid').children().remove();
   var eGridDiv = document
         .querySelector('#careerInfoListGrid');
   new agGrid.Grid(eGridDiv, careergridOptions);
}                  

//학력정보 그리드 호출
function showEducationInfoListGrid() {
   var columnDefs = [
      {headerName : "사원코드",field : "empCode",editable : false},
      {headerName : "일련번호",field : "educationCode", hide : true },
      {headerName : "학교명",field : "schoolName",editable : true},  
      {headerName : "전공",field : "major",editable : true},
      {headerName : "입학일",field : "entranceDate",editable : true,cellEditor : 'datePicker'}, 
      {headerName : "졸업일",field : "graduateDate",editable : true,cellEditor : 'datePicker'},
      {headerName : "학점",field : "grade" ,editable : true,},
      {headerName : "상태",field : "status"   }
   ];
   educationgridOptions = {
      columnDefs : columnDefs,
      rowData : updatedEmpBean.educationInfoList,
      defaultColDef : {
         editable : false,
         width : 100
      },
      components : {
          datePicker : getDatePicker(),
          datePicker1 : getDatePickerNumber()
       },
      rowSelection : 'single', /* 'single' or 'multiple',*/
      enableColResize : true,
      enableSorting : true,
      enableFilter : true,
      enableRangeSelection : true,
      suppressRowClickSelection : false,
      animateRows : true,
      suppressHorizontalScroll : true,
      localeText : {
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
  	onCellEditingStopped : function(event) { // 하나의 cell 이 수정이 끝났을때 status를 update로 바꿈
		if (event.data.status === "normal" || event.data.status == "delete") {
			event.data.status = "update" }
		
		educationgridOptions.api.updateRowData({ update : [event.data] });
	},
   };
   $('#educationInfoListGrid').children().remove();
   var eGridDiv = document.querySelector('#educationInfoListGrid');
   new agGrid.Grid(eGridDiv, educationgridOptions);
}                  

//자격증정보 그리드 호출
function showLicenseInfoListGrid() {
   var columnDefs = [ 
      {headerName : "사원코드",field : "empCode",editable : false},
      {headerName : "일련번호",field : "licenseCode", hide : true},
      {headerName : "자격증명",field : "licenseName",editable : true},
      {headerName : "취득일",field : "getDate",editable : true,cellEditor : 'datePicker'}, 
      {headerName : "만료일",field : "expireDate",editable : true,cellEditor : 'datePicker'},
      {headerName : "급수",field : "licenseLevel" ,editable : true},
      {headerName : "발행기관",field : "licenseCenter" ,editable : true},
      {headerName : "발급번호",field : "issueNumber" ,editable : true},
      {headerName : "상태",field : "status"},
   ];
   licensegridOptions = { 
      columnDefs : columnDefs,
      rowData : updatedEmpBean.licenseInfoList,
      defaultColDef : {
         editable : false,
         width : 100
      },
      components : {
         datePicker : getDatePicker(),
         datePicker1 : getDatePickerNumber()
      },
      rowSelection : 'single', /* 'single' or 'multiple',*/
      enableColResize : true,
      enableSorting : true,
      enableFilter : true,
      enableRangeSelection : true,
      suppressRowClickSelection : false,
      animateRows : true,
      suppressHorizontalScroll : true,
      localeText : {
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
  	onCellEditingStopped : function(event) { // 하나의 cell 이 수정이 끝났을때 status를 update로 바꿈
		if (event.data.status === "normal" || event.data.status == "delete") {
			event.data.status = "update" }
		
		licensegridOptions.api.updateRowData({ update : [event.data] });
	},
   };
   $('#licenseInfoListGrid').children().remove();
   var eGridDiv = document.querySelector('#licenseInfoListGrid');
   new agGrid.Grid(eGridDiv, licensegridOptions);
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
$("#add_work_btn").click(addListGridRow);
$("#add_career_btn").click(addListGridRow);
$("#add_education_btn").click(addListGridRow);
$("#add_license_btn").click(addListGridRow);
$("#add_family_btn").click(addListGridRow);

$("#del_work_btn").click(delListGridRow);
$("#del_career_btn").click(delListGridRow);
$("#del_education_btn").click(delListGridRow);
$("#del_license_btn").click(delListGridRow);
$("#del_family_btn").click(delListGridRow);

/* $("#deptName").click(function(){
  getCode("CO-07","deptName");
});
$("#position").click(function(){
  getCode("CO-04","position");
}); */
$("#gender").click(function() {
   getCode("CO-01", "gender");
});
$("#lastSchool").click(function() {
   getCode("CO-02", "lastSchool");
});

$("#birthdate").click(getDatePicker($("#birthdate")));

$("#btn_dept_del").click(function() { // 부서검색제버튼
   var flag = confirm("선택한 사원을 정말 삭제하시겠습니까?");
   if (flag)
      removeEmpListD();
});

$("#btn_name_del").click(function() { // 이름삭제버튼
   var flag = confirm("선택한 사원을 정말 삭제하시겠습니까?");
   if (flag)
      removeEmpListN();
});

showEmpListDeptGrid();
showEmpListNameGrid();
showWorkInfoListGrid();
showCareerInfoListGrid();
showEducationInfoListGrid();
showLicenseInfoListGrid();
showFamilyInfoListGrid();

});
               

   /* 사진찾기 버튼 눌렀을 때 실행되는 함수 */
   function readURL(input) {
	  
      $("#emp_img_empCode").val(updatedEmpBean.empCode);
      if (input.files && input.files[0]) {  //즉 찾아온 파일이 있을때 
         var reader = new FileReader();
         reader.onload = function(e) {
            // 이미지 Tag의 SRC속성에 읽어들인 File내용을 지정 (아래 코드에서 읽어들인 dataURL형식)
            $("#profileImg").attr("src", e.target.result);
         }
         reader.readAsDataURL(input.files[0]); //File내용을 읽어 dataURL형식의 문자열로 저장
      }
   }
   /* 달력 띄우기 */
   $(function(){
      $("#birthdate").datepicker({
         changeMonth : true,
         changeYear : true,
         showOn:"button",
         buttonImage:"${pageContext.request.contextPath}/image/cal.png",
         buttonImageOnly:true,
         dateFormat : "yy/mm/dd",
         yearRange: "1900:2030",
      })
   });


</script>


</head>
<body>
   <!-- 왼쪽창 -->
   <div id="tabs" style="width: 600px; height: 400px; position: absolute; top: 200px; left: 300px;">  <!-- 리먼트를 기본적으로 브라우저 화면(viewport) 상에서 어디든지 원하는 위치에 자유롭게 배치시킬 수 있으며, 심지어 부모 엘리먼트 위에 겹쳐서 배치할 수도 있습니다. -->
      <ul>
         <li><a href="#tabs-11">부서명 검색</a></li>
         <li><a href="#tabs-1">사원명 검색</a></li>
      </ul>
      <!-- 부서명검색 탭 -->
      <div id="tabs-11">
         <select id="sel_dept">
            <option value="전체부서">전체부서</option>
            <option value="회계팀">회계팀</option>
            <option value="인사팀">인사팀</option>
            <option value="전산팀">전산팀</option>
            <option value="보안팀">보안팀</option>
         </select>
         <button id="btn_dept_search"
            class="ui-button ui-widget ui-corner-all">검색</button>
         <button id="btn_dept_del" class="ui-button ui-widget ui-corner-all">삭제</button>
         <br /> <br />
         <div id="deptfindgrid" style="height: 250px;" class="ag-theme-balham"></div>

      </div>
      
      <!-- 사원명 검색탭 -->
      <div id="tabs-1">
         <input type="text" id="txt_name"
            class="ui-button ui-widget ui-corner-all">
         <button id="btn_name_search"
            class="ui-button ui-widget ui-corner-all">검색</button>
         <button id="btn_name_del" class="ui-button ui-widget ui-corner-all">삭제</button>
         <br /> <br />
         <div id="namefindgrid" style="height: 250px;" class="ag-theme-balham"></div>
      </div>

   </div>

   <!-- 오른쪽 창 -->
   <div id="tabs1"
      style="width: 850px; height: 750px; position: absolute; top: 200px; left: 930px;">
      <ul>
         <li><a href="#tabs-0">기본 정보</a></li>
         <li><a href="#tabs-2">재직 정보</a></li>
         <li><a href="#tabs-3">경력 정보</a></li>
         <li><a href="#tabs-4">학력 정보</a></li>
         <li><a href="#tabs-5">자격증 정보</a></li>
         <li><a href="#tabs-6">가족 정보</a></li>
      </ul>
      <!-- 기본정보 -->
      <div id="tabs-0" align="left">
         <!-- 사진박스 -->
         <div id="divImg">
            <img id="profileImg" src="${pageContext.request.contextPath}/profile/profile.png" width="180px" height="200px"><br>
            <form id="emp_img_form" action="${pageContext.request.contextPath }/base/empImg.do" enctype="multipart/form-data" method="post">
               <input type="hidden" name="empCode" id="emp_img_empCode">
               <input type="file" name="empImgFile" style="display: none;" id="emp_img_file" onChange="readURL(this)">  <!--  숨겨둠   -->
               <button type="button" style="width: 150px" class="ui-button ui-widget ui-corner-all" id="findPhoto">사진찾기</button>
               <br />
            </form>
            <br />
         </div>
         <!-- 상세정보박스1 -->
         <div id="divEmpInfo"
            style="display: inline-block; position: absolute; margin-left: 50px;">
            <br />
            <table>
               <tr>
                  <td><font>사원코드</font></td>
                  <td><input class="ui-button ui-widget ui-corner-all"
                     id="empCode" readonly></td>
               </tr>
               <tr>
                  <td><font>이름</font></td>
                  <td><input class="ui-button ui-widget ui-corner-all"
                     id="empName" readonly></td>
               </tr>
               <tr>
                  <td><font>부서</font></td>
                  <td><input class="ui-button ui-widget ui-corner-all"
                     id="deptName" readonly></td>
               </tr>
               <tr>
                  <td><font>직급</font></td>
                  <td><input class="ui-button ui-widget ui-corner-all"
                     id="position" readonly></td>
               </tr>
               <tr>
                  <td><font>아이디</font></td>
                  <td><input class="ui-button ui-widget ui-corner-all"
                     id="id" readonly></td>
               </tr>
               <tr>
                  <td><font>비밀번호</font></td>
                  <td><input class="ui-button ui-widget ui-corner-all"
                     id="pw" readonly></td>
               </tr>
            </table>
            <!-- <font style="font-size: 10px">전화번호 </font><br/>-->
            <br />
            <!-- IMG_EXTEND -->
         </div>
         <hr>
         <!-- 선 -->
         <!-- 상제정보박스2 -->
         <div id="divEmpDinfo"
            style="display: block; position: relative; width: 700px; height: 300px; margin-left: 230px; margin-top: 0px">
            <table>
               <tr>
                  <td><font>e-mail</font></td>
                  <td><input class="ui-button ui-widget ui-corner-all"
                     id="email"></td>
               </tr>
               <tr>
                  <td><font>휴대전화 </font></td>
                  <td><input class="ui-button ui-widget ui-corner-all"
                     id="mobileNumber"></td>
               </tr>
               <tr>
                  <td><font>생년월일</font></td>
                  <td><input class="ui-button ui-widget ui-corner-all"
                     id="birthdate" readonly></td>
               </tr>
               <tr>
                  <td><font>성별</font></td>
                  <td><input class="ui-button ui-widget ui-corner-all"
                     id="gender" readonly></td>
               </tr>
               <tr>
                  <td><font>최종학력</font></td>
                  <td><input class="ui-button ui-widget ui-corner-all"
                     id="lastSchool" readonly></td>
               </tr>
               <tr>
                  <td><font>주소</font></td>
                  <td><input
                     class="postcodify_address ui-button ui-widget ui-corner-all"
                     id="address" readonly></td>
               </tr>
               <tr>
                  <td><font>상세주소</font></td>
                  <td><input
                     class="postcodify_details ui-button ui-widget ui-corner-all"
                     id="detailAddress"></td>
               </tr>
               <tr>
                  <td><font>우편번호</font></td>
                  <td><input
                     class="postcodify_postcode5 ui-button ui-widget ui-corner-all"
                     id="postNumber" readonly></td>
               </tr>
            </table>
            <input type="hidden" id="imgExtend">
         </div>
      </div>
      <!-- 재직정보 -->
      <div class="grid-wrapper" id="tabs-2">
         <input type="button" id="add_work_btn"
            class="ui-button ui-widget ui-corner-all" value="추가"> <input
            type="button" id="del_work_btn"
            class="ui-button ui-widget ui-corner-all" value="삭제"><br />
         <br />
         <div id="workInfoListGrid" style="height: 500px; width: 820px"
            class="ag-grid-div ag-theme-balham ag-basic" :defaultColDef="defaultColDef"></div>
      </div>
      <!-- 경력정보 -->
      <div id="tabs-3">
         <input type="button" id="add_career_btn"
            class="ui-button ui-widget ui-corner-all" value="추가"> <input
            type="button" id="del_career_btn"
            class="ui-button ui-widget ui-corner-all" value="삭제"><br />
         <br />
         <div id="careerInfoListGrid" style="height: 500px; width: 720px"
            class="ag-theme-balham"></div>
      </div>
      <!-- 학력정보 -->
      <div id="tabs-4">
         <input type="button" id="add_education_btn"
            class="ui-button ui-widget ui-corner-all" value="추가"> <input
            type="button" id="del_education_btn"
            class="ui-button ui-widget ui-corner-all" value="삭제"><br />
         <br />
         <div id="educationInfoListGrid" style="height: 500px; width: 720px"
            class="ag-theme-balham"></div>
      </div>
      <!-- 자격증정보 -->
      <div id="tabs-5">
         <input type="button" id="add_license_btn"
            class="ui-button ui-widget ui-corner-all" value="추가"> <input
            type="button" id="del_license_btn"
            class="ui-button ui-widget ui-corner-all" value="삭제"><br />
         <br />
         <div id="licenseInfoListGrid" style="height: 500px; width: 720px"
            class="ag-theme-balham"></div>
      </div>
      <!-- 가족정보 -->
      <div id="tabs-6">
         <input type="button" id="add_family_btn" 
            class="ui-button ui-widget ui-corner-all" value="추가"> <input
            type="button" id="del_family_btn"
            class="ui-button ui-widget ui-corner-all" value="삭제"><br />
         <br />
         <div id="familyInfoListGrid" style="height: 500px; width: 720px"
            class="ag-theme-balham"></div>
      </div>

      <!-- 저장/취소버튼 -->
      <input type="button" id="modifyEmp_Btn"
         class="ui-button ui-widget ui-corner-all" value="저장"> 
      <input
         type="button" id="can_work_btn"
         class="ui-button ui-widget ui-corner-all" value="취소">
   </div>


</body>
</html>