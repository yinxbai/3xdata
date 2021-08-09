package com.txdata.modules.dailyProblem.dao;

import com.txdata.modules.dailyProblem.domain.DailyProblemDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DailyProblemDao {
    int save(DailyProblemDO dailyProblemDO);
    int remove(String id);
    int update(DailyProblemDO dailyProblemDO);
    DailyProblemDO findByDailyId(String dailyId);
}
