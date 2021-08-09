package com.txdata.system.domain;

import com.txdata.common.domain.DataEntity;

public class UserRoleDO extends DataEntity<UserRoleDO> {
	private static final long serialVersionUID = 1L;

	private String userId;
	private String roleId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	@Override
	public String toString() {
		return "UserRoleDO{" + "id=" + id + ", userId=" + userId + ", roleId=" + roleId + '}';
	}
}
