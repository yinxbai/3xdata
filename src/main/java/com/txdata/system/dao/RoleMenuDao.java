package com.txdata.system.dao;

import com.txdata.common.persistence.proxy.CrudDao;
import com.txdata.system.domain.RoleMenuDO;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色与菜单对应关系
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-10-03 11:08:59
 */
@Mapper
public interface RoleMenuDao extends CrudDao<RoleMenuDO> {

	RoleMenuDO get(String id);
	
	List<RoleMenuDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(RoleMenuDO roleMenu);
	
	int update(RoleMenuDO roleMenu);
	
	int remove(String id);
	
	int batchRemove(String[] ids);
	/**
	 * 根据roleId 查询menuId
	 * @param roleId
	 * @return
	 */
	List<String> listMenuIdByRoleId(String roleId);
	/**
	 * 根据roleId删除
	 * @param roleId
	 * @return
	 */
	int removeByRoleId(String roleId);
	/**
	 * 根据menuId删除
	 * @param menuId
	 * @return
	 */
	int removeByMenuId(String menuId);
	
	int batchSave(List<RoleMenuDO> list);
}
