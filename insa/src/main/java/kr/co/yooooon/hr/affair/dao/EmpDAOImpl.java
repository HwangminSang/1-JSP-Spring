package kr.co.yooooon.hr.affair.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import kr.co.yooooon.common.exception.DataAccessException;
import kr.co.yooooon.common.transaction.DataSourceTransactionManager;
import kr.co.yooooon.hr.affair.to.EmpTO;

public class EmpDAOImpl implements EmpDAO{
	private DataSourceTransactionManager dataSourceTransactionManager ;
	   public void setDataSourceTransactionManager(DataSourceTransactionManager dataSourceTransactionManager) {
		      this.dataSourceTransactionManager = dataSourceTransactionManager;
		   }
   
   //�삤踰꾨씪�씠�뱶 硫붿꽌�뱶
   public EmpTO selectEmp(String id){
     
      Connection con = null;
      PreparedStatement pstmt = null;
      ResultSet rs=null;
      try {
         StringBuffer insertQuery = new StringBuffer();
         insertQuery.append("SELECT E.EMP_CODE, E.EMP_NAME, TO_CHAR(E.BIRTHDATE,'YYYY/MM/DD') BIRTHDATE");
         insertQuery.append(", E.GENDER, E.MOBILE_NUMBER, E.ADDRESS");
         insertQuery.append(", E.DETAIL_ADDRESS, E.POST_NUMBER, E.EMAIL");
         insertQuery.append(", E.LAST_SCHOOL, E.IMG_EXTEND, PD.DEPT_NAME, PD.POSITION ,E.AUTHORITY,E.ID,E.PW ");
         insertQuery.append("FROM EMP E, (SELECT * FROM POSITION P, DEPT D) PD ");
         insertQuery.append("WHERE 1=1 ");  //1 = 1�� 留먭렇��濡� 李몄쓣 �쓽誘명빐�슂.  //https://hyjykelly.tistory.com/5
         insertQuery.append("AND E.POSITION_CODE = PD.POSITION_CODE(+) ");  //�븘�썐�꽣而ㅻ━ 
         insertQuery.append("AND E.DEPT_CODE = PD.DEPT_CODE(+) ");
         insertQuery.append("AND E.ID = ? ");
         insertQuery.append("ORDER BY E.EMP_CODE");  //�뾾�뼱�룄 �맆�벏? 
         con = dataSourceTransactionManager.getConnection();
         pstmt = con.prepareStatement(insertQuery.toString());
         pstmt.setString(1,id);
         rs=pstmt.executeQuery();
         EmpTO emp=null;
         if(rs.next()){
            emp=new EmpTO();
            emp.setEmpCode(rs.getString("emp_code"));
            emp.setEmpName(rs.getString("emp_name"));
            emp.setDeptName(rs.getString("dept_name"));
            emp.setPosition(rs.getString("position")); //吏곴툒 �궗�썝 ��由� �벑�벑
            emp.setGender(rs.getString("gender"));
            emp.setMobileNumber(rs.getString("mobile_number"));
            emp.setAuthority(rs.getString("authority")); //沅뚰븳
            emp.setId(rs.getString("id"));
            emp.setPw(rs.getString("pw"));
         }
         
        
         return emp;
      } catch(Exception sqle) {
        
         throw new DataAccessException(sqle.getMessage());         
      } finally {
         dataSourceTransactionManager.close(pstmt, rs);
      }
   }
   
   @Override
   public String selectLastEmpCode() {
      // TODO Auto-generated method stub
     

      Connection con = null;
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try {
         con = dataSourceTransactionManager.getConnection();
         StringBuffer query = new StringBuffer();
         query.append("select emp_code from emp order by emp_code desc");

         pstmt = con.prepareStatement(query.toString());
         rs = pstmt.executeQuery();
         rs.next();

        
         return rs.getString("emp_code");
      } catch (Exception sqle) {
       
         throw new DataAccessException(sqle.getMessage());
      } finally {
         dataSourceTransactionManager.close(pstmt, rs);
      }
   }
   
