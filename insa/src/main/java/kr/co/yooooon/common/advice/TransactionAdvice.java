package kr.co.yooooon.common.advice;


import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import kr.co.yooooon.common.transaction.DataSourceTransactionManager;

                                          //<구현시점은 스프링에서 제공하는 인터페이스에 따라 어느 시점에 적용될지 알수있다>
public class TransactionAdvice implements MethodInterceptor {  // 메서드 호출 전  후   catch까지 한다 
	   private DataSourceTransactionManager dataSourceTransactionManager;

	   public void setDataSourceTransactionManager(DataSourceTransactionManager dataSourceTransactionManager) {
	      this.dataSourceTransactionManager = dataSourceTransactionManager;
	   }

	   protected final Log logger = LogFactory.getLog(getClass());

	   @Override
	   public Object invoke(MethodInvocation invocation) throws Throwable {
	      if (logger.isDebugEnabled()) {
	         logger.debug(invocation.getThis().getClass().getSimpleName() + "." + invocation.getMethod().getName() + "() 시작");
	      }
	      Object reVal = null;
	      try {
	    	  
	    	  if(invocation.getMethod().getName().startsWith("find")) {  //읽기전용 JDBC 드라이버에서 데이터베이스 최적화를 수행하기 ,연결을 읽기 전용으로 하려면 true 이고, 그렇지 않으면 false 입니다
	    		  
	    		  dataSourceTransactionManager.beginReadOnlyTransaction();
	    		
	    		  reVal = invocation.proceed();//메서드호출
	    	  
	    	     }else { // 그 외 insert, update , delete는  auto commit x 따로 commit해줌
	        
	    	  dataSourceTransactionManager.beginTransaction();
	          
	    	  reVal = invocation.proceed();//메서드호출
	          
	    	  dataSourceTransactionManager.commitTransaction();
	    	 
	    	  }
	         return reVal;
	      
	      }catch (Exception e) {
	         dataSourceTransactionManager.rollbackTransaction();
	         dataSourceTransactionManager.closeConnection();  //추가한거 
	         logger.fatal(e.getMessage());
	         throw e;
		} finally {
			dataSourceTransactionManager.closeConnection();  //추가한거 
	         if (logger.isDebugEnabled()) {
	            logger.debug(invocation.getThis().getClass().getSimpleName() + "." + invocation.getMethod().getName() + "() 종료");
	         }
	      }
	   }
}
