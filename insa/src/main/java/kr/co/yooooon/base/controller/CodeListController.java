package kr.co.yooooon.base.controller;


import java.util.ArrayList;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import kr.co.yooooon.base.sf.BaseServiceFacade;

import kr.co.yooooon.base.to.CodeTO;
import kr.co.yooooon.base.to.DetailCodeTO;
import kr.co.yooooon.common.exception.DataAccessException;





public class CodeListController extends MultiActionController {
	 private BaseServiceFacade baseServiceFacade;
		
	 public void setBaseServiceFacade(BaseServiceFacade baseServiceFacade) {
	      this.baseServiceFacade = baseServiceFacade;
	   }

	 
	 private ModelAndView mav=null;
		private ModelMap map = new ModelMap();  //ModelMap - 클래스
	
	public ModelAndView detailCodelist(HttpServletRequest request, HttpServletResponse response) {  // �꽭遺�肄붾뱶議고쉶 
		
		String code = request.getParameter("code"); //CO-10
		
		try {
			
			ArrayList<DetailCodeTO> detailCodeList=baseServiceFacade.findDetailCodeList(code);
			
			map.put("detailCodeList", detailCodeList);
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
	
	
	public ModelAndView detailCodelistRest(HttpServletRequest request, HttpServletResponse response) {  // // restAttendance.jsp 洹쇳깭�쇅�떊泥� 議고쉶�뿉�꽌 �벐�엫
		
		
		String code1 = request.getParameter("code1");  //"DAC004"
		String code2 = request.getParameter("code2");  //"ADC003"
		String code3 = request.getParameter("code3");  //"ADC005"
	
		try {
		
			ArrayList<DetailCodeTO> detailCodeList=baseServiceFacade.findDetailCodeListRest(code1,code2,code3);
			map.put("detailCodeList", detailCodeList);
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
	
	public ModelAndView codelist(HttpServletRequest request, HttpServletResponse response){  //�떆�뒪�뀥愿�由� 
		response.setContentType("application/json; charset=UTF-8");
		
	
	try {
		
		ArrayList<CodeTO> codeList=baseServiceFacade.findCodeList();
		map.put("codeList", codeList);
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

}
