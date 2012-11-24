package net.simpleframework.module.dict.web.component.dictSelect;

import net.simpleframework.common.xml.XmlElement;
import net.simpleframework.mvc.PageDocument;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DictTreeSelectBean extends DictListSelectBean {

	public DictTreeSelectBean(final PageDocument pageDocument, final XmlElement xmlElement) {
		super(pageDocument, xmlElement);
		setHandleClass(DictTreeSelectHandler.class);
	}
}
