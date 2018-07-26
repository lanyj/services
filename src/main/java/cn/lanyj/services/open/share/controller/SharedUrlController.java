package cn.lanyj.services.open.share.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lanyj.services.models.SharedUrl;
import cn.lanyj.services.service.SharedUrlService;

@Controller
@RequestMapping("/open/url")
public class SharedUrlController {

	@Autowired
	private SharedUrlService service;
	
	@RequestMapping("/invoke/{id}")
	public void invoke(@PathVariable String id, HttpServletRequest req, HttpServletResponse rep) {
		if(!StringUtils.hasText(id)) {
			rep.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		boolean reload = req.getParameter("reload") != null;
		SharedUrl url = reload ? service.sync(id) : service.get(id);
		if(url == null) {
			rep.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		try {
			rep.sendRedirect(url.getUrl());
		} catch (IOException e) {
			rep.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
		}
	}
	
	@ResponseBody
	@RequestMapping("/desc/{id}")
	public SharedUrl desc(@PathVariable String id, HttpServletRequest req, HttpServletResponse rep) {
		if(!StringUtils.hasText(id)) {
			rep.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		boolean reload = req.getParameter("reload") != null;
		SharedUrl _url = reload ? service.sync(id) : service.get(id);
		
		SharedUrl url = new SharedUrl();
		try {
			BeanUtils.copyProperties(url, _url);
		} catch (IllegalAccessException | InvocationTargetException e) {
			rep.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
			return null;
		}
		return url;
	}
	
	
	@ResponseBody
	@RequestMapping("/save")
	public SharedUrl save(@RequestParam(name="expire", required=false, defaultValue="3") int expire, @RequestParam String desc, @RequestParam String url, HttpServletResponse rep) {
		if(!StringUtils.hasText(url)) {
			rep.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
		SharedUrl _url = service.findByUrl(url);
		if(_url == null) {
			_url = new SharedUrl();
			_url.setDescription(desc);
			_url.setUrl(url);
			_url.setExpireDay(expire);
			service.save(_url);
			
			return _url;
		}
		return _url;
	}
}
