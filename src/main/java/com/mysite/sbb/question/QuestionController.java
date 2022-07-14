package com.mysite.sbb.question;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysite.sbb.answer.AnswerForm;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/question")
@RequiredArgsConstructor		// 생성자를 작성하여 객체를 주입하는 방식 
@Controller
public class QuestionController {
	
//	private final QuestionRepository questionRepository;
	private final QuestionService questionService;		// 리포지터리대신 서비스를 사용하여 데이터 처리 (Controller -> Service -> Repository)
	private final UserService userService;
	
	@RequestMapping("/list")
	public String list(Model model, @RequestParam(value="page", defaultValue = "0") int page) {		// 페이지 번호를 매개변수로 받음
//		List<Question> questionList = this.questionService.getList();
		Page<Question> paging = this.questionService.getList(page);
		model.addAttribute("paging", paging);
		return "question_list";		// question_list.html 리턴
	}
	
	@RequestMapping(value = "/detail/{id}")
	public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm) {
		Question question = this.questionService.getQuestion(id);
		model.addAttribute("question", question);
		return "question_detail";
	}
	
	@PreAuthorize("isAuthenticated()")		
	@GetMapping("/create")
	public String questionCreate(QuestionForm questionForm) {
		return "question_form";
	}

	
	@PreAuthorize("isAuthenticated()")	// 로그인 여부 체크
	@PostMapping("/create")				// 매개변수가 다르면 @GetMapping과 같은 메서드명 사용 가능
	public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal) {	// (@RequestParam String subject, @RequestParam String content)
		if (bindingResult.hasErrors()) {		// 검증에서 오류가 발생한 경우 폼 화면 리로드
			return "question_form";
		}
		
		SiteUser siteUser = this.userService.getUser(principal.getName());
		this.questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser);	// 검증된 Form 값 받아서 db 등록
		return "redirect:/question/list";		// 질문 저장 후 목록으로 이동
	}
}
