package kr.co.yooooon.hr.affair.applicationService;

import java.util.ArrayList;

import kr.co.yooooon.hr.affair.to.EmpTO;


public interface AffairApplicationService {
	public EmpTO selectEmp(String name);
	public String findLastEmpCode();
	public EmpTO findAllEmployeeInfo(String empCode);
	public ArrayList<EmpTO> findEmployeeListByDept(String deptName);
	public void registEmployee(EmpTO emp);
	public void modifyEmployee(EmpTO emp);
	public void deleteEmpList(ArrayList<EmpTO> empList);
	
	
//	public ArrayList<BaseSalaryTO> selectPositionList();
}
