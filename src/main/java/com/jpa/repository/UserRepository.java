package com.jpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jpa.entity.SiteUser;

public interface UserRepository extends JpaRepository<SiteUser, Long> {

	Optional<SiteUser> findByUsername(String username);
	
	Optional<SiteUser> findByEmail(String email);
}