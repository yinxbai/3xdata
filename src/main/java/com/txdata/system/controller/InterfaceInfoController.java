package com.txdata.system.controller;

import java.io.IOException;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.txdata.system.dao.InterfaceInfoDao;
import com.txdata.system.domain.Role;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.txdata.common.controller.BaseController;
import com.txdata.common.utils.Encodes;
import com.txdata.common.utils.Query;
import com.txdata.common.utils.R;
import com.txdata.common.utils.StringUtils;
import com.txdata.system.domain.InterfaceInfo;
import com.txdata.system.domain.InterfaceParam;
import com.txdata.system.service.InterfaceInfoService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 接口管理Controller
 * 
 * @author lmh
 * @version 2018-11-26
 */
@Controller
@RequestMapping("sys/sysInterface")
public class InterfaceInfoController extends BaseController {

	@Autowired
	private InterfaceInfoService interfaceInfoService;
	@Autowired
	private InterfaceInfoDao interfaceInfoDao;

	public InterfaceInfo get(@RequestParam(required = false) String id) {
		InterfaceInfo entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = interfaceInfoService.get(id);
		}
		if (entity == null) {
			entity = new InterfaceInfo();
		}
		return entity;
	}

	/**
	 * 获取列表
	 * 
	 * @param params
	 * @return
	 */
	@ResponseBody
	@PostMapping("/list")
	public R list(@RequestParam Map<String, Object> params) {
		Query query = new Query(params);
		List<InterfaceInfo> list = interfaceInfoService.list(query);// 查询列表
		list.forEach(interfaceInfo -> interfaceInfo.setParams(JSON.toJSONString(interfaceInfo.getParamList())));
		int count = interfaceInfoService.count(query);// 获取条数
		Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("rows", list);
		jsonMap.put("pageSize", query.getPageSize());
		jsonMap.put("pageNo", query.getPageNo());
		jsonMap.put("count", count);
		return R.success(jsonMap);
	}

	/**
	 * 获取对象
	 * 
	 * @param params
	 * @return
	 */
	@ResponseBody
	@PostMapping("/form")
	public R form(@RequestParam Map<String, Object> params) {
		InterfaceInfo interfaceInfo = this.get(params.get("id") == null ? null : params.get("id").toString());
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		Map<String, Object> json = new HashMap<String, Object>();
		if (interfaceInfo != null) {
			json.put("id", interfaceInfo.getId());
			json.put("remarks", interfaceInfo.getRemarks());
			json.put("name", interfaceInfo.getName());
			json.put("accessUrl", interfaceInfo.getAccessUrl());
			json.put("interfaceType", interfaceInfo.getInterfaceType());
			json.put("returnType", interfaceInfo.getReturnType());
			json.put("accessMode", interfaceInfo.getAccessMode());
			json.put("status", interfaceInfo.getStatus());
			if (interfaceInfo.getParamList() == null) {
				json.put("params", new ArrayList<InterfaceParam>());
			} else {
				json.put("params", interfaceInfo.getParamList());
			}
			jsonMap.put("formObject", json);
		} else {
			jsonMap.put("formObject", interfaceInfo);
		}
		return R.success(jsonMap);
	}

	/**
	 * 保存
	 * 
	 * @param interfaceInfo
	 * @return
	 */
	@ResponseBody
	@PostMapping("/save")
	public R save(InterfaceInfo interfaceInfo) {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		interfaceInfo.setParamList(JSON.parseArray(interfaceInfo.getParams(), InterfaceParam.class));
		String validateMsg = wssBeanValidator(interfaceInfo);// 验证对象
		if (StringUtils.isNotBlank(validateMsg)) {
			return R.error(validateMsg);
		}
		interfaceInfoService.save(interfaceInfo);
		return R.success(jsonMap);
	}

	/**
	 * 删除
	 * 
	 * @param params
	 * @return
	 */
	@ResponseBody
	@PostMapping("/delete")
	public R delete(@RequestParam Map<String, Object> params) {
		interfaceInfoService.delete(params.get("id").toString());
		return R.success();
	}

	/**
	 * 验证接口名称
	 * 
	 * @return
	 */
	@ResponseBody
	@PostMapping("/checkInterfaceName")
	public R checkInterfaceName(
			@RequestParam("name") String name,
			@RequestParam(value = "id", required = false) String id
	) {
		JSONObject data = new JSONObject();
		data.put("isExist", true);
		InterfaceInfo interfaceInfo = new InterfaceInfo();
		interfaceInfo.setName(name);
		if (CollectionUtils.isEmpty(interfaceInfoDao.nameValidate(interfaceInfo))) {
			data.put("isExist", false);
		} else if (id != null) {
			interfaceInfo = interfaceInfoDao.get(id);
			if (interfaceInfo != null && Objects.equals(interfaceInfo.getName(), name)) {
				data.put("isExist", false);
			}
		}
		return R.success(data);
	}
}