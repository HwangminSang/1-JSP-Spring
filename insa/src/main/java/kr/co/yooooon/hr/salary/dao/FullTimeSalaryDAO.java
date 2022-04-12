package kr.co.yooooon.hr.salary.dao;

import java.util.ArrayList;
import java.util.List;

import kr.co.yooooon.hr.salary.to.FullTimeSalTO;
import kr.co.yooooon.hr.salary.to.PayDayTO;

public interface FullTimeSalaryDAO {
	public ArrayList<FullTimeSalTO> selectFullTimeSalary(String applyYear, String empCode); 
	public ArrayList<FullTimeSalTO> findAllMoney(String empCode); 
	public void updateFullTimeSalary(List<FullTimeSalTO> fullTimeSalary); 
	public ArrayList<PayDayTO> selectPayDayList(); 
}
