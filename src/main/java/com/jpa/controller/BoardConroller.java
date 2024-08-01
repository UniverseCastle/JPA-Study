package com.jpa.controller;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jpa.dto.request.BoardAddReqDTO;
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
	@GetMapping("list")
	public String list(Model model) {
		
		return "board/list";
	}
	
	// 글쓰기
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/create")
	public String create(BoardAddReqDTO addReqDTO, Model model) {
		model.addAttribute("addReqDTO", addReqDTO);
		
		return "board/create";
	}
	@PreAuthorize("isAuthenticated()")
	@PostMapping("create")
	public String create(@Valid BoardAddReqDTO addReqDTO, BindingResult result, Principal principal) {
		if (result.hasErrors()) {
			return "board_form";
		}
		SiteUser siteUser = this.userService.getUser(principal.getName());
		this.boardService.create(addReqDTO.getTitle(), addReqDTO.getContent(), siteUser);
		
		return "redirect:/board/list";
	}
}