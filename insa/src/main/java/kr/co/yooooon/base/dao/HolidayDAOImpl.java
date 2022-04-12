package kr.co.yooooon.base.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import kr.co.yooooon.base.dao.HolidayDAOImpl;
import kr.co.yooooon.base.to.HolidayTO;
import kr.co.yooooon.common.exception.DataAccessException;
import kr.co.yooooon.common.transaction.DataSourceTransactionManager;

public class HolidayDAOImpl implements HolidayDAO {
	private DataSourceTransactionManager dataSourceTransactionManager ;
	   public void setDataSourceTransactionManager(DataSourceTransactionManager dataSourceTransactionManager) {
		      this.dataSourceTransactionManager = dataSourceTransactionManager;
		   }

	@Override
	public ArrayList<HolidayTO> selectHolidayList() {
		// TODO Auto-generated method stub
		

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			query.append("select to_char(apply_day, 'YYYY-MM-DD') apply_day, holiday_name, note ,seq from holiday  order by apply_day ");

			pstmt = con.prepareStatement(query.toString());
			rs = pstmt.executeQuery();

			ArrayList<HolidayTO> holidayList = new ArrayList<HolidayTO>();
			HolidayTO holiday = null;
			while (rs.next()) {
				holiday = new HolidayTO();
				holiday.setApplyDay(rs.getString("APPLY_DAY"));
				holiday.setHolidayName(rs.getString("HOLIDAY_NAME"));
				holiday.setNote(rs.getString("NOTE"));
				holiday.setSeq(rs.getString("seq"));  // 泥섏쓬�뿉 seq踰덊샇瑜� 媛숈씠 to 媛앹껜�뿉 �꽔�뼱 view�떒�쑝濡� 蹂대궦�썑 �뾽�뜲�씠�듃�떆 �씠嫄몃줈 移쇰읆�쓣 援щ텇�빐�꽌 �뾽�뜲�씠�듃�븳�떎. 洹몃옒�꽌 媛숈씠蹂대깂
				holidayList.add(holiday);
			}
			
			return holidayList;
		} catch (Exception sqle) {
		
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(pstmt, rs);
		}
	}

	@Override
	public String selectWeekDayCount(String startDate, String endDate) {
		

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			query.append("SELECT WEEKDAY_COUNTING_FUNC(?,?) WEEKDAY_COUNT FROM DUAL ");  //�븿�닔

			pstmt = con.prepareStatement(query.toString());
			pstmt.setString(1, startDate);
			pstmt.setString(2, endDate);
			rs = pstmt.executeQuery();

			String weekdayCount = null;
			while (rs.next()) {
				weekdayCount = rs.getString("WEEKDAY_COUNT");  // WEEKDAY_COUNT
			}
			
			return weekdayCount;
		} catch (Exception sqle) {
			
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(pstmt, rs);
		}
	}

	@Override
	public void updateCodeList(HolidayTO holyday) {
		// TODO Auto-generated method stub
		

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();
			query.append("UPDATE holiday SET ");
			query.append("HOLIDAY_NAME = ?, NOTE = ? , APPLY_DAY = ? ");
			query.append("WHERE seq = ? ");  // apply_day媛�濡�  移쇰읆�쓣 援щ텇�븯�뒗�뜲 �씠嫄� �닔�젙�븯硫� �뼱�뼸寃� �꽔�깘? --   洹몃옒�꽌 seq 異붽��븯�뿬 諛붽퓭以�
			pstmt = con.prepareStatement(query.toString());
			pstmt.setString(1, holyday.getHolidayName());
			pstmt.setString(2, holyday.getNote());
			pstmt.setString(3, holyday.getApplyDay());
			pstmt.setString(4, holyday.getSeq());  // �씠嫄몃줈 �뾽�뜲�씠�듃 異붽�
			pstmt.executeUpdate();
			
		} catch (Exception sqle) {
			
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(pstmt);
		}

	}
	
	@Override
	public void insertCodeList(HolidayTO holyday) {
		// TODO Auto-generated method stub
		

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();
			query.append(" insert into holiday values(TO_DATE(?,'YYYY-MM-DD') ,?,?,HOLIDAY_SEQ.nextval)");
			pstmt = con.prepareStatement(query.toString());
			pstmt.setString(1, holyday.getApplyDay());
			pstmt.setString(2, holyday.getHolidayName());
			pstmt.setString(3, holyday.getNote());
			pstmt.executeUpdate();
		
		} catch (Exception sqle) {
		
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(pstmt);
		}
	}

	@Override
	public void deleteCodeList(HolidayTO holyday) {
		// TODO Auto-generated method stub
		
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();
			query.append("delete holiday where APPLY_DAY = ?");
			pstmt = con.prepareStatement(query.toString());
			pstmt.setString(1, holyday.getApplyDay());
			pstmt.executeUpdate();
			
		} catch (Exception sqle) {
			
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(pstmt);
		}

	}

}
