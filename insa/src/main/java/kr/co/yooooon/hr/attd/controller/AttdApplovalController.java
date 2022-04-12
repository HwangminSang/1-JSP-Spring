package kr.co.yooooon.hr.attd.controller;


import java.util.ArrayList;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import kr.co.yooooon.common.exception.DataAccessException;

import kr.co.yooooon.hr.attd.sf.AttdServiceFacade;

import kr.co.yooooon.hr.attd.to.RestAttdTO;


public class AttdApplovalController extends MultiActionController{
	  private AttdServiceFacade attdServiceFacade;
		
		 public void setAttdServiceFacade(AttdServiceFacade attdServiceFacade) {
		      this.attdServiceFacade = attdServiceFacade;
		   }
		 
		 private ModelAndView mav=null;
			private ModelMap map = new ModelMap();  //ModelMap - 클래스

	public ModelAndView findRestAttdListByDept(HttpServletRequest request, HttpServletResponse response){  //議고쉶 
		
		String startDate = request.getParameter("startDate"); // 2021-10-13
		String endDate = request.getParameter("endDate");  // 2021-10-14
		String deptName = request.getParameter("deptName");  // (deptcode)媛� �떞源�

		

		try {
		

			ArrayList<RestAttdTO> restAttdList = attdServiceFacade.findRestAttdListByDept(deptName, startDate, endDate);
			map.put("errorMsg","success");
			map.put("errorCode", 0);
			map.put("restAttdList", restAttdList);

			

		}  catch (DataAccessException dae){
			
			map.clear();
			map.put("errorCode", -1);
			map.put("errorMsg", dae.getMessage());
			
		}
		mav = new ModelAndView("jsonView",map);
		return mav;
	}
	

	public ModelAndView modifyRestAttdList(HttpServletRequest request, HttpServletResponse response){  /// 寃곗젣�듅�씤愿�由ъ뿉�꽌 �솗�젙�쓣 �닃���쓣�븿 
	
		String sendData = request.getParameter("sendData");
	

		try {
					
			Gson gson = new Gson();
			ArrayList<RestAttdTO> restAttdList = gson.fromJson(sendData, new TypeToken<ArrayList<RestAttdTO>>(){}.getType());
			attdServiceFacade.modifyRestAttdList(restAttdList);
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
