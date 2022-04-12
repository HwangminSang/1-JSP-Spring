package kr.co.yooooon.common.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;

public class FileUploadUtil {
	public static String doFileUpload(HttpServletRequest request, FileItem fileitem, String empCode) throws FileNotFoundException, IOException {
		InputStream in = fileitem.getInputStream();
		String fileName = fileitem.getName().substring(fileitem.getName().lastIndexOf("\\")+1);  // 정규표현식  \\는 ---> \이다 .
		
		String fileExt = fileName.substring(fileName.lastIndexOf("."));
		
				
		String saveFileName = empCode + fileExt;
		System.out.println(saveFileName);
		String path = "C:\\dev\\insa\\insa2\\WebContent\\profile\\" + saveFileName; //  이클립스 파일 저장공로
		String Epath = "C:\\apache-tomcat-9.0.50\\webapps\\insa2\\profile\\" + saveFileName; // 내 프로젝트 톰겟경로
		FileOutputStream fout1 = new FileOutputStream(path);
		FileOutputStream fout2 = new FileOutputStream(Epath);
		int bytesRead = 0;
		byte[] buffer = new byte[8192];
		while ((bytesRead = in.read(buffer, 0, 8192)) != -1) {
			fout1.write(buffer, 0, bytesRead);
			fout2.write(buffer, 0, bytesRead);
		}
		in.close();
		fout1.close();
		fout2.close();
		return path;
	}
}
