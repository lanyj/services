package cn.lanyj.services.support.web.translate.impl.helper.http;

import java.io.InputStream;
import java.util.Map;

public interface HttpParams {
	public String send2String(String baseUrl) throws Exception;
	public InputStream send2InputStream(String baseUrl) throws Exception;
	public HttpParams put(String key, String value);
	public Map<String, String> getParams();
}
