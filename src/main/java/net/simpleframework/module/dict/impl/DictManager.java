package net.simpleframework.module.dict.impl;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.ado.db.ITableEntityService;
import net.simpleframework.common.ado.IParamsValue;
import net.simpleframework.ctx.ModuleException;
import net.simpleframework.module.dict.Dict;
import net.simpleframework.module.dict.EDictMark;
import net.simpleframework.module.dict.IDictManager;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DictManager extends AbstractDictManager<Dict> implements IDictManager {
	@Override
	public Dict getDictByName(final String name) {
		return getBean("name=?", name);
	}

	{
		addListener(new TableEntityAdapterEx() {
			@Override
			public void beforeDelete(final ITableEntityService service, final IParamsValue paramsValue) {
				super.beforeDelete(service, paramsValue);
				for (final Dict dict : coll(paramsValue)) {
					// 存在下级字典
					if (queryChildren(dict).getCount() > 0) {
						throw ModuleException.of($m("DictManager.0"));
					}
				}
			}

			@Override
			public void beforeUpdate(final ITableEntityService service, final Object[] beans) {
				super.beforeUpdate(service, beans);
				for (final Object o : beans) {
					if (((Dict) o).getDictMark() == EDictMark.builtIn) {
						throw ModuleException.of($m("DictManager.1"));
					}
				}
			}
		});
	}
}
