package com.txdata.common.controller;

import com.txdata.common.domain.AjaxDictDO;
import com.txdata.common.domain.DictDO;
import com.txdata.common.service.DictService;
import com.txdata.common.utils.DictUtils;
import com.txdata.common.utils.Query;
import com.txdata.common.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字典表
 * 
 * @author huangmk
 * @since 2018-10-21
 */
@Controller
@RequestMapping("/sys/dict")
public class DictController extends BaseController {

	@Autowired
	private DictService dictService;

	@ResponseBody
	@PostMapping("/list")
	@RequiresPermissions("sys:menu:view")
	public R list(@RequestParam Map<String, Object> params) {
		// 查询列表数据
		Query query = new Query(params);
		List<DictDO> dictList = dictService.list(query);
		List<AjaxDictDO> ajaxList = new ArrayList<>();
		for (DictDO dict : dictList) {
			AjaxDictDO ajax = new AjaxDictDO(dict);
			ajaxList.add(ajax);
		}
		int total = dictService.count(query);
		// PageUtils pageUtils = new PageUtils(ajaxList, total, query.getPageNo(),
		// query.getPageSize());
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("rows", ajaxList);
		jsonMap.put("pageSize", query.getPageSize());
		jsonMap.put("pageNo", query.getPageNo());
		jsonMap.put("count", total);
		return R.success(jsonMap);
	}

	@ResponseBody
	@PostMapping("/form")
	@RequiresPermissions("sys:menu:view")
	R form(String id) {
		DictDO dict = new DictDO();
		if (id != null && !"".equals(id)) {
			dict = dictService.get(id);
		}
		AjaxDictDO ajax = new AjaxDictDO(dict);
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("formObject", ajax);
		return R.success(jsonMap);
	}

	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("sys:menu:edit")
	public R save(DictDO dict) {
		if (dict.getId() != null && !"".equals(dict.getId())) {
			if (dictService.update(dict) > 0) {
				return R.success();
			}
		}
		if (dictService.save(dict) > 0) {
			return R.success();
		}
		return R.error();
	}

	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("sys:menu:edit")
	public R update(DictDO dict) {
		int row = dictService.update(dict);
		if (row > 0) {
			return R.success();
		} else {
			return R.error();
		}
	}

	/**
	 * 删除
	 */
	@PostMapping("/delete")
	@ResponseBody
	@RequiresPermissions("sys:menu:edit")
	public R remove(String id) {
		if (dictService.remove(id) > 0) {
			return R.success();
		}
		return R.error();
	}

	/**
	 * 删除
	 */
	@PostMapping("/batchRemove")
	@ResponseBody
	@RequiresPermissions("sys:menu:edit")
	public R remove(@RequestParam("ids[]") String[] ids) {
		dictService.batchRemove(ids);
		return R.success();
	}

	@RequestMapping("/type")
	@ResponseBody
	public List<DictDO> listType() {
		return dictService.listType();
	}

	/**
	 * 根据字典类型获取该类型所有字典
	 * 
	 * @param type
	 *            类型
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getDictList")
	public R listByType(@RequestParam("type") String type) {
		// 查询列表数据
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<>(16);
		map.put("type", type);
		List<DictDO> dictList = dictService.list(map);
		List<AjaxDictDO> ajaxList = new ArrayList<>();
		for (DictDO dict : dictList) {
			// 将dict 封装进AjaxDictDo
			AjaxDictDO ajax = new AjaxDictDO(dict);
			ajaxList.add(ajax);
		}
		jsonMap.put("dictList", ajaxList);
		return R.success(jsonMap);
	}
	
	/**
	 * (门户网)根据字典类型获取该类型所有字典
	 * 
	 * @param type
	 *            类型
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/fore/getDictList")
	public R foreListByType(@RequestParam("type") String type) {
		// 查询列表数据
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<>(16);
		map.put("type", type);
		List<DictDO> dictList = dictService.list(map);
		List<AjaxDictDO> ajaxList = new ArrayList<>();
		for (DictDO dict : dictList) {
			// 将dict 封装进AjaxDictDo
			AjaxDictDO ajax = new AjaxDictDO(dict);
			ajaxList.add(ajax);
		}
		jsonMap.put("dictList", ajaxList);
		return R.success(jsonMap);
	}

	/**
	 * 根据字典类型和value值获取字典的名称
	 * 
	 * @param value
	 * @param type
	 * @param defaultValue
	 * @return
	 */
	@RequestMapping(value = "/getDictLabel")
	@ResponseBody
	public R getDictLabel(String value, String type, String defaultValue) {
		// 获取字典类型名称
		String label = DictUtils.getDictLabel(value, type, defaultValue);
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("label", label);
		return R.success(jsonMap);
	}

	/**
	 * 获取所有的字典类型名称
	 * 
	 * @param dict
	 * @return
	 */
	@RequestMapping(value = "/typeList")
	@ResponseBody
	public R typeList(DictDO dict) {
		List<String> typeList = dictService.findTypeList();
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("typeList", typeList);
		return R.success(jsonMap);
	}
}
