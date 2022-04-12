package kr.co.yooooon.base.to;

public class MenuTO {
	private String menu_name , super_menu_code , menu_code , depth , is_collapce , menu_url;
	
	public String getMenu_url() {
		return menu_url;
	}

	public void setMenu_url(String menu_url) {
		this.menu_url = menu_url;
	}

	public String getMenu_name() {
		return menu_name;
	}

	public void setMenu_name(String menu_name) {
		this.menu_name = menu_name;
	}

	public String getSuper_menu_code() {
		return super_menu_code;
	}

	public void setSuper_menu_code(String super_menu_code) {
		this.super_menu_code = super_menu_code;
	}

	public String getMenu_code() {
		return menu_code;
	}

	public void setMenu_code(String menu_code) {  //decode(super_menu_code , null , 'Y' , 'N')
		this.menu_code = menu_code;
	}

	public String getDepth() { 
		return depth;
	}

	public void setDepth(String depth) {   //level
		this.depth = depth;
	}

	public String getIs_collapce() {
		return is_collapce;
	}

	public void setIs_collapce(String is_collapce) {
		this.is_collapce = is_collapce;
	}
}
