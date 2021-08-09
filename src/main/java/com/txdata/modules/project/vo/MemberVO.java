/**
 * @Copyright: 2018 www.3xdata.cn Inc. All rights reserved. 
 * @description：用一句话描述该文件做什么
 * @Package: com.txdata.modules.project.vo 
 * @author: mark   
 * @date: 2018年12月27日 下午2:35:11 
 */
package com.txdata.modules.project.vo;

import com.txdata.system.domain.UserDO;

/**
 * @ClassName: MemberVO
 * @Description: 项目关联用户
 * 
 * @author: huangmk
 * @date: 2018年12月27日 下午2:35:11
 */
public class MemberVO {

	// 用户ID
	private String userId;
	// 用户姓名
	private String name;
	// 部门名称
	private String officeName;
	// 部门ID
	private String officeId;
	// 职位
	private String position;
	// 联系电话
	private String mobile;
	private String officeParentIds;	// 所属部门ids
	
	public MemberVO() {
		
	}
	
	public MemberVO(UserDO user) {
		this.userId = user.getId();
		this.name = user.getName();
		this.officeName = user.getOfficeName();
		this.position = user.getPosition();
		this.mobile = user.getMobile();
		this.officeId = user.getOfficeId();
		this.officeParentIds = user.getOffice() != null?user.getOffice().getParentIds():"";
	}
	
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the officeName
	 */
	public String getOfficeName() {
		return officeName;
	}
	/**
	 * @param officeName the officeName to set
	 */
	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}
	/**
	 * @return the officeId
	 */
	public String getOfficeId() {
		return officeId;
	}

	/**
	 * @param officeId the officeId to set
	 */
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
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
	/**
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}
	/**
	 * @param mobile the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getOfficeParentIds() {
		return officeParentIds;
	}

	public void setOfficeParentIds(String officeParentIds) {
		this.officeParentIds = officeParentIds;
	}
	
}
