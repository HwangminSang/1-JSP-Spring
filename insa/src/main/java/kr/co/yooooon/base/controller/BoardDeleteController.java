package kr.co.yooooon.base.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import kr.co.yooooon.base.sf.BaseServiceFacade;

import kr.co.yooooon.common.exception.DataAccessException;



public class BoardDeleteController extends AbstractController{
	 private BaseServiceFacade baseServiceFacade;
		
	 public void setBaseServiceFacade(BaseServiceFacade baseServiceFacade) {
	      this.baseServiceFacade = baseServiceFacade;
	   }
	 
	 
	 private ModelAndView mav=null;
		private ModelMap map = new ModelMap();  //ModelMap - 클래스
	
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {
		
		try {
		
			int board_seq=Integer.parseInt(request.getParameter("board_seq"));
			baseServiceFacade.removeBoard(board_seq);
						
			map.put("errorMsg", "寃뚯떆湲��씠 �궘�젣�릺�뿀�뒿�땲�떎");
			map.put("errorCode", 0);
			
		}catch (DataAccessException dae) {
			
			map.clear();
			map.put("errorCode", -1);
			map.put("errorMsg", dae.getMessage());
		
			
		}
		 mav = new ModelAndView("jsonView",map);
		return mav;
	}
}
