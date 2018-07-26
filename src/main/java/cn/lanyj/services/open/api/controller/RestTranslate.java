package cn.lanyj.services.open.api.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lanyj.services.service.TranslateService;
 
@Controller
@RequestMapping("/open/api/translate")
public class RestTranslate {

	@Autowired
	TranslateService service;
	
	@ResponseBody
	@RequestMapping("/trans")
	public String translate(HttpServletResponse rep, @RequestParam(required=true) String from, @RequestParam(required=true) String to, @RequestParam(required=true) String content) {
		return service.trans(from, to, content);
	}
	
	@RequestMapping("/tts")
	public void tts(HttpServletResponse rep, @RequestParam(required=true) String lang, @RequestParam(required=true) String content) {
		byte[] buf = service.tts(lang, content);
		try {
			rep.setContentType("audio/mpeg");
			rep.getOutputStream().write(buf);
		} catch (IOException e) {
		}
	}
	
}
