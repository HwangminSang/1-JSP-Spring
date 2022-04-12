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

import kr.co.yooooon.hr.salary.to.BaseExtSalTO;


public class BaseExtSalController extends MultiActionController{

	 private SalaryServiceFacade salaryServiceFacade;
		
	 public void setSalaryServiceFacade(SalaryServiceFacade salaryServiceFacade) {
	      this.salaryServiceFacade = salaryServiceFacade;
	   }
		
	 
	    private ModelAndView mav=null;
		private ModelMap map = new ModelMap();  //ModelMap - 클래스


	public ModelAndView findBaseExtSalList(HttpServletRequest request, HttpServletResponse response){ // 珥덇낵�닔�떦 由ъ뒪�듃 遺덈윭�샂 
		

		try {
		
			ArrayList<BaseExtSalTO> baseExtSalList = salaryServiceFacade.findBaseExtSalList();
			map.put("baseExtSalList", baseExtSalList);
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

	public ModelAndView modifyBaseExtSalList(HttpServletRequest request, HttpServletResponse response){ // �씪愿꾩쿂由�
	
		String sendData = request.getParameter("sendData");
	

		try {
				
			Gson gson = new Gson();
			ArrayList<BaseExtSalTO> baseExtSalList = gson.fromJson(sendData, new TypeToken<ArrayList<BaseExtSalTO>>(){}.getType());
			salaryServiceFacade.modifyBaseExtSalList(baseExtSalList);
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
}
