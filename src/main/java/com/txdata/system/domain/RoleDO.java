package com.txdata.system.domain;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import com.txdata.common.domain.DataEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;

public class RoleDO extends DataEntity<RoleDO> {
	private static final long serialVersionUID = 1L;

//	private Long roleId;
	private String roleName;
	private String roleSign;
//	private String remark;
//	private Long userIdCreate;
//	private Timestamp gmtCreate;
//	private Timestamp gmtModified;
//	private String menuIds;

	private String roleType;// 权限类型
	private String dataScope;// 数据范围
	private String sysData; // 是否是系统数据
	private String useable; // 是否是可用

	@JsonIgnore
	private List<MenuDO> menuList;

	// 数据范围（1：所有数据；2：所在公司及以下数据；3：所在公司数据；4：所在部门及以下数据；5：所在部门数据；8：仅本人数据；9：按明细设置）
	public static final String DATA_SCOPE_ALL = "1";
	public static final String DATA_SCOPE_COMPANY_AND_CHILD = "2";
	public static final String DATA_SCOPE_COMPANY = "3";
	public static final String DATA_SCOPE_OFFICE_AND_CHILD = "4";
	public static final String DATA_SCOPE_OFFICE = "5";
	public static final String DATA_SCOPE_SELF = "8";
	public static final String DATA_SCOPE_CUSTOM = "9";

	public RoleDO() {
		super();
		this.dataScope = DATA_SCOPE_SELF;
		this.useable = "1";
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleSign() {
		return roleSign;
	}

	public void setRoleSign(String roleSign) {
		this.roleSign = roleSign;
	}

//	public List<String> getMenuIds() {
//		return menuIds;
//	}
//
//	public void setMenuIds(List<String> menuIds) {
//		this.menuIds = menuIds;
//	}

	public List<MenuDO> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<MenuDO> menuList) {
		this.menuList = menuList;
	}

	public String getMenuIds() {
		return StringUtils.join(getMenuIdList(), ",");
	}

	public void setMenuIds(String menuIds) {
		menuList = Lists.newArrayList();
		if (menuIds != null) {
			String[] ids = StringUtils.split(menuIds, ",");
			setMenuIdList(Lists.newArrayList(ids));
		}
	}

	public List<String> getMenuIdList() {
		List<String> menuIdList = Lists.newArrayList();
		for (MenuDO menu : menuList) {
			menuIdList.add(menu.getId());
		}
		return menuIdList;
	}

	public void setMenuIdList(List<String> menuIdList) {
		menuList = Lists.newArrayList();
		for (String menuId : menuIdList) {
			MenuDO menu = new MenuDO();
			menu.setId(menuId);
			menuList.add(menu);
		}
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

//	@Override
//	public String toString() {
//		return "RoleDO{" +
//				"id=" + id +
//				", roleName='" + roleName + '\'' +
//				", roleSign='" + roleSign + '\'' +
//				", remarks='" + remarks + '\'' +
//				", createBy=" + createBy +
//				", createDate=" + createDate +
//				", updateBy=" + updateBy +
//				", updateDate=" + updateDate +
//				", menuIds=" + menuIds +
//				'}';
//	}
}
