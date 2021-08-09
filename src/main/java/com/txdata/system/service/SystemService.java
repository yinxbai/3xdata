package com.txdata.system.service;

import java.util.Collection;
import java.util.List;
import com.txdata.common.persistence.ServiceException;
import com.txdata.common.redis.shiro.RedisSessionDAO;
import com.txdata.common.service.BaseService;
import com.txdata.common.utils.CacheUtils;
import com.txdata.common.utils.IdGen;
import com.txdata.common.utils.MD5Utils;
import com.txdata.common.utils.ShiroUtils;
import com.txdata.common.utils.StringUtils;
import com.txdata.system.dao.RoleDao;
import com.txdata.system.dao.UserDao;
import com.txdata.system.domain.Role;
import com.txdata.system.domain.UserDO;
import com.txdata.system.utils.UserUtils;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统管理，安全相关实体的管理类,包括用户、角色、菜单.
 * 
 * @author ThinkGem
 * @version 2013-12-05
 */
@Service
@Transactional(readOnly = true)
public class SystemService extends BaseService {

	@Autowired
	private UserDao userDao;
	@Autowired
	private RedisSessionDAO sessionDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private IdentityService identityService;

	/**
	 * 获取用户
	 * 
	 * @param id
	 * @return
	 */
	public UserDO getUser(String id) {
		return UserUtils.get(id);
	}

	@Transactional(readOnly = false)
	public void saveUser(UserDO user) {
		if (StringUtils.isBlank(user.getId())) {
			user.preInsert();
			user.setStatus(1);
			userDao.save(user);
		} else {
			// 清除原用户机构用户缓存
			UserDO oldUser = userDao.get(user.getId());
			Role role = new Role();
			role.setUserId(user.getId());
			oldUser.setRoleList(roleDao.findList(role));
			if (oldUser.getOffice() != null && oldUser.getOffice().getId() != null) {
				CacheUtils.remove(UserUtils.USER_CACHE,
						UserUtils.USER_CACHE_LIST_BY_OFFICE_ID_ + oldUser.getOffice().getId());
			}
			// 更新用户数据
			user.preUpdate();
			userDao.update(user);
		}
		if (StringUtils.isNotBlank(user.getId())) {
			// 更新用户与角色关联
			userDao.deleteUserRole(user);
			if (user.getRoleList() != null && user.getRoleList().size() > 0) {
				for (Role role : user.getRoleList()) {
					role.setUserRoleId(IdGen.uuid());
				}
				userDao.insertUserRole(user);
			}
			// 将当前用户同步到Activiti
			saveActivitiUser(user);
			// 清除用户缓存
			UserUtils.clearCache(user);
			// 剔除该用户所有的session
			Collection<Session> sessions = sessionDao.getActiveSessions(false, user.getId(), null);
			for (Session session : sessions) {
				// session.stop();
				sessionDao.delete(session);
			}
		}
	}

	/**
	 * 修改用户信息
	 * 
	 * @param user
	 */
	@Transactional(readOnly = false)
	public void updateUserInfo(UserDO user) {
		user.preUpdate();
		userDao.updateUserInfo(user);
		// 清除用户缓存
		UserUtils.clearCache(user);
	}

	/**
	 * 修改用户密码
	 * 
	 * @param id
	 * @param loginName
	 * @param newPassword
	 */
	@Transactional(readOnly = false)
	public void updatePasswordById(String id, String loginName, String newPassword) {
		UserDO user = new UserDO(id);
		user.setPassword(MD5Utils.getMD5(newPassword));
		userDao.updatePasswordById(user);
		user.setUsername(loginName);
		// 清除用户缓存
		UserUtils.clearCache(user);
	}

	/**
	 * 生成安全的密码，生成随机的16位salt并经过1024次 sha-1 hash
	 */
	public static String entryptPassword(String plainPassword) {
		/*
		 * String plain = Encodes.unescapeHtml(plainPassword); byte[] salt =
		 * Digests.generateSalt(SALT_SIZE); byte[] hashPassword =
		 * Digests.sha1(plain.getBytes(), salt, HASH_INTERATIONS);
		 */
		return MD5Utils.getMD5(plainPassword);// Encodes.encodeHex(salt)+Encodes.encodeHex(hashPassword);
	}

