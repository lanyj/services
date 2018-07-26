package cn.lanyj.services.support.web.translate.impl.helper.auxs;

import java.net.URISyntaxException;

import cn.lanyj.services.support.web.translate.Translator;

final public class TranslatorFactory extends AbstractTranslatorFactory{

	public TranslatorFactory() throws ClassNotFoundException, InstantiationException, IllegalAccessException, DupIdException, URISyntaxException {
		super();
	}

	@Override
	public Translator get(String id) {
		return translatorMap.get(id);
	}

}
