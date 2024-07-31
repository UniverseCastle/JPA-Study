package com.jpa.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SiteUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true)
	private String username;
	
	private String password;
	
	@Column(unique = true)
	private String email;
	
	private String gender;
	private String country;
	private LocalDateTime modifyDate;
	private LocalDateTime createDate;
	private String deleteYn;
	
	@ElementCollection
	@CollectionTable(name = "user_hobbies", joinColumns = @JoinColumn(name = "id"))
	@Column(name = "hobby")
	private List<String> hobbies;
}