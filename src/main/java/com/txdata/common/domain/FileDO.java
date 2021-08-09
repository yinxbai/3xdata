package com.txdata.common.domain;

import java.util.Date;

/**
 * 文件上传
 *
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-09-19 16:02:20
 */
public class FileDO extends DataEntity<FileDO> {
	private static final long serialVersionUID = 1L;

	// 文件类型
	private String type;

	// URL地址
	private String url;

	public FileDO() {
		super();
	}

	public FileDO(String type, String url, Date createDate) {
		super();
		this.type = type;
		this.url = url;
		this.createDate = createDate;
	}

	/**
	 * 设置：文件类型
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 获取：文件类型
	 */
	public String getType() {
		return type;
	}

	/**
	 * 设置：URL地址
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 获取：URL地址
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 获取：创建时间
	 */
	public Date getCreateDate() {
		return createDate;
	}

	@Override
	public String toString() {
		return "FileDO{" + "id=" + id + ", type=" + type + ", url='" + url + '\'' + ", createDate=" + createDate + '}';
	}
}
