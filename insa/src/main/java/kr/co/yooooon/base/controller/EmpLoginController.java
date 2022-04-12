package kr.co.yooooon.base.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import kr.co.yooooon.base.sf.BaseServiceFacade;


import kr.co.yooooon.hr.affair.sf.AffairServiceFacade;

import kr.co.yooooon.hr.affair.to.EmpTO;


public class EmpLoginController extends AbstractController {

	 private BaseServiceFacade baseServiceFacade;
		
	 public void setBaseServiceFacade(BaseServiceFacade baseServiceFacade) {
	      this.baseServiceFacade = baseServiceFacade;
	   }
	 
	 private AffairServiceFacade affairServiceFacade;
		
	 public void setAffairServiceFacade(AffairServiceFacade affairServiceFacade) {
	      this.affairServiceFacade = affairServiceFacade;
	   }
	 
	 private ModelAndView mav=null;
		private ModelMap map = new ModelMap();  //ModelMap - 클래스
	

	@Override
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {
	
		
		
		
		try {
			String id = request.getParameter("id"); 
			String pw = request.getParameter("pw"); 
			  System.out.print(id);
			  System.out.print(pw);
			if (baseServiceFacade.login(id, pw,request, response)) { 
				EmpTO empto = affairServiceFacade.getEmp(id);
				
				request.getSession().setAttribute("id", id);
				request.getSession().setAttribute("name", empto.getEmpName());
				request.getSession().setAttribute("dept", empto.getDeptName());
				request.getSession().setAttribute("position", empto.getPosition());
				request.getSession().setAttribute("code", empto.getEmpCode());
				request.getSession().setAttribute("authority", empto.getAuthority());
				
			
		
				map.put("me", "enter"); 
			
								 
			}

		} catch (Exception e) {
			map.put("errorCode", -1);
			map.put("errorMsg", e.getMessage());
			
				
		}
	
		mav = new ModelAndView("jsonView",map);
		return mav;
	}
}
