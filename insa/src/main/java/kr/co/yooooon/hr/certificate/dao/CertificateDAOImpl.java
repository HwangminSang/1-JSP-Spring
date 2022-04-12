package kr.co.yooooon.hr.certificate.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;



import kr.co.yooooon.common.exception.DataAccessException;
import kr.co.yooooon.common.transaction.DataSourceTransactionManager;
import kr.co.yooooon.hr.certificate.to.CertificateTO;

public class CertificateDAOImpl implements CertificateDAO {
	private DataSourceTransactionManager dataSourceTransactionManager ;
	
	   public void setDataSourceTransactionManager(DataSourceTransactionManager dataSourceTransactionManager) {
		      this.dataSourceTransactionManager = dataSourceTransactionManager;
		   }
	
	@Override
	public void insertCertificateRequest(CertificateTO certificate) {
		// TODO Auto-generated method stub
		
		
		Connection con = null;
		PreparedStatement pstmt = null;	
		
		try {
			con = dataSourceTransactionManager.getConnection();
			
			StringBuffer query = new StringBuffer();
			query.append("INSERT INTO CERTIFICATE VALUES (?,?,?,?,?,null,?)");

			pstmt = con.prepareStatement(query.toString());
			pstmt.setString(1, certificate.getEmpCode());
			pstmt.setString(2, certificate.getUsageCode());	
			pstmt.setString(3, certificate.getRequestDate());
			pstmt.setString(4, certificate.getUseDate());					
			pstmt.setString(5, certificate.getApprovalStatus());
			pstmt.setString(6, certificate.getEtc());
			
			pstmt.executeUpdate();
			
		} catch (Exception sqle) {
			
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(pstmt);
		}		
		
	}
	@Override
	public ArrayList<CertificateTO> selectCertificateList(String empCode, String startDate, String endDate) {
		

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<CertificateTO> certificateList=new ArrayList<CertificateTO>();
		try {
			con = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			query.append("SELECT C.EMP_CODE, C.USAGE_CODE, D.DETAIL_CODE_NAME");
			query.append(",TO_CHAR(C.REQUEST_DATE, 'YYYY-MM-DD') REQUEST_DATE");
			query.append(",TO_CHAR(C.USE_DATE,'YYYY-MM-DD') USE_DATE");
			query.append(",C.APPROVAL_STATUS, C.REJECT_CAUSE, C.ETC");
			query.append(" FROM CERTIFICATE C, DETAIL_CODE D");
			query.append(" WHERE C.USAGE_CODE=D.DETAIL_CODE_NUMBER");
			query.append(" AND C.EMP_CODE=?");
			query.append(" AND C.REQUEST_DATE BETWEEN TO_DATE(?,'YYYY-MM-DD') AND TO_DATE(?,'YYYY-MM-DD')");
			
			pstmt = con.prepareStatement(query.toString());
			pstmt.setString(1, empCode);
			pstmt.setString(2, startDate);
			pstmt.setString(3, endDate);
			rs = pstmt.executeQuery();
			CertificateTO certificate = null;
			while (rs.next()) {
				certificate=new CertificateTO();
				certificate.setEmpCode(rs.getString("EMP_CODE"));
				certificate.setUsageName(rs.getString("DETAIL_CODE_NAME"));
				certificate.setRequestDate(rs.getString("REQUEST_DATE"));
				certificate.setUseDate(rs.getString("USE_DATE"));
				certificate.setApprovalStatus(rs.getString("APPROVAL_STATUS"));
				certificate.setRejectCause(rs.getString("REJECT_CAUSE"));
				certificate.setEtc(rs.getString("ETC"));
				
				certificateList.add(certificate);
				System.out.println("�뀋�뀋�뀋�뀋�뀋�뀋�뀋"+certificate.getUsageName());
			}
			
			return certificateList;
		} catch (Exception sqle) {
			
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(pstmt, rs);
		}
	}
	@Override
	public void deleteCertificate(CertificateTO certificate) {
		
		Connection con=null;
		PreparedStatement pstmt=null;
		
		try {
			con=dataSourceTransactionManager.getConnection();
			
			StringBuffer query=new StringBuffer();
			query.append("DELETE FROM CERTIFICATE WHERE EMP_CODE=? AND REQUEST_DATE=TO_DATE(?,'YYYY-MM-DD')");
			
			pstmt=con.prepareStatement(query.toString());
			pstmt.setString(1, certificate.getEmpCode());
			pstmt.setString(2, certificate.getRequestDate());
			pstmt.executeUpdate();
			
		
		}catch(Exception sqle) {
		
			throw new DataAccessException(sqle.getMessage());
		}finally {
			dataSourceTransactionManager.close(pstmt);
		}		
	}
	@Override
	public ArrayList<CertificateTO> selectCertificateListByAllDept(String requestDate) {
		// TODO Auto-generated method stub
				

				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				ArrayList<CertificateTO> certificateReqeustList=new ArrayList<CertificateTO>();
				try {
					con = dataSourceTransactionManager.getConnection();

					StringBuffer query = new StringBuffer();
					query.append("SELECT D.DEPT_NAME, C.EMP_CODE, E.EMP_NAME, C.USAGE_CODE, DD.DETAIL_CODE_NAME, ");
					query.append("TO_CHAR(C.REQUEST_DATE, 'YYYY-MM-DD') REQUEST_DATE, ");
					query.append("TO_CHAR(C.USE_DATE, 'YYYY-MM-DD') USE_DATE, ");
					query.append("C.APPROVAL_STATUS, C.REJECT_CAUSE, C.ETC");
					query.append(" FROM CERTIFICATE C, EMP E,DEPT D, DETAIL_CODE DD");
					query.append(" WHERE C.EMP_CODE=E.EMP_CODE(+)");
					query.append(" AND E.DEPT_CODE=D.DEPT_CODE(+)");
					query.append(" AND C.USAGE_CODE=DD.DETAIL_CODE_NUMBER(+)");
					query.append(" AND REQUEST_DATE=TO_DATE(?,'yyyy-mm-dd')");
					

					pstmt = con.prepareStatement(query.toString());
					pstmt.setString(1, requestDate);
					rs = pstmt.executeQuery();
					CertificateTO certificate = null;
					while (rs.next()) {
						certificate = new CertificateTO();
						certificate.setDeptName(rs.getString("DEPT_NAME"));
						certificate.setEmpName(rs.getString("EMP_NAME"));
						certificate.setEmpCode(rs.getString("EMP_CODE"));
						certificate.setUsageName(rs.getString("DETAIL_CODE_NAME"));
						certificate.setRequestDate(rs.getString("REQUEST_DATE"));
						certificate.setUseDate(rs.getString("USE_DATE"));
						certificate.setApprovalStatus(rs.getString("APPROVAL_STATUS"));
						certificate.setRejectCause(rs.getString("REJECT_CAUSE"));
						certificate.setEtc(rs.getString("ETC"));
						
						certificateReqeustList.add(certificate);
					}
				
					return certificateReqeustList;
				} catch (Exception sqle) {
				
					throw new DataAccessException(sqle.getMessage());
				} finally {
					dataSourceTransactionManager.close(pstmt, rs);
				}
	}
	@Override
	public ArrayList<CertificateTO> selectCertificateListByDept(String deptName, String startDate, String endDate) {
		// TODO Auto-generated method stub
		

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<CertificateTO> certificateReqeustList=new ArrayList<CertificateTO>();
		try {
			con = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			query.append("SELECT D.DEPT_NAME, C.EMP_CODE,E.EMP_NAME, C.USAGE_CODE, DD.DETAIL_CODE_NAME, ");
			query.append("TO_CHAR(C.REQUEST_DATE, 'YYYY-MM-DD') REQUEST_DATE, ");
			query.append("TO_CHAR(C.USE_DATE, 'YYYY-MM-DD') USE_DATE, ");
			query.append("C.APPROVAL_STATUS, C.REJECT_CAUSE, C.ETC");
			query.append(" FROM CERTIFICATE C, EMP E,DEPT D, DETAIL_CODE DD");
			query.append(" WHERE C.EMP_CODE=E.EMP_CODE(+)");
			query.append(" AND E.DEPT_CODE=D.DEPT_CODE(+)");
			query.append(" AND C.USAGE_CODE=DD.DETAIL_CODE_NUMBER(+)");  //'ELM004' 湲고��젣異쒖슜
			query.append(" AND D.DEPT_NAME=?");
			query.append(" AND REQUEST_DATE BETWEEN TO_DATE(?,'YYYY-MM-DD') AND TO_DATE(?,'YYYY-MM-DD')");
			

			pstmt = con.prepareStatement(query.toString());
			pstmt.setString(1, deptName);
			pstmt.setString(2, startDate);
			pstmt.setString(3, endDate);
			rs = pstmt.executeQuery();
			CertificateTO certificate = null;
			while (rs.next()) {
				certificate = new CertificateTO();
				certificate.setDeptName(rs.getString("DEPT_NAME"));
				certificate.setEmpName(rs.getString("EMP_NAME"));
				certificate.setEmpCode(rs.getString("EMP_CODE"));
				certificate.setUsageName(rs.getString("DETAIL_CODE_NAME"));
				certificate.setRequestDate(rs.getString("REQUEST_DATE"));
				certificate.setUseDate(rs.getString("USE_DATE"));
				certificate.setApprovalStatus(rs.getString("APPROVAL_STATUS"));
				certificate.setRejectCause(rs.getString("REJECT_CAUSE"));
				certificate.setEtc(rs.getString("ETC"));
				
				certificateReqeustList.add(certificate);
			}
			
			return certificateReqeustList;
		} catch (Exception sqle) {
			
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(pstmt, rs);
		}		
	}
	@Override
	public void updateCertificate(CertificateTO certificate) {
		
		
		Connection con = null;
		PreparedStatement pstmt = null;	
		
		try {
			con = dataSourceTransactionManager.getConnection();
			
			StringBuffer query = new StringBuffer(); 
			query.append("UPDATE CERTIFICATE SET ");
			query.append("APPROVAL_STATUS=?,  REJECT_CAUSE=? ");
			query.append("WHERE EMP_CODE = ? AND REQUEST_DATE = TO_DATE(?,'YYYY-MM-DD')");

			pstmt = con.prepareStatement(query.toString());
			
			  pstmt.setString(1, certificate.getApprovalStatus()); 
			  pstmt.setString(2, certificate.getRejectCause()); 
			  pstmt.setString(3, certificate.getEmpCode());
			  pstmt.setString(4, certificate.getRequestDate());		 			  
			System.out.println(certificate.getApprovalStatus()+certificate.getRejectCause()
			+certificate.getEmpCode()+certificate.getRequestDate());
			pstmt.executeUpdate();
			
			
		} catch (Exception sqle) {
		
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(pstmt);
		}
		
	}
	
}
