package com.txdata.oa.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.txdata.oa.domain.NotifyDO;
import com.txdata.common.persistence.proxy.CrudDao;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 通知通告
 * @author 3xdata
 * @email 3xdata@3xdata.cn
 * @date 2019-07-11 14:57:30
 */
@Mapper
public interface NotifyDao extends CrudDao<NotifyDO> {

	NotifyDO get(String id);
	
	Page<NotifyDO> list(Page<NotifyDO> page, @Param("entity")Map<String,Object> map);
	
	List<NotifyDO> list(@Param("entity")Map<String,Object> map);
	
	int insert(NotifyDO notify);
	
	int update(NotifyDO notify);
	
	int remove(String id);
	
	int batchRemove(String[] ids);
	
	int delete(String id);
	
	int batchDelete(String[] ids);
	
	int batchInsert(List<NotifyDO> notifys);
	
	int batchUpdate(List<NotifyDO> notifys);
	
	int count(Map<String, Object> map);
}
