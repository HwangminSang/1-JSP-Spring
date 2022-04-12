package kr.co.yooooon.base.controller;


import java.util.ArrayList;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.yooooon.base.sf.BaseServiceFacade;

import kr.co.yooooon.base.to.HolidayTO;
import kr.co.yooooon.common.exception.DataAccessException;



public class HolidayListController extends MultiActionController {
	  private BaseServiceFacade baseServiceFacade;
	   
	   public void setBaseServiceFacade(BaseServiceFacade baseServiceFacade) {
		      this.baseServiceFacade = baseServiceFacade;
		   }
		 
	   private ModelAndView mav=null;
		private ModelMap map = new ModelMap();  //ModelMap - 클래스

	public ModelAndView findHolidayList(HttpServletRequest request, HttpServletResponse response) { // �떆�뒪�뀥愿�由� �쑕�씪議고쉶 
		// TODO Auto-generated method stub
		
		try {
			

			ArrayList<HolidayTO> holidayList = baseServiceFacade.findHolidayList();
			HolidayTO holito = new HolidayTO();
			map.put("holidayList", holidayList);
			map.put("emptyHoilday", holito);
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

	public ModelAndView findWeekDayCount(HttpServletRequest request, HttpServletResponse response) {  // �떊泥��떆 二쇰쭚�떆媛꾩쓣 類� �떆媛꾩쓣 以��떎.
		// TODO Auto-generated method stub
		
		
		String startDate = request.getParameter("startDate"); // 2021/10/13
		String endDate = request.getParameter("endDate");  //2021/10/14
		try {
			

			String weekdayCount = baseServiceFacade.findWeekDayCount(startDate, endDate);
			map.put("weekdayCount", weekdayCount);
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

	public ModelAndView batchHoilyDayProcess(HttpServletRequest request, HttpServletResponse response) {   // �떆�뒪�뀥愿�由� �쑕�씪議고쉶�뿉�꽌 �닔�젙�궘�젣�썑 ���옣�븷�븣 �샇異쒗븯�뒗 硫붿냼�뱶 
		
		String sendData = request.getParameter("sendData");  // [] 
	

		try {
			
			ObjectMapper mapper = new ObjectMapper(); //   //https://interconnection.tistory.com/137
			ArrayList<HolidayTO> holydayList = mapper.readValue(sendData, new TypeReference<ArrayList<HolidayTO>>() {});  // (Sting , .Class)   // new TypeReference<>() {} JSON Array String" to "Java List"
												//readValue濡� json媛앹껜瑜� �옄諛붽컼泥대줈 蹂��솚

			
			baseServiceFacade.batchHoilyDayProcess(holydayList);
			map.put("errorMsg", "success");
			map.put("errorCode", 0);
			
		} catch (Exception e) {
			
			map.put("errorMsg", e.getMessage());
		
			
			
		}
		mav = new ModelAndView("jsonView",map);
		return mav;
	}

}
