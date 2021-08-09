package com.txdata.system.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.txdata.system.dao.*;
import com.txdata.system.domain.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import com.txdata.common.utils.CacheUtils;
import com.txdata.common.utils.ShiroUtils;
import com.txdata.common.utils.SpringContextHolder;
import com.txdata.common.utils.StringUtils;
import com.txdata.system.dao.AreaDao;
import com.txdata.system.dao.OfficeDao;
import com.txdata.system.dao.RoleDao;
import com.txdata.system.dao.UserDao;
import com.txdata.system.domain.AjaxMenu;
import com.txdata.system.domain.Area;
import com.txdata.system.domain.Menu;
import com.txdata.system.domain.Office;
import com.txdata.system.domain.UserDO;

public class UserUtils {

	private static MenuDao menuDao = SpringContextHolder.getBean(MenuDao.class);
	private static RoleDao roleDao = SpringContextHolder.getBean(RoleDao.class);
	private static UserDao userDao = SpringContextHolder.getBean(UserDao.class);
	private static AreaDao areaDao = SpringContextHolder.getBean(AreaDao.class);
	private static OfficeDao officeDao = SpringContextHolder.getBean(OfficeDao.class);

	public static final String USER_CACHE = "userCache";
	public static final String USER_CACHE_ID_ = "id_";
	public static final String USER_CACHE_LOGIN_NAME_ = "ln";
	public static final String USER_CACHE_LIST_BY_OFFICE_ID_ = "oid_";
	public static final String CACHE_AUTH_INFO = "authInfo";
	public static final String CACHE_ROLE_LIST = "roleApiList";
	public static final String CACHE_MENU_LIST = "menuList";
	public static final String CACHE_AREA_LIST = "areaList";
	public static final String CACHE_OFFICE_LIST = "officeList";
	public static final String CACHE_OFFICE_ALL_LIST = "officeAllList";
	public static final String CACHE_MENU_LIST_JSON = "menuListJson";
	public static final String CACHE_OFFICE_TREE = "officeTree";
	public static final String CACHE_OFFICE_LEVEL_ONE_LIST = "officeLevelOneList";
	public static final String CACHE_AREA_ROOT_TREE = "areaRootTree";
	public static final String CACHE_OFFICE_USER_TREE = "officeUserTree";

	/**
	 * 根据ID获取用户
	 * 
	 * @param id
	 * @return 取不到返回null
	 */
	public static UserDO get(String id) {
		UserDO user = (UserDO)CacheUtils.get(USER_CACHE, USER_CACHE_ID_ + id);
		if (user == null) {
			user = userDao.get(id);
//			return null;
			Role role = new Role();
			role.setUserId(user.getId());
			role.setUserName(user.getUsername());
			user.setRoleList(roleDao.findList(role));
			CacheUtils.put(USER_CACHE, USER_CACHE_ID_ + user.getId(), user);
//			CacheUtils.put(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getUsername(), user);
		}
		return user;
	}

	/**
	 * 获取当前用户
	 * 
	 * @return 取不到返回 new User()
	 */
	public static UserDO getUser() {
		UserDO principal = getPrincipal();
		if (principal != null) {
			UserDO user = get(principal.getId());
			if (user != null) {
				return user;
			}
			return new UserDO();
		}
		// 如果没有登录，则返回实例化空的User对象。
		return new UserDO();
	}

	/**
	 * 获取当前登录者对象
	 */
	public static UserDO getPrincipal() {
		try {
			Subject subject = SecurityUtils.getSubject();
			UserDO principal = (UserDO) subject.getPrincipal();
			if (principal != null) {
				return principal;
			}
		} catch (UnavailableSecurityManagerException e) {

		} catch (InvalidSessionException e) {

		}
		return null;
	}

	/**
	 * 获取当前用户授权的区域
	 * 
	 * @return
	 */
	public static List<Area> getAreaList() {
		@SuppressWarnings("unchecked")
		List<Area> areaList = (List<Area>) getCache(CACHE_AREA_LIST);
		if (areaList == null) {
			areaList = areaDao.findAllList(new Area());
			putCache(CACHE_AREA_LIST, areaList);
		}
		return areaList;
	}

	/**
	 * 获取当前用户有权限访问的部门
	 * 
	 * @return
	 */
	public static List<Office> getOfficeList() {
		@SuppressWarnings("unchecked")
		List<Office> officeList = (List<Office>) getCache(CACHE_OFFICE_LIST);
		if (officeList == null) {
			officeList = officeDao.findAllList(new Office());
			putCache(CACHE_OFFICE_LIST, officeList);
		}
		return officeList;
	}

	/**
	 * 获取所有部门
	 * 
	 * @return
	 */
	public static List<Office> getOfficeAllList() {
		@SuppressWarnings("unchecked")
		List<Office> officeList = (List<Office>) CacheUtils.get(CACHE_OFFICE_ALL_LIST);
		if (officeList == null) {
			officeList = officeDao.findAllList(new Office());
			CacheUtils.put(CACHE_OFFICE_ALL_LIST, officeList);
		}
		return officeList;
	}

	public static Object getCache(String key) {
		return getCache(key, null);
	}

	public static Object getCache(String key, Object defaultValue) {
		// Object obj = getCacheMap().get(key);
		Object obj = getSession().getAttribute(key);
		return obj == null ? defaultValue : obj;
	}

	public static void putCache(String key, Object value) {
		// getCacheMap().put(key, value);
		getSession().setAttribute(key, value);
	}

	/**
	 * 移除session
	 * 
	 * @param key
	 */
	public static void removeCache(String key) {
		// getCacheMap().remove(key);
		getSession().removeAttribute(key);
	}

