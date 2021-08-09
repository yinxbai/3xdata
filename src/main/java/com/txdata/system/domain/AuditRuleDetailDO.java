package com.txdata.system.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.txdata.common.domain.DataEntity;

/**
 * 
 * 审批规则明细实体类
 * 
 * @author huangmk
 * @email huangmk@3xdata.cn
 * @date 2019-01-15 11:14:32
 */
public class AuditRuleDetailDO extends DataEntity<AuditRuleDetailDO> {
	private static final long serialVersionUID = 1L;

	private String auditUserId; // 审批人ID
	private String applyUserIds; // 申请人ID，逗号分隔
	private String ruleId; // 关联审批规则
	/**
	 * 自定义属性 满足前端接口要求
	 */
	private String auditUserName; // 审批人用户姓名
	private String auditUserOfficeName; // 审批人所属部门
	private String applyUserNames; // 申请人姓名，逗号分隔
	// 排序字段
	@JsonIgnore
	private Integer sort;

	public void setAuditUserId(String auditUserId) {
		this.auditUserId = auditUserId;
	}

	public String getAuditUserId() {
		return auditUserId;
	}

	public void setApplyUserIds(String applyUserIds) {
		this.applyUserIds = applyUserIds;
	}

	public String getApplyUserIds() {
		return applyUserIds;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public String getRuleId() {
		return ruleId;
	}

	public String getAuditUserOfficeName() {
		return auditUserOfficeName;
	}

	public void setAuditUserOfficeName(String auditUserOfficeName) {
		this.auditUserOfficeName = auditUserOfficeName;
	}

	public String getApplyUserNames() {
		return applyUserNames;
	}

	public void setApplyUserNames(String applyUserNames) {
		this.applyUserNames = applyUserNames;
	}

	public String getAuditUserName() {
		return auditUserName;
	}

	public void setAuditUserName(String auditUserName) {
		this.auditUserName = auditUserName;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
}
