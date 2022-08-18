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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.answer.AnswerForm;
import com.mysite.sbb.answer.AnswerService;
import com.mysite.sbb.question.Question;
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
	@RequestMapping("/category")		
	public String categoryEdit(Model model, CategoryForm categoryForm, Principal principal
							, @RequestParam(value="category", defaultValue = "") Integer category) {

		List<Category> categoryList = this.categoryService.getCategoryAll();
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("userRoles", UserRole.values());
		model.addAttribute("category", category);
		model.addAttribute("hidden", true);		// 전체보기 메뉴 hidden
		
		if (category != null) {
			for(int i = 0; i < categoryList.size(); i++) {
				Category c = categoryList.get(i);
				if(c.getId().equals(category)) {
					categoryForm.setTitle(c.getTitle());
					categoryForm.setUserRole(c.getUserRole());
					categoryForm.setSortOrder(c.getSortOrder());
					break;
				}
			}
		}
		
		return "category_form";
	}

	@PreAuthorize("isAuthenticated()")	// 로그인 여부 체크
	@PostMapping("/category")			// 매개변수가 다르면 @GetMapping과 같은 메서드명 사용 가능
	public String categoryCreate(Model model, @Valid CategoryForm categoryForm, BindingResult bindingResult, Principal principal
							, @RequestParam(value="category", defaultValue = "") Integer category_id) {	
		if (bindingResult.hasErrors()) {		// 검증에서 오류가 발생한 경우 폼 화면 리로드
			List<Category> categoryList = this.categoryService.getCategoryAll();
			model.addAttribute("categoryList", categoryList);
			model.addAttribute("userRoles", UserRole.values());
			model.addAttribute("category", category_id);
			
			return "category_form";
		}
		
		if (category_id == null) {
			// 카테고리 등록
			this.categoryService.create(categoryForm.getTitle(), categoryForm.getUserRole(), categoryForm.getSortOrder());	// 검증된 Form 값 받아서 db 등록
		}
		else {
			// 카테고리 수정
			Category category = this.categoryService.getCategoryById(category_id);
			this.categoryService.modify(category, categoryForm.getTitle(), categoryForm.getUserRole(), categoryForm.getSortOrder());
		}
		
		return "redirect:/admin/category";		// 질문 저장 후 목록으로 이동
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/category/delete/{id}")
	public String categoryDelete(Principal principal, @PathVariable("id") Integer id) {
		Category category = this.categoryService.getCategoryById(id);
		
		// 카테고리 삭제
		this.categoryService.delete(category);
		return "redirect:/admin/category";	
	}
}
