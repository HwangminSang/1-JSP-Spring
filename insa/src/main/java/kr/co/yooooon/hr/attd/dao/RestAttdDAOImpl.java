package kr.co.yooooon.hr.attd.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import kr.co.yooooon.common.exception.DataAccessException;
import kr.co.yooooon.common.transaction.DataSourceTransactionManager;
import kr.co.yooooon.hr.attd.to.RestAttdTO;

public class RestAttdDAOImpl implements RestAttdDAO{
	private DataSourceTransactionManager dataSourceTransactionManager ;
	
	   public void setDataSourceTransactionManager(DataSourceTransactionManager dataSourceTransactionManager) {
		      this.dataSourceTransactionManager = dataSourceTransactionManager;
		   }
	@Override
	public ArrayList<RestAttdTO> selectRestAttdListByToday(String empCode, String toDay) {
		

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<RestAttdTO> restAttdList=new ArrayList<RestAttdTO>();
		try {
			con = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			query.append("SELECT R.EMP_CODE, E.EMP_NAME, R.REST_ATTD_CODE, ");
			query.append("R.REST_TYPE_CODE, R.REST_TYPE_NAME, ");
			query.append("TO_CHAR(R.REQUEST_DATE, 'YYYY-MM-DD') REQUEST_DATE, ");
			query.append("TO_CHAR(R.START_DATE, 'YYYY-MM-DD') START_DATE, ");
			query.append("TO_CHAR(R.END_DATE, 'YYYY-MM-DD') END_DATE, ");
			query.append("R.NUMBER_OF_DAYS, R.COST, R.CAUSE, R.APPLOVAL_STATUS, ");
			query.append("R.REJECT_CAUSE, R.START_TIME,R.END_TIME ");
			query.append("FROM REST_ATTD R, EMP E WHERE R.EMP_CODE = ? AND R.START_DATE = to_DATE(?,'YYYY-MM-DD') ");
			query.append("AND R.END_DATE = to_DATE(?,'YYYY-MM-DD') ");
			query.append("AND R.APPLOVAL_STATUS = '�듅�씤' ");
			query.append("AND R.REST_TYPE_CODE <> 'ASC008' ");
			query.append("AND R.EMP_CODE = E.EMP_CODE ");

			
			pstmt = con.prepareStatement(query.toString());
			pstmt.setString(1, empCode);
			pstmt.setString(2, toDay);
			pstmt.setString(3, toDay);
			rs = pstmt.executeQuery();
			RestAttdTO restAttd = null;
			while (rs.next()) {
				restAttd = new RestAttdTO();
				restAttd.setEmpCode(rs.getString("EMP_CODE"));
				restAttd.setEmpName(rs.getString("EMP_NAME"));
				restAttd.setRestAttdCode(rs.getString("REST_ATTD_CODE"));
				restAttd.setRestTypeCode(rs.getString("REST_TYPE_CODE"));
				restAttd.setRestTypeName(rs.getString("REST_TYPE_NAME"));
				restAttd.setRequestDate(rs.getString("REQUEST_DATE"));
				restAttd.setStartDate(rs.getString("START_DATE"));
				restAttd.setEndDate(rs.getString("END_DATE"));
				restAttd.setNumberOfDays(rs.getString("NUMBER_OF_DAYS"));
				restAttd.setCost(rs.getString("COST"));
				restAttd.setCause(rs.getString("CAUSE"));
				restAttd.setApplovalStatus(rs.getString("APPLOVAL_STATUS"));
				restAttd.setRejectCause(rs.getString("REJECT_CAUSE"));
				restAttd.setStartTime(rs.getString("START_TIME"));
				restAttd.setEndTime(rs.getString("END_TIME"));
				
				restAttdList.add(restAttd);
			}
			
			return restAttdList;
		} catch (Exception sqle) {
			
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(pstmt, rs);
		}
	}
	