   @Override
   public void registEmployee(EmpTO empto) {  // 吏곸썝�쓣 �엯�젰 �떆�궎�뒗 遺�遺�
     
      Connection con = null;
      CallableStatement cstmt = null;       //�봽濡쒖떆�� �샇異쒖쐞�빐�꽌 �꽑�뼵
      
      try {
         con = dataSourceTransactionManager.getConnection();
         StringBuffer query = new StringBuffer();
         
         query.append("{call P_INSERT_EMP(?,?,TO_DATE(?,'RRRR/MM/DD'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}"); //P_INSERT_EMP
         cstmt = con.prepareCall(query.toString());        
           cstmt.setString(1, empto.getEmpCode()); 		 // �궗�썝踰덊샇 
           cstmt.setString(2, empto.getEmpName()); 		 // �궗�썝�씠由� 
           cstmt.setString(3, empto.getBirthdate()); 	 // �궗�썝 �깮�씪 
           cstmt.setString(4, empto.getGender() ); 		 // �궗�썝�쓽 �꽦蹂� 
           cstmt.setString(5, empto.getMobileNumber());  // �궗�썝 �룿 踰덊샇 
           cstmt.setString(6, empto.getAddress()); 		 // �궗�썝�쓽 二쇱냼 
           cstmt.setString(7, empto.getDetailAddress()); // �궗�썝�쓽 �긽�꽭二쇱냼 
           cstmt.setString(8, empto.getPostNumber()); 	 // �슦�렪踰덊샇 
           cstmt.setString(9, empto.getEmail()); 		 // �씠硫붿씪 
           cstmt.setString(10, empto.getLastSchool()); 	 // 理쒖쥌�븰�젰 
           cstmt.setString(11, empto.getImgExtend()); 	 // �궗吏� �씠誘몄� �솗�옣�옄 
           cstmt.setString(12, empto.getDeptName()); 	 // 遺��꽌�씠由� 
           cstmt.setString(13, empto.getPosition()); 	 // 吏곴툒 
           cstmt.setString(14, empto.getHobong()); 		 // �샇遊� 
           cstmt.setString(15, empto.getOccupation()); 	 //洹쇰Т吏��뿭 ex ) �깮�궛吏� , �궗臾댁쭅 
           cstmt.setString(16, empto.getEmployment()); 	 // 洹쇰Т�삎�깭
           cstmt.setString(17, empto.getId());
           cstmt.setString(18, empto.getPw());
           cstmt.execute();
         
        
         
      } catch(Exception sqle) {
         
         throw new DataAccessException(sqle.getMessage());         
      } finally {
         dataSourceTransactionManager.close(cstmt);
      }      
   }
   
   @Override
   public ArrayList<EmpTO> selectEmpList() {
     
      Connection con = null;
      PreparedStatement pstmt = null;
      ResultSet rs=null;
      try {
         StringBuffer insertQuery = new StringBuffer();
         insertQuery.append("SELECT E.EMP_CODE, E.EMP_NAME, TO_CHAR(E.BIRTHDATE,'YYYY/MM/DD') BIRTHDATE");
         insertQuery.append(", E.GENDER, E.MOBILE_NUMBER, E.ADDRESS");
         insertQuery.append(", E.DETAIL_ADDRESS, E.POST_NUMBER, E.EMAIL");
         insertQuery.append(", E.LAST_SCHOOL, E.IMG_EXTEND, PD.DEPT_NAME, PD.POSITION , E.ID, E.PW ");
         insertQuery.append("FROM EMP E, (SELECT * FROM POSITION P, DEPT D) PD ");
         insertQuery.append("WHERE 1=1 ");  
         insertQuery.append("AND E.POSITION_CODE = PD.POSITION_CODE(+) ");
         insertQuery.append("AND E.DEPT_CODE = PD.DEPT_CODE(+) ");
         insertQuery.append("ORDER BY E.EMP_CODE");
         con = dataSourceTransactionManager.getConnection();
         pstmt = con.prepareStatement(insertQuery.toString());
         rs=pstmt.executeQuery();
         ArrayList<EmpTO> list=new ArrayList<EmpTO>(); 
         while(rs.next()){
            EmpTO emp=new EmpTO();
            emp.setEmpName(rs.getString("emp_name"));
            emp.setDeptName(rs.getString("dept_name"));
            emp.setPosition(rs.getString("position"));
            emp.setGender(rs.getString("gender"));
            emp.setMobileNumber(rs.getString("mobile_number"));
            emp.setEmpCode(rs.getString("emp_code"));
            emp.setAddress(rs.getString("address"));
            emp.setDetailAddress(rs.getString("detail_address"));
            emp.setBirthdate(rs.getString("birthdate"));
            emp.setPostNumber(rs.getString("post_number"));
            emp.setImgExtend(rs.getString("img_extend"));
            emp.setLastSchool(rs.getString("last_school"));
            emp.setEmail(rs.getString("email"));
            emp.setId(rs.getString("id"));
            list.add(emp);
         }
        
         return list;
      } catch(Exception sqle) {
       
         throw new DataAccessException(sqle.getMessage());         
      } finally {
         dataSourceTransactionManager.close(pstmt, rs);
      }
   }
   
