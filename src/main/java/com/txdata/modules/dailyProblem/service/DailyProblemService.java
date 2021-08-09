package com.txdata.modules.dailyProblem.service;

import com.txdata.modules.dailyProblem.domain.DailyProblemDO;

import java.util.List;

public interface DailyProblemService {
    int remove(String id);
    DailyProblemDO findByDailyId(String dailyId);
}
