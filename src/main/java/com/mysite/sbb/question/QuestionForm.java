package com.mysite.sbb.question;

import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.mysite.sbb.category.Category;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionForm {
	@NotEmpty(message="제목은 필수 항목입니다.")
	@Size(max=200)
	private String subject;
	
	@NotEmpty(message="내용은 필수 항목입니다.")
	private String content;
	
//	@NotEmpty(message="카테고리는 필수 항목입니다.")
	private Integer category;
}
