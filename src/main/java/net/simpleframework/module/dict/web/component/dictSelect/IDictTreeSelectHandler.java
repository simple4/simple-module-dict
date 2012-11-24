package net.simpleframework.module.dict.web.component.dictSelect;

import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.dictionary.IDictionaryHandle;
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
public interface IDictTreeSelectHandler extends IDictionaryHandle {

	/**
	 * 获取字典的条目列表
	 * 
	 * @param cParameter
	 * @param treeBean
	 * @param parent
	 * @return
	 */
	TreeNodes getDictItems(ComponentParameter cParameter, TreeBean treeBean, TreeNode parent);
}
