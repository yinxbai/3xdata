package com.txdata.modules.dailyArrange.service.impl;

import com.txdata.modules.dailyArrange.dao.DailyArrangeDao;
import com.txdata.modules.dailyArrange.domain.DailyArrangeDO;
import com.txdata.modules.dailyArrange.service.DailyArrangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DailyArrangeServiceImpl implements DailyArrangeService {
    @Autowired
    private DailyArrangeDao dailyArrangeDao;

    @Override
    public int remove(String id) {
        return dailyArrangeDao.remove(id);
    }


    @Override
    public DailyArrangeDO findByDailyId(String dailyId) {
        return dailyArrangeDao.findByDailyId(dailyId);
    }
}
