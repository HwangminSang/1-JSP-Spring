package kr.co.yooooon.hr.attd.applicationService;

import java.util.ArrayList;
import java.util.HashMap;


import kr.co.yooooon.common.exception.DataAccessException;
import kr.co.yooooon.common.to.ResultTO;

import kr.co.yooooon.hr.attd.dao.AnnualVacationMgtDAO;

import kr.co.yooooon.hr.attd.dao.DayAttdDAO;

import kr.co.yooooon.hr.attd.dao.DayAttdMgtDAO;

import kr.co.yooooon.hr.attd.dao.MonthAttdMgtDAO;

import kr.co.yooooon.hr.attd.dao.RestAttdDAO;

import kr.co.yooooon.hr.attd.to.DayAttdMgtTO;
import kr.co.yooooon.hr.attd.to.DayAttdTO;
import kr.co.yooooon.hr.attd.to.MonthAttdMgtTO;
import kr.co.yooooon.hr.attd.to.RestAttdTO;
import kr.co.yooooon.hr.attd.to.annualVacationMgtTO;

public class AttdApplicationServiceImpl implements AttdApplicationService{
 

   private DayAttdDAO dayAttdDAO;
   private RestAttdDAO restAttdDAO ;
   private DayAttdMgtDAO dayAttdMgtDAO ;
   private MonthAttdMgtDAO monthAttdMgtDAO ;
   private AnnualVacationMgtDAO annualVacationMgtDAO ; 
 
	public void setDayAttdDAO(DayAttdDAO dayAttdDAO) {
		this.dayAttdDAO = dayAttdDAO;
	}
	
	public void setRestAttdDAO(RestAttdDAO restAttdDAO ) {
		this.restAttdDAO = restAttdDAO;
	}
	
	public void setDayAttdMgtDAO(DayAttdMgtDAO dayAttdMgtDAO) {
		this.dayAttdMgtDAO = dayAttdMgtDAO;
	}
	
	public void setMonthAttdMgtDAO(MonthAttdMgtDAO monthAttdMgtDAO ) {
		this.monthAttdMgtDAO = monthAttdMgtDAO; 
	}
	
	public void setAnnualVacationMgtDAO(AnnualVacationMgtDAO annualVacationMgtDAO) {
		this.annualVacationMgtDAO = annualVacationMgtDAO;
	}
	
   @Override
   public ArrayList<DayAttdTO> findDayAttdList(String empCode, String applyDay) {
   
      ArrayList<DayAttdTO> dayAttdList=dayAttdDAO.selectDayAttdList(empCode, applyDay);

   
      return dayAttdList;
   }

   @Override
   public ResultTO registDayAttd(DayAttdTO dayAttd) { //�씪洹쇳깭 愿��젴 硫붿꽌�뱶 �엯�젰�떆  // �벑濡� . 硫붿꽌�뱶 �씠由� 臾몄젣�엳�쓬 
	  
	
	   ResultTO resultTO=dayAttdDAO.batchInsertDayAttd(dayAttd); // �씪愿꾩쿂由� �씤�뵒?  < �봽濡쒖떆���뿉�꽌 諛쏆븘�삩 �뿉�윭硫붿꽌吏� 肄붾뱶瑜� �떞�뒗 媛앹껜 RESULTtO
     
     
   
      return resultTO;      
      
   }

   @Override
   public void removeDayAttdList(ArrayList<DayAttdTO> dayAttdList) {
    
      for(DayAttdTO dayAttd : dayAttdList){
         dayAttdDAO.deleteDayAttd(dayAttd);
      }
    
      
   }
   
   @Override
   public ArrayList<RestAttdTO> findRestAttdListByToday(String empCode, String toDay) {
   
      ArrayList<RestAttdTO> restAttdList = restAttdDAO.selectRestAttdListByToday(empCode, toDay);
     
      return restAttdList;
   }

