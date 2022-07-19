package com.mysite.sbb.question;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.answer.AnswerForm;
import com.mysite.sbb.answer.AnswerService;
import com.mysite.sbb.category.Category;
import com.mysite.sbb.category.CategoryService;
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
	private final CategoryService categoryService;
	private final AnswerService answerService;
	
	@RequestMapping("/list")
	public String list(Model model, @RequestParam(value="page", defaultValue = "0") int page,	// 페이지 번호를 매개변수로 받음
					@RequestParam(value="kw", defaultValue = "") String kw,
					@RequestParam(value="category", defaultValue = "") Integer category) {		
//		List<Question> questionList = this.questionService.getList();
		Page<Question> paging = this.questionService.getList(page, kw, category);
		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);
		model.addAttribute("category", category);		
		
		List<Category> categoryList = this.categoryService.getCategoryAll();
		model.addAttribute("categoryList", categoryList);
				
		return "question_list";		// question_list.html 리턴
	}
	
	@RequestMapping(value = "/detail/{id}")
	public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm, @RequestParam(value="answer_page", defaultValue = "0") int answer_page) {
		Question question = this.questionService.getQuestion(id);		
		Page<Answer> answerList = this.answerService.getList(id, answer_page);
		
		// 조회 수 증가
		this.questionService.views(question);
		
		// 화면 뿌리기
		model.addAttribute("question", question);
		model.addAttribute("answerList", answerList);
		return "question_detail";
	}
	
	@PreAuthorize("isAuthenticated()")		
	@GetMapping("/create")
	public String questionCreate(Model model, QuestionForm questionForm, @RequestParam(value="category", defaultValue="0") int category_id) {
		
		// 카테고리 정보 가져오기
		List<Category> category = this.categoryService.getCategoryAll();
		model.addAttribute("categoryList", category);		
		questionForm.setCategory(category_id);
		
		return "question_form";
	}

	
	@PreAuthorize("isAuthenticated()")	// 로그인 여부 체크
	@PostMapping("/create")				// 매개변수가 다르면 @GetMapping과 같은 메서드명 사용 가능
	public String questionCreate(Model model, @Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal) {	// (@RequestParam String subject, @RequestParam String content)
		if (bindingResult.hasErrors()) {		// 검증에서 오류가 발생한 경우 폼 화면 리로드
			List<Category> category = this.categoryService.getCategoryAll();
			model.addAttribute("categoryList", category);
			
			return "question_form";
		}
		
		// 질문 등록
		SiteUser siteUser = this.userService.getUser(principal.getName());
		Category category = this.categoryService.getCategoryById(questionForm.getCategory());	// category.id로 조회
		this.questionService.create(questionForm.getSubject(), questionForm.getContent(), category, siteUser);	// 검증된 Form 값 받아서 db 등록
		return "redirect:/question/list";		// 질문 저장 후 목록으로 이동
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify/{id}")
	public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id, Principal principal) {
		Question question = this.questionService.getQuestion(id);
		if (!question.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
		}
		
		// 수정 화면 set
		questionForm.setSubject(question.getSubject());
		questionForm.setContent(question.getContent());
		questionForm.setCategory(question.getCategory().getId());
		
		return "question_form";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify/{id}")
	public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal, @PathVariable("id") Integer id) {
		if (bindingResult.hasErrors()) {
			return "question_form";
		}
		Question question = this.questionService.getQuestion(id);
		if(!question.getAuthor().getUsername().equals(principal.getName())) {		// 작성자와 로그인 유저와 같은지 확인
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
		}
		
		// 질문 수정
		this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent());
		return String.format("redirect:/question/detail/%s", id);
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{id}")
	public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
		Question question = this.questionService.getQuestion(id);
		if(!question.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
		}
		
		// 질문 삭제
		this.questionService.delete(question);
		return "redirect:/";	// 루트로 리다이렉트
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/vote/{id}")
	public String questionVote(Principal principal, @PathVariable("id") Integer id) {
		Question question = this.questionService.getQuestion(id);
		SiteUser siteUser = this.userService.getUser(principal.getName());
		this.questionService.vote(question, siteUser);
		return String.format("redirect:/question/detail/%d", id);
	}
}
