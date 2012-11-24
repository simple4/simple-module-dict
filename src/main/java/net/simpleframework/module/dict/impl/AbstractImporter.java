package net.simpleframework.module.dict.impl;

import net.simpleframework.common.ObjectEx;
import net.simpleframework.module.dict.DictContextFactory;
import net.simpleframework.module.dict.IDictContext;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractImporter extends ObjectEx {

	public static IDictContext context = DictContextFactory.get();

}
