package com.txdata.system.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.txdata.system.service.RoleService;
import com.txdata.system.service.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.txdata.common.beanvalidator.BeanValidators;
import com.txdata.common.controller.BaseController;
import com.txdata.common.utils.DateUtils;
import com.txdata.common.utils.Query;
import com.txdata.common.utils.R;
import com.txdata.common.utils.StringUtils;
import com.txdata.common.utils.excel.ExportExcel;
import com.txdata.common.utils.excel.ImportExcel;
import com.txdata.system.domain.AjaxOffice;
import com.txdata.system.domain.Area;
import com.txdata.system.domain.Office;
import com.txdata.system.domain.UserDO;
import com.txdata.system.service.OfficeService;
import com.txdata.system.utils.UserUtils;
import com.google.common.collect.Lists;

/**
 * 部门管理
 * 
 * @author lmh
 */
@Controller
@RequestMapping(value = "/sys/office")
public class OfficeController extends BaseController {
	@Autowired
	private OfficeService officeService;

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	/**
	 * 下载导入机构数据模板
	 * 
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "import/template")
	public String importFileTemplate(HttpServletResponse response) {
		try {
			String fileName = "机构数据导入模板.xlsx";
			List<Office> list = Lists.newArrayList();
			UserDO user = UserUtils.getUser();
			Office office = officeService.get(user.getOfficeId());
			list.add(office);
			new ExportExcel("机构数据", Office.class, 2).setDataList(list).write(response, fileName).dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 机构列表/查询
	 * 
	 * @param office
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "list", method = RequestMethod.POST)
	@RequiresPermissions("sys:office:view")
	public R<?> list(Office office, HttpServletRequest request) {
		office.setArea(new Area(request.getParameter("areaId")));
		Query.entitySet(office);
		office.setArea(new Area(office.getAreaId()));
		return officeService.findOffices(office);
	}

	/**
	 * 机构删除
	 * 
	 * @param id
	 * @param office
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@RequiresPermissions("sys:office:edit")
	public R<?> delete(@RequestParam(required = true) String id) {
		Office office = new Office(id);
		if (officeService.isRoot(office)) { // 判断是否是根目录
			return R.error("500", "删除机构失败,该机构下存在子级机构,请先删除子级机构后再进行删除操作");
		} else {
			Map<String, Object> map = new HashMap<>();
			map.put("officeId", id);
			if(userService.count(map) > 0) {
				return R.error("300", "删除机构失败，该机构下还有用户，不允许删除");
			}
			if(roleService.countRolesByOfficeId(map) > 0) {
				return R.error("301", "删除机构失败，请先删除与此机构关联的用户角色!");
			}
			officeService.delete(office);
			return R.success();
		}
	}

	/**
	 * 导入机构数据
	 * 
	 * @param file
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public R<?> importFile(@RequestParam(required = true) MultipartFile file) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Office> list = ei.getDataList(Office.class);
			for (Office office : list) {
				try {
					// 验证机构是否唯一
					if ("true".equals(checkOfficeUnique(office))) {
						officeService.save(office);
						successNum++;
					} else {
						failureMsg.append("<br/>机构名 " + office.getName() + " 已存在，其ID和机构编码不能重复; ");
						failureNum++;
					}
				} catch (ConstraintViolationException ex) {
					failureMsg.append("<br/>机构名 " + office.getName() + " 导入失败：");
					// 转换ConstraintViolationException中的Set为List.
					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
					for (String message : messageList) {
						failureMsg.append(message + "; ");
						failureNum++;
					}
				} catch (Exception ex) {
					failureMsg.append("<br/>机构名 " + office.getName() + " 导入失败：" + ex.getMessage());
					failureNum++;
				}
			}
			if (failureNum > 0) {
				failureMsg.insert(0, "，失败 " + failureNum + " 条用户，导入信息如下：");
			}
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("successNum", successNum);
			data.put("failureNum", failureNum);
			data.put("failureMsg", failureMsg);
			return R.success();
		} catch (Exception e) {
			return R.error("500", "导入机构失败！失败信息：" + e.getMessage());
		}
	}

	/**
	 * 验证机构是否唯一（同一机构内不能有两个相同的部门）
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "checkOfficeUnique")
	public String checkOfficeUnique(Office office) {
		List<Office> list = officeService.findByCode(office);
		if (office != null && officeService.get(office.getId()) == null && CollectionUtils.isEmpty(list)) {
			return "true";
		}
		return "false";
	}

	/**
	 * 导出机构数据
	 * 
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public void exportFile(HttpServletResponse response) {
		try {
			String fileName = "机构数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			List<Office> list = officeService.findList(false);// 只导出当前用户有权限访问的部门
			new ExportExcel("机构数据", Office.class).setDataList(list).write(response, fileName).dispose();
			// return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 机构查看
	 * 
	 * @param id
	 * @param office
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "form", method = RequestMethod.POST)
	@RequiresPermissions("sys:office:view")
	public R<?> form(@RequestParam(required = true) String id) {
		return officeService.getOffice(id);
	}

	/**
	 * 获取机构树的JSON数据。
	 * 
	 * @param extId
	 *            排除的ID
	 * @param type
	 *            类型（1：公司；2：部门/小组/其它：3：用户）
	 * @return
	 */
	@RequestMapping(value = "treeData")
	@ResponseBody
	public R treeData(@RequestParam(required = false) String extId, @RequestParam(required = false) String type) {
		List<AjaxOffice> officeTree = new ArrayList<AjaxOffice>();
		// 获取当前用户有权限访问的部门
		List<Office> rootList = UserUtils.getOfficeList();
		for (Office office : rootList) {
			officeTree.add(new AjaxOffice(office, true));
		}
		// 获取机构树
		officeTree = officeService.getNewOfficeTree(officeTree, false);
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("treeData", officeTree);
		return R.success(jsonMap);
	}

