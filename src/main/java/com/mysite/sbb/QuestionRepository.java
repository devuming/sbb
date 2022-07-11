package com.mysite.sbb;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Integer>{	// table에 접근하기 위한 인터페이스, 대상이 되는 Entity의 타입과 PK의 속성 타입 지정

}
