package kr.co.yooooon.base.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;


public class EmpLogoutController extends AbstractController {

	
	@Override
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {
		HashMap<String,Object> map=new HashMap<String,Object>();
		String viewName="redirect:loginTest.html"; //redirect �븷 �븣�뒗 property�뿉 �벑濡앸맂 key 媛믨낵 �룞�씪�빐�빞 �븳�떎
		
		try{
			
			request.getSession().invalidate();
		}catch(Exception e){
			map.put("errorCode", -1);
			map.put("errorMsg", e.getMessage());
			viewName="error";
		}
		ModelAndView modelAndView=new ModelAndView(viewName,map);
		return modelAndView;
	}
}

