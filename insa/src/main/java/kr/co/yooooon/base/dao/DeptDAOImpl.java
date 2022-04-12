package kr.co.yooooon.base.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;



import kr.co.yooooon.base.to.DeptTO;
import kr.co.yooooon.common.exception.DataAccessException;
import kr.co.yooooon.common.transaction.DataSourceTransactionManager;

public class DeptDAOImpl implements DeptDAO{
	private DataSourceTransactionManager dataSourceTransactionManager ;
	   public void setDataSourceTransactionManager(DataSourceTransactionManager dataSourceTransactionManager) {
		      this.dataSourceTransactionManager = dataSourceTransactionManager;
		   }

	
	@Override
	public ArrayList<DeptTO> selectDeptList() {
		
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs=null;
		
		try {
			StringBuffer selectQuery = new StringBuffer();
			selectQuery.append("select * from dept order by dept_code");
			
			con = dataSourceTransactionManager.getConnection();
			
			pstmt = con.prepareStatement(selectQuery.toString());
			rs=pstmt.executeQuery();
			
			ArrayList<DeptTO> list=new ArrayList<DeptTO>(); 
			
			while(rs.next()){
				DeptTO dept=new DeptTO();
				dept.setDeptCode(rs.getString("dept_code"));
				dept.setDeptName(rs.getString("dept_name"));
				dept.setDeptTel(rs.getString("dept_tel"));
				list.add(dept);
			}
			
			
			return list;
		} catch(Exception sqle) {
			throw new DataAccessException(sqle.getMessage());			
		} finally {
			dataSourceTransactionManager.close(pstmt, rs);
		}
	}
	
	
	@Override
	public void updateDept(DeptTO dept) {
	
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			StringBuffer updateQuery = new StringBuffer();
			updateQuery.append("UPDATE dept SET ");
			updateQuery.append("DEPT_NAME = ?, DEPT_TEL = ? ");
			updateQuery.append("WHERE DEPT_CODE = ? ");
			con = dataSourceTransactionManager.getConnection();
			pstmt = con.prepareStatement(updateQuery.toString());
			pstmt.setString(1, dept.getDeptName());
			pstmt.setString(2, dept.getDeptTel());
			pstmt.setString(3, dept.getDeptCode());
			pstmt.executeUpdate();
			
		} catch(Exception sqle) {
			throw new DataAccessException(sqle.getMessage());			
		} finally {
			dataSourceTransactionManager.close(pstmt);
		}
		
	}
	@Override
	public void registDept(DeptTO dept) {
		// TODO Auto-generated method stub
	
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			StringBuffer insertQuery = new StringBuffer();
			insertQuery.append("insert into dept values(?,?,?)");
			con = dataSourceTransactionManager.getConnection();
			pstmt = con.prepareStatement(insertQuery.toString());
			pstmt.setString(1, dept.getDeptCode());
			pstmt.setString(2, dept.getDeptName());
			pstmt.setString(3, dept.getDeptTel());
			pstmt.executeUpdate();
			
		
		} catch(Exception sqle) {
			throw new DataAccessException(sqle.getMessage());			
		} finally {
			dataSourceTransactionManager.close(pstmt);
		}
			
	}
	
	@Override
	public void deleteDept(DeptTO dept) {
		// TODO Auto-generated method stub
		
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			StringBuffer insertQuery = new StringBuffer();
			insertQuery.append("delete dept where dept_code = ?");
			con = dataSourceTransactionManager.getConnection();
			pstmt = con.prepareStatement(insertQuery.toString());
			pstmt.setString(1, dept.getDeptCode());
			pstmt.executeUpdate();
			
			
		} catch(Exception sqle) {
			throw new DataAccessException(sqle.getMessage());			
		} finally {
			dataSourceTransactionManager.close(pstmt);
		}
		
	}

}
