package kr.co.yooooon.base.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import kr.co.yooooon.base.to.MenuTO;
import kr.co.yooooon.common.exception.DataAccessException;
import kr.co.yooooon.common.transaction.DataSourceTransactionManager;

public class MenuDAOImpl implements MenuDAO{
	private DataSourceTransactionManager dataSourceTransactionManager ;
	   public void setDataSourceTransactionManager(DataSourceTransactionManager dataSourceTransactionManager) {
		      this.dataSourceTransactionManager = dataSourceTransactionManager;
		   }
	@Override
	public ArrayList<MenuTO> selectMenuList() {  
		// TODO Auto-generated method stub
				
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				try {
					con = dataSourceTransactionManager.getConnection();

					StringBuffer query = new StringBuffer();
					query.append("select  menu_name , ");
					query.append("super_menu_code , ");
					query.append("menu_code , ");
					query.append("level as depth , ");
					query.append("decode(super_menu_code , null , 'Y' , 'N') as is_collapce , ");
					query.append("menu_url ");
					query.append("from menu ");
					query.append("start with super_menu_code is null connect by prior menu_code = super_menu_code");  
					pstmt = con.prepareStatement(query.toString());
					rs = pstmt.executeQuery();

					ArrayList<MenuTO> menuList = new ArrayList<MenuTO>();
					MenuTO menu = null;
					while (rs.next()) {
						menu = new MenuTO();
						menu.setMenu_name(rs.getString("menu_name"));
						menu.setSuper_menu_code(rs.getString("super_menu_code"));
						menu.setMenu_code(rs.getString("menu_code"));
						menu.setDepth(rs.getString("depth"));
						menu.setIs_collapce(rs.getString("is_collapce"));
						menu.setMenu_url(rs.getString("menu_url"));
						menuList.add(menu);
					}
					
					
					return menuList;
				} catch (Exception sqle) {
					
					throw new DataAccessException(sqle.getMessage());
				} finally {
					dataSourceTransactionManager.close(pstmt, rs);
				}
	}
}
