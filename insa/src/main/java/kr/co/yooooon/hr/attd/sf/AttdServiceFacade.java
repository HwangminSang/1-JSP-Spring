package kr.co.yooooon.hr.attd.sf;


import java.util.ArrayList;

import kr.co.yooooon.common.to.ResultTO;
import kr.co.yooooon.hr.attd.to.DayAttdMgtTO;
import kr.co.yooooon.hr.attd.to.DayAttdTO;
import kr.co.yooooon.hr.attd.to.MonthAttdMgtTO;
import kr.co.yooooon.hr.attd.to.RestAttdTO;
import kr.co.yooooon.hr.attd.to.annualVacationMgtTO;

public interface AttdServiceFacade {
   public ArrayList<DayAttdTO> findDayAttdList(String empCode, String applyDay); //사원한명의 일근태를 알아옴
   public ResultTO registDayAttd(DayAttdTO dayAttd); // ResultTO 에러 코드 메세지가 있는 클래스
   public void removeDayAttdList(ArrayList<DayAttdTO> dayAttdList); // 근태기록 삭제 
   public void insertDayAttd(DayAttdTO dayAttd); // test

   public ArrayList<RestAttdTO> findRestAttdList(String empCode, String startDate, String endDate, String code); // 일정기간 신청내역 가져옴
   public ArrayList<RestAttdTO> findRestAttdListByDept(String deptName, String startDate, String endDate);
   public ArrayList<RestAttdTO> findRestAttdListByToday(String empCode, String toDay);
   public void registRestAttd(RestAttdTO restAttd); //근태신청
   public void modifyRestAttdList(ArrayList<RestAttdTO> restAttdList);
   public void removeRestAttdList(ArrayList<RestAttdTO> restAttdList); // 근태외 조회 삭제하기 버튼눌렀을때

   public ArrayList<DayAttdMgtTO> findDayAttdMgtCheckList(String applyDay);
   public ArrayList<DayAttdMgtTO> findDayAttdMgtList(String applyDay); // 일근태관리에서 조회하기 버튼
   public void modifyDayAttdMgtList(ArrayList<DayAttdMgtTO> dayAttdMgtList); // 일근태관리에서 전체마감 및 전체 마감취소 

   public ArrayList<MonthAttdMgtTO> findMonthAttdMgtList(String applyYearMonth);
   public void modifyMonthAttdMgtList(ArrayList<MonthAttdMgtTO> monthAttdMgtList);
   
   public ArrayList<annualVacationMgtTO> findAnnualVacationMgtList(String applyYearMonth);
   public void modifyAnnualVacationMgtList(ArrayList<annualVacationMgtTO> annualVacationMgtList);
   public void cancelAnnualVacationMgtList(ArrayList<annualVacationMgtTO> annualVacationMgtList);
   
   
   public void createVacation(String year);
}