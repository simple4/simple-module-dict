package net.simpleframework.module.dict.web.component.dictSelect;

import net.simpleframework.common.ado.query.IDataQuery;
import net.simpleframework.module.dict.Dict;
import net.simpleframework.module.dict.DictItem;
import net.simpleframework.module.dict.IDictItemManager;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.tree.TreeBean;
import net.simpleframework.mvc.component.ui.tree.TreeNode;
import net.simpleframework.mvc.component.ui.tree.TreeNodes;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DictTreeSelectHandler extends AbstractDictSelectHandler implements
		IDictTreeSelectHandler {

	protected TreeNode createItem(final TreeBean treeBean, final TreeNode parent,
			final DictItem dictItem) {
		final TreeNode node = new TreeNode(treeBean, parent, dictItem);
		node.setId(dictItem.getCodeNo());
		return node;
	}

	@SuppressWarnings("unchecked")
	@Override
	public TreeNodes getDictItems(final ComponentParameter cParameter, final TreeBean treeBean,
			final TreeNode parent) {
		final Dict dict = getDict(cParameter);
		if (dict == null) {
			return null;
		}
		final IDictItemManager dictItemMgr = context.getDictItemManager();
		final TreeNodes nodes = TreeNodes.of();
		final IDataQuery<DictItem> dq;
		if (parent == null) {
			dq = dictItemMgr.queryRoot(dict);
		} else {
			dq = (IDataQuery<DictItem>) dictItemMgr.queryChildren((DictItem) parent.getDataObject());
		}
		DictItem dictItem;
		while ((dictItem = dq.next()) != null) {
			final TreeNode node = createItem(treeBean, parent, dictItem);
			if (node != null) {
				nodes.add(node);
			}
		}
		return nodes;
	}
}
