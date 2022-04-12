package kr.co.yooooon.hr.attd.sf;

import java.util.ArrayList;




import kr.co.yooooon.common.to.ResultTO;

import kr.co.yooooon.hr.attd.applicationService.AttdApplicationService;


import kr.co.yooooon.hr.attd.sf.AttdServiceFacadeImpl;
import kr.co.yooooon.hr.attd.to.DayAttdMgtTO;
import kr.co.yooooon.hr.attd.to.DayAttdTO;
import kr.co.yooooon.hr.attd.to.MonthAttdMgtTO;
import kr.co.yooooon.hr.attd.to.RestAttdTO;
import kr.co.yooooon.hr.attd.to.annualVacationMgtTO;

public class AttdServiceFacadeImpl implements AttdServiceFacade{
   

  
	 private  AttdApplicationService  attdApplicationService;
		
	 public void setAttdApplicationService(AttdApplicationService  attdApplicationService) {
	      this.attdApplicationService = attdApplicationService;
	   }
   
   @Override
   public ArrayList<DayAttdTO> findDayAttdList(String empCode, String applyDay) {
     

  
         ArrayList<DayAttdTO> dayAttdList=attdApplicationService.findDayAttdList(empCode, applyDay);
      
         return dayAttdList;
         
     
   }
   
   @Override
   public ResultTO registDayAttd(DayAttdTO dayAttd) { // �씪洹쇳깭 湲곕줉 
     

      
         ResultTO resultTO=attdApplicationService.registDayAttd(dayAttd);
         
        
         return resultTO;
      
      
   }

   @Override
   public void removeDayAttdList(ArrayList<DayAttdTO> dayAttdList) {
    
         attdApplicationService.removeDayAttdList(dayAttdList);
       
   }
   
   @Override
   public ArrayList<RestAttdTO> findRestAttdList(String empCode, String startDate, String endDate, String code) {
     
         ArrayList<RestAttdTO> restAttdList = attdApplicationService.findRestAttdList(empCode, startDate, endDate, code);
        
         return restAttdList;
    
   }

   @Override
   public ArrayList<RestAttdTO> findRestAttdListByDept(String deptName, String startDate, String endDate) {
      // TODO Auto-generated method stub
    
         ArrayList<RestAttdTO> restAttdList = attdApplicationService.findRestAttdListByDept(deptName, startDate, endDate);
       
         return restAttdList;
    
   }  

   @Override
   public ArrayList<RestAttdTO> findRestAttdListByToday(String empCode, String toDay) {
      
     
         ArrayList<RestAttdTO> restAttdList = attdApplicationService.findRestAttdListByToday(empCode,toDay);
        
         return restAttdList;
      
   }

   @Override
   public void registRestAttd(RestAttdTO restAttd) {  //洹쇳깭 �벑濡�
      // TODO Auto-generated method stub
    
         attdApplicationService.registRestAttd(restAttd);
        
   }

   @Override
   public void modifyRestAttdList(ArrayList<RestAttdTO> restAttdList) {
      // TODO Auto-generated method stub
    
         attdApplicationService.modifyRestAttdList(restAttdList);
       
            
   }
   
   @Override
   public void removeRestAttdList(ArrayList<RestAttdTO> restAttdList) { //洹쇳깭�쇅 議고쉶�뿉�꽌 �궘�젣�떆 �닃���쓣�뻹 
      // TODO Auto-generated method stub
     
         attdApplicationService.removeRestAttdList(restAttdList);
        
   }
   
   @Override
   public ArrayList<DayAttdMgtTO> findDayAttdMgtList(String applyDay) {
    
    	  
         ArrayList<DayAttdMgtTO> dayAttdMgtList = attdApplicationService.findDayAttdMgtList(applyDay);
         
         return dayAttdMgtList;
    
   }
   
   @Override
   public ArrayList<DayAttdMgtTO> findDayAttdMgtCheckList(String applyDay) {
	  

	    
	         ArrayList<DayAttdMgtTO> dayAttdMgtList = attdApplicationService.findDayAttdMgtCheckList(applyDay);
	        
	         return dayAttdMgtList;
	      
   }

   @Override
   public void modifyDayAttdMgtList(ArrayList<DayAttdMgtTO> dayAttdMgtList) { // �씪洹쇳깭愿�由� 媛� 吏곸썝 留덇컧�븯湲�. �썡洹쇳깭 留덇컧寃��궗 �걹�굹怨� �샂 
      

      
         attdApplicationService.modifyDayAttdMgtList(dayAttdMgtList);
      
   }
   
   @Override
   public ArrayList<MonthAttdMgtTO> findMonthAttdMgtList(String applyYearMonth) {
      // TODO Auto-generated method stub
    

         ArrayList<MonthAttdMgtTO> monthAttdMgtList = attdApplicationService.findMonthAttdMgtList(applyYearMonth);
        
         return monthAttdMgtList;
      
   }
   
   @Override
   public void modifyMonthAttdMgtList(ArrayList<MonthAttdMgtTO> monthAttdMgtList) {
      // TODO Auto-generated method stub
    
         attdApplicationService.modifyMonthAttdMgtList(monthAttdMgtList);
       
   }
   @Override
   public void insertDayAttd(DayAttdTO dayAttd) {
      // TODO Auto-generated method stub
     
         attdApplicationService.insertDayAttd(dayAttd);
      
      
   }
   @Override
   public ArrayList<annualVacationMgtTO> findAnnualVacationMgtList(String applyYearMonth){
    
         ArrayList<annualVacationMgtTO> annualVacationMgtList = attdApplicationService.findAnnualVacationMgtList(applyYearMonth);
         
         return annualVacationMgtList;
      
   }
   
   @Override
   public void modifyAnnualVacationMgtList(ArrayList<annualVacationMgtTO> annualVacationMgtList) { //留덇컧 泥섎━
      // TODO Auto-generated method stub
     
         attdApplicationService.modifyAnnualVacationMgtList(annualVacationMgtList);
        
   }
   @Override
      public void cancelAnnualVacationMgtList(ArrayList<annualVacationMgtTO> annualVacationMgtList) { //留덇컧痍⑥냼
         // TODO Auto-generated method stub
        
            attdApplicationService.cancelAnnualVacationMgtList(annualVacationMgtList);
           
      }
@Override
     public void createVacation(String year) {
	// TODO Auto-generated method stub
	   
    attdApplicationService.createVacation(year);
    
	
}


}