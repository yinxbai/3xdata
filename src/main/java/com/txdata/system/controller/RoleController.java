package com.txdata.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.txdata.common.config.Global;
import com.txdata.common.controller.BaseController;
import com.txdata.common.utils.CacheUtils;
import com.txdata.common.utils.R;
import com.txdata.system.dao.RoleDao;
import com.txdata.system.domain.*;
import com.txdata.system.service.RoleService;
import com.txdata.system.service.SystemService;
import com.txdata.system.service.UserService;
import com.txdata.system.utils.UserUtils;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 后端所需要的权限相关Controller 前端需要的见RoleApiController
 * 
 * @author mark
 *
 */
@RequestMapping("/sys/role")
@Controller
public class RoleController extends BaseController {

	@Autowired
	RoleService roleService;
	@Autowired
	UserService userService;
	@Autowired
	SystemService systemService;
	@Autowired
	RoleDao roleDao;

	 @ModelAttribute("role")
	public Role get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return systemService.getRole(id);
		} else {
			return new Role();
		}
	}

	@ResponseBody
	@PostMapping(value = "list")
	@RequiresPermissions("sys:role:view")
	public R list() {
		// 查询当前用户所有角色
		List<Role> list = roleService.findAllRole();
		List<AjaxRole> ajaxRoleList = new ArrayList<AjaxRole>();
		for (Role ro : list) {
			AjaxRole ajaxRole = new AjaxRole(ro);
			ajaxRoleList.add(ajaxRole);
		}
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("rows", ajaxRoleList);
		return R.success(jsonMap);
	}

	@RequestMapping(value = "delete")
	@ResponseBody
	@RequiresPermissions("sys:role:edit")
	public R delete(Role role) {
		if (!UserUtils.getUser().isAdmin() && role.getSysData().equals(Global.YES)) {
			R r = R.error();
			r.setCode("403");
			r.setMessage("越权操作，只有超级管理员才能修改此数据！");
			return r;
		}
		List<UserDO> userList = roleService.findUserById(role.getId());
		for(UserDO user : userList) {
			if (systemService.getUser(user.getId()).getRoleList().size() <= 1) {
				return R.error("400", "删除失败，存在用户" + user.getName() + "只关联此角色为唯一角色，请先为此用户关联其他角色！");
			}
		}
		systemService.deleteRole(role);
		return R.success();
	}

	@ResponseBody
	@PostMapping(value = "form")
	@RequiresPermissions("sys:role:view")
	public String form(String id, HttpServletRequest request, HttpServletResponse response) {
		Role role = new Role();
		if (id != null && !"".equals(id)) {
			role = roleService.get(id);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		AjaxRole ajaxRole = new AjaxRole(role);
		map.put("formObject", ajaxRole);
		return renderString(response, R.success(map));
	}

	@PostMapping("/save")
	@ResponseBody
	@RequiresPermissions("sys:role:edit")
	public R save(AjaxRole ajaxRole) {
		if (!UserUtils.getUser().isAdmin() && ajaxRole.getSysData().equals(Global.YES)) {
			return R.error("403", "越权操作，只有超级管理员才能修改此数据！");
		}
		Role role = new Role(ajaxRole);
		// 验证实体类有效性，返回错误信息
		String validateMsg = wssBeanValidator(role);
		if (StringUtils.isNotBlank(validateMsg)) {
			return R.error("400", validateMsg);
		}
		systemService.saveRole(role);
		return R.success();
	}

	/**
	 * 角色确认分配
	 * 
	 * @param
	 * @param idsArr
	 * @return
	 */
	@RequestMapping(value = "assignrole")
	@ResponseBody
	@RequiresPermissions("sys:role:edit")
	public R assignRole(String id, String idsArr) {
		Role role = roleService.get(id);
		StringBuilder msg = new StringBuilder();
		int newNum = 0;
		String[] idsArrs = idsArr.split(",");
		for (int i = 0; i < idsArrs.length; i++) {
			String userId = idsArrs[i];
			UserDO user = roleService.assignUserToRole(id, userService.getOffice(userId));
			if (null != user) {
				msg.append("<br/>新增用户【" + user.getName() + "】到角色【" + role.getName() + "】！");
				newNum++;
			}
		}
		for(String idStr : idsArrs) {
			CacheUtils.remove(UserUtils.USER_CACHE,UserUtils.USER_CACHE_ID_ + idStr);
		}
		return R.success("已成功分配 " + newNum + " 个用户" + msg);
	}

	/**
	 * 角色分配 -- 从角色中移除用户
	 * 
	 * @param userId
	 * @param roleId
	 * @return
	 */
	@RequestMapping(value = "outrole")
	@ResponseBody
	@RequiresPermissions("sys:role:edit")
	public R outrole(String userId, String roleId) {
		Role role = systemService.getRole(roleId);
		UserDO user = systemService.getUser(userId);
		R result = new R();
		String msg = "";
		if (UserUtils.getUser().getId().equals(userId)) {
			msg = "无法从角色【" + role.getName() + "】中移除用户【" + user.getName() + "】自己！";
			return R.error("406", msg);
		} else {
			if (user.getRoleList().size() <= 1) {
				msg = "用户【" + user.getName() + "】从角色【" + role.getName() + "】中移除失败！这已经是该用户的唯一角色，不能移除。";
				return R.error("406", msg);
			} else {
				Boolean flag = systemService.outUserInRole(role, user);
				if (!flag) {
					msg = "用户【" + user.getName() + "】从角色【" + role.getName() + "】中移除失败！";
					return R.error("406", msg);
				} else {
					result = R.success();
				}
			}
		}
		return result;
	}

	/**
	 * 检验英文名唯一性
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "validateEname")
	public R validateEname(
			@RequestParam("enname") String enname,
			@RequestParam(value = "id", required = false) String id
	) {
		JSONObject data = new JSONObject();
		data.put("isExist", true);
		Role role = new Role();
		role.setEnname(enname);
		if (CollectionUtils.isEmpty(roleDao.findListByEnName(role))) {
			data.put("isExist", false);
		} else if (id != null) {
			role = roleDao.get(id);
			if (role != null && Objects.equals(role.getEnname(), enname)) {
				data.put("isExist", false);
			}
		}
		return R.success(data);
	}

	/**
	 * 角色分配 -- 根据部门编号获取用户列表
	 * 
	 * @param officeId
	 * @param response
	 * @return
	 */
	@ResponseBody
	@PostMapping(value = "users")
	public R<Map<String, Object>> users(String officeId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("officeId", officeId);
		List<UserDO> userList = userService.list(params);
		for (UserDO e : userList) {
			Map<String, Object> map = Maps.newHashMap();
			map.put("userId", e.getId());
			map.put("userName", e.getName());
			mapList.add(map);
		}
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("userList", mapList);
		R<Map<String, Object>> r = R.success();
		r.setData(jsonMap);
		return r;
	}

	/**
	 * 根据角色英文名获取用户列表
	 * 
	 * @param eName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "assignByEname")
	public R<Map<String, Object>> assignByEname(String eName) {
		Role role = new Role();
		role.setEnname(eName);
		List<UserDO> userList = systemService.findUserByEnName(role);
		List<Map<String, String>> ajaxUserList = new ArrayList<Map<String, String>>();
		for (UserDO user : userList) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", user.getId());
			map.put("name", user.getName());
			map.put("loginName", user.getUsername());
			map.put("mobile", user.getMobile());
			map.put("detailName",
					user.getName() + (StringUtils.isNotBlank(user.getMobile()) ? "_" + user.getMobile() : ""));
			map.put("officeName", user.getOfficeName());
			ajaxUserList.add(map);
		}
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("userList", ajaxUserList);
		return R.success(jsonMap);
	}

	/**
	 * 根据角色id获取用户列表
	 * 
	 * @param
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "assign")
	@ApiOperation(value = "角色分配用户列表", httpMethod = "POST")
	public R assign(String id) {
		List<UserDO> userList = systemService.findUser(new UserDO(new Role(id)));
		List<Map<String, String>> ajaxUserList = new ArrayList<Map<String, String>>();
		for (UserDO user : userList) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", user.getId());
			map.put("name", user.getName());
			map.put("loginName", user.getUsername());
			map.put("phone", user.getPhone());
			map.put("mobile", user.getMobile());
			Office office = user.getOffice();
			if (office != null) {
				map.put("officeName", user.getOffice().getName());
			}
			ajaxUserList.add(map);
		}
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("rows", ajaxUserList);
		return R.success(jsonMap);
	}

}
