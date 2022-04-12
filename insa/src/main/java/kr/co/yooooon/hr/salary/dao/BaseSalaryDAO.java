package kr.co.yooooon.hr.salary.dao;

import java.util.ArrayList;

import kr.co.yooooon.hr.salary.to.BaseSalaryTO;

public interface BaseSalaryDAO {
	public ArrayList<BaseSalaryTO> selectBaseSalaryList();
	public void updateBaseSalary(BaseSalaryTO baseSalary);
	
	public void insertBaseSalary(BaseSalaryTO baseSalary);
	public void deleteBaseSalary(BaseSalaryTO baseSalary);
}
