package kr.co.yooooon.common.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.fileupload.FileItem;


public class FileUploadUtil3 {

	public static BoardFile doFileUpload(FileItem fileitem)
		throws FileNotFoundException, IOException {
	
		InputStream in = fileitem.getInputStream();
		String filename=fileitem.getName().substring(fileitem.getName().lastIndexOf("\\")+1);
		String newFileName = Long.toString(System.currentTimeMillis()+new Object().hashCode());

		//파일을 업로드할 절대 경로를 지정해야 한다. 
		String path ="C:/upload/";
		FileOutputStream fout = new FileOutputStream(path + newFileName );
		int bytesRead = 0;
		byte[] buffer = new byte[8192];
		while ((bytesRead = in.read(buffer, 0, 8192)) != -1) 
			fout.write(buffer, 0, bytesRead);
				
		in.close();
		fout.close();
		
		BoardFile boardFile = new BoardFile();
		boardFile.setFileName(filename);
		boardFile.setTempFileName(newFileName);

		return boardFile;
	}
}