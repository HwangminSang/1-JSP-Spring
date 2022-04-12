package kr.co.yooooon.hr.certificate.sf;

import java.util.ArrayList;



import kr.co.yooooon.hr.certificate.applicationService.CertificateApplicationService;

import kr.co.yooooon.hr.certificate.to.CertificateTO;
import kr.co.yooooon.hr.certificate.to.proofTO;

public class CertificateServiceFacadeImpl implements CertificateServiceFacade{


	
	 private CertificateApplicationService certificateApplicationService;
		
	 public void setCertificateApplicationService(CertificateApplicationService certificateApplicationService) {
	      this.certificateApplicationService = certificateApplicationService;
	   }
	

	public void registRequest(CertificateTO certificate) {
		// TODO Auto-generated method stub
				
					certificateApplicationService.registRequest(certificate);
				
		
	}
	@Override
	public ArrayList<CertificateTO> findCertificateList(String empCode, String startDate, String endDate) {
	
			ArrayList<CertificateTO> certificateList=certificateApplicationService.findCertificateList(empCode, startDate, endDate);
			
			return certificateList;
		
	}
	@Override
	public void removeCertificateRequest(ArrayList<CertificateTO> certificateList) {
		// TODO Auto-generated method stub
	
			certificateApplicationService.removeCertificateRequest(certificateList);
		
		
	}
	@Override
	public ArrayList<CertificateTO> findCertificateListByDept(String deptName, String startDate, String endDate) {
		
			ArrayList<CertificateTO> certificateList=certificateApplicationService.findCertificateListByDept(deptName, startDate, endDate);
			
			return certificateList;
		
	}
	@Override
	public void modifyCertificateList(ArrayList<CertificateTO> certificateList) { // �닔�젙 
		// TODO Auto-generated method stub
	
			certificateApplicationService.modifyCertificateList(certificateList);
		
		
	}
	public void proofRequest(proofTO proof) {
		// TODO Auto-generated method stub
			
					certificateApplicationService.proofRequest(proof);
				
		
	}
	@Override
	public ArrayList<proofTO> findProofLookupList(String empCode,String Code,String startDate, String endDate) {  // 議고쉶
		
			ArrayList<proofTO> proofLookupList=certificateApplicationService.findProofLookupList(empCode,Code,startDate, endDate);
			
			return proofLookupList;
		
	}
	
	@Override
	public void removeProofRequest(ArrayList<proofTO> proofList) {
		// TODO Auto-generated method stub
	
			certificateApplicationService.removeProofRequest(proofList);
		
		
	}
	@Override
	public ArrayList<proofTO> findProofListByDept(String deptName, String startDate, String endDate) {
		
			ArrayList<proofTO> proofList=certificateApplicationService.findProofListByDept(deptName, startDate, endDate);
			
		
			return proofList;
		
	}
	@Override
	public void modifyProofList(ArrayList<proofTO> proofList) {
		// TODO Auto-generated method stub
		
			certificateApplicationService.modifyProofList(proofList);
		
		
	}
	public void  rsgistProofImg(String cashCode,String proofImg) {  
		// TODO Auto-generated method stub
				
					certificateApplicationService.rsgistProofImg(cashCode,proofImg);
	}
	
}
