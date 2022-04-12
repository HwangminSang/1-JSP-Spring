package kr.co.yooooon.hr.affair.dao;

import java.util.ArrayList;

import kr.co.yooooon.hr.affair.to.LicenseInfoTO;

public interface LicenseInfoDAO {
	public ArrayList<LicenseInfoTO> selectLicenseList(String code);

	public void insertLicenseInfo(LicenseInfoTO licenscInfo);
	public void updateLicenseInfo(LicenseInfoTO licenscInfo);
	public void deleteLicenseInfo(LicenseInfoTO licenscInfo);
}
