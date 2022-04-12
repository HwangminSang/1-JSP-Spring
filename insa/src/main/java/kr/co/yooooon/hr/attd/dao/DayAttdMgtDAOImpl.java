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
import kr.co.yooooon.hr.attd.to.DayAttdMgtTO;


public class DayAttdMgtDAOImpl implements DayAttdMgtDAO {

	private DataSourceTransactionManager dataSourceTransactionManager ;
	
	   public void setDataSourceTransactionManager(DataSourceTransactionManager dataSourceTransactionManager) {
		      this.dataSourceTransactionManager = dataSourceTransactionManager;
		   }
	

	@Override
	public HashMap<String, Object> batchDayAttdMgtProcess(String applyDay) { //�씪洹쇳깭愿�由� 議고쉶�븯湲� �닃���쓣�븣 遺덈윭�삤�뒗 濡쒖쭅
		
		
		Connection con = null;
		CallableStatement cstmt = null; //�봽濡쒖떆�� �샇異쒖쓣 �쐞�빐�꽌 
		ResultSet rs = null;
		
		ResultTO resultTO = null; 												 // �봽濡쒖떆�� �삤瑜� �벑 �벑 �벑 寃곌낵媛믪쓣 諛쏆쓣 TO
		ArrayList<DayAttdMgtTO> dayAttdMgtList = new ArrayList<DayAttdMgtTO>();  // 由ъ뒪�듃瑜� 媛�吏�怨� �삱 .. �벑�벑�벑 
		
		HashMap<String, Object> resultMap = new HashMap<>(); 					 // List�옉 TO 瑜� �떞�쓣 Map 
		
		try {
			
			System.out.println("DAO �뿉 �룄李⑺븳 �궇吏쒓컪 : "+applyDay);
			
			
			con = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();
			query.append("{call P_HR_ATTENDANCE.P_CREATE_DAY_ATTD_MANAGE(TO_DATE(?,'YYYY-MM-DD'),?,?,?)}"); // �봽濡쒖떆�� �뙣�궎吏�紐�  P_HR_ATTENDANCE   �봽濡쒖떆�� 紐� P_CREATE_DAY_ATTD_MANAGE  
			cstmt = con.prepareCall(query.toString()); // �봽濡쒖떆�� �샇�뀥�꽮
			cstmt.setString(1, applyDay);
			cstmt.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR); // 寃곌낵瑜� 而ㅼ꽌濡� 諛쏆븘�샂
			cstmt.registerOutParameter(3, java.sql.Types.VARCHAR); //error code
			cstmt.registerOutParameter(4, java.sql.Types.VARCHAR); // error msg
			cstmt.execute();

			resultTO = new ResultTO();
			
			resultTO.setErrorCode(cstmt.getString(3));
			resultTO.setErrorMsg(cstmt.getString(4));
			
		
			rs = (ResultSet)cstmt.getObject(2);  // �봽濡쒖떆��瑜� object �삎�깭濡� 諛쏆븘���꽌 �삎蹂��솚�빐以��썑 ResultSet�뿉  �꽔�뼱以��떎.  //Object	getObject(int parameterIndex)
			
			DayAttdMgtTO datAttdMgt = null;
			
			while (rs.next()) { //諛쏆븘�삩 而ㅼ꽌瑜� while臾몄쑝濡� �룎�젮�꽌 媛곴컖�쓽 �젅肄붾뱶瑜� DayAttdMgtTo�뿉 �꽔�뼱以��떎.
				datAttdMgt = new DayAttdMgtTO();
				datAttdMgt.setEmpCode(rs.getString("EMP_CODE"));
				datAttdMgt.setEmpName(rs.getString("EMP_NAME"));
				datAttdMgt.setApplyDays(rs.getString("APPLY_DAYS"));
				datAttdMgt.setDayAttdCode(rs.getString("DAY_ATTD_CODE"));
				datAttdMgt.setDayAttdName(rs.getString("DAY_ATTD_NAME"));
				datAttdMgt.setAttendTime(rs.getString("ATTEND_TIME"));
				datAttdMgt.setQuitTime(rs.getString("QUIT_TIME"));
				datAttdMgt.setLateWhether(rs.getString("LATE_WHETHER"));
				datAttdMgt.setLeaveHour(rs.getString("LEAVE_HOUR"));
				datAttdMgt.setWorkHour(rs.getString("WORK_HOUR"));
				datAttdMgt.setOverWorkHour(rs.getString("OVER_WORK_HOUR"));
				datAttdMgt.setNightWorkHour(rs.getString("NIGHT_WORK_HOUR"));
				datAttdMgt.setFinalizeStatus(rs.getString("FINALIZE_STATUS"));
				datAttdMgt.setPrivateLeaveHour(rs.getString("PRIVATE_LEAVE_HOUR"));
				datAttdMgt.setPublicLeaveHour(rs.getString("PUBLIC_LEAVE_HOUR"));
				
				dayAttdMgtList.add(datAttdMgt);
			}
			
			resultMap.put("dayAttdMgtList", dayAttdMgtList);
			resultMap.put("resultTO", resultTO);
			
			return resultMap;
		} catch (Exception sqle) {
			
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(cstmt,rs);
		}
	}
	
	@Override
	public void updateDayAttdMgtList(DayAttdMgtTO dayAttdMgt) {
		// TODO Auto-generated method stub
		

		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			query.append("UPDATE DAY_ATTD_MANAGE SET ");
			query.append("FINALIZE_STATUS = ? ");
			query.append("WHERE EMP_CODE = ? AND APPLY_DAYS = ? ");
			
			pstmt = con.prepareStatement(query.toString());
			pstmt.setString(1, dayAttdMgt.getFinalizeStatus());
			pstmt.setString(2, dayAttdMgt.getEmpCode());
			pstmt.setString(3, dayAttdMgt.getApplyDays());

			pstmt.executeUpdate();
			
			
		} catch (Exception sqle) {
			
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(pstmt);
		}
	}
	@Override
	public ArrayList<DayAttdMgtTO> findDayAttdMgtCheckList(String applyDay) {
		

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<DayAttdMgtTO> dayAttdMgtList = new ArrayList<DayAttdMgtTO>();
		try {
			con = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			query.append("SELECT a.EMP_CODE,e.EMP_NAME,a.APPLY_DAYS,a.FINALIZE_STATUS FROM DAY_ATTD_MANAGE a ,EMP e ");
			query.append("WHERE a.APPLY_DAYS BETWEEN TO_DATE(?,'RRRR-MM') AND LAST_DAY(TO_DATE(?,'RRRR-MM')) ");
			query.append("AND a.EMP_CODE=e.EMP_CODE");
			
			pstmt = con.prepareStatement(query.toString());
			pstmt.setString(1, applyDay);
			pstmt.setString(2, applyDay);
			
			 rs=pstmt.executeQuery();

			
      DayAttdMgtTO datAttdMgt = null;
			
			while (rs.next()) { //諛쏆븘�삩 而ㅼ꽌瑜� while臾몄쑝濡� �룎�젮�꽌 媛곴컖�쓽 �젅肄붾뱶瑜� DayAttdMgtTo�뿉 �꽔�뼱以��떎.
				datAttdMgt = new DayAttdMgtTO();
				datAttdMgt.setEmpCode(rs.getString("EMP_CODE"));
				datAttdMgt.setEmpName(rs.getString("EMP_NAME"));
				datAttdMgt.setApplyDays(rs.getString("APPLY_DAYS"));
				
				datAttdMgt.setFinalizeStatus(rs.getString("FINALIZE_STATUS"));
				
				
				dayAttdMgtList.add(datAttdMgt);
			}
			
		} catch (Exception sqle) {
		
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(pstmt);
		}
		// TODO Auto-generated method stub
		return dayAttdMgtList;
	}

}
