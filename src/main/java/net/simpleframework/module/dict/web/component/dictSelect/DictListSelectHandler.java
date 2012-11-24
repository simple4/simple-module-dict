package net.simpleframework.module.dict.web.component.dictSelect;

import net.simpleframework.common.ado.query.IDataQuery;
import net.simpleframework.module.dict.Dict;
import net.simpleframework.module.dict.DictItem;
import net.simpleframework.module.dict.IDictItemManager;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.listbox.ListItem;
import net.simpleframework.mvc.component.ui.listbox.ListItems;
import net.simpleframework.mvc.component.ui.listbox.ListboxBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DictListSelectHandler extends AbstractDictSelectHandler implements
		IDictListSelectHandler {

	protected ListItem createItem(final ListboxBean listbox, final DictItem dictItem) {
		final ListItem item = new ListItem(listbox, dictItem.getText());
		item.setId(dictItem.getCodeNo());
		return item;
	}

	@Override
	public ListItems getDictItems(final ComponentParameter cParameter, final ListboxBean listbox) {
		final Dict dict = getDict(cParameter);
		if (dict == null) {
			return null;
		}
		final IDictItemManager dictItemMgr = context.getDictItemManager();
		final ListItems items = ListItems.of();
		final IDataQuery<DictItem> dq = dictItemMgr.queryItems(dict);
		DictItem dictItem;
		while ((dictItem = dq.next()) != null) {
			final ListItem item = createItem(listbox, dictItem);
			if (item != null) {
				items.add(item);
			}
		}
		return items;
	}
}
