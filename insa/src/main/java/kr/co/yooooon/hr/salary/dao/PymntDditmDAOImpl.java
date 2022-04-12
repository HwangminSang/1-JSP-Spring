package kr.co.yooooon.hr.salary.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import kr.co.yooooon.common.exception.DataAccessException;
import kr.co.yooooon.common.transaction.DataSourceTransactionManager;
import kr.co.yooooon.hr.salary.to.AmnclFrmlRsTO;
import kr.co.yooooon.hr.salary.to.AmnclFrmlTO;
import kr.co.yooooon.hr.salary.to.BasePymntItmNameCodeTO;
import kr.co.yooooon.hr.salary.to.DissectionRsTO;
import kr.co.yooooon.hr.salary.to.DissectionTO;
import kr.co.yooooon.hr.salary.to.JobCodeRsTO;
import kr.co.yooooon.hr.salary.to.JobCodeTO;
import kr.co.yooooon.hr.salary.to.PaymentItmOptionRsTO;
import kr.co.yooooon.hr.salary.to.PaymentItmOptionTO;
import kr.co.yooooon.hr.salary.to.PymntDditmTO;

public class PymntDditmDAOImpl implements PymntDditmDAO{
	   private DataSourceTransactionManager dataSourceTransactionManager;
	   
	   public void setDataSourceTransactionManager(DataSourceTransactionManager dataSourceTransactionManager) {
			this.dataSourceTransactionManager = dataSourceTransactionManager;
		}
	
   @Override
      public ArrayList<BasePymntItmNameCodeTO> findPymntDditmList(PymntDditmTO pymntData) {

         Connection con = null;
         PreparedStatement pstmt = null;
         ResultSet rs = null;
         BasePymntItmNameCodeTO BPIC = null;
         ArrayList<BasePymntItmNameCodeTO> pymntDditmList=new ArrayList<BasePymntItmNameCodeTO>();
         try {
            con = dataSourceTransactionManager.getConnection();

            StringBuffer query = new StringBuffer();
              
            query.append("select code, PAYMENT_ITEM_NAME from pymnt_dditm ");
            query.append("WHERE WAGE_CLASSIFICATION = ? and PYMDD_CLSFC = ? and YEAR = ? ");

            String slryClsfc=pymntData.getSlryClsfc();
            String pymddClsfc=pymntData.getPymddClsfc();
            String year=pymntData.getYear();
            
            
            pstmt = con.prepareStatement(query.toString());
            pstmt.setString(1, slryClsfc);
            pstmt.setString(2, pymddClsfc);
            pstmt.setString(3, year);
            rs = pstmt.executeQuery();
            while (rs.next()) {
              BPIC= new BasePymntItmNameCodeTO();
               
              BPIC.setCode(rs.getString("CODE"));
              BPIC.setPymmtItmName(rs.getString("PAYMENT_ITEM_NAME"));
               
              pymntDditmList.add(BPIC);
            }
            return pymntDditmList;
         } catch (Exception sqle) {
            throw new DataAccessException(sqle.getMessage());
         } finally {
            dataSourceTransactionManager.close(pstmt, rs);
         }
      }


