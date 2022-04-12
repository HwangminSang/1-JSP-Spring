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

import kr.co.yooooon.hr.salary.to.BaseDeductionTO;


public class BaseDeductionController extends MultiActionController {
	
	 private SalaryServiceFacade salaryServiceFacade;
		
	 public void setSalaryServiceFacade(SalaryServiceFacade salaryServiceFacade) {
	      this.salaryServiceFacade = salaryServiceFacade;
	   }
	
	 private ModelAndView mav=null;
		private ModelMap map = new ModelMap();  //ModelMap - 클래스

	
	public ModelAndView findBaseDeductionList(HttpServletRequest request, HttpServletResponse response){
	

		try {
		
			ArrayList<BaseDeductionTO> baseDeductionList = salaryServiceFacade.findBaseDeductionList();
			BaseDeductionTO emptyBean = new BaseDeductionTO(); 
			emptyBean.setStatus("insert");
			
			map.put("baseDeductionList", baseDeductionList);
			map.put("emptyBean", emptyBean);
			map.put("errorMsg","success");
			map.put("errorCode", 0);

		
		}  catch (DataAccessException dae){
			
			map.clear();
			map.put("errorCode", -1);
			map.put("errorMsg", dae.getMessage());
			
		}
		mav = new ModelAndView("jsonView",map);
		return mav;
	}
	
	public ModelAndView batchBaseDeductionProcess(HttpServletRequest request, HttpServletResponse response){ // ag洹몃━�뱶�씠�슜 �씪愿꾩쿂由� 
		
		String sendData = request.getParameter("sendData");  
		

		try {
				
			Gson gson = new Gson();
			ArrayList<BaseDeductionTO> baseDeductionList = gson.fromJson(sendData, new TypeToken<ArrayList<BaseDeductionTO>>(){}.getType());
			salaryServiceFacade.batchBaseDeductionProcess(baseDeductionList);
			
			map.put("errorMsg","success");
			map.put("errorCode", 0);

			
		}catch (DataAccessException dae){
			logger.fatal(dae.getMessage());
			map.clear();
			map.put("errorCode", -1);
			map.put("errorMsg", dae.getMessage());
			
		}
		mav = new ModelAndView("jsonView",map);
		return mav;
	}
}
