package com.txdata.modules.contract.domain;

import com.txdata.common.domain.DataEntity;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author InRoota
 * @2021-08-02 09:22:50
 *
 */
public class InfoDO extends DataEntity<InfoDO> {
	private static final long serialVersionUID = 1L;

	private String code;  //合同编号 
	private String name;  //合同名称 
	private String partyA;  //甲方 
	private String partyB;  //乙方 
	private String unitName;  //委托单位名称 
	private BigDecimal amount;  //合同总金额 
	private BigDecimal performanceBond;  //违约保证金 
	private BigDecimal serviceLength;  //服务年限 
	private BigDecimal businessCost;  //商务成本 
	private String projectId;  //关联项目Id 
	private String customerName;  //客户名称 
	private BigDecimal telephone;  //联系电话
	private String isArchive; //归档标识 0.未归档；1.已归档
	private String  archiveDate; //归档日期
	private Date finishDate;  //交付时间
	private String status;
	private String procId;
 

	private String paymentDOS;
	private String purchaseDOS;
	private String contractProjectDOS;

	private List<PaymentDO> payments;
	private List<PurchaseDO> purchases;
	private List<ContractProjectDO> contractProjects;

	private String filePath;  //合同评审材料

	public void setCode(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	} 
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	} 
	
	public void setPartyA(String partyA) {
		this.partyA = partyA;
	}
	
	public String getPartyA() {
		return partyA;
	} 
	
	public void setPartyB(String partyB) {
		this.partyB = partyB;
	}
	
	public String getPartyB() {
		return partyB;
	} 
	
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	
	public String getUnitName() {
		return unitName;
	} 
	
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public BigDecimal getAmount() {
		return amount;
	} 
	
	public void setPerformanceBond(BigDecimal performanceBond) {
		this.performanceBond = performanceBond;
	}
	
	public BigDecimal getPerformanceBond() {
		return performanceBond;
	} 
	
	public void setServiceLength(BigDecimal serviceLength) {
		this.serviceLength = serviceLength;
	}
	
	public BigDecimal getServiceLength() {
		return serviceLength;
	} 
	
	public void setBusinessCost(BigDecimal businessCost) {
		this.businessCost = businessCost;
	}
	
	public BigDecimal getBusinessCost() {
		return businessCost;
	} 
	
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	
	public String getProjectId() {
		return projectId;
	} 
	
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	public String getCustomerName() {
		return customerName;
	} 
	
	public void setTelephone(BigDecimal telephone) {
		this.telephone = telephone;
	}
	
	public BigDecimal getTelephone() {
		return telephone;
	}

	public String getIsArchive() {
		return isArchive;
	}

	public void setIsArchive(String isArchive) {
		this.isArchive = isArchive;
	}

	public String getArchiveDate() {
		return archiveDate;
	}

	public void setArchiveDate(String archiveDate) {
		this.archiveDate = archiveDate;
	}

	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}
	
	public Date getFinishDate() {
		return finishDate;
	} 
	
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public String getFilePath() {
		return filePath;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getProcId() {
		return procId;
	}

	public void setProcId(String procId) {
		this.procId = procId;
	}

	public List<PaymentDO> getPayments() {
		return payments;
	}

	public void setPayments(List<PaymentDO> payments) {
		this.payments = payments;
	}

	public List<PurchaseDO> getPurchases() {
		return purchases;
	}

	public void setPurchases(List<PurchaseDO> purchases) {
		this.purchases = purchases;
	}

	public List<ContractProjectDO> getContractProjects() {
		return contractProjects;
	}

	public void setContractProjects(List<ContractProjectDO> contractProjects) {
		this.contractProjects = contractProjects;
	}

	public String getPaymentDOS() {
		return paymentDOS;
	}

	public void setPaymentDOS(String paymentDOS) {
		this.paymentDOS = paymentDOS;
	}

	public String getPurchaseDOS() {
		return purchaseDOS;
	}

	public void setPurchaseDOS(String purchaseDoS) {
		this.purchaseDOS = purchaseDOS;
	}

	public String getContractProjectDOS() {
		return contractProjectDOS;
	}

	public void setContractProjectDOS(String contractProjectDOS) {
		this.contractProjectDOS = contractProjectDOS;
	}
}