   @Override
      public ArrayList<PaymentItmOptionRsTO> setPaymentItmNodeOption(PaymentItmOptionTO paymntOtionTO) {
          Connection con = null;
          PreparedStatement pstmt = null;
          ResultSet rs = null;
          PaymentItmOptionRsTO PIOR = null;
          ArrayList<PaymentItmOptionRsTO> paymentItmOptionRs=new ArrayList<PaymentItmOptionRsTO>();
          try {
             con = dataSourceTransactionManager.getConnection();

              StringBuffer query = new StringBuffer();
              String code=paymntOtionTO.getCode();
              
              
            if(code.equals("PYC001") || code.equals("PYC002") || code.equals("PYC003") || code.equals("PYC006") || code.equals("DEC004")) { 
              query.append(" SELECT TAXATION_CLASSIFICATION, "
                    + " TAX_FREE_TYPE, "
                    + " REDUCTION_STATUS, "
                    + " APPRENTICE_APPLICATION, "
                    + " MONTHLY_WAGE, "
                    + " CUT_SELECTION, "
                    + " CLASSIFICATION_STATUS, "
                    + " CALCULATION_CLASSIFICATION, "
                    + " APPLY_LEAVE_ABSENCE, "
                    + " APPLY_PERSON_WHO_LEAVES_OFFICE, "
                    + " EMPLOYMENT_INSURANCE, "
                    + " EXCLS_CLCD, "
                    + " p.PRIORITY, "
                    + " p.PRIORITY_CODE, "
                    + " p.CLSFC_CDCL"
                    + " from PYMNT_DDITM, PRIORITY p ");
              query.append(" WHERE pymnt_dditm.CODE = ? AND PYMNT_DDITM.CODE = p.CODE AND WAGE_CLASSIFICATION = ? AND PYMDD_CLSFC = ? AND YEAR = ? ");
              
              code=paymntOtionTO.getCode();
              String slryClsfc=paymntOtionTO.getSlryClsfc();
              String pymntDdctn=paymntOtionTO.getPymntDdctn();
              String year=paymntOtionTO.getYear();
              
              pstmt = con.prepareStatement(query.toString());
              pstmt.setString(1, code);
              pstmt.setString(2, slryClsfc);
              pstmt.setString(3, pymntDdctn);
              pstmt.setString(4, year);
              rs = pstmt.executeQuery();
                 while (rs.next()) {
                    PIOR= new PaymentItmOptionRsTO();
                     
                    PIOR.setTaxationClassification(rs.getString("TAXATION_CLASSIFICATION"));
                    PIOR.setTaxFreeType(rs.getString("TAX_FREE_TYPE"));
                    PIOR.setReductionStatus(rs.getString("REDUCTION_STATUS"));
                    PIOR.setApprenticeApplication(rs.getString("APPRENTICE_APPLICATION"));
                    PIOR.setMonthlyWage(rs.getString("MONTHLY_WAGE"));
                    PIOR.setCutSelection(rs.getString("CUT_SELECTION"));
                    PIOR.setClassificationStatus(rs.getString("CLASSIFICATION_STATUS"));
                    PIOR.setCalculationClassification(rs.getString("CALCULATION_CLASSIFICATION"));
                    PIOR.setApplyLeaveAbsence(rs.getString("APPLY_LEAVE_ABSENCE"));
                    PIOR.setApplyPersonWhoLeavesOffice(rs.getString("APPLY_PERSON_WHO_LEAVES_OFFICE"));
                    PIOR.setEmploymentInsurance(rs.getString("EMPLOYMENT_INSURANCE"));
                    PIOR.setExclsClcd(rs.getString("EXCLS_CLCD"));
                    PIOR.setPriority(rs.getString("PRIORITY"));
                    PIOR.setPriorityCode(rs.getString("PRIORITY_CODE"));
                    PIOR.setClsfcCdcl(rs.getString("CLSFC_CDCL"));
                    paymentItmOptionRs.add(PIOR);
                 }
              }else {
              
                 query.append(" SELECT TAXATION_CLASSIFICATION, "
                       + " TAX_FREE_TYPE, "
                       + " REDUCTION_STATUS, "
                       + " APPRENTICE_APPLICATION, "
                       + " MONTHLY_WAGE, "
                       + " CUT_SELECTION, "
                       + " CLASSIFICATION_STATUS, "
                       + " CALCULATION_CLASSIFICATION, "
                       + " APPLY_LEAVE_ABSENCE, "
                       + " APPLY_PERSON_WHO_LEAVES_OFFICE, "
                       + " EMPLOYMENT_INSURANCE, "
                       + " EXCLS_CLCD "
                       + " from pymnt_dditm");
                 query.append(" WHERE pymnt_dditm.CODE = ? AND WAGE_CLASSIFICATION = ? AND PYMDD_CLSFC = ? AND YEAR = ? ");
               
                 code=paymntOtionTO.getCode();
                 String slryClsfc=paymntOtionTO.getSlryClsfc();
                 String pymntDdctn=paymntOtionTO.getPymntDdctn();
                 String year=paymntOtionTO.getYear();
                 
                 pstmt = con.prepareStatement(query.toString());
                 pstmt.setString(1, code);
                 pstmt.setString(2, slryClsfc);
                 pstmt.setString(3, pymntDdctn);
                 pstmt.setString(4, year);
                 
                 rs = pstmt.executeQuery();
                 while (rs.next()) {
                    PIOR= new PaymentItmOptionRsTO();
                     
                    PIOR.setTaxationClassification(rs.getString("TAXATION_CLASSIFICATION"));
                    PIOR.setTaxFreeType(rs.getString("TAX_FREE_TYPE"));
                    PIOR.setReductionStatus(rs.getString("REDUCTION_STATUS"));
                    PIOR.setApprenticeApplication(rs.getString("APPRENTICE_APPLICATION"));
                    PIOR.setMonthlyWage(rs.getString("MONTHLY_WAGE"));
                    PIOR.setCutSelection(rs.getString("CUT_SELECTION"));
                    PIOR.setClassificationStatus(rs.getString("CLASSIFICATION_STATUS"));
                    PIOR.setCalculationClassification(rs.getString("CALCULATION_CLASSIFICATION"));
                    PIOR.setApplyLeaveAbsence(rs.getString("APPLY_LEAVE_ABSENCE"));
                    PIOR.setApplyPersonWhoLeavesOffice(rs.getString("APPLY_PERSON_WHO_LEAVES_OFFICE"));
                    PIOR.setEmploymentInsurance(rs.getString("EMPLOYMENT_INSURANCE"));
                    PIOR.setExclsClcd(rs.getString("EXCLS_CLCD"));

                    paymentItmOptionRs.add(PIOR);
                 }

              }
              return paymentItmOptionRs;
          } catch (Exception sqle) {
              throw new DataAccessException(sqle.getMessage());
          } finally {
             dataSourceTransactionManager.close(pstmt, rs);
          }
      }
      
