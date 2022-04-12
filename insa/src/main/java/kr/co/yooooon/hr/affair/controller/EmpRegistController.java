package kr.co.yooooon.hr.affair.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import kr.co.yooooon.common.exception.DataAccessException;

import kr.co.yooooon.hr.affair.sf.AffairServiceFacade;

import kr.co.yooooon.hr.affair.to.EmpTO;



public class EmpRegistController extends MultiActionController{
	
	
	 private AffairServiceFacade affairServiceFacade;
		
	 public void setAffairServiceFacade(AffairServiceFacade affairServiceFacade) {
	      this.affairServiceFacade = affairServiceFacade;
	   }
	
	    private ModelAndView mav=null;
		private ModelMap map = new ModelMap();  //ModelMap - 클래스

	
	public ModelAndView registEmployee(HttpServletRequest request, HttpServletResponse response) {
		
		
		String sendData = request.getParameter("sendData");
		
		
		try {
			
			Gson gson = new Gson();  
			EmpTO emp = gson.fromJson(sendData, new TypeToken<EmpTO>(){}.getType());
			
			affairServiceFacade.registEmployee(emp); 

			map.put("errorMsg","request.getParameter(\"emp_name\")+\" �궗�썝�씠 �벑濡앸릺�뿀�뒿�땲�떎.\"");
			map.put("errorCode", 0);

					
			
		} catch (DataAccessException dae){
			
			
			map.put("errorMsg", "�궗�썝 �벑濡앹뿉 �떎�뙣�뻽�뒿�땲�떎 : "+dae.getMessage());
			map.put("errorCode", -1);
            
		} catch(Exception e) {
		
			
			map.put("errorMsg", e.getMessage());
			map.put("errorCode", -1);

		
		}
		
		mav = new ModelAndView("jsonView",map);
		return mav;
	}
	
	public ModelAndView findLastEmpCode(HttpServletRequest request, HttpServletResponse response){  // �궗�썝�벑濡앹떆 �궗踰덉깮�꽦 踰꾪듉 �겢由��떆 
		
		try {
			
			String empCode = affairServiceFacade.findLastEmpCode();
			map.put("lastEmpCode", empCode);  //A490080
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
