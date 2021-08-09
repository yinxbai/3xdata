package com.txdata.oa.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.txdata.oa.domain.NotifyRecordDO;
import com.txdata.common.persistence.proxy.CrudDao;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 通知通告发送记录
 * @author 3xdata
 * @email 3xdata@3xdata.cn
 * @date 2019-07-12 11:01:06
 */
@Mapper
public interface NotifyRecordDao extends CrudDao<NotifyRecordDO> {

	NotifyRecordDO get(String id);
	
	Page<NotifyRecordDO> list(Page<NotifyRecordDO> page, @Param("entity")Map<String,Object> map);
	
	List<NotifyRecordDO> list(@Param("entity")Map<String,Object> map);
	
	int insert(NotifyRecordDO notifyRecord);
	
	int update(NotifyRecordDO notifyRecord);
	
	int remove(String id);
	
	int batchRemove(String[] ids);
	
	int delete(String id);
	
	int batchDelete(String[] ids);
	
	int batchInsert(List<NotifyRecordDO> notifyRecords);
	
	int batchUpdate(List<NotifyRecordDO> notifyRecords);
	
	int updateByWhere(@Param("param")NotifyRecordDO notifyRecord, @Param("where")Map<String,Object> whereMap);
	
	int removeByWhere(@Param("where")Map<String,Object> whereMap);
	
	int deleteByWhere(@Param("where")Map<String,Object> whereMap);
}
