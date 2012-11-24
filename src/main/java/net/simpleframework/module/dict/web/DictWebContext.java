package net.simpleframework.module.dict.web;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.ctx.Module;
import net.simpleframework.module.dict.impl.DictContext;
import net.simpleframework.module.dict.web.page.DictMgrPage;
import net.simpleframework.mvc.ctx.WebModuleFunction;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DictWebContext extends DictContext {

	@Override
	protected Module createModule() {
		return super.createModule().setDefaultFunction(
				new WebModuleFunction(DictMgrPage.class).setName("simple-module-dict-DictMgrPage")
						.setText($m("DictContext.0")));
	}
}
