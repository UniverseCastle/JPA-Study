package com.jpa.service;

import org.springframework.stereotype.Service;

import com.jpa.entity.Board;
import com.jpa.entity.SiteUser;
import com.jpa.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

	private final BoardRepository boardRepository;
	
	// 글 작성
	public void create(String title, String content, SiteUser user) {
		Board board = new Board();
		board.setTitle(title);
		board.setContent(content);
		board.setAuthor(user);
		
		this.boardRepository.save(board);
	}
}