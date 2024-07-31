package com.jpa.controller;

import java.security.Principal;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jpa.dto.request.SignupReqDTO;
import com.jpa.entity.SiteUser;
import com.jpa.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

	private final UserService userService;
	private final PasswordEncoder passwordEncoder;
	
	// 회원가입
	@GetMapping("/signup")
	public String signup(SignupReqDTO signupReq, Model model) {
		model.addAttribute("signupReq", signupReq);
		
		return "user/signup";
	}
	@PostMapping("/signup")
	public String signup(@Valid SignupReqDTO signupReq, BindingResult result) {
		if (result.hasErrors()) {
			return "user/signup";
		}
		if (!signupReq.getPassword1().equals(signupReq.getPassword2())) {
			result.rejectValue("password2", "passwordInCorrect", "2개의 비밀번호가 일치하지 않습니다.");
			
			return "user/signup";
		}
		try {
			userService.signup(signupReq);
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			result.reject("signupFailed", "이미 등록된 사용자 입니다.");
			
			return "user/signup";
		} catch (Exception e) {
			e.printStackTrace();
			result.reject("signupFailed", "이미 등록된 사용자 입니다.");
			
			return "user/signup";
		}
		return "redirect:/board/list";
	}
	
	// 로그인
	@GetMapping("/login")
	public String login() {
		
		return "user/login";
	}
	@PreAuthorize("isAuthenticated()")
	@GetMapping("loginPass")
	public String loginPass(Principal principal) {
		SiteUser siteUser = this.userService.getUser(principal.getName());
		if (siteUser.getDeleteYn().equals("y")) {
			return "redirect:/user/logout";
		}
		return "redirect:/board/list";
	}
}