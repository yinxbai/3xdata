package com.txdata.modules.contract.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.txdata.modules.contract.domain.PurchaseDO;
import com.txdata.common.persistence.proxy.CrudDao;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author InRoota
 * @2021-08-02 09:22:23
 */
@Mapper
public interface PurchaseDao extends CrudDao<PurchaseDO> {

	PurchaseDO get(String id);
	
	Page<PurchaseDO> list(Page<PurchaseDO> page, @Param("entity")Map<String,Object> map);
	
	List<PurchaseDO> list(@Param("entity")Map<String,Object> map);

	/**
	 * 保存已支付明细
	 * @param purchase
	 * @return
	 */
	int save(PurchaseDO purchase);
	
	int update(PurchaseDO purchase);
	
	int remove(String id);
	
	int batchRemove(String[] ids);
	
	int delete(String id);
	
	int batchDelete(String[] ids);
	
	int batchInsert(List<PurchaseDO> purchases);
	
	int batchUpdate(List<PurchaseDO> purchases);
	
	int updateByWhere(@Param("param")PurchaseDO purchase, @Param("where")Map<String,Object> whereMap);
	
	int removeByWhere(@Param("where")Map<String,Object> whereMap);
	
	int deleteByWhere(@Param("where")Map<String,Object> whereMap);
}
