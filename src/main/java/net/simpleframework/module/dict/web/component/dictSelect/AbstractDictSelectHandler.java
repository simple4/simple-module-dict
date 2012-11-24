package net.simpleframework.module.dict.web.component.dictSelect;

import java.util.Map;

import net.simpleframework.common.StringUtils;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.module.dict.Dict;
import net.simpleframework.module.dict.DictContextFactory;
import net.simpleframework.module.dict.IDictContext;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.dictionary.AbstractDictionaryHandler;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractDictSelectHandler extends AbstractDictionaryHandler {
	public static IDictContext context = DictContextFactory.get();

	protected Dict getDict(final ComponentParameter cParameter) {
		String dictName = cParameter.getParameter("dictName");
		if (!StringUtils.hasText(dictName)) {
			dictName = (String) cParameter.getBeanProperty("dictName");
		}
		return context.getDictManager().getDictByName(dictName);
	}

	@Override
	public Object getBeanProperty(final ComponentParameter cParameter, final String beanProperty) {
		if ("title".equals(beanProperty)) {
			final Dict dict = getDict(cParameter);
			if (dict != null) {
				return dict.getText();
			}
		}
		return super.getBeanProperty(cParameter, beanProperty);
	}

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter cParameter) {
		final KVMap parameters = (KVMap) super.getFormParameters(cParameter);
		final Dict dict = getDict(cParameter);
		if (dict != null) {
			parameters.add("dictName", dict.getName());
		}
		return parameters;
	}
}
