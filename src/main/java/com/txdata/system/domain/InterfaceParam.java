package com.txdata.system.domain;

import com.txdata.common.domain.DataEntity;

/**
 * 接口参数Entity
 * 
 * @author lmh
 * @version 2018-07-24
 */
public class InterfaceParam extends DataEntity<InterfaceInfo> {

	private static final long serialVersionUID = 1L;

	private String id;
	private String interfaceId; // 关联接口id
	private String paramName; // 参数名称
	private String defValue; // 默认值
	private String remark; // 参数说明
	private String remarks;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInterfaceId() {
		return interfaceId;
	}

	public void setInterfaceId(String interfaceId) {
		this.interfaceId = interfaceId;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getDefValue() {
		return defValue;
	}

	public void setDefValue(String defValue) {
		this.defValue = defValue;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
