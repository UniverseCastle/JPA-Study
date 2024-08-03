package com.jpa.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.jpa.dto.response.BoardPageResDTO;
import com.jpa.entity.Board;
import com.jpa.entity.Comments;
import com.jpa.entity.SiteUser;
import com.jpa.exception.DataNotFoundException;
import com.jpa.repository.BoardRepository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import javassist.SerialVersionUID;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

	private final BoardRepository boardRepository;
	
	// 키워드
	private Specification<Board> search(String kw) {
		return new Specification<Board>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public Predicate toPredicate(Root<Board> b, CriteriaQuery<?> query, CriteriaBuilder cb) {
				query.distinct(true);
				Join<Board, SiteUser> u1 = b.join("author", JoinType.LEFT);
				Join<Board, Comments> c = b.join("commentList", JoinType.LEFT);
				Join<Comments, SiteUser> u2 = c.join("author", JoinType.LEFT);
				
				return cb.or(cb.like(b.get("title"), "%" + kw + "%"),
							 cb.like(b.get("content"), "%" + kw + "%"),
							 cb.like(u1.get("username"), "%" + kw + "%"),
							 cb.like(c.get("content"), "%" + kw + "%"),
							 cb.like(u2.get("username"), "%" + kw + "%"));
			}
		};
	}
	
	// 페이징 처리
	public Page<Board> getList(int page, String kw) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createDate"));
		// page: 번호 0으로 변환 , pageSize , Sort.by(): 정렬 기준
		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(sorts));
		Specification<Board> spec = search(kw);
		
		return this.boardRepository.findAll(spec, pageable);
	}
	public BoardPageResDTO getBoardPage(Page<Board> boardList) {
		int pageSize = 10;
		int currentPage = boardList.getNumber(); // 현재 페이지 번호
		int totalPages = boardList.getTotalPages(); // 전체 페이지 수 
		
		int startPage = (currentPage / pageSize) * pageSize + 1; // 시작 페이지 계산
		int endPage = Math.min(startPage + pageSize - 1, totalPages); // 끝 페이지 계산
		
		return new BoardPageResDTO(boardList.getContent(), startPage, endPage, totalPages);
	}
	
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