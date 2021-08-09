package com.txdata.modules.daily.service.impl;

import com.alibaba.druid.sql.visitor.functions.If;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.txdata.common.utils.DateUtils;
import com.txdata.common.utils.IdGen;
import com.txdata.common.utils.Query;
import com.txdata.modules.daily.dao.DailyDao;
import com.txdata.modules.daily.dao.MyUserDao;
import com.txdata.modules.daily.domain.DailyDO;
import com.txdata.modules.daily.domain.DailyInfoDO;
import com.txdata.modules.daily.domain.MyUserDO;
import com.txdata.modules.daily.service.DailyService;
import com.txdata.modules.dailyArrange.dao.DailyArrangeDao;
import com.txdata.modules.dailyArrange.domain.DailyArrangeDO;
import com.txdata.modules.dailyProblem.dao.DailyProblemDao;
import com.txdata.modules.dailyProblem.domain.DailyProblemDO;
import com.txdata.modules.dailyTask.dao.DailyTaskDao;
import com.txdata.modules.dailyTask.domain.DailyTaskDO;

import com.txdata.system.utils.UserUtils;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

@Service
public class DailyServiceImpl implements DailyService {

    @Autowired
    private DailyDao dailyDao; // 获取主表的dao对象
    @Autowired
    private DailyTaskDao dailyTaskDao; // 获取任务明细表的dao对象
    @Autowired
    private DailyArrangeDao dailyArrangeDao; // 获取下一步安排表的dao对象
    @Autowired
    private DailyProblemDao dailyProblemDao; // 获取项目问题表的dao对象
    @Autowired
    private MyUserDao myUserDao; // 获取用户类
    @Autowired
    TaskService taskService;
    @Autowired
    HistoryService historyService;
    // 引擎配置
    ProcessEngineConfiguration pec = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml") // 读取配置文件
            .setActivityFontName("宋体").setLabelFontName("宋体").setAnnotationFontName("宋体"); // 设置字体格式

    // 获取流程引擎对象
    ProcessEngine processEngine=pec.buildProcessEngine();
    // 获取查询集合
    List<DailyDO> resultList = new ArrayList<>();

    /**
     * 日报列表和查询
     * @param dailyPage
     * @param map
     * @return
     */
    @Override
    public Page<DailyDO> list(Page<DailyDO> dailyPage,@Param("entity") Map<String,Object> map) {
        return dailyDao.list(dailyPage,map);
    }

