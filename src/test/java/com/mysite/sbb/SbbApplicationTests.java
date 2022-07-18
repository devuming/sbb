package com.mysite.sbb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.answer.AnswerRepository;
import com.mysite.sbb.answer.AnswerService;
import com.mysite.sbb.category.CategoryService;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionRepository;
import com.mysite.sbb.question.QuestionService;

@SpringBootTest
class SbbApplicationTests {
	
	@Autowired		// DI(Dependency Injection) : 스프링이 객체를 대신 생성하여 주입해주는 Annotation
	private QuestionService questionService;
	@Autowired	
	private AnswerService answerService;
	
//	@Transactional	// Question 조회 후 DB 세션이 끊어지는 것을 방지하기 위함 (Test 시에만 필요)	
	@Test			// Test 메서드임을 나타냄, Junit 실행시 호출되는 메소드
	void testJpa() {
		Question question = this.questionService.getQuestion(2614);
		for(int i = 0; i <= 200; i++) {
			String content = String.format("테스트 답변입니다. : [%03d]", i);
			this.answerService.create(question, content, null);
		}

	}

}