   @Override
   public void modifyDayAttdMgtList(ArrayList<DayAttdMgtTO> dayAttdMgtList) { // �씪洹쇳깭愿�由� �쟾泥대쭏媛먰븯湲� 
    

      for(DayAttdMgtTO dayAttdMgt : dayAttdMgtList){
    	  
         if(dayAttdMgt.getStatus().equals("update")){ // �닔�젙�씠湲곕뻹臾몄뿉 蹂대궡�삱�븣 update濡� �꽕�젙�쓣 �빐以섏꽌 �삩�떎
            dayAttdMgtDAO.updateDayAttdMgtList(dayAttdMgt);
         }
         
      }

    
   }

   @Override
   public ArrayList<MonthAttdMgtTO> findMonthAttdMgtList(String applyYearMonth) {
     

      HashMap<String, Object> resultMap = monthAttdMgtDAO.batchMonthAttdMgtProcess(applyYearMonth);
      ResultTO resultTO = (ResultTO) resultMap.get("resultTO");
      
      if(Integer.parseInt(resultTO.getErrorCode()) < 0){
         throw new DataAccessException(resultTO.getErrorMsg());
      }
      
      @SuppressWarnings("unchecked")
      ArrayList<MonthAttdMgtTO> monthAttdMgtList = (ArrayList<MonthAttdMgtTO>) resultMap.get("monthAttdMgtList");
      
  
      return monthAttdMgtList;
   }
   
   @Override
   public ArrayList<RestAttdTO> findRestAttdListByDept(String deptName, String startDate, String endDate) {
      ArrayList<RestAttdTO> restAttdList = null;
    
      
      if(deptName.equals("모든부서")) { 
         restAttdList = restAttdDAO.selectRestAttdListByAllDept(startDate); 
      }else {
         restAttdList = restAttdDAO.selectRestAttdListByDept(deptName, startDate, endDate);
      }
      
   
      return restAttdList;
   }
   
   
   @Override
   public void registRestAttd(RestAttdTO restAttd) {
    
      restAttdDAO.insertRestAttd(restAttd);
         
   }
   
   @Override
   public ArrayList<RestAttdTO> findRestAttdList(String empCode, String startDate, String endDate, String code) {
  
      ArrayList<RestAttdTO> restAttdList=null;
    
      if(code == "")
         restAttdList = restAttdDAO.selectRestAttdList(empCode, startDate, endDate);
      else
         restAttdList = restAttdDAO.selectRestAttdListCode(empCode, startDate, endDate, code);  
   
      
      return restAttdList;
   }
   
   @Override
   public void removeRestAttdList(ArrayList<RestAttdTO> restAttdList) {
    
      for(RestAttdTO restAttd : restAttdList){
         restAttdDAO.deleteRestAttd(restAttd);
      }
   }

   @Override
   public void modifyRestAttdList(ArrayList<RestAttdTO> restAttdList) {
      // TODO Auto-generated method stub
    
      for(RestAttdTO restAttd : restAttdList){
       
         if(restAttd.getStatus().equals("update")){
            restAttdDAO.updateRestAttd(restAttd);
         }
      }
    
   }
   
