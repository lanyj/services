package cn.lanyj.services.support.web.translate;

import cn.lanyj.services.support.web.translate.impl.helper.auxs.LANG;

public interface Translator {
	public String trans(LANG from, LANG targ, String query) throws Exception;
}
