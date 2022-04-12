package kr.co.yooooon.base.dao;

import java.util.ArrayList;

import kr.co.yooooon.base.to.MenuTO;

public interface MenuDAO {
	public ArrayList<MenuTO> selectMenuList();
}
