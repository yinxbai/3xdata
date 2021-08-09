package com.txdata.system.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.txdata.common.controller.BaseController;
import com.txdata.common.utils.R;
import com.txdata.system.domain.Menu;
import com.txdata.system.service.MenuService;
import com.txdata.system.utils.UserUtils;
import com.google.common.collect.Lists;

/**
 * 权限验证
 * 
 * @author lmh
 *
 */
@Controller
public class PermissionController extends BaseController {

	@Autowired
	MenuService menuService;

	/**
	 * 判断用户是否拥有某个权限
	 * 
	 * @param name
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "permission/hasPermission", method = RequestMethod.POST)
	@ResponseBody
	public R hasPermission(String name, HttpServletRequest request, HttpServletResponse response) {
		List<Menu> menuList = UserUtils.getMenuList();
		boolean flag = false;
		for (Menu menu : menuList) {
			if (name.equals(menu.getPermission())) {
				flag = true;
				break;
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("hasPermission", flag);
		return R.success(map);
	}

	/**
	 * 查询用户所有权限
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "permission/allPermission", method = RequestMethod.POST)
	@ResponseBody
	public R allPermission(HttpServletRequest request, HttpServletResponse response) {
		List<Menu> menuList = UserUtils.getMenuList();
		List<String> permissions = Lists.newArrayList();
		for (Menu menu : menuList) {
			if (menu.getPermission() != null && !"".equals(menu.getPermission())) {
				// 获取用户权限
				String permission = menu.getPermission();
				// 截取 ， 获取单个权限
				String[] ps = permission.split(",");
				for (String p : ps) {
					// 封装进list
					permissions.add(p);
				}
			}
		}
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("permissions", permissions);
		return R.success(jsonMap);
	}

	/**
	 * 查询服务器当前时间
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getCurServerTime", method = RequestMethod.POST)
	@ResponseBody
	public R getCurServerTime() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dateTime", sdf.format(date));
		return R.success(map);
	}

}
