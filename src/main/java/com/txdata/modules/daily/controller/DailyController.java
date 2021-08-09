package com.txdata.modules.daily.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.txdata.common.utils.DateUtils;
import com.txdata.common.utils.Query;
import com.txdata.common.utils.R;
import com.txdata.common.utils.StringUtils;
import com.txdata.common.utils.excel.ExcelUtil;
import com.txdata.common.utils.excel.ExportExcel;
import com.txdata.modules.daily.activiti.ActivitiService;
import com.txdata.modules.daily.domain.DailyDO;
import com.txdata.modules.daily.domain.DailyInfoDO;
import com.txdata.modules.daily.service.DailyService;
import com.txdata.modules.dailyArrange.domain.DailyArrangeDO;
import com.txdata.modules.dailyProblem.domain.DailyProblemDO;
import com.txdata.modules.dailyTask.domain.DailyTaskDO;
import com.txdata.system.domain.UserDO;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.Socket;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * 日报主表
 */
@RestController
@RequestMapping("/daily")
public class DailyController {
    @Autowired
    private DailyService dailyService;

    /**
     * 新增日报和更新日报
     * @param dailyDO
     * @return
     */
    @ResponseBody
    @PostMapping("/save")
    public R add(DailyDO dailyDO ){
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(dailyDO.getDailyTaskDOS())) {
            dailyDO.setDailyTasks(JSON.parseArray(dailyDO.getDailyTaskDOS(), DailyTaskDO.class));
        }
        if (StringUtils.isNotBlank(dailyDO.getDailyArrangeDOS())) {
            dailyDO.setDailyArranges(JSON.parseArray(dailyDO.getDailyArrangeDOS(), DailyArrangeDO.class));
        }
        if (StringUtils.isNotBlank(dailyDO.getDailyProblemDOS())) {
            dailyDO.setDailyProblems(JSON.parseArray(dailyDO.getDailyProblemDOS(), DailyProblemDO.class));
        }
        if(dailyService.save(dailyDO)<0){
            return R.error();
        }
        map.put("dailyDO",dailyDO.getId());
        return R.success(map);
    }


    /**
     * 查询分页列表
     * @param params
     * @return
     */
    @ResponseBody
    @PostMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        //查询列表数据
        Query query = new Query(params);
        Page<DailyDO> page = new Page<DailyDO>(query.getPageNo(), query.getPageSize());
        page = dailyService.list(page,query);

        // 封装分页数据
        Map<String,Object> jsonMap = new HashMap<String,Object>();
        jsonMap.put("rows", page.getRecords());
        jsonMap.put("pageSize", page.getSize());
        jsonMap.put("pageNo", page.getCurrent());
        jsonMap.put("count", page.getTotal());
        return R.success(jsonMap);
    }

    /**
     * 删除日报
     * @param id
     * @return
     */
    @ResponseBody
    @PostMapping( "/delete")
    public R remove(String id){
        if(dailyService.remove(id)>0){
            return R.success();
        }
        return R.error();
    }

    /**
     * 查看日报
     * @param id
     * @return
     */
    @ResponseBody
    @PostMapping( "/view")
    public R view(@RequestParam(required = true)String id) {
        DailyDO dailyDO = dailyService.get(id);
        Map<String, Object> map = new HashMap<String, Object>();
        if (dailyDO == null) {
            return R.error("查无此数据！");
        }
        map.put("dailyDO", dailyDO);
        return R.success(map);
    }

    /**
     * 导出日报
     * @param response
     * @param params 查询条件
     */
    @ResponseBody
    @RequestMapping(value = "/export")
    public R export(HttpServletRequest request,HttpServletResponse response, @RequestParam Map<String, Object> params){
        Map<String, Object> map = new HashMap<String, Object>();
        dailyService.export(request,response,params);
        return R.success();
    }


    /**
     * 查询待审批列表
     */
    @ResponseBody
    @PostMapping( "/noApproveList")
    public R queryNoApprove(String userName) {
        List<DailyDO> list = dailyService.find(userName);
        Map<String, Object> map = new HashMap<String, Object>();
        if (list.size() == 0) {
            return R.error("查无此数据！");
        }
        map.put("list", list);
        return R.success(map);
    }
    /**
     * 查询已审批列表
     */
    @ResponseBody
    @PostMapping( "/approveList")
    public R finish(String userName) {
        List<DailyDO> list = dailyService.finish(userName);
        Map<String, Object> map = new HashMap<String, Object>();
        if (list.size() == 0) {
            return R.error("查无此数据！");
        }
        map.put("list", list);
        return R.success(map);
    }
    /**
     * 审批功能
     */
    @ResponseBody
    @PostMapping( "/approve")
    public R approve(int sign,String procId,String userId) {
        int i = dailyService.approve(sign,procId,userId);
        if(i>0){
            return R.success();
        }
        return R.error();
    }

    /**
     * 获取流转历史列表
     */
    @ResponseBody
    @PostMapping( "/history")
    public R historyList(String procInsId) {
        List<DailyInfoDO> list = dailyService.histoicFlowList(procInsId);
        Map<String, Object> map = new HashMap<String, Object>();
        if (list.size() == 0) {
            return R.error("查无此数据！");
        }
        map.put("list", list);
        return R.success(map);
    }

    /**
     * 查看任务明细
     * @param dailyId
     * @return
     */
    @ResponseBody
    @PostMapping("/Taskform")
    public R getTask(String dailyId){
        List<DailyTaskDO> taskDO = dailyService.getTask(dailyId);
        Map<String, Object> map = new HashMap<String, Object>();
        if (taskDO.isEmpty()){
            return R.error("查无此数据！");
        }
        map.put("list", taskDO);
        return R.success(map);
    }

    /**
     * 根据项目查看任务列表
     * @param projectId
     * @return
     */
    @ResponseBody
    @PostMapping("/taskList")
    public R taskList(String projectId){
        List<DailyTaskDO> taskDOList = dailyService.taskList(projectId);
        Map<String, Object> map = new HashMap<String, Object>();
        if (taskDOList.isEmpty()){
            return R.error();
        }
        map.put("list", taskDOList);
        return R.success(map);
    }

    @ResponseBody
    @PostMapping("/task")
    public R getone(String proc_id){
        DailyTaskDO taskDO = (DailyTaskDO) dailyService.find(proc_id);
        Map<String, Object> map = new HashMap<String, Object>();
        if (taskDO.getProjectId()==null){
            return R.error();
        }
        map.put("list", taskDO);
        return R.success(map);
    }
}
