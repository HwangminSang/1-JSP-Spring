package kr.co.yooooon.hr.affair.sf;

import java.util.ArrayList;



import kr.co.yooooon.common.transaction.DataSourceTransactionManager;
import kr.co.yooooon.hr.affair.applicationService.AffairApplicationService;

import kr.co.yooooon.hr.affair.to.EmpTO;


public class AffairServiceFacadeImpl implements AffairServiceFacade {


	
	 private AffairApplicationService affairApplicationService;
		
	 public void setAffairApplicationService(AffairApplicationService affairApplicationService) {
	      this.affairApplicationService = affairApplicationService;
	   }
	 
   	 

	

	@Override
	public EmpTO getEmp(String id) {
		
		
		
		EmpTO empto = affairApplicationService.selectEmp(id);
			
		
		
		return empto;
	}

	@Override
	public String findLastEmpCode() {
	
			String empCode = affairApplicationService.findLastEmpCode();
			
			
			return empCode;
		
	}
	
	@Override
	public EmpTO findAllEmpInfo(String empCode) {
	
			EmpTO empTO = affairApplicationService.findAllEmployeeInfo(empCode);
			
		
			return empTO;
		
		
	}

	@Override
	public ArrayList<EmpTO> findEmpList(String dept) {
		
			ArrayList<EmpTO> empList = affairApplicationService.findEmployeeListByDept(dept);
			
			
			return empList;
		
	}

	@Override
	public void registEmployee(EmpTO empto) {
	
			affairApplicationService.registEmployee(empto);
			
	}
	
	@Override
	public void modifyEmployee(EmpTO emp) {
		
			affairApplicationService.modifyEmployee(emp);
		
	}

	@Override
	public void deleteEmpList(ArrayList<EmpTO> empList) {
	
			affairApplicationService.deleteEmpList(empList);
			
	}

	
}
