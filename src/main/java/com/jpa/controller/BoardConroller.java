package com.jpa.controller;

import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.jpa.dto.request.BoardAddReqDTO;
import com.jpa.dto.request.CommentAddReqDTO;
import com.jpa.dto.response.BoardPageResDTO;
import com.jpa.entity.Board;
import com.jpa.entity.SiteUser;
import com.jpa.service.BoardService;
import com.jpa.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardConroller {
	
	private final BoardService boardService;
	private final UserService userService;
	
	// 글목록
	@GetMapping("/list")
	public String list(@RequestParam(value = "page", defaultValue = "1") int page,
					   @RequestParam(value = "kw", defaultValue = "") String kw, Model model) {
		Page<Board> paging = this.boardService.getList(page, kw);
		BoardPageResDTO pageResDTO = this.boardService.getBoardPage(paging);
		
		model.addAttribute("paging", paging); // Pageable, 
		model.addAttribute("kw", kw);
		model.addAttribute("pageResDTO", pageResDTO); // startPage, endPage, totalPages
		
		return "board/list";
	}
	
	// 글작성
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/create")
	public String create(BoardAddReqDTO addReqDTO, Model model) {
		model.addAttribute("addReqDTO", addReqDTO);
		
		return "board/create";
	}
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create")
	public String create(@Valid @ModelAttribute(name = "addReqDTO") BoardAddReqDTO addReqDTO, BindingResult result, Principal principal) {
		if (result.hasErrors()) {
			return "board/create";
		}
		SiteUser siteUser = this.userService.getUser(principal.getName());
		this.boardService.create(addReqDTO.getTitle(), addReqDTO.getContent(), siteUser);
		
		return "redirect:/board/list";
	}
	
	// 글내용
	@GetMapping("/detail/{id}")
	public String detail(Model model, @PathVariable(name = "id") Long id,
						 CommentAddReqDTO addReqDTO) {
		Board board = this.boardService.getBoard(id);
		model.addAttribute("board", board);
		model.addAttribute("addReqDTO", addReqDTO);
		
		return "board/detail";
	}
	
	// 게시글 수정
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/update/{id}")
	public String update(BoardAddReqDTO addReqDTO, @PathVariable(name = "id") Long id,
						 Principal principal, Model model) {
		Board board = this.boardService.getBoard(id);
		if (!board.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
		}
		addReqDTO.setTitle(board.getTitle());
		addReqDTO.setContent(board.getContent());
		
		model.addAttribute("addReqDTO", addReqDTO);
		
		return "board/update";
	}
	
	/* BoardRestController 에서 비동기 처리된 메서드
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/update/{id}")
	public String update(@Valid @ModelAttribute(name = "addReqDTO") BoardAddReqDTO addReqDTO, BindingResult bindingResult,
						 @PathVariable(name = "id") Long id, Principal principal) {
		if (bindingResult.hasErrors()) {
			return "board/create";
		}
		Board board = this.boardService.getBoard(id);
		if (!board.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
		}
		this.boardService.update(board, addReqDTO.getTitle(), addReqDTO.getContent());
		
		return String.format("redirect:/board/detail/%s", id);
	}
	*/
	
	/*
	// 게시글 삭제
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/detete/{id}")
	public String delete(@PathVariable(name = "id") Long id, Principal principal) {
		Board board = this.boardService.getBoard(id);
		if (!board.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
		}
		this.boardService.delete(board);
		
		return "redirect:/board/list";
	}
	*/
}