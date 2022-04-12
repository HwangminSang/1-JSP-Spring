package kr.co.yooooon.hr.affair.dao;

import java.util.ArrayList;

import kr.co.yooooon.hr.affair.to.FamilyInfoTO;

public interface FamilyInfoDAO {
	public ArrayList<FamilyInfoTO> selectFamilyList(String code);

	public void insertFamilyInfo(FamilyInfoTO familyInfo);
	public void updateFamilyInfo(FamilyInfoTO familyInfo);
	public void deleteFamilyInfo(FamilyInfoTO familyInfo);
}
