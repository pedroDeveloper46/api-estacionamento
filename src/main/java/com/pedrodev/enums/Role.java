package com.pedrodev.enums;

public enum Role {
	
	ADMIN("ADMIN");
	
	private String role;
	
	Role(String role){
		this.role = role;
	}
	
	public String getRole() {
		return this.role;
	}

}
