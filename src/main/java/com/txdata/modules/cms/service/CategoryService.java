package com.txdata.modules.cms.service;

import java.util.*;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.txdata.common.domain.Tree;
import com.txdata.common.persistence.TreeService;
import com.txdata.common.utils.BuildTree;
import com.txdata.modules.cms.dao.CategoryDao;
import com.txdata.modules.cms.domain.CategoryDO;

/**
 * 栏目表
 * 
 * @author 3xdata
 * @email 3xdata@3xdata.cn
 * @date 2019-06-14 19:36:17
 */
 @Service
public class CategoryService extends TreeService<CategoryDao, CategoryDO> {

    @Autowired
    private CategoryDao categoryDao;
    
    public CategoryDO get(String id){
        return categoryDao.get(id);
    }
    
    public Page<CategoryDO> page(Page<CategoryDO> page, Map<String, Object> map){
        return categoryDao.list(page, map);
    }
    
    public List<CategoryDO> list(Map<String, Object> map){
        return categoryDao.list(map);
    }
    
    @Transactional(readOnly=false)
    public int save(CategoryDO category){
       return super.save(category);
    }
    
    @Transactional(readOnly=false)
    public int remove(String id){
        return categoryDao.remove(id);
    }
    
    @Transactional(readOnly=false)
    public int batchRemove(String[] ids){
        return categoryDao.batchRemove(ids);
    }
    
    /**
	 * 通过编号获取栏目列表
	 */
	public List<CategoryDO> findByIds(String ids) {
		List<CategoryDO> list = Lists.newArrayList();
		String[] idss = StringUtils.split(ids,",");
		if (idss.length>0){
			for(String id : idss){
				CategoryDO e = categoryDao.get(id);
				if(null != e){
					list.add(e);
				}
			}
		}
		return list;
	}
    
    /**
	 * 
	 * @Description: 栏目树
	 *
	 * @param: 参数描述
	 * @return: 返回结果描述
	 * @throws: 异常描述
	 *
	 * @author: czy
	 * @date: 2019年6月17日 下午12:00:09
	 */
	public List<Tree<CategoryDO>> categoryTree(List<CategoryDO> list) {
		List<Tree<CategoryDO>> trees = new ArrayList<Tree<CategoryDO>>();
		for (CategoryDO category : list) {
			Tree<CategoryDO> tree = new Tree<CategoryDO>();
			tree.setId(category.getId());
			tree.setHref(category.getHref());
			tree.setText(category.getName());
			tree.setParentId(category.getParentId());
			tree.setImage(category.getImage());
			tree.setImageAnti(category.getImageAnti());
			tree.setParentName(category.getParentName());
			Map<String, Object> state = new HashMap<>(16);
			state.put("opened", true);
			tree.setState(state);
			tree.setSort(category.getSort());
			tree.setCreateDate(category.getCreateDate());
			trees.add(tree);
		}
		List<Tree<CategoryDO>> t = BuildTree.buildList(trees, "1");
		return t;
	}
    
	/**
	 * 校验栏目名称唯一性,true表示校验通过
	 * @Description: 该方法的功能描述
	 * @param: 参数描述
	 * @return: 返回结果描述
	 * @throws: 异常描述
	 * @author: wanjm
	 * @date: 2019年8月15日 下午2:44:30
	 */
	public boolean validateTitle(String id, String name, String parentId) {
    	if (StringUtils.isNotBlank(id)) {
			List<CategoryDO> category = categoryDao.validateTitle(id, name);
			if (category.isEmpty()) {
				return true;
			}
		} else {
			CategoryDO category = new CategoryDO();
			category.setName(name);
			List<CategoryDO> categoryList = categoryDao.findList(category);
			for(CategoryDO c : categoryList) {
				if(Objects.equals(c.getParentId(), parentId)) {
					return false;
				}
			}
		}
		return true;
    }
}
