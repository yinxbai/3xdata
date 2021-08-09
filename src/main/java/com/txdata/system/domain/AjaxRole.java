package com.txdata.system.domain;

import java.io.Serializable;

/**
 * 角色接口Entity
 * 
 * @author mark
 * @version 2018-03-08
 */
public class AjaxRole implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	private String officeId;
	private String officeName;
	private String name; // 角色名称
	private String enname; // 英文名称
	private String roleType;// 权限类型
	private String dataScope;// 数据范围
	private String menuIds;
	private String officeIds;
	private String sysData; // 是否是系统数据
	private String useable; // 是否是可用
	private String remarks;
	private String oldName; // 原角色名称
	private String oldEnname; // 原英文名称

	public AjaxRole() {

	}

	public AjaxRole(Role role) {
		this.id = role.getId();
		this.name = role.getName();
		this.enname = role.getEnname();
		this.roleType = role.getRoleType();
		this.dataScope = role.getDataScope();
		this.menuIds = role.getMenuIds();
		this.officeIds = role.getOfficeIds();
		this.sysData = role.getSysData();
		this.useable = role.getUseable();
		this.remarks = role.getRemarks();
		Office office = role.getOffice();
		if (office != null) {
			this.officeId = office.getId();
			this.officeName = office.getName();
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEnname() {
		return enname;
	}

	public void setEnname(String enname) {
		this.enname = enname;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	public String getDataScope() {
		return dataScope;
	}

	public void setDataScope(String dataScope) {
		this.dataScope = dataScope;
	}

	public String getMenuIds() {
		return menuIds;
	}

	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}

	public String getOfficeIds() {
		return officeIds;
	}

	public void setOfficeIds(String officeIds) {
		this.officeIds = officeIds;
	}

	public String getSysData() {
		return sysData;
	}

	public void setSysData(String sysData) {
		this.sysData = sysData;
	}

	public String getUseable() {
		return useable;
	}

	public void setUseable(String useable) {
		this.useable = useable;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getOldName() {
		return oldName;
	}

	public void setOldName(String oldName) {
		this.oldName = oldName;
	}

	public String getOldEnname() {
		return oldEnname;
	}

	public void setOldEnname(String oldEnname) {
		this.oldEnname = oldEnname;
	}
}
