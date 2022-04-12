package kr.co.yooooon.base.controller;


import java.util.ArrayList;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import kr.co.yooooon.base.sf.BaseServiceFacade;

import kr.co.yooooon.base.to.BoardTO;
import kr.co.yooooon.common.exception.DataAccessException;

import kr.co.yooooon.common.to.ListForm;


public class BoardListController1 extends AbstractController{
	 private BaseServiceFacade baseServiceFacade;
		
	 public void setBaseServiceFacade(BaseServiceFacade baseServiceFacade) {
	      this.baseServiceFacade = baseServiceFacade;
	   }
	 private ModelAndView mav=null;
		private ModelMap map = new ModelMap(); 
	
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {
	
	
		ArrayList<BoardTO> list=null;
		ListForm boardList = null;
		int pagenum, sr, er, dbCount, selectValue;
		try {
			
			pagenum=1;
			if(request.getParameter("pn")!=null){
				pagenum=Integer.parseInt(request.getParameter("pn"));
			}
			selectValue=Integer.parseInt(request.getParameter("selectValue"));
			boardList=new ListForm();
			dbCount=baseServiceFacade.getRowCount();
			boardList.setRowsize(selectValue);
			boardList.setDbcount(dbCount);
			boardList.setPagenum(pagenum);
			sr=boardList.getStartrow();
			er=boardList.getEndrow();
			list=baseServiceFacade.getBoardList(sr,er);
			boardList.setList(list);
					
			map.put("errorMsg", "success");
			map.put("errorCode", 0);
			map.put("boardlist", list);
			map.put("board", boardList);
			
		}  catch (DataAccessException dae) {
			
			map.clear();
			map.put("errorCode", -1);
			map.put("errorMsg", dae.getMessage());
			
		}
		mav = new ModelAndView("jsonView",map);
		return mav;
	}
}
