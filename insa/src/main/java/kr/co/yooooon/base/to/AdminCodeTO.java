package kr.co.yooooon.base.to;

public class AdminCodeTO extends BaseTO{
	String admin_code , admin_authority;

	public String getAdmin_code() {
		return admin_code;
	}

	public void setAdmin_code(String admin_code) {
		this.admin_code = admin_code;
	}

	public String getAdmin_authority() {
		return admin_authority;
	}

	public void setAdmin_authority(String admin_authority) {
		this.admin_authority = admin_authority;
	}
}
