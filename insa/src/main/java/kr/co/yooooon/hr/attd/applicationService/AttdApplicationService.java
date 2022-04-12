package kr.co.yooooon.hr.attd.applicationService;

import java.util.ArrayList;

import kr.co.yooooon.common.to.ResultTO;
import kr.co.yooooon.hr.attd.to.DayAttdMgtTO;
import kr.co.yooooon.hr.attd.to.DayAttdTO;
import kr.co.yooooon.hr.attd.to.MonthAttdMgtTO;
import kr.co.yooooon.hr.attd.to.RestAttdTO;
import kr.co.yooooon.hr.attd.to.annualVacationMgtTO;

public interface AttdApplicationService {
   public ArrayList<DayAttdTO> findDayAttdList(String empCode, String applyDay);
   public ResultTO registDayAttd(DayAttdTO dayAttd); //일근태 관련 메서드 입력시
   public void removeDayAttdList(ArrayList<DayAttdTO> dayAttdList);
   public void insertDayAttd(DayAttdTO dayAttd); //test

   
   public ArrayList<DayAttdMgtTO> findDayAttdMgtCheckList(String applyDay);
   public ArrayList<DayAttdMgtTO> findDayAttdMgtList(String applyDay);
   public void modifyDayAttdMgtList(ArrayList<DayAttdMgtTO> dayAttdMgtList);
   public ArrayList<MonthAttdMgtTO> findMonthAttdMgtList(String applyYearMonth);
   public void modifyMonthAttdMgtList(ArrayList<MonthAttdMgtTO> monthAttdMgtList);
   
   public ArrayList<RestAttdTO> findRestAttdList(String empCode, String startDate, String endDate, String code);
   public ArrayList<RestAttdTO> findRestAttdListByDept(String deptName, String startDate, String endDate);
   public ArrayList<RestAttdTO> findRestAttdListByToday(String empCode, String toDay);
   public void registRestAttd(RestAttdTO restAttd);
   public void modifyRestAttdList(ArrayList<RestAttdTO> restAttdList);
   public void removeRestAttdList(ArrayList<RestAttdTO> restAttdList);
   
   public ArrayList<annualVacationMgtTO> findAnnualVacationMgtList(String applyYearMonth);
   public void modifyAnnualVacationMgtList(ArrayList<annualVacationMgtTO> annualVacationMgtList);
   public void cancelAnnualVacationMgtList(ArrayList<annualVacationMgtTO> annualVacationMgtList);
   public void  createVacation(String year);
   
}