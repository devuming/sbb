package com.mysite.sbb.question;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.category.Category;
import com.mysite.sbb.user.SiteUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class QuestionService {
	private final QuestionRepository questionRepository;

	// 검색 메소드
	private Specification<Question> search(String kw, Integer category){
		return new Specification<>() {	// 쿼리 작성을 도와주는 JPA의 도구
			private static final long serialVersionUID = 1L;
			
			@Override
			public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
				query.distinct(true);	// 중복 제거
				Join<Question, SiteUser> u1 = q.join("author", JoinType.LEFT);	// Question과 SiteUser 조인
				Join<Question, Category> c = q.join("category", JoinType.LEFT);	// Question과 Category 조인
				Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);	// Question과 Answer 조인
				Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);	// Answer와 SiteUser 조인
				System.out.println(category);
				return cb.and(cb.equal(c.get("id"),  category),
								cb.or(cb.like(q.get("subject"), "%" + kw + "%"),		// 제목
										cb.like(q.get("content"),  "%" + kw + "%"),		// 내용
										cb.like(u1.get("username"),  "%" + kw + "%"),	// 작성자
										cb.like(a.get("content"),  "%" + kw + "%"), 	// 답변내용
										cb.like(u2.get("username"),  "%" + kw + "%")));	// 답변 작성자				
			}
		};
	}
	
	// 질문목록 조회 - 페이지의 질문 목록을 조회
	public Page<Question> getList(int page, String kw, Integer category){
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createDate"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));		// 10개씩 조회
		Specification<Question> spec = search(kw, category);
		
		return this.questionRepository.findAll(spec, pageable);
	}
	
	public Question getQuestion(Integer id) {
		Optional<Question> question = this.questionRepository.findById(id);
		if(question.isPresent()) {
			return question.get();
		}
		else {
			throw new DataNotFoundException("question not found");
		}
	}
	
	public void create(String subject, String content, Category category, SiteUser author) {
		Question q = new Question();
		q.setSubject(subject);
		q.setContent(content);
		q.setCategory(category);
		q.setAuthor(author);
		q.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(q);
	}
	
	public void modify(Question question, String subject, String content) {
		question.setSubject(subject);
		question.setContent(content);
		question.setModifyDate(LocalDateTime.now());
		this.questionRepository.save(question);
	}
	
	public void delete(Question question) {
		this.questionRepository.delete(question);
	}
	
	public void vote(Question question, SiteUser siteUser) {
		question.getVoter().add(siteUser);
		this.questionRepository.save(question);
	}
	
	public void views(Question question) {
		question.setViewCount(question.getViewCount() + 1);	// 조회수 증가
		this.questionRepository.save(question);
	}
	
}
