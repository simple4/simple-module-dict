package net.simpleframework.module.dict.web.page;

import java.util.Map;

import net.simpleframework.common.ado.query.DataQueryUtils;
import net.simpleframework.common.ado.query.IDataQuery;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.module.dict.Dict;
import net.simpleframework.module.dict.DictContextFactory;
import net.simpleframework.module.dict.DictItem;
import net.simpleframework.module.dict.IDictContext;
import net.simpleframework.module.dict.IDictItemManager;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.PageRequestResponse;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ext.category.CategoryBean;
import net.simpleframework.mvc.component.ext.category.ctx.CategoryBeanAwareHandler;
import net.simpleframework.mvc.component.ui.tree.TreeBean;
import net.simpleframework.mvc.component.ui.tree.TreeNode;
import net.simpleframework.mvc.component.ui.tree.TreeNodes;
import net.simpleframework.mvc.template.t1.ext.CategoryWindowPage;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DictItemCategoryPage extends CategoryWindowPage {
	public static final IDictContext context = DictContextFactory.get();

	private static Dict getDict(final PageRequestResponse rRequest) {
		Dict dict = (Dict) rRequest.getRequestAttr("dictId");
		if (dict == null) {
			dict = context.getDictManager().getBean(rRequest.getParameter("dictId"));
		}
		return dict;
	}

	@Override
	protected void onInit(final PageParameter pParameter) {
		super.onInit(pParameter);

		final CategoryBean category = addCategoryBean(pParameter, DictItemCategory.class);
		if (context.getDictItemManager().queryItems(getDict(pParameter)).getCount() > 100) {
			category.setDynamicTree(true);
		}
	}

	public static class DictItemCategory extends CategoryBeanAwareHandler<DictItem> {

		@Override
		protected IDictItemManager beanMgr() {
			return context.getDictItemManager();
		}

		@Override
		public Map<String, Object> getFormParameters(final ComponentParameter cParameter) {
			final KVMap parameters = (KVMap) super.getFormParameters(cParameter);
			final Dict dict = context.getDictManager().getBean(cParameter.getParameter("dictId"));
			if (dict != null) {
				parameters.add("dictId", dict.getId());
				cParameter.setRequestAttr("dictId", dict);
			}
			return parameters;
		}

		@Override
		protected IDataQuery<?> categoryBeans(final ComponentParameter cParameter,
				final Object categoryId) {
			final Dict dict = getDict(cParameter);
			if (dict == null) {
				return DataQueryUtils.nullQuery();
			}
			final IDictItemManager dictItemMgr = beanMgr();
			final DictItem parent = dictItemMgr.getBean(categoryId);
			return parent == null ? dictItemMgr.queryRoot(dict) : dictItemMgr.queryChildren(parent);
		}

		@Override
		protected String[] getContextMenuKeys() {
			return new String[] { "Refresh", "-", "Move" };
		}

		@Override
		public TreeNodes getCategoryTreenodes(final ComponentParameter cParameter,
				final TreeBean treeBean, final TreeNode parent) {
			if (parent == null) {
				final Dict dict = getDict(cParameter);
				if (dict != null) {
					final TreeNodes nodes = TreeNodes.of();
					final TreeNode node = new TreeNode(treeBean, parent, null);
					node.setText(dict.getText());
					node.setAcceptdrop(true);
					nodes.add(node);
					return nodes;
				}
			}
			return super.getCategoryTreenodes(cParameter, treeBean, parent);
		}
	}
}
