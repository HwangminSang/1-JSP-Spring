package kr.co.yooooon.base.controller;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import kr.co.yooooon.base.sf.BaseServiceFacade;

import kr.co.yooooon.base.to.BoardTO;
import kr.co.yooooon.common.exception.DataAccessException;



public class BoardDetailController extends AbstractController{
	 private BaseServiceFacade baseServiceFacade;
		
	 public void setBaseServiceFacade(BaseServiceFacade baseServiceFacade) {
	      this.baseServiceFacade = baseServiceFacade;
	   }
	 private ModelAndView mav=null;
		private ModelMap map = new ModelMap();  //ModelMap - 클래스
	
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {
	
		int board_seq;
		String sessionId=null;
		try {
			
			board_seq=Integer.parseInt(request.getParameter("board_seq"));
			sessionId=(String)request.getSession().getAttribute("id");
			BoardTO board =baseServiceFacade.getBoard(sessionId,board_seq);
					
			map.put("errorMsg", "success");
			map.put("errorCode", 0);
			map.put("board", board);
			
		}catch (DataAccessException dae) {
			
			map.clear();
			map.put("errorCode", -1);
			map.put("errorMsg", dae.getMessage());
			
		}
		mav = new ModelAndView("jsonView",map);
		return mav;
	}
}
