package com.txdata.modules.dailyTask.dao;

import com.txdata.modules.dailyTask.domain.DailyTaskDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DailyTaskDao {
    int save(DailyTaskDO dailyTaskDO);
    int remove(String id);
    int update(DailyTaskDO dailyTaskDO);
    DailyTaskDO findByDailyId(String dailyId);
}
