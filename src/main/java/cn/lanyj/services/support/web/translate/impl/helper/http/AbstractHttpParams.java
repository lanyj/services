package cn.lanyj.services.support.web.translate.impl.helper.http;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public abstract class AbstractHttpParams implements HttpParams{
	protected final Map<String, String> params = new HashMap<>();
	
	@Override
	public HttpParams put(String key, String value){
		params.put(key, value);
		return this;
	}
	
	@Override
	public String send2String(String baseUrl) throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try{
			CloseableHttpResponse response = send(httpClient, baseUrl);
			return EntityUtils.toString(response.getEntity());
		}finally{
			httpClient.close();
		}
	}
	
	@Override
	public InputStream send2InputStream(String baseUrl) throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();
//				HttpClients.custom()
//					.useSystemProperties()
////					.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.79 Safari/537.36")
//					.build();
		try{
			CloseableHttpResponse response = send(httpClient, baseUrl);
			return response.getEntity().getContent();
		}finally{
			httpClient.close();
		}
	}
	
	@Override
	public Map<String, String> getParams() {
		return params;
	}
	
	abstract protected CloseableHttpResponse send(CloseableHttpClient httpClient, String baseUrl) throws Exception ;
}
