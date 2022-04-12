package kr.co.yooooon.common.exception;

@SuppressWarnings("serial")
public class DataAccessException extends RuntimeException {  //내가 자체적으로 선언한것
	public DataAccessException(){
		super();
	}
	
	public DataAccessException(String message){
		super(message);
	}
	public DataAccessException(Throwable cause){
		super(cause);
	}
	public DataAccessException(String message, Throwable cause){
		super(message, cause);
	}
}