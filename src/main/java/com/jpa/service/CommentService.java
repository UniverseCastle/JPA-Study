package com.jpa.service;

import org.springframework.stereotype.Service;

import com.jpa.entity.Board;
import com.jpa.entity.Comments;
import com.jpa.entity.SiteUser;
import com.jpa.repository.CommentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository comeCommentRepository;
	
	public Comments create(Board board, String content, SiteUser author) {
		Comments comment = new Comments();
		comment.setBoard(board);
		comment.setContent(content);
		comment.setAuthor(author);
		
		this.comeCommentRepository.save(comment);
		
		return comment;
	}
}