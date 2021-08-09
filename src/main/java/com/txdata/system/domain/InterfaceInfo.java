package com.txdata.system.domain;

import java.util.List;
import org.hibernate.validator.constraints.Length;
import com.txdata.common.domain.DataEntity;

/**
 * 接口管理Entity
 * 
 * @author lmh
 * @version 2018-07-24
 */
public class InterfaceInfo extends DataEntity<InterfaceInfo> {

	private static final long serialVersionUID = 1L;
	private String name; // 接口名称
	private String accessUrl; // 访问路径
	private String returnType; // 返回值类型
	private String accessMode; // 访问方式
	private String status; // 是否启用 0、禁用 1、启用
	private String rest1; // 备用字段
	private String interfaceType; // 接口类型
	private List<InterfaceParam> paramList;
	private String params;

	public InterfaceInfo() {
		super();
	}

	public InterfaceInfo(String id) {
		super(id);
	}

	@Length(min = 0, max = 100, message = "name长度必须介于 0 和 100 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Length(min = 0, max = 1024, message = "access_url长度必须介于 0 和 1024 之间")
	public String getAccessUrl() {
		return accessUrl;
	}

	public void setAccessUrl(String accessUrl) {
		this.accessUrl = accessUrl;
	}

	@Length(min = 0, max = 256, message = "return_type长度必须介于 0 和 256 之间")
	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	@Length(min = 0, max = 50, message = "access_mode长度必须介于 0 和 50 之间")
	public String getAccessMode() {
		return accessMode;
	}

	public void setAccessMode(String accessMode) {
		this.accessMode = accessMode;
	}

	@Length(min = 0, max = 1, message = "status长度必须介于 0 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Length(min = 0, max = 256, message = "rest1长度必须介于 0 和 256 之间")
	public String getRest1() {
		return rest1;
	}

	public void setRest1(String rest1) {
		this.rest1 = rest1;
	}

	@Length(min = 1, max = 50, message = "interface_type长度必须介于 1 和 50 之间")
	public String getInterfaceType() {
		return interfaceType;
	}

	public void setInterfaceType(String interfaceType) {
		this.interfaceType = interfaceType;
	}

	public List<InterfaceParam> getParamList() {
		return paramList;
	}

	public void setParamList(List<InterfaceParam> paramList) {
		this.paramList = paramList;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}
}