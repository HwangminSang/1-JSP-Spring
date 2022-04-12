package kr.co.yooooon.base.applicationService;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import kr.co.yooooon.base.dao.AdminCodeDAO;

import kr.co.yooooon.base.dao.BoardDAO;

import kr.co.yooooon.base.dao.CodeDAO;

import kr.co.yooooon.base.dao.DeptDAO;

import kr.co.yooooon.base.dao.DetailCodeDAO;

import kr.co.yooooon.base.dao.HolidayDAO;

import kr.co.yooooon.base.dao.PositonDAO;

import kr.co.yooooon.base.dao.ReportDAO;

import kr.co.yooooon.base.dao.MenuDAO;


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
import kr.co.yooooon.common.exception.DataAccessException;
import kr.co.yooooon.hr.affair.applicationService.AffairApplicationService;

import kr.co.yooooon.hr.affair.to.EmpTO;



public class BaseApplicationServiceImpl implements BaseApplicationService {


	 private AffairApplicationService affairApplicationService;
	 private DetailCodeDAO detailCodeDAO;
		private HolidayDAO holidayDAO;
		private DeptDAO deptDAO;
		private BoardDAO boardDAO;
		
		private CodeDAO codeDAO;
		private ReportDAO reportDAO;
		private MenuDAO menuDAO;
		private AdminCodeDAO adminDAO;
		private PositonDAO positionDAO;
		
		
		
	 public void setAffairApplicationService(AffairApplicationService affairApplicationService) {
	      this.affairApplicationService = affairApplicationService;
	   }
  
	
	
	
	public void setDetailCodeDAO(DetailCodeDAO detailCodeDAO) {
		this.detailCodeDAO = detailCodeDAO;
	}
	
	public void setHolidayDAO(HolidayDAO holidayDAO) {
		this.holidayDAO = holidayDAO;
	}
	
	public void setDeptDAO(DeptDAO deptDAO) {
		this.deptDAO = deptDAO;
	}
	
	public void setBoardDAO(BoardDAO boardDAO) {
		this.boardDAO = boardDAO; 
	}
	
	public void setCodeDAO(CodeDAO codeDAO) {
		this.codeDAO = codeDAO;
	}
	
	public void setReportDAO( ReportDAO reportDAO) {
		this.reportDAO = reportDAO;
	}
	
	public void setMenuDAO(MenuDAO menuDAO) {
		this.menuDAO = menuDAO;
	}
	
	public void setAdminCodeDAO(AdminCodeDAO adminDAO) {
		this.adminDAO = adminDAO;
	}
	
	public void setPositonDAO(PositonDAO positionDAO) {
		this.positionDAO = positionDAO;
	}
	
	
	
	public boolean loginEmployee(String id, String pw, HttpServletRequest request, HttpServletResponse response)  {
		

		EmpTO emp = affairApplicationService.selectEmp(id); 

		if (emp == null) {
			
			throw new DataAccessException("존재하지 않는 ID!!!");
			
		}else {
			
			if (emp.getPw().equals(pw)){
				  
				if(request.getParameter("box").equals("false")){ 
					
					Cookie[] array=request.getCookies();
					
					if(array!=null){ 
						for(Cookie coo: array){
							if(coo.getName().equals("id")){
								Cookie c=new Cookie("id","");  
								c.setMaxAge(0);  
								response.addCookie(c); 
							}
						}
					}   
					
				}else{
					Cookie coo=new Cookie("id",id); 
					coo.setMaxAge(60*5); 
					response.addCookie(coo);
				}
				
			
				
				return true;
				
			}else { 
				
				throw new DataAccessException("비밀번호가 다릅니다!!");
			 }
		}
	}

	@Override
	public ArrayList<DetailCodeTO> findDetailCodeList(String codetype) {
	
	
		ArrayList<DetailCodeTO>  detailCodeList = detailCodeDAO.selectDetailCodeList(codetype);

	
		return detailCodeList;
	}

	@Override
	public void registEmpCode(EmpTO emp) {
	
		DetailCodeTO detailCodeto = new DetailCodeTO();
		detailCodeto.setDetailCodeNumber(emp.getEmpCode());
		detailCodeto.setDetailCodeName(emp.getEmpName());
		detailCodeto.setCodeNumber("CO-17");
		detailCodeto.setDetailCodeNameusing("N");
		detailCodeDAO.registDetailCode(detailCodeto);

			}

