package kr.co.yooooon.hr.affair.applicationService;

import java.util.ArrayList;


import kr.co.yooooon.base.applicationService.BaseApplicationService;

import kr.co.yooooon.base.dao.AdminCodeDAO;
import kr.co.yooooon.base.dao.BoardDAO;
import kr.co.yooooon.base.dao.CodeDAO;
import kr.co.yooooon.base.dao.DeptDAO;
import kr.co.yooooon.base.dao.DetailCodeDAO;
import kr.co.yooooon.base.dao.HolidayDAO;
import kr.co.yooooon.base.dao.MenuDAO;
import kr.co.yooooon.base.dao.PositonDAO;
import kr.co.yooooon.base.dao.ReportDAO;
import kr.co.yooooon.hr.affair.dao.CareerInfoDAO;
import kr.co.yooooon.hr.affair.dao.CareerInfoDAOImpl;
import kr.co.yooooon.hr.affair.dao.EducationInfoDAO;
import kr.co.yooooon.hr.affair.dao.EducationInfoDAOImpl;
import kr.co.yooooon.hr.affair.dao.EmpDAO;
import kr.co.yooooon.hr.affair.dao.EmpDAOImpl;
import kr.co.yooooon.hr.affair.dao.FamilyInfoDAO;
import kr.co.yooooon.hr.affair.dao.FamilyInfoDAOImpl;
import kr.co.yooooon.hr.affair.dao.LicenseInfoDAO;
import kr.co.yooooon.hr.affair.dao.LicenseInfoDAOImpl;
import kr.co.yooooon.hr.affair.dao.WorkInfoDAO;
import kr.co.yooooon.hr.affair.dao.WorkInfoDAOImpl;
import kr.co.yooooon.hr.affair.to.CareerInfoTO;
import kr.co.yooooon.hr.affair.to.EducationInfoTO;
import kr.co.yooooon.hr.affair.to.EmpTO;
import kr.co.yooooon.hr.affair.to.FamilyInfoTO;
import kr.co.yooooon.hr.affair.to.LicenseInfoTO;
import kr.co.yooooon.hr.affair.to.WorkInfoTO;

public class AffairApplicationServiceImpl implements AffairApplicationService {

	private EmpDAO empDAO;
	private WorkInfoDAO workInfoDAO ;
	private CareerInfoDAO careerInfoDAO;
	private EducationInfoDAO educationInfoDAO ;
	private LicenseInfoDAO licenseInfoDAO;
	private FamilyInfoDAO familyInfoDAO ;
	private BaseApplicationService baseApplicationService;
	
	
	public void setBaseApplicationService(BaseApplicationService baseApplicationService) {
		this.baseApplicationService = baseApplicationService;
	}
	
	
	public void setEmpDAO(EmpDAO empDAO) {
		this.empDAO = empDAO;
	}
	
	public void setWorkInfoDAO(WorkInfoDAO workInfoDAO ) {
		this.workInfoDAO = workInfoDAO;
	}
	
	public void setCareerInfoDAO(CareerInfoDAO careerInfoDAO) {
		this.careerInfoDAO = careerInfoDAO;
	}
	
	public void setEducationInfoDAO(EducationInfoDAO educationInfoDAO) {
		this.educationInfoDAO = educationInfoDAO; 
	}
	
	public void setLicenseInfoDAO(LicenseInfoDAO licenseInfoDAO) {
		this.licenseInfoDAO = licenseInfoDAO;
	}
	
	public void setFamilyInfoDAO( FamilyInfoDAO familyInfoDAO) {
		this.familyInfoDAO = familyInfoDAO;
	}
	
	
	
	@Override
	public EmpTO selectEmp(String id) {
	
		EmpTO empto = empDAO.selectEmp(id);
		
		return empto;
	}
	
	@Override
	public String findLastEmpCode() {
	
		String empCode = empDAO.selectLastEmpCode();		
		
		return empCode;
	}
	
	@Override
	public EmpTO findAllEmployeeInfo(String empCode) {  // 이 과정을 i바티스가 해준단  외부 result 맵사용
		
		EmpTO empTO = empDAO.selectEmployee(empCode);  
		
		ArrayList<WorkInfoTO> workInfoList = workInfoDAO.selectWorkList(empCode);
		ArrayList<CareerInfoTO> careerInfoList = careerInfoDAO.selectCareerList(empCode);
		ArrayList<EducationInfoTO> educationInfoList = educationInfoDAO.selectEducationList(empCode);
		ArrayList<LicenseInfoTO> licenseInfoList = licenseInfoDAO.selectLicenseList(empCode);
		ArrayList<FamilyInfoTO> familyInfoList = familyInfoDAO.selectFamilyList(empCode);
		empTO.setWorkInfoList(workInfoList);
		empTO.setCareerInfoList(careerInfoList);
		empTO.setEducationInfoList(educationInfoList);
		empTO.setLicenseInfoList(licenseInfoList);
		empTO.setFamilyInfoList(familyInfoList);
	
		return empTO;
	}
	
