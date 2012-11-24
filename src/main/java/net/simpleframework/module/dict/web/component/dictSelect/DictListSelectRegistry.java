package net.simpleframework.module.dict.web.component.dictSelect;

import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ComponentBean;
import net.simpleframework.mvc.component.ComponentName;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentRender;
import net.simpleframework.mvc.component.ui.dictionary.DictionaryBean;
import net.simpleframework.mvc.component.ui.dictionary.DictionaryRegistry;
import net.simpleframework.mvc.component.ui.listbox.AbstractListboxHandler;
import net.simpleframework.mvc.component.ui.listbox.ListItems;
import net.simpleframework.mvc.component.ui.listbox.ListboxBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
@ComponentBean(DictListSelectBean.class)
@ComponentName(DictListSelectRegistry.dictListSelect)
@ComponentRender(DictSelectRender.class)
public class DictListSelectRegistry extends DictionaryRegistry {
	public static final String dictListSelect = "dictListSelect";

	@Override
	public DictionaryBean createComponentBean(final PageParameter pParameter, final Object data) {
		final DictListSelectBean dictSelect = (DictListSelectBean) super.createComponentBean(
				pParameter, data);
		final ComponentParameter nComponentParameter = ComponentParameter.get(pParameter, dictSelect);

		final String dictSelectName = (String) nComponentParameter.getBeanProperty("name");
		final ListboxBean listbox = (ListboxBean) pParameter.addComponentBean(
				dictSelectName + "_list", ListboxBean.class).setHandleClass(DictList.class);
		dictSelect.addListboxRef(nComponentParameter, listbox.getName());
		listbox.setAttr("$dictSelect", dictSelect);

		return dictSelect;
	}

	public static class DictList extends AbstractListboxHandler {

		@Override
		public ListItems getListItems(final ComponentParameter cParameter) {
			final ComponentParameter nComponentParameter = ComponentParameter.getByAttri(cParameter,
					"$dictSelect");
			return ((IDictListSelectHandler) nComponentParameter.getComponentHandler()).getDictItems(
					nComponentParameter, (ListboxBean) cParameter.componentBean);
		}
	}
}
