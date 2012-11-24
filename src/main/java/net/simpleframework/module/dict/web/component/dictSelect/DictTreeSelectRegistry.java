package net.simpleframework.module.dict.web.component.dictSelect;

import java.util.Map;

import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ComponentBean;
import net.simpleframework.mvc.component.ComponentName;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentRender;
import net.simpleframework.mvc.component.ui.dictionary.DictionaryBean;
import net.simpleframework.mvc.component.ui.dictionary.DictionaryRegistry;
import net.simpleframework.mvc.component.ui.tree.AbstractTreeHandler;
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
@ComponentBean(DictTreeSelectBean.class)
@ComponentName(DictTreeSelectRegistry.dictTreeSelect)
@ComponentRender(DictSelectRender.class)
public class DictTreeSelectRegistry extends DictionaryRegistry {
	public static final String dictTreeSelect = "dictTreeSelect";

	@Override
	public DictionaryBean createComponentBean(final PageParameter pParameter, final Object data) {
		final DictTreeSelectBean dictSelect = (DictTreeSelectBean) super.createComponentBean(
				pParameter, data);

		final ComponentParameter nComponentParameter = ComponentParameter.get(pParameter, dictSelect);

		final String dictSelectName = (String) nComponentParameter.getBeanProperty("name");
		final TreeBean treeBean = (TreeBean) pParameter.addComponentBean(dictSelectName + "_tree",
				TreeBean.class).setHandleClass(DictTree.class);
		dictSelect.addTreeRef(pParameter, treeBean.getName());
		treeBean.setAttr("$dictSelect", dictSelect);

		return dictSelect;
	}

	public static class DictTree extends AbstractTreeHandler {

		@Override
		public Map<String, Object> getFormParameters(final ComponentParameter cParameter) {
			final ComponentParameter nComponentParameter = ComponentParameter.getByAttri(cParameter,
					"$dictSelect");
			return ((IDictTreeSelectHandler) nComponentParameter.getComponentHandler())
					.getFormParameters(nComponentParameter);
		}

		@Override
		public TreeNodes getTreenodes(final ComponentParameter cParameter, final TreeNode parent) {
			final ComponentParameter nComponentParameter = ComponentParameter.getByAttri(cParameter,
					"$dictSelect");
			return ((IDictTreeSelectHandler) nComponentParameter.getComponentHandler()).getDictItems(
					nComponentParameter, (TreeBean) cParameter.componentBean, parent);
		}
	}
}
