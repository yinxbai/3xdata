package com.txdata.common.domain;

import java.io.Serializable;
import java.util.Map;
import javax.xml.bind.annotation.XmlTransient;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import com.alibaba.fastjson.annotation.JSONField;
import com.txdata.common.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Maps;
import org.springframework.data.annotation.Id;

/**
 * Entity支持类
 * 
 * @author ThinkGem
 * @version 2014-05-16
 */
public abstract class BaseEntity<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 实体编号（唯一标识）
	 */
	@Id
	protected String id;

	// 分页查询每页开始的下标
	@JsonIgnore
	@JSONField(serialize = false)
	private int offset;

	// 每页条数
	@JsonIgnore
	@JSONField(serialize = false)
	private int limit;

	@JsonIgnore
	@JSONField(serialize = false)
	private String pageSize = "15";

	@JsonIgnore
	@JSONField(serialize = false)
	private String pageNo = "1";

	/**
	 * 自定义SQL（SQL标识，SQL内容）
	 */
	protected Map<String, String> sqlMap;

	/**
	 * 是否是新记录（默认：false），调用setIsNewRecord()设置新记录，使用自定义ID。
	 * 设置为true后强制执行插入语句，ID不会自动生成，需从手动传入。
	 */
	@JsonIgnore
	@JSONField(serialize = false)
	protected boolean isNewRecord = false;

	public BaseEntity() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@JsonIgnore
	@XmlTransient
	@JSONField(serialize = false)
	public Map<String, String> getSqlMap() {
		if (sqlMap == null) {
			sqlMap = Maps.newHashMap();
		}
		return sqlMap;
	}

	public void setSqlMap(Map<String, String> sqlMap) {
		this.sqlMap = sqlMap;
	}

	/**
	 * 插入之前执行方法，子类实现
	 */
	public abstract void preInsert();

	/**
	 * 更新之前执行方法，子类实现
	 */
	public abstract void preUpdate();

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	public String getPageNo() {
		return pageNo;
	}

	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	/**
	 * 删除标记（0：正常；1：删除；2：审核；）
	 */
	public static final String DEL_FLAG_NORMAL = "0";
	public static final String DEL_FLAG_DELETE = "1";
	public static final String DEL_FLAG_AUDIT = "2";

	/**
	 * 是否是新记录（默认：false），调用setIsNewRecord()设置新记录，使用自定义ID。
	 * 设置为true后强制执行插入语句，ID不会自动生成，需从手动传入。
	 * 
	 * @return
	 */
	public boolean getIsNewRecord() {
		return isNewRecord || StringUtils.isBlank(getId());
	}

}
