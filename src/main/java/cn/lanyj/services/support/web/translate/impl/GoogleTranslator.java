package cn.lanyj.services.support.web.translate.impl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.Proxy.Type;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.http.client.utils.URIBuilder;

import cn.lanyj.services.support.web.translate.impl.helper.auxs.AbstractOnlineTranslator;
import cn.lanyj.services.support.web.translate.impl.helper.auxs.LANG;
import cn.lanyj.services.support.web.translate.impl.helper.auxs.TranslatorComponent;
import cn.lanyj.services.support.web.translate.impl.helper.http.HttpGetParams;
import cn.lanyj.services.support.web.translate.impl.helper.http.HttpParams;
import cn.lanyj.services.support.web.translate.impl.helper.http.HttpPostParams;
import net.sf.json.JSONArray;

@TranslatorComponent(id = "google")
final public class GoogleTranslator extends AbstractOnlineTranslator {
	private static ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
	private static final int MAX_LENGTH = 100;
	private static final Proxy PROXY = new Proxy(Type.HTTP, new InetSocketAddress("localhost", 1080));
	
	public GoogleTranslator(){
		langMap.put(LANG.EN, "en");
		langMap.put(LANG.ZH, "zh-CN");
		langMap.put(LANG.RU, "ru");
		langMap.put(LANG.DE, "de");
		langMap.put(LANG.FR, "fr");
		langMap.put(LANG.JA, "ja");
		langMap.put(LANG.ZHTW, "zh-TW");
	}
	
	@Override
	protected String getResponse(LANG from, LANG targ, String query) throws Exception{
		
		HttpParams params = null;
		params = new HttpPostParams();		//统一采用post，若字符长度小于999用get也可以的
		String tk = tk(query);
		
		params.put("client", "t")
				.put("sl", langMap.get(from))
				.put("tl", langMap.get(targ))
				.put("hl", "zh-CN")
				.put("dt", "at")
				.put("dt", "bd")
				.put("dt", "ex")
				.put("dt", "ld")
				.put("dt", "md")
				.put("dt", "qca")
				.put("dt", "rw")
				.put("dt", "rm")
				.put("dt", "ss")
				.put("dt", "t")
				
				.put("ie", "UTF-8")
				.put("oe", "UTF-8")
				.put("source", "btn")
				.put("srcrom", "1")
				.put("ssel", "0")
				.put("tsel", "0")
				.put("kc", "11")
				.put("tk", tk)
				.put("q", query);

		return params.send2String("https://translate.google.cn/translate_a/single");
	}
	
	@Override
	protected String parseString(String jsonString){
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		JSONArray segments = jsonArray.getJSONArray(0);
		StringBuilder result = new StringBuilder();
		
		for(int i=0; i<segments.size(); i++){
			result.append(segments.getJSONArray(i).getString(0));
		}
		
		return new String(result);
	}
	
	private String tk(String val) throws Exception{
		String script ="function tk(a) {"
						+"var TKK = ((function() {var a = 561666268;var b = 1526272306;return 406398 + '.' + (a + b); })());\n"
						+"function b(a, b) { for (var d = 0; d < b.length - 2; d += 3) { var c = b.charAt(d + 2), c = 'a' <= c ? c.charCodeAt(0) - 87 : Number(c), c = '+' == b.charAt(d + 1) ? a >>> c : a << c; a = '+' == b.charAt(d) ? a + c & 4294967295 : a ^ c } return a }\n"
						+"for (var e = TKK.split('.'), h = Number(e[0]) || 0, g = [], d = 0, f = 0; f < a.length; f++) {"  
						+"var c = a.charCodeAt(f);"  
						+"128 > c ? g[d++] = c : (2048 > c ? g[d++] = c >> 6 | 192 : (55296 == (c & 64512) && f + 1 < a.length && 56320 == (a.charCodeAt(f + 1) & 64512) ? (c = 65536 + ((c & 1023) << 10) + (a.charCodeAt(++f) & 1023), g[d++] = c >> 18 | 240, g[d++] = c >> 12 & 63 | 128) : g[d++] = c >> 12 | 224, g[d++] = c >> 6 & 63 | 128), g[d++] = c & 63 | 128)"  
						+"}"      
						+"a = h;"  
						+"for (d = 0; d < g.length; d++) a += g[d], a = b(a, '+-a^+6');"  
						+"a = b(a, '+-3^+b+-f');"  
						+"a ^= Number(e[1]) || 0;"  
						+"0 > a && (a = (a & 2147483647) + 2147483648);"  
						+"a %= 1E6;"  
						+"return a.toString() + '.' + (a ^ h)\n"
						+"}";
		
		engine.eval(script);  
		Invocable inv = (Invocable) engine;
		return (String) inv.invokeFunction("tk", val);
	}
		
	public byte[] tts(LANG lang, String query) throws Exception {
		if(query == null) {
			return null;
		}
		query = query.replaceAll("\\s+", "");
		
		byte[] bytes = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		int curPos = 0;
		for(int i = 0, count = query.length() / MAX_LENGTH; i < count; i++) {
			tts(baos, lang, query.substring(curPos, curPos + MAX_LENGTH));
			curPos += MAX_LENGTH;
		}
		if(curPos < query.length()) {
			tts(baos, lang, query.substring(curPos, query.length()));
		}
		
		bytes = baos.toByteArray();
		baos.close();
		return bytes;
	}
	
	private void tts(ByteArrayOutputStream baos, LANG lang, String query) throws Exception {
		if(query == null) {
			return;
		}
		HttpParams params = null;
		params = new HttpGetParams();
		params.put("client", "tw-ob")
			.put("ie", "UTF-8")
			.put("q", query)
			.put("tl", langMap.get(lang));
		
		URIBuilder uri = new URIBuilder("http://translate.google.com.vn/translate_tts");
		for (String key : params.getParams().keySet()) {
			String value = params.getParams().get(key);
			uri.addParameter(key, value);
		}
		URL url = uri.build().toURL();

		HttpURLConnection connection = (HttpURLConnection) url.openConnection(PROXY);
		connection.setRequestMethod("GET");
		connection.setRequestProperty("User-Agent", "Mozilla/5.0");
		connection.connect();
		
		InputStream is = connection.getInputStream();
		int length = -1;
		byte[] buf = new byte[1024 * 4];
		while ((length = is.read(buf)) != -1) {
			baos.write(buf, 0, length);
		}
		is.close();
	}
	
}