      @Override
      public ArrayList<JobCodeRsTO> setJobCode(JobCodeTO priorityCodeTO) {

            Connection con = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            JobCodeRsTO JCRT = null;
            ArrayList<JobCodeRsTO> jobCodeRs=new ArrayList<JobCodeRsTO>();
            try {
               con = dataSourceTransactionManager.getConnection();

               StringBuffer query = new StringBuffer();
                 
               query.append("SELECT JOB_CODE, CATEGORY_NAME FROM JOB_CODE ");
               query.append("WHERE PRIORITY_CODE = ? AND PAYMENT_ITEM_NAME = ? ");
               
               String priorityCode=priorityCodeTO.getPriorityCode();
               String slryClsfc=priorityCodeTO.getSlryClsfc();
               pstmt = con.prepareStatement(query.toString());
               pstmt.setString(1, priorityCode);
               pstmt.setString(2, slryClsfc);
               rs = pstmt.executeQuery();
               while (rs.next()) {
                 JCRT= new JobCodeRsTO();
                  
                 JCRT.setJobCode(rs.getString("JOB_CODE"));
                 JCRT.setCategoryName(rs.getString("CATEGORY_NAME"));

                 jobCodeRs.add(JCRT);
               }

               return jobCodeRs;
            } catch (Exception sqle) {
               throw new DataAccessException(sqle.getMessage());
            } finally {
               dataSourceTransactionManager.close(pstmt, rs);
            }
         }
      
      @Override
      public ArrayList<DissectionRsTO> setDissection(DissectionTO dissectionData) {
    	  
            Connection con = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            DissectionRsTO DSECTRT = null;
            ArrayList<DissectionRsTO> dissectionRs=new ArrayList<DissectionRsTO>();
            try {
               con = dataSourceTransactionManager.getConnection();

               StringBuffer query = new StringBuffer();
               String num=dissectionData.getNum();
               if(num.equals("1")) {
               query.append("SELECT CATEGORY_CODE, CATEGORY_NAME, CLCLT_CLSFC FROM PRSNL_AFBSC " );
               query.append("WHERE JOB_CODE = ? AND PRIORITY_CODE = ? ");
               
               
               String disecPrioCode=dissectionData.getDisecPrioCode();
               String jobCode=dissectionData.getJobCode();
               
               pstmt = con.prepareStatement(query.toString());
               pstmt.setString(1, jobCode);
               pstmt.setString(2, disecPrioCode);
               rs = pstmt.executeQuery();
               while (rs.next()) {
                  DSECTRT= new DissectionRsTO();
                  
                  DSECTRT.setCategoryCode(rs.getString("CATEGORY_CODE"));
                  DSECTRT.setCategoryName(rs.getString("CATEGORY_NAME"));
                  DSECTRT.setCLCLT_CLSFC(rs.getString("CLCLT_CLSFC"));
                  dissectionRs.add(DSECTRT);
               }
               }else {
                  query.append("SELECT CATEGORY_CODE, CATEGORY_NAME, CLCLT_CLSFC FROM PRSNL_AFBSC " );
                  query.append("WHERE PRIORITY_CODE = ? ");
                  
                  
                  String disecPrioCode=dissectionData.getDisecPrioCode();
                  
                  pstmt = con.prepareStatement(query.toString());
                  pstmt.setString(1, disecPrioCode);
                  rs = pstmt.executeQuery();
                  while (rs.next()) {
                     DSECTRT= new DissectionRsTO();
                     
                     DSECTRT.setCategoryCode(rs.getString("CATEGORY_CODE"));
                     DSECTRT.setCategoryName(rs.getString("CATEGORY_NAME"));
                     DSECTRT.setCLCLT_CLSFC(rs.getString("CLCLT_CLSFC"));
                     dissectionRs.add(DSECTRT);
                  }
               }
               return dissectionRs;
            } catch (Exception sqle) {
               throw new DataAccessException(sqle.getMessage());
            } finally {
               dataSourceTransactionManager.close(pstmt, rs);
            }
         }
      
