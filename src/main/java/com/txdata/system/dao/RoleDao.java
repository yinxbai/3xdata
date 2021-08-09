package com.txdata.system.dao;

import com.txdata.common.persistence.proxy.CrudDao;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

import com.txdata.system.domain.Role;
import org.apache.ibatis.annotations.Param;

/**
 * 角色管理DAO接口（与前端对接已弃用）
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-10-02 20:24:47
 */
@Mapper
public interface RoleDao extends CrudDao<Role> {

	/**
	 * 根据用户名或者用户id查询Role
	 * 
	 * @param map
	 * @return
	 */
	List<Role> findByUser(Map<String, Object> map);

	/**
	 * 根据名称获取对象
	 * 
	 * @param role
	 * @return
	 */
	Role getByName(Role role);

	/**
	 * 根据英文名称获取对象
	 * 
	 * @param role
	 * @return
	 */
	Role getByEnname(Role role);

	/**
	 * 根据英文名称获取id
	 * 
	 * @param role
	 * @return
	 */
	String findIdByEnName(Role role);

	/**
	 * 维护角色与菜单权限关系
	 * 
	 * @param role
	 * @return
	 */
	public int deleteRoleMenu(Role role);

	/**
	 * 保存角色——菜单
	 * 
	 * @param role
	 * @return
	 */
	int insertRoleMenu(Role role);

	/**
	 * 维护角色与公司部门关系
	 * 
	 * @param role
	 * @return
	 */
	int deleteRoleOffice(Role role);

	/**
	 * 保存角色——部门
	 * 
	 * @param role
	 * @return
	 */
	int insertRoleOffice(Role role);

	/**
	 * 根据英文名称查询
	 * 
	 * @param role
	 * @return
	 */
	List<Role> findListByEnName(Role role);

	int countRolesByOfficeId(@Param("entity") Map<String, Object> map);
}
