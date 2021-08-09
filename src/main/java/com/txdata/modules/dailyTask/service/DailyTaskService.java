package com.txdata.modules.dailyTask.service;


import com.txdata.modules.dailyTask.domain.DailyTaskDO;

public interface DailyTaskService {
    int remove(String id);
    DailyTaskDO findByDailyId(String dailyId);
}
