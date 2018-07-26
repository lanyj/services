package cn.lanyj.services.open.plugin.controller;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lanyj.services.models.Plugin;
import cn.lanyj.services.service.PluginService;

@Controller
@RequestMapping("/open/plugin")
public class PluginController {
	
	@Autowired
	PluginService service;
	
	@RequestMapping("/invoke/{id}")
	public void invoke(@PathVariable String id, HttpServletRequest req, HttpServletResponse rep) {
		boolean reload = req.getAttribute("reload") != null;
		service.invoke(id, reload, req, rep);
	}
	
	@ResponseBody
	@RequestMapping("/desc/{id}")
	public Plugin desc(@PathVariable String id, HttpServletRequest req, HttpServletResponse rep) {
		boolean reload = req.getAttribute("reload") != null;
		Plugin _plugin = null;
		if(reload) {
			_plugin = service.sync(id);
		} else {
			_plugin = service.get(id);
		}
		if(_plugin == null) {
			rep.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		
		// https://github.com/FasterXML/jackson-datatype-hibernate/issues/87
		// this issue is not solved yet
		Plugin plugin = new Plugin();
		try {
			BeanUtils.copyProperties(plugin, _plugin);
		} catch (IllegalAccessException | InvocationTargetException e) {
		}
//		plugin.setId(_plugin.getId());
//		plugin.setName(_plugin.getName());
//		plugin.setDescription(_plugin.getDescription());
//		plugin.setRemoteUrl(_plugin.getRemoteUrl());
//		plugin.setCreatedAt(_plugin.getCreatedAt());
//		plugin.setUpdatedAt(_plugin.getUpdatedAt());
		return plugin;
	}
	
//	@ResponseBody
//	@RequestMapping("/save")
//	public Plugin save(@RequestParam String name, @RequestParam String desc, @RequestParam String url, HttpServletResponse rep) {
//		if(!StringUtils.hasText(url)) {
//			rep.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//			return null;
//		}
//		
//		Plugin plugin = service.findByUrl(url);
//		if(plugin != null) {
//			return plugin;
//		}
//		plugin = new Plugin();
//		plugin.setName(name);
//		plugin.setDescription(desc);
//		plugin.setRemoteUrl(url);
//		service.save(plugin);
//		
//		return plugin;
//	}
	
}
