package com.mysite.sbb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SbbApplicationTests {
	
	@Autowired		// DI(Dependency Injection) : 스프링이 객체를 대신 생성하여 주입해주는 Annotation
	private QuestionRepository questionRepository;
	
	@Test			// Test 메서드임을 나타냄, Junit 실행시 호출되는 메소드
	void testJpa() {
		assertEquals(2, this.questionRepository.count());
		Optional<Question> oq = this.questionRepository.findById(1);
		assertTrue(oq.isPresent());
		Question q = oq.get();
		this.questionRepository.delete(q);		// 삭제
		assertEquals(1, this.questionRepository.count());	// 삭제 후 갯수 비교	
	}

}
