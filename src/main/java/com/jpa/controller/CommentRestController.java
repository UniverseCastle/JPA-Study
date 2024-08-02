package com.jpa.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.jpa.entity.Comments;
import com.jpa.service.CommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentRestController {

	private final CommentService commentService;
	
	// 댓글 삭제 ajax
	@PreAuthorize("isAuthenticated()")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id, Principal principal) {
		Comments comments = this.commentService.getComment(id);
		if (!comments.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
		}
		// 댓글 삭제 메서드
		this.commentService.delete(comments);
		// 삭제 성공하면 204 응답 반환
		return ResponseEntity.noContent().build();
	}
}