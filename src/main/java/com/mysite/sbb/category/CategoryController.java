package com.mysite.sbb.category;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mysite.sbb.user.UserRole;


@RequestMapping("/admin")
@Controller
public class CategoryController {
	@PreAuthorize("isAuthenticated()")	// 로그인 여부 체크
	@RequestMapping("/category/create")				// 매개변수가 다르면 @GetMapping과 같은 메서드명 사용 가능
	public String categoryCreate(Model model, @Valid CategoryForm categoryForm, BindingResult bindingResult, Principal principal) {

		model.addAttribute("userRoles", UserRole.values());
		return "category_form";
	}
}
