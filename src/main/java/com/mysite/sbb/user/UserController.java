package com.mysite.sbb.user;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {
	
	private final UserService userService;
	private final QuestionService questionService;
	
	@GetMapping("/signup")
	public String signup(UserCreateForm userCreateForm) {
		return "signup_form";
	}
	
	@PostMapping("/signup")
	public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "signup_form";
		}
		
		if(!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
			bindingResult.rejectValue("password2", "passwordInCorrect", "2개의 패스워드가 일치하지 않습니다.");
			return "signup_form";
		}
		
		try {
			// 회원등록
			userService.create(userCreateForm.getUsername(), userCreateForm.getEmail(), userCreateForm.getPassword1());			
		} catch(DataIntegrityViolationException e) {		// 데이터 중복 오류가 발생한 경우
			e.printStackTrace();
			bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");		
			return "signup_form";
		} catch(Exception e) {
			e.printStackTrace();
			bindingResult.reject("signupFailed", e.getMessage());
			return "signup_form";
		}
		
		return "redirect:/";
	}
	
	@GetMapping("/login")		// PostMapping은 스프링 시큐리티가 대신 처리
	public String login() {
		return "login_form";
	}
	
	@RequestMapping("/profile/question")
	public String profile(Principal principal, Model model, @RequestParam(value="page", defaultValue="0") int page) {
		SiteUser author = this.userService.getUser(principal.getName());
		Page<Question> paging = this.questionService.getListByAuthor(page, author);

		model.addAttribute("type", "question");
		model.addAttribute("paging", paging);
		return "profile_detail";
	}
}
