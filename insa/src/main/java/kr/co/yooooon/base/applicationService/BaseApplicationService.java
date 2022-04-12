package kr.co.yooooon.base.applicationService;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.yooooon.base.exception.IdNotFoundException;
import kr.co.yooooon.base.exception.PwMissMatchException;
import kr.co.yooooon.base.to.AdminCodeTO;
import kr.co.yooooon.base.to.BoardTO;
import kr.co.yooooon.base.to.CodeTO;
import kr.co.yooooon.base.to.DeptTO;
import kr.co.yooooon.base.to.DetailCodeTO;
import kr.co.yooooon.base.to.HolidayTO;
import kr.co.yooooon.base.to.MenuTO;
import kr.co.yooooon.base.to.PositionTO;
import kr.co.yooooon.base.to.ReportSalaryTO;
import kr.co.yooooon.base.to.ReportTO;
import kr.co.yooooon.hr.affair.to.EmpTO;
import kr.co.yooooon.hr.salary.to.BaseSalaryTO;


public interface BaseApplicationService {
	//Login Part
   public boolean loginEmployee(String name, String empCode,HttpServletRequest request, HttpServletResponse response) ; 

   // Detail Code Part
   public ArrayList<DetailCodeTO> findDetailCodeList(String codetype);
   public ArrayList<DetailCodeTO> findDetailCodeListRest(String code1, String code2, String code3);

   //HoilyDay
   public ArrayList<HolidayTO> findHolidayList();
   public String findWeekDayCount(String startDate, String endDate);

   // ImgUpload Part 
   public void registEmpImg(String empCode, String imgExtend);

   //Position Part 
   public ArrayList<PositionTO> findPositionList();
   public void batchDeptProcess(ArrayList<DeptTO> deptto);
   public void modifyPosition(ArrayList<PositionTO> positionList);
   
   //Emp Part 
   public void registEmpCode(EmpTO emp);
   public void deleteEmpCode(EmpTO emp);
   public ArrayList<CodeTO> findCodeList();
   public void batchHoilyDayProcess(List<HolidayTO> holyday);
   
   //ireport
   public ReportTO findViewReport(String empCode);
   public ReportSalaryTO findViewSalaryReport(String empCode, String applyMonth);
   
   //Dept part 
   
   public ArrayList<DeptTO> findDeptList();	
   
   //Board Part
   public ArrayList<BoardTO> getBoardList();
   public void addBoard(BoardTO board);
   public BoardTO getBoard(int board_seq);
   public void changeHit(int board_seq);
   public int getRowCount();
   public ArrayList<BoardTO> getBoardList(int sr, int er);
   public void removeBoard(int board_seq);
   
   //Menu Part 
   public ArrayList<MenuTO> findMenuList();
   
   //Authority Part
	public ArrayList<AdminCodeTO> adminCodeList();
	public void modifyAuthority(String empCode, String adminCode);
}