package com.txdata.activiti.dao;

import com.txdata.activiti.domain.ActivitiDO;
import com.txdata.common.persistence.proxy.CrudDao;

/**
 * 审批DAO接口
 * @author thinkgem
 * @version 2014-05-16
 */
public interface ActivitiDao extends CrudDao<ActivitiDO> {

	public int updateProcInsIdByBusinessId(ActivitiDO act);
	
}
