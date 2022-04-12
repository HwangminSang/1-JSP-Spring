package kr.co.yooooon.hr.certificate.dao;

import java.util.ArrayList;

import kr.co.yooooon.hr.certificate.to.proofTO;

public interface ProofCertificateDAO{
	

	public void insertProofCertificateRequest(proofTO proof);
	public  ArrayList<proofTO> selectProofCertificateList(String empCode,String Code,String startDate,String endDate);
	public void deleteProof(proofTO proof);
	public ArrayList<proofTO> selectProofListByDept(String deptName, String startDate, String endDate);
	public ArrayList<proofTO> selectProofListByAllDept(String startDate);
	public void updateProof(proofTO proof);
	
	public void updateProofImg(String cashCode, String proofImg);
	

	

		
	

}
