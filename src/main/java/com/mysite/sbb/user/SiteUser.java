package com.mysite.sbb.user;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class SiteUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true)
	private String username;		// 	로그인 ID
	
	private String password;
	
	@Column(unique = true)
	private String email;

	@Column(unique = true)
	private String sns_id;			// SNS ID : 값 있는 경우 SNS 로그인, null 인 경우 사이트 가입자
	
	@Column(length = 200)
	private String sns_name;

	@Column(length = 200)
	private String sns_type;		// SNS 로그인 타입

	private LocalDateTime createDate;
	
}
