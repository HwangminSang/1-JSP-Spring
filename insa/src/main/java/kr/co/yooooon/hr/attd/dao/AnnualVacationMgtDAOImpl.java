package kr.co.yooooon.hr.attd.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;


import kr.co.yooooon.common.exception.DataAccessException;
import kr.co.yooooon.common.to.ResultTO;
import kr.co.yooooon.common.transaction.DataSourceTransactionManager;

import kr.co.yooooon.hr.attd.to.annualVacationMgtTO;

public class AnnualVacationMgtDAOImpl implements AnnualVacationMgtDAO{
	private DataSourceTransactionManager dataSourceTransactionManager ;
	
	   public void setDataSourceTransactionManager(DataSourceTransactionManager dataSourceTransactionManager) {
		      this.dataSourceTransactionManager = dataSourceTransactionManager;
		   }
   
   @Override
   public HashMap<String, Object> batchAnnualVacationMgtProcess(String applyYearMonth) { 
      // TODO Auto-generated method stub
     

      Connection con = null;
      CallableStatement cstmt = null;
      ResultSet rs = null;
      ResultTO resultTO = null;
      ArrayList<annualVacationMgtTO> annualVacationMgtList=new ArrayList<annualVacationMgtTO>();
      HashMap<String, Object> resultMap = new HashMap<>();
      try {
         con = dataSourceTransactionManager.getConnection();
      
         StringBuffer query = new StringBuffer();
         query.append("{call P_HR_VACATION.P_CREATE_ANNUAL_VACATION_MGT(?,?,?,?,?)}");
         cstmt = con.prepareCall(query.toString());
         
         cstmt.setString(1, applyYearMonth);
         cstmt.setString(2, "인사팀");
         cstmt.registerOutParameter(3, oracle.jdbc.OracleTypes.CURSOR);
         cstmt.registerOutParameter(4, java.sql.Types.VARCHAR);
         cstmt.registerOutParameter(5, java.sql.Types.VARCHAR);
         cstmt.execute();
         
         resultTO = new ResultTO();
         resultTO.setErrorCode(cstmt.getString(4));
         resultTO.setErrorMsg(cstmt.getString(5));
         System.out.println("메세제:"+resultTO.getErrorMsg());
         rs = (ResultSet)cstmt.getObject(3); 
              
         annualVacationMgtTO annualVacationMgt = null;
         
         while (rs.next()) {
            annualVacationMgt = new annualVacationMgtTO();
            annualVacationMgt.setEmpCode(rs.getString("EMP_CODE"));
            annualVacationMgt.setEmpName(rs.getString("EMP_NAME"));
            annualVacationMgt.setApplyYearMonth(rs.getString("APPLY_YEAR_MONTH"));
            annualVacationMgt.setAfternoonOff(rs.getString("AFTERNOON_OFF"));
            annualVacationMgt.setMonthlyLeave(rs.getString("MONTHLY_LEAVE"));
            annualVacationMgt.setRemainingHoliday(rs.getString("REMAINING_HOLIDAY")); // 珥앹뿰李⑥뿉�꽌 - �뿰李�+諛섏감= 媛� 
            annualVacationMgt.setFinalizeStatus(rs.getString("FINALIZE_STATUS"));
            annualVacationMgtList.add(annualVacationMgt);
         }

         resultMap.put("annualVacationMgtList", annualVacationMgtList);
         resultMap.put("resultTO", resultTO);
         
         
         return resultMap;
      } catch (Exception sqle) {
   
         throw new DataAccessException(resultTO.getErrorMsg());
      } finally {
         dataSourceTransactionManager.close(cstmt,rs);
      }
   }
   
   public void updateAnnualVacationMgtList(annualVacationMgtTO annualVacationMgt) {
      // TODO Auto-generated method stub
    

      Connection con = null;
      PreparedStatement pstmt = null;
      try {
         con = dataSourceTransactionManager.getConnection();

         StringBuffer query = new StringBuffer();
         query.append("UPDATE ANNUAL_VACATION_MANAGE SET ");
         query.append("FINALIZE_STATUS = ?,REMAINING_HOLIDAY = ?");
         query.append("WHERE EMP_CODE = ? AND APPLY_YEAR_MONTH = ?");

         pstmt = con.prepareStatement(query.toString());
         pstmt.setString(1, annualVacationMgt.getFinalizeStatus());
         pstmt.setInt(2, Integer.parseInt(annualVacationMgt.getRemainingHoliday()));
         pstmt.setString(3, annualVacationMgt.getEmpCode());
         pstmt.setString(4, annualVacationMgt.getApplyYearMonth());

         pstmt.executeUpdate();
        
      } catch (Exception sqle) {
       
         throw new DataAccessException(sqle.getMessage());
      } finally {
         dataSourceTransactionManager.close(pstmt);
      }
   }
   
   
  
   
   
   
   
   
   public void cancelAnnualVacationMgtList(annualVacationMgtTO annualVacationMgt) {
      // TODO Auto-generated method stub
      

      Connection con = null;
      PreparedStatement pstmt = null;
      try {
         con = dataSourceTransactionManager.getConnection();

         int holiday = Integer.parseInt(annualVacationMgt.getRemainingHoliday()); //�궓�� �뿰李�
         int afternoonOff = Integer.parseInt(annualVacationMgt.getAfternoonOff()); //諛섏감�궗�슜媛쒖닔
         int monthlyLeave = Integer.parseInt(annualVacationMgt.getMonthlyLeave()); //�뿰李⑥궗�슜媛쒖닔
         

         StringBuffer query = new StringBuffer();
         query.append("UPDATE ANNUAL_VACATION_MANAGE SET ");
         query.append("FINALIZE_STATUS = ?,REMAINING_HOLIDAY = ?");
         query.append("WHERE EMP_CODE = ? AND APPLY_YEAR_MONTH = ?");

         pstmt = con.prepareStatement(query.toString());
         pstmt.setString(1, annualVacationMgt.getFinalizeStatus());
         pstmt.setInt(2, holiday+afternoonOff+monthlyLeave);
         pstmt.setString(3, annualVacationMgt.getEmpCode());
         pstmt.setString(4, annualVacationMgt.getApplyYearMonth());

         pstmt.executeUpdate();
       
      } catch (Exception sqle) {
      
         throw new DataAccessException(sqle.getMessage());
      } finally {
         dataSourceTransactionManager.close(pstmt);
      }
   }
   
   
 
