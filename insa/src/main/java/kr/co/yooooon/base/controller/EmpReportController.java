package kr.co.yooooon.base.controller;

import java.io.OutputStream;

import java.util.HashMap;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import kr.co.yooooon.base.sf.BaseServiceFacade;

import kr.co.yooooon.base.to.ReportSalaryTO;
import kr.co.yooooon.base.to.ReportTO;


import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;


public class EmpReportController extends MultiActionController {
	
	private BaseServiceFacade baseServiceFacade;

	public void setBaseServiceFacade(BaseServiceFacade baseServiceFacade) {
		this.baseServiceFacade = baseServiceFacade;
	}



	public ModelAndView requestEmployment(HttpServletRequest request, HttpServletResponse response) {

		String empCode = request.getParameter("empCode");
		String usage = request.getParameter("usage");
		String requestDay = request.getParameter("requestDay");
		String useDay = request.getParameter("useDay");

		try {
			ReportTO to = baseServiceFacade.findViewReport(empCode);
			Map<String, Object> map = new HashMap<String, Object>();
			
			 JasperReport jasperReport =JasperCompileManager.compileReport((request.getServletContext().getRealPath("/report/employment.jrxml"))); 

			 JRDataSource da=new JREmptyDataSource();
			
			
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
			
			 OutputStream outputStream = null;
			
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map,da);
			 
			
			outputStream = response.getOutputStream();
			 
			 response.setContentType("application/pdf");
			 JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
			 outputStream.flush();
			
			 outputStream.close();
		} catch (Exception e) {
			

			logger.fatal(e.getMessage());
		} 
		return null;
	}

	public ModelAndView requestMonthSalary(HttpServletRequest request, HttpServletResponse response) { 
		HashMap<String, Object> parameters = new HashMap<>(); 
		parameters.put("empCode", request.getParameter("empCode")); 
		parameters.put("applyMonth", request.getParameter("applyMonth"));


		String empCode = request.getParameter("empCode");
		String applyMonth = request.getParameter("applyMonth");

		try {
			ReportSalaryTO to = baseServiceFacade.findViewSalaryReport(empCode, applyMonth);
			Map<String, Object> map = new HashMap<String, Object>();
		
			JasperReport jasperReport =JasperCompileManager.compileReport((request.getServletContext().getRealPath( "/report/SalaryStatement.jrxml")));
			
			
			 JRDataSource da=new JREmptyDataSource();
			

			map.put("empName", to.getEmpName());
			map.put("position", to.getPosition());
			map.put("deptName", to.getDeptName());
			map.put("hiredate", to.getHiredate());
			map.put("applyYearMonth", to.getApplyYearMonth());
			map.put("totalExtSal", to.getTotalExtSal());
			map.put("totalDeduction", to.getTotalDeduction());
			map.put("totalPayment", to.getTotalPayment());
			map.put("realSalary", to.getRealSalary());
			map.put("salary", to.getSalary());
			map.put("cost", to.getCost());
			map.put("healthIns", to.getHealthIns());
			map.put("goyongIns", to.getGoyongIns());
			map.put("janggiIns", to.getJanggiIns());
			map.put("gukmin", to.getGukmin()); 
			
			OutputStream outputStream = null; 
			 JasperPrint jasperPrint =JasperFillManager.fillReport(jasperReport, map, da); 
			 outputStream = response.getOutputStream();
				
			 response.setContentType("application/pdf");
			 
			 JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
				outputStream.flush();
				 outputStream.close();
		} catch (Exception e) {

			logger.fatal(e.getMessage());
		}
		return null;
	}
}
