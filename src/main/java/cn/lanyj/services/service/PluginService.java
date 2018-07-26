package cn.lanyj.services.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.lanyj.services.models.Plugin;
import cn.lanyj.services.repositories.PluginRepository;

@Service("plugin-service")
public class PluginService implements CacheableService<Plugin> {
	private Logger log = LoggerFactory.getLogger(PluginService.class);
	
	@Autowired
	private PluginRepository repository;
	
	public PluginService() {
	}
	
	public void invoke(String _plugin, boolean reload, HttpServletRequest req, HttpServletResponse rep) {
		if(!StringUtils.hasText(_plugin)) {
			log.debug("plugin {} not found", _plugin);
			throw new PluginServiceNotFoundException("plugin " + _plugin + " not found");
		}
		Plugin plugin = null;
		if(reload) {
			plugin = sync(_plugin);
		} else {
			plugin = get(_plugin);
		}
		if(plugin == null) {
			log.debug("plugin {} not found", _plugin);
			throw new PluginServiceNotFoundException("plugin " + _plugin + " not found");
		}
		
		try {
			rep.sendRedirect(plugin.getRemoteUrl());
		} catch (IOException e) {
		}
	}

	@CacheEvict(value="pluginCache", key="#id")
	@Override
	public Plugin sync(String id) {
		Plugin plugin = repository.getOne(id);
		return plugin;
	}

	@Cacheable(value="pluginCache", key="#id")
	@Override
	public Plugin get(String id) {
		return repository.getOne(id);
	}

	@Override
	public String save(Plugin value) {
		String id = repository.save(value).getId();
		value.setId(id);
		return id;
	}
	
	public Plugin findByUrl(String url) {
		return repository.findByRemoteUrl(url);
	}
	
}
