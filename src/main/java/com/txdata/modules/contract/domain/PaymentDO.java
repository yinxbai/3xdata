package com.txdata.modules.contract.domain;

import com.txdata.common.domain.DataEntity;
import java.util.Date;
import java.math.BigDecimal;

/**
 * @author InRoota
 * @2021-08-02 09:23:10
 *
 */
public class PaymentDO extends DataEntity<PaymentDO> {
	private static final long serialVersionUID = 1L;

	private String contractId;  //关联合同Id 
	private BigDecimal receivedMoney;  //已收款金额
	private String receivedBank;  //回款银行
	private String receivedAccount;  //回款账号
	private Date receivedTime;  //回款时间
	private String filepath;  //回单 

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public BigDecimal getReceivedMoney() {
		return receivedMoney;
	}

	public void setReceivedMoney(BigDecimal receivedMoney) {
		this.receivedMoney = receivedMoney;
	}

	public String getReceivedBank() {
		return receivedBank;
	}

	public void setReceivedBank(String receivedBank) {
		this.receivedBank = receivedBank;
	}

	public String getReceivedAccount() {
		return receivedAccount;
	}

	public void setReceivedAccount(String receivedAccount) {
		this.receivedAccount = receivedAccount;
	}

	public Date getReceivedTime() {
		return receivedTime;
	}

	public void setReceivedTime(Date receivedTime) {
		this.receivedTime = receivedTime;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
}
