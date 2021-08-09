package com.txdata.modules.daily.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.txdata.modules.daily.domain.DailyDO;
import com.txdata.modules.dailyTask.domain.DailyTaskDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 日报基本信息表
 * @author InRoota
 */
@Mapper
public interface DailyDao {
    /**
     * 查询日报列表
     * @param dailyPage
     * @param map
     * @return
     */
    Page<DailyDO> list(Page<DailyDO> dailyPage,@Param("entity")Map<String,Object> map);

    /**
     * 新增日报
     * @param daily
     * @return
     */
    int save(DailyDO daily);

    /**
     * 新增日报
     * @param dailyDO
     * @return
     */
    int add(DailyDO dailyDO);
    /**
     * 删除日报表
     * @param id
     * @return
     */
    int remove(String id);

    /**
     * 删除任务表
     * @param id
     * @return
     */
    int deleteTask(String id);

    /**
     * 删除安排表
     * @param id
     * @return
     */
    int deleteArrange(String id);

    /**
     * 删除问题表
     * @param id
     * @return
     */
    int deleteProblem(String id);

    /**
     * 修改日报
     * @param daily
     * @return
     */
    int update(DailyDO daily);

    /**
     * 获取单条日报
     * @param id
     * @return
     */
    DailyDO get(String id);

    /**
     * 日报汇报日期校验
     * @param reportDate
     * @param writeUser
     * @return
     */
    int check(Date reportDate, String writeUser);

    /**
     * 导出日报
     * @param dailyPage
     * @param map
     * @return
     */
    Page<DailyDO> export(Page<DailyDO> dailyPage,@Param("entity")Map<String,Object> map);

    /**
     *根据项目Id查询日报1
     * @param procId
     * @return
     */
    DailyDO findTask(String procId);

    /**
     * 审批日报
     * @param procId
     * @param status
     * @return
     */
    int updateStatus(String procId,String status);

    /**
     * 根据项目Id查询日报2
     * @param dailyId
     * @return
     */
    List<DailyTaskDO> getTask(String dailyId);

    /**
     * 获取任务列表
     * @param projectId
     * @return
     */
    List<DailyTaskDO> taskList(String projectId);
}
