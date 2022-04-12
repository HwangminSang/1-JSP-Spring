package kr.co.yooooon.hr.salary.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

import kr.co.yooooon.common.exception.DataAccessException;
import kr.co.yooooon.common.transaction.DataSourceTransactionManager;
import kr.co.yooooon.hr.salary.dao.SocialInsureDAOImpl;
import kr.co.yooooon.hr.salary.to.SocialInsureTO;

public class SocialInsureDAOImpl implements SocialInsureDAO {
	   private DataSourceTransactionManager dataSourceTransactionManager;
	   
	   public void setDataSourceTransactionManager(DataSourceTransactionManager dataSourceTransactionManager) {
			this.dataSourceTransactionManager = dataSourceTransactionManager;
		}
	
	@Override
	public ArrayList<SocialInsureTO> selectBaseInsureList(String yearBox) {
		Connection con = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		ArrayList<SocialInsureTO> BaseInsureList=new ArrayList<SocialInsureTO>();
		try {
			con = dataSourceTransactionManager.getConnection();
			
	        String sql="{call P_INSURE_SELECT(?,?)}";
	        cstmt=con.prepareCall(sql);
			cstmt.setString(1, yearBox);
			cstmt.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
			cstmt.execute();
			
			SocialInsureTO baseInsure = null;
			rs=(ResultSet)cstmt.getObject(2);
			if (rs.next()) {
				baseInsure = new SocialInsureTO();
				baseInsure.setAttributionYear(rs.getString("ATTRIBUTION_YEAR"));
				baseInsure.setHealthinsureRates(rs.getString("HEALTH_INSURE_RATES"));
				baseInsure.setLongtermcareRate(rs.getString("LONG_TERM_CARE_RATE"));
				baseInsure.setNationpenisionRates(rs.getString("NATION_PENISION_RATES"));
				baseInsure.setTeachpenisionRates(rs.getString("TEACH_PENISION_RATES")); 
				baseInsure.setEmpinsureRates(rs.getString("EMP_INSURE_RATES")); 
				baseInsure.setWrkinsureRates(rs.getString("WRK_INSURE_RATES"));    
				baseInsure.setJobstabilRates(rs.getString("JOBSTABIL_RATES"));  
				baseInsure.setVocacompetencyRates(rs.getString("VOCA_COMPETENCY_RATES"));  
				baseInsure.setIndustinsureRates(rs.getString("INDUST_INSURE_RATES"));  
				baseInsure.setIndustinsurecharRates(rs.getString("INDUST_INSURE_CHAR_RATES")); 
			}
			BaseInsureList.add(baseInsure);
			return BaseInsureList;
		} catch (Exception sqle) {
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(cstmt, rs);
		}
	}
	
	@Override
	public void updateInsureData(SocialInsureTO baseInsure) {
		
		Connection con = null;
		CallableStatement cstmt = null;

		try {
			con = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			query.append("{call P_INSURE_UPDATE(?,?,?,?,?,?,?,?,?,?,?)}");

			cstmt = con.prepareCall(query.toString());
			cstmt.setString(1, baseInsure.getAttributionYear());
			cstmt.setString(2, baseInsure.getHealthinsureRates());
			cstmt.setString(3, baseInsure.getLongtermcareRate());
			cstmt.setString(4, baseInsure.getNationpenisionRates());
			cstmt.setString(5, baseInsure.getTeachpenisionRates());
			cstmt.setString(6, baseInsure.getEmpinsureRates());
			cstmt.setString(7, baseInsure.getWrkinsureRates());
			cstmt.setString(8, baseInsure.getJobstabilRates());
			cstmt.setString(9, baseInsure.getVocacompetencyRates());
			cstmt.setString(10, baseInsure.getIndustinsureRates());
			cstmt.setString(11, baseInsure.getIndustinsurecharRates());
			cstmt.executeUpdate();
			
		} catch (Exception sqle) {
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(cstmt);
		}	
	}
	
	@Override
	public void deleteInsureData(SocialInsureTO baseInsure) {
		Connection con = null;
		CallableStatement cstmt = null;
		try {
			con = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();
			query.append("{call P_INSURE_DELETE(?)}");
			cstmt = con.prepareCall(query.toString());
			cstmt.setString(1, baseInsure.getAttributionYear());
			cstmt.executeUpdate();
		} catch (Exception sqle) {
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(cstmt);
		}
	}
}