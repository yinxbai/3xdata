package com.txdata.common.domain;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 字典表
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-09-29 18:28:07
 */
public class AjaxDictDO {
	// 数据值
	private String value;
	// 类型
	private String type;
	// 描述
	private String description;
	// 排序（升序）
	private BigDecimal sort;
	// 父级编号
	private String parentId;

	private String label;

	private Date createDate;

	private Date updateDate;

	private String id;

	private String remarks;

	public AjaxDictDO(DictDO dict) {
		if (dict.getId() != null && !"".equals(dict.getId())) {
			this.id = dict.getId();
		}
		this.value = dict.getValue();
		this.type = dict.getType();
		this.description = dict.getDescription();
		this.sort = dict.getSort();
		this.parentId = dict.getParentId();
		this.label = dict.getName();
		this.createDate = dict.getCreateDate();
		this.updateDate = dict.getUpdateDate();
		this.remarks = dict.getRemarks();
	}

	/**
	 * 设置：数据值
	 */
	public void setValue(String value) {
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * 获取：数据值
	 */
	public String getValue() {
		return value;
	}

	/**
	 * 设置：类型
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 获取：类型
	 */
	public String getType() {
		return type;
	}

	/**
	 * 设置：描述
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 获取：描述
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 设置：排序（升序）
	 */
	public void setSort(BigDecimal sort) {
		this.sort = sort;
	}

	/**
	 * 获取：排序（升序）
	 */
	public BigDecimal getSort() {
		return sort;
	}

	/**
	 * 设置：父级编号
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * 获取：父级编号
	 */
	public String getParentId() {
		return parentId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
