package cn.lanyj.services.open.osgi.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lanyj.services.support.web.osgi.OSGIService;

@Controller
@RequestMapping("/open/osgi")
public class OSGIController {

	@Autowired
	OSGIService service;
	
	@ResponseBody
	@RequestMapping("/invoke/{name}")
	public Object invoke(@PathVariable String name, HttpServletRequest req, HttpServletResponse rep) {
		return service.invoke(name, req, rep);
	}
	
	@ResponseBody
	@RequestMapping("/avaliable/{name}")
	public boolean avaliable(@PathVariable String name) {
		return service.avaliabe(name);
	}
	
//	@ResponseBody
//	@RequestMapping("/reload")
//	public void reload(HttpServletRequest req) {
//		service.reload();
//	}
	
}
