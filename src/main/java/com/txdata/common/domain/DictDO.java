package com.txdata.common.domain;

import java.math.BigDecimal;

/**
 * 字典表
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-09-29 18:28:07
 */
public class DictDO extends DataEntity<DictDO> {
	private static final long serialVersionUID = 1L;

	// 标签名
	private String name;

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

	/**
	 * 设置：标签名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取：标签名
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置：数据值
	 */
	public void setValue(String value) {
		this.value = value;
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

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return "DictDO{" + "id=" + id + ", name='" + name + '\'' + ", value='" + value + '\'' + ", type='" + type + '\''
				+ ", description='" + description + '\'' + ", sort=" + sort + ", parentId=" + parentId + ", createBy="
				+ createBy + ", createDate=" + createDate + ", updateBy=" + updateBy + ", updateDate=" + updateDate
				+ ", remarks='" + remarks + '\'' + ", delFlag='" + delFlag + '\'' + '}';
	}
}
