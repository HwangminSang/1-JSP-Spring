package kr.co.yooooon.hr.certificate.sf;


import java.util.ArrayList;
import kr.co.yooooon.hr.certificate.to.CertificateTO;
import kr.co.yooooon.hr.certificate.to.proofTO;


public interface CertificateServiceFacade {
	
	public void registRequest(CertificateTO certificate);	
	public ArrayList<CertificateTO> findCertificateList(String empCode, String startDate, String endDate);
	public void removeCertificateRequest(ArrayList<CertificateTO> certificateList);
	public ArrayList<CertificateTO> findCertificateListByDept(String deptName, String startDate, String endDate);
	public void modifyCertificateList(ArrayList<CertificateTO> certificateList);
	public void proofRequest(proofTO proof);
	public ArrayList<proofTO> findProofLookupList(String empCode, String Code,String startDate, String endDate);
	public void removeProofRequest(ArrayList<proofTO> proofList);
	public ArrayList<proofTO> findProofListByDept(String deptName, String startDate, String endDate);
	public void modifyProofList(ArrayList<proofTO> proofList);
	public void rsgistProofImg(String cashCode,String proofImg); // 湲덉븸怨� �뙆�씪�솗�옣�옄
	
	
}
