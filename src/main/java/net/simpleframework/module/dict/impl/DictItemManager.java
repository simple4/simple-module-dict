package net.simpleframework.module.dict.impl;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.ado.db.ITableEntityService;
import net.simpleframework.common.ado.IParamsValue;
import net.simpleframework.common.ado.query.DataQueryUtils;
import net.simpleframework.common.ado.query.IDataQuery;
import net.simpleframework.ctx.ModuleException;
import net.simpleframework.module.dict.Dict;
import net.simpleframework.module.dict.DictItem;
import net.simpleframework.module.dict.EDictItemMark;
import net.simpleframework.module.dict.IDictItemManager;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DictItemManager extends AbstractDictManager<DictItem> implements IDictItemManager {

	@Override
	public IDataQuery<DictItem> queryItems(final Dict dict) {
		if (dict == null) {
			return DataQueryUtils.nullQuery();
		}
		return query("dictId=?", dict.getId());
	}

	@Override
	public IDataQuery<DictItem> queryRoot(final Dict dict) {
		if (dict == null) {
			return DataQueryUtils.nullQuery();
		}
		return query("dictId=? and parentId is null", dict.getId());
	}

	@Override
	public DictItem getItemByCode(final Dict dict, final String codeNo) {
		return query("dictId=? and codeNo=?", dict.getId(), codeNo).next();
	}

	{
		addListener(new TableEntityAdapterEx() {
			@Override
			public void beforeDelete(final ITableEntityService service, final IParamsValue paramsValue) {
				super.beforeDelete(service, paramsValue);
				for (final DictItem item : coll(paramsValue)) {
					if (item.getItemMark() != EDictItemMark.normal) {
						throw ModuleException.of($m("DictItemManager.0"));
					}
				}
			}

			@Override
			public void beforeUpdate(final ITableEntityService service, final Object[] beans) {
				super.beforeUpdate(service, beans);
				for (final Object o : beans) {
					if (((DictItem) o).getItemMark() == EDictItemMark.builtIn_r) {
						throw ModuleException.of($m("DictItemManager.1"));
					}
				}
			}
		});
	}
}
