package kr.co.yooooon.base.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import kr.co.yooooon.base.sf.BaseServiceFacade;
import kr.co.yooooon.base.to.BoardTO;
import kr.co.yooooon.common.exception.DataAccessException;
import kr.co.yooooon.common.util.BoardFile;
import kr.co.yooooon.common.util.FileUploadUtil3;

public class BoardRegistController extends AbstractController{
	private BaseServiceFacade baseServiceFacade;
	private ModelAndView mav = null;
	private ModelMap map = new ModelMap();
	
	public void setBaseServiceFacade(BaseServiceFacade baseServiceFacade) {
		this.baseServiceFacade = baseServiceFacade;
	}
	
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response){
		
		DiskFileItemFactory factory = new DiskFileItemFactory(); 
        ServletFileUpload upload = new ServletFileUpload(factory);
        		
        BoardTO board=new BoardTO(); 
        
		String name=null,title=null,content=null,reg_date=null;
		System.out.println("이름이나오나요??"+name);
		int board_seq=0;
		try {
			List<FileItem> items=upload.parseRequest(request);
			
			for( FileItem item : items){
				if(item.isFormField()){
				
					String n=item.getFieldName();
					if(n.equals("name"))
						name=item.getString("utf-8");
					else if(n.equals("title"))
						title=item.getString("utf-8");
					else if(n.equals("content"))
						content=item.getString("utf-8");
					else if(n.equals("reg_date"))
						reg_date=item.getString("utf-8");
					else if(n.equals("board_seq"))
						board_seq=Integer.parseInt(item.getString());
				}else{
				
					String fileName=item.getName();
					if (( fileName!= null) && (item.getSize()>0) ) {
						BoardFile boardFile = FileUploadUtil3.doFileUpload(item);			
						board.addBoardFile(boardFile);
					}
				}
			}
			System.out.println("이름이나오나요??"+name);
			board.setName(name);
			board.setContent(content);
			board.setTitle(title);
			board.setBoard_seq(board_seq);
			board.setReg_date(reg_date);
			
			baseServiceFacade.addBoard(board);
			
			map.put("errorMsg", "게시글등록에 성공랬습니다");
			map.put("errorCode", 0);
		}catch (FileUploadException e){
        	map.clear();
			map.put("errorCode", -1);
			map.put("errorMsg", e.getMessage());			

        }catch (IOException ioe) {
			logger.fatal(ioe.getMessage());			
			map.clear();
			map.put("errorCode", -1);
			map.put("errorMsg", ioe.getMessage());			
		} catch (DataAccessException dae) {
			dae.printStackTrace();
			logger.fatal(dae.getMessage());
			map.clear();
			map.put("errorCode", -1);
			map.put("errorMsg", dae.getMessage());
		}
		
		mav = new ModelAndView("jsonView" , map);
	
		return mav;
	}
}