    /**
     * 新增和更新日报
     * @param dailyDO
     * @return
     */
    @Transactional(readOnly = false)
    @Override
    public int save(DailyDO dailyDO) {
        // 判断是否存在主键ID
        if(dailyDO.getId()==null||dailyDO.getId()==""){
            // 判断是否符合基本校验
            if(dailyDao.check(dailyDO.getReportDate(),dailyDO.getWriteUser())==0 //判断汇报日期是否已经汇报
                    && dailyDO.getDailyArranges().size()>=1 // 下一步安排表必须大于等于1
                    && dailyDO.getDailyTasks().size()>=1 // 日报明细表必须大于等于1
             ) {
                processEngine.getRepositoryService() // 生成本地库服务
                        .createDeployment() // 创建部署类
                        .addClasspathResource("bpmn/daily.bpmn") //加载流程文件
                        .addClasspathResource("bpmn/daily.png") // 加载流程图片
                        .name("日报审查") // 设置名称
                        .deploy(); // 部署
                //获取到RuntimeService对象
                RuntimeService runtimeService = processEngine.getRuntimeService();
                // 封装数据
                DailyInfoDO dailyInfoDO = new DailyInfoDO();
                // 存储职位
                dailyInfoDO.setJob(dailyDO.getWriteJob());
                // 存储用户名
                dailyInfoDO.setUserId(dailyDO.getWriteUser());
                // 生成map集合
                Map<String, Object> map = new HashMap<>();
                // 将信息封装到map中
                map.put("dailyInfoDO",dailyInfoDO);
                //创建流程实例
                ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("daily",map);
                //获得流程实例id
                String procId = processInstance.getId();
                // 将流程实例id封装到对象中
                dailyInfoDO.setProcId(procId);
                dailyDO.setProcId(procId);
                // 新增日报
                String id = IdGen.uuid(); // 获得主键ID
                dailyDO.setId(id);
                // 将填写日期存入日报对象
                dailyDO.setWriteDate(new Date());
                // 自动生成日报名称
                String title = dailyDO.getWriteUser() + "_" + dailyDO.getReportDate() + "_日报";
                dailyDO.setTitle(title);
                // 设置附件上传的路径
                String path = "/upload/" + dailyDO.getReportDate().toString();
                dailyDO.setFilePath(path);
                // 保存日报主表对象
                int num = dailyDao.save(dailyDO);

                // 将对象中的子表对象进行转化操作
                List<DailyTaskDO> dailyTaskDOS = dailyDO.getDailyTasks();
                List<DailyArrangeDO> dailyArrangeDOS = dailyDO.getDailyArranges();
                List<DailyProblemDO> dailyProblemDOS = dailyDO.getDailyProblems();

                // 循环保存任务详细表
                dailyTaskDOS.stream().filter(Objects::nonNull).forEach(dailyTaskDO -> {
                    // 存储任务明细表对象的id
                    dailyTaskDO.setId(IdGen.uuid());
                    // 存储任务明细表对象中日报id的值
                    dailyTaskDO.setDailyId(id);
                    // 保存
                    dailyTaskDao.save(dailyTaskDO);
                });
                /*for (DailyTaskDO dailyTaskDO : dailyTaskDOS) {
                    // 判断是否存在任务明细表对象
                    if (dailyTaskDO != null) {
                        // 存储任务明细表对象的id
                        dailyTaskDO.setId(IdGen.uuid());
                        // 存储任务明细表对象中日报id的值
                        dailyTaskDO.setDailyId(id);
                        // 保存
                        dailyTaskDao.save(dailyTaskDO);
                    }
                }*/
                // 循环保存下一步安排表
                    dailyArrangeDOS.stream().filter(Objects::nonNull).forEach(dailyArrangeDO -> {
                        // 存储下一步安排对象的id
                        dailyArrangeDO.setId(IdGen.uuid());
                        // 存储下一步安排对象中日报id的值
                        dailyArrangeDO.setDailyId(id);
                        // 保存
                        dailyArrangeDao.save(dailyArrangeDO);
                    });
                /*for (DailyArrangeDO dailyArrangeDO : dailyArrangeDOS) {
                    // 判断是否存在下一步安排对象
                    if (dailyArrangeDO != null) {
                        // 存储下一步安排对象的id
                        dailyArrangeDO.setId(IdGen.uuid());
                        // 存储下一步安排对象中日报id的值
                        dailyArrangeDO.setDailyId(id);
                        // 保存
                        dailyArrangeDao.save(dailyArrangeDO);
                    }
                }*/
                if (dailyDO.getDailyProblems()!=null){
                    // 循环保存问题表
                    dailyProblemDOS.stream().filter(Objects::nonNull).forEach(dailyProblemDO -> {
                        // 存储项目问题对象的id
                        dailyProblemDO.setId(IdGen.uuid());
                        // 存储项目问题对象中日报id的值
                        dailyProblemDO.setDailyId(id);
                        // 保存
                        dailyProblemDao.save(dailyProblemDO);
                    });
                    /*for (DailyProblemDO dailyProblemDO : dailyProblemDOS) {
                        // 判断是否存在项目问题对象
                        if (dailyProblemDO != null) {
                            // 存储项目问题对象的id
                            dailyProblemDO.setId(IdGen.uuid());
                            // 存储项目问题对象中日报id的值
                            dailyProblemDO.setDailyId(id);
                            // 保存
                            dailyProblemDao.save(dailyProblemDO);
                        }
                    }*/
                }else {
                    return 0;
                }

                // 根据流程实例id查询一个任务
                Task task = taskService.createTaskQuery().processInstanceId(procId).singleResult();
                // 将任务id存入活动对象中
                dailyInfoDO.setTaskId(task.getId());
                // 认领任务
                taskService.claim(task.getId(),dailyInfoDO.getUserId());
                // 提交任务
                taskService.complete(task.getId(),map);
                // 返回保存的结果
                return num;
            }else {
                return 0;
            }
        }else {
            // 若含有主键则进行更新操作，将对象中的子表对象进行转化操作
            List<DailyTaskDO> dailyTaskDOS = dailyDO.getDailyTasks();
            List<DailyArrangeDO> dailyArrangeDOS = dailyDO.getDailyArranges();
            List<DailyProblemDO> dailyProblemDOS = dailyDO.getDailyProblems();

            dailyTaskDOS.stream().filter(Objects::nonNull).forEach(dailyTaskDO -> {
                dailyTaskDao.update(dailyTaskDO);
            });
            // 循环保存任务详细表
            /*for (DailyTaskDO dailyTaskDO:dailyTaskDOS) {
                // 判断是否存在任务明细表对象
                if (dailyTaskDO!=null) {
                    dailyTaskDao.update(dailyTaskDO); // 更新
                }
            }*/
            dailyArrangeDOS.stream().filter(Objects::nonNull).forEach(dailyArrangeDO -> {
                dailyArrangeDao.update(dailyArrangeDO);
            });
            // 循环保存下一步安排表
            /*for (DailyArrangeDO dailyArrangeDO:dailyArrangeDOS) {
                // 判断是否存在下一步安排表对象
                if (dailyArrangeDO!=null) {
                    dailyArrangeDao.update(dailyArrangeDO);// 更新
                }
            }*/
            dailyProblemDOS.stream().filter(Objects::nonNull).forEach(dailyProblemDO -> {
                dailyProblemDao.update(dailyProblemDO);
            });
            // 循环保存项目问题表
           /* for (DailyProblemDO dailyProblemDO:dailyProblemDOS) {
                // 判断是否存在项目问题表对象
                if (dailyProblemDO!=null) {
                    dailyProblemDao.update(dailyProblemDO);// 更新
                }
            }*/
            return dailyDao.update(dailyDO);// 返回更新结果
        }

    }

