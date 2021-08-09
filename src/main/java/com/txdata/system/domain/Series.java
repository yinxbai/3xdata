package com.txdata.system.domain;

import java.io.Serializable;
import java.util.List;

/**
 * 图标数据所需实体类
 * 
 * @author huangmk
 *
 */
public class Series implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private List<Object> data;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Object> getData() {
		return data;
	}

	public void setData(List<Object> data) {
		this.data = data;
	}

}
