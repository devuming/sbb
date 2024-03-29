package com.mysite.sbb.question;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.ColumnDefault;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.category.Category;
import com.mysite.sbb.user.SiteUser;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Question {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 200)
	private String subject;
	
	@Column(columnDefinition = "TEXT")
	private String content;
	
	private LocalDateTime createDate;
	
	@OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
	private List<Answer> answerList;	// 1:N 관계 이므로 List 선언 : 하나의 질문에 답변은 여러
	
	@ManyToOne
	private SiteUser author;			// N:1 여러개의 질문이 한명에 의해 작성될 수 있음

	private LocalDateTime modifyDate;
	
	@ManyToMany
	Set<SiteUser> voter;				// N:M 추천인 - Question_Voter 테이블 생성됨
	
	@ColumnDefault("0")	// default = 0
	private int viewCount;				// 조회수

	@ManyToOne
	private Category category;			// 카테고리 : 하나의 카테고리에 답변 여러개 
}
