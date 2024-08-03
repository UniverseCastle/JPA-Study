package com.jpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.jpa.entity.Board;
import com.jpa.repository.BoardRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
class JpaApplicationTests {
	
	@Autowired
	private BoardRepository boardRepository;
	
//	@Test
	@Transactional
	void create() {
		for (int i=1; i<=120; i++) {
			Board board = new Board();
			board.setTitle("테스트 제목" + i);
			board.setContent("테스트 내용" + i);
			boardRepository.save(board);
		}
	}

	@Test
	void contextLoads() {
	}

}
