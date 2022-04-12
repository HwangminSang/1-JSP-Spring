package kr.co.yooooon.base.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import kr.co.yooooon.base.to.DetailCodeTO;
import kr.co.yooooon.common.exception.DataAccessException;
import kr.co.yooooon.common.transaction.DataSourceTransactionManager;

public class DetailCodeDAOImpl implements DetailCodeDAO {
	private DataSourceTransactionManager dataSourceTransactionManager ;
	   public void setDataSourceTransactionManager(DataSourceTransactionManager dataSourceTransactionManager) {
		      this.dataSourceTransactionManager = dataSourceTransactionManager;
		   }

	@Override
	public ArrayList<DetailCodeTO> selectDetailCodeList(String codetype) {
		

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
		
			con = dataSourceTransactionManager.getConnection();
			

			StringBuffer query = new StringBuffer();
			query.append("select * from detail_code where code_number = ?");

			pstmt = con.prepareStatement(query.toString());
			pstmt.setString(1, codetype);
			rs = pstmt.executeQuery();
			ArrayList<DetailCodeTO> detailCodeList=new ArrayList<DetailCodeTO>();
			DetailCodeTO detailCode=null;
			
			while(rs.next()){
				detailCode=new DetailCodeTO();
				detailCode.setCodeNumber(rs.getString("code_number"));
				detailCode.setDetailCodeNumber(rs.getString("detail_code_number"));
				detailCode.setDetailCodeName(rs.getString("detail_code_name"));
				detailCode.setDetailCodeNameusing(rs.getString("detail_code_nameusing"));
				detailCodeList.add(detailCode);
			}
			
			return detailCodeList;
			
		} catch (Exception sqle) {
			
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(pstmt, rs);
		}
	}

	@Override
	public void updateDetailCode(DetailCodeTO detailCodeto) {
		// TODO Auto-generated method stub
		

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			query.append("UPDATE DETAIL_CODE SET ");
			query.append("CODE_NUMBER = ?, DETAIL_CODE_NAME = ?, DETAIL_CODE_NAMEUSING = ? ");
			query.append("WHERE DETAIL_CODE_NUMBER = ? ");
		
			pstmt = con.prepareStatement(query.toString());
			pstmt.setString(1, detailCodeto.getCodeNumber());
			pstmt.setString(2, detailCodeto.getDetailCodeName());
			pstmt.setString(3, detailCodeto.getDetailCodeNameusing());
			pstmt.setString(4, detailCodeto.getDetailCodeNumber());
			pstmt.executeUpdate();

		} catch (Exception sqle) {
			
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(pstmt);
		}
		
	}
	
	@Override
	public void registDetailCode(DetailCodeTO detailCodeto) {
		// TODO Auto-generated method stub
		
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			StringBuffer insertQuery = new StringBuffer();
			insertQuery.append("insert into detail_code values(?,?,?,?)");
			con = dataSourceTransactionManager.getConnection();
			pstmt = con.prepareStatement(insertQuery.toString());
			
			pstmt.setString(1, detailCodeto.getDetailCodeNumber());
			pstmt.setString(2, detailCodeto.getCodeNumber());
			pstmt.setString(3, detailCodeto.getDetailCodeName());
			pstmt.setString(4, detailCodeto.getDetailCodeNameusing());
			
			pstmt.executeUpdate();
				
		} catch(Exception sqle) {
	
			throw new DataAccessException(sqle.getMessage());			
		} finally {
			dataSourceTransactionManager.close(pstmt);
		}		
	}
	
	@Override
	public void deleteDetailCode(DetailCodeTO detailCodeto) {
		// TODO Auto-generated method stub
		

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			query.append("DELETE FROM DETAIL_CODE WHERE DETAIL_CODE_NUMBER = ? AND DETAIL_CODE_NAME = ? ");
		
			pstmt = con.prepareStatement(query.toString());
			pstmt.setString(1, detailCodeto.getDetailCodeNumber());
			pstmt.setString(2, detailCodeto.getDetailCodeName());
			pstmt.executeUpdate();

		} catch (Exception sqle) {
			
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(pstmt);
		}
		
	}
	
	@Override
	public ArrayList<DetailCodeTO> selectDetailCodeListRest(String code1, String code2, String code3) { // �궗�썝硫붾돱 洹쇳깭�쇅 �떊泥��뿉�꽌 �벐�엫.
		// TODO Auto-generated method stub
		

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			query.append("select * from detail_code where DETAIL_CODE_NUMBER = ? OR DETAIL_CODE_NUMBER = ? OR DETAIL_CODE_NUMBER = ?"); //"DAC004","ADC003","ADC005

			pstmt = con.prepareStatement(query.toString());
			pstmt.setString(1, code1);
			pstmt.setString(2, code2);
			pstmt.setString(3, code3);			
			rs = pstmt.executeQuery();

			ArrayList<DetailCodeTO> detailCodeList=new ArrayList<DetailCodeTO>();
			DetailCodeTO detailCode=null;
			while(rs.next()){
				detailCode=new DetailCodeTO();
				detailCode.setCodeNumber(rs.getString("code_number"));
				detailCode.setDetailCodeNumber(rs.getString("detail_code_number"));
				detailCode.setDetailCodeName(rs.getString("detail_code_name"));
				detailCode.setDetailCodeNameusing(rs.getString("detail_code_nameusing"));				
				detailCodeList.add(detailCode);
				
			}
			
			return detailCodeList;
		} catch (Exception sqle) {
			
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(pstmt, rs);
		}
	}
	
	
}
