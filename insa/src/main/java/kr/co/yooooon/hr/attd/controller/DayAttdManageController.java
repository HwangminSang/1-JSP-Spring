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

import kr.co.yooooon.hr.attd.to.DayAttdMgtTO;


public class DayAttdManageController extends MultiActionController{

	  private AttdServiceFacade attdServiceFacade;
		
		 public void setAttdServiceFacade(AttdServiceFacade attdServiceFacade) {
		      this.attdServiceFacade = attdServiceFacade;
		   }
		 private ModelAndView mav=null;
			private ModelMap map = new ModelMap();  //ModelMap - 클래스

	public ModelAndView findDayAttdMgtList(HttpServletRequest request, HttpServletResponse response){  // �씪洹쇳깭愿�由ъ뿉�꽌 議고쉶�븯湲� 
		
		
		String applyDay = request.getParameter("applyDay"); // �쟻�슜�궇吏� 2021-10-12
	
		
		


		try {
		

			ArrayList<DayAttdMgtTO> dayAttdMgtList = attdServiceFacade.findDayAttdMgtList(applyDay);
			
			map.put("dayAttdMgtList", dayAttdMgtList);
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
	public ModelAndView findDayAttdMgtCheckList(HttpServletRequest request, HttpServletResponse response){  // �씪洹쇳깭愿�由ъ뿉�꽌 議고쉶�븯湲� 

		
		String applyDay = request.getParameter("applyDay"); 


		try {
		

			ArrayList<DayAttdMgtTO> dayAttdMgtList = attdServiceFacade.findDayAttdMgtCheckList(applyDay);
			
			map.put("dayAttdMgtList", dayAttdMgtList);
			map.put("errorMsg","success");
			map.put("errorCode", 0);

			
		}catch (DataAccessException dae){
			
			map.clear();
			map.put("errorCode", -1);
			map.put("errorMsg", dae.getMessage());
			
		}
		mav = new ModelAndView("jsonView",map);
		return mav;
	}

	public ModelAndView modifyDayAttdList(HttpServletRequest request, HttpServletResponse response){
	
		String sendData = request.getParameter("sendData"); 
		

		try {
				
			Gson gson = new Gson();
			ArrayList<DayAttdMgtTO> dayAttdMgtList = gson.fromJson(sendData, new TypeToken<ArrayList<DayAttdMgtTO>>(){}.getType()); // 諛곗뿴�씠湲곕븣臾몄뿉 ���엯�쓣 TypeToken �씠�슜
			attdServiceFacade.modifyDayAttdMgtList(dayAttdMgtList);
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

}
