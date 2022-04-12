package kr.co.yooooon.hr.salary.sf;

import java.util.ArrayList;

import kr.co.yooooon.hr.salary.to.BaseDeductionTO;
import kr.co.yooooon.hr.salary.to.BaseExtSalTO;
import kr.co.yooooon.hr.salary.to.BaseSalaryTO;
import kr.co.yooooon.hr.salary.to.MonthSalaryTO;

public interface SalaryServiceFacade {
	
	public ArrayList<BaseSalaryTO> findBaseSalaryList();
	public void modifyBaseSalaryList(ArrayList<BaseSalaryTO> baseSalaryList);

	public ArrayList<BaseDeductionTO> findBaseDeductionList();
	public void batchBaseDeductionProcess(ArrayList<BaseDeductionTO> baseDeductionList);

	public ArrayList<BaseExtSalTO> findBaseExtSalList();
	public void modifyBaseExtSalList(ArrayList<BaseExtSalTO> baseExtSalList);

	public MonthSalaryTO findMonthSalary(String ApplyYearMonth, String empCode);
	public ArrayList<MonthSalaryTO> findYearSalary(String applyYear, String empCode);
	public void modifyMonthSalary(MonthSalaryTO monthSalary);
}