	@Override
	public ArrayList<RestAttdTO> selectRestAttdList(String empCode, String startDate, String endDate) {
		// TODO Auto-generated method stub
	

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<RestAttdTO> restAttdList=new ArrayList<RestAttdTO>();
		try {
			con = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			query.append("SELECT R.EMP_CODE, E.EMP_NAME, R.REST_ATTD_CODE, ");
			query.append("R.REST_TYPE_CODE, R.REST_TYPE_NAME, ");
			query.append("TO_CHAR(R.REQUEST_DATE, 'YYYY-MM-DD') REQUEST_DATE, ");
			query.append("TO_CHAR(R.START_DATE, 'YYYY-MM-DD') START_DATE, ");
			query.append("TO_CHAR(R.END_DATE, 'YYYY-MM-DD') END_DATE, ");
			query.append("R.NUMBER_OF_DAYS, R.COST, R.CAUSE, R.APPLOVAL_STATUS, ");
			query.append("R.REJECT_CAUSE, R.START_TIME,  ");
			query.append("CASE WHEN R.NUMBER_OF_DAYS != 1 THEN R.END_TIME - 2400 ELSE R.END_TIME END AS END_TIME ");
			query.append("FROM REST_ATTD R, EMP E WHERE R.EMP_CODE = ? AND R.REQUEST_DATE BETWEEN TO_DATE(?,'YYYY-MM-DD') AND TO_DATE(?,'YYYY-MM-DD') ");
			query.append("AND R.EMP_CODE = E.EMP_CODE ");

			pstmt = con.prepareStatement(query.toString());
			pstmt.setString(1, empCode);
			pstmt.setString(2, startDate);
			pstmt.setString(3, endDate);
			rs = pstmt.executeQuery();
			RestAttdTO restAttd = null;
			while (rs.next()) {
				restAttd = new RestAttdTO();
				restAttd.setEmpCode(rs.getString("EMP_CODE"));
				restAttd.setEmpName(rs.getString("EMP_NAME"));
				restAttd.setRestAttdCode(rs.getString("REST_ATTD_CODE"));
				restAttd.setRestTypeCode(rs.getString("REST_TYPE_CODE"));
				restAttd.setRestTypeName(rs.getString("REST_TYPE_NAME"));
				restAttd.setRequestDate(rs.getString("REQUEST_DATE"));
				restAttd.setStartDate(rs.getString("START_DATE"));
				restAttd.setEndDate(rs.getString("END_DATE"));
				restAttd.setNumberOfDays(rs.getString("NUMBER_OF_DAYS"));
				restAttd.setCost(rs.getString("COST"));
				restAttd.setCause(rs.getString("CAUSE"));
				restAttd.setApplovalStatus(rs.getString("APPLOVAL_STATUS"));
				restAttd.setRejectCause(rs.getString("REJECT_CAUSE"));
				restAttd.setStartTime(rs.getString("START_TIME"));
				restAttd.setEndTime(rs.getString("END_TIME"));
				restAttdList.add(restAttd);
			}
			
			return restAttdList;
		} catch (Exception sqle) {
			
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(pstmt, rs);
		}
	}
	
