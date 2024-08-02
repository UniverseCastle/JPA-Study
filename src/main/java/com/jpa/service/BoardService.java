package com.jpa.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.jpa.entity.Board;
import com.jpa.entity.SiteUser;
import com.jpa.exception.DataNotFoundException;
import com.jpa.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

	private final BoardRepository boardRepository;
	
	// 게시글 불러오기
	public Board getBoard(Long id) {
		Optional<Board> board = this.boardRepository.findById(id);
		if (board.isPresent()) {
			return board.get();
		}else {
			throw new DataNotFoundException("id not found");
		}
	}
	
	// 글 작성
	public void create(String title, String content, SiteUser user) {
		Board board = new Board();
		board.setTitle(title);
		board.setContent(content);
		board.setAuthor(user);
		
		this.boardRepository.save(board);
	}
	
	// 글 수정
	public void update(Board board, String title, String content) {
		board.setTitle(title);
		board.setContent(content);
		
		this.boardRepository.save(board);
	}
	
	// 글 삭제
	public void delete(Board board) {
		this.boardRepository.delete(board);
	}
}