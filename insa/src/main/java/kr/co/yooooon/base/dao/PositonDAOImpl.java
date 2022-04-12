package kr.co.yooooon.base.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;


import kr.co.yooooon.base.to.PositionTO;
import kr.co.yooooon.common.exception.DataAccessException;

import kr.co.yooooon.common.transaction.DataSourceTransactionManager;



public class PositonDAOImpl implements PositonDAO {
	private DataSourceTransactionManager dataSourceTransactionManager ;
	   public void setDataSourceTransactionManager(DataSourceTransactionManager dataSourceTransactionManager) {
		      this.dataSourceTransactionManager = dataSourceTransactionManager;
		   }

	@Override
	public void updatePosition(PositionTO position) {
		// TODO Auto-generated method stub
		
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			query.append(" UPDATE  POSITION SET ");
			query.append(" POSITION = ? ");
			query.append(" WHERE POSITION_CODE = ? ");

			pstmt = con.prepareStatement(query.toString());
			pstmt.setString(1, position.getPosition());
			pstmt.setString(2, position.getPositionCode());
			pstmt.executeUpdate();
			
			
		
		

		} catch (Exception sqle) {
			
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(pstmt);
		}			
		
	}

	@Override
	public void insertPosition(PositionTO position) {
		// TODO Auto-generated method stub
		
		

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			query.append( " INSERT INTO POSITION VALUES ( ?, ? ) " );

			pstmt = con.prepareStatement(query.toString());
			pstmt.setString(1, position.getPositionCode());
			pstmt.setString(2, position.getPosition());
		
			
			pstmt.executeUpdate();
		
			
		} catch (Exception sqle) {
			
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(pstmt);
		}			
	}

	@Override
	public void deletePosition(PositionTO position) {
		// TODO Auto-generated method stub
	

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			query.append(" DELETE  POSITION ");
			query.append(" WHERE POSITION_CODE = ? AND POSITION = ? ");

			pstmt = con.prepareStatement(query.toString());
			pstmt.setString(1, position.getPositionCode());
			pstmt.setString(2, position.getPosition());
			pstmt.executeUpdate();

		} catch (Exception sqle) {
			
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(pstmt);
		}	
	}

	@Override
	public ArrayList<PositionTO> selectPositonList() {
		

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			query.append("select * from POSITION order by position_code");
			pstmt = con.prepareStatement(query.toString());
			rs = pstmt.executeQuery();

			ArrayList<PositionTO> positionList=new ArrayList<PositionTO>();
			PositionTO position=null;
			while (rs.next()) {
				position = new PositionTO();
				position.setPositionCode(rs.getString("POSITION_CODE"));
				position.setPosition(rs.getString("POSITION"));
				
				positionList.add(position);
			}

		
			return positionList;
		} catch (Exception sqle) {
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(pstmt, rs);
		}
	}

}
