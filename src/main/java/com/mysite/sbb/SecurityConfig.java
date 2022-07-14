package com.mysite.sbb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.mysite.sbb.user.UserSecurityService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private final UserSecurityService userSecurityService;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		http.authorizeRequests().antMatchers("/**").permitAll()		// 모든 인증되지 않은 요청 허락
			.and().csrf().ignoringAntMatchers("/h2-console/**")		// csrf 토큰 처리시 h2 콘솔은 예외
			.and()
				.headers()
				.addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))	// frame 구조인 콘텐츠처리
			.and()
				.formLogin()
				.loginPage("/user/login")		// 로그인 페이지 : /user/login
				.defaultSuccessUrl("/")			// 로그인 성공 시 루트로 이동
			.and()
				.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))	// 로그아웃 URL
				.logoutSuccessUrl("/")												// 로그아웃 성공 시 루트로 이동
				.invalidateHttpSession(true);										// 로그아웃 시 사용자 세션 삭제
		
		return http.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();	// 해싱을 통한 비밀번호 암호화
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{	// 인증담당
		return authenticationConfiguration.getAuthenticationManager();
	}
}
