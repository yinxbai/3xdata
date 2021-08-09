package com.txdata.system.domain;

import com.txdata.common.domain.DataEntity;
import com.txdata.common.utils.Collections3;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;

import java.util.Date;
import java.util.List;

/**
 * 用户相关实体类
 * 
 * @author huangmk
 */
public class UserDO extends DataEntity<UserDO> {
	private static final long serialVersionUID = 1L;
	// 用户名
	private String username;
	// 用户真实姓名
	private String name;
	// 密码
	private String password;
	// 邮箱
	private String email;
	// 手机号
	private String mobile;
	private String phone; // 电话
	// 状态 0:禁用，1:正常
	private Integer status;
	// 角色
	private List<String> roleIds;
	// 出身日期
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date birth;
	// 用户类型
	private String userType;
	private String userRoleId;
	private Office company; // 归属部门
	private Office office; // 归属部门
	private String officeId;
	private String officeName;
	private String companyId;
	private String companyName;
	private String roleId;
	private RoleDO role; // 根据角色查询用户条件
	private String loginFlag; // 是否允许登陆
	private List<Role> roleList = Lists.newArrayList(); // 拥有角色列表
	private String areaId;// 区域主键辅助查询
	private String areaName; // 归属区域名称
	private String oldLoginName;// 原登录名
	private Role roleApi;
	private String no; // 工号
	private String loginName; // 登录名
	private String newPassword; // 新密码
	// 分管部门id
	private String manageOfficeId;
	private String position;	// 职位
	public UserDO(AjaxUser u) {
		super();
		this.loginFlag = "1";
		this.email = u.getEmail();
		this.phone = u.getPhone();
		this.mobile = u.getMobile();
		this.remarks = u.getRemarks();
		this.officeId = u.getOfficeId();
		this.username = u.getLoginName();
		this.password = u.getPassword();
		this.id = u.getId();
		this.userType = u.getUserType();
		this.loginFlag = u.getLoginFlag();
		this.no = u.getNo();
		this.companyId = u.getCompanyId();
		this.name = u.getName();
		this.position = u.getPosition();
		this.manageOfficeId = u.getManageOfficeId();
	}

	public UserDO() {
		super();
		this.loginFlag = "1";
	}

	public UserDO(String id) {
		super(id);
	}

	public UserDO(String id, String username) {
		super(id);
		this.username = username;
	}

	public UserDO(RoleDO role) {
		super();
		this.role = role;
	}

	public UserDO(Role role) {
		super();
		this.roleApi = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

//	public String getDeptId() {
//		return deptId;
//	}
//
//	public void setDeptId(String deptId) {
//		this.deptId = deptId;
//	}

//	public String getDeptName() {
//		return deptName;
//	}
//
//	public void setDeptName(String deptName) {
//		this.deptName = deptName;
//	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public List<String> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(List<String> roleIds) {
		this.roleIds = roleIds;
	}

//	public Long getSex() {
//		return sex;
//	}
//
//	public void setSex(Long sex) {
//		this.sex = sex;
//	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getLoginFlag() {
		return loginFlag;
	}

	public void setLoginFlag(String loginFlag) {
		this.loginFlag = loginFlag;
	}

	public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
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

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public boolean isAdmin() {
		return isAdmin(this.id);
	}

	public static boolean isAdmin(String id) {
		return id != null && "1".equals(id);
	}

	public String getOldLoginName() {
		return oldLoginName;
	}

	public void setOldLoginName(String oldLoginName) {
		this.oldLoginName = oldLoginName;
	}

	public Office getCompany() {
		return company;
	}

	public void setCompany(Office company) {
		this.company = company;
	}

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	public RoleDO getRole() {
		return role;
	}

	public void setRole(RoleDO role) {
		this.role = role;
	}

	// @Override
	// public String toString() {
	// return "UserDO{" +
	// "id=" + id +
	// ", username='" + username + '\'' +
	// ", name='" + name + '\'' +
	// ", password='" + password + '\'' +
	// ", deptId=" + deptId +
	// ", deptName='" + deptName + '\'' +
	// ", email='" + email + '\'' +
	// ", mobile='" + mobile + '\'' +
	// ", status=" + status +
	// ", createBy=" + createBy +
	// ", createDate=" + createDate +
	// ", updateBy=" + updateBy +
	// ", updateDate=" + updateDate +
	// ", roleIds=" + roleIds +
	// ", sex=" + sex +
	// ", birth=" + birth +
	// ", picId=" + picId +
	// ", liveAddress='" + liveAddress + '\'' +
	// ", hobby='" + hobby + '\'' +
	// ", province='" + province + '\'' +
	// ", city='" + city + '\'' +
	// ", district='" + district + '\'' +
	// ", userType='" + userType + '\''+
	// '}';
	// }

	public Role getRoleApi() {
		return roleApi;
	}

	public void setRoleApi(Role roleApi) {
		this.roleApi = roleApi;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(String userRoleId) {
		this.userRoleId = userRoleId;
	}

	/**
	 * 用户拥有的角色英文名称字符串, 多个角色名称用','分隔.
	 */
	public String getRoleEnames() {
		return Collections3.extractToString(roleList, "enname", ",");
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	/**
	 * 用户拥有的角色名称字符串, 多个角色名称用','分隔.
	 */
	public String getRoleNames() {
		return Collections3.extractToString(roleList, "name", ",");
	}

	public List<String> getRoleIdList() {
		List<String> roleIdList = Lists.newArrayList();
		for (Role role : roleList) {
			roleIdList.add(role.getId());
		}
		return roleIdList;
	}

	public void setRoleIdList(List<String> roleIdList) {
		roleList = Lists.newArrayList();
		for (String roleId : roleIdList) {
			Role role = new Role();
			role.setId(roleId);
			roleList.add(role);
		}
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	/**
	 * @return the roleId
	 */
	public String getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId the roleId to set
	 */
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	/**
	 * @return the position
	 */
	public String getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(String position) {
		this.position = position;
	}

	public String getManageOfficeId() {
		return manageOfficeId;
	}

	public void setManageOfficeId(String manageOfficeId) {
		this.manageOfficeId = manageOfficeId;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

}
