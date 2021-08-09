package com.txdata.modules.dailyProblem.service.impl;

import com.txdata.modules.dailyProblem.dao.DailyProblemDao;
import com.txdata.modules.dailyProblem.domain.DailyProblemDO;
import com.txdata.modules.dailyProblem.service.DailyProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
public class DailyProblemServiceImpl implements DailyProblemService {
    @Autowired
    private DailyProblemDao dailyProblemDao;

    @Override
    public int remove(String id) {
        return dailyProblemDao.remove(id);
    }


    @Override
    public DailyProblemDO findByDailyId(String dailyId) {
        return dailyProblemDao.findByDailyId(dailyId);
    }
}
