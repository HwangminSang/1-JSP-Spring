package kr.co.yooooon.base.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import kr.co.yooooon.base.sf.BaseServiceFacade;

import kr.co.yooooon.base.to.DeptTO;




public class DeptListController extends MultiActionController {	
	
	
	 private BaseServiceFacade baseServiceFacade;
		
	 public void setBaseServiceFacade(BaseServiceFacade baseServiceFacade) {
	      this.baseServiceFacade = baseServiceFacade;
	   }
	 

	 private ModelAndView mav=null;
	private ModelMap map = new ModelMap();  //ModelMap - 클래스

	
	
	
	public ModelAndView batchDeptProcess(HttpServletRequest request, HttpServletResponse response) {  //�떆�뒪�뀥愿�由�  遺��꽌愿�由� 紐⑸줉 �닔�젙�떆 
	
		String sendData = request.getParameter("sendData"); // row媛� 異붽��맂 �뜲�씠�꽣�굹 , status媛� 蹂�寃쎈맂 �뜲�씠�꽣媛� �뱾�뼱�엳�쓬 

		Gson gson = new Gson();
		
		ArrayList<DeptTO> deptto = gson.fromJson(sendData, new TypeToken<ArrayList<DeptTO>>(){}.getType()); // 蹂�寃� 
		
		
		
		try {
			
		
			baseServiceFacade.batchDeptProcess(deptto); 
			
			map.put("errorCode", 0);
			map.put("errorMsg", request.getParameter("deptName")+" 遺��꽌媛� �벑濡�/�궘�젣媛� �셿猷뚮릺�뿀�뒿�땲�떎.");
			
		} catch (Exception e) {
			
			map.put("errorCode", -1);
			map.put("errorMsg", e.getMessage());
			
			
		}
		mav = new ModelAndView("jsonView",map);
		return mav;
	}
	
	
	public ModelAndView findDeptList(HttpServletRequest request, HttpServletResponse response) { //遺��꽌議고쉶
		HashMap<String, Object> map = new HashMap<String, Object>();
		

		
		try {
			
			
			List<DeptTO> list = baseServiceFacade.findDeptList();
			
			DeptTO emptyBean = new DeptTO();
			
			map.put("emptyBean", emptyBean);
			map.put("list", list);

			
		
			
		}catch (Exception e) {
			
			map.put("errorCode", -1);
			map.put("errorMsg", e.getMessage());
			
			
			
		}
		
		mav = new ModelAndView("jsonView",map);
		return mav;
	}
}
