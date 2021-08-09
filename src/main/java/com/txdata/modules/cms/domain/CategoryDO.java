package com.txdata.modules.cms.domain;

import com.google.common.collect.Lists;
import com.txdata.common.persistence.TreeEntity;

import java.util.List;


/**
 * 栏目表
 * 
 * @author 3xdata
 * @email 3xdata@3xdata.cn
 * @date 2019-06-14 19:36:17
 */
public class CategoryDO extends TreeEntity<CategoryDO> {
	private static final long serialVersionUID = 1L;
	
	private String parentId;
	private String parentName;
	private String image;
	private String href;
	private String imageAnti;

	private List<CategoryDO> childList = Lists.newArrayList(); 	// 拥有子分类列表

	public CategoryDO(){
		super();
		this.sort = 30;
		this.delFlag = DEL_FLAG_NORMAL;
	}

	public CategoryDO(String id){
		this();
		this.id = id;
	}

//	@JsonBackReference
//	@NotNull
	public CategoryDO getParent() {
		return parent;
	}

	public void setParent(CategoryDO parent) {
		this.parent = parent;
	}
	
	public List<CategoryDO> getChildList() {
		return childList;
	}
	
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setChildList(List<CategoryDO> childList) {
		this.childList = childList;
	}
	
	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}
	
	public String getImageAnti() {
		return imageAnti;
	}

	public void setImageAnti(String imageAnti) {
		this.imageAnti = imageAnti;
	}
	
	public static void sortList(List<CategoryDO> list, List<CategoryDO> sourcelist, String parentId){
		for (int i=0; i<sourcelist.size(); i++){
			CategoryDO e = sourcelist.get(i);
			if (e.getParent()!=null && e.getParent().getId()!=null
					&& e.getParent().getId().equals(parentId)){
				list.add(e);
				// 判断是否还有子节点, 有则继续获取子节点
				for (int j=0; j<sourcelist.size(); j++){
					CategoryDO child = sourcelist.get(j);
					if (child.getParent()!=null && child.getParent().getId()!=null
							&& child.getParent().getId().equals(e.getId())){
						sortList(list, sourcelist, e.getId());
						break;
					}
				}
			}
		}
	}
	
//	public String getIds() {
//		return (this.getParentIds() !=null ? this.getParentIds().replaceAll(",", " ") : "") 
//				+ (this.getId() != null ? this.getId() : "");
//	}

	public boolean isRoot(){
		return isRoot(this.id);
	}
	
	public static boolean isRoot(String id){
		return id != null && id.equals("1");
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

}