   @Override
   public ArrayList<EmpTO> selectEmpListD(String dept) {
     
      ArrayList<EmpTO> list=new ArrayList<EmpTO>();
      Connection con = null;
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try {
         StringBuffer query = new StringBuffer();
         query.append("SELECT E.EMP_CODE, E.EMP_NAME, TO_CHAR(E.BIRTHDATE,'YYYY/MM/DD') BIRTHDATE");
         query.append(", E.GENDER, E.MOBILE_NUMBER, E.ADDRESS");
         query.append(", E.DETAIL_ADDRESS, E.POST_NUMBER, E.EMAIL");
         query.append(", E.LAST_SCHOOL, E.IMG_EXTEND, PD.DEPT_NAME, PD.POSITION , E.ID ");
         query.append("FROM EMP E, (SELECT * FROM POSITION P, DEPT D) PD ");
         query.append("WHERE 1=1 "); // 臾댁“嫄� 李� 
         query.append("AND E.POSITION_CODE = PD.POSITION_CODE(+) ");
         query.append("AND E.DEPT_CODE = PD.DEPT_CODE(+) ");
         query.append("AND PD.DEPT_NAME = ? ");
         query.append("ORDER BY E.EMP_CODE");
         con = dataSourceTransactionManager.getConnection();
         pstmt = con.prepareStatement(query.toString());
         pstmt.setString(1, dept);
         
         rs = pstmt.executeQuery();
         while(rs.next()){
            EmpTO emp=new EmpTO();
            emp.setEmpName(rs.getString("emp_name"));
            emp.setDeptName(rs.getString("dept_name"));
            emp.setPosition(rs.getString("position"));
            emp.setGender(rs.getString("gender"));
            emp.setMobileNumber(rs.getString("mobile_number"));
            emp.setEmpCode(rs.getString("emp_code"));
            emp.setAddress(rs.getString("address"));
            emp.setDetailAddress(rs.getString("detail_address"));
            emp.setBirthdate(rs.getString("birthdate"));
            emp.setPostNumber(rs.getString("post_number"));
            emp.setImgExtend(rs.getString("img_extend"));
            emp.setLastSchool(rs.getString("last_school"));
            emp.setEmail(rs.getString("email"));
            emp.setId(rs.getString("id"));
            list.add(emp);
         }
        
         return list;
      } catch(Exception sqle) {
       
         throw new DataAccessException(sqle.getMessage());         
      } finally {
         dataSourceTransactionManager.close(pstmt, rs);
      }
   }
   
