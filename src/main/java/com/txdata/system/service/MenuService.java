package com.txdata.system.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.txdata.common.persistence.CrudService;
import com.txdata.common.utils.CacheUtils;
import com.txdata.system.dao.MenuDao;
import com.txdata.system.domain.Menu;
import com.txdata.system.utils.UserUtils;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class MenuService extends CrudService<MenuDao, Menu> {

	public static final String CACHE_MENU_NAME_PATH_MAP = "menuNamePathMap";

	@Autowired
	private MenuDao dao;

	public Menu getMenu(String id) {
		return dao.get(id);
	}

	/**
	 * 获取当前用户有权限查看的所有菜单列表
	 * 
	 * @return
	 */
	public List<Menu> findAllMenu() {
		return UserUtils.getMenuList();
	}

	@Transactional(readOnly = false)
	public void saveMenu(Menu menu) {
		// 获取父节点实体
		Menu parent = this.getMenu(menu.getParent().getId());
		if (parent != null) {
			menu.setParent(parent);
		}
		// 获取修改前的parentIds，用于更新子节点的parentIds
		String oldParentIds = menu.getParentIds();
		// 设置新的父节点串
		if (menu.getParent() != null) {
			menu.setParentIds(menu.getParent().getParentIds() + menu.getParent().getId() + ",");
		}
		// 保存或更新实体
		if (StringUtils.isBlank(menu.getId())) {
			menu.preInsert();
			dao.insert(menu);
		} else {
			menu.preUpdate();
			dao.update(menu);
		}
		// 更新子节点 parentIds
		Menu m = new Menu();
		m.setParentIds("%," + menu.getId() + ",%");
		List<Menu> list = dao.findByParentIdsLike(m);
		for (Menu e : list) {
			e.setParentIds(e.getParentIds().replace(oldParentIds, menu.getParentIds()));
			dao.updateParentIds(e);
		}
		// 清除用户菜单缓存
		UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
//		// 清除权限缓存
//		systemRealm.clearAllCachedAuthorizationInfo();
		// 清除菜单缓存
//		CacheUtils.remove(UserUtils.USER_CACHE, CACHE_MENU_NAME_PATH_MAP);
	}

	@Transactional(readOnly = false)
	public void deleteMenu(Menu menu) {
		dao.delete(menu);
		// 清除用户菜单缓存
		UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
//		// 清除权限缓存
//		systemRealm.clearAllCachedAuthorizationInfo();
		// 清除日志相关缓存
//		CacheUtils.remove(UserUtils.USER_CACHE, CACHE_MENU_NAME_PATH_MAP);
	}

	public Set<String> listPerms(String userId) {
		List<String> perms = dao.listUserPerms(userId);
		Set<String> permsSet = new HashSet<>();
		for (String perm : perms) {
			if (StringUtils.isNotBlank(perm)) {
				permsSet.addAll(Arrays.asList(perm.trim().split(",")));
			}
		}
		return permsSet;
	}
}
