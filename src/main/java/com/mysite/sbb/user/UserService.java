package com.mysite.sbb.user;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mysite.sbb.DataNotFoundException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;		// 해싱을 통한 비밀번호 암호화
	
	public SiteUser create(String username, String email, String password) {
		SiteUser user = new SiteUser();
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(password));
		user.setCreateDate(LocalDateTime.now());
		this.userRepository.save(user);
		return user;
	}
	public SiteUser createSNS(String username, String email, String password, String sns_id, String sns_name, String sns_type) {
		SiteUser user = new SiteUser();
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(password));
		user.setSns_id(sns_id);
		user.setSns_name(sns_name);
		user.setSns_type(sns_type);
		user.setCreateDate(LocalDateTime.now());
		this.userRepository.save(user);
		return user;
	}
	
	public SiteUser getUser(String username) {
		Optional<SiteUser> siteUser = this.userRepository.findByusername(username);
		if(siteUser.isPresent()) {
			return siteUser.get();
		}else {
			return null;
		}
	}
	
}
