package kr.co.yooooon.hr.attd.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;



import kr.co.yooooon.common.exception.DataAccessException;
import kr.co.yooooon.common.to.ResultTO;
import kr.co.yooooon.common.transaction.DataSourceTransactionManager;
import kr.co.yooooon.hr.attd.to.DayAttdTO;

public class DayAttdDAOImpl implements DayAttdDAO{

	private DataSourceTransactionManager dataSourceTransactionManager ;
	
	   public void setDataSourceTransactionManager(DataSourceTransactionManager dataSourceTransactionManager) {
		      this.dataSourceTransactionManager = dataSourceTransactionManager;
		   }
	
	@Override
	public ArrayList<DayAttdTO> selectDayAttdList(String empCode, String applyDay) {
		

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<DayAttdTO> dayAttdList=new ArrayList<DayAttdTO>(); //�븯猷⑤룞�븞�쓽 紐⑤뱺 異쒓렐 �눜洹� �궗�쇅異� 怨듭쇅異� �벑�벑�쓽 �떊泥�紐⑸줉�쓣 媛��졇�삤湲곕뻹臾몄뿉 list�뿉 �떞�쓬
		try {
			con = dataSourceTransactionManager.getConnection();
			
			StringBuffer query = new StringBuffer();
			query.append("SELECT D.EMP_CODE, E.EMP_NAME, D.DAY_ATTD_CODE, ");
			query.append("TO_CHAR(D.APPLY_DAY, 'YYYY/MM/DD') APPLY_DAY, ");
			query.append("D.ATTD_TYPE_CODE, D.ATTD_TYPE_NAME, D.TIME ");
			query.append("FROM DAY_ATTD D, EMP E WHERE D.EMP_CODE = ? AND D.APPLY_DAY = ? ");
			query.append("AND D.EMP_CODE = E.EMP_CODE ");
			query.append("ORDER BY D.TIME ");  //�떆媛꾩닚�쑝濡� �젙�젹�빐�꽌 �굹�샂 

			pstmt = con.prepareStatement(query.toString());
			pstmt.setString(1, empCode);
			pstmt.setString(2, applyDay);
			rs = pstmt.executeQuery();

			DayAttdTO dayAttd = null;
			while (rs.next()) {
				dayAttd = new DayAttdTO();
				dayAttd.setEmpCode(rs.getString("EMP_CODE"));
				dayAttd.setEmpName(rs.getString("EMP_NAME"));
				dayAttd.setDayAttdCode(rs.getString("DAY_ATTD_CODE"));
				dayAttd.setApplyDay(rs.getString("APPLY_DAY"));
				dayAttd.setAttdTypeCode(rs.getString("ATTD_TYPE_CODE"));
				dayAttd.setAttdTypeName(rs.getString("ATTD_TYPE_NAME"));
				dayAttd.setTime(rs.getString("TIME"));
				dayAttdList.add(dayAttd);
			}
			
			return dayAttdList;
		} catch (Exception sqle) {
		
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(pstmt, rs);
		}
	}

	@Override
	public void insertDayAttd(DayAttdTO dayAttd) {
		// TODO Auto-generated method stub
		
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = dataSourceTransactionManager.getConnection();
			
			StringBuffer query = new StringBuffer();
			query.append("INSERT INTO DAY_ATTD VALUES (?, DAY_ATTD_CODE_SEQ.NEXTVAL, ?, ?, ?, ?)");

			pstmt = con.prepareStatement(query.toString());
			pstmt.setString(1, dayAttd.getEmpCode());
			pstmt.setString(2, dayAttd.getApplyDay());
			pstmt.setString(3, dayAttd.getAttdTypeCode());
			pstmt.setString(4, dayAttd.getAttdTypeName());
			pstmt.setString(5, dayAttd.getTime());
			pstmt.executeUpdate();
			
		} catch (Exception sqle) {
			
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(pstmt);
		}
		
	}
	
	@Override
	public void deleteDayAttd(DayAttdTO dayAttd) {
	

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			query.append("DELETE FROM DAY_ATTD WHERE EMP_CODE = ? AND DAY_ATTD_CODE = ?");  // ??臾몄젣�엳�쓣�벏

			pstmt = con.prepareStatement(query.toString());
			pstmt.setString(1, dayAttd.getEmpCode());
			pstmt.setString(2, dayAttd.getDayAttdCode());
			pstmt.executeUpdate();
			
		} catch (Exception sqle) {
			
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(pstmt);
		}
		
	}
	@Override
	public ResultTO batchInsertDayAttd(DayAttdTO dayAttd) { // �꽌�썝硫붾돱�뿉�꽌 �씪洹쇳깭 湲곕줉 �븯湲�
		 
	      Connection con = null;
	      CallableStatement cstmt = null; //�봽猶곗� 
	      ResultTO resultTO = null; // �뿉�윭硫붿꽭吏� 愿�由� �뀒�씠釉�

	      try {
	         con = dataSourceTransactionManager.getConnection();
	         StringBuffer query = new StringBuffer();
	         query.append("{call P_INSERT_DAY_ATTD(?,DAY_ATTD_CODE_SEQ.NEXTVAL,?,?,?,?,?,?)}");
	         cstmt = con.prepareCall(query.toString());
	        	
	         cstmt.setString(1, dayAttd.getEmpCode());//"�씪洹쇳깭 �궗�썝踰덊샇 : "
	         cstmt.setString(2, dayAttd.getAttdTypeCode()); //"�씪洹쇳깭 洹쇰Т 肄붾뱶 : "
	         cstmt.setString(3, dayAttd.getAttdTypeName());//"�씪洹쇳깭 洹쇰Т �긽�깭 : "
	         cstmt.setString(4, dayAttd.getApplyDay());//"�삤�뒛�궇吏�"
	         cstmt.setString(5, dayAttd.getTime());//"�궗�썝�씠 異� , �눜洹쇳븳 �떆媛� : "
	         cstmt.registerOutParameter(6,java.sql.Types.VARCHAR); // 異쒕젰留ㅺ쾶 蹂��닔 諛쏆쓣�뻹 < 利� �뿉�윭�궗�쓣�븣>  Return Data type
	         cstmt.registerOutParameter(7,java.sql.Types.VARCHAR);	         
	         cstmt.execute();	//而ㅻ━�떎�뻾         
	         
	         resultTO = new ResultTO();
			 resultTO.setErrorCode(cstmt.getString(6));
			 resultTO.setErrorMsg(cstmt.getString(7));			 
	         
	      
			
	         return resultTO;	       
	      } catch (Exception sqle) {
	    	
	    	
	       
	        
	         throw new DataAccessException(sqle.getMessage());
	         
	      } finally {
	         dataSourceTransactionManager.close(cstmt);
	      }
	}
}