    /**
     * 删除日报
     * @param id
     * @return
     */
    @Override
    public int remove(String id) {
        if (dailyDao.get(id)!=null){
            dailyDao.deleteTask(id);
            dailyDao.deleteArrange(id);
            dailyDao.deleteProblem(id);
        }
        return dailyDao.remove(id);
    }

    /**
     * 查看日报
     * @param id
     * @return
     */
    @Override
    public DailyDO get(String id) {
        return dailyDao.get(id);
    }

    /**
     * 查询待审批列表
     * @param userName
     * @return
     */
    @Override
    public List<DailyDO> find(String userName){
        // 查询待审批列表
        List<Task> taskList = taskService.createTaskQuery().taskAssignee(userName).list();
        List<DailyDO> resultList = taskList.stream().map(task -> dailyDao.findTask(task.getProcessInstanceId())).collect(Collectors.toList());
        /*for (Task task : taskList) {
            // 将查询出的结果封装到list集合中
            DailyDO dailyDO = dailyDao.findTask(task.getProcessInstanceId());
            dailyDO.setProcId(task.getProcessInstanceId());
            resultList.add(dailyDO);
        }*/
        return resultList;
    }

    /**
     *查询已审批列表
     * @param userName
     * @return
     */
    @Override
    public List<DailyDO> finish(String userName){
        // 获取历史服务
        HistoryService historyService = processEngine.getHistoryService();
        // 查询已审批列表
        List<HistoricTaskInstance> taskList  = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(userName)
                .finished()
                .list();
        List<DailyDO> resultList = taskList.stream().map(task -> dailyDao.findTask(task.getProcessInstanceId())).collect(Collectors.toList());
        // 循环查询出来的结果
        /*for (HistoricTaskInstance historicTaskInstance : taskList) {
            // 查询对应的主表对象
            DailyDO dailyDO = dailyDao.findTask(historicTaskInstance.getProcessInstanceId());
            if(dailyDO!=null){
                // 将查询出的结果封装到list集合中
                resultList.add(dailyDO);
            }
        }*/
        return resultList;
    }

