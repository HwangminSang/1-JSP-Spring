package kr.co.yooooon.base.sf;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import kr.co.yooooon.hr.salary.to.BaseSalaryTO;

public interface BaseServiceFacade {
	//Login Part
	public boolean login(String name, String empCode,HttpServletRequest request, HttpServletResponse response);
	
	//Code Part 
	public ArrayList<DetailCodeTO> findDetailCodeList(String codetype);
	public ArrayList<DetailCodeTO> findDetailCodeListRest(String code1,String code2,String code3);
	public ArrayList<CodeTO> findCodeList();
	//HoilyDay Part
	public ArrayList<HolidayTO> findHolidayList();
	public String findWeekDayCount(String startDate, String endDate);
	void batchHoilyDayProcess(List<HolidayTO> holyday);
	
	//IO part
	public void registEmpImg(String empCode, String imgExtend);
	
	//Dept part 
	public void batchDeptProcess(ArrayList<DeptTO> deptto);
	public ArrayList<DeptTO> findDeptList();
	//Position Part 
	public ArrayList<PositionTO> findPositionList();
	public void modifyPosition(ArrayList<PositionTO> positionList);

	// Ireport Part 
	public ReportTO findViewReport(String empCode);
	public ReportSalaryTO findViewSalaryReport(String empCode, String applyMonth);
	
	//Board Part
	public ArrayList<BoardTO> getBoardList();
	public void addBoard(BoardTO board);
	public BoardTO getBoard(int board_seq);
	public BoardTO getBoard(String sessionId,int board_seq);
	public void changeHit(int board_seq);
	public int getRowCount();
	public ArrayList<BoardTO> getBoardList(int sr, int er);
	public void removeBoard(int board_seq);
	// Menu Part 
	public ArrayList<MenuTO> findMenuList();
	
	//Authority Part
	public ArrayList<AdminCodeTO> adminCodeList(); 
	public void modifyAuthority(String empCode, String adminCode);
}
