package kr.co.yooooon.hr.salary.controller;


import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.google.gson.Gson;

import kr.co.yooooon.common.exception.DataAccessException;

import kr.co.yooooon.hr.salary.sf.SalaryServiceFacade;

import kr.co.yooooon.hr.salary.to.MonthSalaryTO;


public class monthSalaryController extends MultiActionController{
	 private SalaryServiceFacade salaryServiceFacade;
		
	 public void setSalaryServiceFacade(SalaryServiceFacade salaryServiceFacade) {
	      this.salaryServiceFacade = salaryServiceFacade;
	   }
	 private ModelAndView mav=null;
		private ModelMap map = new ModelMap();  //ModelMap - 클래스


	public ModelAndView findMonthSalary(HttpServletRequest request, HttpServletResponse response){ //�썡湲됱뿬 議고쉶�븯湲� 踰꾪듉
		// TODO Auto-generated method stub
	
		String applyYearMonth = request.getParameter("applyYearMonth"); 
		String empCode = request.getParameter("empCode");
		

		try {
		

			MonthSalaryTO monthSalary = salaryServiceFacade.findMonthSalary(applyYearMonth,empCode);
			map.put("monthSalary", monthSalary);
			map.put("errorMsg","success");
			map.put("errorCode", 0);

		

		} catch (DataAccessException dae){
		
			map.clear();
			map.put("errorCode", -1);
			map.put("errorMsg", dae.getMessage());
			
		}
		mav = new ModelAndView("jsonView",map);
		return mav;
	}
	
	public ModelAndView findYearSalary(HttpServletRequest request, HttpServletResponse response){  
		// TODO Auto-generated method stub
		
		String applyYear = request.getParameter("applyYear");
		String empCode = request.getParameter("empCode");
		

		try {
		

			ArrayList<MonthSalaryTO> yearSalary = salaryServiceFacade.findYearSalary(applyYear, empCode); 
			map.put("yearSalary", yearSalary);
			map.put("errorMsg","success");
			map.put("errorCode", 0);

		} catch (DataAccessException dae){
			logger.fatal(dae.getMessage());
			map.clear();
			map.put("errorCode", -1);
			map.put("errorMsg", dae.getMessage());
			
		} 
		mav = new ModelAndView("jsonView",map);
		return mav;
	}

	public ModelAndView modifyMonthSalary(HttpServletRequest request, HttpServletResponse response){  
		
		String sendData = request.getParameter("sendData");  
		response.setContentType("application/json; charset=UTF-8");

		try {
					
			Gson gson = new Gson();
		
			MonthSalaryTO monthSalary = gson.fromJson(sendData, MonthSalaryTO.class);
			salaryServiceFacade.modifyMonthSalary(monthSalary);
			map.put("errorMsg","success");
			map.put("errorCode", 0);

			
		} catch (DataAccessException dae){
			
			map.clear();
			map.put("errorCode", -1);
			map.put("errorMsg", dae.getMessage());
			
		}
		mav = new ModelAndView("jsonView",map);
		return mav;
	}
}