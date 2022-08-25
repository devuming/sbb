package com.mysite.sbb.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class PrincipalDetail implements OAuth2User, UserDetails {
	private SiteUser siteUser;
	private Map<String, Object> attributes;

	// 일반 로그인
	public PrincipalDetail(SiteUser siteUser){
		this.siteUser = siteUser;
	}
	
	// OAuth 로그인 
	public PrincipalDetail(SiteUser siteUser, Map<String, Object> attributes){
		this.siteUser = siteUser;
		this.attributes = attributes;
	}
	

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
//		Collection<GrantedAuthority> collect = new ArrayList<>();
//		collect.add(new GrantedAuthority() {
//			
//			@Override
//			public String getAuthority() {
//				// TODO Auto-generated method stub
//				return siteUser.get;
//			}
//		})
		return null;
	}

	@Override
	public String getName() {
		return siteUser.getUsername();
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return siteUser.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return siteUser.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

}
