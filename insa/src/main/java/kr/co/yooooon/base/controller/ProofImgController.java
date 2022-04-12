package kr.co.yooooon.base.controller;

import java.io.IOException;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import kr.co.yooooon.common.util.FileUploadUtil2;
import kr.co.yooooon.hr.certificate.sf.CertificateServiceFacade;


public class ProofImgController extends AbstractController {
	
	
	
	 private CertificateServiceFacade certificateServiceFacade;
		
	 public void setCertificateServiceFacade(CertificateServiceFacade certificateServiceFacade) {
	      this.certificateServiceFacade = certificateServiceFacade;
	   }
	 
	 
	    private ModelAndView mav=null;
		private ModelMap map = new ModelMap();  //ModelMap - 클래스


	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {

		
		
        DiskFileItemFactory factory = new DiskFileItemFactory(); 
        ServletFileUpload upload = new ServletFileUpload(factory);
        RequestContext rc = new ServletRequestContext(request);
        
        String cashCode = null;
        String empImgUrl = null;
        String check = (String)request.getSession().getAttribute("newcheck");
        int newCheck = 0;

        try {
        	

	        @SuppressWarnings("unchecked")
			
			List<FileItem> items = upload.parseRequest(rc);
	        for (FileItem fileItem : items){  
	        
	        	if(fileItem.isFormField()){ 
	        		if("cashCode".equals(fileItem.getFieldName())){
	        			cashCode = fileItem.getString();  //湲덉븸
	        			System.out.println("cash:"+cashCode);
	        		}
	        		if("newcheck".equals(fileItem.getFieldName())){
	        			check = fileItem.getString();
	        			System.out.println("check:"+check);

	        		}
	        	} else { 
	        		if((fileItem.getName() != null) && (fileItem.getSize() > 0)){ 
	        			empImgUrl = FileUploadUtil2.doFileUpload(request, fileItem, cashCode); 
	        			System.out.println("empImgUrl:"+empImgUrl);
	        		}
	        	}
	        }
	        
	        if("1".equals(check)) {
	        	newCheck = 1;
	        }

	       if(newCheck == 0) {
	        	
	        
	        	certificateServiceFacade.rsgistProofImg(cashCode, empImgUrl.substring(empImgUrl.lastIndexOf(".")+1));         
	        	}
	        
	        
	       map.put("empImgUrl", empImgUrl);
	       map.put("errorCode", 0);
	       map.put("errorMsg", "사진저장에 성공했습니다");
        } catch (FileUploadException e){
        	
        	map.put("errorCode", -1);
        	map.put("errorMsg", "사진저장실패");
        } catch (IOException e){
        	logger.fatal(e.getMessage());
        	map.put("errorCode", -1);
        	map.put("errorMsg", "사진저장실패");
        }
    	mav = new ModelAndView("jsonView",map);
		return mav;
        
	}

}