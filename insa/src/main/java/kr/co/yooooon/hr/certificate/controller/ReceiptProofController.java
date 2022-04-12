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


public class ReceiptProofController extends MultiActionController{
	
	 private CertificateServiceFacade certificateServiceFacade;
		
	 public void setCertificateServiceFacade(CertificateServiceFacade certificateServiceFacade) {
	      this.certificateServiceFacade = certificateServiceFacade;
	   }
	 private ModelAndView mav=null;
		private ModelMap map = new ModelMap();  //ModelMap - 클래스

	
	public ModelAndView proofRequest(HttpServletRequest request, HttpServletResponse response) {  //�떊泥�
	
		String sendData = request.getParameter("sendData");
		
		try {
			
			Gson gson = new Gson();
			proofTO proof = gson.fromJson(sendData, proofTO.class); //�쁺�닔利� �뀒�씠釉�
			
			certificateServiceFacade.proofRequest(proof);
			
			map.put("errorMsg", "success");
			map.put("errorCode", 0);

		
		}  catch (DataAccessException dae) {
		
			map.clear();
			map.put("errorCode", -1);
			map.put("errorMsg", dae.getMessage());
			
	
		}
		mav = new ModelAndView("jsonView",map);
		return mav;
	}

	public ModelAndView proofLookupList(HttpServletRequest request, HttpServletResponse response) { //利앸튃�꽌瑜� 議고쉶
		
		String empCode = request.getParameter("empCode");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");		
		String Code = request.getParameter("code");
		
		try {
			
			ArrayList<proofTO> proofList = certificateServiceFacade.findProofLookupList(empCode,Code,startDate, endDate);
			map.put("proofList", proofList);
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
	
	public ModelAndView removeProofAttdList(HttpServletRequest request, HttpServletResponse response) {  //�궘�젣
	
		String sendData=request.getParameter("sendData");
		
		try {
		
			Gson gson=new Gson();
			ArrayList<proofTO> proofList=gson.fromJson(sendData, new TypeToken<ArrayList<proofTO>>(){}.getType()); //諛곗뿴�뿉 �떞寃⑥꽌 �삤湲곕뻹臾몄뿉 �씠�젃寃�
			certificateServiceFacade.removeProofRequest(proofList);
			map.put("errorMsg", "success");
			map.put("errorCode", 0);
			
			
		}catch(DataAccessException dae) {
			
			map.clear();
			map.put("errorCode", -1);
			map.put("errorMsg", dae.getMessage());
			
			
		}
		
		mav = new ModelAndView("jsonView",map);
		return mav;
		
	}
}