      @Override
      public ArrayList<AmnclFrmlRsTO> setAmnclFrml(AmnclFrmlTO AFTTO) {

            Connection con = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            AmnclFrmlRsTO AFRTO = null;
            ArrayList<AmnclFrmlRsTO> AFRTOList=new ArrayList<AmnclFrmlRsTO>();
            try {
               con = dataSourceTransactionManager.getConnection();

               StringBuffer query = new StringBuffer();
               
               
               
               
               String num=AFTTO.getNum();
               if(num.equals("1")) {
                  
                  query.append("SELECT DATA FROM AMNCL_FRML " );
                  query.append("WHERE CODE = ? "
                        + "  AND WAGE_CLASSIFICATION = ? "
                        + "  AND PYMDD_CLSFC = ? "
                        + "  AND YEAR = ? ");
                  
                  String code=AFTTO.getCode();
                  String slryClsfc=AFTTO.getSlryClsfc();
                  String pymntDdctn=AFTTO.getPymntDdctn();
                  String year=AFTTO.getYear();
                  pstmt = con.prepareStatement(query.toString());
                  pstmt.setString(1, code);
                  pstmt.setString(2, slryClsfc);
                  pstmt.setString(3, pymntDdctn);
                  pstmt.setString(4, year);
                  
                  rs = pstmt.executeQuery();
                  while (rs.next()) {
                     AFRTO= new AmnclFrmlRsTO();
                     
                     AFRTO.setData(rs.getString("DATA"));
                     AFRTOList.add(AFRTO);
                  }
               }else {
                  if(num.equals("2")) {
                     query.append("SELECT DATA FROM AMNCL_FRML " );
                     query.append("WHERE AMNCL_FRML = ? "
                           + "  AND CODE = ? "
                           + "  AND PRIORITY_CODE = ? "
                           + "  AND WAGE_CLASSIFICATION = ? "
                           + "  AND PYMDD_CLSFC = ? "
                           + "  AND YEAR = ? ");
                     
                     String amnclFrmlCode=AFTTO.getAmnclFrmlCode();
                     String code=AFTTO.getCode();
                     String disecPrioCode=AFTTO.getDisecPrioCode();
                     
                     String slryClsfc=AFTTO.getSlryClsfc();
                     String pymntDdctn=AFTTO.getPymntDdctn();
                     String year=AFTTO.getYear();
                     
                     pstmt = con.prepareStatement(query.toString());
                     pstmt.setString(1, amnclFrmlCode);
                     pstmt.setString(2, code);
                     pstmt.setString(3, disecPrioCode);
                     pstmt.setString(4, slryClsfc);
                     pstmt.setString(5, pymntDdctn);
                     pstmt.setString(6, year);
                     rs = pstmt.executeQuery();
                     while (rs.next()) {
                        AFRTO= new AmnclFrmlRsTO();
                        
                        AFRTO.setData(rs.getString("DATA"));
                        AFRTOList.add(AFRTO);
                     }
                  }else{
                     query.append("SELECT DATA FROM AMNCL_FRML " );
                     query.append("WHERE AMNCL_FRML = ? "
                           + "  AND CODE = ? "
                           + "  AND PRIORITY_CODE = ? "
                           + "  AND JOB_CODE = ? "
                           + "  AND WAGE_CLASSIFICATION = ? "
                           + "  AND PYMDD_CLSFC = ? "
                           + "  AND YEAR = ? ");
                     
                     String amnclFrmlCode=AFTTO.getAmnclFrmlCode();
                     String code=AFTTO.getCode();
                     String disecPrioCode=AFTTO.getDisecPrioCode();
                     String jobCode=AFTTO.getJobCode();
                     String slryClsfc=AFTTO.getSlryClsfc();
                     String pymntDdctn=AFTTO.getPymntDdctn();
                     String year=AFTTO.getYear();

                     pstmt = con.prepareStatement(query.toString());
                     pstmt.setString(1, amnclFrmlCode);
                     pstmt.setString(2, code);
                     pstmt.setString(3, disecPrioCode);
                     pstmt.setString(4, jobCode);
                     pstmt.setString(5, slryClsfc);
                     pstmt.setString(6, pymntDdctn);
                     pstmt.setString(7, year);
                     rs = pstmt.executeQuery();
                     while (rs.next()) {
                        AFRTO= new AmnclFrmlRsTO();
                        
                        AFRTO.setData(rs.getString("DATA"));
                        AFRTOList.add(AFRTO);
                     }
                  }
               }
               return AFRTOList;
            } catch (Exception sqle) {
               throw new DataAccessException(sqle.getMessage());
            } finally {
               dataSourceTransactionManager.close(pstmt, rs);
            }
         }
}