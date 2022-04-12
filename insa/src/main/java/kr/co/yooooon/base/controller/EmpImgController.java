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

import kr.co.yooooon.base.sf.BaseServiceFacade;


import kr.co.yooooon.common.util.FileUploadUtil;


public class EmpImgController extends AbstractController {
	 private BaseServiceFacade baseServiceFacade;
		
	 public void setBaseServiceFacade(BaseServiceFacade baseServiceFacade) {
	      this.baseServiceFacade = baseServiceFacade;
	   }
	 private ModelAndView mav=null;
		private ModelMap map = new ModelMap();  //ModelMap - 클래스
	

	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {
	
		
		//org.apache.tomcat.util.http.fileupload package : �씤肄붾뵫���엯 - 'multipart/form-data'
        DiskFileItemFactory factory = new DiskFileItemFactory(); //�뵒�뒪�겕�뿉 �엳�뒗 �뙆�씪 �씫�뼱�삤�뒗 class
        ServletFileUpload upload = new ServletFileUpload(factory); //�씫�뼱�삩�뙆�씪 �꽌踰꾩뿉 �삱由щ뒗 �븷
        RequestContext rc = new ServletRequestContext(request);
        
        String empCode = null;
        String empImgUrl = null;
        String check = (String)request.getSession().getAttribute("newcheck");
        int newCheck = 0;
        try {
        	
	        @SuppressWarnings("unchecked")
			/*
			1. all : 紐⑤뱺 寃쎄퀬瑜� �뼲�젣
			2. cast : 罹먯뒪�듃 �뿰�궛�옄 愿��젴 寃쎄퀬 �뼲�젣
			3. dep-ann : �궗�슜�븯吏� 留먯븘�빞 �븷 二쇱꽍 愿��젴 寃쎄퀬 �뼲�젣
			4. deprecation : �궗�슜�븯吏� 留먯븘�빞 �븷 硫붿냼�뱶 愿��젴 寃쎄퀬 �뼲�젣
			5. fallthrough : switch臾몄뿉�꽌�쓽 break �늻�씫 愿��젴 寃쎄퀬 �뼲�젣
			6. finally : 諛섑솚�븯吏� �븡�뒗 finally 釉붾윮 愿��젴 寃쎄퀬 �뼲�젣
			7. null : null 遺꾩꽍 愿��젴 寃쎄퀬 �뼲�젣
			8. rawtypes : �젣�꽕由��쓣 �궗�슜�븯�뒗 �겢�옒�뒪 留ㅺ컻 蹂��닔媛� 遺덊듅�젙�씪 �븣�쓽 寃쎄퀬 �뼲�젣
			9. unchecked : 寃�利앸릺吏� �븡�� �뿰�궛�옄 愿��젴 寃쎄퀬 �뼲�젣
			10. unused : �궗�슜�븯吏� �븡�뒗 肄붾뱶 愿��젴 寃쎄퀬 �뼲�젣
			*/	        
			List<FileItem> items = upload.parseRequest(rc);
	        for (FileItem fileItem : items){
	        	System.out.println("######## fileItem"+fileItem);
	        	if(fileItem.isFormField()){ // �씪諛� �뙆�씪留덊꽣�씪�븣  true
	        		if("empCode".equals(fileItem.getFieldName())){  // �뙆�씪誘명꽣 �씠由꾩뼸�뼱�삩�떎
	        			empCode = fileItem.getString();  // empCode=xxxx   xxx瑜� 諛섑솚
	        			System.out.println("empCode:"+empCode);
	        		}
	        		if("newcheck".equals(fileItem.getFieldName())){ //???
	        			check = fileItem.getString();
	        			System.out.println("check:"+check);

	        		}
	        	} else { // �뙆�씪�씪�뻹 �뾽濡쒕뱶
	        		if((fileItem.getName() != null) && (fileItem.getSize() > 0)){  //�뙆�씪�씠 �엳怨�  �궗�씠利덇� 0蹂대떎 �겢�븣 
	        			empImgUrl = FileUploadUtil.doFileUpload(request, fileItem, empCode);
	        			System.out.println("empImgUrl:"+empImgUrl);
	        		}
	        	}
	        }
	        
	        if("1".equals(check)) {
	        	newCheck = 1;
	        }

	        if(newCheck == 0) {
	        	System.out.println("@@@@@@@@@@�뱾�뼱媛��굹@@@@@@@@@@@@@@@@@@@@@@@@@");
	        	baseServiceFacade.registEmpImg(empCode, empImgUrl.substring(empImgUrl.lastIndexOf(".")+1));
	        }
	        
	        
	        map.put("empImgUrl", empImgUrl); 
	        map.put("errorCode", 0);
	        map.put("errorMsg", "�궗吏� ���옣�뿉 �꽦怨듯뻽�뒿�땲�떎");
        } catch (FileUploadException e){
        	
        	map.put("errorCode", -1);
        	map.put("errorMsg", "�궗吏� ���옣�뿉 �떎�뙣�뻽�뒿�땲�떎");
        } catch (IOException e){
        	
        	map.put("errorCode", -1);
        	map.put("errorMsg", "�궗吏� ���옣�뿉 �떎�뙣�뻽�뒿�땲�떎");
        }

        mav = new ModelAndView("jsonView",map);
		return mav;
	}

}