	@Override
	public void deleteEmpCode(EmpTO emp) {
		
		DetailCodeTO detailCodeto = new DetailCodeTO();
		detailCodeto.setDetailCodeNumber(emp.getEmpCode());
		detailCodeto.setDetailCodeName(emp.getEmpName());
		detailCodeDAO.deleteDetailCode(detailCodeto);

	

	}

	@Override
	public ArrayList<DetailCodeTO> findDetailCodeListRest(String code1, String code2, String code3) { 
			ArrayList<DetailCodeTO> detailCodeList = null;
		detailCodeList = detailCodeDAO.selectDetailCodeListRest(code1, code2, code3);

		
		return detailCodeList;
	}

	@Override
	public ArrayList<HolidayTO> findHolidayList() {
		

		ArrayList<HolidayTO> holidayList = holidayDAO.selectHolidayList();

		
		return holidayList;
	}

	@Override
	public void batchDeptProcess(ArrayList<DeptTO> deptto) {
				DetailCodeTO detailCodeto = new DetailCodeTO(); //媛앹껜媛� �븯�궃�뜲 �뼱�뼸寃� �룎�젮�벐吏� ?  // set�빐以섏꽌 湲곗〈�쓽 媛믪뿉 �뜮�뼱�뵆�슦�땲源� 媛��뒫�븯吏��븡�굹? �븯�굹�뵫 �닚李⑥쟻�쑝濡� �뱾�뼱媛��땲源�

		for (DeptTO dept : deptto) {
			
			switch (dept.getStatus()) {

				case "update":
					deptDAO.updateDept(dept);
					detailCodeto.setDetailCodeNumber(dept.getDeptCode());
					detailCodeto.setDetailCodeName(dept.getDeptName());
					detailCodeto.setCodeNumber("CO-07");  // co-07�씠 遺��꽌愿��젴 肄붾뱶 
					detailCodeto.setDetailCodeNameusing("Y"); //�깉濡� 留뚮뱾�뼱�졇�꽌 �씠�젃寃뚰븯�떎 .
					detailCodeDAO.updateDetailCode(detailCodeto);
				break;

				case "insert":
					deptDAO.registDept(dept);
					detailCodeto.setDetailCodeNumber(dept.getDeptCode()); 
					detailCodeto.setDetailCodeName(dept.getDeptName());
					detailCodeto.setCodeNumber("CO-07");
					detailCodeto.setDetailCodeNameusing("Y");
					detailCodeDAO.registDetailCode(detailCodeto);
				break;

				case "delete":
					deptDAO.deleteDept(dept);
					detailCodeto.setDetailCodeNumber(dept.getDeptCode());
					detailCodeto.setDetailCodeName(dept.getDeptName());
					detailCodeDAO.deleteDetailCode(detailCodeto);
				break;

				case "normal":break;
			}
		}

	}

	@Override
	public void modifyPosition(ArrayList<PositionTO> positionList) {

		if (positionList != null && positionList.size() > 0){ // �븘臾닿쾬�룄 �뾾�뼱嫄곕굹 鍮덈같�뿴�씪寃쎌슦瑜� ��鍮� 
			
			for (PositionTO position : positionList) {
				DetailCodeTO detailCodeto = new DetailCodeTO();
				switch (position.getStatus()) {

				case "update":
					positionDAO.updatePosition(position);
					detailCodeto.setDetailCodeNumber(position.getPositionCode());
					detailCodeto.setDetailCodeName(position.getPosition());
					detailCodeto.setCodeNumber("CO-04"); // 吏곴툒怨쇰젴 肄붾뱶. 
					detailCodeto.setDetailCodeNameusing("Y");
					detailCodeDAO.updateDetailCode(detailCodeto);
					break;

				case "insert":
					positionDAO.insertPosition(position);
					detailCodeto.setDetailCodeNumber(position.getPositionCode());
					detailCodeto.setDetailCodeName(position.getPosition());
					detailCodeto.setCodeNumber("CO-04");
					detailCodeto.setDetailCodeNameusing("Y");
					detailCodeDAO.registDetailCode(detailCodeto);
					break;

				case "delete":
					positionDAO.deletePosition(position);
					detailCodeto.setDetailCodeNumber(position.getPositionCode());
					detailCodeto.setDetailCodeName(position.getPosition());
					detailCodeDAO.deleteDetailCode(detailCodeto);
					break;
				}
			}
		}

	}

