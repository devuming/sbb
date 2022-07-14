package com.mysite.sbb.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService{		// UserDetailsService 구현
	
	private final UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{		// 인터페이스의 loadUserByUsername 메소드 구현
		// 1. username 으로 SiteUser 조회
		Optional<SiteUser> _siteUser = this.userRepository.findByusername(username);
		
		if (_siteUser.isEmpty()) {	// 사용자가 없는 경우 NotFound
			throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
		}
		
		// 2. 유저의 권한 체크
		SiteUser siteUser = _siteUser.get();
		List<GrantedAuthority> authorities = new ArrayList<>();
		
		if("admin".equals(username)) {		// 사용자명이 admin인 경우에만 관리자 권한 부여
			authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
		} else {
			authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
		}
		
		return new User(siteUser.getUsername(), siteUser.getPassword(), authorities);
	}

}
