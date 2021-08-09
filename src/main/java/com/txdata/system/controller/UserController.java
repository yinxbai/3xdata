package com.txdata.system.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.txdata.common.beanvalidator.BeanValidators;
import com.txdata.common.controller.BaseController;
import com.txdata.common.utils.*;
import com.txdata.common.utils.excel.ExportExcel;
import com.txdata.common.utils.excel.ImportExcel;
import com.txdata.system.dao.AreaDao;
import com.txdata.system.domain.AjaxUser;
import com.txdata.system.domain.Area;
import com.txdata.system.domain.Role;
import com.txdata.system.domain.UserDO;
import com.txdata.system.service.AreaService;
import com.txdata.system.service.RoleService;
import com.txdata.system.service.SystemService;
import com.txdata.system.service.UserService;
import com.txdata.system.utils.UserUtils;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户管理
 * 
 * @author mark
 *
 */
@RequestMapping("/sys/user")
@Controller
public class UserController extends BaseController {

	@Autowired
	UserService userService;
	@Autowired
	RoleService roleService;
	@Autowired
	AreaDao areaDao;
	@Autowired
	private SystemService systemService;

	@ResponseBody
	@PostMapping("/list")
	@RequiresPermissions("sys:user:view")
	String list(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) {
		// 查询列表数据
		Query query = new Query(params);
		String areaId = (String) params.get("areaId");
		if (StringUtils.isNotEmpty(areaId)) {
			Area area = new Area();
			area.setParentIds(areaId);
			List<Area> areas = areaDao.findByParentIdsLike(area);
			areas.add(areaDao.get(areaId));
			query.put("areaIds", areas.stream().map(Area::getId).map(s -> '\'' + s + '\'').collect(Collectors.joining(",")));
		}
        Page<UserDO> page = new Page<>(query.getPageNo(), query.getPageSize());
		page = userService.queryList(page, query);
		List<UserDO> userList = page.getRecords();
		List<AjaxUser> ajaxList = new ArrayList<>();
		for (UserDO user : userList) {
			AjaxUser ajax = new AjaxUser(user);
			ajaxList.add(ajax);
		}
		Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("rows", ajaxList);
		jsonMap.put("pageSize", page.getSize());
        jsonMap.put("pageNo", page.getCurrent());
        jsonMap.put("count", page.getTotal());
		return renderString(response, R.success(jsonMap));
	}

	@PostMapping("/form")
	@ResponseBody
	@RequiresPermissions("sys:user:view")
	String form(@RequestParam(required = true) String id, HttpServletRequest request, HttpServletResponse response) {
		UserDO userDO = UserUtils.get(id);
		userDO.setLoginName(userDO.getUsername());
		JSONObject user = (JSONObject) JSON.toJSON(new AjaxUser(userDO));
		user.put("roleIdList", JSON.parseArray(user.getString("roleIdList")));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("formObject", user);
		return renderString(response, R.success(map));
	}

	@PostMapping("/delete")
	@ResponseBody
	@RequiresPermissions("sys:user:edit")
	R remove(@RequestParam(required = true) String id) {
		if ("1".equals(id)) {
			return R.error("406", "删除用户失败, 不允许删除超级管理员用户");
		} else if (id.equals(getUserId())) {
			return R.error("406", "删除用户失败, 不允许删除当前用户");
		}
		if (userService.remove(id) > 0) {
			return R.success();
		}
		return R.error();
	}

	@PostMapping("/save")
	@ResponseBody
	@RequiresPermissions("sys:user:edit")
	R save(AjaxUser user, HttpServletRequest request) {
		if (StringUtils.isNotBlank(user.getNewPassword())) {
			user.setPassword(SystemService.entryptPassword(user.getNewPassword()));
		}
		List<Role> roleList = Lists.newArrayList();
		List<String> roleIdList = JSON.parseArray(user.getRoleIdList(), String.class);
		for (Role r : roleService.findAllRole()) {
			if (roleIdList.contains(r.getId())) {
				roleList.add(r);
			}
		}
		// 保存用户信息
		UserDO users = new UserDO(user);
		users.setRoleList(roleList);
		systemService.saveUser(users);
		// 清除当前用户缓存
		UserUtils.clearCache();
		return R.success();
	}

