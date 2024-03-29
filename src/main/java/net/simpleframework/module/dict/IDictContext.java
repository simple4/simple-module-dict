package net.simpleframework.module.dict;

import net.simpleframework.ctx.ado.IADOModuleContext;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IDictContext extends IADOModuleContext {

	/**
	 * 获取字典管理器
	 * 
	 * @return
	 */
	IDictManager getDictManager();

	/**
	 * 获取字典条目管理器
	 * 
	 * @return
	 */
	IDictItemManager getDictItemManager();
}
