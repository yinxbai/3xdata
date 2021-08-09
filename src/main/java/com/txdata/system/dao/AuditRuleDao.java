package com.txdata.system.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.txdata.common.persistence.proxy.CrudDao;
import com.txdata.system.domain.AuditRuleDO;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 审批规则相关Dao
 * @author huangmk
 * @email huangmk@3xdata.cn
 * @date 2019-01-15 11:13:47
 */
@Mapper
public interface AuditRuleDao extends CrudDao<AuditRuleDO> {

	AuditRuleDO get(String id);

	Page<AuditRuleDO> list(Page<AuditRuleDO> page, @Param("entity") Map<String, Object> map);

	List<AuditRuleDO> list(@Param("entity") Map<String, Object> map);

	int insert(AuditRuleDO auditRule);

	int update(AuditRuleDO auditRule);

	int remove(String id);

}
