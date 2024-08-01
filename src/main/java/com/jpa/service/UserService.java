package com.jpa.service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jpa.dto.request.UserPwReqDTO;
import com.jpa.dto.request.UserSignupReqDTO;
import com.jpa.dto.request.UserUpdateReqDTO;
import com.jpa.entity.SiteUser;
import com.jpa.exception.DataNotFoundException;
import com.jpa.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	// 회원 이름으로 객체 불러오기
	public SiteUser getUser(String username) {
		Optional<SiteUser> siteUser = this.userRepository.findByUsername(username);
		if (siteUser.isPresent()) {
			return siteUser.get();
		}else {
			throw new DataNotFoundException("siteUser not found");
		}
	}
	
	// 회원 아이디로 객체 불러오기
	public SiteUser getId(Long id) {
		Optional<SiteUser> siteUser = this.userRepository.findById(id);
		if (siteUser.isPresent()) {
			return siteUser.get();
		}else {
			throw new DataNotFoundException("siteUser not found");
		}
	}
	
	// 회원가입
	public SiteUser signup(UserSignupReqDTO signupReq) {
		SiteUser user = new SiteUser();
		user.setUsername(signupReq.getUsername());
		user.setPassword(passwordEncoder.encode(signupReq.getPassword1()));
		user.setEmail(signupReq.getEmail());
		user.setGender(signupReq.getGender());
		user.setCountry(signupReq.getCountry());
		user.setModifyDate(LocalDateTime.now());
		user.setCreateDate(LocalDateTime.now());
		user.setDeleteYn("n");
		user.setHobbies(signupReq.getHobbies());
		
		userRepository.save(user);
		
		return user;
	}
	
	
	// 정보수정
	public void update(SiteUser siteUser, UserUpdateReqDTO updateReq,
					   Principal principal) {
		Optional<SiteUser> ouser = this.userRepository.findByUsername(principal.getName());
		if (ouser.isPresent()) {
			siteUser = ouser.get();
			
			if (!passwordEncoder.matches(updateReq.getPassword(), siteUser.getPassword())) {
				siteUser.setPassword(updateReq.getPassword());
			}else if (updateReq.getPassword() == null) {
				throw new DataNotFoundException("password not found");
			}
			siteUser.setPassword(passwordEncoder.encode(updateReq.getPassword()));
			
			if (updateReq.getEmail() != null && updateReq.getEmail() != siteUser.getEmail()) {
				siteUser.setEmail(updateReq.getEmail());
			}else if (updateReq.getEmail() == null) {
				siteUser.setEmail("없음");
			}
			
			siteUser.setCountry(updateReq.getCountry());
			
			if (updateReq.getHobbies() != null) {
				List<String> hobbies = new ArrayList<>();
				for (String hobby : updateReq.getHobbies()) {
					hobbies.add(hobby);
				}
				siteUser.setHobbies(hobbies);
			}else {
				siteUser.setHobbies(null);
			}
			siteUser.setModifyDate(LocalDateTime.now());
			
			this.userRepository.save(siteUser);
		}else {
			throw new DataNotFoundException("siteUser not found");
		}
	}
	
	// 회원탈퇴
	public void delete(SiteUser siteUser, Principal principal) {
		Optional<SiteUser> ouser = this.userRepository.findByUsername(principal.getName());
		if (ouser.isPresent()) {
			siteUser = ouser.get();
			siteUser.setDeleteYn("y");
			
			this.userRepository.save(siteUser);
		}
	}
	
	// 아이디 찾기 by email
	public SiteUser findId(String email) {
		Optional<SiteUser> siteUser = this.userRepository.findByEmail(email);
		if (siteUser.isPresent()) {
			return siteUser.get();
		}else {
			throw new DataNotFoundException("siteUser not found");
		}
	}
	
	// 비밀번호 변경 (재설정)
	public void updatePw(UserPwReqDTO pwReq, Long id) {
		Optional<SiteUser> ouser = this.userRepository.findById(id);
		if (ouser.isEmpty()) {
			throw new DataNotFoundException("siteUser not found");
		}
		SiteUser siteUser = ouser.get();
		siteUser.setPassword(passwordEncoder.encode(pwReq.getPassword1()));
		
		this.userRepository.save(siteUser);
	}
}