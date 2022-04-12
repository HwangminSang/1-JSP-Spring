package kr.co.yooooon.base.controller;


import java.util.ArrayList;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import kr.co.yooooon.base.sf.BaseServiceFacade;

import kr.co.yooooon.base.to.AdminCodeTO;



public class AdminCodeController extends MultiActionController{
	
	
	
	 private BaseServiceFacade baseServiceFacade;
		
	 public void setBaseServiceFacade(BaseServiceFacade baseServiceFacade) {
	      this.baseServiceFacade = baseServiceFacade;
	   }
	
	 
	   private ModelAndView mav=null;
		private ModelMap map = new ModelMap();
	 
	public ModelAndView	adminCodeList(HttpServletRequest request, HttpServletResponse response) {  // 沅뚰븳愿�由� 肄붾뱶瑜� 媛��졇�삩�떎 
	
		
		
		try {
			ArrayList<AdminCodeTO> adminCodeList = baseServiceFacade.adminCodeList();		
		
			map.put("adminCodeList", adminCodeList);

			

		} catch (Exception e) {
		
			map.clear();
			map.put("errorCode", -1);
			map.put("errorMsg", e.getMessage());
		
		}
		
		mav=new ModelAndView("jsonView",map);
		return mav;
	}
	
	public ModelAndView modifyAuthority(HttpServletRequest request, HttpServletResponse response){  //沅뚰븳遺��뿬 踰꾪듉 �닃���쓣�뻹 
		
	
		
		String empCode = request.getParameter("empCode");
		String adminCode = request.getParameter("adminCode");
		
			try {
				baseServiceFacade.modifyAuthority(empCode , adminCode);		
				map.put("errorCode", 0);
				map.put("errorMsg", "Successable!");
				
			} catch (Exception e) {
				
				map.put("errorCode", -1);
				map.put("errorMsg", e.getMessage());
				
			
				
		}
		
			mav=new ModelAndView("jsonView",map);
			return mav;
	}
}
