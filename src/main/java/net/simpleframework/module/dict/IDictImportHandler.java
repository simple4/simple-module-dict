package net.simpleframework.module.dict;

import java.io.InputStream;

import net.simpleframework.ctx.IModuleContext;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IDictImportHandler {

	/**
	 * 
	 * @param ctx
	 * @return
	 */
	InputStream getImportStream(IModuleContext ctx);

	/**
	 * 获取导入源
	 * 
	 * @return
	 */
	EDictImportSource getImportSource();
}
