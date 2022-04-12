package kr.co.yooooon.hr.certificate.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;



import kr.co.yooooon.common.exception.DataAccessException;
import kr.co.yooooon.common.transaction.DataSourceTransactionManager;
import kr.co.yooooon.hr.certificate.to.proofTO;

public class ProofCertificateDAOImpl implements  ProofCertificateDAO{
	private DataSourceTransactionManager dataSourceTransactionManager ;
	
	   public void setDataSourceTransactionManager(DataSourceTransactionManager dataSourceTransactionManager) {
		      this.dataSourceTransactionManager = dataSourceTransactionManager;
		   }
	
	@Override
	public void insertProofCertificateRequest(proofTO proof) {
		// TODO Auto-generated method stub
		
		Connection con = null;
		PreparedStatement pstmt = null;	
		
		try {
			System.out.println("@@@!!!!!!!");
			con = dataSourceTransactionManager.getConnection();
			System.out.println("zzzzzzzzzzzzzzzzzzzzzzz@@@!!!!!!!");
			StringBuffer query = new StringBuffer();
			query.append("INSERT INTO PROOF_LIST VALUES (?,?,?,?,?,?,?,?,?,?,sequence_tab1.nextval)");  //�씪�젴踰덊샇

			pstmt = con.prepareStatement(query.toString());
			pstmt.setString(1, proof.getEmpCode());
			pstmt.setString(2, proof.getProofTypeCode());
			pstmt.setString(3, proof.getProofTypeName());
			pstmt.setString(4, proof.getPosition());
			pstmt.setString(5, proof.getDept());
			pstmt.setString(6, proof.getCash());
			pstmt.setString(7, proof.getStartDate());
			pstmt.setString(8, proof.getCause());
			pstmt.setString(9, proof.getReceipt());
			pstmt.setString(10, proof.getApplovalStatus());
			pstmt.executeUpdate();
			
		} catch (Exception sqle) {
			
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(pstmt);
		}		
		
	}
	@Override
	public ArrayList<proofTO> selectProofCertificateList(String empCode,String Code,String startDate, String endDate) { // 利앸튃�꽌瑜� 踰붿쐞 議고쉶
		

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<proofTO> proofLooupList=new ArrayList<proofTO>();
		try {
			con = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			query.append("SELECT E.EMP_NAME,P.PROOF_NAME,P.POSITION,P.DEPT_NAME,P.PROOT_COST");
			query.append(",P.REQUEST_DATE,P.REASON,P.RECEIPT,P.APPLOVALSTATUS");
			query.append(" FROM PROOF_LIST P, EMP E");
			query.append(" WHERE E.EMP_CODE=?");
			query.append(" AND P.PROOF_CODE=?");
			query.append(" AND P.REQUEST_DATE BETWEEN TO_DATE(?,'YYYY-MM-DD') AND TO_DATE(?,'YYYY-MM-DD')");
			
			pstmt = con.prepareStatement(query.toString());
			pstmt.setString(1, empCode);
			pstmt.setString(2,Code);
			pstmt.setString(3, startDate);
			pstmt.setString(4, endDate);
			rs = pstmt.executeQuery();
			proofTO proofList = null;
			while (rs.next()) {
				proofList=new proofTO();
				proofList.setEmpName(rs.getString("EMP_NAME"));
				proofList.setProofTypeName(rs.getString("PROOF_NAME"));
				proofList.setPosition(rs.getString("POSITION"));
				proofList.setDept(rs.getString("DEPT_NAME"));
				proofList.setCash(rs.getString("PROOT_COST"));
				proofList.setStartDate(rs.getString("REQUEST_DATE"));
				proofList.setCause(rs.getString("REASON"));
				proofList.setReceipt(rs.getString("RECEIPT"));
				proofList.setApplovalStatus(rs.getString("APPLOVALSTATUS"));
				
				
				proofLooupList.add(proofList);
				
			}
			
			return proofLooupList;
		} catch (Exception sqle) {
			
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(pstmt, rs);
		}
	}
	@Override
	public void deleteProof(proofTO proof) {
		
		
		Connection con=null;
		PreparedStatement pstmt=null;
		
		try {
			con=dataSourceTransactionManager.getConnection();
			
			StringBuffer query=new StringBuffer();
			query.append("DELETE FROM PROOF_LIST WHERE PROOT_COST=? AND RECEIPT=?");  // 湲덉븸怨� �뙆�씪�솗�옣�옄紐낆쑝濡� �궘�젣�븯�꽕???  �씠嫄� �닔�젙�븘�슂
			
			pstmt=con.prepareStatement(query.toString());
			pstmt.setString(1, proof.getCash());
			pstmt.setString(2, proof.getReceipt());
			pstmt.executeUpdate();
			
		}catch(Exception sqle) {
			
			throw new DataAccessException(sqle.getMessage());
		}finally {
			dataSourceTransactionManager.close(pstmt);
		}		
	}
	@Override
	public ArrayList<proofTO> selectProofListByDept(String deptName, String startDate, String endDate) {
		// TODO Auto-generated method stub
		

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<proofTO> proofReqeustList=new ArrayList<proofTO>();
		try {
			con = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			query.append(" SELECT D.DEPT_NAME, P.EMP_CODE,E.EMP_NAME, P.PROOF_CODE, DD.DETAIL_CODE_NAME, ");
			query.append(" P.REQUEST_DATE, P.APPLOVALSTATUS,P.PROOT_COST,P.REASON,P.RECEIPT ");
			query.append(" FROM PROOF_LIST P, EMP E,DEPT D, DETAIL_CODE DD");
			query.append(" WHERE P.EMP_CODE=E.EMP_CODE(+)");
			query.append(" AND E.DEPT_CODE=D.DEPT_CODE(+)");
			query.append(" AND P.PROOF_CODE=DD.DETAIL_CODE_NUMBER(+)");
			query.append(" AND P.PROOF_NAME=?");
			query.append(" AND REQUEST_DATE BETWEEN TO_DATE(?,'YYYY-MM-DD') AND TO_DATE(?,'YYYY-MM-DD')");
			

			pstmt = con.prepareStatement(query.toString());
			pstmt.setString(1, deptName);
			pstmt.setString(2, startDate);
			pstmt.setString(3, endDate);
			rs = pstmt.executeQuery();
			proofTO proof = null;
			while (rs.next()) {
				proof = new proofTO();
				proof.setDept(rs.getString("DEPT_NAME"));
				proof.setEmpName(rs.getString("EMP_NAME"));
				proof.setEmpCode(rs.getString("EMP_CODE"));
				proof.setProofTypeCode(rs.getString("PROOF_CODE"));
				proof.setProofTypeName(rs.getString("DETAIL_CODE_NAME"));
				proof.setStartDate(rs.getString("REQUEST_DATE"));
				proof.setApplovalStatus(rs.getString("APPLOVALSTATUS"));
				proof.setCash(rs.getString("PROOT_COST"));
				proof.setCause(rs.getString("REASON"));
				proof.setReceipt(rs.getString("RECEIPT"));
				
				
				 proofReqeustList.add(proof);
			}
			
			return proofReqeustList;
		} catch (Exception sqle) {
			
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(pstmt, rs);
		}		
	}
	@Override
	public ArrayList<proofTO> selectProofListByAllDept(String requestDate) {
		// TODO Auto-generated method stub
				

				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				ArrayList<proofTO> proofReqeustList=new ArrayList<proofTO>();
				try {
					con = dataSourceTransactionManager.getConnection();

					StringBuffer query = new StringBuffer();
					query.append(" SELECT D.DEPT_NAME, P.EMP_CODE,E.EMP_NAME, P.PROOF_CODE, DD.DETAIL_CODE_NAME, ");
					query.append(" P.REQUEST_DATE, P.APPLOVALSTATUS,P.PROOT_COST,P.REASON,P.RECEIPT ");
					query.append(" FROM PROOF_LIST P, EMP E,DEPT D, DETAIL_CODE DD");
					query.append(" WHERE P.EMP_CODE=E.EMP_CODE(+)");
					query.append(" AND E.DEPT_CODE=D.DEPT_CODE(+)");
					query.append(" AND P.PROOF_CODE=DD.DETAIL_CODE_NUMBER(+)");
					query.append(" AND REQUEST_DATE =TO_DATE(?,'YYYY-MM-DD')"); //�삤�뒛�궇吏�
					

					pstmt = con.prepareStatement(query.toString());
					pstmt.setString(1, requestDate);
					rs = pstmt.executeQuery();
					proofTO proof = null;
					while (rs.next()) {
						proof = new proofTO();
						proof.setDept(rs.getString("DEPT_NAME"));
						proof.setEmpName(rs.getString("EMP_NAME"));
						proof.setEmpCode(rs.getString("EMP_CODE"));
						proof.setProofTypeCode(rs.getString("PROOF_CODE"));
						proof.setProofTypeName(rs.getString("DETAIL_CODE_NAME"));
						proof.setStartDate(rs.getString("REQUEST_DATE"));
						proof.setApplovalStatus(rs.getString("APPLOVALSTATUS"));
						proof.setCash(rs.getString("PROOT_COST"));
						proof.setCause(rs.getString("REASON"));
						proof.setReceipt(rs.getString("RECEIPT"));
						
						proofReqeustList.add(proof);
					}
					
					return proofReqeustList;
				} catch (Exception sqle) {
					
					throw new DataAccessException(sqle.getMessage());
				} finally {
					dataSourceTransactionManager.close(pstmt, rs);
				}
	}
	@Override
	public void updateProof(proofTO proof) { //利앸튃�듅�씤愿몃━ 
		
		
		Connection con = null;
		PreparedStatement pstmt = null;	
		
		try {
			con = dataSourceTransactionManager.getConnection();
			
			StringBuffer query = new StringBuffer();
			query.append("UPDATE PROOF_LIST SET ");
			query.append("APPLOVALSTATUS = ? ");
			query.append("WHERE EMP_CODE = ? AND REQUEST_DATE = TO_DATE(?,'YYYY-MM-DD') AND PROOT_COST = ?");

			pstmt = con.prepareStatement(query.toString());
			
			  pstmt.setString(1, proof.getApplovalStatus()); 
			  pstmt.setString(2, proof.getEmpCode());
			  pstmt.setString(3, proof.getStartDate());
			  pstmt.setString(4, proof.getCash());
		
			pstmt.executeUpdate();
			
			
		} catch (Exception sqle) {
			
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(pstmt);
		}
		
	}
	@Override
	public void updateProofImg(String cashCode,String proofImg) { //�썝�옒 �뱾�뼱媛붾뜕嫄곗뿉�꽌 �떎�떆 �닔�젙�빐二쇰떎.
		
		Connection con = null;
		PreparedStatement pstmt = null;	
		
		try {
			con = dataSourceTransactionManager.getConnection();
			
			StringBuffer query = new StringBuffer();
			query.append("UPDATE PROOF_LIST SET ");
			query.append("RECEIPT=?");
			query.append("WHERE PROOT_COST = ?");

			pstmt = con.prepareStatement(query.toString());
			
			  pstmt.setString(1, proofImg); //�뙆�씪�솗�옣�옄
			  pstmt.setString(2, cashCode);
			 
		
			pstmt.executeUpdate();
			
			
		} catch (Exception sqle) {
			
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(pstmt);
		}
		
	}

}
