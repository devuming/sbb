package com.mysite.sbb;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Integer>{	// table에 접근하기 위한 인터페이스, 대상이 되는 Entity의 타입과 PK의 속성 타입 지정
	Question findBySubject(String subject);								// subject 필드 내용으로 데이터 조회하기 위한 메서드
	Question findBySubjectAndContent(String subject, String content);	// 제목 and 내용 조건
	List<Question> findBySubjectLike(String subject);					// 제목 like 조건
}
