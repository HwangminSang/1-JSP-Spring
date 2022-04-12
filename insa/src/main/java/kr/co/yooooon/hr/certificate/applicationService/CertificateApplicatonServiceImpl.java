package kr.co.yooooon.hr.certificate.applicationService;

import java.util.ArrayList;



import kr.co.yooooon.hr.certificate.dao.CertificateDAO;

import kr.co.yooooon.hr.certificate.dao.ProofCertificateDAO;

import kr.co.yooooon.hr.certificate.to.CertificateTO;
import kr.co.yooooon.hr.certificate.to.proofTO;

public class CertificateApplicatonServiceImpl implements CertificateApplicationService{
	
	private CertificateDAO certificateDAO;
	private ProofCertificateDAO proofCertificateDAO; 
	
	public void setCertificateDAO(CertificateDAO certificateDAO) {
		this.certificateDAO = certificateDAO;
	}
	
	public void setProofCertificateDAO( ProofCertificateDAO proofCertificateDAO ) {
		this.proofCertificateDAO = proofCertificateDAO;
	}
	
	@Override
	public void registRequest(CertificateTO certificate) {		
		// TODO Auto-generated method stub
		
		certificateDAO.insertCertificateRequest(certificate);
		
		 
	}
	@Override
	public ArrayList<CertificateTO> findCertificateList(String empCode, String startDate, String endDate) {
		// TODO Auto-generated method stub
				
				ArrayList<CertificateTO> certificateList=certificateDAO.selectCertificateList(empCode, startDate, endDate);

				
				
				return certificateList;
	}
	@Override
	public void removeCertificateRequest(ArrayList<CertificateTO> certificateList) {
		// TODO Auto-generated method stub
		
		for(CertificateTO certificate : certificateList) {
			certificateDAO.deleteCertificate(certificate);
		}
		
	
	}
	@Override
	public ArrayList<CertificateTO> findCertificateListByDept(String deptName, String startDate, String endDate) {
		ArrayList<CertificateTO> certificateList = null;
		
	
		if(deptName.equals("모든부서")) {  
			certificateList = certificateDAO.selectCertificateListByAllDept(startDate);
		
		}else {
			certificateList = certificateDAO.selectCertificateListByDept(deptName, startDate, endDate);
		}
		
		
		
		return certificateList;
	}
	@Override
	public void modifyCertificateList(ArrayList<CertificateTO> certificateList) {
		
		
		
		for(CertificateTO certificate : certificateList) {
			
		if(certificate.getStatus().equals("update")) {  
			
				certificateDAO.updateCertificate(certificate);
			
		}
		
		}
		
		
	}
	@Override
	public void proofRequest(proofTO proof) {
		// TODO Auto-generated method stub
		
		
		proofCertificateDAO.insertProofCertificateRequest(proof);
		
		
		
	}
	public ArrayList<proofTO> findProofLookupList(String empCode,String Code,String startDate, String endDate) {
		// TODO Auto-generated method stub
				
			

				ArrayList<proofTO> proofLookupList=proofCertificateDAO.selectProofCertificateList(empCode,Code,startDate, endDate);

				
				
				return proofLookupList;
	}
	
	@Override
	public void removeProofRequest(ArrayList<proofTO> proofList) {
		// TODO Auto-generated method stub
		
		for(proofTO proof : proofList) {
			proofCertificateDAO.deleteProof(proof);
		} 
		
		
	}
	@Override
	public ArrayList<proofTO> findProofListByDept(String deptName, String startDate, String endDate) {
		ArrayList<proofTO> proofList = null;
		
		
		
		if(deptName.equals("紐⑤뱺遺��꽌")) { //泥섏쓬�솕硫댁뿉 �굹�삱�븣 
			proofList = proofCertificateDAO.selectProofListByAllDept(startDate);
		}else {
			proofList = proofCertificateDAO.selectProofListByDept(deptName, startDate, endDate);
			
		}
		
		
		
		
		return proofList;
	}
	@Override
	public void modifyProofList(ArrayList<proofTO> proofList) {
	
		
		for(proofTO proof : proofList) {
			System.out.println(proof.getApplovalStatus());
			
			if(proof.getStatus().equals("update")) {
				proofCertificateDAO.updateProof(proof);
			}
		}
		
		
		
	}
	
	public void rsgistProofImg(String cashCode,String proofImg) {		
		
		
		proofCertificateDAO.updateProofImg(cashCode,proofImg);
		
		
	}
	
}
