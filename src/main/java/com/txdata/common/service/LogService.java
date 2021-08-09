package com.txdata.common.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.txdata.common.dao.LogDao;
import com.txdata.common.domain.LogDO;
import com.txdata.common.domain.PageDO;
import com.txdata.common.persistence.CrudService;
import com.txdata.common.utils.Query;

@Service
public class LogService extends CrudService<LogDao, LogDO> {
	
	@Autowired
	LogDao logMapper;

	@Async
	@Transactional(readOnly = false)
	public void saveLog(LogDO logDO) {
		logDO.preInsert();
		logMapper.save(logDO);
	}

	public PageDO<LogDO> queryList(Query query) {
		int total = logMapper.count(query);
		List<LogDO> logs = logMapper.list(query);
		PageDO<LogDO> page = new PageDO<>();
		page.setTotal(total);
		page.setRows(logs);
		return page;
	}

	@Override
	@Transactional(readOnly = false)
	public int remove(String id) {
		int count = logMapper.remove(id);
		return count;
	}

	@Transactional(readOnly = false)
	public int batchRemove(String[] ids) {
		return logMapper.batchRemove(ids);
	}
}
