package com.txdata.modules.contract.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.txdata.modules.contract.domain.ContractProjectDO;
import com.txdata.common.persistence.proxy.CrudDao;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 
 * @author 3xdata
 * @email 3xdata@3xdata.cn
 * @date 2021-08-02 16:42:56
 */
@Mapper
public interface ContractProjectDao extends CrudDao<ContractProjectDO> {

	ContractProjectDO get(String id);

	Page<ContractProjectDO> list(Page<ContractProjectDO> page, @Param("entity") Map<String, Object> map);

	List<ContractProjectDO> list(@Param("entity") Map<String, Object> map);

	/**
	 * 保存项目明细
	 * @param project
	 * @return
	 */
	int save(ContractProjectDO project);

	int update(ContractProjectDO project);

	int remove(String id);

	int delete(String id);
}
