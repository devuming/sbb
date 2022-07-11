package com.mysite.sbb;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SbbApplicationTests {
	
	@Autowired		// DI(Dependency Injection) : 스프링이 객체를 대신 생성하여 주입해주는 Annotation
	private QuestionRepository questionRepository;
	
	@Test			// Test 메서드임을 나타냄, Junit 실행시 호출되는 메소드
	void testJpa() {
		Question q1 = new Question();
		q1.setSubject("sbb가 무엇인가요?");
		q1.setContent("sbb에 대해서 알고 싶습니다.");
		q1.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(q1);			// QuestionRepository 사용하여 db에 값 저장

		Question q2 = new Question();
		q2.setSubject("sbb가 무엇인가요?");
		q2.setContent("sbb에 대해서 알고 싶습니다.");
		q2.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(q2);
	}

}
