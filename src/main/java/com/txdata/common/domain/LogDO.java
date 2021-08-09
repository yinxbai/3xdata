package com.txdata.common.domain;

public class LogDO extends DataEntity<LogDO> {
	private static final long serialVersionUID = 1L;

	private String userId;

	// 用户名
	private String username;

	// 用户操作
	private String operation;

	// 响应时间
	private Integer time;

	// 请求方法
	private String method;

	// 请求参数
	private String params;

	// ip地址
	private String ip;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username == null ? null : username.trim();
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation == null ? null : operation.trim();
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method == null ? null : method.trim();
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params == null ? null : params.trim();
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip == null ? null : ip.trim();
	}

	@Override
	public String toString() {
		return "LogDO{" + "id=" + id + ", userId=" + userId + ", username='" + username + '\'' + ", operation='"
				+ operation + '\'' + ", time=" + time + ", method='" + method + '\'' + ", params='" + params + '\''
				+ ", ip='" + ip + '\'' + ", createDate=" + createDate + '}';
	}
}