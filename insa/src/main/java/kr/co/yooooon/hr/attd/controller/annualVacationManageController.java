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

import kr.co.yooooon.hr.attd.to.annualVacationMgtTO;


public class annualVacationManageController extends MultiActionController{ //�뿰李④�由�
  
	 private ModelAndView mav=null;
		private ModelMap map = new ModelMap();  //ModelMap - 클래스
  
     private AttdServiceFacade attdServiceFacade;
	
	 public void setAttdServiceFacade(AttdServiceFacade attdServiceFacade) {
	      this.attdServiceFacade = attdServiceFacade;
	   }
	
	 
   public ModelAndView findAnnualVacationMgtList(HttpServletRequest request, HttpServletResponse response){
      // TODO Auto-generated method stub
   
      String applyYearMonth = request.getParameter("applyYearMonth"); //202109
      
      try {
      

         ArrayList<annualVacationMgtTO> annualVacationMgtList = attdServiceFacade.findAnnualVacationMgtList(applyYearMonth);
         
         map.put("annualVacationMgtList", annualVacationMgtList);
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
   
   public ModelAndView modifyAnnualVacationMgtList(HttpServletRequest request, HttpServletResponse response){  // 留덇컧泥섎━ 
    
      String sendData = request.getParameter("sendData");
     
      try {
       
         Gson gson = new Gson();
         ArrayList<annualVacationMgtTO> annualVacationMgtList = gson.fromJson(sendData, new TypeToken<ArrayList<annualVacationMgtTO>>(){}.getType());  //jsonobject.fromobjct + jsonobject.tobean 鍮덇컼泥� 留뚮뱾媛� set源뚯� �떎�빐以�
         attdServiceFacade.modifyAnnualVacationMgtList(annualVacationMgtList);
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
   
   
   public ModelAndView cancelAnnualVacationMgtList(HttpServletRequest request, HttpServletResponse response){ //留덇컧痍⑥냼 
    
      String sendData = request.getParameter("sendData");
      
      try {
       
         Gson gson = new Gson();
         ArrayList<annualVacationMgtTO> annualVacationMgtList = gson.fromJson(sendData, new TypeToken<ArrayList<annualVacationMgtTO>>(){}.getType());
         attdServiceFacade.cancelAnnualVacationMgtList(annualVacationMgtList);
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
   public ModelAndView createVacation(HttpServletRequest request, HttpServletResponse response){ //�뿰李⑥깮�꽦
	      
	      String year = request.getParameter("applyYear");
	     
	      try {
	      
	         
	         attdServiceFacade.createVacation(year);
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