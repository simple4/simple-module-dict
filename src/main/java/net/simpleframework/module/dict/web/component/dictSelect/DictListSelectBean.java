package net.simpleframework.module.dict.web.component.dictSelect;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.common.xml.XmlElement;
import net.simpleframework.mvc.PageDocument;
import net.simpleframework.mvc.component.ui.dictionary.DictionaryBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DictListSelectBean extends DictionaryBean {
	private String dictName;

	public DictListSelectBean(final PageDocument pageDocument, final XmlElement xmlElement) {
		super(pageDocument, xmlElement);
		setTitle($m("DictSelectBean.0"));
		setHandleClass(DictListSelectHandler.class);
	}

	public String getDictName() {
		return dictName;
	}

	public DictListSelectBean setDictName(final String dictName) {
		this.dictName = dictName;
		return this;
	}
}
