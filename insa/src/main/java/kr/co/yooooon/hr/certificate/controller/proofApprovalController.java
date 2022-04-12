package kr.co.yooooon.hr.certificate.controller;


import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import kr.co.yooooon.common.exception.DataAccessException;

import kr.co.yooooon.hr.certificate.sf.CertificateServiceFacade;

import kr.co.yooooon.hr.certificate.to.proofTO;



public class proofApprovalController extends MultiActionController{

	 private CertificateServiceFacade certificateServiceFacade;
		
	 public void setCertificateServiceFacade(CertificateServiceFacade certificateServiceFacade) {
	      this.certificateServiceFacade = certificateServiceFacade;
	   }
	 private ModelAndView mav=null;
		private ModelMap map = new ModelMap();  //ModelMap - 클래스


	public ModelAndView findProofAttdListByDept(HttpServletRequest request, HttpServletResponse response){  //湲됱뿬愿�由�  - 利앸튃�듅�씤愿�由� 
		
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String deptName = request.getParameter("deptName");
		
		

		try {
		

			ArrayList<proofTO> proofAttdList = certificateServiceFacade.findProofListByDept(deptName, startDate, endDate);
			map.put("errorMsg","success");
			map.put("errorCode", 0);
			map.put("proofAttdList", proofAttdList);

		
		} catch (DataAccessException dae){
			logger.fatal(dae.getMessage());
			map.clear();
			map.put("errorCode", -1);
			map.put("errorMsg", dae.getMessage());
			
		} 
		mav = new ModelAndView("jsonView",map);
		return mav;
	}
	

	public ModelAndView modifyProofList(HttpServletRequest request, HttpServletResponse response) { //�닔�젙 �궘�젣 . �뾽�뜲�씠�듃 
		
		String sendData = request.getParameter("sendData");
		
		
		try {
			
			Gson gson = new Gson();
			ArrayList<proofTO> proofList = gson.fromJson(sendData, new TypeToken<ArrayList<proofTO>>(){}.getType());
		
			certificateServiceFacade.modifyProofList(proofList);
			map.put("errorMsg","success");
			map.put("errorCode", 0);

		}  catch (DataAccessException dae) {
			logger.fatal(dae.getMessage());
			map.clear();
			map.put("errorCode", -1);
			map.put("errorMsg", dae.getMessage());
			
		}
		mav = new ModelAndView("jsonView",map);
		return mav;
	}
}
