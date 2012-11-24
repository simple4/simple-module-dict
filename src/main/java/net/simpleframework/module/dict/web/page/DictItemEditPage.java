package net.simpleframework.module.dict.web.page;

import static net.simpleframework.common.I18n.$m;

import java.util.Map;

import net.simpleframework.common.Convert;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.ado.query.DataQueryUtils;
import net.simpleframework.common.ado.query.IDataQuery;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.common.html.element.EInputType;
import net.simpleframework.common.html.element.InputElement;
import net.simpleframework.ctx.ModuleException;
import net.simpleframework.module.dict.Dict;
import net.simpleframework.module.dict.DictContextFactory;
import net.simpleframework.module.dict.DictItem;
import net.simpleframework.module.dict.EDictMark;
import net.simpleframework.module.dict.IDictContext;
import net.simpleframework.module.dict.IDictItemManager;
import net.simpleframework.mvc.AbstractMVCPage;
import net.simpleframework.mvc.IForwardCallback.IJavascriptForwardCallback;
import net.simpleframework.mvc.IPageHandler.PageSelector;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.PageRequestResponse;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.base.validation.EValidatorMethod;
import net.simpleframework.mvc.component.base.validation.Validator;
import net.simpleframework.mvc.component.ext.category.ICategoryHandler;
import net.simpleframework.mvc.component.ui.dictionary.DictionaryBean;
import net.simpleframework.mvc.component.ui.dictionary.DictionaryTreeHandler;
import net.simpleframework.mvc.component.ui.tree.TreeBean;
import net.simpleframework.mvc.component.ui.tree.TreeNode;
import net.simpleframework.mvc.component.ui.tree.TreeNodes;
import net.simpleframework.mvc.template.FormTableRowTemplatePage;
import net.simpleframework.mvc.template.struct.RowField;
import net.simpleframework.mvc.template.struct.TableRow;
import net.simpleframework.mvc.template.struct.TableRows;
import net.simpleframework.mvc.template.struct.TextButtonInput;
import net.simpleframework.mvc.template.t1.ext.CategoryTableLCTemplatePage;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DictItemEditPage extends FormTableRowTemplatePage {
	public static IDictContext context = DictContextFactory.get();

	@Override
	protected void onInit(final PageParameter pParameter) {
		super.onInit(pParameter);

		// 验证
		addFormValidationBean(pParameter).addValidators(
				new Validator(EValidatorMethod.required, "#di_text, #di_dictText, #di_codeNo"));

		// 字典选取
		addComponentBean(pParameter, "dictSelectTree", TreeBean.class).setHandleClass(
				DictSelectedTree.class);
		addComponentBean(pParameter, "dictSelect", DictionaryBean.class).setBindingId("di_dictId")
				.setBindingText("di_dictText").addTreeRef(pParameter, "dictSelectTree")
				.setTitle($m("DictItemPage.0"));

		// 上级条目选取
		addComponentBean(pParameter, "itemParentTree", TreeBean.class).setHandleClass(
				ItemParentTree.class);
		addComponentBean(pParameter, "itemParentSelect", DictionaryBean.class)
				.addTreeRef(pParameter, "itemParentTree").setBindingId("di_parentId")
				.setBindingText("di_parentText").setTitle($m("DictItemPage.1")).setDestroyOnClose(true);
	}

	private DictItem getDictItem(final PageParameter pParameter) {
		return context.getDictItemManager().getBean(pParameter.getParameter("itemId"));
	}

	@Override
	public void onLoad(final PageParameter pParameter, final Map<String, Object> dataBinding,
			final PageSelector selector) {
		final DictItem item = getDictItem(pParameter);
		if (item != null) {
			dataBinding.put("itemId", item.getId());
			dataBinding.put("di_text", item.getText());
			dataBinding.put("di_codeNo", item.getCodeNo());
			final DictItem parent = context.getDictItemManager().getBean(item.getParentId());
			if (parent != null) {
				dataBinding.put("di_parentId", parent.getId());
				dataBinding.put("di_parentText", parent.getText());
			}
			dataBinding.put("di_description", item.getDescription());
		} else {
			selector.visibleToggleSelector = ".DictItemPage .b .l";
		}

		final Dict dict = context.getDictManager().getBean(
				item != null ? item.getDictId() : pParameter.getParameter("dictId"));
		if (dict != null) {
			dataBinding.put("di_dictId", dict.getId());
			dataBinding.put("di_dictText", dict.getText());
		}
	}

	@Override
	protected boolean show_opt_next(final PageParameter pParameter) {
		return getDictItem(pParameter) == null;
	}

	@Override
	public JavascriptForward doSave(final ComponentParameter cParameter) {
		final Dict dict = context.getDictManager().getBean(cParameter.getParameter("di_dictId"));
		if (dict == null) {
			throw ModuleException.of($m("DictItemPage.2"));
		}

		DictItem item = getDictItem(cParameter);
		final boolean insert = item == null;
		final IDictItemManager dictItemMgr = context.getDictItemManager();
		if (insert) {
			item = dictItemMgr.createBean();
			item.setDictId(dict.getId());
		}
		item.setText(cParameter.getParameter("di_text"));
		item.setCodeNo(cParameter.getParameter("di_codeNo"));

		DictItem parent = null;
		final String parentText = cParameter.getParameter("di_parentText");
		if (StringUtils.hasText(parentText)) {
			final String[] arr = StringUtils.split(parentText, ";");
			if (arr.length == 1) {
				parent = dictItemMgr.getItemByCode(dict, arr[0]);
			} else if (arr.length > 1) {
			}
		}
		if (parent == null) {
			parent = dictItemMgr.getBean(cParameter.getParameter("di_parentId"));
		}
		if (parent != null) {
			item.setParentId(parent.getId());
		}

		item.setDescription(cParameter.getParameter("di_description"));
		final DictItem item2 = item;
		return doJavascriptForward(new IJavascriptForwardCallback() {
			@Override
			public void doAction(final JavascriptForward js) {
				if (insert) {
					dictItemMgr.insert(item2);
				} else {
					dictItemMgr.update(item2);
				}
				js.append("$Actions['").append(CategoryTableLCTemplatePage.COMPONENT_TABLE)
						.append("']('dictId=").append(dict.getId()).append("');");
				if (Convert.toBool(cParameter.getParameter(OPT_NEXT))) {
					js.append("$w('di_text di_codeNo di_description').each(function(e) { $(e).clear(); }); $('di_text').focus();");
				} else {
					js.append("$Actions['dictItemWindow'].close();");
				}
			}
		});
	}

	public static class DictSelectedTree extends DictionaryTreeHandler {
		@Override
		public TreeNodes getTreenodes(final ComponentParameter cParameter, final TreeNode parent) {
			final ComponentParameter nComponentParameter = ComponentParameter.get(cParameter,
					AbstractMVCPage.get(DictMgrPage.class).getCategoryBean());
			final ICategoryHandler cHandle = (ICategoryHandler) nComponentParameter
					.getComponentHandler();
			return cHandle.getCategoryDictTreenodes(nComponentParameter,
					(TreeBean) cParameter.componentBean, parent);
		}

		@Override
		public Map<String, Object> getTreenodeAttributes(final ComponentParameter cParameter,
				final TreeNode treeNode) {
			final KVMap kv = (KVMap) super.getTreenodeAttributes(cParameter, treeNode);
			Object o;
			if (treeNode != null && (o = treeNode.getDataObject()) instanceof Dict
					&& ((Dict) o).getDictMark() == EDictMark.category) {
				disabled_selected(kv);
			}
			return kv;
		}
	}

	public static class ItemParentTree extends DictionaryTreeHandler {
		@Override
		public Map<String, Object> getFormParameters(final ComponentParameter cParameter) {
			final Map<String, Object> parameters = super.getFormParameters(cParameter);
			final Dict dict = getDict(cParameter);
			if (dict != null) {
				parameters.put("dictId", dict.getId());
			}
			return parameters;
		}

		private Dict getDict(final PageRequestResponse rRequest) {
			return context.getDictManager().getBean(rRequest.getParameter("dictId"));
		}

		@SuppressWarnings("unchecked")
		@Override
		public TreeNodes getTreenodes(final ComponentParameter cParameter, final TreeNode parent) {
			final IDictItemManager dictItemMgr = context.getDictItemManager();
			IDataQuery<DictItem> dq;
			final Dict dict = getDict(cParameter);
			if (dict == null) {
				dq = DataQueryUtils.nullQuery();
			} else {
				DictItem pItem = null;
				if (parent != null) {
					pItem = (DictItem) parent.getDataObject();
				}
				dq = (IDataQuery<DictItem>) (pItem == null ? dictItemMgr.queryRoot(dict) : dictItemMgr
						.queryChildren(pItem));
			}
			final TreeNodes nodes = TreeNodes.of();
			DictItem item;
			while ((item = dq.next()) != null) {
				final TreeNode node = new TreeNode((TreeBean) cParameter.componentBean, parent, item);
				nodes.add(node);
			}
			return nodes;
		}
	}

	@Override
	public int labelWidth(final PageParameter pParameter) {
		return 70;
	}

	private final InputElement itemId = new InputElement("itemId", EInputType.hidden);
	private final InputElement di_text = new InputElement("di_text");

	final TextButtonInput di_dictText = new TextButtonInput("di_dictText").setHiddenField(
			"di_dictId").setOnclick("$Actions['dictSelect']();");

	private final InputElement di_codeNo = new InputElement("di_codeNo");

	final TextButtonInput di_parentText = new TextButtonInput("di_parentText").setHiddenField(
			"di_parentId").setOnclick("$Actions['itemParentSelect']('dictId=' + $F('di_dictId'))");

	private final InputElement di_description = new InputElement("di_description",
			EInputType.textarea).setRows(6);

	@Override
	protected TableRows tableRows(final PageParameter pParameter) {
		final TableRow r1 = new TableRow(new RowField($m("DictMgrPage.1"), itemId, di_text),
				new RowField($m("DictItemPage.0"), di_dictText));
		final TableRow r2 = new TableRow(new RowField($m("DictMgrPage.2"), di_codeNo), new RowField(
				$m("DictItemPage.1"), di_parentText));
		final TableRow r3 = new TableRow(new RowField($m("Description"), di_description));
		return TableRows.of(r1, r2, r3);
	}
}
