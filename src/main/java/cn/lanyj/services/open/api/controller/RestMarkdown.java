package cn.lanyj.services.open.api.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lanyj.services.support.web.markdown.MarkdownService;

@Controller
@RequestMapping(path="/open/api/markdown")
public class RestMarkdown {
	
	@Autowired
	MarkdownService service;
	
	@ResponseBody
	@RequestMapping(path="/html")
	public String html(HttpServletResponse rep, @RequestParam(name="content", required=true) String content) {
		return service.renderToHtml(content);
	}
	
}
