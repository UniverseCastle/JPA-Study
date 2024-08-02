package com.jpa.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.jpa.entity.Board;
import com.jpa.entity.Comments;
import com.jpa.entity.SiteUser;
import com.jpa.exception.DataNotFoundException;
import com.jpa.repository.CommentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository comeCommentRepository;
	
	// 댓글 작성
	public Comments create(Board board, String content, SiteUser author) {
		Comments comment = new Comments();
		comment.setBoard(board);
		comment.setContent(content);
		comment.setAuthor(author);
		
		this.comeCommentRepository.save(comment);
		
		return comment;
	}
	
	// 댓글 불러오기
	public Comments getComment(Long id) {
		Optional<Comments> comment = this.comeCommentRepository.findById(id);
		if (comment.isPresent()) {
			return comment.get();
		}else {
			throw new DataNotFoundException("comment not found");
		}
	}
	
	// 댓글 수정
	public void update(Comments comment, String content) {
		comment.setContent(content);
		
		comeCommentRepository.save(comment);
	}
	
	// 댓글 삭제
	public void delete(Comments comment) {
		comeCommentRepository.delete(comment);
	}
}