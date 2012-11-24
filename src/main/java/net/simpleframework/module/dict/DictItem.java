package net.simpleframework.module.dict;

import java.util.Date;

import net.simpleframework.common.ID;
import net.simpleframework.ctx.ado.ITreeBeanManagerAware;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DictItem extends AbstractDict {

	private ID dictId;

	/**
	 * 编码
	 */
	private String codeNo;

	private EDictItemMark itemMark;

	/**
	 * 字典条目的创建日期
	 */
	private Date createDate;

	/**
	 * 字典条目的拥有人，null为全局
	 */
	private ID userId;

	public ID getDictId() {
		return dictId;
	}

	public void setDictId(final ID dictId) {
		this.dictId = dictId;
	}

	public EDictItemMark getItemMark() {
		return itemMark == null ? EDictItemMark.normal : itemMark;
	}

	public void setItemMark(final EDictItemMark itemMark) {
		this.itemMark = itemMark;
	}

	public String getCodeNo() {
		return codeNo;
	}

	public void setCodeNo(final String codeNo) {
		this.codeNo = codeNo;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(final Date createDate) {
		this.createDate = createDate;
	}

	public ID getUserId() {
		return userId;
	}

	public void setUserId(final ID userId) {
		this.userId = userId;
	}

	@Override
	public void setParentId(final ID parentId) {
		((ITreeBeanManagerAware<DictItem>) context().getDictItemManager()).assertParentId(this,
				parentId);
		super.setParentId(parentId);
	}

	private static final long serialVersionUID = 5683629062027025972L;
}