    /**
     * 审批日报
     */
    @Override
    public int approve(int sign, String procId, String userId){
        // 获取活动对象
        DailyInfoDO dailyInfoDO = new DailyInfoDO();
        // 查询用户名和职位
        MyUserDO myUserDO = myUserDao.findById(userId);
        // 用户名
        String name = myUserDO.getName();
        // 职位
        String job = myUserDO.getPosition();
        dailyInfoDO.setProcId(procId); // 封装流程实例id
        dailyInfoDO.setJob(job); // 封装职位
        // 查询任务并获得任务id
        Task task = taskService.createTaskQuery().processInstanceId(dailyInfoDO.getProcId()).singleResult();
        dailyInfoDO.setTaskId(task.getId()); // 封装
        Map<String, Object> map = new HashMap<>();
        // 返回结果
        int num = 0;
        switch (job){
            case "项目经理":
                dailyInfoDO.setApprover1(name); // 封装名字
                map.put("dailyInfoDO",dailyInfoDO);
                map.put("sign",sign);
                // 认领任务
                taskService.setAssignee(task.getId(),dailyInfoDO.getApprover1());
                // 提交任务
                taskService.complete(dailyInfoDO.getTaskId(), map);
                if(sign==1){
                    num = 1;
                }else {
                    num = dailyDao.updateStatus(procId,"3");
                }
                break;
            case "高层领导":
                dailyInfoDO.setApprover2(name); // 封装名字
                map.put("flag",sign);
                // 认领任务
                taskService.setAssignee(task.getId(),dailyInfoDO.getApprover2());
                // 提交任务
                taskService.complete(dailyInfoDO.getTaskId(),map);
                if(sign==1){
                    num = dailyDao.updateStatus(procId,"2");
                }else {
                    num = dailyDao.updateStatus(procId,"3");
                }
                break;
        }
        return num;
    }

    /**
     * 获取流转历史列表
     *
     * @param procInsId
     *            流程实例
     */
    @Override
    public List<DailyInfoDO> histoicFlowList(String procInsId) {
        // 生成活动对象集合
        List<DailyInfoDO> dailyInfoDOList = new ArrayList<>();
        // 设置备注为空
        String remark = null;
        // 查询历史记录
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(procInsId).finished().orderByHistoricActivityInstanceStartTime().asc() // 排序
                .list();
        // 判断list集合是否为空
        if (list != null && list.size() > 0) {
            // 循环list
            for (HistoricActivityInstance activityInstance : list) {
                DailyInfoDO dailyInfoDO = new DailyInfoDO();
                // 封装数据
                dailyInfoDO.setActivityName(activityInstance.getActivityName()); // 活动名称
                dailyInfoDO.setAssigneeName(activityInstance.getAssignee()); // 办理人
                dailyInfoDO.setStartDate(activityInstance.getStartTime()); // 开始时间
                dailyInfoDO.setEndDate(activityInstance.getEndTime()); // 结束时间
                // 添加结果
                dailyInfoDOList.add(dailyInfoDO);
            }
        }
        return dailyInfoDOList;
    }

