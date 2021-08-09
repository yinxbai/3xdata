package com.txdata.modules.project.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.txdata.common.domain.DataEntity;
import com.txdata.modules.project.vo.MemberVO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 项目备案实体类
 * 
 * @author huangmk
 * @email huangmk@3xdata.cn
 * @date 2018-12-27 10:59:18
 */
public class ProjectDO extends DataEntity<ProjectDO> {
	private static final long serialVersionUID = 1L;

	// 项目编号
	private String code;
	// 项目名称
	private String name;
	// 项目类型
	private String projectType;
	// 备案人
	private String recordPeopel;
	// 备案时间
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date recordTime;
	// 项目干系人(关联客户)
	private String customers;
	// 项目状态
	private String status;
	// 项目成员ID，逗号分隔
	private String memberIds;
	private String areaId;	// 所属地区ID
	/**
	 * 以下属性是招投标信息的属性
	 */
	private String areaName;	// 所属地区名称
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
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date callBidTime;
	// 投标文件
	private String bidFiles;
	// 招标文件
	private String callBidFiles;
	// 所含自主产品
	private String selfProducts;
	// 所含第三方产品
	private String thirdpartyProducts;
	/**
	 * 以下为自定义属性
	 */
	private String recordPeopelName;	// 备案人姓名
	
	/**
	 * 关联用户信息
	 */
	private List<MemberVO> memberList;

	/**
	 * 设置：项目编号
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 获取：项目编号
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 设置：项目名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取：项目名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置：项目类型
	 */
	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	/**
	 * 获取：项目类型
	 */
	public String getProjectType() {
		return projectType;
	}

	/**
	 * 设置：备案人
	 */
	public void setRecordPeopel(String recordPeopel) {
		this.recordPeopel = recordPeopel;
	}

	/**
	 * 获取：备案人
	 */
	public String getRecordPeopel() {
		return recordPeopel;
	}

	/**
	 * 设置：备案时间
	 */
	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}

	/**
	 * 获取：备案时间
	 */
	public Date getRecordTime() {
		return recordTime;
	}

	/**
	 * 设置：项目干系人(关联客户)
	 */
	public void setCustomers(String customers) {
		this.customers = customers;
	}

	/**
	 * 获取：项目干系人(关联客户)
	 */
	public String getCustomers() {
		return customers;
	}

	/**
	 * 设置：项目状态
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * 获取：项目状态
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @return the memberIds
	 */
	public String getMemberIds() {
		return memberIds;
	}

	/**
	 * @param memberIds the memberIds to set
	 */
	public void setMemberIds(String memberIds) {
		this.memberIds = memberIds;
	}

	/**
	 * @return the cooperateType
	 */
	public String getCooperateType() {
		return cooperateType;
	}

	/**
	 * @param cooperateType the cooperateType to set
	 */
	public void setCooperateType(String cooperateType) {
		this.cooperateType = cooperateType;
	}

	/**
	 * @return the cooperateIntegrator
	 */
	public String getCooperateIntegrator() {
		return cooperateIntegrator;
	}

	/**
	 * @param cooperateIntegrator the cooperateIntegrator to set
	 */
	public void setCooperateIntegrator(String cooperateIntegrator) {
		this.cooperateIntegrator = cooperateIntegrator;
	}

	/**
	 * @return the bidCode
	 */
	public String getBidCode() {
		return bidCode;
	}

	/**
	 * @param bidCode the bidCode to set
	 */
	public void setBidCode(String bidCode) {
		this.bidCode = bidCode;
	}

	/**
	 * @return the bidType
	 */
	public String getBidType() {
		return bidType;
	}

	/**
	 * @param bidType the bidType to set
	 */
	public void setBidType(String bidType) {
		this.bidType = bidType;
	}

	/**
	 * @return the projectBudget
	 */
	public BigDecimal getProjectBudget() {
		return projectBudget;
	}

	/**
	 * @param projectBudget the projectBudget to set
	 */
	public void setProjectBudget(BigDecimal projectBudget) {
		this.projectBudget = projectBudget;
	}

	/**
	 * @return the bidPrice
	 */
	public BigDecimal getBidPrice() {
		return bidPrice;
	}

	/**
	 * @param bidPrice the bidPrice to set
	 */
	public void setBidPrice(BigDecimal bidPrice) {
		this.bidPrice = bidPrice;
	}

	/**
	 * @return the grossProfit
	 */
	public BigDecimal getGrossProfit() {
		return grossProfit;
	}

	/**
	 * @param grossProfit the grossProfit to set
	 */
	public void setGrossProfit(BigDecimal grossProfit) {
		this.grossProfit = grossProfit;
	}

	/**
	 * @return the callBidTime
	 */
	public Date getCallBidTime() {
		return callBidTime;
	}

	/**
	 * @param callBidTime the callBidTime to set
	 */
	public void setCallBidTime(Date callBidTime) {
		this.callBidTime = callBidTime;
	}

	/**
	 * @return the bidFiles
	 */
	public String getBidFiles() {
		return bidFiles;
	}

	/**
	 * @param bidFiles the bidFiles to set
	 */
	public void setBidFiles(String bidFiles) {
		this.bidFiles = bidFiles;
	}

	/**
	 * @return the callBidFiles
	 */
	public String getCallBidFiles() {
		return callBidFiles;
	}

	/**
	 * @param callBidFiles the callBidFiles to set
	 */
	public void setCallBidFiles(String callBidFiles) {
		this.callBidFiles = callBidFiles;
	}

	/**
	 * @return the selfProducts
	 */
	public String getSelfProducts() {
		return selfProducts;
	}

	/**
	 * @param selfProducts the selfProducts to set
	 */
	public void setSelfProducts(String selfProducts) {
		this.selfProducts = selfProducts;
	}

	/**
	 * @return the thirdpartyProducts
	 */
	public String getThirdpartyProducts() {
		return thirdpartyProducts;
	}

	/**
	 * @param thirdpartyProducts the thirdpartyProducts to set
	 */
	public void setThirdpartyProducts(String thirdpartyProducts) {
		this.thirdpartyProducts = thirdpartyProducts;
	}

	/**
	 * @return the memberList
	 */
	public List<MemberVO> getMemberList() {
		return memberList;
	}

	/**
	 * @param memberList the memberList to set
	 */
	public void setMemberList(List<MemberVO> memberList) {
		this.memberList = memberList;
	}

	public String getRecordPeopelName() {
		return recordPeopelName;
	}

	public void setRecordPeopelName(String recordPeopelName) {
		this.recordPeopelName = recordPeopelName;
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
}
