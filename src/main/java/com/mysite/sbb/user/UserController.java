package com.mysite.sbb.user;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.Principal;
import java.security.SecureRandom;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.answer.AnswerService;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {
	
	private final UserService userService;
	private final QuestionService questionService;
	private final AnswerService answerService;
	
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
	public String login(Model model, HttpSession session) {
		model.addAttribute("naverLoginUrl", generateNaverUrl(session));
		return "login_form";
	}
	
	private final String CLIENT_ID = "BXenC7aa1ecXRYtrOmoa";
	private final String CLIENT_SECRET = "rjMGMSte2t";
	private final String CALLBACK_URL = "http://localhost:8080/user/callbackNaver";
	public String generateState()
	{
	    SecureRandom random = new SecureRandom();
	    return new BigInteger(130, random).toString(32);
	}
	
	public String generateNaverUrl(HttpSession session) {
		String state = "";
		String requestUrl = "";

		//---- 1. 로그인 연동 URL 생성
		try {
			String callbackUrl = URLEncoder.encode(CALLBACK_URL, "UTF-8");

			state = generateState();				// 랜덤
			session.setAttribute("state", state);		// 세션 저장 (csrf 공격 방지)

			
			requestUrl = "https://nid.naver.com/oauth2.0/authorize"
					+ "?response_type=code"
					+ "&client_id=" + CLIENT_ID
					+ "&state=" + state
					+ "&redirect_uri=" + callbackUrl;

			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return requestUrl;
	}

	
	@RequestMapping("/callbackNaver")
	public String callbackNaver(HttpSession session
								, @RequestParam(value="code", defaultValue = "") String code
								, @RequestParam(value="state", defaultValue = "") String state
								, @RequestParam(value="error", defaultValue = "") String error
								, @RequestParam(value="error_description", defaultValue = "") String error_description) {
		// /callbackNaver/redirect?code={code값}&state={state값}
		String storedState = session.getAttribute("state").toString();
		if (!storedState.equals(state)) {
			return "login_form";		// TODO : 오류 발생 시키기
		}
		
		//---- 2. Access Token 발급
		String accessToken = "";
	    String refreshToken = "";	    
		int expire = 0;
		
		String tokenUrl = "https://nid.naver.com/oauth2.0/token"
				+ "?grant_type=authorization_code"
				+ "&client_id=" + CLIENT_ID
				+ "&client_secret=" + CLIENT_SECRET
				+ "&code=" + code;

		
		try {
			URL url = new URL(tokenUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET"); // Get 방식 요청 
			
			int responseCode = con.getResponseCode();
			BufferedReader br;

			if (responseCode == 200) 
			{ // 정상 호출
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			} else { // 에러 발생
				br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			}
			String inputLine;
			StringBuffer res = new StringBuffer();
			
			while ((inputLine = br.readLine()) != null) 
			{
				res.append(inputLine);
			}
			br.close();
			
			if (responseCode == 200) 
			{
				// JSON Parsing
				JSONParser jsonParser = new JSONParser();
				JSONObject jsonObject = (JSONObject) jsonParser.parse(res.toString()); 

				accessToken = jsonObject.get("access_token").toString();
				refreshToken = jsonObject.get("refresh_token").toString();
				expire = Integer.parseInt(jsonObject.get("expires_in").toString());
				
				System.out.println("accessToken : " + accessToken);
			}		
			
		} catch (Exception e) {
			System.out.println(e);
		}
	    
		
		//---- 3. 프로필 정보 조회
		String profileUrl = "https://openapi.naver.com/v1/nid/me";
		
		// response
		String sns_id = ""; 		// response/id
		String sns_name = ""; 		//response/name
		String email = "";			//response/email

		
		try {
			URL url = new URL(profileUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST"); // Get 방식 요청 
			con.setRequestProperty("Authorization", "Bearer " + accessToken);

			
			int responseCode = con.getResponseCode();
			BufferedReader br;

			if (responseCode == 200) { // 정상 호출
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			} else { // 에러 발생
				br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			}
			String inputLine;
			StringBuffer res = new StringBuffer();
			
			while ((inputLine = br.readLine()) != null) {
				res.append(inputLine);
			}
			br.close();
			
			if (responseCode == 200) {
				// JSON Parsing
				JSONParser jsonParser = new JSONParser();
				JSONObject jsonObject = (JSONObject) jsonParser.parse(res.toString()); 
				String resultcode = jsonObject.get("resultcode").toString();
				String message = jsonObject.get("message").toString();
				System.out.println(jsonObject.toString());
				
				JSONObject jsResponse = (JSONObject)jsonObject.get("response");
				
				sns_id = jsResponse.get("id").toString();				// response/id	동일인 식별 정보 (고유값)
				sns_name = jsResponse.get("name").toString();			// response/name 사용자 이름
				email = jsResponse.get("email").toString();				// response/email
				System.out.println(jsResponse.toString());
				// 1. 회원여부 확인
				SiteUser siteuser = userService.getUser(sns_id);
				if (siteuser == null) {
					// 2. 회원가입
					userService.createSNS(sns_id, email, sns_id, sns_id, sns_name, "NAVER");	
					siteuser = userService.getUser(sns_id);
				}
				
				// 3. 로그인 처리
				session.setAttribute("username", siteuser.getSns_id());
				session.setAttribute("password", siteuser.getPassword());
			}		
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return "redirect:/user/login";
	}
	
	@RequestMapping("/profile/question")
	public String profile(Principal principal, Model model, @RequestParam(value="page", defaultValue="0") int page) {
		SiteUser author = this.userService.getUser(principal.getName());
		Page<Question> paging = this.questionService.getListByAuthor(page, author);

		model.addAttribute("type", "question");
		model.addAttribute("paging", paging);
		return "profile_detail";
	}
	
	@RequestMapping("/profile/answer")
	public String userAnswer(Principal principal, Model model, @RequestParam(value="page", defaultValue="0") int page) {
		SiteUser author = this.userService.getUser(principal.getName());
		Page<Question> paging = this.questionService.getListByAnswers(page, author);	// 로그인 유저가 답변을 달은 게시물 조회

		model.addAttribute("type", "answer");
		model.addAttribute("paging", paging);
		return "profile_detail";
	}
}
