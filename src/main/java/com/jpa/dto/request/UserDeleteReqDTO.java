package com.jpa.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDeleteReqDTO {
	
	private String username;
	
	@NotEmpty(message = "비밀번호는 필수 항목 입니다.")
	private String password;
}