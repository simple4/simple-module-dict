package net.simpleframework.module.dict.impl;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.ado.db.common.DbColumn;
import net.simpleframework.ado.db.common.DbTable;
import net.simpleframework.common.ClassUtils;
import net.simpleframework.common.ClassUtils.ScanClassResourcesCallback;
import net.simpleframework.ctx.Module;
import net.simpleframework.ctx.ado.AbstractADOModuleContext;
import net.simpleframework.module.dict.Dict;
import net.simpleframework.module.dict.DictItem;
import net.simpleframework.module.dict.EDictImportSource;
import net.simpleframework.module.dict.IDictContext;
import net.simpleframework.module.dict.IDictImportHandler;
import net.simpleframework.module.dict.IDictItemManager;
import net.simpleframework.module.dict.IDictManager;
import net.simpleframework.mvc.MVCContextFactory;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class DictContext extends AbstractADOModuleContext implements IDictContext {

	@Override
	public void onInit() throws Exception {
		super.onInit();
		dataServiceFactory.putEntityService(Dict.class, new DbTable("sf_dict")).putEntityService(
				DictItem.class, new DbTable("sf_dict_item").setDefaultOrder(DbColumn.order));

		// 扫描类，执行
		final String[] packageNames = MVCContextFactory.ctx().getScanPackageNames();
		if (packageNames != null) {
			for (final String packageName : packageNames) {
				ClassUtils.scanResources(packageName, new ScanClassResourcesCallback() {
					@Override
					public void doResources(final String filepath, final boolean isDirectory) {
						final IDictImportHandler hdl = newInstance(loadClass(filepath),
								IDictImportHandler.class);
						doImportHandler(hdl);
					}
				});
			}
		}
	}

	private void doImportHandler(final IDictImportHandler hdl) {
		if (hdl == null) {
			return;
		}
		if (hdl.getImportSource() == EDictImportSource.xml) {
			new XmlImporter().doImport(hdl.getImportStream(this));
		}
	}

	@Override
	protected Module createModule() {
		return new Module().setName("simple-module-dict").setText($m("DictContext.0")).setOrder(3);
	}

	@Override
	public IDictManager getDictManager() {
		return singleton(DictManager.class);
	}

	@Override
	public IDictItemManager getDictItemManager() {
		return singleton(DictItemManager.class);
	}
}
