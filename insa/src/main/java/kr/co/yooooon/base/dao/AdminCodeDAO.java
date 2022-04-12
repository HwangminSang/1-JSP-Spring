package kr.co.yooooon.base.dao;

import java.util.ArrayList;

import kr.co.yooooon.base.to.AdminCodeTO;

public interface AdminCodeDAO {
	public ArrayList<AdminCodeTO> selectAdminCodeList();
	public void updateAuthority(String empCode ,String adminCode);
}
