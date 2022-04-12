package kr.co.yooooon.base.exception;

@SuppressWarnings("serial")
public class PwMissMatchException extends Exception{ // 무조건 체크드 그래야 컨트롤러 단에서 호풀했으때 컴파일 단계에서 잡혀서 그 원인을 알고 후처리가 가능하다.
	public PwMissMatchException(String msg){ super(msg); } //이렇게하면 메서드 호출시 명세(즉 어떤 입섹션이 나오는지 알고 처리가능)
}