	@Override
	public String findWeekDayCount(String startDate, String endDate) {


		String weekdayCount = holidayDAO.selectWeekDayCount(startDate, endDate);


		return weekdayCount;
	}

	@Override
	public void registEmpImg(String empCode, String imgExtend) {
	 
           		EmpTO emp = affairApplicationService.findAllEmployeeInfo(empCode);
		if (emp == null) {
			
			emp = new EmpTO();
			emp.setEmpCode(empCode);
			emp.setStatus("insert");
		} else {
			
			emp.setStatus("update");
		}
		
		emp.setImgExtend(imgExtend);
		affairApplicationService.modifyEmployee(emp);

	
	}

	@Override
	public ArrayList<CodeTO> findCodeList() { // 
	

		ArrayList<CodeTO> codeList = codeDAO.selectCode();

	
		return codeList;
	}

	@Override
	public void batchHoilyDayProcess(List<HolidayTO> holyday) {
	
		
		for (HolidayTO holiday : holyday) {
			switch (holiday.getStatus()) {

			case "update":
				holidayDAO.updateCodeList(holiday);
				break;
			
			case "insert":
				holidayDAO.insertCodeList(holiday);
				break;

			case "delete":
				holidayDAO.deleteCodeList(holiday);
				break;

			}
			
		
		}
	}
	
	@Override
	public ReportTO findViewReport(String empCode) {
		
		
		

		
			ReportTO to=reportDAO.selectReport(empCode);
		
		
		return to;
	}
	
	
	@Override
	public ReportSalaryTO findViewSalaryReport(String empCode, String applyMonth) {
		
		
			ReportSalaryTO to=reportDAO.selecSalarytReport(empCode,applyMonth);
		
		return to;
	}
	
	@Override
	public ArrayList<BoardTO> getBoardList(){
		
		ArrayList<BoardTO> list = boardDAO.selectBoardList();
		
		return list;
	}
	
	@Override
	public void addBoard(BoardTO board) {
		
        boardDAO.insertBoard(board);

		
	}
	
	@Override
	public BoardTO getBoard(int board_seq){
		
		BoardTO board = boardDAO.selectBoard(board_seq);
	
		return board;
	}
	
	@Override
	public void changeHit(int board_seq) {
		
        boardDAO.updateHit(board_seq);

	}
	
	@Override
	public int getRowCount(){
		
		int dbCount = boardDAO.selectRowCount();
		
		return dbCount;
	}
	
	@Override
	public ArrayList<BoardTO> getBoardList(int sr, int er){
		
		ArrayList<BoardTO> list = boardDAO.selectBoardList(sr, er);
		
		return list;
	}
	
	public void removeBoard(int board_seq) {
		
		
       
        boardDAO.deleteBoard(board_seq);

		
	}
	@Override
	public ArrayList<MenuTO> findMenuList() {
		

		ArrayList<MenuTO> menuList = menuDAO.selectMenuList();

		
		return menuList;
	}
	@Override
	public ArrayList<AdminCodeTO> adminCodeList() {
		
		
		ArrayList<AdminCodeTO> adminCodeList = adminDAO.selectAdminCodeList();
		
		
		
		return adminCodeList;
	}
	
	@Override
	public void modifyAuthority(String empCode, String adminCode) {
		
		adminDAO.updateAuthority(empCode ,adminCode);
		
		
	}
	
	@Override
	public ArrayList<DeptTO> findDeptList() {
		
		
		ArrayList<DeptTO> deptList = deptDAO.selectDeptList();
		
		
		return deptList;
	}
	@Override
	public ArrayList<PositionTO> findPositionList() {
		
		
		ArrayList<PositionTO> positionList = positionDAO.selectPositonList();
		
		
		return positionList;
	}
}