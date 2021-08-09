package com.txdata.system.domain;

import java.io.Serializable;
import java.util.List;

/**
 * 机构Entity
 * 
 * @author ThinkGem
 * @version 2013-05-15
 */
public class AjaxOffice implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String parentId;
	private String parentName;
	private String areaId;
	private String areaName;
	// 机构编码
	private String code;
	// 机构名称
	private String name;
	// 是否可用
	private String useable;
	// 统一社会信用代码 18位
	private String uscreditCode;
	// 用户还是机构，用户为0，机构为1
	private String type;
	// 公司印章图片地址
	private String sealPath;
	// 水政监察印章图片地址
	private String szjcSealPath;
	private List<AjaxOffice> children;
	private String leader; // 分管领导
	private String principal; // 部门负责人
	private String leaderName; // 分管领导名称
	private String principalName; // 机构负责人名称

	/**
	 * 用于机构用户树中保存用户信息
	 */
	private String officeName;
	private String position;
	private String mobile;
	private String email;

	public AjaxOffice() {
	}

	public AjaxOffice(UserDO user, String parentId) {
		this.parentId = parentId;
		this.id = user.getId();
		this.name = user.getName();
		this.type = "0";
		this.officeName = user.getOfficeName();
		this.position = user.getPosition();
		this.mobile = user.getMobile();
		this.email = user.getEmail();
	}

	public AjaxOffice(Office o, boolean isAll) {
		this.id = o.getId();
		this.name = o.getName();
		this.type = "1";
		if (isAll) {
			this.code = o.getCode();
			this.useable = o.getUseable();
			this.uscreditCode = o.getUscreditCode();
			Office parent = o.getParent();
			if (parent != null) {
				this.parentId = parent.getId();
				this.parentName = parent.getName();
			}
//			this.parentId = o.getParentId();
//			this.parentName = o.getParentName();
			this.areaId = o.getAreaId();
			this.areaName = o.getAreaName();
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUseable() {
		return useable;
	}

	public void setUseable(String useable) {
		this.useable = useable;
	}

	public String getUscreditCode() {
		return uscreditCode;
	}

	public void setUscreditCode(String uscreditCode) {
		this.uscreditCode = uscreditCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<AjaxOffice> getChildren() {
		return children;
	}

	public void setChildren(List<AjaxOffice> children) {
		this.children = children;
	}

	public String getSealPath() {
		return sealPath;
	}

	public void setSealPath(String sealPath) {
		this.sealPath = sealPath;
	}

	public String getSzjcSealPath() {
		return szjcSealPath;
	}

	public void setSzjcSealPath(String szjcSealPath) {
		this.szjcSealPath = szjcSealPath;
	}

	public String getLeader() {
		return leader;
	}

	public void setLeader(String leader) {
		this.leader = leader;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String getLeaderName() {
		return leaderName;
	}

	public void setLeaderName(String leaderName) {
		this.leaderName = leaderName;
	}

	public String getPrincipalName() {
		return principalName;
	}

	public void setPrincipalName(String principalName) {
		this.principalName = principalName;
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
}