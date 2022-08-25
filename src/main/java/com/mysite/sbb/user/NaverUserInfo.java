package com.mysite.sbb.user;

import java.util.Map;

public class NaverUserInfo implements OAuth2UserInfo{
	private Map<String, Object> attributes;
	
	public NaverUserInfo (Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	
	@Override
	public String getProvider() {
		return "naver";
	}

	@Override
	public String getProviderId() {
		// response 의 값 parsing
		return attributes.get("id").toString();
	}

	@Override
	public String getName() {
		return attributes.get("name").toString();
	}

	@Override
	public String getEmail() {
		return attributes.get("email").toString();
	}

}
