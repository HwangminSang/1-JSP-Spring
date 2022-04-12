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

import kr.co.yooooon.hr.certificate.to.CertificateTO;


public class CertificateApprovalController extends MultiActionController{
	
	  private CertificateServiceFacade certificateServiceFacade;
		
		 public void setCertificateServiceFacade(CertificateServiceFacade certificateServiceFacade) {
		      this.certificateServiceFacade = certificateServiceFacade;
		   }
		 private ModelAndView mav=null;
			private ModelMap map = new ModelMap();  //ModelMap - 클래스

	
	public ModelAndView findCertificateListByDept(HttpServletRequest request, HttpServletResponse response) { // �씤�궗愿�由� - �옱吏곸쬆紐낆꽌 愿�由� �솕硫댁뿉�꽌 �궗�슜 
	
		String deptName = request.getParameter("deptName");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");		
		
		try {
		
			ArrayList<CertificateTO> certificateList = certificateServiceFacade.findCertificateListByDept(deptName, startDate, endDate);
			map.put("certificateList", certificateList);
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
	
	public ModelAndView modifyCertificateList(HttpServletRequest request, HttpServletResponse response) {   // �닔�젙  �씤�궗愿�由� - �옱吏곸쬆紐낆꽌 愿�由� �솕硫댁뿉�꽌 �궗�슜  �솗�젙 �닃���쓣�뻹 
		
		String sendData = request.getParameter("sendData"); 
		
		
		try {
			
			Gson gson = new Gson();
			ArrayList<CertificateTO> certificateList = gson.fromJson(sendData, new TypeToken<ArrayList<CertificateTO>>(){}.getType());
			certificateServiceFacade.modifyCertificateList(certificateList);
			map.put("errorMsg","success");
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
