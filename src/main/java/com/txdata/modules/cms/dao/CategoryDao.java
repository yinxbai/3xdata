package com.txdata.modules.cms.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.txdata.common.persistence.TreeDao;
import com.txdata.modules.cms.domain.CategoryDO;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 栏目表
 * @author 3xdata
 * @email 3xdata@3xdata.cn
 * @date 2019-06-14 19:36:17
 */
@Mapper
public interface CategoryDao extends TreeDao<CategoryDO> {

	CategoryDO get(String id);
	
//	CategoryDO getIdParentId(String id,String parentId);
	
	Page<CategoryDO> list(Page<CategoryDO> page, @Param("entity")Map<String,Object> map);
	
	List<CategoryDO> list(@Param("entity")Map<String,Object> map);
	
	int insert(CategoryDO category);
	
	int update(CategoryDO category);
	
	int remove(String id);
	
	int batchRemove(String[] ids);
	
	/**
	 * 
	 * @Description: 查询是否存在相同的栏目名,只查询delflag为0状态的
	 * @author: 吁新鹏
	 * @date: 2019年7月1日 下午2:13:58
	 */
	List<CategoryDO> validateTitle(@Param("id")String id, @Param("name")String name);
}
