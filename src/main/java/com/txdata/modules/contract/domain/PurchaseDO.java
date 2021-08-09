package com.txdata.modules.contract.domain;

import com.txdata.common.domain.DataEntity;

import java.security.PrivateKey;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author InRoota
 * @2021-08-02 09:23:30
 *
 */
public class PurchaseDO extends DataEntity<PurchaseDO> {
	private static final long serialVersionUID = 1L;

	private String projectId;  //项目Id
	private String contractId;  //关联合同ID 
	private Date periods;  //期数 
	private BigDecimal money;  //付款金额 
	private String paymentTerms;  //付款条件 
	private String additionalConditions;  //附加条件

//	private ContractProjectDO contractProjectDO;

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public String getContractId() {
		return contractId;
	}

	public void setPeriods(Date periods) {
		this.periods = periods;
	}

	public Date getPeriods() {
		return periods;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setPaymentTerms(String paymentTerms) {
		this.paymentTerms = paymentTerms;
	}

	public String getPaymentTerms() {
		return paymentTerms;
	}

	public void setAdditionalConditions(String additionalConditions) {
		this.additionalConditions = additionalConditions;
	}

	public String getAdditionalConditions() {
		return additionalConditions;
	}

	/*public ContractProjectDO getContractProjectDO() {
		return contractProjectDO;
	}

	public void setContractProjectDO(ContractProjectDO contractProjectDO) {
		this.contractProjectDO = contractProjectDO;
	}*/
}