	@Override
	public void insertRestAttd(RestAttdTO restAttd) {
		
		

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			query.append("INSERT INTO REST_ATTD VALUES (?,REST_ATTD_CODE_SEQ.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?,?)");

			pstmt = con.prepareStatement(query.toString());
			pstmt.setString(1, restAttd.getEmpCode());
			pstmt.setString(2, restAttd.getRestTypeCode());
			pstmt.setString(3, restAttd.getRestTypeName());
			pstmt.setString(4, restAttd.getRequestDate()); //�슂泥��궇吏�
			pstmt.setString(5, restAttd.getStartDate());
			pstmt.setString(6, restAttd.getEndDate());
			pstmt.setString(7, restAttd.getCause());
			pstmt.setString(8, restAttd.getApplovalStatus()); //�듅�씤�긽�깭 泥섏쓬�뿉�뒗   �듅�씤��湲� !!!!!
			pstmt.setString(9, restAttd.getRejectCause());
			pstmt.setString(10, restAttd.getCost()); // 鍮꾩슜�쟻�뒗怨녹씠 �뾾�뼱�꽌 �븘臾닿쾬�룄 �뾾�뒗 �긽�깭�엫.
			pstmt.setString(11, restAttd.getStartTime());
			pstmt.setString(12, restAttd.getEndTime());
			pstmt.setString(13, restAttd.getNumberOfDays());
			pstmt.executeUpdate();  // �떎�뻾 
			
		} catch (Exception sqle) {
			
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(pstmt);
		}		
	}
	
	@Override
	public ArrayList<RestAttdTO> selectRestAttdListCode(String empCode, String startDate, String endDate, String code) {
		// TODO Auto-generated method stub
		

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<RestAttdTO> restAttdList=new ArrayList<RestAttdTO>();
		try {
			con = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			query.append("SELECT R.EMP_CODE, E.EMP_NAME, R.REST_ATTD_CODE, ");
			query.append("R.REST_TYPE_CODE, R.REST_TYPE_NAME, ");
			query.append("TO_CHAR(R.REQUEST_DATE, 'YYYY-MM-DD') REQUEST_DATE, ");
			query.append("TO_CHAR(R.START_DATE, 'YYYY-MM-DD') START_DATE, ");
			query.append("TO_CHAR(R.END_DATE, 'YYYY-MM-DD') END_DATE, ");
			query.append("R.NUMBER_OF_DAYS, R.COST, R.CAUSE, R.APPLOVAL_STATUS, ");
			query.append("R.REJECT_CAUSE, R.START_TIME,  "); //�븘�옒�뒗 珥덇낵洹쇰Т以묒뿉 2 �씠�긽�씤寃쎌슦  利� �떎�쓬�궇源뚯� �씪�뻼�쓣寃쎌슦   
			query.append("CASE WHEN R.NUMBER_OF_DAYS != 1 AND REST_TYPE_CODE = 'ASC008' THEN R.END_TIME - 2400 ELSE R.END_TIME END AS END_TIME "); // REST ATTD �뿉�꽌 怨꾩궛 �맂 寃곌낵媛믩뱾�씠 �깉踰� �븳 �떆媛� �꽆�쑝硫� 2500 �쑝濡� 怨꾩궛�릺�뒗 寃쎌슦媛� �엳�떎 , 洹� 寃쎌슦 �뿰�궛泥섎━瑜� 媛꾨떒�븯寃� �븯湲� �쐞�빐�꽌 泥섎━�븯�뒗 寃� 
			query.append("FROM REST_ATTD R, EMP E WHERE R.EMP_CODE = ? AND R.REQUEST_DATE BETWEEN TO_DATE(?,'YYYY-MM-DD') AND TO_DATE(?,'YYYY-MM-DD') ");
			query.append("AND R.REST_TYPE_CODE = ? AND R.EMP_CODE = E.EMP_CODE ");

		
			pstmt = con.prepareStatement(query.toString());
			pstmt.setString(1, empCode);
			pstmt.setString(2, startDate);
			pstmt.setString(3, endDate);
			pstmt.setString(4, code);
			rs = pstmt.executeQuery(); // 寃곌낵�뀑 
			RestAttdTO restAttd = null;
			while (rs.next()) {
				restAttd = new RestAttdTO();
				restAttd.setEmpCode(rs.getString("EMP_CODE"));
				restAttd.setEmpName(rs.getString("EMP_NAME"));
				restAttd.setRestAttdCode(rs.getString("REST_ATTD_CODE"));
				restAttd.setRestTypeCode(rs.getString("REST_TYPE_CODE"));
				restAttd.setRestTypeName(rs.getString("REST_TYPE_NAME"));
				restAttd.setRequestDate(rs.getString("REQUEST_DATE"));
				restAttd.setStartDate(rs.getString("START_DATE"));
				restAttd.setEndDate(rs.getString("END_DATE"));
				restAttd.setNumberOfDays(rs.getString("NUMBER_OF_DAYS"));
				restAttd.setCost(rs.getString("COST"));
				restAttd.setCause(rs.getString("CAUSE"));
				restAttd.setApplovalStatus(rs.getString("APPLOVAL_STATUS"));
				restAttd.setRejectCause(rs.getString("REJECT_CAUSE"));
				restAttd.setStartTime(rs.getString("START_TIME"));
				restAttd.setEndTime(rs.getString("END_TIME"));
				restAttdList.add(restAttd);
			}
			
			return restAttdList;
		} catch (Exception sqle) {
			
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(pstmt, rs);
		}

	}

	@Override
	public ArrayList<RestAttdTO> selectRestAttdListByDept(String deptName, String startDate, String endDate) {
		// TODO Auto-generated method stub
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<RestAttdTO> restAttdList=new ArrayList<RestAttdTO>();
		try {
			con = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			query.append("SELECT R.EMP_CODE, E.EMP_NAME, R.REST_ATTD_CODE, ");
			query.append("R.REST_TYPE_CODE, R.REST_TYPE_NAME, ");
			query.append("TO_CHAR(R.REQUEST_DATE, 'YYYY-MM-DD') REQUEST_DATE, ");
			query.append("TO_CHAR(R.START_DATE, 'YYYY-MM-DD') START_DATE, ");
			query.append("TO_CHAR(R.END_DATE, 'YYYY-MM-DD') END_DATE, ");
			query.append("R.NUMBER_OF_DAYS, R.COST, R.CAUSE, R.APPLOVAL_STATUS, ");
			query.append("R.REJECT_CAUSE, R.START_TIME,");
	         query.append("CASE WHEN R.NUMBER_OF_DAYS != 1 AND REST_TYPE_CODE = 'ASC008' THEN R.END_TIME - 2400 ELSE R.END_TIME END AS END_TIME ");
			query.append(" FROM REST_ATTD R, EMP E");
			query.append(" WHERE E.DEPT_CODE = ?");
			query.append(" AND R.REQUEST_DATE BETWEEN TO_DATE(?,'YYYY-MM-DD') AND TO_DATE(?,'YYYY-MM-DD')");
			query.append(" AND R.EMP_CODE = E.EMP_CODE ");
			

			pstmt = con.prepareStatement(query.toString());
			pstmt.setString(1, deptName);
			pstmt.setString(2, startDate);
			pstmt.setString(3, endDate);
			rs = pstmt.executeQuery();
			RestAttdTO restAttd = null;
			while (rs.next()) {
				restAttd = new RestAttdTO();
				restAttd.setEmpCode(rs.getString("EMP_CODE"));
				restAttd.setEmpName(rs.getString("EMP_NAME"));
				restAttd.setRestAttdCode(rs.getString("REST_ATTD_CODE"));
				restAttd.setRestTypeCode(rs.getString("REST_TYPE_CODE"));
				restAttd.setRestTypeName(rs.getString("REST_TYPE_NAME"));
				restAttd.setRequestDate(rs.getString("REQUEST_DATE"));
				restAttd.setStartDate(rs.getString("START_DATE"));
				restAttd.setEndDate(rs.getString("END_DATE"));
				restAttd.setNumberOfDays(rs.getString("NUMBER_OF_DAYS"));
				restAttd.setCost(rs.getString("COST"));
				restAttd.setCause(rs.getString("CAUSE"));
				restAttd.setApplovalStatus(rs.getString("APPLOVAL_STATUS"));
				restAttd.setRejectCause(rs.getString("REJECT_CAUSE"));
				restAttd.setStartTime(rs.getString("START_TIME"));
				restAttd.setEndTime(rs.getString("END_TIME"));
				restAttdList.add(restAttd);
			}
			
			return restAttdList;
		} catch (Exception sqle) {
		
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(pstmt, rs);
		}
	}
	
	@Override
	public ArrayList<RestAttdTO> selectRestAttdListByAllDept(String applyDay) { // 寃곗젣�듅�씤 議고쉶�븯湲� 泥ロ솕硫댁뿉 �씠嫄� �샇異쒗븳�떎. deptname=�쟾泥대��꽌濡� �븯�뿬 �븵�뿉 application�뿉�꽌 �궇吏쒖꽕�젙�빐�꽌 �삤�뒗�븷�� 援щ텇 
		// TODO Auto-generated method stub
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<RestAttdTO> restAttdList=new ArrayList<RestAttdTO>();
		try {
			con = dataSourceTransactionManager.getConnection(); // 而⑤꽖�뀡 罹묒껜瑜� 鍮쇱삩�떎 

			StringBuffer query = new StringBuffer();
			query.append("SELECT R.EMP_CODE, E.EMP_NAME, R.REST_ATTD_CODE, ");
			query.append("R.REST_TYPE_CODE, R.REST_TYPE_NAME, ");
			query.append("TO_CHAR(R.REQUEST_DATE, 'YYYY-MM-DD') REQUEST_DATE, ");
			query.append("TO_CHAR(R.START_DATE, 'YYYY-MM-DD') START_DATE, ");
			query.append("TO_CHAR(R.END_DATE, 'YYYY-MM-DD') END_DATE, ");
			query.append("R.NUMBER_OF_DAYS, R.COST, R.CAUSE, R.APPLOVAL_STATUS, ");
			query.append("R.REJECT_CAUSE, R.START_TIME, ");
			query.append("CASE WHEN R.NUMBER_OF_DAYS != 1 AND REST_TYPE_CODE = 'ASC008' THEN R.END_TIME - 2400 ELSE R.END_TIME END AS END_TIME ");  //珥덇낵洹쇰Т �떎�쓬�궇源뚯� �뻽�쓣寃쎌슦 
			query.append("FROM REST_ATTD R, EMP E WHERE R.REQUEST_DATE = TO_DATE(?,'YYYY-MM-DD') ");  //�떊泥��궇吏쒓� �떦�씪濡� 媛��졇�샂
			query.append("AND R.EMP_CODE = E.EMP_CODE ");

			pstmt = con.prepareStatement(query.toString());
			pstmt.setString(1, applyDay);
			rs = pstmt.executeQuery();
			RestAttdTO restAttd = null;
			while (rs.next()) {
				restAttd = new RestAttdTO();
				restAttd.setEmpCode(rs.getString("EMP_CODE"));
				restAttd.setEmpName(rs.getString("EMP_NAME"));
				restAttd.setRestAttdCode(rs.getString("REST_ATTD_CODE"));
				restAttd.setRestTypeCode(rs.getString("REST_TYPE_CODE"));
				restAttd.setRestTypeName(rs.getString("REST_TYPE_NAME"));
				restAttd.setRequestDate(rs.getString("REQUEST_DATE"));
				restAttd.setStartDate(rs.getString("START_DATE"));
				restAttd.setEndDate(rs.getString("END_DATE"));
				restAttd.setNumberOfDays(rs.getString("NUMBER_OF_DAYS"));
				restAttd.setCost(rs.getString("COST"));
				restAttd.setCause(rs.getString("CAUSE"));
				restAttd.setApplovalStatus(rs.getString("APPLOVAL_STATUS"));
				restAttd.setRejectCause(rs.getString("REJECT_CAUSE"));
				restAttd.setStartTime(rs.getString("START_TIME"));
				restAttd.setEndTime(rs.getString("END_TIME"));
				restAttdList.add(restAttd);
			}
			
			return restAttdList;
		} catch (Exception sqle) {
			
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(pstmt, rs);
		}
	}

	@Override
	public void updateRestAttd(RestAttdTO restAttd) {
		// TODO Auto-generated method stub
		
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			query.append("UPDATE REST_ATTD SET ");
			query.append("CAUSE = ?, APPLOVAL_STATUS = ?, REJECT_CAUSE = ? ");
			query.append("WHERE EMP_CODE = ? AND REST_ATTD_CODE = ?");

			pstmt = con.prepareStatement(query.toString());
			pstmt.setString(1, restAttd.getCause());
			pstmt.setString(2, restAttd.getApplovalStatus());
			pstmt.setString(3, restAttd.getRejectCause());
			pstmt.setString(4, restAttd.getEmpCode());
			pstmt.setString(5, restAttd.getRestAttdCode());
			pstmt.executeUpdate();
			
		} catch (Exception sqle) {
			
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(pstmt);
		}		
	}
	
	@Override
	public void deleteRestAttd(RestAttdTO restAttd) {
		// TODO Auto-generated method stub
		
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			System.out.println(restAttd.getEmpCode());
			System.out.println(restAttd.getRestAttdCode());
			con = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			query.append("DELETE FROM REST_ATTD WHERE EMP_CODE = ? AND REST_ATTD_CODE = ?");  //REST_ATTD_CODE �떆�몄뒪 �씪�젴踰덊샇. 利� 寃뱀튂寃� �뾾�뜑.

			pstmt = con.prepareStatement(query.toString());
			pstmt.setString(1, restAttd.getEmpCode());
			pstmt.setString(2, restAttd.getRestAttdCode());
			pstmt.executeUpdate();
			
		} catch (Exception sqle) {
			
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(pstmt);
		}
				
	}
	
	
}
