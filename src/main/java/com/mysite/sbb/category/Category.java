package com.mysite.sbb.category;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.ColumnDefault;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.question.Question;
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

//	@ColumnDefault("0")	
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer sortOrder;

	@OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
	private List<Question> questionList;	// 	1:N	하나의 카테고리에 여러개의 질문 등록 가능
}