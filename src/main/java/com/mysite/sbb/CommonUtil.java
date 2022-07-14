package com.mysite.sbb;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Component;

@Component	// Bean 객체로 등록됨 (템플릿에서 바로 사용가능)
public class CommonUtil {
	public String markdown(String markdown) {
		Parser parser = Parser.builder().build();
		Node document = parser.parse(markdown);
		HtmlRenderer renderer = HtmlRenderer.builder().build();	// HTML 형식으로 변환하는 렌더러
		return renderer.render(document);		// 마크다운을 Html로 변환하여 리턴
	}
}