   public HashMap<String, Object> createVacation(String applyYear) { //�뿰李� �깮�꽦
	      // TODO Auto-generated method stub
	     

	      Connection con = null;
	      CallableStatement cstmt = null;
	      HashMap<String, Object> resultMap=new HashMap<String, Object>();
	      ResultTO resultTO=null;
	      try {
	         con = dataSourceTransactionManager.getConnection();
	         System.out.println("�뤌�뿉�꽌 �궇�븘�삩 �궇吏쒓컪 : "+applyYear);
	         StringBuffer query = new StringBuffer();
	         query.append("{call P_HR_VACATION.P_CREATE_ANNUAL_VACATION(?,?,?)}");
	         cstmt = con.prepareCall(query.toString());
	         
	         cstmt.setInt(1,Integer.parseInt(applyYear));
	         cstmt.registerOutParameter(2, java.sql.Types.VARCHAR);
	         cstmt.registerOutParameter(3, java.sql.Types.VARCHAR);
	         cstmt.execute();
	      
	        
	         resultTO = new ResultTO();
	         resultTO.setErrorCode(cstmt.getString(2));
	         resultTO.setErrorMsg(cstmt.getString(3));
	         resultMap.put("resultTO", resultTO);
	         
	         
	         return resultMap;
	      } catch (Exception sqle) {
	         
	         throw new DataAccessException(sqle.getMessage());
	      } finally {
	         dataSourceTransactionManager.close(cstmt);
	      }
	   }
   
   public void updateAnnualVacationList(annualVacationMgtTO annualVacationMgt) {
	      // TODO Auto-generated method stub
	     

	      Connection con = null;
	      PreparedStatement pstmt = null;
	      try {
	         con = dataSourceTransactionManager.getConnection();
	         int holiday = Integer.parseInt(annualVacationMgt.getRemainingHoliday());
	         
	         StringBuffer query = new StringBuffer();
	         query.append("UPDATE ANNUAL_VACATION SET ");  // ???
	         query.append("remaining_vacation = ? ");
	         query.append("WHERE EMP_CODE = ? AND year = ? ");

	         pstmt = con.prepareStatement(query.toString());
	         pstmt.setInt(1, holiday);
	         pstmt.setString(2, annualVacationMgt.getEmpCode());
	         pstmt.setInt(3, Integer.parseInt(annualVacationMgt.getApplyYearMonth().substring(0,4))); // �썡�씠�뾾�쓬

	         pstmt.executeUpdate();
	        
	      } catch (Exception sqle) {
	      
	         throw new DataAccessException(sqle.getMessage());
	      } finally {
	         dataSourceTransactionManager.close(pstmt);
	      }
	   }
   
   public void cancelAnnualVacationList(annualVacationMgtTO annualVacationMgt) {
	      // TODO Auto-generated method stub
	    

	      Connection con = null;
	      PreparedStatement pstmt = null;
	      try {
	         con = dataSourceTransactionManager.getConnection();
	         int holiday = Integer.parseInt(annualVacationMgt.getRemainingHoliday());
	         int afternoonOff = Integer.parseInt(annualVacationMgt.getAfternoonOff());
	         int monthlyLeave = Integer.parseInt(annualVacationMgt.getMonthlyLeave());
	         
	         StringBuffer query = new StringBuffer();
	         query.append("UPDATE ANNUAL_VACATION SET ");
	         query.append("remaining_vacation = ? ");
	         query.append("WHERE EMP_CODE = ? AND year = ? ");

	         pstmt = con.prepareStatement(query.toString());
	         pstmt.setInt(1, holiday+afternoonOff+monthlyLeave);
	         pstmt.setString(2, annualVacationMgt.getEmpCode());
	         pstmt.setInt(3, Integer.parseInt(annualVacationMgt.getApplyYearMonth().substring(0,4)));

	         pstmt.executeUpdate();
	        
	      } catch (Exception sqle) {
	         
	         throw new DataAccessException(sqle.getMessage());
	      } finally {
	         dataSourceTransactionManager.close(pstmt);
	      }
	   }
   
}