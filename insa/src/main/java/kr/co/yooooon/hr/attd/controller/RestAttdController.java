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


public class RestAttdController extends MultiActionController {

	  private AttdServiceFacade attdServiceFacade;
		
		 public void setAttdServiceFacade(AttdServiceFacade attdServiceFacade) {
		      this.attdServiceFacade = attdServiceFacade;
		   }
	
		 private ModelAndView mav=null;
			private ModelMap map = new ModelMap();  //ModelMap - 클래스

	

	public ModelAndView findRestAttdListByToday(HttpServletRequest request, HttpServletResponse response) {
		
		String empCode = request.getParameter("empCode");
		String toDay = request.getParameter("toDay");
		

		try {
			
			ArrayList<RestAttdTO> restAttdList = attdServiceFacade.findRestAttdListByToday(empCode, toDay);
			map.put("errorMsg", "success");
			map.put("errorCode", 0);
			map.put("restAttdList", restAttdList);

			
		}  catch (DataAccessException dae) {
			
			map.clear();
			map.put("errorCode", -1);
			map.put("errorMsg", dae.getMessage());
			
		}
		mav = new ModelAndView("jsonView",map);
		return mav;
	}

	
	public ModelAndView registRestAttd(HttpServletRequest request, HttpServletResponse response) {  // 洹쇳깭�쇅 �떊泥� / 議고쉶  / �쑕媛��떊泥�(�삁鍮꾧뎔 寃쎌“�궗 �삤�쟾諛섏감 �삤�썑諛섏감 
		
		String sendData = request.getParameter("sendData");
		response.setContentType("application/json; charset=UTF-8");
		try {
			
			Gson gson = new Gson();
			RestAttdTO restAttd = gson.fromJson(sendData, RestAttdTO.class); // JSONOBJECT.toBean 怨� 媛숈씠 諛쏆븘�삩 array-like瑜� seting�빐二쇰뒗怨쇱젙�쓣 寃る뒗�떎.
			attdServiceFacade.registRestAttd(restAttd); //�씤�꽕�듃 
			map.put("errorMsg", "success");
			map.put("errorCode", 0);

		} catch (DataAccessException dae) {
			logger.fatal(dae.getMessage());
			map.clear();
			map.put("errorCode", -1);
			map.put("errorMsg", dae.getMessage());
			
		}
		mav = new ModelAndView("jsonView",map);
		return mav;
	}

	public ModelAndView findRestAttdList(HttpServletRequest request, HttpServletResponse response) { // 洹쇳깭�쇅 議고쉶 
		
		String empCode = request.getParameter("empCode");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String code = request.getParameter("code");
		
	
		
		try {
			
			ArrayList<RestAttdTO> restAttdList = attdServiceFacade.findRestAttdList(empCode, startDate, endDate, code); // �뀒�씠釉� 遺꾨━ �빐�넄�빞 �맆 嫄� 媛숈��뜲 議대굹 蹂묒떊媛숈쓬 �씠嫄� 
			map.put("restAttdList", restAttdList);
			map.put("errorMsg", "success");
			map.put("errorCode", 0);

			
		} catch (DataAccessException dae) {
			
			map.clear();
			map.put("errorCode", -1);
			map.put("errorMsg", dae.getMessage());
			
		}
		mav = new ModelAndView("jsonView",map);
		return mav;
	}

	public ModelAndView removeRestAttdList(HttpServletRequest request, HttpServletResponse response) {  // t�궘�젣�븯湲� 
		
		String sendData = request.getParameter("sendData");
	
		try {
			
			Gson gson = new Gson();
			ArrayList<RestAttdTO> restAttdList = gson.fromJson(sendData, new TypeToken<ArrayList<RestAttdTO>>(){}.getType());  //諭뚯뿴濡� 諛쏆븘���꽌 �뮘�뿉 TypeToken�쓣 �궗�슜
			attdServiceFacade.removeRestAttdList(restAttdList);
			map.put("errorMsg", "success");
			map.put("errorCode", 0);

		} catch (DataAccessException dae) {
		
			map.clear();
			map.put("errorCode", -1);
			map.put("errorMsg", dae.getMessage());
			
		} 
		mav = new ModelAndView("jsonView",map);
		return mav;
	}

}
