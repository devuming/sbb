package com.mysite.sbb.answer;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.mysite.sbb.question.Question;
import com.mysite.sbb.user.SiteUser;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Answer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(columnDefinition = "TEXT")
	private String content;
	
	private LocalDateTime createDate;
	
	@ManyToOne
	private Question question;		// 질문 엔티티를 참조하기 위해 추가 - N:1 관계 : 답변 여러개에 질문은 하나
	
	@ManyToOne
	private SiteUser author;
	
	private LocalDateTime modifyDate;

	@ManyToMany
	Set<SiteUser> voter;				// N:M 추천인 - Answer_voter 테이블 생성됨
}
