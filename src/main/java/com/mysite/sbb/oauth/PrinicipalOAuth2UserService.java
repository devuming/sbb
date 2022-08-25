package com.mysite.sbb.oauth;

import java.time.LocalDateTime;
import java.util.Map;
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

import com.mysite.sbb.user.GoogleUserInfo;
import com.mysite.sbb.user.NaverUserInfo;
import com.mysite.sbb.user.OAuth2UserInfo;
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
		System.out.println("AccessToken : " + userRequest.getAccessToken());
		System.out.println("ClientRegistration : " + userRequest.getClientRegistration());
		System.out.println("userRequest : " + super.loadUser(userRequest).getAttributes());

		
		OAuth2User oauth2User = super.loadUser(userRequest);
		OAuth2UserInfo oAuthUserInfo = null;
		
		String provider = userRequest.getClientRegistration().getRegistrationId();	// provider 이름
		if(provider.equals("google")) {
			System.out.println("구글 로그인 요청");
			oAuthUserInfo = new GoogleUserInfo(oauth2User.getAttributes());
		}
		else if(provider.equals("naver")) {
			System.out.println("네이버 로그인 요청");
			oAuthUserInfo = new NaverUserInfo((Map)oauth2User.getAttributes().get("response"));
		}
		
		String sns_id = oAuthUserInfo.getProviderId();
		String sns_name = oAuthUserInfo.getName();
		String email = oAuthUserInfo.getEmail();
		String username = provider + "_" + sns_id;
		String password = sns_id;

		// 가입된 유저인지 확인 
		Optional<SiteUser> user = this.userRepository.findByusername(username);
		SiteUser siteUser = null;
		
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
