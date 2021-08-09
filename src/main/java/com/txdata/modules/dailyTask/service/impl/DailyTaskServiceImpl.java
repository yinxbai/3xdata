package com.txdata.modules.dailyTask.service.impl;

import com.txdata.modules.dailyTask.dao.DailyTaskDao;
import com.txdata.modules.dailyTask.domain.DailyTaskDO;
import com.txdata.modules.dailyTask.service.DailyTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DailyTaskServiceImpl implements DailyTaskService {
    @Autowired
    private DailyTaskDao dailyTaskDao;

    @Override
    public int remove(String id) {
        return dailyTaskDao.remove(id);
    }


    @Override
    public DailyTaskDO findByDailyId(String dailyId) {
        return dailyTaskDao.findByDailyId(dailyId);
    }
}
