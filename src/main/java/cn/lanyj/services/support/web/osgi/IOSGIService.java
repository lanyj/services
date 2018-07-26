package cn.lanyj.services.support.web.osgi;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IOSGIService {
	
	/**
	 * osgi service's name
	 * @return
	 */
	public String name();
	
	/**
	 * to invoke osgi service
	 * @param req
	 * @param rep
	 * @return
	 */
	public Object invoke(HttpServletRequest req, HttpServletResponse rep);
	
}
