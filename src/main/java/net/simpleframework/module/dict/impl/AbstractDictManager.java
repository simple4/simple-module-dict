package net.simpleframework.module.dict.impl;

import java.io.Serializable;

import net.simpleframework.ctx.ado.AbstractBeanDbManager;
import net.simpleframework.module.dict.DictContextFactory;
import net.simpleframework.module.dict.IDictContext;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractDictManager<T extends Serializable> extends
		AbstractBeanDbManager<T, T> {
	@Override
	public IDictContext getModuleContext() {
		return DictContextFactory.get();
	}

	protected DictItemManager dictItemMgr() {
		return (DictItemManager) getModuleContext().getDictItemManager();
	}
}
