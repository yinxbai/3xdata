package com.txdata.system.service;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.txdata.system.config.AuditRuleConstant;
import com.txdata.system.dao.AuditRuleDao;
import com.txdata.system.dao.AuditRuleDetailDao;
import com.txdata.system.dao.UserDao;
import com.txdata.system.domain.AuditRuleDO;
import com.txdata.system.domain.AuditRuleDetailDO;
import com.txdata.system.domain.UserDO;
import com.txdata.system.service.UserService;
import cn.hutool.core.util.StrUtil;

import com.txdata.common.persistence.CrudService;
import com.txdata.common.utils.IdGen;
import com.txdata.common.utils.StringUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

/**
 * 
 * 审批规则相关Service
 * 
 * @author huangmk
 * @email huangmk@3xdata.cn
 * @date 2019-01-15 11:13:47
 */
@Service
public class AuditRuleService extends CrudService<AuditRuleDao, AuditRuleDO> {

	@Autowired
	private AuditRuleDao auditRuleDao;
	@Autowired
	private AuditRuleDetailDao auditRuleDetailDao;
	@Autowired
	private UserService userService;
	@Autowired
	private UserDao userDao;

	public AuditRuleDO get(String id) {
		return auditRuleDao.get(id);
	}

	public AuditRuleDO getInfo(String id) {
		AuditRuleDO auditRule = auditRuleDao.get(id);
		if (auditRule != null) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("ruleId", auditRule.getId());
			List<AuditRuleDetailDO> detailList = auditRuleDetailDao.list(param);
			for (AuditRuleDetailDO detail : detailList) {
				String userIds = detail.getApplyUserIds();
				List<UserDO> userList = userService.listByUserIds(userIds);
				String applyUserNames = "";
				if (userList != null) {
					for (int i = 0; i < userList.size(); i++) {
						UserDO user = userList.get(i);
						String name = user.getName();
						if (i == 0) {
							applyUserNames = name;
						} else {
							applyUserNames = applyUserNames + "," + name;
						}
					}
				}
				detail.setApplyUserNames(applyUserNames);
			}
			auditRule.setDetailList(detailList);
		}
		return auditRule;
	}

	public Page<AuditRuleDO> page(Page<AuditRuleDO> page, Map<String, Object> map) {
		return auditRuleDao.list(page, map);
	}

	public List<AuditRuleDO> list(Map<String, Object> map) {
		return auditRuleDao.list(map);
	}

	@Transactional(readOnly = false)
	public int save(AuditRuleDO auditRule) {
		return super.save(auditRule);
	}

	/**
	 * 
	 * @Description: 保存审批规则，包括明细
	 *
	 * @param: auditRule 审批规则
	 * @param: details 审批规则明细
	 * @return: 返回结果描述
	 * @throws: 异常描述
	 *
	 * @author: huangmk
	 * @date: 2019年1月15日 下午2:34:45
	 */
	@Transactional(readOnly = false)
	public int save(AuditRuleDO auditRule, String details) {
		String auditRuleId = auditRule.getId();
		if (StringUtils.isNotBlank(auditRuleId)) {
			// 删除审批规则明细
			auditRuleDetailDao.deleteByRuleId(auditRuleId);
		}
		int row = super.save(auditRule);
		if (StringUtils.isNotBlank(details)) {
			details = details.replace("&quot;", "\"");
			List<AuditRuleDetailDO> detailList = JSONArray.parseArray(details, AuditRuleDetailDO.class);
			saveDetailList(detailList, auditRule.getId());
		}
		return row;
	}

	/**
	 * 
	 * @Description: 保存明细列表
	 *
	 * @param: detailList 明细列表
	 * @param: auditRuleId 审批规则ID
	 * @return: 返回结果描述
	 * @throws: 异常描述
	 *
	 * @author: huangmk
	 * @date: 2019年1月15日 下午2:33:45
	 */
	@Transactional(readOnly = false)
	public void saveDetailList(List<AuditRuleDetailDO> detailList, String auditRuleId) {
		int index = 0;
		for (AuditRuleDetailDO detail : detailList) {
			detail.setId(IdGen.uuid());
			detail.setRuleId(auditRuleId);
			index++;
			detail.setSort(index);
			auditRuleDetailDao.insert(detail);
		}
	}

	@Transactional(readOnly = false)
	public int remove(String id) {
		if ("1".equals(auditRuleDao.get(id).getRuleStatus())) {
			return 500;
		}
		return auditRuleDao.remove(id);
	}

	/**
	 * 
	 * @Description: 根据标识获取启用的审核规则，再根据审核人id和审核规则id获取所有申请人id
	 *
	 * @param: 参数描述
	 * @return: 返回结果描述
	 * @throws: 异常描述
	 *
	 * @version: v1.0.0
	 * @author: lmh
	 * @date: 2019年1月15日 下午5:16:37
	 */
	public Map<String, Object> findApplyUserIds(String userId, String ruleSign) {
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> map = new HashMap<>();
		map.put("ruleSign", ruleSign);
		map.put("ruleStatus", AuditRuleConstant.RULE_STATUS_ENABLE);
		List<AuditRuleDO> list = auditRuleDao.list(map);
		AuditRuleDO ruleEntity = new AuditRuleDO();
		String userIds = "";
		String userName = "";
		if (list.size() > 0) {
			ruleEntity = list.get(0);
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("auditUserId", userId);
			param.put("ruleId", ruleEntity.getId());
			List<AuditRuleDetailDO> detailList = auditRuleDetailDao.list(param);
			// 遍历获取审批规则明细，获取申请人id和申请人名称
			for (AuditRuleDetailDO detail : detailList) {
				String[] ids = detail.getApplyUserIds().split(",");
				for (String id : ids) {
					userName = userDao.get(id).getName() + "," + userName;
				}
				userIds = detail.getApplyUserIds() + "," + userIds;
			}
			resultMap.put("applyUserIds", StrUtil.sub(userIds, 0, -1));
			resultMap.put("applyUserNames", StrUtil.sub(userName, 0, -1));
		}
		return resultMap;
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
	 * @date: 2019年1月21日 下午4:31:21
	 */
	public int checkRuleSign(String id, String ruleSign) {
		Map<String, Object> map = new HashMap<>();
		map.put("ruleSign", ruleSign);
		List<AuditRuleDO> detailList = auditRuleDao.list(map);
		if (StringUtils.isBlank(id)) {
			if (detailList.size() > 0) {
				return 500; // 规则标识不唯一
			}
		} else {
			for (AuditRuleDO detail : detailList) {
				if (!id.equals(detail.getId())) {
					return 500; // 规则标识不唯一
				}
			}
		}
		return 200; // 规则标识唯一
	}
}
