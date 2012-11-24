package net.simpleframework.module.dict;

import net.simpleframework.ctx.ado.IBeanManagerAware;
import net.simpleframework.ctx.ado.ITreeBeanManagerAware;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IDictManager extends IBeanManagerAware<Dict>, ITreeBeanManagerAware<Dict> {

	/**
	 * 根据名字获取字典
	 * 
	 * @param name
	 * @return
	 */
	Dict getDictByName(String name);
}
