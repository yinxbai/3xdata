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
public class AjaxArea implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String name;
	private String code;
	private String type; // 链接
	private String parentId;
	private String parentName;
	private String parentIds;
	private String remarks;
	private List<AjaxArea> subs;

	public AjaxArea() {

	}

	public AjaxArea(Area area, boolean isAll) {
		this.id = area.getId();
		this.name = area.getName();
		this.type = area.getType();
		this.code = area.getCode();
		if (isAll) {
			Area parent = area.getParent();
			if (parent != null) {
				this.parentId = area.getParent().getId();
				this.parentName = area.getParent().getName();
				this.parentIds = area.getParentIds();
			}
			this.remarks = area.getRemarks();
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	public List<AjaxArea> getSubs() {
		return subs;
	}

	public void setSubs(List<AjaxArea> subs) {
		this.subs = subs;
	}

}
