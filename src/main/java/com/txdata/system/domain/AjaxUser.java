package com.txdata.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.JSON;
import org.hibernate.validator.constraints.Length;
import com.txdata.common.domain.DataEntity;
import com.txdata.common.utils.excel.annotation.ExcelField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 用户Entity
 * 
 * @author ThinkGem
 * @version 2013-12-05
 */
public class AjaxUser extends DataEntity<AjaxUser> {

	private static final long serialVersionUID = 1L;
	private Office office; // 归属部门
	private String loginName;// 登录名
	private String password;// 密码
	private String no; // 工号
	private String name; // 姓名
	private String email; // 邮箱
	private String phone; // 电话
	private String mobile; // 手机
	private String userType;// 用户类型
	private String loginIp; // 最后登陆IP
	private Date loginDate; // 最后登陆日期
	private String loginFlag; // 是否允许登陆
	private String photo; // 头像
	private String carNum; // 车牌号
	private String oldloginName;// 前端个人中心详细信息页面命名错误，后台增加该字段以用来传值
	private String newPassword; // 新密码
	private BigDecimal stock; // 额定方量
	private String oldLoginIp; // 上次登陆IP
	private Date oldLoginDate; // 上次登陆日期
	private String officeId;
	private String officeName;
	private RoleDO role; // 根据角色查询用户条件
	private String roleNames; // 角色名称
	private String roleEnames; // 角色英文名称
	private String companyId;
	private String oldLoginName;// 原登录名
	private String roleIdList;
	private String position;	// 职位
	// 分管部门id
	private String manageOfficeId;
	private String areaName; // 归属区域名称
	// private List<Role> roleList = Lists.newArrayList(); // 拥有角色列表
	public AjaxUser(UserDO user) {
		this.office = user.getOffice();
		this.loginName = user.getUsername();
		this.name = user.getName();
		this.no = "";
		this.email = user.getEmail();
		this.phone = user.getPhone() == null ? "" : user.getPhone();
		this.mobile = user.getMobile() == null ? "" : user.getMobile();
		this.userType = user.getUserType();
		this.loginIp = "";
		this.loginFlag = user.getLoginFlag();
		this.officeId = user.getOfficeId();
		this.officeName = user.getOfficeName();
		this.roleNames = user.getRoleNames();
		this.roleEnames = user.getRoleEnames();
		this.roleIdList = JSON.toJSONString(user.getRoleIdList());
		this.oldloginName = user.getUsername();
		this.oldLoginName = user.getUsername();
		this.id = user.getId();
		this.remarks = user.getRemarks();
		this.companyId = user.getCompanyId();
		this.position = user.getPosition() == null ? "" : user.getPosition();
		this.manageOfficeId = user.getManageOfficeId();
		this.areaName = user.getAreaName();
	}

	public AjaxUser() {
		super();
		this.loginFlag = "1";
	}

	public AjaxUser(String id) {
		super(id);
	}

	public AjaxUser(String id, String loginName) {
		super(id);
		this.loginName = loginName;
	}

	public AjaxUser(RoleDO role) {
		super();
		this.role = role;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getLoginFlag() {
		return loginFlag;
	}

	public void setLoginFlag(String loginFlag) {
		this.loginFlag = loginFlag;
	}

//	@SupCol(isUnique="true", isHide="true")
//	@ExcelField(title="ID", type=1, align=2, sort=1)
//	public String getId() {
//		return id;
//	}

//	@JsonIgnore
//	@NotNull(message="归属机构不能为空")
//	@ExcelField(title="归属机构", align=2, sort=20)
//	public Office getCompany() {
//		return company;
//	}
//
//	public void setCompany(Office company) {
//		this.company = company;
//	}

	@JsonIgnore
	@NotNull(message = "归属部门不能为空")
	@ExcelField(title = "归属部门", align = 2, sort = 25)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	@Length(min = 1, max = 100, message = "登录名长度必须介于 1 和 100 之间")
	@ExcelField(title = "登录名", align = 2, sort = 30)
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@JsonIgnore
	@Length(min = 1, max = 100, message = "密码长度必须介于 1 和 100 之间")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Length(min = 1, max = 100, message = "姓名长度必须介于 1 和 100 之间")
	@ExcelField(title = "姓名", align = 2, sort = 40)
	public String getName() {
		return name;
	}

	@Length(min = 0, max = 100, message = "工号长度必须介于0 和 100 之间")
	@ExcelField(title = "工号", align = 2, sort = 45)
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Length(min = 0, max = 200, message = "邮箱长度必须介于 1 和 200 之间")
	@ExcelField(title = "邮箱", align = 1, sort = 50)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Length(min = 0, max = 200, message = "电话长度必须介于 1 和 200 之间")
	@ExcelField(title = "电话", align = 2, sort = 60)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Length(min = 0, max = 200, message = "手机长度必须介于 1 和 200 之间")
	@ExcelField(title = "手机", align = 2, sort = 70)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@ExcelField(title = "备注", align = 1, sort = 900)
	public String getRemarks() {
		return remarks;
	}

	@Length(min = 0, max = 100, message = "用户类型长度必须介于 1 和 100 之间")
	@ExcelField(title = "用户类型", align = 2, sort = 80, dictType = "sys_user_type")
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	@ExcelField(title = "创建时间", type = 0, align = 1, sort = 90)
	public Date getCreateDate() {
		return createDate;
	}

	@ExcelField(title = "最后登录IP", type = 1, align = 1, sort = 100)
	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title = "最后登录日期", type = 1, align = 1, sort = 110)
	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public String getOldloginName() {
		return oldloginName;
	}

	public void setOldloginName(String oldloginName) {
		this.oldloginName = oldloginName;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getOldLoginIp() {
		if (oldLoginIp == null) {
			return loginIp;
		}
		return oldLoginIp;
	}

	public void setOldLoginIp(String oldLoginIp) {
		this.oldLoginIp = oldLoginIp;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getOldLoginDate() {
		if (oldLoginDate == null) {
			return loginDate;
		}
		return oldLoginDate;
	}

	public void setOldLoginDate(Date oldLoginDate) {
		this.oldLoginDate = oldLoginDate;
	}

	public RoleDO getRole() {
		return role;
	}

	public void setRole(RoleDO role) {
		this.role = role;
	}

	public boolean isAdmin() {
		return isAdmin(this.id);
	}

	public static boolean isAdmin(String id) {
		return id != null && "1".equals(id);
	}

	@Override
	public String toString() {
		return id;
	}

	public String getCarNum() {
		return carNum;
	}

	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}

	public BigDecimal getStock() {
		return stock;
	}

	public void setStock(BigDecimal stock) {
		this.stock = stock;
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

	public String getRoleNames() {
		return roleNames;
	}

	public void setRoleNames(String roleNames) {
		this.roleNames = roleNames;
	}

	public String getRoleEnames() {
		return roleEnames;
	}

	public void setRoleEnames(String roleEnames) {
		this.roleEnames = roleEnames;
	}

	public String getRoleIdList() {
		return roleIdList;
	}

	public void setRoleIdList(String roleIdList) {
		this.roleIdList = roleIdList;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getOldLoginName() {
		return oldLoginName;
	}

	public void setOldLoginName(String oldLoginName) {
		this.oldLoginName = oldLoginName;
	}

	public String getPosition() {
		return position;
	}

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