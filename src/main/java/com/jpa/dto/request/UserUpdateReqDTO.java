package com.jpa.dto.request;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor // 모든 필드를 포함하는 생성자를 자동으로 생성
@NoArgsConstructor // DTO를 설계할 때 이 두 어노테이션을 함께 사용하는 것이 좋은 관행
public class UserUpdateReqDTO {

	private String username;
	private String gender;
	
	@NotBlank(message = "비밀번호는 필수 항목 입니다.")
	private String password;
	
	@Email
	@NotBlank(message = "이메일은 필수 항목 입니다.")
	private String email;

	private String country;
	private LocalDateTime modifyDate;
	private String deleteYn;
	private List<String> hobbies;
}