package com.txdata.system.domain;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单接口Entity
 * 
 * @author mark
 * @since 2018-03-08
 *
 */
public class AjaxMenu implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String title;
	private String icon;
	private String index; // 链接
	private Integer sort; // 排序
	private String isShow; // 是否在菜单中显示（1：显示；0：不显示）
	private String permission; // 权限标识
	private String remarks;
	private String parentId;
	private String parentName;
	private String component;
	private List<AjaxMenu> children;

	public AjaxMenu() {

	}

	public AjaxMenu(Menu menu, boolean isAll) {
		this.id = menu.getId();
		this.title = menu.getName();
		this.icon = menu.getIcon();
		this.index = menu.getHref();
		this.isShow = menu.getIsShow();
		this.sort = menu.getSort();
		this.permission = menu.getPermission();
		this.component = menu.getComponent();
		if (isAll) {
			Menu parent = menu.getParent();
			if (parent != null) {
				this.parentId = menu.getParent().getId();
				this.parentName = menu.getParent().getName();
			}
			this.remarks = menu.getRemarks();
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public List<AjaxMenu> getChildren() {
		return children;
	}

	public void setChildren(List<AjaxMenu> children) {
		this.children = children;
	}

}
