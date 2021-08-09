package com.txdata.modules.dailyArrange.service;

import com.txdata.modules.dailyArrange.domain.DailyArrangeDO;

public interface DailyArrangeService {
    int remove(String id);
    DailyArrangeDO findByDailyId(String dailyId);
}
