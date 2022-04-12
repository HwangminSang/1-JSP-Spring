package kr.co.yooooon.base.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;


import kr.co.yooooon.base.to.AdminCodeTO;
import kr.co.yooooon.common.exception.DataAccessException;
import kr.co.yooooon.common.transaction.DataSourceTransactionManager;



public class AdminCodeDAOImpl implements AdminCodeDAO{
	
	   private DataSourceTransactionManager dataSourceTransactionManager ;
	   public void setDataSourceTransactionManager(DataSourceTransactionManager dataSourceTransactionManager) {
		      this.dataSourceTransactionManager = dataSourceTransactionManager;
		   }
	  
		
	
	@Override
	public ArrayList<AdminCodeTO> selectAdminCodeList() {
		
	      Connection con = null;
	      PreparedStatement pstmt = null;
	      ResultSet rs=null;
	      try {
	         StringBuffer insertQuery = new StringBuffer();
	         insertQuery.append("select * from admin_code where admin_code not in('AD_00','AD_01')");
	        
	         con = dataSourceTransactionManager.getConnection();
	         pstmt = con.prepareStatement(insertQuery.toString());
	         rs=pstmt.executeQuery();
	         
	         ArrayList<AdminCodeTO> list=new ArrayList<AdminCodeTO>(); 
	         
	         while(rs.next()){
	            AdminCodeTO adminCodeTO=new AdminCodeTO();
	            adminCodeTO.setAdmin_code(rs.getString("admin_code"));
	            adminCodeTO.setAdmin_authority(rs.getString("admin_authority"));
	            
	            list.add(adminCodeTO);
	         }
	        
	         
	         return list;
	      } catch(Exception sqle) {
	        
	         throw new DataAccessException(sqle.getMessage());         
	      } finally {
	         dataSourceTransactionManager.close(pstmt, rs);
	      }
		
	}

	@Override
	public void updateAuthority(String empCode, String adminCode) {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			StringBuffer updateQuery = new StringBuffer();
			updateQuery.append("UPDATE EMP SET AUTHORITY = ? WHERE EMP_CODE = ? ");
			
			con = dataSourceTransactionManager.getConnection();
			pstmt = con.prepareStatement(updateQuery.toString());
			pstmt.setString(1,	adminCode);
			pstmt.setString(2,	empCode);
			
			pstmt.executeUpdate();
			
		} catch(Exception sqle) {
			throw new DataAccessException(sqle.getMessage());			
		} finally {
			dataSourceTransactionManager.close(pstmt);
		}
		
	}	
}
