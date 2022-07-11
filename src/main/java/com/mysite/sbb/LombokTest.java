package com.mysite.sbb;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
public class LombokTest {
	private final String hello;
	private final int lombok;
	
	public static void main(String[] args) {
		LombokTest test = new LombokTest("헬로", 5);
		
		System.out.println(test.getHello());
		System.out.println(test.getLombok());
	}

}
