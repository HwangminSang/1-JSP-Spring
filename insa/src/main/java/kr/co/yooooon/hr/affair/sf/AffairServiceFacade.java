package kr.co.yooooon.hr.affair.sf;

import java.util.ArrayList;

import kr.co.yooooon.base.to.DeptTO;
import kr.co.yooooon.hr.affair.to.EmpTO;

public interface AffairServiceFacade {
	public EmpTO getEmp(String name); //selectEmp
	public String findLastEmpCode();
	public EmpTO findAllEmpInfo(String empCode);	
	public ArrayList<EmpTO> findEmpList(String dept); //findEmployeeListByDept
	public void registEmployee(EmpTO empto);
	public void modifyEmployee(EmpTO emp);
	public void deleteEmpList(ArrayList<EmpTO> empList);
	
	
}