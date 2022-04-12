package kr.co.yooooon.hr.salary.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;



import kr.co.yooooon.common.exception.DataAccessException;
import kr.co.yooooon.common.to.ResultTO;
import kr.co.yooooon.common.transaction.DataSourceTransactionManager;
import kr.co.yooooon.hr.salary.dao.MonthSalaryDAOImpl;
import kr.co.yooooon.hr.salary.to.MonthSalaryTO;

public class MonthSalaryDAOImpl implements MonthSalaryDAO{
	
	private DataSourceTransactionManager dataSourceTransactionManager ;
	
	   public void setDataSourceTransactionManager(DataSourceTransactionManager dataSourceTransactionManager) {
		      this.dataSourceTransactionManager = dataSourceTransactionManager;
		   }
	   
	   @Override
	   public ArrayList<MonthSalaryTO> selectYearSalary(String applyYear, String empCode) {
	      // TODO Auto-generated method stub
	    
	      Connection con = null;
	      PreparedStatement pstmt = null;
	      ResultSet rs = null;
	      ArrayList<MonthSalaryTO> yearSalaryList=new ArrayList<MonthSalaryTO>();
	      try {
	         con = dataSourceTransactionManager.getConnection();  

	         StringBuffer query = new StringBuffer();
	           
	         query.append("SELECT EMP_CODE, SUM(SALARY) AS SALARY, SUM(TOTAL_EXT_SAL) AS TOTAL_EXT_SAL, SUM(TOTAL_DEDUCTION) AS TOTAL_DEDUCTION, SUM(TOTAL_PAYMENT) AS TOTAL_PAYMENT, SUM(REAL_SALARY) AS REAL_SALARY ");
	         query.append("FROM MONTH_SALARY WHERE APPLY_YEAR_MONTH LIKE ? AND EMP_CODE = ? AND FINALIZE_STATUS = 'Y' ");
	         query.append("GROUP BY EMP_CODE");

	        
	         
	         
	         
	         
	         pstmt = con.prepareStatement(query.toString());
	         pstmt.setString(1, applyYear + "%"); //  % �빐以섏빞�븳�떎.
	         pstmt.setString(2, empCode);
	         rs = pstmt.executeQuery();
	         MonthSalaryTO monthSalary = null;
	         while (rs.next()) {  // 而ㅻ━臾몄븷�꽌 洹몃９�븿�닔瑜� �궗�슜�븯�슜 �썡蹂� �듅�씤諛쏆� �썡湲됱� SUM�븯���떎.
	            monthSalary = new MonthSalaryTO();
	            monthSalary.setEmpCode(rs.getString("EMP_CODE"));
	            monthSalary.setSalary(rs.getString("SALARY"));
	            monthSalary.setTotalExtSal(rs.getString("TOTAL_EXT_SAL"));
	            monthSalary.setTotalPayment(rs.getString("TOTAL_PAYMENT"));
	            monthSalary.setTotalDeduction(rs.getString("TOTAL_DEDUCTION"));
	            monthSalary.setRealSalary(rs.getString("REAL_SALARY"));
	            yearSalaryList.add(monthSalary); // �솢 諛곗뿴�뿉 �떞吏�? �젅肄붾뱶 �븳�뻾�씤�뵒? 
	         }
	       
	         return yearSalaryList;
	      } catch (Exception sqle) {
	       
	         throw new DataAccessException(sqle.getMessage());
	      } finally {
	         dataSourceTransactionManager.close(pstmt, rs);
	      }
	   }
	   
	   @Override
	   public HashMap<String, Object> batchMonthSalaryProcess(String applyYearMonth, String empCode) { // 議고쉶
	      // TODO Auto-generated method stub
	      
	      Connection con = null;
	      CallableStatement cstmt = null;  // PreparedStatement �긽�냽諛쏆� �씤�꽣�럹�씠�뒪
	      ResultSet rs = null;
	      ResultTO resultTO = null;
	      HashMap<String, Object> resultMap = new HashMap<>();
	      try {
	         con = dataSourceTransactionManager.getConnection();

	         StringBuffer query = new StringBuffer();
	         query.append("{call P_HR_SALARY.P_CREATE_MONTH_SALARY(?,?,?,?,?)}");
	         cstmt = con.prepareCall(query.toString());
	         System.out.println("DAO  : "+applyYearMonth);
	         System.out.println("DAO : "+empCode);
	         cstmt.setString(1, applyYearMonth);
	         cstmt.setString(2, empCode);
	         cstmt.registerOutParameter(3, oracle.jdbc.OracleTypes.CURSOR); 
	         cstmt.registerOutParameter(4, java.sql.Types.VARCHAR);
	         cstmt.registerOutParameter(5, java.sql.Types.VARCHAR);
	         cstmt.execute();

	         resultTO = new ResultTO();
	         resultTO.setErrorCode(cstmt.getString(4));
	         resultTO.setErrorMsg(cstmt.getString(5));
	      
	         
	         rs = (ResultSet)cstmt.getObject(3); 
	         MonthSalaryTO monthSalary = null;
	         
	         while (rs.next()) {
	            monthSalary = new MonthSalaryTO();
	            monthSalary.setEmpCode(rs.getString("EMP_CODE"));
	            monthSalary.setApplyYearMonth(rs.getString("APPLY_YEAR_MONTH"));
	            monthSalary.setSalary(rs.getString("SALARY"));
	            monthSalary.setTotalExtSal(rs.getString("TOTAL_EXT_SAL"));
	            monthSalary.setTotalPayment(rs.getString("TOTAL_PAYMENT"));
	            monthSalary.setTotalDeduction(rs.getString("TOTAL_DEDUCTION"));
	            monthSalary.setCost(rs.getString("COST"));
	            monthSalary.setUnusedDaySalary(rs.getString("UNUSED_DAY_SALARY"));
	            monthSalary.setRealSalary(rs.getString("REAL_SALARY"));
	            monthSalary.setFinalizeStatus(rs.getString("FINALIZE_STATUS"));
	         }
	         
	         resultMap.put("monthSalary", monthSalary);
	         resultMap.put("resultTO", resultTO);
	         
	         
	         
	         return resultMap;
	      } catch (Exception sqle) {
	         
	         throw new DataAccessException(resultTO.getErrorMsg());
	      } finally {
	         dataSourceTransactionManager.close(cstmt);   // PreparedStatement �긽�냽諛쏆� �씤�꽣�럹�씠�뒪
	      }
	   }
	   
	   @Override
	   public void updateMonthSalary(MonthSalaryTO monthSalary) {
	      // TODO Auto-generated method stub
	     

	      Connection con = null;
	      PreparedStatement pstmt = null;
	      try {
	         con = dataSourceTransactionManager.getConnection();

	         StringBuffer query = new StringBuffer();
	         query.append("update month_salary set ");
	         query.append("finalize_status = ? ");
	         query.append("where emp_code = ? and apply_year_month = ? ");

	         pstmt = con.prepareStatement(query.toString());
	         pstmt.setString(1, monthSalary.getFinalizeStatus());
	         pstmt.setString(2, monthSalary.getEmpCode());
	         pstmt.setString(3, monthSalary.getApplyYearMonth());

	         pstmt.executeUpdate();
	       
	      } catch (Exception sqle) {
	         
	         throw new DataAccessException(sqle.getMessage());
	      } finally {
	         dataSourceTransactionManager.close(pstmt);
	      }
	   }
}