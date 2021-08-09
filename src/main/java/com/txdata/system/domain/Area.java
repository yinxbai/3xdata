package com.txdata.system.domain;

import org.hibernate.validator.constraints.Length;
import com.txdata.common.persistence.TreeEntity;

/**
 * 区域Entity
 * 
 * @author ThinkGem
 * @version 2013-05-15
 */
public class Area extends TreeEntity<Area> {

	private static final long serialVersionUID = 1L;
//	private Area parent;	// 父级编号
//	private String parentIds; // 所有父级编号
	private String code; // 区域编码
//	private String name; 	// 区域名称
//	private Integer sort;		// 排序
	private String type; // 区域类型（1：国家；2：省份、直辖市；3：地市；4：区县）

	public Area(AjaxArea ajaxArea) {
		super(ajaxArea.getId());
		this.name = ajaxArea.getName();
		this.code = ajaxArea.getCode();
		this.type = ajaxArea.getType();
		Area parent = new Area(ajaxArea.getParentId());
		this.parent = parent;
//        this.parentId = ajaxArea.getParentId();
		this.remarks = ajaxArea.getRemarks();
		this.delFlag = DEL_FLAG_NORMAL;
		this.sort = 30;
	}

	public Area() {
		super();
		this.sort = 30;
	}

	public Area(String id) {
		super(id);
	}

	public Area(String id, String code) {
		super(id);
		this.code = code;
	}

	public Area(String id, String name, String code) {
		this.id = id;
		this.name = name;
		this.code = code;
	}

//	@JsonBackReference
//	@NotNull
	public Area getParent() {
		return parent;
	}

	public void setParent(Area parent) {
		this.parent = parent;
	}

	@Length(min = 1, max = 1, message = "类型必填！")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Length(min = 0, max = 100)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

//	public String getParentIds() {
//		return parentIds;
//	}
//
//	public void setParentIds(String parentIds) {
//		this.parentIds = parentIds;
//	}
//
//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}
//
//	public Integer getSort() {
//		return sort;
//	}
//
//	public void setSort(Integer sort) {
//		this.sort = sort;
//	}

	@Override
	public String toString() {
		return name;
	}
}