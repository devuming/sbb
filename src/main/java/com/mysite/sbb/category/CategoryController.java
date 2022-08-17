package com.mysite.sbb.category;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.answer.AnswerForm;
import com.mysite.sbb.answer.AnswerService;
import com.mysite.sbb.question.QuestionForm;
import com.mysite.sbb.question.QuestionService;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserRole;
import com.mysite.sbb.user.UserService;

import lombok.RequiredArgsConstructor;


@RequestMapping("/admin")
@RequiredArgsConstructor	// DI
@Controller
public class CategoryController {
	private final CategoryService categoryService;
	
	@PreAuthorize("isAuthenticated()")	// 로그인 여부 체크
	@GetMapping("/category/create")		
	public String categoryCreate(Model model, CategoryForm categoryForm, Principal principal) {

		model.addAttribute("userRoles", UserRole.values());
		return "category_form";
	}

	@PreAuthorize("isAuthenticated()")	// 로그인 여부 체크
	@PostMapping("/category/create")	// 매개변수가 다르면 @GetMapping과 같은 메서드명 사용 가능
	public String questionCreate(Model model, @Valid CategoryForm categoryForm, BindingResult bindingResult, Principal principal) {	
		if (bindingResult.hasErrors()) {		// 검증에서 오류가 발생한 경우 폼 화면 리로드
			model.addAttribute("userRoles", UserRole.values());
			
			return "category_form";
		}
		
		// 카테고 등록
		this.categoryService.create(categoryForm.getTitle(), categoryForm.getUserRole());	// 검증된 Form 값 받아서 db 등록
		return "redirect:/question/list";		// 질문 저장 후 목록으로 이동
	}
}
