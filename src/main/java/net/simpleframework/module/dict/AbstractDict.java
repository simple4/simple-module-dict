package net.simpleframework.module.dict;

import net.simpleframework.common.ID;
import net.simpleframework.common.bean.AbstractTextDescriptionBean;
import net.simpleframework.common.bean.IOrderBeanAware;
import net.simpleframework.common.bean.ITreeBeanAware;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public abstract class AbstractDict extends AbstractTextDescriptionBean implements ITreeBeanAware,
		IOrderBeanAware {

	private ID parentId;

	private long oorder;

	@Override
	public ID getParentId() {
		return parentId;
	}

	@Override
	public void setParentId(final ID parentId) {
		this.parentId = parentId;
	}

	@Override
	public long getOorder() {
		return oorder;
	}

	@Override
	public void setOorder(final long oorder) {
		this.oorder = oorder;
	}

	protected IDictContext context() {
		return DictContextFactory.get();
	}
}
