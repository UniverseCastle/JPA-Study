package com.jpa.controller;

import java.security.Principal;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jpa.dto.request.UserDeleteReqDTO;
import com.jpa.dto.request.UserFindReqDTO;
import com.jpa.dto.request.UserPwReqDTO;
import com.jpa.dto.request.UserSignupReqDTO;
import com.jpa.dto.request.UserUpdateReqDTO;
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
	public String signup(UserSignupReqDTO signupReq, Model model) {
		model.addAttribute("signupReq", signupReq);
		
		return "user/signup";
	}
	@PostMapping("/signup")
	public String signup(@Valid UserSignupReqDTO signupReq, BindingResult result) {
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
			result.reject("signupFailed", e.getMessage());
			
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
	
	// 정보수정
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/update")
	public String update(Principal principal, Model model) {
		SiteUser siteUser = this.userService.getUser(principal.getName());
		
		// builder 패턴으로 DB의 정보를 DTO에 set해줌
		UserUpdateReqDTO updateReq = UserUpdateReqDTO.builder()
				.username(siteUser.getUsername())
				.password(siteUser.getPassword())
				.email(siteUser.getEmail())
				.gender(siteUser.getGender())
				.country(siteUser.getCountry())
				.hobbies(siteUser.getHobbies())
				.build();
		model.addAttribute("updateReq", updateReq);
		
		return "user/update";
	}
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/update")
	public String update(@Valid @ModelAttribute(name = "updateReq") UserUpdateReqDTO updateReq, 
						 BindingResult result, SiteUser siteUser, Principal principal) {
		if (result.hasErrors()) {
			return "user/update";
		}
		this.userService.update(siteUser, updateReq, principal);
		
		return "redirect:/board/list";
	}
	
	// 회원탈퇴
	@PreAuthorize("isAuthenticated()")
	@GetMapping("delete")
	public String userDelete(UserDeleteReqDTO deleteReq, Model model) {
		model.addAttribute("deleteReq", deleteReq);
		
		return "user/delete";
	}
	@PreAuthorize("isAuthenticated()")
	@PostMapping("delete")
	public String delete(@Valid @ModelAttribute(name = "deleteReq") UserDeleteReqDTO deleteReq,
						 BindingResult result, Principal principal) {
		if (result.hasErrors()) {
			return "user/delete";
		}
		SiteUser siteUser = this.userService.getUser(principal.getName());
		
		if (!passwordEncoder.matches(deleteReq.getPassword(), siteUser.getPassword())) {
			result.rejectValue("password", "passwordInCorrect", "비밀번호가 맞지 않습니다.");
			return "user/delete";
		}else {
			userService.delete(siteUser, principal); // 회원탈퇴 메서드
			return "redirect:/user/logout"; // 회원탈퇴 성공한 경우 로그아웃 처리
		}
	}
	
	// 아이디 찾기 by email
	@GetMapping("findId")
	public String findId(UserFindReqDTO findReq, Model model) {
		model.addAttribute("findReq", findReq);
		return "user/find_id";
	}
	@PostMapping("findId")
	public String findId(@Valid @ModelAttribute(name = "findReq") UserFindReqDTO findReq, BindingResult result,
						 Model model) {
		if (result.hasErrors()) {
			return "user/find_id";
		}
		try {
			SiteUser siteUser = this.userService.findId(findReq.getEmail());
	        model.addAttribute("username", siteUser.getUsername());
	        model.addAttribute("findReq", findReq);
		} catch (Exception e) {
			result.rejectValue("email", "email not found", "이메일을 찾을 수 없습니다.");
			return "user/find_id"; // 에러 페이지 대신 원래 페이지로 돌아감
		}
		return "user/find_id";
	}
	
	// 비밀번호 찾기 by id & email
	@GetMapping("findPw")
	public String findPw(UserFindReqDTO findReq, Model model) {
		model.addAttribute("findReq", findReq);
		
		return "user/find_pw";
	}
	@PostMapping("findPw")
	public String findPw(@Valid @ModelAttribute(name = "findReq") UserFindReqDTO findReq, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "user/find_pw";
		}
		if (findReq.getUsername() != null && findReq.getEmail() != null) {
			try {
				SiteUser siteUser = this.userService.getUser(findReq.getUsername());
				if (siteUser.getEmail().equals(findReq.getEmail())) {
					
					return String.format("redirect:/user/updatePw/%s", siteUser.getId());
				}
			} catch (Exception e) {
				model.addAttribute("result", result);
				return "user/find_pw";
			}
		}
		model.addAttribute("result", result);
		return "user/find_pw";
	}
	
	// 새 비밀번호 설정
	@GetMapping("updatePw/{id}")
	public String updatePw(UserPwReqDTO pwReq, @PathVariable(name = "id") Long id, Model model) {
		model.addAttribute("id", id);
		model.addAttribute("pwReq", pwReq);
		
		return "user/update_pw";
	}
	@PostMapping("updatePw")
	public String updatePw(@Valid @ModelAttribute(name = "pwReq") UserPwReqDTO pwReq, BindingResult result,
						   SiteUser siteUser, @RequestParam(name = "id") Long id) {
		if (result.hasErrors()) {
			return String.format("redirect:/user/updatePw/%s", siteUser.getId());
		}
		if (!pwReq.getPassword1().equals(pwReq.getPassword2())) {
			result.rejectValue("password2", "passwordInCorrect", "2개의 비밀번호가 일치하지 않습니다.");
			
			return String.format("redirect:/user/updatePw/%s", siteUser.getId());
		}
		try {
			userService.updatePw(pwReq, id);
		} catch (Exception e) {
			e.printStackTrace();
			result.reject("updateFailed", e.getMessage());
			
			return String.format("redirect:/user/updatePw/%s", siteUser.getId());
		}
		return "redirect:/board/list";
	}
}