package kr.co.yooooon.base.controller;


import java.util.HashMap;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import kr.co.yooooon.base.sf.BaseServiceFacade;
import kr.co.yooooon.base.to.ReportTO;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class SendEmailController extends MultiActionController{
	  
	//https://ktko.tistory.com/entry/JAVA-SMTP%EC%99%80-Mail-%EB%B0%9C%EC%86%A1%ED%95%98%EA%B8%B0Google-Naver
	
	private BaseServiceFacade baseServiceFacade;

	public void setBaseServiceFacade(BaseServiceFacade baseServiceFacade) {
		this.baseServiceFacade = baseServiceFacade;
	}
	
    private Multipart multipart; 
    private ModelAndView  mav = new ModelAndView();

    public ModelAndView sendEmail(HttpServletRequest request, HttpServletResponse response) {
    	String empCode = request.getParameter("empCode");
		String usage = request.getParameter("usage");
		String requestDay = request.getParameter("requestDay");
		String useDay = request.getParameter("useDay");
    	
    	ReportTO to = baseServiceFacade.findViewReport(empCode);
       HashMap<String, Object> map = new HashMap<>();

	      map.put("empCode", empCode);
		  map.put("usage", usage);
		  map.put("date", requestDay);
	   	  map.put("end", useDay);
		  map.put("empName",to.getEmpName());
		  map.put("hiredate",to.getHiredate());
		  map.put("occupation",to.getOccupation());
		  map.put("employmentType",to.getEmploymentType());
		  map.put("position",to.getPosition());
		  map.put("address",to.getAddress());
		  map.put("detailAddress",to.getDetailAddress());
		  map.put("deptName",to.getDeptName());
       
		  JRDataSource da=new JREmptyDataSource();
       
       String recipient = request.getParameter("eMail"); // view?????? ?????? ???????????????
       System.out.println(recipient);
       final String user = "alstkd1q1q@naver.com";    //?????????????????????
       final String password = "alstkd1q1q!@"; 
     
       String host = "smtp.naver.com";  
       int port =587;  
      
     //?????? ????????? ??????
       String subject = "???????????????"; 
       
     //?????? ????????? ??????
       String body = user+"?????? ????????? ???????????????.";
 
       JasperReport jasperReport;
       JasperPrint jasperPrint;
     
       
       try {   
    	
          jasperReport = JasperCompileManager.compileReport("C:\\dev\\insaSpring\\insa\\src\\main\\webapp\\report\\employment.jrxml");  
       
          jasperPrint = JasperFillManager.fillReport(jasperReport, map, da);
          JasperExportManager.exportReportToPdfFile(jasperPrint, "C:\\dev\\insaSpring\\insa\\src\\main\\webapp\\report\\test01.pdf"); //  ???????????? ??????

        
          Properties prop= new Properties();
          prop.put("mail.smtp.host", "smtp.naver.com");  //????????? ????????? ???????????? stmp ??????
          prop.put("mail.smtp.port", port);
          prop.put("mail.smtp.auth", "true");
          prop.put("mail.smtp.ssl.enable", "true"); 
          prop.put("mail.smtp.ssl.trust", host);


   


          Session session = Session.getDefaultInstance(prop, new javax.mail.Authenticator() 
          {
        	  protected PasswordAuthentication getPasswordAuthentication() {      
               
        	  return new javax.mail.PasswordAuthentication(user, password);
             }
          });
        

           MimeMessage message = new MimeMessage(session);
            
             message.setFrom(new InternetAddress(user));  
          
             
             //????????? ????????????
             message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
           
              //????????????
             message.setSubject(subject);
       
             
              multipart = new MimeMultipart(); 
                   
            
             MimeBodyPart mbp1 = new MimeBodyPart();
            
           //?????? ????????? ??????
                mbp1.setText(body);
                multipart.addBodyPart(mbp1);

            
           
                   DataSource source = new FileDataSource("C:\\dev\\insaSpring\\insa\\src\\main\\webapp\\report\\test01.pdf"); 
                   BodyPart messageBodyPart = new MimeBodyPart();
                   messageBodyPart.setDataHandler(new DataHandler(source));
                   messageBodyPart.setFileName("test.pdf"); 
                   multipart.addBodyPart(messageBodyPart);
            
              
                message.setContent(multipart);
                   
                
                //?????? ??????
                Transport.send(message);
                mav.addObject("errorMsg", "?????? ????????? ?????????????????????");
                mav.addObject("errorCode", 0);
       } catch (Exception e) {
    	   
    	   mav.addObject("errorCode", -1);
           mav.addObject("errorMsg", "??????????????? ?????????????????????.");
          e.printStackTrace();
       
       }
       mav.setViewName("jsonView");
		return mav;
       
    }
}