	@Override
	public ArrayList<EmpTO> findEmployeeListByDept(String deptName) { 
		ArrayList<EmpTO> empList = null;
		
	
		if (deptName.equals("전체부서")) {
			empList = empDAO.selectEmpList();
		} else if (deptName.substring(deptName.length()-1, deptName.length()).equals("팀")) {
			empList = empDAO.selectEmpListD(deptName);
				
		} else {  
			empList = empDAO.selectEmpListN(deptName);
		
		}
		
		
			return empList;
	}
	
	@Override
	public void registEmployee(EmpTO emp) {
	
		
		empDAO.registEmployee(emp); // �궗�썝異붽� �븯�뒗 �봽濡쒖떆��濡� 媛��뒗 以� 
		baseApplicationService.registEmpCode(emp); // �뵒�뀒�씪 肄붾뱶�뿉 異붽��븯�뒗 �옉�뾽�쓣 �뿬湲곗꽌 �떎�뻾�븿 
	
	}
	
	@Override
	public void modifyEmployee(EmpTO emp) {
		
		if (emp.getStatus().equals("update")) { 
			;
			empDAO.updateEmployee(emp);
		}		
		if (emp.getWorkInfoList() != null) { 
			ArrayList<WorkInfoTO> workInfoList = emp.getWorkInfoList();
			for (WorkInfoTO workInfo : workInfoList) {
				
				switch (workInfo.getStatus()) { 
				case "insert":
					workInfoDAO.insertWorkInfo(workInfo);
					break;
				case "update":
					workInfoDAO.updateWorkInfo(workInfo);
					break;
				case "delete":
					workInfoDAO.deleteWorkInfo(workInfo);
					break;
				}
			}
		}		
		
		if (emp.getCareerInfoList() != null && emp.getCareerInfoList().size() > 0) {
			ArrayList<CareerInfoTO> careerInfoList = emp.getCareerInfoList();
			for (CareerInfoTO careerInfo : careerInfoList) {
				switch (careerInfo.getStatus()) {
				case "insert":
					careerInfoDAO.insertCareerInfo(careerInfo);
					break;
				case "update":
					careerInfoDAO.updateCareerInfo(careerInfo);
					break;
				case "delete":
					careerInfoDAO.deleteCareerInfo(careerInfo);
					break;
				}
			}
		}		
		
		if (emp.getEducationInfoList() != null && emp.getEducationInfoList().size() > 0) {
			ArrayList<EducationInfoTO> educationInfoList = emp.getEducationInfoList();
			for (EducationInfoTO educationInfo : educationInfoList) {
				switch (educationInfo.getStatus()) {
				case "insert":
					educationInfoDAO.insertEducationInfo(educationInfo);
					break;
				case "update":
					educationInfoDAO.updateEducationInfo(educationInfo);
					break;
				case "delete":
					educationInfoDAO.deleteEducationInfo(educationInfo);
					break;
				}
			}
		}		
		
		if (emp.getLicenseInfoList() != null && emp.getLicenseInfoList().size() > 0) {
			ArrayList<LicenseInfoTO> licenseInfoList = emp.getLicenseInfoList();
			for (LicenseInfoTO licenseInfo : licenseInfoList) {
				switch (licenseInfo.getStatus()) {
				case "insert":
					licenseInfoDAO.insertLicenseInfo(licenseInfo);
					break;
				case "update":
					licenseInfoDAO.updateLicenseInfo(licenseInfo);
					break;
				case "delete":
					licenseInfoDAO.deleteLicenseInfo(licenseInfo);
					break;
				}
			}
		}		
		
		if (emp.getFamilyInfoList() != null && emp.getFamilyInfoList().size() > 0) {
			ArrayList<FamilyInfoTO> familyInfoList = emp.getFamilyInfoList();
			for (FamilyInfoTO familyInfo : familyInfoList) {
				switch (familyInfo.getStatus()) {
				case "insert":
					familyInfoDAO.insertFamilyInfo(familyInfo);
					break;
				case "update":
					familyInfoDAO.updateFamilyInfo(familyInfo);
					break;
				case "delete":
					familyInfoDAO.deleteFamilyInfo(familyInfo);
					break;
				}
			}
		}

		
	}
	@Override
	public void deleteEmpList(ArrayList<EmpTO> empList) {
		 
		  for(EmpTO emp : empList){
			  empDAO.deleteEmployee(emp);
			  baseApplicationService.deleteEmpCode(emp);
		  } 
		 
		  	
	}
	
	



	public String getEmpCode(String name) {
		
		
		String empCode = empDAO.getEmpCode(name);
		
		
		return empCode;
	}

}