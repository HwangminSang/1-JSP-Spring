package kr.co.yooooon.hr.affair.controller;


import java.util.ArrayList;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import kr.co.yooooon.common.exception.DataAccessException;


import kr.co.yooooon.hr.affair.sf.AffairServiceFacade;

import kr.co.yooooon.hr.affair.to.CareerInfoTO;
import kr.co.yooooon.hr.affair.to.EducationInfoTO;
import kr.co.yooooon.hr.affair.to.EmpTO;
import kr.co.yooooon.hr.affair.to.FamilyInfoTO;
import kr.co.yooooon.hr.affair.to.LicenseInfoTO;
import kr.co.yooooon.hr.affair.to.WorkInfoTO;


public class EmpDetailController extends MultiActionController {
	  
	 private AffairServiceFacade affairServiceFacade;
	
	 public void setAffairServiceFacade(AffairServiceFacade affairServiceFacade) {
	      this.affairServiceFacade = affairServiceFacade;
	   }
	

	    private ModelAndView mav=null;
		private ModelMap map = new ModelMap();  //ModelMap - 클래스

	
	
	public ModelAndView findAllEmployeeInfo(HttpServletRequest request, HttpServletResponse response){
	
		String empCode = request.getParameter("empCode");
		
		response.setContentType("application/json; charset=UTF-8");
		try{
			
			EmpTO empTO=affairServiceFacade.findAllEmpInfo(empCode);
			map.put("empBean", empTO); 
			WorkInfoTO workInfoTO = new WorkInfoTO(); //  鍮덇컼泥대�� �꽔�뼱二쇨린�쐞�빐 .
			CareerInfoTO careerInfoTO = new CareerInfoTO();
			EducationInfoTO educationInfoTO = new EducationInfoTO();
			LicenseInfoTO licenseInfoTO = new LicenseInfoTO();			
			FamilyInfoTO familyInfoTO = new FamilyInfoTO();
			
			map.put("emptyFamilyInfoBean",familyInfoTO );
			map.put("emptyCareerInfoBean", careerInfoTO);
			map.put("emptyEducationInfoBean", educationInfoTO);
			map.put("emptyLicenseInfoBean", licenseInfoTO);
			map.put("emptyWorkInfoBean", workInfoTO);
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
	
	
	public ModelAndView modifyEmployee(HttpServletRequest request, HttpServletResponse response){  //���옣�떆 �궗�슜
		
		String sendData = request.getParameter("sendData"); 
		
		try{
			
			Gson gson = new Gson();
			EmpTO emp = gson.fromJson(sendData, EmpTO.class);  // JSONobject.toBean(媛앹껜,to.class)  媛숈쓬  //Gson.fromJson(String json, Class<?> 
			affairServiceFacade.modifyEmployee(emp);
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
	
	public ModelAndView removeEmployeeList(HttpServletRequest request, HttpServletResponse response){ // �긽�꽭議고쉶�뿉�꽌 �궘�젣�떆
		
		String sendData = request.getParameter("sendData"); //[ {吏곸썝�븳紐낆젙蹂�} , { 吏곸썝�븳紐낆젙蹂�} ]
		
		
		try{
			
			Gson gson = new Gson();
			ArrayList<EmpTO> empList = gson.fromJson(sendData, new TypeToken<ArrayList<EmpTO>>(){}.getType());  // https://namocom.tistory.com/671
			affairServiceFacade.deleteEmpList(empList);
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
