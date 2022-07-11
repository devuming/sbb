package com.mysite.sbb.question;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;

@RequestMapping("/question")
@RequiredArgsConstructor		// 생성자를 작성하여 객체를 주입하는 방식 
@Controller
public class QuestionController {
	
//	private final QuestionRepository questionRepository;
	private final QuestionService questionService;		// 리포지터리대신 서비스를 사용하여 데이터 처리 (Controller -> Service -> Repository)
	
	@RequestMapping("/list")
	public String list(Model model) {
		List<Question> questionList = this.questionService.getList();
		model.addAttribute("questionList", questionList);
		return "question_list";		// question_list.html 리턴
	}
	
	@RequestMapping(value = "/detail/{id}")
	public String detail(Model model, @PathVariable("id") Integer id) {
		Question question = this.questionService.getQuestion(id);
		model.addAttribute("question", question);
		return "question_detail";
	}
}
