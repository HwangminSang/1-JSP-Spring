package kr.co.yooooon.hr.affair.dao;

import java.util.ArrayList;

import kr.co.yooooon.hr.affair.to.WorkInfoTO;


public interface WorkInfoDAO {
	public ArrayList<WorkInfoTO> selectWorkList(String empCode);

	public void insertWorkInfo(WorkInfoTO workInfo);;
	public void updateWorkInfo(WorkInfoTO workInfo);
	public void deleteWorkInfo(WorkInfoTO workInfo);
}
