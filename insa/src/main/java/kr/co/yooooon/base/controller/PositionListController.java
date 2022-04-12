package kr.co.yooooon.base.controller;


import java.util.ArrayList;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import kr.co.yooooon.base.sf.BaseServiceFacade;

import kr.co.yooooon.base.to.PositionTO;
import kr.co.yooooon.common.exception.DataAccessException;



public class PositionListController extends MultiActionController{
	 private BaseServiceFacade baseServiceFacade;
	   
	   public void setBaseServiceFacade(BaseServiceFacade baseServiceFacade) {
		      this.baseServiceFacade = baseServiceFacade;
		   }
	   private ModelAndView mav=null;
		private ModelMap map = new ModelMap();
	
	public ModelAndView findPositionList(HttpServletRequest request, HttpServletResponse response) { 
		
		
	
			try {
			
				ArrayList<PositionTO> positionList = baseServiceFacade.findPositionList();
				PositionTO positionTO = new PositionTO();  
				
				map.put("positionList", positionList);
				map.put("emptyPositionBean", positionTO);
				map.put("errorCode",0);
				map.put("errorMsg","success");
							
				
			}  catch (DataAccessException dae){
				logger.fatal(dae.getMessage());
				map.clear();
				map.put("errorCode", -1);
				map.put("errorMsg", dae.getMessage());
				
			}
			
			mav = new ModelAndView("jsonView",map);
			return mav;
	}
	
	public ModelAndView modifyPosition(HttpServletRequest request, HttpServletResponse response){
	
		String sendData = request.getParameter("sendData");
		
		response.setContentType("application/json; charset=UTF-8");
		try{
		
			Gson gson = new Gson();                                     
			ArrayList<PositionTO> positionList = gson.fromJson(sendData , new TypeToken<ArrayList<PositionTO>>(){}.getType());
			
			baseServiceFacade.modifyPosition(positionList);
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
