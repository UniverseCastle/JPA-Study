package com.jpa.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Comments {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length = 255)
	private String content;
	
	@ManyToOne
	private SiteUser author;
	
	private LocalDateTime createDate;
	
	private LocalDateTime modifyDate;
	
	@PrePersist
	protected void onCreate() {
		this.createDate = LocalDateTime.now();
	}
	
	@PreUpdate
	protected void onUpdate() {
		this.modifyDate = LocalDateTime.now();
	}
	
	@ManyToOne
	@JoinColumn(name = "board_id")
	private Board board;
}