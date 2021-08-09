package com.txdata.system.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.txdata.system.dao.*;
import com.txdata.system.domain.*;
import com.txdata.system.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.txdata.common.persistence.CrudService;
import com.txdata.common.utils.CacheUtils;

@Service
public class RoleService extends CrudService<RoleDao, Role> {

	public static final String ROLE_ALL_KEY = "\"role_all\"";
	public static final String DEMO_CACHE_NAME = "role";

	@Autowired
	RoleDao roleMapper;
	@Autowired
	RoleMenuDao roleMenuMapper;
	@Autowired
	UserDao userMapper;
	@Autowired
	UserRoleDao userRoleMapper;

	public Role get(String id) {
		Role role = roleMapper.get(id);
		return role;
	}

	public List<UserDO> findUserById(String id) {
		List<UserDO> userList = new ArrayList<UserDO>();
		userList = userMapper.listUserByRole(id);
		return userList;
	}

	/**
	 * 给用户分配角色
	 * 
	 * @param roleId
	 * @param user
	 * @return
	 */
	@Transactional(readOnly = false)
	public UserDO assignUserToRole(String roleId, UserDO user) {
		if (user == null) {
			return null;
		}
		List<String> roleIds = user.getRoleIds();
		if (roleIds.contains(roleId)) {
			return null;
		}
		roleIds.add(roleId);
		user.setRoleIds(roleIds);
		userRoleMapper.removeByUserId(user.getId());
		List<UserRoleDO> list = new ArrayList<>();
		for (String rId : roleIds) {
			UserRoleDO ur = new UserRoleDO();
			ur.setUserId(user.getId());
			ur.setRoleId(rId);
			ur.preInsert();
			list.add(ur);
		}
		if (list.size() > 0) {
			userRoleMapper.batchSave(list);
		}
		// 清除用户角色缓存
		UserUtils.removeCache(UserUtils.CACHE_ROLE_LIST);
		// 清除用户缓存
		CacheUtils.removeAll(UserUtils.USER_CACHE);
		return user;
	}

	/**
	 * 查询当前用户所有角色
	 * 
	 * @return
	 */
	public List<Role> findAllRole() {
		return UserUtils.getRoleList();
	}

    public int countRolesByOfficeId(Map<String, Object> map) {
		return roleMapper.countRolesByOfficeId(map);
    }
}
