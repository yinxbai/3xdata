package com.txdata.common.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * tree TODO <br>
 * 
 * @author kangxu2 2017-1-7
 * 
 */
public class Tree<T> implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 节点ID
	 */
	private String id;

	/**
	 * 显示节点文本
	 */
	private String text;

	/**
	 * 节点状态，open closed
	 */
	private Map<String, Object> state;

	/**
	 * 节点是否被选中 true false
	 */
	private boolean checked = false;

	/**
	 * 节点属性
	 */
	private Map<String, Object> attributes;

	/**
	 * 节点的子节点
	 */
	private List<Tree<T>> children = new ArrayList<Tree<T>>();

	/**
	 * 父ID
	 */
	private String parentId;

	private String parentName;
	/**
	 * 是否有父节点
	 */
	private boolean hasParent = false;

	/**
	 * 是否有子节点
	 */
	private boolean hasChildren = false;
	/**
	 * 节点层级
	 */
	private int level;
	
	/**
	 * 节点类型
	 */
	private String type;
	
	/**
	 * 编号
	 */
	private String code;

	// 排序
	private Integer sort;
	
	private String image;
	
	private String imageAnti;
	
	private String href;
	


	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createDate;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Map<String, Object> getState() {
		return state;
	}

	public void setState(Map<String, Object> state) {
		this.state = state;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public List<Tree<T>> getChildren() {
		return children;
	}

	public void setChildren(List<Tree<T>> children) {
		this.children = children;
	}

	public boolean isHasParent() {
		return hasParent;
	}

	public void setHasParent(boolean isParent) {
		this.hasParent = isParent;
	}

	public boolean isHasChildren() {
		return hasChildren;
	}

	public void setChildren(boolean isChildren) {
		this.hasChildren = isChildren;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}
	
	public Tree(String id, String text, Map<String, Object> state, boolean checked, Map<String, Object> attributes,
			List<Tree<T>> children, boolean isParent, boolean isChildren, String parentID ,String parentName) {
		super();
		this.id = id;
		this.text = text;
		this.state = state;
		this.checked = checked;
		this.attributes = attributes;
		this.children = children;
		this.hasParent = isParent;
		this.hasChildren = isChildren;
		this.parentId = parentID;
		this.parentName = parentName;
	}

	public Tree() {
		super();
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	
	public String getImageAnti() {
		return imageAnti;
	}

	public void setImageAnti(String imageAnti) {
		this.imageAnti = imageAnti;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}