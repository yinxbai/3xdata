package com.txdata.modules.contract.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.txdata.modules.contract.domain.PurchaseDO;
import com.txdata.modules.contract.dao.PurchaseDao;
import com.txdata.common.persistence.CrudService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

/**
 * @author InRoota
 * @2021-08-02 09:24:43
 *
 */
 @Service
public class PurchaseService extends CrudService<PurchaseDao, PurchaseDO> {

    @Autowired
    private PurchaseDao purchaseDao;
    
    /**
	 * 通过id查找数据
	 */
    public PurchaseDO get(String id){
        return purchaseDao.get(id);
    }
    
    /**
	 * 分页查询列表
	 */
    public Page<PurchaseDO> page(Page<PurchaseDO> page, Map<String, Object> map){
        return purchaseDao.list(page, map);
    }
    
    /**
	 * 查询列表
	 */
    public List<PurchaseDO> list(Map<String, Object> map){
        return purchaseDao.list(map);
    }
    
    /**
	 * 保存/修改
	 */
    @Transactional(readOnly=false)
    public int save(PurchaseDO purchase){
        return purchaseDao.save(purchase);
    }
    
    /**
	 * 通过id逻辑删除
	 */
    @Transactional(readOnly=false)
    public int remove(String id){
        return purchaseDao.remove(id);
    }
    
    /**
	 * 通过ids批量逻辑删除
	 */
    @Transactional(readOnly=false)
    public int batchRemove(String[] ids){
        return purchaseDao.batchRemove(ids);
    }
    
    /**
	 * 通过id物理删除
	 */
    @Transactional(readOnly=false)
    public int delete(String id){
        return purchaseDao.delete(id);
    }
    
    /**
	 * 通过ids物理删除
	 */
    @Transactional(readOnly=false)
    public int batchDelete(String[] ids){
        return purchaseDao.batchDelete(ids);
    }
    
    /**
	 * 批量插入
	 */
    @Transactional(readOnly=false)
    public int batchInsert(List<PurchaseDO> purchases){
    	return purchaseDao.batchInsert(purchases);
    }
    
    /**
	 * 批量修改
	 */
	@Transactional(readOnly=false)
	public int batchUpdate(List<PurchaseDO> purchases){
		return purchaseDao.batchUpdate(purchases);
	}
    
    /**
	 * 通过id复制一条相同的数据
	 */
    @Transactional(readOnly=false)
    public int copy(String id){
    	int result = 0;
    	PurchaseDO purchase = purchaseDao.get(id);
    	if (purchase != null){
    		purchase.setId(null);
    		purchase.preInsert();
    		//表结构中name字段不一定存在，故此自动生成代码时注释下列代码，存在时取消注释即可
//    		if (StrUtil.isNotBlank(purchase.getName())){ 
//    			purchase.setName(purchase.getName() + "-复制");
//    		}
    		result = purchaseDao.insert(purchase);
    	}
        return result;
    }
    
    /**
	 * 
	 * @Description: 修改（通过自定义的条件进行修改操作）
	 * @param purchase 要被修改的参数
	 * @param whereMap 修改条件
	 * @return: 返回修改数量
	 */
	@Transactional(readOnly=false)
    public int updateByWhere(PurchaseDO purchase, Map<String,Object> whereMap){
    	return purchaseDao.updateByWhere(purchase, whereMap);
    }
    
    /**
	 * 
	 * @Description: 逻辑删除（通过自定义的条件进行逻辑删除操作）
	 * @param whereMap 逻辑删除条件
	 * @return: 返回逻辑删除数量
	 */
	@Transactional(readOnly=false)
    public int removeByWhere(Map<String,Object> whereMap){
    	return purchaseDao.removeByWhere(whereMap);
    }
	
	/**
	 * 
	 * @Description: 物理删除（通过自定义的条件进行物理删除操作）慎用
	 * @param whereMap 物理删除条件
	 * @return: 返回物理删除数量
	 */
	@Transactional(readOnly=false) 
	public int deleteByWhere(Map<String,Object> whereMap){
		return purchaseDao.deleteByWhere(whereMap);
	}
}