	public static Session getSession() {
		try {
			Subject subject = SecurityUtils.getSubject();
			Session session = subject.getSession(false);
			if (session == null) {
				session = subject.getSession();
			}
			if (session != null) {
				return session;
			}
			// subject.logout();
		} catch (InvalidSessionException e) {

		}
		return null;
	}

	/**
	 * 清除指定用户缓存
	 * 
	 * @param user
	 */
	public static void clearCache(UserDO user) {
		CacheUtils.remove(USER_CACHE, USER_CACHE_ID_ + user.getId());
		CacheUtils.remove(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getUsername());
		CacheUtils.remove(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getOldLoginName());
		/*
		 * if (user.getOffice() != null && user.getOffice().getId() != null) {
		 * CacheUtils.remove(USER_CACHE, USER_CACHE_LIST_BY_OFFICE_ID_ +
		 * user.getOffice().getId()); }
		 */
	}

	/**
	 * 清除当前用户缓存
	 */
	public static void clearCache() {
		removeCache(CACHE_AUTH_INFO);
		removeCache(CACHE_ROLE_LIST);
		removeCache(CACHE_MENU_LIST);
		removeCache(CACHE_AREA_LIST);
		removeCache(CACHE_OFFICE_LIST);
		removeCache(CACHE_OFFICE_ALL_LIST);
		removeCache(CACHE_MENU_LIST_JSON);
		removeCache(CACHE_OFFICE_TREE);
		removeCache(CACHE_OFFICE_USER_TREE);
		UserUtils.clearCache(getUser());
	}

	/**
	 * 得到菜单树
	 * 
	 * @param isShowHide
	 * @return
	 */
	public static List<AjaxMenu> getMenuTree(boolean isShowHide) {
		// 原始的数据
		List<Menu> rootMenu = getMenuList();
		// 最后的结果
		List<AjaxMenu> menuList = new ArrayList<AjaxMenu>();
		// 先找到所有的一级菜单
		for (int i = 0; i < rootMenu.size(); i++) {
			// 一级菜单parentId为0
			if ("0".equals(rootMenu.get(i).getParentId())) {
				Menu menu = rootMenu.get(i);
				AjaxMenu ajaxMenu = new AjaxMenu(menu, true);
				menuList.add(ajaxMenu);
			}
		}
		// 为一级菜单设置子菜单，getChild是递归调用的
		for (AjaxMenu menu : menuList) {
			menu.setChildren(getMenuChild(menu.getId(), rootMenu, isShowHide));
			if (StringUtils.isBlank(menu.getIndex())) {
				menu.setIndex(menu.getId());
			}
		}
		return menuList;
	}

	/**
	 * 获取当前用户授权菜单
	 * 
	 * @return
	 */
	public static List<Menu> getMenuList() {
		@SuppressWarnings("unchecked")
		List<Menu> menuList = new ArrayList<Menu>();
		if (menuList == null || menuList.size() == 0) {
			UserDO user = ShiroUtils.getUser();
			if (user.isAdmin()) {
				menuList = menuDao.findAllList(new Menu());
			} else {
				Menu m = new Menu();
				m.setUserId(user.getId());
				menuList = menuDao.findByUserId(m);
			}
		}
		return menuList;
	}

	private static List<AjaxMenu> getMenuChild(String id, List<Menu> rootMenu, boolean isShowHide) {
		// 子菜单
		List<AjaxMenu> childList = new ArrayList<>();
		for (Menu menu : rootMenu) {
			if (!isShowHide && "0".equals(menu.getIsShow())) { // 过滤掉不显示的菜单
				continue;
			}
			// 遍历所有节点，将父菜单id与传过来的id比较
			if (StringUtils.isNotBlank(menu.getParentId())) {
				if (menu.getParentId().equals(id)) {
					AjaxMenu ajaxMenu = new AjaxMenu(menu, true);
					childList.add(ajaxMenu);
				}
			}
		}
		// 把子菜单的子菜单再循环一遍
		for (AjaxMenu menu : childList) {// 没有url子菜单还有子菜单
			if (StringUtils.isBlank(menu.getIndex())) {
				menu.setIndex(menu.getId());
			}
			// 递归
			menu.setChildren(getMenuChild(menu.getId(), rootMenu, isShowHide));
		} // 递归退出条件
		if (childList.size() == 0) {
			return null;
		}
		return childList;
	}

	/**
	 * 根据登录名获取用户
	 * 
	 * @param loginName
	 * @return 取不到返回null
	 */
	public static UserDO getByLoginName(String loginName) {
		UserDO user = (UserDO) CacheUtils.get(USER_CACHE, USER_CACHE_LOGIN_NAME_ + loginName);
		if (user == null) {
			user = userDao.getByLoginName(new UserDO(null, loginName));
			if (user == null) {
				return null;
			}
			Map<String, Object> map = new HashMap<>();
			map.put("userId", user.getId());
			user.setRoleList(get(user.getId()).getRoleList());
			CacheUtils.put(USER_CACHE, USER_CACHE_ID_ + user.getId(), user);
			CacheUtils.put(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getUsername(), user);
		}
		return user;
	}

	/**
	 * 获取当前用户角色列表
	 *
	 * @return
	 */
	public static List<Role> getRoleList() {
		@SuppressWarnings("unchecked")
		List<Role> roleList = (List<Role>) getCache(CACHE_ROLE_LIST);
		if (roleList == null) {
			UserDO user = getUser();
			if (user.isAdmin()) {
				roleList = roleDao.findAllList(new Role());
			} else {
				Role role = new Role();
				// role.getSqlMap().put(
				// "dsf",
				// BaseService.dataScopeFilter(ShiroUtils.getUser(), "o",
				// "u"));
				roleList = roleDao.findList(role);
			}
			putCache(CACHE_ROLE_LIST, roleList);
		}
		return roleList;
	}
}