package kr.co.yooooon.hr.attd.dao;


import java.util.HashMap;


import kr.co.yooooon.hr.attd.to.annualVacationMgtTO;

public interface AnnualVacationMgtDAO {
   public HashMap<String, Object> batchAnnualVacationMgtProcess(String applyYearMonth);
   public void updateAnnualVacationMgtList(annualVacationMgtTO annualVacationMgt);
   
   public void cancelAnnualVacationMgtList(annualVacationMgtTO annualVacationMgt);
   public void cancelAnnualVacationList(annualVacationMgtTO annualVacationMgt);
   public void updateAnnualVacationList(annualVacationMgtTO annualVacationMgt);
   public HashMap<String, Object> createVacation(String applyYear);
}