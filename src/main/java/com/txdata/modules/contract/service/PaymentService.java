package com.txdata.modules.contract.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.txdata.modules.contract.domain.PaymentDO;
import com.txdata.modules.contract.dao.PaymentDao;
import com.txdata.common.persistence.CrudService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

/**
 * @author InRoota
 * @2021-08-02 09:24:57
 */
 @Service
public class PaymentService extends CrudService<PaymentDao, PaymentDO> {

    @Autowired
    private PaymentDao paymentDao;
    
    /**
	 * 通过id查找数据
	 */
    public PaymentDO get(String id){
        return paymentDao.get(id);
    }
    
    /**
	 * 分页查询列表
	 */
    public Page<PaymentDO> page(Page<PaymentDO> page, Map<String, Object> map){
        return paymentDao.list(page, map);
    }
    
    /**
	 * 查询列表
	 */
    public List<PaymentDO> list(Map<String, Object> map){
        return paymentDao.list(map);
    }
    
    /**
	 * 保存/修改
	 */
    @Transactional(readOnly=false)
    public int save(PaymentDO payment){
        return paymentDao.save(payment);
    }
    
    /**
	 * 通过id逻辑删除
	 */
    @Transactional(readOnly=false)
    public int remove(String id){
        return paymentDao.remove(id);
    }
    
    /**
	 * 通过ids批量逻辑删除
	 */
    @Transactional(readOnly=false)
    public int batchRemove(String[] ids){
        return paymentDao.batchRemove(ids);
    }
    
    /**
	 * 通过id物理删除
	 */
    @Transactional(readOnly=false)
    public int delete(String id){
        return paymentDao.delete(id);
    }
    
    /**
	 * 通过ids物理删除
	 */
    @Transactional(readOnly=false)
    public int batchDelete(String[] ids){
        return paymentDao.batchDelete(ids);
    }
    
    /**
	 * 批量插入
	 */
    @Transactional(readOnly=false)
    public int batchInsert(List<PaymentDO> payments){
    	return paymentDao.batchInsert(payments);
    }
    
    /**
	 * 批量修改
	 */
	@Transactional(readOnly=false)
	public int batchUpdate(List<PaymentDO> payments){
		return paymentDao.batchUpdate(payments);
	}
    
    /**
	 * 通过id复制一条相同的数据
	 */
    @Transactional(readOnly=false)
    public int copy(String id){
    	int result = 0;
    	PaymentDO payment = paymentDao.get(id);
    	if (payment != null){
    		payment.setId(null);
    		payment.preInsert();
    		//表结构中name字段不一定存在，故此自动生成代码时注释下列代码，存在时取消注释即可
//    		if (StrUtil.isNotBlank(payment.getName())){ 
//    			payment.setName(payment.getName() + "-复制");
//    		}
    		result = paymentDao.insert(payment);
    	}
        return result;
    }
    
    /**
	 * 
	 * @Description: 修改（通过自定义的条件进行修改操作）
	 * @param payment 要被修改的参数
	 * @param whereMap 修改条件
	 * @return: 返回修改数量
	 */
	@Transactional(readOnly=false)
    public int updateByWhere(PaymentDO payment, Map<String,Object> whereMap){
    	return paymentDao.updateByWhere(payment, whereMap);
    }
    
    /**
	 * 
	 * @Description: 逻辑删除（通过自定义的条件进行逻辑删除操作）
	 * @param whereMap 逻辑删除条件
	 * @return: 返回逻辑删除数量
	 */
	@Transactional(readOnly=false)
    public int removeByWhere(Map<String,Object> whereMap){
    	return paymentDao.removeByWhere(whereMap);
    }
	
	/**
	 * 
	 * @Description: 物理删除（通过自定义的条件进行物理删除操作）慎用
	 * @param whereMap 物理删除条件
	 * @return: 返回物理删除数量
	 */
	@Transactional(readOnly=false) 
	public int deleteByWhere(Map<String,Object> whereMap){
		return paymentDao.deleteByWhere(whereMap);
	}
}
