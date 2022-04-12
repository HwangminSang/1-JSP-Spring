package kr.co.yooooon.hr.salary.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;



import kr.co.yooooon.common.exception.DataAccessException;
import kr.co.yooooon.common.transaction.DataSourceTransactionManager;
import kr.co.yooooon.hr.salary.dao.BaseSalaryDAOImpl;
import kr.co.yooooon.hr.salary.to.BaseSalaryTO;

public class BaseSalaryDAOImpl implements BaseSalaryDAO{
	private DataSourceTransactionManager dataSourceTransactionManager ;
	
	   public void setDataSourceTransactionManager(DataSourceTransactionManager dataSourceTransactionManager) {
		      this.dataSourceTransactionManager = dataSourceTransactionManager;
		   }
	@Override
	public ArrayList<BaseSalaryTO> selectBaseSalaryList() {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			query.append("select * from BASE_SALARY order by position_code");
			pstmt = con.prepareStatement(query.toString());
			rs = pstmt.executeQuery();

			ArrayList<BaseSalaryTO> baseSalaryList=new ArrayList<BaseSalaryTO>();
			BaseSalaryTO baseSalary = null;
			while (rs.next()) {
				baseSalary = new BaseSalaryTO();
				baseSalary.setPositionCode(rs.getString("position_code"));
				baseSalary.setPosition(rs.getString("position"));
				baseSalary.setBaseSalary(rs.getString("base_salary"));
				baseSalary.setHobongRatio(rs.getString("hobong_ratio"));
				baseSalaryList.add(baseSalary);
			}

			
			return baseSalaryList;
		} catch (Exception sqle) {
			
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(pstmt, rs);
		}
	}


	
	@Override
	public void updateBaseSalary(BaseSalaryTO baseSalary) {
		// TODO Auto-generated method stub
		

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			query.append(" UPDATE  BASE_SALARY SET ");
			query.append(" POSITION = ?, BASE_SALARY = ?, HOBONG_RATIO = ? ");
			query.append(" WHERE POSITION_CODE = ? ");

			pstmt = con.prepareStatement(query.toString());
			
			
			
			pstmt.setString(1, baseSalary.getPosition());
			pstmt.setString(2, baseSalary.getBaseSalary());
			pstmt.setString(3, baseSalary.getHobongRatio());
			pstmt.setString(4, baseSalary.getPositionCode());
			pstmt.executeUpdate();

			
		} catch (Exception sqle) {
			
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(pstmt);
		}			
	}
	
	@Override
	public void insertBaseSalary(BaseSalaryTO baseSalary) {
		// TODO Auto-generated method stub
		

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			query.append( " INSERT INTO BASE_SALARY VALUES ( ?, ?, ?, ? ) " );

			pstmt = con.prepareStatement(query.toString());
			pstmt.setString(1, baseSalary.getBaseSalary());
			pstmt.setString(2, baseSalary.getHobongRatio());
			pstmt.setString(3, baseSalary.getPositionCode());
			pstmt.setString(4, baseSalary.getPosition());
		
			pstmt.executeUpdate();
		
		} catch (Exception sqle) {
			
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(pstmt);
		}			
	}
	
	@Override
	public void deleteBaseSalary(BaseSalaryTO baseSalary) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			query.append(" DELETE  BASE_SALARY ");
			query.append(" WHERE POSITION_CODE = ? AND POSITION = ? ");

			pstmt = con.prepareStatement(query.toString());
			pstmt.setString(1, baseSalary.getPositionCode());
			pstmt.setString(2, baseSalary.getPosition());
			pstmt.executeUpdate();

			
		} catch (Exception sqle) {
			
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(pstmt);
		}			
	}
	
}
