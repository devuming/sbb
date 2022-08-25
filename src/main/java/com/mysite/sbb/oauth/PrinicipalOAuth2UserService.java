package com.mysite.sbb.oauth;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.mysite.sbb.user.PrincipalDetail;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserRepository;
import com.mysite.sbb.user.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PrinicipalOAuth2UserService extends DefaultOAuth2UserService{
	
	private final UserRepository userRepository;
	
	// 구글로부터 받은 userRequest 데이터에 대한 후처리 메소드
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		System.out.println("??");
		System.out.println("userRequest : " + userRequest.getAccessToken());
		System.out.println("userRequest : " + userRequest.getClientRegistration());
		System.out.println("userRequest : " + super.loadUser(userRequest).getAttributes());

		
		OAuth2User oauth2User = super.loadUser(userRequest);
		
		String provider = userRequest.getClientRegistration().getRegistrationId();	// google
		String sns_id = oauth2User.getAttribute("sub");
		String sns_name = oauth2User.getAttribute("name");
		String email = oauth2User.getAttribute("email");
		String username = provider + "_" + sns_id;
		String password = sns_id;
//		String password = "";
		// 가입된 유저인지 확인 
		Optional<SiteUser> user = this.userRepository.findByusername(username);
		SiteUser siteUser = null;
		System.out.println(provider);
		
		if (user.isEmpty()) {
			// 회원가입
			System.out.println(provider + "회원가입");
			siteUser = SiteUser.builder()
									.username(username)
									.password(password)
									.email(email)
									.sns_id(sns_id)	
									.sns_type(provider)
									.sns_name(sns_name)
									.build();
			this.userRepository.save(siteUser);
		}
		else {
			System.out.println(provider + "로그인");
			siteUser = user.get();
		}
		
		return new PrincipalDetail(siteUser, oauth2User.getAttributes());
	}
	
}
