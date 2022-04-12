package kr.co.yooooon.base.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;


//https://velog.io/@garam0410/Java-OPEN-API-%ED%8C%8C%EC%8B%B1%ED%95%98%EA%B8%B0-JSON

public class BusanFoodController extends MultiActionController {
	 private ModelAndView mav=null;
		private ModelMap map = new ModelMap();  
	   BufferedReader br = null;
	   
	   public ModelAndView show(HttpServletRequest request, HttpServletResponse response) {
		  
		   response.setContentType("application/json; charset=UTF-8");
		   
		   BufferedReader br = null;
		   HttpURLConnection urlconnection=null;
	        try{     
	        	
	        	
	        
	        	String serviceKey = "%2BGyQgpIBRtS%2BqRZIfQoOEq1rgttG3kgs8%2B6hCjZUqHSJwrooOE5Cu%2BKUVtLy7QCPQ2Ah9ZJn3t2dPba7t%2BSJJQ%3D%3D";
	        	String pageNo ="1"; 
	        	String numOfRows="133"; 
	        	String resultType="json";  
	        	String UC_SEQ="1"; 
	        	
	        	
	       	
	            String urlstr = "http://apis.data.go.kr/6260000/FoodService/getFoodKr?"+"serviceKey=" + serviceKey +"&pageNo=" + pageNo + "&numOfRows=" + numOfRows +"&resultType=" + resultType; 

	       
	            URL url = new URL(urlstr);
	     
	            urlconnection = (HttpURLConnection) url.openConnection();
	       
	           urlconnection.setRequestMethod("GET");
	           urlconnection.setRequestProperty("Content-type", "application/json");
	         
	           if(urlconnection.getResponseCode() >= 200 && urlconnection.getResponseCode() <= 300) {
	        	   br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(),"UTF-8"));
	           } else {
	        	   br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream(),"UTF-8"));
	           }
	         
	    
	           StringBuilder sb = new StringBuilder();
	            String line="";
	            while((line = br.readLine()) != null) {
	            	sb.append(line);
	            }
	            map.put("api",sb.toString());
	               br.close();
     		      urlconnection.disconnect();
	        }catch(IOException e){
	        	map.put("fail",e.getMessage());
	        };
	          
		       
		         
       		  
	            mav=new ModelAndView("jsonView",map);
		    return mav;
	        
	   }

	}