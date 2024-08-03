package com.jpa.dto.response;

import java.util.List;

import com.jpa.entity.Board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardPageResDTO {

	private List<Board> boards; // 게시글 목록
	private int startPage; // 시작 페이지 번호
	private int endPage; // 마지막 페이지 번호
	private int totalPages; // 전체 페이지 수
}