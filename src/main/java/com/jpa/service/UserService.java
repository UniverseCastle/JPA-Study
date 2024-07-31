package com.jpa.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jpa.dto.request.SignupReqDTO;
import com.jpa.entity.SiteUser;
import com.jpa.exception.DataNotFoundException;
import com.jpa.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	// 회원가입
	public SiteUser signup(SignupReqDTO signupReq) {
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
	
	public SiteUser getUser(String username) {
		Optional<SiteUser> siteUser = this.userRepository.findByUsername(username);
		if (siteUser.isPresent()) {
			return siteUser.get();
		}else {
			throw new DataNotFoundException("siteUser not found");
		}
	}
}