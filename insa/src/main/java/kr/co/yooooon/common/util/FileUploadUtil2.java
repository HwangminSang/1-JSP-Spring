package kr.co.yooooon.common.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;


public class FileUploadUtil2 {
	public static String doFileUpload(HttpServletRequest request, FileItem fileitem, String cashCode)
			throws FileNotFoundException, IOException {

			InputStream in = fileitem.getInputStream();
			String fileName = fileitem.getName().substring(fileitem.getName().lastIndexOf("\\")+1);
			
			String fileExt = fileName.substring(fileName.lastIndexOf("."));  
			String saveFileName = cashCode + fileExt;
			
			
		
			String path2 = "C:\\dev\\insaSpring\\insa\\src\\main\\webapp\\profile\\" + saveFileName; 
			String path3 = "C:\\apache-tomcat-9.0.50\\webapps\\insa2\\profile\\" + saveFileName; 
			
		
			FileOutputStream fout2 = new FileOutputStream(path2); 
			FileOutputStream fout3 = new FileOutputStream(path3);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = in.read(buffer, 0, 8192)) != -1) {
			
				fout2.write(buffer, 0, bytesRead);
				fout3.write(buffer, 0, bytesRead);
			}
			in.close();
		
			fout2.close();
			fout3.close();

			return "C:\\dev\\insaSpring\\insa\\src\\main\\webapp\\profile\\" + saveFileName;
		}
}
