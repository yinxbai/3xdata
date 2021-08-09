package com.txdata.oa.domain;

import com.txdata.common.domain.DataEntity;
import java.util.Date;

/**
 * 通知通告发送记录
 * 
 * @author 3xdata
 * @email 3xdata@3xdata.cn
 * @date 2019-07-12 11:01:06
 */
public class NotifyRecordDO extends DataEntity<NotifyRecordDO> {
	private static final long serialVersionUID = 1L;

	private String notifyId;  //通知通告ID 
	private String userId;  //接受人 
	private Integer isRead;  //阅读标记 
	private Date readDate;  //阅读时间 
	//辅助字段
	private Integer readFlag;//阅读标记（历史遗留字段，前端通过该字段判断是否阅读，就是isRead）
	private String userName;//接收人名称
	private String officeName;//接收部门
	
	public void setNotifyId(String notifyId) {
		this.notifyId = notifyId;
	}
	
	public String getNotifyId() {
		return notifyId;
	} 
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getUserId() {
		return userId;
	} 
	
	public void setIsRead(Integer isRead) {
		this.readFlag = isRead;
		this.isRead = isRead;
	}
	
	public Integer getIsRead() {
		return isRead;
	} 
	
	public void setReadDate(Date readDate) {
		this.readDate = readDate;
	}
	
	public Date getReadDate() {
		return readDate;
	}

	public Integer getReadFlag() {
		return readFlag;
	}

	public void setReadFlag(Integer readFlag) {
		this.readFlag = readFlag;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	} 
}