	/**
	 * 验证密码
	 * 
	 * @param plainPassword
	 *            明文密码
	 * @param password
	 *            密文密码
	 * @return 验证成功返回true
	 */
	public static boolean validatePassword(String plainPassword, String password) {
		/*
		 * String plain = Encodes.unescapeHtml(plainPassword); byte[] salt =
		 * Encodes.decodeHex(password.substring(0,16)); byte[] hashPassword =
		 * Digests.sha1(plain.getBytes(), salt, HASH_INTERATIONS);
		 * Encodes.encodeHex(salt)+Encodes.encodeHex(hashPassword)
		 */
		return password.equals(MD5Utils.getMD5(plainPassword));
	}

	public Role getRole(String id) {
		return roleDao.get(id);
	}

	public Role getRoleByName(String name) {
		Role r = new Role();
		r.setName(name);
		return roleDao.getByName(r);
	}

	public Role getRoleByEnname(String enname) {
		Role r = new Role();
		r.setEnname(enname);
		return roleDao.getByEnname(r);
	}

	public List<Role> findAllRole() {
		return UserUtils.getRoleList();
	}

	@Transactional(readOnly = false)
	public void saveRole(Role role) {
		if (StringUtils.isBlank(role.getId())) {
			role.preInsert();
			roleDao.insert(role);
			// 同步到Activiti
			saveActivitiGroup(role);
		} else {
			role.preUpdate();
			roleDao.update(role);
		}
		// 更新角色与菜单关联
		roleDao.deleteRoleMenu(role);
		if (role.getMenuList().size() > 0) {
			roleDao.insertRoleMenu(role);
		}
		// 更新角色与部门关联
		roleDao.deleteRoleOffice(role);
		if (role.getOfficeList().size() > 0) {
			roleDao.insertRoleOffice(role);
		}
		// 同步到Activiti
		saveActivitiGroup(role);
		// 清除用户角色缓存
		UserUtils.removeCache(UserUtils.CACHE_ROLE_LIST);
		// 清除用户缓存
//		CacheUtils.removeAll(UserUtils.USER_CACHE);
		// // 清除权限缓存
		// systemRealm.clearAllCachedAuthorizationInfo();
	}

	@Transactional(readOnly = false)
	public void deleteRole(Role role) {
		roleDao.delete(role);
		// 同步到Activiti
		deleteActivitiGroup(role);
		// 清除用户角色缓存
		UserUtils.removeCache(UserUtils.CACHE_ROLE_LIST);
		// // 清除权限缓存
		// systemRealm.clearAllCachedAuthorizationInfo();
	}

	@Transactional(readOnly = false)
	public Boolean outUserInRole(Role role, UserDO user) {
		List<Role> roles = user.getRoleList();
		for (Role e : roles) {
			if (e.getId().equals(role.getId())) {
				roles.remove(e);
				saveUser(user);
				return true;
			}
		}
		return false;
	}
	//
	// @Transactional(readOnly = false)
	// public User assignUserToRole(Role role, User user) {
	// if (user == null){
	// return null;
	// }
	// List<String> roleIds = user.getRoleIdList();
	// if (roleIds.contains(role.getId())) {
	// return null;
	// }
	// user.getRoleList().add(role);
	// saveUser(user);
	// return user;
	// }

	/**
	 * 验证名称是否重复
	 * 
	 * @param oldName
	 * @param name
	 * @return
	 */
	public boolean checkName(String oldName, String name) {
		boolean flag = false;
		if (name != null && name.equals(oldName)) {
			flag = true;
		} else if (name != null && getRoleByName(name) == null) {
			flag = true;
		}

		return flag;
	}

	/**
	 * 验证英文名称是否重复
	 * 
	 * @param oldEnname
	 * @param enname
	 * @return
	 */
	public boolean checkEnname(String oldEnname, String enname) {
		boolean flag = false;
		if (enname != null && enname.equals(oldEnname)) {
			flag = true;
		} else if (enname != null && getRoleByEnname(enname) == null) {
			flag = true;
		}

		return flag;
	}

	// ///////////////// Synchronized to the Activiti //////////////////
	//
	// // 已废弃，同步见：ActGroupEntityServiceFactory.java、ActUserEntityServiceFactory.java

