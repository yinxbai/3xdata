package com.txdata.system.domain;

import java.util.List;

import com.txdata.common.domain.DataEntity;

/**
 * 
 * 审批规则实体类
 * @author huangmk
 * @email huangmk@3xdata.cn
 * @date 2019-01-15 11:13:47
 */
public class AuditRuleDO extends DataEntity<AuditRuleDO> {
	private static final long serialVersionUID = 1L;

	private String ruleName; // 审批规则名称
	private String ruleSign;	// 规则标志
	private String ruleStatus; // 规则状态 1-启用 2-禁用
	private List<AuditRuleDetailDO> detailList;	// 规则明细

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public String getRuleName() {
		return ruleName;
	}

	public String getRuleSign() {
		return ruleSign;
	}

	public void setRuleSign(String ruleSign) {
		this.ruleSign = ruleSign;
	}

	public void setRuleStatus(String ruleStatus) {
		this.ruleStatus = ruleStatus;
	}

	public String getRuleStatus() {
		return ruleStatus;
	}

	public List<AuditRuleDetailDO> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<AuditRuleDetailDO> detailList) {
		this.detailList = detailList;
	}
}