   @Override
   public ArrayList<EmpTO> selectEmpListN(String name) { // �궗�썝紐낆쑝濡� 寃��깋�뻽�쓣�븣
     
     
      Connection con = null;
      PreparedStatement pstmt = null;
      ResultSet rs=null;
      ArrayList<EmpTO> list=new ArrayList<EmpTO>(); 
      try {
         StringBuffer insertQuery = new StringBuffer();
         insertQuery.append("SELECT E.EMP_CODE, E.EMP_NAME, TO_CHAR(E.BIRTHDATE,'YYYY/MM/DD') BIRTHDATE");
         insertQuery.append(", E.GENDER, E.MOBILE_NUMBER, E.ADDRESS");
         insertQuery.append(", E.DETAIL_ADDRESS, E.POST_NUMBER, E.EMAIL");
         insertQuery.append(", E.LAST_SCHOOL, E.IMG_EXTEND, PD.DEPT_NAME, PD.POSITION, E.ID ");
         insertQuery.append("FROM EMP E, (SELECT * FROM POSITION P, DEPT D) PD ");
         insertQuery.append("WHERE 1=1 "); // 李�
         insertQuery.append("AND E.POSITION_CODE = PD.POSITION_CODE(+) ");
         insertQuery.append("AND E.DEPT_CODE = PD.DEPT_CODE(+) ");
         insertQuery.append("AND E.EMP_NAME = ? ");
         insertQuery.append("ORDER BY E.EMP_CODE");
       
         con = dataSourceTransactionManager.getConnection();
         pstmt = con.prepareStatement(insertQuery.toString());
         
         pstmt.setString(1, name);
         rs=pstmt.executeQuery();
         while(rs.next()){ 
            
               EmpTO emp=new EmpTO();
               emp.setEmpCode(rs.getString("emp_code"));
               emp.setEmpName(rs.getString("emp_name"));
               emp.setBirthdate(rs.getString("birthdate"));
               emp.setGender(rs.getString("gender"));
               emp.setMobileNumber(rs.getString("mobile_number"));
               emp.setAddress(rs.getString("address"));
               emp.setDetailAddress(rs.getString("detail_address"));
               emp.setPostNumber(rs.getString("post_number"));
               emp.setEmail(rs.getString("email"));
               emp.setLastSchool(rs.getString("last_school"));
               emp.setImgExtend(rs.getString("img_extend"));
               emp.setPosition(rs.getString("position"));
               emp.setDeptName(rs.getString("dept_name"));
               emp.setId(rs.getString("id"));
               list.add(emp);
            
         }
         
         return list;
      } catch(Exception sqle) {
        
         throw new DataAccessException(sqle.getMessage());         
      } finally {
         dataSourceTransactionManager.close(pstmt, rs);
      }
   }
   
   @Override
   public String getEmpCode(String name) {
      // TODO Auto-generated method stub
      String empCode=null;
      
      Connection con = null;
      PreparedStatement pstmt = null;
      ResultSet rs=null;
      ArrayList<EmpTO> list=new ArrayList<EmpTO>();
      try {
         StringBuffer insertQuery = new StringBuffer();
         insertQuery.append("select emp_code from emp where emp_name=?");
         con = dataSourceTransactionManager.getConnection();
         pstmt = con.prepareStatement(insertQuery.toString());
         pstmt.setString(1, name);
         rs=pstmt.executeQuery();
         while(rs.next()){
               empCode = rs.getString("emp_code");
         }
       
         return empCode;
      } catch(Exception sqle) {
   
         throw new DataAccessException(sqle.getMessage());         
      } finally {
         dataSourceTransactionManager.close(pstmt, rs);
      }
   }
   
   @Override
   public EmpTO selectEmployee(String empCode) {
      

      Connection con = null;
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try {
         con = dataSourceTransactionManager.getConnection();

         StringBuffer query = new StringBuffer();
         query.append("SELECT E.EMP_CODE, E.EMP_NAME, TO_CHAR(E.BIRTHDATE,'YYYY/MM/DD') BIRTHDATE");
         query.append(", E.GENDER, E.MOBILE_NUMBER, E.ADDRESS");
         query.append(", E.DETAIL_ADDRESS, E.POST_NUMBER, E.EMAIL");
         query.append(", E.LAST_SCHOOL, E.IMG_EXTEND, PD.DEPT_NAME, PD.POSITION, E.ID,E.PW ");
         query.append("FROM EMP E, (SELECT * FROM POSITION P, DEPT D) PD ");
         query.append("WHERE 1=1 ");
         query.append("AND E.POSITION_CODE = PD.POSITION_CODE(+) ");
         query.append("AND E.DEPT_CODE = PD.DEPT_CODE(+) ");
         query.append("AND E.EMP_CODE = ? ");
         query.append("ORDER BY E.EMP_CODE");
                  

         
         pstmt = con.prepareStatement(query.toString());
         pstmt.setString(1, empCode);
         rs = pstmt.executeQuery();
         EmpTO emp = new EmpTO();
         if (rs.next()) {
            emp.setEmpName(rs.getString("emp_name"));
            emp.setDeptName(rs.getString("dept_name"));
            emp.setPosition(rs.getString("position"));
            emp.setGender(rs.getString("gender"));
            emp.setMobileNumber(rs.getString("mobile_number"));
            emp.setEmpCode(rs.getString("emp_code"));
            emp.setAddress(rs.getString("address"));
            emp.setDetailAddress(rs.getString("detail_address"));
            emp.setBirthdate(rs.getString("birthdate"));
            emp.setPostNumber(rs.getString("post_number"));
            emp.setImgExtend(rs.getString("img_extend"));
            emp.setLastSchool(rs.getString("last_school"));
            emp.setEmail(rs.getString("email"));
            emp.setId(rs.getString("id"));
            emp.setPw(rs.getString("pw"));
         }

        
         return emp;
      } catch (Exception sqle) {
       
         throw new DataAccessException(sqle.getMessage());
      } finally {
         dataSourceTransactionManager.close(pstmt, rs);
      }
   }
   
