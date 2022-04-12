package kr.co.yooooon.hr.salary.sf;

import java.util.ArrayList;



import kr.co.yooooon.hr.salary.applicationService.SalaryApplicationService;

import kr.co.yooooon.hr.salary.sf.SalaryServiceFacadeImpl;
import kr.co.yooooon.hr.salary.to.BaseDeductionTO;
import kr.co.yooooon.hr.salary.to.BaseExtSalTO;
import kr.co.yooooon.hr.salary.to.BaseSalaryTO;
import kr.co.yooooon.hr.salary.to.MonthSalaryTO;                                            

public class SalaryServiceFacadeImpl implements SalaryServiceFacade{
	

	
	
	
	private SalaryApplicationService salaryApplicationService;
	
	 public void setSalaryApplicationService(SalaryApplicationService salaryApplicationService) {
	      this.salaryApplicationService = salaryApplicationService;
	   }
	@Override
	public ArrayList<BaseSalaryTO> findBaseSalaryList() {
		
		
			ArrayList<BaseSalaryTO> baseSalaryList=salaryApplicationService.findBaseSalaryList();
			
			return baseSalaryList;
		
	}
	
	@Override
	public void modifyBaseSalaryList(ArrayList<BaseSalaryTO> baseSalaryList) {
		// TODO Auto-generated method stub
		
			salaryApplicationService.modifyBaseSalaryList(baseSalaryList);
			
	}
	
	@Override
	public ArrayList<BaseDeductionTO> findBaseDeductionList() {
		
			ArrayList<BaseDeductionTO> baseDeductionList=salaryApplicationService.findBaseDeductionList();
			
			
			
			return baseDeductionList;
		
	}
	
	@Override
	public void batchBaseDeductionProcess(ArrayList<BaseDeductionTO> baseDeductionList) {
		
		
			salaryApplicationService.batchBaseDeductionProcess(baseDeductionList);
			
	}
	
	@Override
	public ArrayList<BaseExtSalTO> findBaseExtSalList() {
		// TODO Auto-generated method stub
		
			ArrayList<BaseExtSalTO> baseExtSalList=salaryApplicationService.findBaseExtSalList();
			
			return baseExtSalList;
		
	}
	
	@Override
	public void modifyBaseExtSalList(ArrayList<BaseExtSalTO> baseExtSalList) {
		// TODO Auto-generated method stub
		
			salaryApplicationService.modifyBaseExtSalList(baseExtSalList);
			
		
	}

	@Override
	public MonthSalaryTO findMonthSalary(String ApplyYearMonth, String empCode) {
		// TODO Auto-generated method stub
		
			MonthSalaryTO monthSalary=salaryApplicationService.findMonthSalary(ApplyYearMonth, empCode);
			
			
			return monthSalary;
		
	}

	@Override
	public ArrayList<MonthSalaryTO> findYearSalary(String applyYear, String empCode) {
		// TODO Auto-generated method stub
		
			ArrayList<MonthSalaryTO> monthSalary=salaryApplicationService.findYearSalary(applyYear, empCode);
			
			return monthSalary;
		
	}
	
	@Override
	public void modifyMonthSalary(MonthSalaryTO monthSalary) {
		// TODO Auto-generated method stub
		
			salaryApplicationService.modifyMonthSalary(monthSalary);
			
	}
	
}
