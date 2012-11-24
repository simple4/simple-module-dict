package net.simpleframework.module.dict.impl;

import java.io.InputStream;

import net.simpleframework.common.ClassUtils;
import net.simpleframework.ctx.IModuleContext;
import net.simpleframework.module.dict.EDictImportSource;
import net.simpleframework.module.dict.IDictImportHandler;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DefaultDictImportHandler implements IDictImportHandler {

	@Override
	public InputStream getImportStream(final IModuleContext ctx) {
		return ClassUtils.getResourceAsStream(DefaultDictImportHandler.class, "dict-data.xml");
	}

	@Override
	public EDictImportSource getImportSource() {
		return EDictImportSource.xml;
	}
}
