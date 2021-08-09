package com.txdata.system.controller;

import java.util.Map;
import java.util.HashMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.txdata.common.controller.BaseController;
import com.txdata.common.utils.Query;
import com.txdata.common.utils.R;
import com.txdata.common.utils.ShiroUtils;
import com.txdata.system.domain.AuditRuleDO;
import com.txdata.system.service.AuditRuleService;

/**
 * 
 * 审批规则相关接口
 * 
 * @author 3xdata
 * @email 3xdata@3xdata.cn
 * @date 2019-01-15 11:13:47
 */
@Controller
@RequestMapping("/auditRule")
public class AuditRuleController extends BaseController {
	@Autowired
	private AuditRuleService auditRuleService;

	@ResponseBody
	@PostMapping("/list")
	public R list(@RequestParam Map<String, Object> params) {
		// 查询列表数据
		Query query = new Query(params);
		Page<AuditRuleDO> page = new Page<AuditRuleDO>(query.getPageNo(), query.getPageSize());
		page = auditRuleService.page(page, query);
		// 封装分页数据
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("rows", page.getRecords());
		jsonMap.put("pageSize", page.getSize());
		jsonMap.put("pageNo", page.getCurrent());
		jsonMap.put("count", page.getTotal());
		return R.success(jsonMap);
	}

	@ResponseBody
	@PostMapping("/form")
	public R form(@RequestParam(required = true) String id) {
		JSONObject auditRule = (JSONObject) JSON.toJSON(auditRuleService.getInfo(id));
		auditRule.put("auditRules", auditRule.remove("detailList"));
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("formObject", auditRule);
		return R.success(jsonMap);
	}

	@ResponseBody
	@PostMapping("/save")
	public R save(AuditRuleDO auditRule, @RequestParam("auditRules") String auditRules) {
		if (auditRuleService.save(auditRule, auditRules) > 0) {
			return R.success();
		}
		return R.error();
	}

	@PostMapping("/delete")
	@ResponseBody
	public R remove(String id) {
		int result = auditRuleService.remove(id);
		if (result == 500) {
			return R.error("请先禁用该审批规则");
		}
		if (result > 0) {
			return R.success();
		}
		return R.error();
	}

	/**
	 * 
	 * @Description: 状态禁用/启用
	 *
	 * @param: id 审批规则ID
	 * @param: status 审批规则状态 1-启用 2-禁用
	 * @return: 返回结果描述
	 * @throws: 异常描述
	 *
	 * @author: huangmk
	 * @date: 2019年1月15日 下午2:16:28
	 */
	@PostMapping("/updateStatus")
	@ResponseBody
	public R updateStatus(String id, String status) {
		AuditRuleDO auditRule = new AuditRuleDO();
		auditRule.setId(id);
		auditRule.setRuleStatus(status);
		if (auditRuleService.save(auditRule) > 0) {
			return R.success();
		}
		return R.error();
	}

	/**
	 * 
	 * @Description: 通过规则标识查出当前用户下面要审批的人
	 *
	 * @param: 参数描述
	 * @return: 返回结果描述
	 * @throws: 异常描述
	 *
	 * @version: v1.0.0
	 * @author: lmh
	 * @date: 2019年1月16日 下午5:44:39
	 */
	@PostMapping("/approveByUser")
	@ResponseBody
	public R approveByUser(String ruleSign) {
		Map<String, Object> map = auditRuleService.findApplyUserIds(ShiroUtils.getUserId(), ruleSign);
		return R.success(map);
	}

	/**
	 * 
	 * @Description: 验证规则标识唯一
	 *
	 * @param: 参数描述
	 * @return: 返回结果描述
	 * @throws: 异常描述
	 *
	 * @version: v1.0.0
	 * @author: lmh
	 * @date: 2019年1月21日 下午4:31:05
	 */
	@PostMapping("/checkRuleSign")
	@ResponseBody
	public R checkRuleSign(String id, String ruleSign) {
		int code = auditRuleService.checkRuleSign(id, ruleSign);
		Map<String, Object> map = new HashMap<>();
		boolean isExist = false;
		if (code == 500) {
			isExist = true;
			map.put("isExist", isExist);
			return R.success(map);
		}
		map.put("isExist", isExist);
		return R.success(map);
	}
}
