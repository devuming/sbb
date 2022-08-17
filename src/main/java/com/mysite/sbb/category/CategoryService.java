package com.mysite.sbb.category;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import com.mysite.sbb.user.UserRole;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CategoryService {
	
	private final CategoryRepository categoryRepository;
		
	public List<Category> getCategoryAll() {		
		List<Category> category = this.categoryRepository.findAll();
		Collections.sort(category, new Comparator<Category>() {
			@Override
			public int compare(Category c1, Category c2) {
				return c1.getSortOrder() - c2.getSortOrder();
			}
		});
		
		return category;
	}

	public Category getCategoryById(Integer id) {
		Optional<Category> category = this.categoryRepository.findById(id);
		if(category.isPresent()) {
			return category.get();
		}else {
			throw new DataNotFoundException("category Not Found");
		}
	}

	public void create(String title, String userRole, Integer order) {
		Category category = new Category();
		category.setTitle(title);
		category.setUserRole(userRole.isEmpty() ? UserRole.USER.getValue() : userRole);
		category.setSortOrder(order == null ? 0 : order);
		this.categoryRepository.save(category);
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
