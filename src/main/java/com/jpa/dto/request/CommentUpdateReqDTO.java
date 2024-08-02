package com.jpa.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentUpdateReqDTO {

	@NotBlank(message = "내용은 필수 항목 입니다.")
	private String content;
}