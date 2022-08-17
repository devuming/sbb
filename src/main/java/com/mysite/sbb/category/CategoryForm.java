package com.mysite.sbb.category;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryForm {
	private String title;
	private String userRole;
}
