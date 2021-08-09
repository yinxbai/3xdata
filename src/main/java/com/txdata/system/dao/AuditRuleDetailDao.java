package com.txdata.system.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.txdata.common.persistence.proxy.CrudDao;
import com.txdata.system.domain.AuditRuleDetailDO;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 审批规则明细相关Dao
 * 
 * @author huangmk
 * @email huangmk@3xdata.cn
 * @date 2019-01-15 11:14:32
 */
@Mapper
public interface AuditRuleDetailDao extends CrudDao<AuditRuleDetailDO> {

	AuditRuleDetailDO get(String id);

	Page<AuditRuleDetailDO> list(Page<AuditRuleDetailDO> page, @Param("entity") Map<String, Object> map);

	List<AuditRuleDetailDO> list(@Param("entity") Map<String, Object> map);

	int insert(AuditRuleDetailDO auditRuleDetail);

	int update(AuditRuleDetailDO auditRuleDetail);

	int remove(String id);

	int batchRemove(String[] ids);
	
	int deleteByRuleId(String auditRuleId);
}
