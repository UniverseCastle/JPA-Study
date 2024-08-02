package com.jpa.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPwReqDTO {
	@NotBlank(message = "비밀번호는 필수항목 입니다.")
	private String password1;
	
	@NotBlank(message = "비밀번호 확인은 필수항목 입니다.")
	private String password2;
}