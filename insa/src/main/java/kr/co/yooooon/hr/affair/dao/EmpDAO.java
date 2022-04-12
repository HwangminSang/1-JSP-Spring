package kr.co.yooooon.hr.affair.dao;
import java.util.ArrayList;

import kr.co.yooooon.hr.affair.to.EmpTO;

public interface EmpDAO {
	public EmpTO selectEmp(String empno);
	public String selectLastEmpCode();
	public ArrayList<EmpTO> selectEmpList();
	public ArrayList<EmpTO> selectEmpListD(String dept); //부서이름으로 검색
	public ArrayList<EmpTO> selectEmpListN(String name);  //이름으로검색
	public String getEmpCode(String name);
	public EmpTO selectEmployee(String empCode);
	

	public void registEmployee(EmpTO empto);
	public void updateEmployee(EmpTO emp);
	public void deleteEmployee(EmpTO emp);
}
