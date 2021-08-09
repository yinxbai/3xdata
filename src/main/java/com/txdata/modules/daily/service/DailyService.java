package com.txdata.modules.daily.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.txdata.modules.daily.domain.DailyDO;
import com.txdata.modules.daily.domain.DailyInfoDO;
import com.txdata.modules.dailyTask.domain.DailyTaskDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface DailyService {
    Page<DailyDO> list(Page<DailyDO> dailyPage,@Param("entity")Map<String,Object> map);
    int save(DailyDO daily);
    int remove(String id);
    DailyDO get(String id);
    List<DailyDO> find(String userName);
    List<DailyDO> finish(String userName);
    int approve(int sign,String procId,String userId);
    List<DailyInfoDO> histoicFlowList(String procInsId);
    void export(HttpServletRequest request,HttpServletResponse response, @Param("entity")Map<String,Object>  params);
    List<DailyTaskDO> getTask(String dailyId);
    List<DailyTaskDO> taskList(String projectId);
}
