package net.simpleframework.module.dict;

import net.simpleframework.common.ado.query.IDataQuery;
import net.simpleframework.ctx.ado.IBeanManagerAware;
import net.simpleframework.ctx.ado.ITreeBeanManagerAware;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IDictItemManager extends IBeanManagerAware<DictItem>,
		ITreeBeanManagerAware<DictItem> {

	/**
	 * 获取指定字典的条目
	 * 
	 * @param dict
	 * @return
	 */
	IDataQuery<DictItem> queryItems(Dict dict);

	/**
	 * 获取指定字典的第一级根条目
	 * 
	 * @param dict
	 * @return
	 */
	IDataQuery<DictItem> queryRoot(Dict dict);

	/**
	 * 根据值获取字典条目
	 * 
	 * @param dict
	 * @param codeNo
	 * @return
	 */
	DictItem getItemByCode(Dict dict, String codeNo);
}
