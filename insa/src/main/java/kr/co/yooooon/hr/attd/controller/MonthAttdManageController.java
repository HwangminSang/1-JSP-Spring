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

import kr.co.yooooon.hr.attd.to.MonthAttdMgtTO;


public class MonthAttdManageController extends MultiActionController{
 
	    private AttdServiceFacade attdServiceFacade;
		
		 public void setAttdServiceFacade(AttdServiceFacade attdServiceFacade) {
		      this.attdServiceFacade = attdServiceFacade;
		   }
	
		 private ModelAndView mav=null;
			private ModelMap map = new ModelMap();  //ModelMap - 클래스


	public ModelAndView findMonthAttdMgtList(HttpServletRequest request, HttpServletResponse response){  // �씪洹쇳깭愿�由ъ뿉�꽌 �쟾泥대쭏媛먰븯湲� �닃���쓣�븣�룄 �샇異쒕맂�떎
		
		String applyYearMonth = request.getParameter("applyYearMonth");  // 2021-8
	
		try {
		

			ArrayList<MonthAttdMgtTO> monthAttdMgtList = attdServiceFacade.findMonthAttdMgtList(applyYearMonth);
			map.put("monthAttdMgtList", monthAttdMgtList);
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

	public ModelAndView modifyMonthAttdList(HttpServletRequest request, HttpServletResponse response){  // �썡洹쇳깭 留덇컧 諛� 泥섎━ 
		
		String sendData = request.getParameter("sendData");
		
		try {
			
			Gson gson = new Gson();
			ArrayList<MonthAttdMgtTO> monthAttdMgtList = gson.fromJson(sendData, new TypeToken<ArrayList<MonthAttdMgtTO>>(){}.getType());
			attdServiceFacade.modifyMonthAttdMgtList(monthAttdMgtList);
			map.put("errorMsg","success");
			map.put("errorCode", 0);

			
		}  catch (DataAccessException dae){
			
			map.clear();
			map.put("errorCode", -1);
		
		}
		mav = new ModelAndView("jsonView",map);
		return mav;
	} 

}
