package com.sumanthk.gettingstarted;

public class Product {
	
	private String number;
	private String desc;

	
	
	public Product(String number, String desc) {
		super();
		this.number = number;
		this.desc = desc;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	

}
