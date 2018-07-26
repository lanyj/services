package cn.lanyj.services.support.web.osgi.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.lanyj.services.support.web.osgi.IOSGIService;

public class SayHelloOSGIService implements IOSGIService {

	@Override
	public String name() {
		return "hello";
	}

	@Override
	public Object invoke(HttpServletRequest req, HttpServletResponse rep) {
		return "Hello world!";
	}
	
}
