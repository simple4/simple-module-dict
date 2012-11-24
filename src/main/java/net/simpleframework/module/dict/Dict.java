package net.simpleframework.module.dict;

import net.simpleframework.common.ID;
import net.simpleframework.common.bean.INameBeanAware;
import net.simpleframework.ctx.ado.ITreeBeanManagerAware;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class Dict extends AbstractDict implements INameBeanAware {

	private String name;

	private EDictMark dictMark;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(final String name) {
		this.name = name;
	}

	public EDictMark getDictMark() {
		return dictMark == null ? EDictMark.normal : dictMark;
	}

	public void setDictMark(final EDictMark dictMark) {
		this.dictMark = dictMark;
	}

	@Override
	public void setParentId(final ID parentId) {
		((ITreeBeanManagerAware<Dict>) context().getDictManager()).assertParentId(this, parentId);
		super.setParentId(parentId);
	}

	private static final long serialVersionUID = -1642651214282707289L;
}
