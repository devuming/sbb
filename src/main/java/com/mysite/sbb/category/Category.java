package com.mysite.sbb.category;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.ColumnDefault;

import com.mysite.sbb.user.UserRole;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String title;

	private String userRole;		// 카테고리 권한 추가
}
