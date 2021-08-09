package com.txdata.modules.project.domain;

import com.txdata.common.domain.DataEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 招投标信息实体类
 * 
 * @author huangmk
 * @email huangmk@3xdata.cn
 * @date 2018-12-27 11:21:10
 */
public class ProjectBidInfoDO extends DataEntity<ProjectBidInfoDO> {
	private static final long serialVersionUID = 1L;

	// 关联项目
	private String projectId;
	// 合作方式
	private String cooperateType;
	// 合作集成商
	private String cooperateIntegrator;
	// 招标编号
	private String bidCode;
	// 招标方式
	private String bidType;
	// 项目预算
	private BigDecimal projectBudget;
	// 项目中标价
	private BigDecimal bidPrice;
	// 毛利润
	private BigDecimal grossProfit;
	// 招标时间
	private Date callBidTime;
	// 投标文件
	private String bidFiles;
	// 招标文件
	private String callBidFiles;
	// 所含自主产品
	private String selfProducts;
	// 所含第三方产品
	private String thirdpartyProducts;

	public ProjectBidInfoDO() {
		
	}
	public ProjectBidInfoDO(ProjectDO project) {
		this.projectId = project.getId();
		this.cooperateType = project.getCooperateType();
		this.cooperateIntegrator = project.getCooperateIntegrator();
		this.bidCode = project.getBidCode();
		this.bidType = project.getBidType();
		this.projectBudget = project.getProjectBudget();
		this.bidPrice = project.getBidPrice();
		this.grossProfit = project.getGrossProfit();
		this.callBidTime = project.getCallBidTime();
		this.bidFiles = project.getBidFiles();
		this.callBidFiles = project.getCallBidFiles();
		this.selfProducts = project.getSelfProducts();
		this.thirdpartyProducts = project.getThirdpartyProducts();
	}
	
	/**
	 * 设置：关联项目
	 */
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	/**
	 * 获取：关联项目
	 */
	public String getProjectId() {
		return projectId;
	}

	/**
	 * 设置： 合作方式
	 */
	public void setCooperateType(String cooperateType) {
		this.cooperateType = cooperateType;
	}

	/**
	 * 获取： 合作方式
	 */
	public String getCooperateType() {
		return cooperateType;
	}

	/**
	 * 设置：合作集成商
	 */
	public void setCooperateIntegrator(String cooperateIntegrator) {
		this.cooperateIntegrator = cooperateIntegrator;
	}

	/**
	 * 获取：合作集成商
	 */
	public String getCooperateIntegrator() {
		return cooperateIntegrator;
	}

	/**
	 * 设置：招标编号
	 */
	public void setBidCode(String bidCode) {
		this.bidCode = bidCode;
	}

	/**
	 * 获取：招标编号
	 */
	public String getBidCode() {
		return bidCode;
	}

	/**
	 * 设置：招标方式
	 */
	public void setBidType(String bidType) {
		this.bidType = bidType;
	}

	/**
	 * 获取：招标方式
	 */
	public String getBidType() {
		return bidType;
	}

	/**
	 * 设置：项目预算
	 */
	public void setProjectBudget(BigDecimal projectBudget) {
		this.projectBudget = projectBudget;
	}

	/**
	 * 获取：项目预算
	 */
	public BigDecimal getProjectBudget() {
		return projectBudget;
	}

	/**
	 * 设置：项目中标价
	 */
	public void setBidPrice(BigDecimal bidPrice) {
		this.bidPrice = bidPrice;
	}

	/**
	 * 获取：项目中标价
	 */
	public BigDecimal getBidPrice() {
		return bidPrice;
	}

	/**
	 * 设置：毛利润
	 */
	public void setGrossProfit(BigDecimal grossProfit) {
		this.grossProfit = grossProfit;
	}

	/**
	 * 获取：毛利润
	 */
	public BigDecimal getGrossProfit() {
		return grossProfit;
	}

	/**
	 * 设置：招标时间
	 */
	public void setCallBidTime(Date callBidTime) {
		this.callBidTime = callBidTime;
	}

	/**
	 * 获取：招标时间
	 */
	public Date getCallBidTime() {
		return callBidTime;
	}

	/**
	 * 设置：投标文件
	 */
	public void setBidFiles(String bidFiles) {
		this.bidFiles = bidFiles;
	}

	/**
	 * 获取：投标文件
	 */
	public String getBidFiles() {
		return bidFiles;
	}

	/**
	 * 设置：招标文件
	 */
	public void setCallBidFiles(String callBidFiles) {
		this.callBidFiles = callBidFiles;
	}

	/**
	 * 获取：招标文件
	 */
	public String getCallBidFiles() {
		return callBidFiles;
	}

	/**
	 * 设置：所含自主产品
	 */
	public void setSelfProducts(String selfProducts) {
		this.selfProducts = selfProducts;
	}

	/**
	 * 获取：所含自主产品
	 */
	public String getSelfProducts() {
		return selfProducts;
	}

	/**
	 * 设置：所含第三方产品
	 */
	public void setThirdpartyProducts(String thirdpartyProducts) {
		this.thirdpartyProducts = thirdpartyProducts;
	}

	/**
	 * 获取：所含第三方产品
	 */
	public String getThirdpartyProducts() {
		return thirdpartyProducts;
	}
}
