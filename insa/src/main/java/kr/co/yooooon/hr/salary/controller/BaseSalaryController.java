package kr.co.yooooon.hr.salary.controller;


import java.util.ArrayList;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import kr.co.yooooon.common.exception.DataAccessException;

import kr.co.yooooon.hr.salary.sf.SalaryServiceFacade;

import kr.co.yooooon.hr.salary.to.BaseSalaryTO;


public class BaseSalaryController extends MultiActionController {
	 private SalaryServiceFacade salaryServiceFacade;
		
	 public void setSalaryServiceFacade(SalaryServiceFacade salaryServiceFacade) {
	      this.salaryServiceFacade = salaryServiceFacade;
	   }
	
	 private ModelAndView mav=null;
		private ModelMap map = new ModelMap();  //ModelMap - 클래스

	public ModelAndView findBaseSalaryList(HttpServletRequest request, HttpServletResponse response){  // 湲됱뿬愿�由�  湲됱뿬湲곗�愿�由� �겢由��썑 �샇異�
	
		
		try {
		

			ArrayList<BaseSalaryTO> baseSalaryList = salaryServiceFacade.findBaseSalaryList();
			map.put("baseSalaryList", baseSalaryList);
			map.put("errorMsg","success");
			map.put("errorCode", 0);

		
		}  catch (DataAccessException dae){
			logger.fatal(dae.getMessage());
			map.clear();
			map.put("errorCode", -1);
			map.put("errorMsg", dae.getMessage());
			
		} 
		mav = new ModelAndView("jsonView",map);
		return mav;
	}

	public ModelAndView modifyBaseSalaryList(HttpServletRequest request, HttpServletResponse response){ // �닔�젙 
	
		String sendData = request.getParameter("sendData");
	

		try {
					
			Gson gson = new Gson();
			ArrayList<BaseSalaryTO> baseSalaryList = gson.fromJson(sendData, new TypeToken<ArrayList<BaseSalaryTO>>(){}.getType());
			salaryServiceFacade.modifyBaseSalaryList(baseSalaryList);
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
