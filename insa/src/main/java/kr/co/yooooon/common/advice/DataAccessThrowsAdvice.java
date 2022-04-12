package kr.co.yooooon.common.advice;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.ThrowsAdvice;

import kr.co.yooooon.common.exception.DataAccessException;



public class DataAccessThrowsAdvice implements ThrowsAdvice {  //joinpoint ������ �μ����� ������<��ũ �������̽�> ���ܰ� �߻��K�� ���������� ��
	protected final Log logger = LogFactory.getLog(this.getClass());

	public void afterThrowing(DataAccessException e) throws Throwable {  // �� �޼���� ThrowsAdvice�� �ִ°� �ƴ϶� ���������� �ø��°�
		if (logger.isDebugEnabled()) {
			logger.debug("DataAccessException afterThrowing start");
			logger.debug("Caught: " + e.getClass().getName());
		}

			logger.fatal(e.getMessage());


		if (logger.isDebugEnabled()) {
			logger.debug("DataAccessException afterThrowing end");
		}
		throw e;
	}
   
	public void afterThrowing(Exception e) throws Throwable {
		if (logger.isDebugEnabled()) {
			logger.debug("Exception afterThrowing start");
			logger.debug("Caught: " + e.getClass().getName());
		}

			logger.fatal(e.getMessage());

		if (logger.isDebugEnabled()) {
			logger.debug("Exception afterThrowing end");
		}
		throw e;
	}
}