	/**
	 * 机构用户树
	 * 
	 * @param extId
	 * @param type
	 * @param isUser
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "treeUserData")
	public String treeUserData(@RequestParam(required = false) String extId,
			@RequestParam(required = false) String type, @RequestParam(required = false) Boolean isUser,
			HttpServletResponse response) {
		List<AjaxOffice> officeTree = (List<AjaxOffice>)UserUtils.getCache("officeUserTree");
		if (officeTree == null) {
			officeTree = new ArrayList<AjaxOffice>();
			List<Office> rootList = UserUtils.getOfficeList();
			for (Office office : rootList) {
				officeTree.add(new AjaxOffice(office, true));
			}
			officeTree = officeService.getNewOfficeTree(officeTree, isUser);
			UserUtils.putCache("officeUserTree", officeTree);
		} 
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("treeData", officeTree);
		return renderString(response, R.success(jsonMap));
	}

	/**
	 * 机构添加/编辑
	 * 
	 * @param ajaxOffice
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "save", method = RequestMethod.POST)
	@RequiresPermissions("sys:office:edit")
	public R<?> save(AjaxOffice ajaxOffice) {
		Office office = new Office(ajaxOffice);
		if(office.getId() != null) {
			office.setParentIds(officeService.get(office.getId()).getParentIds());
		}
		String validateMsg = wssBeanValidator(office); // 验证实体类有效性，返回错误信息
		if (StringUtils.isNotBlank(validateMsg)) {
			return R.error("400", validateMsg);
		}
		officeService.save(office);
		return R.success();
	}

	/**
	 * 校验name或code的唯一性
	 * 
	 * @param id
	 * @param name
	 * @param code
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "checkNameCode", method = RequestMethod.POST)
	public R<?> checkNameCode(String id, String name, String code) {
		List<Office> list = officeService.checkNameCode(id, name, code);
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		if (list != null && list.size() > 0) {
			// code已被占用，不可用
			jsonMap.put("isExist", true);
		} else {
			jsonMap.put("isExist", false);
		}
		return R.success(jsonMap);
	}
}
