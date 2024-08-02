package com.jpa.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.jpa.dto.request.BoardAddReqDTO;
import com.jpa.entity.Board;
import com.jpa.service.BoardService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardRestController {

	private final BoardService boardService;
	
	// 게시글 수정 ajax
	@PreAuthorize("isAuthenticated()")
	@PutMapping("/update/{id}")
	public ResponseEntity<Void> update(@Valid @RequestBody BoardAddReqDTO addReqDTO,
									   @PathVariable(name = "id") Long id, Principal principal) {
		Board board = boardService.getBoard(id);
		// 작성자와 동일한지 확인
		if (!board.getAuthor().getUsername().equals(principal.getName())) {
			// 동일하지 않다면 403 Forbidden 응답 반환
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		// 게시글 수정 메서드
		boardService.update(board, addReqDTO.getTitle(), addReqDTO.getContent());
		// 200 OK 응답 반환
		return ResponseEntity.ok().build();
	}
	
	// 게시글 삭제 ajax
	@PreAuthorize("isAuthenticated()")
	@DeleteMapping("/delete/{id}") // DELETE 요청을 처리하는 메서드
	public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id, Principal principal) {
		Board board = this.boardService.getBoard(id);
		// 작성자가 아닐 경우 BAD_REQUEST 상태와 예외 발생
		if (!board.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
		}
		// 게시글 삭제 메서드
		this.boardService.delete(board);
		// 성공적으로 삭제했음을 나타내는 204 No Content 응답 반환
		return ResponseEntity.noContent().build();
	}
}