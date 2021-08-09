package com.txdata.system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.txdata.common.controller.BaseController;
import com.txdata.common.domain.Tree;
import com.txdata.common.utils.R;
import com.txdata.system.domain.AjaxArea;
import com.txdata.system.domain.Area;
import com.txdata.system.domain.Office;
import com.txdata.system.service.AreaService;
import com.txdata.system.service.OfficeService;
import com.txdata.system.utils.UserUtils;
import net.sf.json.JSONObject;

/**
 * 区域管理(前端调用)
 * 
 * @author huangmk
 * @since 2018-11-26
 *
 */
@Controller
@RequestMapping(value = "/sys/area")
public class AreaController extends BaseController {

	@Autowired
	private AreaService areaService;
	@Autowired
	private OfficeService officeService;

	/**
	 * 区域列表
	 * 
	 * @param area
	 *            参数对象
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "list")
	@RequiresPermissions("sys:area:view")
	public R list(Area area, HttpServletRequest request, HttpServletResponse response) {
		// 获取区域树
		List<Tree<AjaxArea>> t = areaService.rootAreaTreeList(UserUtils.getAreaList());
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("rows", t);
		return R.success(jsonMap);
	}

	/**
	 * 区域删除
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@RequiresPermissions("sys:area:edit")
	public R<?> delete(@RequestParam(required = true) String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("areaId", id);
		// 根据查询条件(名称和区域)查询机构列表
		List<Office> officeList = officeService.list(map);
		if (officeList != null && officeList.size() > 0) {
			return R.error("300", "该地区下还有组织机构，不能删除");
		}
		areaService.delete(areaService.get(id));
		return R.success();
	}

	/**
	 * 区域查看
	 * 
	 * @param id
	 * @param parentId
	 * @param area
	 * @return
	 */
	@RequestMapping(value = "form", method = RequestMethod.POST)
	@RequiresPermissions("sys:area:view")
	public String form(String id, String parentId, Area area, HttpServletResponse response) {
		if (StringUtils.isNotBlank(id)) {
			R r = areaService.getArea(id);// 获取区域信息
			return renderString(response, r);
		}
		if (StringUtils.isNotBlank(parentId)) {
			area.setParent(areaService.get(parentId));// 设置父级
		} else {
			if (area == null) {
				area = new Area();
			}
			if (area.getParent() == null || area.getParent().getId() == null) {
				if (area.getParent() != null && area.getParent().getId() != null) {
					area.setParent(areaService.get(area.getParent().getId()));// 设置父级
				}
			}
		}
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		JSONObject object = new JSONObject();
		object.put("parentId", area.getParent() != null ? area.getParent().getId() : "");
		object.put("parentName", area.getParent() != null ? area.getParent().getName() : "");
		jsonMap.put("formObject", object);
		return renderString(response, R.success(jsonMap));
	}

	/**
	 * 区域树
	 * 
	 * @param isAllGrade
	 *            是否显示第四级地区
	 * @param extId
	 *            要排除的区域ID
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "treeData", method = RequestMethod.POST)
	public String treeData(Boolean isAllGrade, @RequestParam(required = false) String extId,
			HttpServletResponse response) {
		// 获取当前用户授权的区域
		List<Area> list = areaService.findAll();
		List<Tree<AjaxArea>> t = areaService.rootAreaTreeList(list);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("treeData", t);
		return renderString(response, R.success(data));
	}

	/**
	 * 区域添加/编辑/添加下级区域
	 * 
	 * @param ajaxArea
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "save", method = RequestMethod.POST)
	@RequiresPermissions("sys:area:edit")
	public R<?> save(AjaxArea ajaxArea) {
		Area area = new Area(ajaxArea);
		// 验证实体类有效性，返回错误信息
		String validateMsg = wssBeanValidator(area);
		if (StringUtils.isNotBlank(validateMsg)) {
			return R.error("400", validateMsg);
		}
		areaService.save(area);
		return R.success();
	}

	/**
	 * 区域下拉框
	 * 
	 * @param area
	 * @return
	 */
	@RequestMapping(value = "findByparentId", method = RequestMethod.POST)
	public String findMapByparentId(Area area, HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("areaList", areaService.findByParentId(area));// 根据父级id查询列表
		return renderString(response, R.success(data));
	}
}
