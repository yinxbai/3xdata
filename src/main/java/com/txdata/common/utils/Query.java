package com.txdata.common.utils;

import java.util.LinkedHashMap;
import java.util.Map;

import com.txdata.common.domain.BaseEntity;

/**
 * 查询参数
 */
public class Query extends LinkedHashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	// 偏移量（列表分页从哪个位置开始）
	private int offset;
	// 每页条数
	private int limit;

	private int pageNo;

	private int pageSize;

	public Query() {
		super();
	}

//	public Query(BaseEntity<T> entity) {
//		this.pageNo = entity.getPageNo();
//		this.pageSize = entity.getPageSize();
//		this.put("offset",(pageNo-1)*pageSize>0 ? (pageNo-1)*pageSize : 0);
//		this.put("page", pageNo);
//		this.put("limit", pageSize);
//		entity.setLimit(pageSize);
//		entity.setOffset((pageNo-1)*pageSize>0 ? (pageNo-1)*pageSize : 0);
//	}

	public Query(Map<String, Object> params) {
		this.putAll(params);
		// 分页参数
		if (params.get("pageNo") != null && !"".equals(params.get("pageNo").toString())) {
			this.pageNo = Integer.parseInt(params.get("pageNo").toString());
		} else {
			this.pageNo = 1;
		}

		if (params.get("pageSize") != null && !"".equals(params.get("pageSize").toString())) {
			this.pageSize = Integer.parseInt(params.get("pageSize").toString());
		} else {
			this.pageSize = 10;
		}
		if(params.get("name") != null && !"".equals(params.get("name").toString())) {
			String name = params.get("name").toString();
			name = name.replace("\\", "\\\\");
			this.put("name", name);
		}
		if(params.get("title") != null && !"".equals(params.get("title").toString())) {
			String title = params.get("title").toString();
			title = title.replace("\\", "\\\\");
			this.put("title", title);
		}
		if(params.get("ruleName") != null && !"".equals(params.get("ruleName").toString())) {
			String title = params.get("ruleName").toString();
			title = title.replace("\\", "\\\\");
			this.put("ruleName", title);
		}
		this.put("offset", (pageNo - 1) * pageSize > 0 ? (pageNo - 1) * pageSize : 0);
		this.put("limit", pageSize);
		this.offset = (pageNo - 1) * pageSize > 0 ? (pageNo - 1) * pageSize : 0;
		this.limit = pageSize;
	}

	public static BaseEntity<?> entitySet(BaseEntity<?> entity) {
		String pageNo = entity.getPageNo();
		String pageSize = entity.getPageSize();
		if (StringUtils.isBlank(pageNo) || "-1".equals(pageNo)) {
			pageNo = "1";
		}
		if (StringUtils.isBlank(pageSize)) {
			pageSize = "15";
		}
		int pSize = Integer.valueOf(pageSize);
		int pNo = Integer.valueOf(pageNo);
		entity.setLimit(pSize);
		entity.setOffset((pNo - 1) * pSize > 0 ? (pNo - 1) * pSize : 0);
		return entity;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.put("offset", offset);
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

}
