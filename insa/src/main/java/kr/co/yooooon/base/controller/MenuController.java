package kr.co.yooooon.base.controller;


import java.util.ArrayList;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import kr.co.yooooon.base.sf.BaseServiceFacade;

import kr.co.yooooon.base.to.MenuTO;
import kr.co.yooooon.common.exception.DataAccessException;



public class MenuController extends MultiActionController{
	
	 private BaseServiceFacade baseServiceFacade;
	   
	   public void setBaseServiceFacade(BaseServiceFacade baseServiceFacade) {
		      this.baseServiceFacade = baseServiceFacade;
		   }
	   private ModelAndView mav=null;
		private ModelMap map = new ModelMap();
	
	public ModelAndView findMenuList(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
		try {
		

			ArrayList<MenuTO> menuList = baseServiceFacade.findMenuList();
			
			map.put("MenuList",menuList);
			map.put("errorMsg", "success");
			map.put("errorCode", 0);

		

		}  catch (DataAccessException dae) {  
		
			map.clear();
			map.put("errorCode", -1);
			map.put("errorMsg", dae.getMessage());
			
		} 
		mav = new ModelAndView("jsonView",map);
		return mav;
	}
}
