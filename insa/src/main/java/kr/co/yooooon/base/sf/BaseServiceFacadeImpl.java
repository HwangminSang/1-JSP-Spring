package kr.co.yooooon.base.sf;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import kr.co.yooooon.base.applicationService.BaseApplicationService;

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


public class BaseServiceFacadeImpl implements BaseServiceFacade {


	
	 private BaseApplicationService baseApplicationService;
		
	 public void setBaseApplicationService(BaseApplicationService baseApplicationService) {
	      this.baseApplicationService = baseApplicationService;
	   }

	
	@Override
	public boolean login(String id, String pw, HttpServletRequest request, HttpServletResponse response)  {
		
		

			boolean check = baseApplicationService.loginEmployee(id, pw,request,response);
			
			return check;
				
	}

	
	@Override
	public ArrayList<DetailCodeTO> findDetailCodeList(String codetype) {
		
			ArrayList<DetailCodeTO> detailCodeto = baseApplicationService.findDetailCodeList(codetype);
			
		
			return detailCodeto;
		
	}

	
	@Override
	public ArrayList<DetailCodeTO> findDetailCodeListRest(String code1, String code2, String code3) { 
			
		ArrayList<DetailCodeTO> detailCodeto = baseApplicationService.findDetailCodeListRest(code1, code2, code3);
					return detailCodeto;
	

	}
	
	
	@Override
	public ArrayList<HolidayTO> findHolidayList() {


		
			ArrayList<HolidayTO> holidayList = baseApplicationService.findHolidayList();
			
			
			return holidayList;
			
	}

	
	@Override
	public String findWeekDayCount(String startDate, String endDate) {
		
			String weekdayCount = baseApplicationService.findWeekDayCount(startDate, endDate);
					return weekdayCount;
			}

	
	@Override
	public void registEmpImg(String empCode, String imgExtend) {
		

		
			baseApplicationService.registEmpImg(empCode, imgExtend);
				
	}

	
	@Override
	public void batchDeptProcess(ArrayList<DeptTO> deptto) {
				
		
		baseApplicationService.batchDeptProcess(deptto);
			
		
	}

	
	@Override
	public ArrayList<PositionTO> findPositionList() {  // 議고쉶�씪�꽌  beginReadOnlyTransaction �븘�슂�빐蹂댁엫 
		
		ArrayList<PositionTO> positionList = baseApplicationService.findPositionList();
		       
		     	return positionList;
		     	
	}

	
	@Override
	public void modifyPosition(ArrayList<PositionTO> positionList) {
		// TODO Auto-generated method stub
					
		baseApplicationService.modifyPosition(positionList);

				}

	
	@Override
	public ArrayList<CodeTO> findCodeList() {
		// TODO Auto-generated method stub
	
		
		   	  ArrayList<CodeTO> codeto = baseApplicationService.findCodeList();
		       return codeto;
		  	}

	
	@Override
	public void batchHoilyDayProcess(List<HolidayTO> holyday) {
					
		baseApplicationService.batchHoilyDayProcess(holyday);
			
		}
	
	@Override
	public ReportTO findViewReport(String empCode) {
			 
		 
		
		 ReportTO to=baseApplicationService.findViewReport(empCode);
				return to;
	}
	
	@Override
	public ReportSalaryTO findViewSalaryReport(String empCode, String applyMonth) {
		 
		 
		 ReportSalaryTO to=baseApplicationService.findViewSalaryReport(empCode,applyMonth);
				return to;
	}
	
	@Override
	public ArrayList<BoardTO> getBoardList(){
	
		
		  ArrayList<BoardTO> list = baseApplicationService.getBoardList();
		      
		  return list;
		      }
	
	@Override
	public void addBoard(BoardTO board) {
		// TODO Auto-generated method stub
				
			if(board.getBoard_seq()!=0){
				BoardTO parentBoard=this.getBoard(board.getBoard_seq());
				board.setRef_seq(parentBoard.getRef_seq());
				board.setReply_seq(parentBoard.getBoard_seq());
			}
			
			baseApplicationService.addBoard(board);
			
		
	}
	
	@Override
	public BoardTO getBoard(int board_seq){
		

		  	 BoardTO board = baseApplicationService.getBoard(board_seq);
		       
		  	 return board;
		   
	}
	
	@Override
	public void changeHit(int board_seq) {
		// TODO Auto-generated method stub
					
		baseApplicationService.changeHit(board_seq);

			}
	
	@Override
	public BoardTO getBoard(String sessionId,int board_seq){
	
		BoardTO board=baseApplicationService.getBoard(board_seq);
		
	if(!sessionId.equals(board.getName())){	// 議고쉶�닔 1利앷�
			
		changeHit(board_seq);
		}
		
		return board;
	}
	
	@Override
	public int getRowCount(){
		
		
	
			int dbCount=baseApplicationService.getRowCount();
			
			
			return dbCount;
		
	}
	
	@Override
	public ArrayList<BoardTO> getBoardList(int sr, int er){
	
			ArrayList<BoardTO> list = baseApplicationService.getBoardList(sr, er);
			
			
			return list;
				
	}
	
	public void removeBoard(int board_seq) {
		// TODO Auto-generated method stub
				
			baseApplicationService.removeBoard(board_seq);
			
			
	}
	@Override
	public ArrayList<MenuTO> findMenuList() {


			ArrayList<MenuTO> menuList = baseApplicationService.findMenuList();
			
			
			return menuList;
	
	}
	@Override
	public ArrayList<AdminCodeTO> adminCodeList() {
				
		ArrayList<AdminCodeTO> amdList = baseApplicationService.adminCodeList();
			
			
			return amdList;
			
	}
	@Override
	public void modifyAuthority(String empCode, String adminCode) {  // 吏곸썝�뿉寃� 沅뚰븳遺��뿬. �닔�젙
		
		baseApplicationService.modifyAuthority(empCode,adminCode);
			
			
		
	}
	@Override
	public ArrayList<DeptTO> findDeptList() {  //�뿬湲�  �썝�옒 empservice�뿉 �엳�뜕嫄�  �봽濡쒖젥�듃 怨듯넻�쑝濡� �삷源� 遺��꽌李얘린.
		
				ArrayList<DeptTO> deptList = baseApplicationService.findDeptList();
			
			
			return deptList;
		}
}
