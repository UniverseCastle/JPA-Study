package com.jpa.controller;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jpa.dto.request.CommentAddReqDTO;
import com.jpa.entity.Board;
import com.jpa.entity.Comments;
import com.jpa.entity.SiteUser;
import com.jpa.service.BoardService;
import com.jpa.service.CommentService;
import com.jpa.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

	private final BoardService boardService;
	private final CommentService commentService;
	private final UserService userService;
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create/{id}")
	public String create(Model model, @PathVariable(name = "id") Long id, 
						 @Valid @ModelAttribute(name = "addReqDTO") CommentAddReqDTO addReqDTO,
						 BindingResult bindingResult, Principal principal) {
		Board board = this.boardService.getBoard(id);
		SiteUser siteUser = this.userService.getUser(principal.getName());
		if (bindingResult.hasErrors()) {
			model.addAttribute("board", board);
			return "/board/detail";
		}
		Comments comment = this.commentService.create(board, addReqDTO.getContent(), siteUser);
		
		return String.format("redirect:/board/detail/%s#comment_%s", comment.getBoard().getId(), comment.getId());
	}
	
}