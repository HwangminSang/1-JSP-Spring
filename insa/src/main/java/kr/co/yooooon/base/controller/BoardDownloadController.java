package kr.co.yooooon.base.controller;



import java.io.OutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;



public class BoardDownloadController extends AbstractController{

	 private ModelAndView mav=null;
	private ModelMap map = new ModelMap();  //ModelMap - 클래스
	OutputStream servletoutputstream1 = null;
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response){
		response.setCharacterEncoding("utf-8");		
		String tempFileName = request.getParameter("tempFileName");
		String fileName = request.getParameter("fileName");
		

		try {
		

			String filePath="C:/upload/"+tempFileName;
			java.io.File tempFile = new java.io.File(filePath);
			int filesize = (int) tempFile.length();		
			response.setContentType("application/octet-stream");
			response.setHeader("Content-disposition", "attachment;filename=" + "" + new String(fileName.getBytes(),"iso-8859-1"));
			response.setHeader("Content-Transper-Encoding","binary");
			response.setContentLength(filesize);

			servletoutputstream1 = response.getOutputStream();

			dumpFile(tempFile, servletoutputstream1);

			servletoutputstream1.flush();
		

		}catch (IOException e){
      
        	map.clear();
        	map.put("errorCode", -1);
        	map.put("errorMsg", e.getMessage());			
			
        }catch (Exception e){
        	
        	map.clear();
        	map.put("errorCode", -1);
        	map.put("errorMsg", e.getMessage());			
			
        }
		mav = new ModelAndView("jsonView",map);
		return mav;
	}
	private void dumpFile(File realFile, OutputStream outputstream) {
		byte[]  readByte = new byte[4096];
		try {
			BufferedInputStream bufferedinputstream = new BufferedInputStream(new FileInputStream(realFile));
			int i;
			while ((i = bufferedinputstream.read(readByte, 0, 4096)) != -1)
				outputstream.write(readByte, 0, i);		
				outputstream.close();
				bufferedinputstream.close();
		} catch (Exception _ex) {
			_ex.printStackTrace();
        	logger.fatal(_ex.getMessage());
		}
	}
}
