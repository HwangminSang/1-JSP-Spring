package kr.co.yooooon.common.advice;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.ThrowsAdvice;

import kr.co.yooooon.common.exception.DataAccessException;



public class DataAccessThrowsAdvice implements ThrowsAdvice {  //joinpoint 시점은 인셉션이 생긴후<마크 인터페이스> 예외가 발생핳때 공통적으로 들어감
	protected final Log logger = LogFactory.getLog(this.getClass());

	public void afterThrowing(DataAccessException e) throws Throwable {  // 이 메서드는 ThrowsAdvice에 있는게 아니라 관행적으로 올리는거
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

