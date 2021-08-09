package com.txdata.oa.domain;

import java.util.List;

import com.txdata.common.domain.DataEntity;


/**
 * 通知通告
 * 
 * @author 3xdata
 * @email 3xdata@3xdata.cn
 * @date 2019-07-11 14:57:30
 */
public class NotifyDO extends DataEntity<NotifyDO> {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 状态 0草稿
	 */
	public static String NOTIFY_STATUS_DRAFT = "0";
	/**
	 * 状态 1 发布
	 */
	public static String NOTIFY_STATUS_PUBLISH = "1";

	private String type;  //类型 
	private String title;  //标题 
	private String content;  //内容 
	private String files;  //附件 
	private String status;  //状态 0草稿 1 发布
	//阅读明细
	private List<NotifyRecordDO> oaNotifyRecordList;
	//辅助字段
	private String readNum;//消息已读数量
	private String unReadNum;//消息未读数量
	private String readFlag;//阅读标记 0未读,1已读 相当于明细表中的isRead
	private boolean isSelf; // 是否只查询自己的通知
	private String oaNotifyRecordIds;//接收用户ids
	private String oaNotifyRecordNames;//接收用户名称s
	private String count;//消息总数
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	} 
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	} 
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getContent() {
		return content;
	} 
	
	public void setFiles(String files) {
		this.files = files;
	}
	
	public String getFiles() {
		return files;
	} 
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getStatus() {
		return status;
	}

	public String getReadNum() {
		return readNum;
	}

	public void setReadNum(String readNum) {
		this.readNum = readNum;
	}

	public String getUnReadNum() {
		return unReadNum;
	}

	public void setUnReadNum(String unReadNum) {
		this.unReadNum = unReadNum;
	}

	public String getReadFlag() {
		return readFlag;
	}

	public void setReadFlag(String readFlag) {
		this.readFlag = readFlag;
	}

	public boolean isSelf() {
		return isSelf;
	}

	public void setSelf(boolean isSelf) {
		this.isSelf = isSelf;
	}

	public String getOaNotifyRecordIds() {
		return oaNotifyRecordIds;
	}

	public void setOaNotifyRecordIds(String oaNotifyRecordIds) {
		this.oaNotifyRecordIds = oaNotifyRecordIds;
	}
	
	public List<NotifyRecordDO> getOaNotifyRecordList() {
		return oaNotifyRecordList;
	}

	public void setOaNotifyRecordList(List<NotifyRecordDO> oaNotifyRecordList) {
		this.oaNotifyRecordList = oaNotifyRecordList;
	}

	public String getOaNotifyRecordNames() {
		return oaNotifyRecordNames;
	}

	public void setOaNotifyRecordNames(String oaNotifyRecordNames) {
		this.oaNotifyRecordNames = oaNotifyRecordNames;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	} 
	
}
