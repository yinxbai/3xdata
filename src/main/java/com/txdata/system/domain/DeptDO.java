package com.txdata.system.domain;

import com.txdata.common.domain.DataEntity;

/**
 * 部门管理
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-09-27 14:28:36
 */
public class DeptDO extends DataEntity<DeptDO> {
	private static final long serialVersionUID = 1L;

 // private Long deptId;
	// 上级部门ID，一级部门为0
	private String parentId;
	// 部门名称
	private String name;
	// 排序
	private Integer orderNum;

	/**
	 * 设置：上级部门ID，一级部门为0
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * 获取：上级部门ID，一级部门为0
	 */
	public String getParentId() {
		return parentId;
	}

	/**
	 * 设置：部门名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取：部门名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置：排序
	 */
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	/**
	 * 获取：排序
	 */
	public Integer getOrderNum() {
		return orderNum;
	}

	@Override
	public String toString() {
		return "DeptDO{" + "id=" + id + ", parentId=" + parentId + ", name='" + name + '\'' + ", orderNum=" + orderNum
				+ ", delFlag=" + delFlag + '}';
	}
}
