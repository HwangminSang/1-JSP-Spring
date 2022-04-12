package kr.co.yooooon.hr.salary.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import kr.co.yooooon.common.exception.DataAccessException;
import kr.co.yooooon.common.transaction.DataSourceTransactionManager;
import kr.co.yooooon.hr.salary.dao.FullTimeSalaryDAOImpl;
import kr.co.yooooon.hr.salary.to.FullTimeSalTO;
import kr.co.yooooon.hr.salary.to.PayDayTO;
import oracle.jdbc.OracleTypes;


public class FullTimeSalaryDAOImpl implements FullTimeSalaryDAO{
	private DataSourceTransactionManager dataSourceTransactionManager;
	
	public void setDataSourceTransactionManager(DataSourceTransactionManager dataSourceTransactionManager) {
		this.dataSourceTransactionManager = dataSourceTransactionManager;
	}
	   
	   @Override
	   public ArrayList<FullTimeSalTO> findAllMoney(String empCode) {
	      Connection con = null;
	      PreparedStatement pstmt = null;
	      ResultSet rs = null;
	      ArrayList<FullTimeSalTO> fullTimeSalaryList=new ArrayList<FullTimeSalTO>();
	      try {
	         con = dataSourceTransactionManager.getConnection();

	         StringBuffer query = new StringBuffer();
	           
	         query.append("SELECT EMP_CODE, APPLY_YEAR_MONTH, BASIC_SALARY, POSITION_SALARY, FAMILY_SALARY, FOOD_SALARY, OVER_WORK_SALARY, NATIONAL_PENSION, HEALTH_INSURANCE, ");
	         query.append("LONG_TERM_INSURANCE, EMPLOYMENT_INSURANCE, RELIGION_DONATION, INCOME_TAX, RESIDENT_TAX, FINALIZE_STATUS, BASIC_SAL_BEFORE ");
	         query.append("FROM  FULLTIME_EMPLOYEE_SALARY WHERE APPLY_YEAR_MONTH = ? ");
	         
	         pstmt = con.prepareStatement(query.toString());
	         pstmt.setString(1, empCode);
	         rs = pstmt.executeQuery();
	         FullTimeSalTO fullTimeSalary = null;
	         while (rs.next()) {
	            fullTimeSalary = new FullTimeSalTO();
	            fullTimeSalary.setEmpCode(rs.getString("EMP_CODE"));
	            fullTimeSalary.setApplyYearMonth(rs.getString("APPLY_YEAR_MONTH"));
	            fullTimeSalary.setBasicSalary(rs.getString("BASIC_SALARY"));
	            fullTimeSalary.setPositionSalary(rs.getString("POSITION_SALARY"));
	            fullTimeSalary.setFamilySalary(rs.getString("FAMILY_SALARY"));
	            fullTimeSalary.setFoodSalary(rs.getString("FOOD_SALARY"));
	            fullTimeSalary.setOverWorkSalary(rs.getString("OVER_WORK_SALARY"));
	            fullTimeSalary.setNationalPension(rs.getString("NATIONAL_PENSION"));
	            fullTimeSalary.setHealthInsurance(rs.getString("HEALTH_INSURANCE"));
	            fullTimeSalary.setLongTermInsurance(rs.getString("LONG_TERM_INSURANCE"));
	            fullTimeSalary.setEmploymentInsurance(rs.getString("EMPLOYMENT_INSURANCE"));
	            fullTimeSalary.setReligionDonation(rs.getString("RELIGION_DONATION"));
	            fullTimeSalary.setIncomeTax(rs.getString("INCOME_TAX"));
	            fullTimeSalary.setResidentTax(rs.getString("RESIDENT_TAX"));
	            fullTimeSalary.setFinalizeStatus(rs.getString("FINALIZE_STATUS"));
	            fullTimeSalary.setBasicSalBefore(rs.getString("BASIC_SAL_BEFORE"));
	            fullTimeSalaryList.add(fullTimeSalary);
	         }
	         return fullTimeSalaryList;
	      } catch (Exception sqle) {
	         throw new DataAccessException(sqle.getMessage());
	      } finally {
	         dataSourceTransactionManager.close(pstmt, rs);
	      }
	   }
	   

