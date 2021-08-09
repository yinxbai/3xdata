package com.txdata.modules.project.domain;

import com.txdata.common.domain.DataEntity;

/**
 * 项目关联团队成员实体类
 * 
 * @author huangmk
 * @email huangmk@3xdata.cn
 * @date 2018-12-27 11:44:22
 */
public class ProjectMemberDO extends DataEntity<ProjectMemberDO> {
	private static final long serialVersionUID = 1L;

	// 关联项目
	private String projectId;
	// 关联用户
	private String userId;
	/**
	 * 以下为自定义属性
	 */
	private String name;			// 成员姓名 
	private String officeId;		// 所属部门id
	private String officeName;	// 所属部门名称
	private String position;		// 职位
	private String mobile;		// 手机
	private String email;		// 邮箱
	private String officeParentIds;	// 所属部门ids

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 设置：关联项目
	 */
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	/**
	 * 获取：关联项目
	 */
	public String getProjectId() {
		return projectId;
	}

	/**
	 * 设置：关联用户
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * 获取：关联用户
	 */
	public String getUserId() {
		return userId;
	}

	public String getOfficeParentIds() {
		return officeParentIds;
	}

	public void setOfficeParentIds(String officeParentIds) {
		this.officeParentIds = officeParentIds;
	}
}
