package com.mysite.sbb.category;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionRepository;
import com.mysite.sbb.user.SiteUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CategoryService {
	
	private final CategoryRepository categoryRepository;
	
	public void create(String title) {
		Category category = new Category();
		category.setTitle(title);
		this.categoryRepository.save(category);
	}

	
	public List<Category> getCategoryAll() {		
		return this.categoryRepository.findAll();
	}

	public Category getCategoryById(Integer id) {
		Optional<Category> category = this.categoryRepository.findById(id);
		if(category.isPresent()) {
			return category.get();
		}else {
			throw new DataNotFoundException("category Not Found");
		}
	}

	public void create(String title, String userRole) {
		Category c = new Category();
		c.setTitle(title);
		c.setUserRole(userRole);
		this.categoryRepository.save(c);
	}

	public void modify(Category category, String title, String userRole) {
		category.setTitle(title);
		category.setUserRole(userRole);
		this.categoryRepository.save(category);
	}
	
	public void delete(Category category) {
		this.categoryRepository.delete(category);
	}
}
