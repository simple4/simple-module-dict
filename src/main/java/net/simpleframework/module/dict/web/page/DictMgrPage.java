package net.simpleframework.module.dict.web.page;

import static net.simpleframework.common.I18n.$m;

import java.util.Map;

import net.simpleframework.ado.db.common.DbColumn;
import net.simpleframework.common.Convert;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.ado.query.IDataQuery;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.common.html.element.ETextAlign;
import net.simpleframework.common.html.element.ElementList;
import net.simpleframework.common.html.element.LinkElement;
import net.simpleframework.ctx.permission.IPermissionHandler;
import net.simpleframework.module.dict.Dict;
import net.simpleframework.module.dict.DictContextFactory;
import net.simpleframework.module.dict.DictItem;
import net.simpleframework.module.dict.EDictMark;
import net.simpleframework.module.dict.IDictContext;
import net.simpleframework.module.dict.IDictItemManager;
import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.chosen.ChosenBean;
import net.simpleframework.mvc.component.ui.menu.MenuBean;
import net.simpleframework.mvc.component.ui.menu.MenuItem;
import net.simpleframework.mvc.component.ui.menu.MenuItems;
import net.simpleframework.mvc.component.ui.pager.AbstractTablePagerSchema;
import net.simpleframework.mvc.component.ui.pager.TablePagerColumn;
import net.simpleframework.mvc.component.ui.pager.TablePagerUtils;
import net.simpleframework.mvc.component.ui.window.WindowBean;
import net.simpleframework.mvc.template.struct.LinkButton;
import net.simpleframework.mvc.template.struct.LinkButtons;
import net.simpleframework.mvc.template.struct.NavigationButtons;
import net.simpleframework.mvc.template.t1.ext.CategoryTableLCTemplatePage;
import net.simpleframework.mvc.template.t1.ext.CategoryTablePagerHandler;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DictMgrPage extends CategoryTableLCTemplatePage {
	public static IDictContext context = DictContextFactory.get();

	@Override
	protected void onInit(final PageParameter pParameter) {
		super.onInit(pParameter);

		addImportCSS(getCssResourceHomePath(pParameter) + "/dict_mgr.css");

		addCategoryBean(pParameter, DictCategory.class);

		addTablePagerBean(pParameter, DictItemList.class)
				.addColumn(
						new TablePagerColumn("text", $m("DictMgrPage.1"), 150)
								.setTextAlign(ETextAlign.left))
				.addColumn(new TablePagerColumn("codeNo", $m("DictMgrPage.2"), 150))
				.addColumn(new TablePagerColumn("itemMark", $m("DictMgrPage.3"), 120))
				.addColumn(new TablePagerColumn("parentId", $m("DictMgrPage.12"), 120))
				.addColumn(
						new TablePagerColumn("description", $m("Description")).setSort(false)
								.setTextAlign(ETextAlign.left)).addColumn(TablePagerColumn.ACTION);

		// 字典条目
		addAjaxRequest(pParameter, "dictItemPage", DictItemEditPage.class);
		addComponentBean(pParameter, "dictItemWindow", WindowBean.class)
				.setTitle($m("DictMgrPage.4")).setContentRef("dictItemPage").setHeight(280)
				.setWidth(500);

		// 树视图
		addAjaxRequest(pParameter, "dictItemCategoryPage", DictItemCategoryPage.class);
		addComponentBean(pParameter, "dictItemCategoryWindow", WindowBean.class)
				.setContentRef("dictItemCategoryPage").setTitle($m("DictMgrPage.7"))
				.setContentStyle("padding: 0;").setHeight(450).setWidth(380);

		// 移动
		addAjaxRequest(pParameter, "dictItemMove").setHandleMethod("doItemMove");

		// 删除
		addAjaxRequest(pParameter, "dictItemDelete").setConfirmMessage($m("Confirm.Delete"))
				.setHandleMethod("doItemDelete");

		// select增强
		addComponentBean(pParameter, "dictChosen", ChosenBean.class).setSelector(".t_bar select");
	}

	@Override
	public String getRole(final PageParameter pParameter) {
		return IPermissionHandler.sj_all_account;
	}

	public IForward doItemMove(final ComponentParameter cParameter) {
		final IDictItemManager dictItemMgr = context.getDictItemManager();
		final DictItem item = dictItemMgr.getBean(cParameter
				.getParameter(TablePagerUtils.PARAM_MOVE_ROWID));
		final DictItem item2 = dictItemMgr.getBean(cParameter
				.getParameter(TablePagerUtils.PARAM_MOVE_ROWID2));
		if (item != null && item2 != null) {
			dictItemMgr.exchange(item, item2, new DbColumn("oorder"),
					Convert.toBool(cParameter.getParameter(TablePagerUtils.PARAM_MOVE_UP)));
		}
		return new JavascriptForward("$Actions['").append(COMPONENT_TABLE).append("']();");
	}

	public IForward doItemDelete(final ComponentParameter cParameter) {
		final Object[] ids = StringUtils.split(cParameter.getParameter("id"));
		context.getDictItemManager().delete(ids);
		return new JavascriptForward("$Actions['").append(COMPONENT_TABLE).append("']();");
	}

	@Override
	public NavigationButtons getNavigationBar(final PageParameter pParameter) {
		return super.getNavigationBar(pParameter).append(new LinkElement($m("DictMgrPage.0")));
	}

	@Override
	protected LinkButtons getToolbarButtons(final PageParameter pParameter) {
		return LinkButtons
				.of(new LinkButton($m("DictMgrPage.5"))
						.setOnclick("$Actions['dictItemWindow']('dictId=' + $F('dictId'));"))
				.append(LinkButton.SEP)
				.append(act_btn("dictItemDelete", $m("DictMgrPage.6")))
				.append(LinkButton.SEP)
				.append(
						new LinkButton($m("DictMgrPage.7"))
								.setOnclick("$Actions['dictItemCategoryWindow']('dictId=' + $F('dictId'));"));
	}

	@Override
	public String toLeftToolbarHTML(final PageParameter pParameter) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<select style='width: 100px;'>");
		sb.append("  <option>#(DictMgrPage.10)</option>");
		sb.append("  <option>#(DictMgrPage.11)</option>");
		sb.append("</select>");
		return sb.toString();
	}

	public static class DictItemList extends CategoryTablePagerHandler {

		@Override
		protected ElementList navigationTitle(final ComponentParameter cParameter) {
			return doNavigationTitle(cParameter, (Dict) cParameter.getRequestAttr("select_dict"),
					new NavigationTitleCallback<Dict>() {
						@Override
						protected String rootText() {
							return $m("DictItemList.0");
						}

						@Override
						protected String categoryIdKey() {
							return "dictId";
						}

						@Override
						protected boolean isLink(final Dict t) {
							return t.getDictMark() != EDictMark.category;
						}

						@Override
						protected Dict get(final Object id) {
							return context.getDictManager().getBean(id);
						}
					});
		}

		@Override
		public Map<String, Object> getFormParameters(final ComponentParameter cParameter) {
			return ((KVMap) super.getFormParameters(cParameter)).add("dictId",
					cParameter.getParameter("dictId"));
		}

		@Override
		public IDataQuery<?> createDataObjectQuery(final ComponentParameter cParameter) {
			final Dict dict = context.getDictManager().getBean(cParameter.getParameter("dictId"));
			if (dict != null) {
				cParameter.setRequestAttr("select_dict", dict);
			}
			return context.getDictItemManager().queryItems(dict);
		}

		@Override
		public MenuItems getContextMenu(final ComponentParameter cParameter, final MenuBean menuBean,
				final MenuItem menuItem) {
			if (menuItem == null) {
				return MenuItems
						.of()
						.append(
								MenuItem
										.of(menuBean, $m("Edit"), MenuItem.ICON_EDIT,
												"$Actions['dictItemWindow']('itemId=' + $pager_action(item).rowId());"))
						.append(
								MenuItem
										.of(menuBean, $m("Delete"), MenuItem.ICON_DELETE,
												"$Actions['dictItemDelete']('itemId=' + $pager_action(item).rowId());"))
						.append(MenuItem.sep(menuBean))
						.append(
								MenuItem
										.of(menuBean, $m("Menu.move"))
										.addChild(
												MenuItem.of(menuBean, $m("Menu.up"), MenuItem.ICON_UP,
														"$pager_action(item).move(true, 'dictItemMove');"))
										.addChild(
												MenuItem.of(menuBean, $m("Menu.up2"), MenuItem.ICON_UP2,
														"$pager_action(item).move2(true, 'dictItemMove');"))
										.addChild(
												MenuItem.of(menuBean, $m("Menu.down"), MenuItem.ICON_DOWN,
														"$pager_action(item).move(false, 'dictItemMove');"))
										.addChild(
												MenuItem.of(menuBean, $m("Menu.down2"), MenuItem.ICON_DOWN2,
														"$pager_action(item).move2(false, 'dictItemMove');")));
			}
			return null;
		}

		@Override
		public AbstractTablePagerSchema createTablePagerSchema() {
			return new DefaultDbTablePagerSchema() {
				@Override
				public Map<String, Object> getRowData(final ComponentParameter cParameter,
						final Object dataObject) {
					final DictItem item = (DictItem) dataObject;
					final KVMap kv = new KVMap();
					kv.put("text", new LinkElement(item.getText())
							.setOnclick("$Actions['dictItemWindow']('itemId=" + item.getId() + "');"));
					kv.put("codeNo", item.getCodeNo());
					kv.put("itemMark", item.getItemMark());
					final DictItem parent = context.getDictItemManager().getBean(item.getParentId());
					if (parent != null) {
						kv.put("parentId", parent.getText());
					}
					kv.put("description", item.getDescription());
					kv.put(TablePagerColumn.ACTION.getColumnName(), ACTIONc);
					return kv;
				}
			};
		}
	}
}