	   @Override
	   public ArrayList<FullTimeSalTO> selectFullTimeSalary(String applyYear, String empCode) {
	      ArrayList<FullTimeSalTO> fullTimeSalaryList=new ArrayList<FullTimeSalTO>();
	      Connection con = null;
	      CallableStatement cstmt = null;
	      ResultSet rs = null;

	         try {         
	        	 	con = dataSourceTransactionManager.getConnection();
	        	 	StringBuffer query = new StringBuffer();
	                query.append("{call P_CREATE_MONTH_SALARY(?,?,?,?,?)}");
	                cstmt = con.prepareCall(query.toString());
	                cstmt.setString(1, applyYear);
	                cstmt.setString(2, empCode);
	                cstmt.registerOutParameter(3, OracleTypes.CURSOR);
	                cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
	                cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
	                cstmt.executeQuery();
	                System.out.println(applyYear);
	                System.out.println(empCode);
	                System.out.println("44444444444444444444");
	                rs = (ResultSet) cstmt.getObject(3);
	                
	                String errorCode = cstmt.getString(4);
	                String errorMsg = cstmt.getString(5);
	                System.out.println(errorCode);
	                System.out.println(errorMsg);
	                
	                System.out.println("555555555555555555555555");
	                FullTimeSalTO fullTimeSalary = null;
	                System.out.println("rs�뿉 �뼹留덈굹�뱾�뼱媛�?");
	                System.out.println(rs);
	        
	         while (rs.next()) {
	        	System.out.println("666666666666666666666666666666");
	            fullTimeSalary = new FullTimeSalTO();
	            fullTimeSalary.setEmpCode(rs.getString("EMP_CODE"));
	            fullTimeSalary.setApplyYearMonth(rs.getString("APPLY_YEAR_MONTH"));
	            fullTimeSalary.setBasicSalary(rs.getString("BASIC_SALARY"));
	            fullTimeSalary.setPositionSalary(rs.getString("POSITION_SALARY"));
	            fullTimeSalary.setFamilySalary(rs.getString("FAMILY_SALARY"));
	            fullTimeSalary.setFoodSalary(rs.getString("FOOD_SALARY"));
	            fullTimeSalary.setOverWorkSalary(rs.getString("OVER_WORK_SALARY"));
	            fullTimeSalary.setNationalPension(rs.getString("NATIONAL_PENSION"));
	            fullTimeSalary.setHealthInsurance(rs.getString("HEALTH_INSURANCE"));
	            fullTimeSalary.setLongTermInsurance(rs.getString("LONG_TERM_INSURANCE"));
	            fullTimeSalary.setEmploymentInsurance(rs.getString("EMPLOYMENT_INSURANCE"));
	            fullTimeSalary.setReligionDonation(rs.getString("RELIGION_DONATION"));
	            fullTimeSalary.setIncomeTax(rs.getString("INCOME_TAX"));
	            fullTimeSalary.setResidentTax(rs.getString("RESIDENT_TAX"));
	            fullTimeSalary.setFinalizeStatus(rs.getString("FINALIZE_STATUS"));
	            fullTimeSalary.setBasicSalBefore(rs.getString("BASIC_SAL_BEFORE"));
	            fullTimeSalaryList.add(fullTimeSalary);
	         }
	         return fullTimeSalaryList;
	      } catch (Exception sqle) {
	         throw new DataAccessException(sqle.getMessage());
	      } finally {
	         dataSourceTransactionManager.close(cstmt, rs);
	      }
	   }
	  
	   
	   @Override
	   public void updateFullTimeSalary(List<FullTimeSalTO> fullTimeSalary) {

	      Connection con = null;
	      PreparedStatement pstmt = null;
	      try {
	    	 for(FullTimeSalTO Code : fullTimeSalary) {
	         con = dataSourceTransactionManager.getConnection();
	         StringBuffer query = new StringBuffer();
	         
	         query.append("UPDATE FULLTIME_EMPLOYEE_SALARY SET FINALIZE_STATUS = ? WHERE EMP_CODE = ? AND APPLY_YEAR_MONTH = ?");
	         pstmt = con.prepareStatement(query.toString());
	         pstmt.setString(1, Code.getFinalizeStatus());
	         pstmt.setString(2, Code.getEmpCode());
	         pstmt.setString(3, Code.getApplyYearMonth());
	    	 }
	    	 pstmt.executeUpdate();
	      } catch (Exception sqle) {
	         throw new DataAccessException(sqle.getMessage());
	      } finally {
	         dataSourceTransactionManager.close(pstmt);
	      }
	   }
	   
	   @Override
		public ArrayList<PayDayTO> selectPayDayList(){

	      Connection con = null;
	      PreparedStatement pstmt = null;
	      ResultSet rs = null;
	      ArrayList<PayDayTO> selectPayDayList=new ArrayList<PayDayTO>();
	      try {
	         con = dataSourceTransactionManager.getConnection();

	         StringBuffer query = new StringBuffer();
	           
	         query.append("select * from SALARY_BONUS_DATE");
	         
	         pstmt = con.prepareStatement(query.toString());
	         rs = pstmt.executeQuery();
	         PayDayTO payday = null;
	         while (rs.next()) {
	        	 payday = new PayDayTO();
	        	 payday.setOrd(rs.getString("ORD"));
	        	 payday.setPayment_date(rs.getString("PAYMENT_DATE"));
	        	 payday.setSmltn_issue(rs.getString("SMLTN_ISSUE"));
	        	 payday.setSalary_type(rs.getString("SALARY_TYPE"));
	        	 payday.setRemarks(rs.getString("REMARKS"));
	             selectPayDayList.add(payday);
	         }
	         return selectPayDayList;
	      } catch (Exception sqle) {
	         throw new DataAccessException(sqle.getMessage());
	      } finally {
	         dataSourceTransactionManager.close(pstmt, rs);
	      }
	   }
	   

}