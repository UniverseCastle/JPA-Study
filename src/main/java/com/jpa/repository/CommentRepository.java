package com.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jpa.entity.Comments;

public interface CommentRepository extends JpaRepository<Comments, Long> {

}