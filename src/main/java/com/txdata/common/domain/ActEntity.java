/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.txdata.common.domain;

import java.io.Serializable;
import com.txdata.activiti.domain.ActivitiDO;

/**
 * Activiti Entity类
 * 
 * @author ThinkGem
 * @version 2013-05-28
 */
public abstract class ActEntity<T> extends DataEntity<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	protected ActivitiDO act; // 流程任务对象

	public ActEntity() {
		super();
	}

	public ActEntity(String id) {
		super(id);
	}

	public ActivitiDO getAct() {
		if (act == null) {
			act = new ActivitiDO();
		}
		return act;
	}

	public void setAct(ActivitiDO act) {
		this.act = act;
	}

	/**
	 * 获取流程实例ID
	 * 
	 * @return
	 */
	public String getProcInsId() {
		return this.getAct().getProcInsId();
	}

	/**
	 * 设置流程实例ID
	 * 
	 * @param procInsId
	 */
	public void setProcInsId(String procInsId) {
		this.getAct().setProcInsId(procInsId);
	}
}
