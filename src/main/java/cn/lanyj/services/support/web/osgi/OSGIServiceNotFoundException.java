package cn.lanyj.services.support.web.osgi;

import cn.lanyj.services.error.ServiceProcessException;

public class OSGIServiceNotFoundException extends ServiceProcessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7304126544630609086L;

	public OSGIServiceNotFoundException(String message) {
		super(message);
	}

}
