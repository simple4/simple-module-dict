package net.simpleframework.module.dict.web.page;

import static net.simpleframework.common.I18n.$m;

import java.util.Map;

import net.simpleframework.common.Convert;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.ado.query.IDataQuery;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.ctx.ModuleException;
import net.simpleframework.module.dict.Dict;
import net.simpleframework.module.dict.DictContextFactory;
import net.simpleframework.module.dict.EDictMark;
import net.simpleframework.module.dict.IDictContext;
import net.simpleframework.module.dict.IDictManager;
import net.simpleframework.mvc.IPageHandler.PageSelector;
import net.simpleframework.mvc.component.AbstractComponentBean;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ext.category.ctx.CategoryBeanAwareHandler;
import net.simpleframework.mvc.component.ui.propeditor.EInputCompType;
import net.simpleframework.mvc.component.ui.propeditor.InputComp;
import net.simpleframework.mvc.component.ui.propeditor.PropEditorBean;
import net.simpleframework.mvc.component.ui.propeditor.PropField;
import net.simpleframework.mvc.component.ui.tree.TreeBean;
import net.simpleframework.mvc.component.ui.tree.TreeNode;
import net.simpleframework.mvc.component.ui.tree.TreeNodes;
import net.simpleframework.mvc.template.t1.ext.CategoryTableLCTemplatePage;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DictCategory extends CategoryBeanAwareHandler<Dict> {

	public static final IDictContext context = DictContextFactory.get();

	@Override
	protected IDictManager beanMgr() {
		return context.getDictManager();
	}

	@Override
	protected IDataQuery<?> categoryBeans(final ComponentParameter cParameter,
			final Object categoryId) {
		final IDictManager dictMgr = beanMgr();
		return dictMgr.queryChildren(dictMgr.getBean(categoryId));
	}

	@Override
	public TreeNodes getCategoryTreenodes(final ComponentParameter cParameter,
			final TreeBean treeBean, final TreeNode parent) {
		final String imgBase = getImgBase(cParameter, DictMgrPage.class);
		if (parent == null) {
			String text = $m("DictCategory.0");
			text += "<br/><a class=\"addbtn a2\" onclick=\"$category_action(this).add();Event.stop(event);\">"
					+ $m("DictCategory.1") + "</a>";
			final TreeNode tn = new TreeNode(treeBean, parent, text);
			tn.setOpened(true);
			tn.setImage(imgBase + "dict_root.png");
			tn.setContextMenu("none");
			tn.setAcceptdrop(true);
			tn.setJsClickCallback("$Actions['" + CategoryTableLCTemplatePage.COMPONENT_TABLE
					+ "']('dictId=');");
			return TreeNodes.of(tn);
		} else {
			final Object obj = parent.getDataObject();
			if (obj instanceof Dict) {
				final Dict dict = (Dict) obj;
				final EDictMark dictMark = dict.getDictMark();
				parent.setImage(dictIcon(cParameter, dict));
				if (dictMark != EDictMark.category) {
					final int count = context.getDictItemManager().queryItems(dict).getCount();
					parent.setPostfixText("(" + count + ")");
					parent.setJsClickCallback("$Actions['" + CategoryTableLCTemplatePage.COMPONENT_TABLE
							+ "']('dictId=" + dict.getId() + "');");
				}
			}
		}
		return super.getCategoryTreenodes(cParameter, treeBean, parent);
	}

	@Override
	public TreeNodes getCategoryDictTreenodes(final ComponentParameter cParameter,
			final TreeBean treeBean, final TreeNode parent) {
		final Object dict;
		if (parent != null && (dict = parent.getDataObject()) instanceof Dict) {
			parent.setImage(dictIcon(cParameter, (Dict) dict));
		}
		return super.getCategoryTreenodes(cParameter, treeBean, parent);
	}

	private String dictIcon(final ComponentParameter cParameter, final Dict dict) {
		final String imgBase = getImgBase(cParameter, DictMgrPage.class);
		final EDictMark dictMark = dict.getDictMark();
		if (dictMark == EDictMark.category) {
			return imgBase + "dict_c.png";
		} else if (dictMark == EDictMark.normal) {
			return imgBase + "dict.gif";
		} else if (dictMark == EDictMark.builtIn) {
			return imgBase + "dict_lock.gif";
		}
		return null;
	}

	@Override
	protected void onLoaded_dataBinding(final ComponentParameter cParameter,
			final Map<String, Object> dataBinding, final PageSelector selector, final Dict dict) {
		if (dict != null) {
			dataBinding.put("dict_mark", dict.getDictMark());
			// 该字段不能编辑
			selector.disabledSelector = "#dict_mark";
		}
	}

	@Override
	protected void onSave_setProperties(final ComponentParameter cParameter, final Dict dict,
			final boolean insert) {
		final String dictMark = cParameter.getParameter("dict_mark");
		if (StringUtils.hasText(dictMark)) {
			dict.setDictMark(Convert.toEnum(EDictMark.class, dictMark));
		}
	}

	@Override
	protected void onDelete_assert(final ComponentParameter cParameter, final Dict dict) {
		super.onDelete_assert(cParameter, dict);
		if (dict.getDictMark() == EDictMark.builtIn) {
			throw ModuleException.of($m("DictCategory.4"));
		}
	}

	@Override
	protected String[] getContextMenuKeys() {
		return new String[] { "Add", "Edit", "Delete", "-", "Refresh", "-", "Move" };
	}

	@Override
	public KVMap categoryEdit_attri(final ComponentParameter cParameter) {
		return super.categoryEdit_attri(cParameter).add(window_title, $m("DictCategory.3"))
				.add(window_height, 320);
	}

	@Override
	protected AbstractComponentBean categoryEdit_createPropEditor(final ComponentParameter cParameter) {
		final PropEditorBean editor = (PropEditorBean) super
				.categoryEdit_createPropEditor(cParameter);
		editor.getFormFields().add(
				2,
				new PropField($m("DictCategory.2")).addComponents(new InputComp("dict_mark").setType(
						EInputCompType.select).setDefaultValue(EDictMark.normal, EDictMark.category)));
		return editor;
	}
}