    @Override
    public void export(HttpServletRequest request,HttpServletResponse response, Map<String, Object> params){

        //乱码
        //字符乱码
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("utf-8");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        // 生成excel的表名
        String fileName = UserUtils.getUser().getName() + DateUtils.getDate("yyyy-MM-dd") + "_日报"+".xlsx";
        SXSSFWorkbook wb = new SXSSFWorkbook();
        Sheet sheet = wb.createSheet("日报表");
        // 设置导出样式
        CellStyle style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        Font titleFont = wb.createFont();
        titleFont.setFontName("宋体");
        titleFont.setFontHeightInPoints((short) 12);
        titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style.setFont(titleFont);
        // 设置第一行
        Row row1 = sheet.createRow(0);
        // 日报的基本信息
        row1.createCell(0).setCellValue("序号");
        row1.createCell(1).setCellValue("日报名称");
        row1.createCell(2).setCellValue("日报编号");
        row1.createCell(3).setCellValue("填写人");
        row1.createCell(4).setCellValue("汇报日期");
        row1.createCell(5).setCellValue("职位");
        // 子表的信息
        row1.createCell(6).setCellValue("任务明细");
        row1.createCell(11).setCellValue("下一步工作安排");
        row1.createCell(14).setCellValue("项目问题");
        // 设置第二行
        Row row2 = sheet.createRow(1);
        // 任务详细表
        row2.createCell(6).setCellValue("任务类型");
        row2.createCell(7).setCellValue("项目名称");
        row2.createCell(8).setCellValue("花费时间(时)");
        row2.createCell(9).setCellValue("任务完成度(%)");
        row2.createCell(10).setCellValue("工作任务描述");
        // 下一步安排表
        row2.createCell(11).setCellValue("任务类型");
        row2.createCell(12).setCellValue("计划完成日期");
        row2.createCell(13).setCellValue("下一步工作安排");
        // 项目问题表
        row2.createCell(14).setCellValue("问题类型");
        row2.createCell(15).setCellValue("项目名称");
        row2.createCell(16).setCellValue("期望答复时间");
        row2.createCell(17).setCellValue("需要得到的支持");
        row2.createCell(18).setCellValue("问题描述");
        // 设置表头样式
        row1.setRowStyle(style);
        row2.setRowStyle(style);
        // 合并单元格生成表头
        for(int i=0;i<=5;i++) {
            merger(0, 1, i, sheet);
        }
        // 合并任务详细表
        CellRangeAddress region7 = new CellRangeAddress(0, 0, 6, 10);
        sheet.addMergedRegion(region7);
        // 合并下一步安排表
        CellRangeAddress region8 = new CellRangeAddress(0, 0, 11, 13);
        sheet.addMergedRegion(region8);
        // 合并项目问题表
        CellRangeAddress region9 = new CellRangeAddress(0, 0, 14, 18);
        sheet.addMergedRegion(region9);

        // 获取数据
        Query query = new Query(params);
        Page<DailyDO> page = new Page<DailyDO>(query.getPageNo(), query.getPageSize());
        // 查询数据
        page = dailyDao.export(page,params);
        List<DailyDO> list = page.getRecords();
        int max = 0;
        int height = 2;
        // 循环查询出来的list集合
        for(int i=0;i<list.size();i++) {
            // 得到主表对象
            DailyDO dailyDO = list.get(i);
            // 获得下一步安排表的列表
            List<DailyArrangeDO> dailyArrangeDOList = dailyDO.getDailyArranges();
            // 获得任务明细表的列表
            List<DailyTaskDO> dailyTaskDOList = dailyDO.getDailyTasks();
            // 获得任务问题表的列表
            List<DailyProblemDO> dailyProblemDOList = dailyDO.getDailyProblems();
            // 获取单元格高度
            max = dailyArrangeDOList.size() > dailyProblemDOList.size() ? dailyArrangeDOList.size() : dailyProblemDOList.size();
            max = max > dailyTaskDOList.size() ? max : dailyTaskDOList.size();
            // 获取主表属性
            Row row3  = sheet.createRow(height);
            // 封装数据
            row3.createCell(0).setCellValue(i+1); // 序号
            row3.createCell(1).setCellValue(dailyDO.getTitle()); // 日报名称
            row3.createCell(2).setCellValue(dailyDO.getId()); // 日报编号
            row3.createCell(3).setCellValue(dailyDO.getWriteUser()); // 填写人
            row3.createCell(4).setCellValue(dailyDO.getReportDate()); // 汇报日期
            row3.createCell(5).setCellValue(dailyDO.getWriteJob()); // 职位
            // 设置样式
            row3.setRowStyle(style);
            // 合并单元格
            for(int j=0;j<=5;j++) {
                merger(height, max, j, sheet);
            }
            // 创建一个行对象
            Row row;
            // 操作任务详细表
            for (int j=0;j<dailyTaskDOList.size();j++) {
                // 获得任务明细表对象
                DailyTaskDO dailyTaskDO = dailyTaskDOList.get(j);
                if(sheet.getRow(height+j)==null) { // 判断是否存在行对象，防止数据的覆盖
                    // 生成一个新的行
                    row = sheet.createRow(height+j);
                }else {
                    row = row3;
                }
                row.createCell(6).setCellValue(dailyTaskDO.getDailyTaskType()); // 任务类型
                row.createCell(7).setCellValue(dailyTaskDO.getProjectName()); // 项目名称
                row.createCell(8).setCellValue(dailyTaskDO.getUsetime()); // 花费时间
                row.createCell(9).setCellValue(dailyTaskDO.getPercentComplete()); // 任务完成度
                row.createCell(10).setCellValue(dailyTaskDO.getRemarks()); // 工作任务描述
                // 设置单元格样式
                row.setRowStyle(style);
            }
            // 操作下一步安排表
            for (int j=0;j<dailyArrangeDOList.size();j++) {
                // 获得下一步工作安排表对象
                DailyArrangeDO dailyArrangeDO = dailyArrangeDOList.get(j);
                if(sheet.getRow(height+j)!=null) { // 判断是否存在行对象，防止数据的覆盖
                    // 生成一个新的行
                    row = sheet.createRow(height+j);
                }else {
                    row = row3;
                }
                row.createCell(11).setCellValue(dailyArrangeDO.getDailyTaskType()); // 任务类型
                row.createCell(12).setCellValue(dailyArrangeDO.getFinishDate()); // 计划完成时间
                row.createCell(13).setCellValue(dailyArrangeDO.getRemarks()); // 下一步工作安排
                // 设置单元格样式
                row.setRowStyle(style);
            }
            // 操作任务问题表
            for (int j=0;j<dailyProblemDOList.size();j++) {
                // 获得项目问题表对象
                DailyProblemDO dailyProblemDO = dailyProblemDOList.get(j);
                if(sheet.getRow(height+j)==null) { // 判断是否存在行对象，防止数据的覆盖
                    // 生成一个新的行
                    row = sheet.createRow(height+j);
                }else {
                    row = row3;
                }
                row.createCell(14).setCellValue(dailyProblemDO.getProblem()); // 问题类型
                row.createCell(15).setCellValue(dailyProblemDO.getProjectName()); // 项目名称
                row.createCell(16).setCellValue(dailyProblemDO.getExpectedTime()); // 期望答复时间
                row.createCell(17).setCellValue(dailyProblemDO.getSupport()); // 需要得到的帮助
                row.createCell(18).setCellValue(dailyProblemDO.getRemarks()); // 问题描述
                // 设置单元格样式
                row.setRowStyle(style);
            }
            // 获得当前高度
            height = height+max;
            max = 0;
        }
        // 获取写入流对象
        ServletOutputStream sos = null;
        // 生成日期格式
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            // 获得输出流
            sos =  response.getOutputStream();
            // 设置文件名格式
            // 导出的设置
            response.setHeader("content-disposition", "attachment;filename="+new String(fileName.getBytes(),"GBK"));
            response.setContentType("application/octet-stream");
            // 将数据导入流中
            wb.write(sos);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                // 关闭流
                sos.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 合并单元格
     */
    public void merger(int height,int max,int i,Sheet sheet){
        CellRangeAddress rangeAddress1 = new CellRangeAddress(height, height+max-1, i, i);
        sheet.addMergedRegion(rangeAddress1);
    }

    @Override
    public List<DailyTaskDO> getTask(String dailyId) {
        return dailyDao.getTask(dailyId);
    }

    @Override
    public List<DailyTaskDO> taskList(String projectId) {
        return dailyDao.taskList(projectId);
    }

}
