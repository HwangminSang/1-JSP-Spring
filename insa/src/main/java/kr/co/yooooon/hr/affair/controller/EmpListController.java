package kr.co.yooooon.hr.affair.controller;


import java.util.ArrayList;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import kr.co.yooooon.hr.affair.sf.AffairServiceFacade;

import kr.co.yooooon.hr.affair.to.EmpTO;


public class EmpListController extends MultiActionController {
	 private AffairServiceFacade affairServiceFacade;
		
	 public void setAffairServiceFacade(AffairServiceFacade affairServiceFacade) {
	      this.affairServiceFacade = affairServiceFacade;
	   }
	 private ModelAndView mav=null;
	private ModelMap map = new ModelMap();  //ModelMap - 클래스


	public ModelAndView emplist(HttpServletRequest request, HttpServletResponse response) { 
	
		try {
			
			String value ="전체부서"; //
		
			if (request.getParameter("value") != null) { 
				value = request.getParameter("value");  
			}
			
		
			ArrayList<EmpTO> list = affairServiceFacade.findEmpList(value);	 
			map.put("list", list);

			

		} catch (Exception e) {
			
			map.put("errorCode", -1);
			map.put("errorMsg", e.getMessage());
			
			
		}
		
		mav = new ModelAndView("jsonView",map);
		return mav;
	}	
}