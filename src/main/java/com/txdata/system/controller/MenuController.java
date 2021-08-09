package com.txdata.system.controller;

import com.txdata.common.controller.BaseController;
import com.txdata.common.utils.R;
import com.txdata.common.utils.StringUtils;
import com.txdata.system.domain.AjaxMenu;
import com.txdata.system.domain.Menu;
import com.txdata.system.service.MenuService;
import com.txdata.system.utils.UserUtils;
import com.google.common.collect.Lists;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @ClassName: MenuApiController
 * @Description: 菜单相关Controller
 * 
 * @author: huangmk
 * @date: 2018年12月24日 上午10:09:26
 */
@RequestMapping("/sys/menu")
@Controller
public class MenuController extends BaseController {
	@Autowired
	private MenuService menuService;

	@PostMapping("/list")
	@ResponseBody
	@RequiresPermissions("sys:menu:view")
	String list(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) {
		List<AjaxMenu> menuTree = UserUtils.getMenuTree(true);// 获取菜单树
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("rows", menuTree);
		return renderString(response, R.success(jsonMap));
	}

	@PostMapping("/form")
	@ResponseBody
	@RequiresPermissions("sys:menu:view")
	public String form(String id, String parentId, HttpServletRequest request, HttpServletResponse response) {
		Menu menu = null;
		if (StringUtils.isNotBlank(id)) {
			menu = menuService.getMenu(id);
		} else {
			menu = new Menu();
			menu.setIsShow("1"); // 默认显示
			if (StringUtils.isBlank(parentId)) {
				// 没有父级id则默认为1
				menu.setParent(new Menu(Menu.getRootId()));
			} else {
				menu.setParent(menuService.getMenu(parentId));
			}
		}
		// 获取排序号，最末节点排序号+30
		if (StringUtils.isBlank(menu.getId())) {
			List<Menu> list = Lists.newArrayList();
			// 获取当前用户有权限查看的所有菜单
			List<Menu> sourcelist = menuService.findAllMenu();
			// 获取所有父级ID为menu.getParentId()下的菜单
			Menu.sortList(list, sourcelist, menu.getParentId(), false);
			if (list.size() > 0) {
				// 取最后一个菜单的排序字段，并加上30复制给新的空菜单
				menu.setSort(list.get(list.size() - 1).getSort() + 30);
			}
		}
		AjaxMenu ajaxMenu = new AjaxMenu(menu, true);
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("formObject", ajaxMenu);
		return renderString(response, R.success(jsonMap));
	}

	/**
	 * 菜单树
	 * 
	 * @param isShowHide
	 * @param request
	 * @param response
	 * @return
	 */
	@PostMapping("/treeData")
	@ResponseBody
	public String treeData(@RequestParam(required = true) String isShowHide, HttpServletRequest request,
			HttpServletResponse response) {
		boolean isShow = false;
		if ("1".equals(isShowHide)) {
			isShow = true;
		}
		List<AjaxMenu> menuTree = UserUtils.getMenuTree(isShow);
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("treeData", menuTree);
		return renderString(response, R.success(jsonMap));
	}

	@PostMapping("/save")
	@ResponseBody
	@RequiresPermissions("sys:menu:edit")
	public R save(AjaxMenu ajaxMenu, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		if (!UserUtils.getUser().isAdmin()) { // 只有超级管理员才能添加或修改数据
			return R.error("403", "越权操作，只有超级管理员才能添加或修改此数据！", jsonMap);
		}
		Menu menu = new Menu(ajaxMenu);
		// 验证实体类有效性，返回错误信息
		String validateMsg = wssBeanValidator(menu);
		if (StringUtils.isNotBlank(validateMsg)) {
			return R.error("400", validateMsg, jsonMap);
		}
		if (StringUtils.isNotBlank(ajaxMenu.getId())) {
			Menu menu2 = menuService.getMenu(ajaxMenu.getId());
			if (menu2 != null) {
				menu.setParentIds(menu2.getParentIds());
			}
		}
		menuService.saveMenu(menu);
		return R.success(jsonMap);
	}

	@PostMapping("/delete")
	@ResponseBody
	@RequiresPermissions("sys:menu:edit")
	public R delete(String id) {
		Menu menu = new Menu();
		menu.setId(id);
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		if (StringUtils.isBlank(menu.getId())) {
			return R.error("400", "参数错误");
		}
		menuService.deleteMenu(menu);
		return R.success(jsonMap);
	}
}