   @Override
   public ArrayList<DayAttdMgtTO> findDayAttdMgtList(String applyDay) {     

      HashMap<String, Object> resultMap = dayAttdMgtDAO.batchDayAttdMgtProcess(applyDay);        
      ResultTO resultTO = (ResultTO) resultMap.get("resultTO");
      
      if(Integer.parseInt(resultTO.getErrorCode()) < 0){ 
         throw new DataAccessException(resultTO.getErrorMsg()); 
      }
      
      @SuppressWarnings("unchecked")
      ArrayList<DayAttdMgtTO> dayAttdMgtList = (ArrayList<DayAttdMgtTO>) resultMap.get("dayAttdMgtList"); // objcet瑜� 諛섑솚�븯湲곕븣臾몄뿉 �삎蹂��솚�빐以��떎.

      return dayAttdMgtList;
   }
   
   
   @Override
   public ArrayList<DayAttdMgtTO> findDayAttdMgtCheckList(String applyDay) {
   	// TODO Auto-generated method stub
	 

	   ArrayList<DayAttdMgtTO> dayAttdMgtList= dayAttdMgtDAO.findDayAttdMgtCheckList(applyDay);  // 洹몃읆 �솢 object �씪源�?  resultTo�� ArrayList<DayAttdMgtTO> 媛� 媛숈씠 �떞湲곌린�뻹臾몄뿉 �몢媛쒕떎 �닔�슜媛��뒫�븳 �옄猷뚰삎�씤 OBJECT瑜� �궗�슜
	      

	    
	      return dayAttdMgtList;
   
   }
   @Override
   public void modifyMonthAttdMgtList(ArrayList<MonthAttdMgtTO> monthAttdMgtList) {
   

      for(MonthAttdMgtTO monthAttdMgt : monthAttdMgtList){ // 諛곗뿴�뿉 �떞�븘�삩 媛앹껜瑜� FOR臾몄쑝濡� �룎由곕떎.
         if(monthAttdMgt.getStatus().equals("update")){ // 留덇컧泥섎━ OR 留덇컧痍⑥냼�떆 view�뿉�꽌 update濡� �꽕�젙�빐�몺 < �쟾泥� 留덇컧 , �쟾泥� 留덇컧 痍⑥냼> 
            monthAttdMgtDAO.updateMonthAttdMgtList(monthAttdMgt);
         }
      }

    
   }
   @Override
   public void insertDayAttd(DayAttdTO dayAttd) { //test
     
      dayAttdDAO.insertDayAttd(dayAttd);
      

      
   }
   public ArrayList<annualVacationMgtTO> findAnnualVacationMgtList(String applyYearMonth){
     

      HashMap<String, Object> resultMap = annualVacationMgtDAO.batchAnnualVacationMgtProcess(applyYearMonth);
      
      ResultTO resultTO = (ResultTO) resultMap.get("resultTO");
      if(Integer.parseInt(resultTO.getErrorCode()) < 0){  
         throw new DataAccessException(resultTO.getErrorMsg());
      }
      
      @SuppressWarnings("unchecked")
      ArrayList<annualVacationMgtTO> annualVacationMgtList = (ArrayList<annualVacationMgtTO>) resultMap.get("annualVacationMgtList");
      
    
      return annualVacationMgtList;
   }
   @Override
   public void modifyAnnualVacationMgtList(ArrayList<annualVacationMgtTO> annualVacationMgtList) {  //留덇컧泥섎━�떆 
    
      for(annualVacationMgtTO annualVacationMgt : annualVacationMgtList){
         if(annualVacationMgt.getStatus().equals("update")){ // 留덇컧泥섎━�떆 update濡� status瑜� �꽕�젙�빐�몺
            annualVacationMgtDAO.updateAnnualVacationMgtList(annualVacationMgt);
            annualVacationMgtDAO.updateAnnualVacationList(annualVacationMgt);
         }
      }

      
   }
   
   @Override
   public void cancelAnnualVacationMgtList(ArrayList<annualVacationMgtTO> annualVacationMgtList) {  //留덈쭏媛먯랬�냼
     

      for(annualVacationMgtTO annualVacationMgt : annualVacationMgtList){
         if(annualVacationMgt.getStatus().equals("update")){
            annualVacationMgtDAO.cancelAnnualVacationMgtList(annualVacationMgt);
            annualVacationMgtDAO.cancelAnnualVacationList(annualVacationMgt);
         }
      }

     
   }
@Override
	public void createVacation(String year) {
	
    
    HashMap<String, Object> resultMap = annualVacationMgtDAO.createVacation(year);
    ResultTO resultTO = (ResultTO) resultMap.get("resultTO");
    if(Integer.parseInt(resultTO.getErrorCode()) < 0){  // 利� �봽濡쒖떆���뿉�꽌 �뿉�윭媛� �궗�쓣�뻹
       throw new DataAccessException(resultTO.getErrorMsg());
    }
    
}

}