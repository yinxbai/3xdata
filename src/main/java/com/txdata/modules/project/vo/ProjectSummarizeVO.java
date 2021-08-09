/**
 * @Copyright: 2018 www.3xdata.cn Inc. All rights reserved. 
 * @description：用一句话描述该文件做什么
 * @Package: com.txdata.modules.project.vo 
 * @author: huangmk   
 * @date: 2018年12月28日 下午5:01:46 
 */
package com.txdata.modules.project.vo;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.txdata.modules.project.domain.ProjectMemberDO;

/**
 * @ClassName: ProjectSummarizeVO
 * @Description: 项目综述信息实体类
 * 
 * @author: huangmk
 * @date: 2018年12月28日 下午5:01:46
 */
public class ProjectSummarizeVO {

	private String id; // 项目ID
	private String code; // 项目编号
	private String name; // 项目名称
	private String projectType; // 项目类型
	private String projectLeader; // 项目负责人id
	private String projectLeaderName;// 项目负责人姓名
	private String status; // 项目状态
	private String currentStage; // 项目当前阶段
	private String completedProgress; // 项目已完成进度
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date createDate; // 项目创建时间
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date startDate; // 项目开始时间
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date endDate; // 交付日期
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date predictEndDate;	// 项目预计结束时间
	private Date updateDate; // 项目计划更新时间
	private String actualDate; // 项目实际结束时间
	private List<ProjectMemberDO> memberList;	// 项目成员
	
	private String memberIds;	// 项目成员用户ID

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public String getProjectLeader() {
		return projectLeader;
	}

	public void setProjectLeader(String projectLeader) {
		this.projectLeader = projectLeader;
	}

	public String getProjectLeaderName() {
		return projectLeaderName;
	}

	public void setProjectLeaderName(String projectLeaderName) {
		this.projectLeaderName = projectLeaderName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCurrentStage() {
		return currentStage;
	}

	public void setCurrentStage(String currentStage) {
		this.currentStage = currentStage;
	}

	public String getCompletedProgress() {
		return completedProgress;
	}

	public void setCompletedProgress(String completedProgress) {
		this.completedProgress = completedProgress;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getPredictEndDate() {
		return predictEndDate;
	}

	public void setPredictEndDate(Date predictEndDate) {
		this.predictEndDate = predictEndDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getActualDate() {
		return actualDate;
	}

	public void setActualDate(String actualDate) {
		this.actualDate = actualDate;
	}

	public List<ProjectMemberDO> getMemberList() {
		return memberList;
	}

	public void setMemberList(List<ProjectMemberDO> memberList) {
		this.memberList = memberList;
	}

	public String getMemberIds() {
		return memberIds;
	}

	public void setMemberIds(String memberIds) {
		this.memberIds = memberIds;
	}

}
