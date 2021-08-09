/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.txdata.common.persistence;

import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.txdata.common.domain.DataEntity;
import com.txdata.common.persistence.proxy.CrudDao;

/**
 * Service基类
 * 
 * @author ThinkGem
 * @version 2014-05-16
 */
@Transactional(readOnly = true)
public abstract class CrudService<D extends CrudDao<T>, T extends DataEntity<T>> {
	/**
	 * 持久层对象
	 */
	@Autowired
	protected D dao;

	/**
	 * 获取单条数据
	 * 
	 * @param id
	 * @return
	 */
	public T get(String id) {
		return dao.get(id);
	}

	/**
	 * 查询列表数据
	 * 
	 * @param entity
	 * @return
	 */
	public List<T> list(Map<String, Object> map) {
		return dao.list(map);
	}

	/**
	 * 查询数据列表总条数;
	 * 
	 * @param map
	 * @return
	 */
	public int count(Map<String, Object> map) {
		return dao.count(map);
	}

	/**
	 * 插入数据
	 * 
	 * @param entity
	 */
	@Transactional(readOnly = false)
	public void insert(T entity) {
		if (StringUtils.isBlank(entity.getId())) {
			entity.preInsert();
		}
		dao.insert(entity);
	}

	/**
	 * 修改数据
	 * 
	 * @param entity
	 */
	@Transactional(readOnly = false)
	public int update(T entity) {
		entity.preUpdate();
		return dao.update(entity);
	}

	/**
	 * 逻辑删除，更新del_flag字段为1
	 * 
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = false)
	public int remove(String id) {
		return dao.remove(id);
	}

	/**
	 * 删除数据（彻底删除数据库的数据）
	 * 
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = false)
	public int delete(String id) {
		return dao.delete(id);
	}

	/**
	 * 获取单条数据
	 * 
	 * @param entity
	 * @deprecated
	 * @return
	 */
	public T get(T entity) {
		return dao.get(entity);
	}

	/**
	 * 保存数据（插入或更新）
	 * 
	 * @param entity
	 */
	@Transactional(readOnly = false)
	public int save(T entity) {
		if (entity.getIsNewRecord()) {
			entity.preInsert();
			return dao.insert(entity);
		} else {
			entity.preUpdate();
			return dao.update(entity);
		}
	}

	/**
	 * 查询列表数据
	 * 
	 * @param entity
	 * @deprecated
	 * @return
	 */
	public List<T> findList(T entity) {
		return dao.findList(entity);
	}

	/**
	 * 删除数据
	 * 
	 * @param entity
	 * @deprecated
	 */
	@Transactional(readOnly = false)
	public void delete(T entity) {
		dao.delete(entity);
	}

}
