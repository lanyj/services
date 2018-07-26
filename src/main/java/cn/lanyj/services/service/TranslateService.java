package cn.lanyj.services.service;

import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.lanyj.services.error.ServiceProcessException;
import cn.lanyj.services.support.web.translate.impl.GoogleTranslator;
import cn.lanyj.services.support.web.translate.impl.helper.auxs.DupIdException;
import cn.lanyj.services.support.web.translate.impl.helper.auxs.LANG;
import cn.lanyj.services.support.web.translate.impl.helper.auxs.TFactory;
import cn.lanyj.services.support.web.translate.impl.helper.auxs.TranslatorFactory;

@Service
public class TranslateService {
	private Logger log = LoggerFactory.getLogger(TranslateService.class);
	
	GoogleTranslator translator;
	{
		log.debug("construct google translator");
		try {
			TFactory factory = new TranslatorFactory();
			translator = (GoogleTranslator) factory.get("google");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | DupIdException
				| URISyntaxException e) {
			log.error("construct google translator failed", e);
		}
	}

	public String trans(String from, String to, String content) {
		try {
			return translator.trans(LANG.valueOf(from), LANG.valueOf(to), content);
		} catch (Exception e) {
			log.error("translator failed from = " + from + ", to = " + to, e);
			throw new ServiceProcessException("Translate error, from = " + from + ", to = " + to);
		}
	}
	
	public byte[] tts(String lang, String content) {
		try {
			return translator.tts(LANG.valueOf(lang), content);
		} catch (Exception e) {
			log.error("tts failed lang = " + lang, e);
			throw new ServiceProcessException("TTS error, lang = " + lang);
		}
	}
	
}
