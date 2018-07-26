package cn.lanyj.services.service;

import cn.lanyj.services.error.ServiceProcessException;

public class PluginServiceNotFoundException extends ServiceProcessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8323394723106339588L;

	public PluginServiceNotFoundException(String msg) {
		super(msg);
	}
	
}
