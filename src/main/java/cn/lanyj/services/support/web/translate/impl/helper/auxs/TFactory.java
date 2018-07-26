package cn.lanyj.services.support.web.translate.impl.helper.auxs;

import cn.lanyj.services.support.web.translate.Translator;

public interface TFactory {
	Translator get(String id);
}
