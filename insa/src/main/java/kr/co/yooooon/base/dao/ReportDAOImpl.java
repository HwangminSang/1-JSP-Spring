package kr.co.yooooon.base.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



import kr.co.yooooon.base.to.ReportSalaryTO;
import kr.co.yooooon.base.to.ReportTO;
import kr.co.yooooon.common.exception.DataAccessException;
import kr.co.yooooon.common.transaction.DataSourceTransactionManager;


public class ReportDAOImpl implements ReportDAO {
	private DataSourceTransactionManager dataSourceTransactionManager;

	public void setDataSourceTransactionManager(DataSourceTransactionManager dataSourceTransactionManager) {
		this.dataSourceTransactionManager = dataSourceTransactionManager;
	}

	@Override
	public ReportTO selectReport(String empCode) {
		ReportTO to = new ReportTO();

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			StringBuffer insertQuery = new StringBuffer();
			insertQuery.append("SELECT E.EMP_NAME as empName ");
			insertQuery.append(", TO_CHAR(W.HIREDATE,'YYYY')||'년 ' ||TO_CHAR(W.HIREDATE, 'MON')||TO_CHAR(W.HIREDATE,'DD')||'일' as hiredate ");
			insertQuery.append(", W.OCCUPATION as occupation ");
			insertQuery.append(", W.EMPLOYMENT_TYPE as employmentType ");
			insertQuery.append(", P.POSITION as position ");
			insertQuery.append(", E.ADDRESS as address ");
			insertQuery.append(", E.DETAIL_ADDRESS as detailAddress ");
			insertQuery.append(", T.DEPT_NAME as deptName ");
			insertQuery.append("FROM EMP E ");
			insertQuery.append(", WORK_INFO W ");
			insertQuery.append(", DEPT T ");
			insertQuery.append(", POSITION P ");
			insertQuery.append("WHERE E.EMP_CODE=W.EMP_CODE ");
			insertQuery.append("AND E.DEPT_CODE=T.DEPT_CODE ");
			insertQuery.append("AND E.POSITION_CODE=P.POSITION_CODE ");
			insertQuery.append("AND E.EMP_CODE = ? ");

			con = dataSourceTransactionManager.getConnection();
			pstmt = con.prepareStatement(insertQuery.toString());
			pstmt.setString(1, empCode);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				to.setEmpName(rs.getString("empName"));
				to.setHiredate(rs.getString("hiredate"));
				to.setOccupation(rs.getString("occupation"));
				to.setEmploymentType(rs.getString("employmentType"));
				to.setPosition(rs.getString("position"));
				to.setAddress(rs.getString("address"));
				to.setDetailAddress(rs.getString("detailAddress"));
				to.setDeptName(rs.getString("deptName"));
			}
			return to;
		} catch (Exception sqle) {
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(pstmt, rs);
		}
	}

	@Override
	public ReportSalaryTO selecSalarytReport(String empCode, String applyMonth) {
		ReportSalaryTO to = new ReportSalaryTO();

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			System.out.println(empCode + "/3/" + applyMonth);
			StringBuffer insertQuery = new StringBuffer();
			insertQuery.append(" SELECT e.EMP_NAME as empName ");
			insertQuery.append(", p.POSITION as position ");
			insertQuery.append(", d.DEPT_name as deptName ");
			insertQuery.append(", TO_CHAR(w.HIREDATE, 'YYYY\"년\"MM\"월\"DD\"일\"' ) as hiredate ");
			insertQuery.append(", REPLACE(ms.APPLY_YEAR_MONTH, '-', '년')||'월' as applyYearMonth ");
			insertQuery.append(", (TO_CHAR(ms.TOTAL_EXT_SAL, '9,999,999,999')) ||'원' as totalExtSal ");
			insertQuery.append(", (TO_CHAR(ms.TOTAL_DEDUCTION, '9,999,999,999')) ||'원' as totalDeduction ");
			insertQuery.append(", (TO_CHAR(ms.TOTAL_PAYMENT, '9,999,999,999'))||'원' as totalPayment ");
			insertQuery.append(", (TO_CHAR(ms.REAL_SALARY, '9,999,999,999')) ||'원' as realSalary ");
			insertQuery.append(", (TO_CHAR(ms.SALARY, '9,999,999,999'))||'원' as salary ");
			insertQuery.append(", (TO_CHAR(ms.COST, '9,999,999,999'))||'원' as cost ");
			insertQuery.append(
					", (TO_CHAR(MAX(decode(md.DEDUCTION_NAME,'건강보험',price,price,null)), '9,999,999,999'))||'원'  as healthIns ");
			insertQuery.append(
					", (TO_CHAR(MAX(decode(md.DEDUCTION_NAME,'고용보험',price,price,null)), '9,999,999,999'))||'원' as goyongIns ");
			insertQuery.append(
					", (TO_CHAR(MAX(decode(md.DEDUCTION_NAME,'장기요양보험',price,price,null)), '9,999,999,999'))||'원' as janggiIns ");
			insertQuery.append(
					", (TO_CHAR(MAX(decode(md.DEDUCTION_NAME,'국민연금',price,price,null)), '9,999,999,999'))||'원' as gukmin ");
			insertQuery
					.append(" FROM EMP e , MONTH_SALARY ms  ,  WORK_INFO w, MONTH_DEDUCTION md , BASE_SALARY p,DEPT d ");
			insertQuery.append(" WHERE e.EMP_CODE = ? ");
			insertQuery.append("AND e.EMP_CODE = w.EMP_CODE ");
			insertQuery.append("AND e.dept_code=d.dept_code ");
			insertQuery.append("AND md.APPLY_YEAR_MONTH = ms.APPLY_YEAR_MONTH ");
			insertQuery.append("AND ms.APPLY_YEAR_MONTH = ? ");
			insertQuery.append("AND P.POSITION_CODE = e.POSITION_CODE ");
			insertQuery.append("GROUP BY ");
			insertQuery.append("e.EMP_NAME, p.POSITION, d.DEPT_name, w.HIREDATE, p.BASE_SALARY ");
			insertQuery.append(
					", ms.APPLY_YEAR_MONTH, ms.SALARY, ms.TOTAL_EXT_SAL, ms.TOTAL_DEDUCTION, ms.TOTAL_PAYMENT, ms.REAL_SALARY,MS.COST ");

			con = dataSourceTransactionManager.getConnection();
			pstmt = con.prepareStatement(insertQuery.toString());
			pstmt.setString(1, empCode);
			pstmt.setString(2, applyMonth);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				to.setEmpName(rs.getString("empName"));
				to.setPosition(rs.getString("position"));
				to.setDeptName(rs.getString("deptName"));
				to.setHiredate(rs.getString("hiredate"));
				to.setApplyYearMonth(rs.getString("applyYearMonth"));
				to.setTotalExtSal(rs.getString("totalExtSal"));
				to.setTotalDeduction(rs.getString("totalDeduction"));
				to.setTotalPayment(rs.getString("totalPayment"));
				to.setRealSalary(rs.getString("realSalary"));
				to.setSalary(rs.getString("salary"));
				to.setCost(rs.getString("cost"));
				to.setHealthIns(rs.getString("healthIns"));
				to.setGoyongIns(rs.getString("goyongIns"));
				to.setJanggiIns(rs.getString("janggiIns"));
				to.setGukmin(rs.getString("gukmin"));
			}
			return to;
		} catch (Exception sqle) {
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(pstmt, rs);
		}
	}
}