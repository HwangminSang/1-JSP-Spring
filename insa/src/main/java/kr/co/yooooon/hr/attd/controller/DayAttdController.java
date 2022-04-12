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

import kr.co.yooooon.common.to.ResultTO;
import kr.co.yooooon.hr.attd.sf.AttdServiceFacade;

import kr.co.yooooon.hr.attd.to.DayAttdTO;


public class DayAttdController extends MultiActionController{

	  private AttdServiceFacade attdServiceFacade;
		
		 public void setAttdServiceFacade(AttdServiceFacade attdServiceFacade) {
		      this.attdServiceFacade = attdServiceFacade;
		   }
	
		 private ModelAndView mav=null;
		private ModelMap map = new ModelMap();  //ModelMap - 클래스

	public ModelAndView findDayAttdList(HttpServletRequest request, HttpServletResponse response){  // �씪洹쇳깭 �븣�븘�삤媛�   < 議고쉶> 
	
		String applyDay = request.getParameter("applyDay"); //2010-01-02    // 2021-10-12
		String empCode = request.getParameter("empCode"); //A490071
	
	
		
		try {
		

			ArrayList<DayAttdTO> dayAttdList = attdServiceFacade.findDayAttdList(empCode, applyDay); // �븳 �궗�썝�쓽 �븯猷삳룞�븞�쓽 紐⑤뱺 洹쇳깭�긽�솴�쓣 list�뿉 �떞�븘�꽌 媛��졇�삩�떎.
			map.put("dayAttdList", dayAttdList);
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

	
	public ModelAndView registDayAttd(HttpServletRequest request, HttpServletResponse response){  // 湲곕줉 �닃���쓣�뻹  <�벑濡� >  �궗�썝硫붾돱 �씪洹쇳깭湲곕줉 /議고쉶 湲곕줉�븯湲� �닃���쓣�뻹 
		
		String sendData = request.getParameter("sendData");
	
		

		try {
			
				
			Gson gson = new Gson();
			DayAttdTO dayAttd = gson.fromJson(sendData, DayAttdTO.class);  // {}   媛앹껜�쓽 紐⑤뱺 Property瑜� �떎 �븞梨꾩썙�룄 �맂�떎.
		
			ResultTO resultTO = attdServiceFacade.registDayAttd(dayAttd); //�뿉�윭硫붿꽭吏� 諛섑솚 �봽濡쒖떆���뿉�꽌 諛쏆븘�삩嫄� 
			
			map.put("errorMsg",resultTO.getErrorMsg());
			map.put("errorCode", resultTO.getErrorCode());

		

		} catch (DataAccessException dae){
			logger.fatal(dae.getMessage());
			map.clear();
			map.put("errorCode", -1);
			map.put("errorMsg", dae.getMessage());
			
		} 
		mav = new ModelAndView("jsonView",map);
		return mav;
	}

	
	public ModelAndView removeDayAttdList(HttpServletRequest request, HttpServletResponse response){ // 洹쇳깭湲곕줉 <�궘�젣> 
		
		String sendData = request.getParameter("sendData");
				

		try {
			
			Gson gson = new Gson();
			ArrayList<DayAttdTO> dayAttdList = gson.fromJson(sendData, new TypeToken<ArrayList<DayAttdTO>>(){}.getType()); // 鍮� to�뿉 set �빐以��떎 �옄�룞�쑝濡�
			System.out.println(dayAttdList);
			attdServiceFacade.removeDayAttdList(dayAttdList);
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
	
	
	public ModelAndView insertDayAttd(HttpServletRequest request, HttpServletResponse response){ //  // 硫붿꽌�뱶 �씠由� ??? 臾몄젣 �엳�쓬 
		
		String sendData = request.getParameter("sendData");
		response.setContentType("application/json; charset=UTF-8");

		try {
				
			Gson gson = new Gson();
			DayAttdTO dayAttd = gson.fromJson(sendData, new TypeToken<DayAttdTO>(){}.getType());
			attdServiceFacade.insertDayAttd(dayAttd);
			
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
}
