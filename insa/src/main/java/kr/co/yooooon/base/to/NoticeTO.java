package kr.co.yooooon.base.to;

public class NoticeTO extends BaseTO{
	
	private String dept , empName , noticeDate , noticeTitle , noticeWord;
	
	public String getDept() {
		return dept;
	}

	public void setDept(String dpet) {
		this.dept = dpet;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getNoticeDate() {
		return noticeDate;
	}

	public void setNoticeDate(String noticeDate) {
		this.noticeDate = noticeDate;
	}

	public String getNoticeTitle() {
		return noticeTitle;
	}

	public void setNoticeTitle(String noticeTitle) {
		this.noticeTitle = noticeTitle;
	}

	public String getNoticeWord() {
		return noticeWord;
	}

	public void setNoticeWord(String noticeWord) {
		this.noticeWord = noticeWord;
	}

}
