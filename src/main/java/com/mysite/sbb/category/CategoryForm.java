package com.mysite.sbb.category;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryForm {
	
	@NotEmpty(message="카테고리명은 필수 항목입니다.")
	@Size(min=3, max=10)
	private String title;
	
	private String userRole;
	
	private Integer sortOrder;	// 순서
}
