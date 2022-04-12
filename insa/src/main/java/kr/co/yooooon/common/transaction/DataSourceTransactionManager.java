package kr.co.yooooon.common.transaction;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import kr.co.yooooon.common.exception.DataAccessException;
import kr.co.yooooon.common.sl.ServiceLocator;

public class DataSourceTransactionManager { 
	private static DataSource dataSource;
	private ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>(); 
	
	static {
		dataSource = ServiceLocator.getInstance().getDataSource("jdbc/insa"); 
	}

	
	public void setDataSource(DataSource dataSource) { 
	DataSourceTransactionManager.dataSource = dataSource;
	}


	public Connection getConnection() { 

		Connection connection = (Connection)threadLocal.get(); 
		try {
			if (connection == null) {			
				connection = dataSource.getConnection();  
				threadLocal.set(connection);			
			}  
			} catch (SQLException e) {
			throw new DataAccessException(e.getMessage());
		}
		return connection;
	}


	public void closeConnection() {  
		System.out.println("closeConnection");
		try {
		Connection conn = (Connection) threadLocal.get(); 
		threadLocal.remove() ;
			if (conn != null)
				conn.close(); 
		} catch (SQLException e) {
			throw new DataAccessException(e.getMessage());
		}
	}

	
	public void beginTransaction() { 
		System.out.println("beginTransaction");
		try {
			getConnection().setAutoCommit(false); 
		} catch (SQLException e) {
			throw new DataAccessException(e.getMessage());
		}
	}

	
	public void rollbackTransaction() {  
		System.out.println("rollbackTransaction");
		try {
			getConnection().rollback();
			closeConnection();
		} catch (SQLException e) {
			throw new DataAccessException(e.getMessage());
		}
	}

	
	public void commitTransaction() {
		
		try {
			System.out.println("commitTransaction");
			getConnection().commit(); 
			closeConnection(); 
		} catch (SQLException e) {
			
			e.printStackTrace();
			throw new DataAccessException(e.getMessage());
		}
	}

	
	public void close(PreparedStatement pstmt) {
		try {
			if (pstmt != null)
				pstmt.close();
		} catch (SQLException e) {
			throw new DataAccessException(e.getMessage());
		}
	}

	
	public void close(PreparedStatement pstmt, ResultSet rs) { 
		try {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		} catch (SQLException e) {
			throw new DataAccessException(e.getMessage());
		}
	}

	
	public void close(Connection conn) {
		closeConnection();
	}

	
	public void close(Connection conn, PreparedStatement ps) {
		try {
			if (ps != null)
				ps.close();
			closeConnection();
		} catch (SQLException e) {
			throw new DataAccessException(e.getMessage());
		}
	}

	
	public void close(Connection conn, PreparedStatement ps, ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
			closeConnection();
		} catch (SQLException e) {
			throw new DataAccessException(e.getMessage());
		}
	}

	
	public void close(Connection conn, Statement st, ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
			if (st != null)
				st.close();
			closeConnection();
		} catch (SQLException e) {
			throw new DataAccessException(e.getMessage());
		}
	}

	
	public void beginReadOnlyTransaction() { 
		System.out.println("beginReadOnlyTransaction");
		try {
			getConnection().setReadOnly(true);  
		} catch (SQLException e) {
			throw new DataAccessException(e.getMessage());
		}
	}
}
