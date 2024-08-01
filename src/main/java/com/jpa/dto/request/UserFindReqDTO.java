package com.jpa.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserFindReqDTO {

	@Email
	@Column(unique = true)
	private String email;
	
	private String username;
}