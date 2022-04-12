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
import kr.co.yooooon.hr.attd.to.MonthAttdMgtTO;

public class MonthAttdMgtDAOImpl implements MonthAttdMgtDAO{


	private DataSourceTransactionManager dataSourceTransactionManager ;
	
	   public void setDataSourceTransactionManager(DataSourceTransactionManager dataSourceTransactionManager) {
		      this.dataSourceTransactionManager = dataSourceTransactionManager;
		   }
	
	@Override
	public HashMap<String, Object> batchMonthAttdMgtProcess(String applyYearMonth) {  //  findMonthAttdMgtList 濡� �궇�씪�솕�떎媛� 諛붾줈 �븵�뿉�꽌 �씠�젃寃� �씠由꾩씠 諛붾�뚮꽕 �씠嫄� �닔�젙�븘�슂
	
		Connection con = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		ResultTO resultTO = null; //�뿉�윭寃곌낵媛� �꽔�뒗怨�
		ArrayList<MonthAttdMgtTO> monthAttdMgtList=new ArrayList<MonthAttdMgtTO>();
		HashMap<String, Object> resultMap = new HashMap<>(); // 
		
		try {
			con = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			query.append("{call P_HR_ATTENDANCE.P_CREATE_MONTH_ATTD_MANAGE(?,?,?,?,?)}");
			cstmt = con.prepareCall(query.toString());
			System.out.println("MonthAttdMgtDAOImpl �뿉 �룄李⑺븳 �궇吏쒓컪 : "+applyYearMonth);
			cstmt.setString(1, applyYearMonth);
			cstmt.setString(2, "�씤�궗��"); //�봽濡쒖떆���뿉�꽌�룄 �븘�슂濡� �븯吏� �븡�뒗�냸
			cstmt.registerOutParameter(3, oracle.jdbc.OracleTypes.CURSOR);  // �옄諛붿뿉�꽌 �꽕�젙�빐以�
			cstmt.registerOutParameter(4, java.sql.Types.VARCHAR);
			cstmt.registerOutParameter(5, java.sql.Types.VARCHAR);
			cstmt.execute();

			resultTO = new ResultTO();
			resultTO.setErrorCode(cstmt.getString(4));
			resultTO.setErrorMsg(cstmt.getString(5));
			
			rs = (ResultSet)cstmt.getObject(3);  // 諛쏆븘�삩 而ㅼ꽌瑜� 寃곌낵�뀑�뿉 �떞�뒗�뜲 由ы꽩�삎�씠 Object�씪�꽌 �삎蹂��솚
			MonthAttdMgtTO monthAttdMgt = null;
			while (rs.next()) {
				monthAttdMgt = new MonthAttdMgtTO();
				monthAttdMgt.setEmpCode(rs.getString("EMP_CODE"));
				monthAttdMgt.setEmpName(rs.getString("EMP_NAME"));
				monthAttdMgt.setApplyYearMonth(rs.getString("APPLY_YEAR_MONTH"));
				monthAttdMgt.setBasicWorkDays(rs.getString("BASIC_WORK_DAYS"));
				monthAttdMgt.setWeekdayWorkDays(rs.getString("WEEKDAY_WORK_DAYS"));
				monthAttdMgt.setBasicWorkHour(rs.getString("BASIC_WORK_HOUR"));
				monthAttdMgt.setWorkHour(rs.getString("WORK_HOUR"));
				monthAttdMgt.setOverWorkHour(rs.getString("OVER_WORK_HOUR"));
				monthAttdMgt.setNightWorkHour(rs.getString("NIGHT_WORK_HOUR"));
				monthAttdMgt.setHolidayWorkDays(rs.getString("HOLIDAY_WORK_DAYS"));
				monthAttdMgt.setHolidayWorkHour(rs.getString("HOLIDAY_WORK_HOUR"));
				monthAttdMgt.setLateDays(rs.getString("LATE_DAYS"));
				monthAttdMgt.setEarlyLeaveDays(rs.getString("EARLY_LEAVE_DAYS"));
				monthAttdMgt.setAbsentDays(rs.getString("ABSENT_DAYS"));
				monthAttdMgt.setHalfHolidays(rs.getString("HALF_HOLIDAYS"));
				monthAttdMgt.setHolidays(rs.getString("HOLIDAYS"));
				monthAttdMgt.setFinalizeStatus(rs.getString("FINALIZE_STATUS")); // �썡洹쇳깭 留덇컧�뿬遺� 諛쏆븘�샂 以묒슂!!
				monthAttdMgtList.add(monthAttdMgt);
			}
			
			resultMap.put("monthAttdMgtList", monthAttdMgtList);
			resultMap.put("resultTO", resultTO);
			
		
			
			return resultMap;
		} catch (Exception sqle) {
			
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(cstmt,rs);
		}
	}
	@Override
	public void updateMonthAttdMgtList(MonthAttdMgtTO monthAttdMgt) { // �썡洹쇳깭 留덇컧 泥섎━ �븷�븣 
		// TODO Auto-generated method stub
	

		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			query.append("UPDATE MONTH_ATTD_MANAGE SET ");
			query.append("FINALIZE_STATUS = ? ");
			query.append("WHERE EMP_CODE = ? AND APPLY_YEAR_MONTH = ? ");

			pstmt = con.prepareStatement(query.toString());
			pstmt.setString(1, monthAttdMgt.getFinalizeStatus());
			pstmt.setString(2, monthAttdMgt.getEmpCode());
			pstmt.setString(3, monthAttdMgt.getApplyYearMonth());

			pstmt.executeUpdate();
			
		} catch (Exception sqle) {
			
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(pstmt);
		}		
	}


}
