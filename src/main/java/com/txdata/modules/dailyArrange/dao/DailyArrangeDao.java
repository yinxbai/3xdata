package com.txdata.modules.dailyArrange.dao;

import com.txdata.modules.dailyArrange.domain.DailyArrangeDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DailyArrangeDao {
    int save(DailyArrangeDO dailyArrangeDO);
    int remove(String id);
    int update(DailyArrangeDO dailyArrangeDO);
    DailyArrangeDO findByDailyId(String dailyId);
}
