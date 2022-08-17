package com.mysite.sbb.service;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@org.springframework.web.bind.annotation.RestController
public class RestController {
	private final QuestionService questionService;
	
	@GetMapping("/listupQuestion")
	public Page<Question> list(@RequestParam(value="page", defaultValue = "0") int page,	// 페이지 번호를 매개변수로 받음
			@RequestParam(value="kw", defaultValue = "") String kw,
			@RequestParam(value="category", defaultValue = "") Integer category) {	
		
		Page<Question> paging = this.questionService.getList(page, kw, category);
				
		return paging;		
	}
}
