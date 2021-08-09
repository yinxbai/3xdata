package com.txdata.common.dao;

import com.txdata.common.domain.LogDO;
import com.txdata.common.persistence.proxy.CrudDao;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统日志
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-10-03 15:45:42
 */
@Mapper
public interface LogDao extends CrudDao<LogDO> {

	LogDO get(String id);

	List<LogDO> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	void save(LogDO log);

	int update(LogDO log);

	int remove(String id);

	int batchRemove(String[] ids);
}
