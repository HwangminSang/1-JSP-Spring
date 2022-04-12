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




public class BoardListController extends AbstractController{
	 private BaseServiceFacade baseServiceFacade;
		
	 public void setBaseServiceFacade(BaseServiceFacade baseServiceFacade) {
	      this.baseServiceFacade = baseServiceFacade;
	   }
	 private ModelAndView mav=null;
		private ModelMap map = new ModelMap();
	
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {
		
		response.setContentType("application/json; charset=UTF-8");
		try {
		
			ArrayList<BoardTO> list=baseServiceFacade.getBoardList();
					
			map.put("errorMsg", "success");
			map.put("errorCode", 0);
			map.put("boardlist", list);
			
		} catch (DataAccessException dae) {
			
			map.clear();
			map.put("errorCode", -1);
			map.put("errorMsg", dae.getMessage());
			
		}
		mav = new ModelAndView("jsonView",map);
		return mav;
	}
}
