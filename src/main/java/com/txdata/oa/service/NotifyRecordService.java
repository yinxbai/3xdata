package com.txdata.oa.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.txdata.oa.domain.NotifyRecordDO;
import com.txdata.oa.dao.NotifyRecordDao;
import com.txdata.common.persistence.CrudService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

/**
 * 通知通告发送记录
 * 
 * @author 3xdata
 * @email 3xdata@3xdata.cn
 * @date 2019-07-12 11:01:06
 */
 @Service
public class NotifyRecordService extends CrudService<NotifyRecordDao, NotifyRecordDO> {

    @Autowired
    private NotifyRecordDao notifyRecordDao;
    
    /**
	 * 通过id查找数据
	 */
    public NotifyRecordDO get(String id){
        return notifyRecordDao.get(id);
    }
    
    /**
	 * 分页查询列表
	 */
    public Page<NotifyRecordDO> page(Page<NotifyRecordDO> page, Map<String, Object> map){
        return notifyRecordDao.list(page, map);
    }
    
    /**
	 * 查询列表
	 */
    public List<NotifyRecordDO> list(Map<String, Object> map){
        return notifyRecordDao.list(map);
    }
    
    /**
	 * 保存/修改
	 */
    @Transactional(readOnly=false)
    public int save(NotifyRecordDO notifyRecord){
        return super.save(notifyRecord);
    }
    
    /**
	 * 通过id逻辑删除
	 */
    @Transactional(readOnly=false)
    public int remove(String id){
        return notifyRecordDao.remove(id);
    }
    
    /**
	 * 通过ids批量逻辑删除
	 */
    @Transactional(readOnly=false)
    public int batchRemove(String[] ids){
        return notifyRecordDao.batchRemove(ids);
    }
    
    /**
	 * 通过id物理删除
	 */
    @Transactional(readOnly=false)
    public int delete(String id){
        return notifyRecordDao.delete(id);
    }
    
    /**
	 * 通过ids物理删除
	 */
    @Transactional(readOnly=false)
    public int batchDelete(String[] ids){
        return notifyRecordDao.batchDelete(ids);
    }
    
    /**
	 * 批量插入
	 */
    @Transactional(readOnly=false)
    public int batchInsert(List<NotifyRecordDO> notifyRecords){
    	return notifyRecordDao.batchInsert(notifyRecords);
    }
    
    /**
	 * 批量修改
	 */
	@Transactional(readOnly=false)
	public int batchUpdate(List<NotifyRecordDO> notifyRecords){
		return notifyRecordDao.batchUpdate(notifyRecords);
	}
    
    /**
	 * 通过id复制一条相同的数据
	 */
    @Transactional(readOnly=false)
    public int copy(String id){
    	int result = 0;
    	NotifyRecordDO notifyRecord = notifyRecordDao.get(id);
    	if (notifyRecord != null){
    		notifyRecord.setId(null);
    		notifyRecord.preInsert();
    		//表结构中name字段不一定存在，故此自动生成代码时注释下列代码，存在时取消注释即可
//    		if (StrUtil.isNotBlank(notifyRecord.getName())){ 
//    			notifyRecord.setName(notifyRecord.getName() + "-复制");
//    		}
    		result = notifyRecordDao.insert(notifyRecord);
    	}
        return result;
    }
    
    /**
	 * 
	 * @Description: 修改（通过自定义的条件进行修改操作）
	 * @param notifyRecord 要被修改的参数
	 * @param whereMap 修改条件
	 * @return: 返回修改数量
	 * @author: 吁新鹏
	 * @date: 2019年7月12日 上午10:51:14
	 */
    @Transactional(readOnly=false)
    public int updateByWhere(NotifyRecordDO notifyRecord, Map<String,Object> whereMap){
    	return notifyRecordDao.updateByWhere(notifyRecord, whereMap);
    }
    
    /**
   	 * 
   	 * @Description: 逻辑删除（通过自定义的条件进行逻辑删除操作）
   	 * @param whereMap 逻辑删除条件
   	 * @return: 返回逻辑删除数量
   	 * @author: 吁新鹏
   	 * @date: 2019年7月12日 上午10:51:14
   	 */
     @Transactional(readOnly=false)
     public int removeByWhere(Map<String,Object> whereMap){
       	return notifyRecordDao.removeByWhere(whereMap);
     }
   	
   	/**
   	 * 
   	 * @Description: 物理删除（通过自定义的条件进行物理删除操作）慎用
   	 * @param whereMap 物理删除条件
   	 * @return: 返回物理删除数量
   	 * @author: 吁新鹏
   	 * @date: 2019年7月12日 上午10:51:14
   	 */
    @Transactional(readOnly=false)
   	public int deleteByWhere(Map<String,Object> whereMap){
   		return notifyRecordDao.deleteByWhere(whereMap);
   	}
}