	/**
	 * 导入用户数据
	 * 
	 * @param file
	 * @return
	 */
	@ResponseBody
	@PostMapping(value = "import")
	public R importFile(@RequestParam(required = true) MultipartFile file) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<UserDO> list = ei.getDataList(UserDO.class);
			for (UserDO user : list) {
				try {
					if ("true".equals(checkLoginName("", user.getUsername()))) {
						// 获取String的MD5值
						user.setPassword(MD5Utils.getMD5("123456"));
						BeanValidators.validateWithException(validator, user);
						userService.save(user);
						successNum++;
					} else {
						failureMsg.append("<br/>登录名 " + user.getUsername() + " 已存在; ");
						failureNum++;
					}
				} catch (ConstraintViolationException ex) {
					failureMsg.append("<br/>登录名 " + user.getUsername() + " 导入失败：");
					// 转换ConstraintViolationException中的Set为List.
					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
					for (String message : messageList) {
						failureMsg.append(message + "; ");
						failureNum++;
					}
				} catch (Exception ex) {
					failureMsg.append("<br/>登录名 " + user.getUsername() + " 导入失败：" + ex.getMessage());
				}
			}
			if (failureNum > 0) {
				failureMsg.insert(0, "，失败 " + failureNum + " 条用户，导入信息如下：");
			}
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("successNum", successNum);
			data.put("failureNum", failureNum);
			data.put("failureMsg", failureMsg);
			return R.success(data);
		} catch (Exception e) {
			return R.error("406", "导入用户失败！失败信息：" + e.getMessage());
		}
	}

	/**
	 * 验证登录名是否有效
	 * 
	 * @return
	 */
	@ResponseBody
	@PostMapping(value = "checkLoginName")
	public R checkLoginName(
			@RequestParam("loginName") String loginName,
			@RequestParam(value = "id", required = false) String id) {
		JSONObject data = new JSONObject();
		data.put("isExist", true);
		if (userService.getByUsername(loginName) == null) {
			data.put("isExist", false);
		} else if (id != null) {
			UserDO user = userService.get(id);
			if (user != null && Objects.equals(user.getUsername(), loginName)) {
				data.put("isExist", false);
			}
		}
		return R.success(data);
	}

	/**
	 * 导出用户数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "export")
	public R exportFile(@RequestParam Map<String, Object> params, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String fileName = "用户数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Query query = new Query(params);
			List<UserDO> userList = userService.list(query);
			new ExportExcel("用户数据", UserDO.class).setDataList(userList).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("406", "导出用户失败！失败信息：" + e.getMessage());
		}
	}

	/**
	 * 下载用户导入模板
	 * 
	 * @param response
	 * @param redirectAttributes
	 * @throws IOException
	 */
	@RequestMapping(value = "import/template")
	public void importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes)
			throws IOException {
		try {
			String fileName = "用户数据导入模板.xlsx";
			List<UserDO> list = Lists.newArrayList();
			list.add(getUser());
			new ExportExcel("用户数据", UserDO.class, 2).setDataList(list).write(response, fileName).dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 修改密码
	 * 
	 * @param oldPassword
	 *            原始密码
	 * @param newPassword
	 *            新密码
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "modifyPwd", method = RequestMethod.POST)
	public R modifyPwd(@RequestParam(required = true) String oldPassword,
			@RequestParam(required = true) String newPassword) {
		UserDO user = UserUtils.getUser();
		if (StringUtils.isNotBlank(oldPassword) && StringUtils.isNotBlank(newPassword)) {
			// 验证密码
			if (SystemService.validatePassword(oldPassword, user.getPassword())) {
				// 修改用户密码并清除缓存
				systemService.updatePasswordById(user.getId(), user.getUsername(), newPassword);
				return R.success();
			} else {
				return R.error();
			}
		}
		return R.error();
	}

	/**
	 * 用户个人信息保存
	 * 
	 * @param name
	 * @param email
	 * @param phone
	 * @param mobile
	 * @param remarks
	 * @param response
	 * @return
	 */
	@ResponseBody
	@PostMapping(value = "info")
	public R info(@RequestParam("name") String name, @RequestParam("email") String email,
			@RequestParam("phone") String phone, @RequestParam("mobile") String mobile,
			@RequestParam("remarks") String remarks, HttpServletResponse response) {
		UserDO currentUser = UserUtils.getUser();
		if (StringUtils.isNotBlank(name)) {
			currentUser.setEmail(email);
			currentUser.setPhone(phone);
			currentUser.setMobile(mobile);
			currentUser.setRemarks(remarks);
			// 修改用户信息
			systemService.updateUserInfo(currentUser);
		}
		return R.success();
	}

	@ResponseBody
	@PostMapping(value = "getAllList")
	public R getAllList(@RequestParam Map<String, Object> params) {
		Query query = new Query(params);
		List<UserDO> userList = userService.queryList(query);
		Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("rows", userList);
		return R.success(jsonMap);
	}
	
	@ResponseBody
	@PostMapping(value = "checkOldPassword")
	public R checkOldPassword(String password) {
		UserDO user = ShiroUtils.getUser();
		boolean validate = false;
		if (MD5Utils.getMD5(password).equals(user.getPassword())) {
			validate = true;
		}
		Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("validate", validate);
		return R.success(jsonMap);
	}
}
