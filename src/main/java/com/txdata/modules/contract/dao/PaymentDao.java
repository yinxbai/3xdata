package com.txdata.modules.contract.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.txdata.modules.contract.domain.PaymentDO;
import com.txdata.common.persistence.proxy.CrudDao;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author InRoota
 * @2021-08-02 09:22:12
 */
@Mapper
public interface PaymentDao extends CrudDao<PaymentDO> {

	PaymentDO get(String id);
	
	Page<PaymentDO> list(Page<PaymentDO> page, @Param("entity")Map<String,Object> map);
	
	List<PaymentDO> list(@Param("entity")Map<String,Object> map);

	/**
	 * 保存支付方式明细
	 * @param payment
	 * @return
	 */
	int save(PaymentDO payment);
	
	int update(PaymentDO payment);
	
	int remove(String Id);
	
	int batchRemove(String[] ids);
	
	int delete(String Id);
	
	int batchDelete(String[] ids);
	
	int batchInsert(List<PaymentDO> payments);
	
	int batchUpdate(List<PaymentDO> payments);
	
	int updateByWhere(@Param("param")PaymentDO payment, @Param("where")Map<String,Object> whereMap);
	
	int removeByWhere(@Param("where")Map<String,Object> whereMap);
	
	int deleteByWhere(@Param("where")Map<String,Object> whereMap);
}
