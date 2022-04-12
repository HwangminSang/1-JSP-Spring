package kr.co.yooooon.hr.certificate.dao;

import java.util.ArrayList;

import kr.co.yooooon.hr.certificate.to.CertificateTO;

public interface CertificateDAO {
	
	public void insertCertificateRequest(CertificateTO certificate);
	public ArrayList<CertificateTO>selectCertificateList(String empCode, String startDate, String endDate);
	public void deleteCertificate(CertificateTO certificate);
	public ArrayList<CertificateTO> selectCertificateListByAllDept(String requestDate);
	public ArrayList<CertificateTO> selectCertificateListByDept(String deptName, String startDate, String endDate);
	public void updateCertificate(CertificateTO certificate);
	
		 
	
}