   @Override
   public void updateEmployee(EmpTO emp) {
   
      // TODO Auto-generated method stub
     

      Connection con = null;
      PreparedStatement pstmt = null;
      
      
      
      try {
         con = dataSourceTransactionManager.getConnection();
         StringBuffer query = new StringBuffer();
         query.append("update emp set ");
           query.append("EMP_NAME = ?, BIRTHDATE = to_date(?,'YYYY/MM/DD'),GENDER= ?, MOBILE_NUMBER=?,  ");
           query.append("ADDRESS = ?, DETAIL_ADDRESS = ?, POST_NUMBER = ?, EMAIL= ?, LAST_SCHOOL=?, IMG_EXTEND=?, ");
           query.append(" position_code = (select position_code from position where position.position=?), ");
           query.append(" dept_code = (select dept_code from dept where dept.dept_name = ?) where emp_code = ? ");

         pstmt = con.prepareStatement(query.toString());
         pstmt.setString(1, emp.getEmpName());
         pstmt.setString(2, emp.getBirthdate());
         pstmt.setString(3, emp.getGender());
         pstmt.setString(4, emp.getMobileNumber());
         pstmt.setString(5, emp.getAddress());
         pstmt.setString(6, emp.getDetailAddress());
         pstmt.setString(7, emp.getPostNumber());
         pstmt.setString(8, emp.getEmail());
         pstmt.setString(9, emp.getLastSchool());
         pstmt.setString(10, emp.getImgExtend());
         pstmt.setString(11, emp.getPosition());
         pstmt.setString(12, emp.getDeptName());
         pstmt.setString(13, emp.getEmpCode());
         pstmt.executeUpdate();

       
      } catch (Exception sqle) {
       
         throw new DataAccessException(sqle.getMessage());
      } finally {
         dataSourceTransactionManager.close(pstmt);
      }
      
   }
   @Override
   public void deleteEmployee(EmpTO emp) {
        

         Connection con = null;
         CallableStatement cstmt = null;      //�봽濡쒖떆��

         try {
            con = dataSourceTransactionManager.getConnection();
            StringBuffer query = new StringBuffer();
            query.append("{call P_DELETE_EMP(?,?,?)}"); // �봽�샇�떆�� �샇�� 
            cstmt = con.prepareCall(query.toString());
            cstmt.setString(1, emp.getEmpCode());
            cstmt.registerOutParameter(2,java.sql.Types.VARCHAR); //https://blog.naver.com/knbawe/100005915455  //�봽濡쒖떆���뿉�꽌 蹂��닔瑜� 諛쏆븘�삱�븣 registerOutParameter() �궗�슜
            cstmt.registerOutParameter(3,java.sql.Types.VARCHAR);            
            cstmt.execute();
            
            System.out.println("�쁾�쁾�쁾�쁾�쁾�쁾�쁾�쁾�봽濡쒖떆���뿉�윭�쁾�쁾�쁾�쁾�쁾�쁾�쁾�쁾�쁾�쁾"+cstmt.getString(2)); // �븘臾댁삤瑜� �븞�굹�꽌 null
            System.out.println("�쁾�쁾�쁾�쁾�쁾�쁾�쁾�쁾�봽濡쒖떆���뿉�윭�쁾�쁾�쁾�쁾�쁾�쁾�쁾�쁾�쁾�쁾"+cstmt.getString(3));
          
          
          
          
         } catch (Exception sqle) {
           
            throw new DataAccessException(sqle.getMessage());
         } finally {
            dataSourceTransactionManager.close(cstmt);
         }
         
   }
      
}