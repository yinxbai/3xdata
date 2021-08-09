package com.txdata.common.dao;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import com.txdata.common.domain.JobDO;
import com.txdata.common.persistence.proxy.CrudDao;

/**
 * 定时任务
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-10-03 15:45:42
 */
@Mapper
public interface JobDao extends CrudDao<JobDO> {

	JobDO get(String id);

	List<JobDO> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	int save(JobDO task);

	int update(JobDO task);

	int remove(String id);

	int batchRemove(String[] ids);
}
