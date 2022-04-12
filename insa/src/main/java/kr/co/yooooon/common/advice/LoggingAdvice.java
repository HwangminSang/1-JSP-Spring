package kr.co.yooooon.common.advice;


import org.aopalliance.intercept.MethodInterceptor;		
import org.aopalliance.intercept.MethodInvocation;		
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LoggingAdvice implements MethodInterceptor{ //joinpoint 메서드  호출 전후.
       //인증처리로도 사용가능
	protected final Log logger = LogFactory.getLog(this.getClass());

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		
		String className = invocation.getThis().getClass().getName();
		String methodName = invocation.getMethod().getName();
		
		if (logger.isDebugEnabled()) {
			logger.debug(className + "  :  " + methodName + "() 시작");
			Object[] args = invocation.getArguments(); //파라미터 
			if ((args != null) && (args.length > 0)) {
				for (int i = 0; i < args.length; i++) { 
					logger.debug("종료: [" + i + "]: " + args[i]);
				}
			}
		}
		
		Object returnValue = invocation.proceed();  // 메서드 호추 

		
		
		if (logger.isDebugEnabled()) {
			logger.debug(className + "  : " + methodName + "()종료");
		}
		return returnValue;
	}
}

