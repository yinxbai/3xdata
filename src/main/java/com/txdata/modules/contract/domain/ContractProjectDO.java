package com.txdata.modules.contract.domain;

import com.txdata.common.domain.DataEntity;
import java.util.Date;
import java.math.BigDecimal;

/**
 * 
 * 
 * @author 3xdata
 * @email 3xdata@3xdata.cn
 * @date 2021-08-02 16:42:56
 */
public class ContractProjectDO extends DataEntity<ContractProjectDO> {
	private static final long serialVersionUID = 1L;

	private String contractId;  //关联合同ID 
	private String productName;  //产品名称 
	private String productCategory;  //产品类目id 
	private Integer number;  //产品数量 
	private BigDecimal unitPrice;  //投标产品单价 
	private String isPurchase;  //是否需要采购，0表示不需要，1表示需要 
	private Date earlyWarningDate;  //采购预警时间 
	private String productBrand;  //产品品牌id 
	private String index;  //排序字段
	private String purchase_status; //采购状态
	
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	
	public String getContractId() {
		return contractId;
	} 
	
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public String getProductName() {
		return productName;
	} 
	
	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}
	
	public String getProductCategory() {
		return productCategory;
	} 
	
	public void setNumber(Integer number) {
		this.number = number;
	}
	
	public Integer getNumber() {
		return number;
	} 
	
	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}
	
	public BigDecimal getUnitPrice() {
		return unitPrice;
	} 
	
	public void setIsPurchase(String isPurchase) {
		this.isPurchase = isPurchase;
	}
	
	public String getIsPurchase() {
		return isPurchase;
	} 
	
	public void setEarlyWarningDate(Date earlyWarningDate) {
		this.earlyWarningDate = earlyWarningDate;
	}
	
	public Date getEarlyWarningDate() {
		return earlyWarningDate;
	} 
	
	public void setProductBrand(String productBrand) {
		this.productBrand = productBrand;
	}
	
	public String getProductBrand() {
		return productBrand;
	} 
	
	public void setIndex(String index) {
		this.index = index;
	}
	
	public String getIndex() {
		return index;
	}

	public String getPurchase_status() {
		return purchase_status;
	}

	public void setPurchase_status(String purchase_status) {
		this.purchase_status = purchase_status;
	}
}
