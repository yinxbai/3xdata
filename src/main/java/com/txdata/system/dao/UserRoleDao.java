package com.txdata.system.dao;

import com.txdata.common.persistence.proxy.CrudDao;
import com.txdata.system.domain.UserRoleDO;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户与角色对应关系
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-10-03 11:08:59
 */
@Mapper
public interface UserRoleDao extends CrudDao<UserRoleDO> {

	UserRoleDO get(String id);

	List<UserRoleDO> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	int save(UserRoleDO userRole);

	int update(UserRoleDO userRole);

	int remove(String id);

	int batchRemove(String[] ids);

	List<String> listRoleId(String userId);

	/**
	 * 根据userId删除用户
	 * 
	 * @param userId
	 * @return
	 */
	int removeByUserId(String userId);

	/**
	 * 根据roleId删除用户
	 * 
	 * @param roleId
	 * @return
	 */
	int removeByRoleId(String roleId);

	/**
	 * 根据userId和roleId删除用户
	 * 
	 * @param userId
	 * @param roleId
	 * @return
	 */
	int removeByUserIdRoleId(String userId, String roleId);

	int batchSave(List<UserRoleDO> list);

	/**
	 * 根据userId多选删除
	 * 
	 * @param ids
	 * @return
	 */
	int batchRemoveByUserId(String[] ids);
}
