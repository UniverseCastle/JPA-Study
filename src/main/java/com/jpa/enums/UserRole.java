package com.jpa.enums;

import lombok.Getter;

@Getter
public enum UserRole {

	ADMIN("ROLE_ADMIN", "관리자"),
	USER("ROLE_USER", "회원");
	
	UserRole(String value, String title) {
		this.value = value;
		this.title = title;
	}
	
	private String value;
	private String title;
}