	private void saveActivitiGroup(Role role) {
		// if (!Global.isSynActivitiIndetity()){
		// return;
		// }
		String groupId = role.getEnname();
		// 如果修改了英文名，则删除原Activiti角色
		if (StringUtils.isNotBlank(role.getOldEnname()) && !role.getOldEnname().equals(role.getEnname())) {
			identityService.deleteGroup(role.getOldEnname());
		}
		Group group = identityService.createGroupQuery().groupId(groupId).singleResult();
		if (group == null) {
			group = identityService.newGroup(groupId);
		}
		group.setName(role.getName());
		group.setType(role.getRoleType());
		identityService.saveGroup(group);
		// 删除用户与用户组关系
		List<org.activiti.engine.identity.User> activitiUserList = identityService.createUserQuery()
				.memberOfGroup(groupId).list();
		for (org.activiti.engine.identity.User activitiUser : activitiUserList) {
			identityService.deleteMembership(activitiUser.getId(), groupId);
		}
		// 创建用户与用户组关系
		List<UserDO> userList = findUser(new UserDO(new Role(role.getId())));
		for (UserDO e : userList) {
			String userId = e.getUsername();// ObjectUtils.toString(user.getId());
			// 如果该用户不存在，则创建一个
			org.activiti.engine.identity.User activitiUser = identityService.createUserQuery().userId(userId)
					.singleResult();
			if (activitiUser == null) {
				activitiUser = identityService.newUser(userId);
				activitiUser.setFirstName(e.getName());
				activitiUser.setLastName(StringUtils.EMPTY);
				activitiUser.setEmail(e.getEmail());
				activitiUser.setPassword(StringUtils.EMPTY);
				identityService.saveUser(activitiUser);
			}
			identityService.createMembership(userId, groupId);
		}
	}

	/**
	 * 从工作流中删除用组
	 * 
	 * @param role
	 */
	public void deleteActivitiGroup(Role role) {
		// if (!Global.isSynActivitiIndetity()){
		// return;
		// }
		if (role != null) {
			String groupId = role.getEnname();
			identityService.deleteGroup(groupId);
		}
	}

	/**
	 * 同步用户到工作流
	 * 
	 * @param user
	 */
	private void saveActivitiUser(UserDO user) {
		// if (!Global.isSynActivitiIndetity()){
		// return;
		// }
		String userId = user.getUsername();// ObjectUtils.toString(user.getId());
		org.activiti.engine.identity.User activitiUser = identityService.createUserQuery().userId(userId)
				.singleResult();
		if (activitiUser == null) {
			activitiUser = identityService.newUser(userId);
		}
		activitiUser.setFirstName(user.getName());
		activitiUser.setLastName(StringUtils.EMPTY);
		activitiUser.setEmail(user.getEmail());
		activitiUser.setPassword(StringUtils.EMPTY);
		identityService.saveUser(activitiUser);
		// 删除用户与用户组关系
		List<Group> activitiGroups = identityService.createGroupQuery().groupMember(userId).list();
		for (Group group : activitiGroups) {
			identityService.deleteMembership(userId, group.getId());
		}
		// 创建用户与用户组关系
		for (Role role : user.getRoleList()) {
			String groupId = role.getEnname();
			// 如果该用户组不存在，则创建一个
			Group group = identityService.createGroupQuery().groupId(groupId).singleResult();
			if (group == null) {
				group = identityService.newGroup(groupId);
				group.setName(role.getName());
				group.setType(role.getRoleType());
				identityService.saveGroup(group);
			}
			identityService.createMembership(userId, role.getEnname());
		}
	}

	private void deleteActivitiUser(UserDO user) {
		// if (!Global.isSynActivitiIndetity()){
		// return;
		// }
		if (user != null) {
			String userId = user.getLoginName();// ObjectUtils.toString(user.getId());
			identityService.deleteUser(userId);
		}
	}

	/**
	 * 校验角色名称
	 * 
	 * @param eName
	 * @return
	 */
	public boolean validateEname(String eName) {
		Role role = new Role();
		role.setEnname(eName);
		List<Role> list = roleDao.findListByEnName(role);
		if (list.size() > 0) {
			return false;
		}
		return true;
	}

	/**
	 * 查询某一角色下的所有用户
	 * 
	 * @param role
	 * @return
	 */
	public List<UserDO> findUserByEnName(Role role) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		String id = roleDao.findIdByEnName(role);
		UserDO user = new UserDO(new Role(id));
		user.getSqlMap().put("dsf", dataScopeFilter(ShiroUtils.getUser(), "o", "a"));
		List<UserDO> list = userDao.findUser(user);
		return list;
	}

	/**
	 * 无分页查询人员列表
	 * 
	 * @param user
	 * @return
	 */
	public List<UserDO> findUser(UserDO user) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		user.getSqlMap().put("dsf", dataScopeFilter(ShiroUtils.getUser(), "o", "a"));
		List<UserDO> list = userDao.findUserList(user);
		return list;
	}
}