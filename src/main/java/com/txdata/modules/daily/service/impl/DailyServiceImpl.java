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
    private DailyDao dailyDao; // ???????????????dao??????
    @Autowired
    private DailyTaskDao dailyTaskDao; // ????????????????????????dao??????
    @Autowired
    private DailyArrangeDao dailyArrangeDao; // ???????????????????????????dao??????
    @Autowired
    private DailyProblemDao dailyProblemDao; // ????????????????????????dao??????
    @Autowired
    private MyUserDao myUserDao; // ???????????????
    @Autowired
    TaskService taskService;
    @Autowired
    HistoryService historyService;
    // ????????????
    ProcessEngineConfiguration pec = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml") // ??????????????????
            .setActivityFontName("??????").setLabelFontName("??????").setAnnotationFontName("??????"); // ??????????????????

    // ????????????????????????
    ProcessEngine processEngine=pec.buildProcessEngine();
    // ??????????????????
    List<DailyDO> resultList = new ArrayList<>();

    /**
     * ?????????????????????
     * @param dailyPage
     * @param map
     * @return
     */
    @Override
    public Page<DailyDO> list(Page<DailyDO> dailyPage,@Param("entity") Map<String,Object> map) {
        return dailyDao.list(dailyPage,map);
    }

    /**
     * ?????????????????????
     * @param dailyDO
     * @return
     */
    @Transactional(readOnly = false)
    @Override
    public int save(DailyDO dailyDO) {
        // ????????????????????????ID
        if(dailyDO.getId()==null||dailyDO.getId()==""){
            // ??????????????????????????????
            if(dailyDao.check(dailyDO.getReportDate(),dailyDO.getWriteUser())==0 //????????????????????????????????????
                    && dailyDO.getDailyArranges().size()>=1 // ????????????????????????????????????1
                    && dailyDO.getDailyTasks().size()>=1 // ?????????????????????????????????1
             ) {
                processEngine.getRepositoryService() // ?????????????????????
                        .createDeployment() // ???????????????
                        .addClasspathResource("bpmn/daily.bpmn") //??????????????????
                        .addClasspathResource("bpmn/daily.png") // ??????????????????
                        .name("????????????") // ????????????
                        .deploy(); // ??????
                //?????????RuntimeService??????
                RuntimeService runtimeService = processEngine.getRuntimeService();
                // ????????????
                DailyInfoDO dailyInfoDO = new DailyInfoDO();
                // ????????????
                dailyInfoDO.setJob(dailyDO.getWriteJob());
                // ???????????????
                dailyInfoDO.setUserId(dailyDO.getWriteUser());
                // ??????map??????
                Map<String, Object> map = new HashMap<>();
                // ??????????????????map???
                map.put("dailyInfoDO",dailyInfoDO);
                //??????????????????
                ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("daily",map);
                //??????????????????id
                String procId = processInstance.getId();
                // ???????????????id??????????????????
                dailyInfoDO.setProcId(procId);
                dailyDO.setProcId(procId);
                // ????????????
                String id = IdGen.uuid(); // ????????????ID
                dailyDO.setId(id);
                // ?????????????????????????????????
                dailyDO.setWriteDate(new Date());
                // ????????????????????????
                String title = dailyDO.getWriteUser() + "_" + dailyDO.getReportDate() + "_??????";
                dailyDO.setTitle(title);
                // ???????????????????????????
                String path = "/upload/" + dailyDO.getReportDate().toString();
                dailyDO.setFilePath(path);
                // ????????????????????????
                int num = dailyDao.save(dailyDO);

                // ?????????????????????????????????????????????
                List<DailyTaskDO> dailyTaskDOS = dailyDO.getDailyTasks();
                List<DailyArrangeDO> dailyArrangeDOS = dailyDO.getDailyArranges();
                List<DailyProblemDO> dailyProblemDOS = dailyDO.getDailyProblems();

                // ???????????????????????????
                dailyTaskDOS.stream().filter(Objects::nonNull).forEach(dailyTaskDO -> {
                    // ??????????????????????????????id
                    dailyTaskDO.setId(IdGen.uuid());
                    // ????????????????????????????????????id??????
                    dailyTaskDO.setDailyId(id);
                    // ??????
                    dailyTaskDao.save(dailyTaskDO);
                });
                /*for (DailyTaskDO dailyTaskDO : dailyTaskDOS) {
                    // ???????????????????????????????????????
                    if (dailyTaskDO != null) {
                        // ??????????????????????????????id
                        dailyTaskDO.setId(IdGen.uuid());
                        // ????????????????????????????????????id??????
                        dailyTaskDO.setDailyId(id);
                        // ??????
                        dailyTaskDao.save(dailyTaskDO);
                    }
                }*/
                // ??????????????????????????????
                    dailyArrangeDOS.stream().filter(Objects::nonNull).forEach(dailyArrangeDO -> {
                        // ??????????????????????????????id
                        dailyArrangeDO.setId(IdGen.uuid());
                        // ????????????????????????????????????id??????
                        dailyArrangeDO.setDailyId(id);
                        // ??????
                        dailyArrangeDao.save(dailyArrangeDO);
                    });
                /*for (DailyArrangeDO dailyArrangeDO : dailyArrangeDOS) {
                    // ???????????????????????????????????????
                    if (dailyArrangeDO != null) {
                        // ??????????????????????????????id
                        dailyArrangeDO.setId(IdGen.uuid());
                        // ????????????????????????????????????id??????
                        dailyArrangeDO.setDailyId(id);
                        // ??????
                        dailyArrangeDao.save(dailyArrangeDO);
                    }
                }*/
                if (dailyDO.getDailyProblems()!=null){
                    // ?????????????????????
                    dailyProblemDOS.stream().filter(Objects::nonNull).forEach(dailyProblemDO -> {
                        // ???????????????????????????id
                        dailyProblemDO.setId(IdGen.uuid());
                        // ?????????????????????????????????id??????
                        dailyProblemDO.setDailyId(id);
                        // ??????
                        dailyProblemDao.save(dailyProblemDO);
                    });
                    /*for (DailyProblemDO dailyProblemDO : dailyProblemDOS) {
                        // ????????????????????????????????????
                        if (dailyProblemDO != null) {
                            // ???????????????????????????id
                            dailyProblemDO.setId(IdGen.uuid());
                            // ?????????????????????????????????id??????
                            dailyProblemDO.setDailyId(id);
                            // ??????
                            dailyProblemDao.save(dailyProblemDO);
                        }
                    }*/
                }else {
                    return 0;
                }

                // ??????????????????id??????????????????
                Task task = taskService.createTaskQuery().processInstanceId(procId).singleResult();
                // ?????????id?????????????????????
                dailyInfoDO.setTaskId(task.getId());
                // ????????????
                taskService.claim(task.getId(),dailyInfoDO.getUserId());
                // ????????????
                taskService.complete(task.getId(),map);
                // ?????????????????????
                return num;
            }else {
                return 0;
            }
        }else {
            // ????????????????????????????????????????????????????????????????????????????????????
            List<DailyTaskDO> dailyTaskDOS = dailyDO.getDailyTasks();
            List<DailyArrangeDO> dailyArrangeDOS = dailyDO.getDailyArranges();
            List<DailyProblemDO> dailyProblemDOS = dailyDO.getDailyProblems();

            dailyTaskDOS.stream().filter(Objects::nonNull).forEach(dailyTaskDO -> {
                dailyTaskDao.update(dailyTaskDO);
            });
            // ???????????????????????????
            /*for (DailyTaskDO dailyTaskDO:dailyTaskDOS) {
                // ???????????????????????????????????????
                if (dailyTaskDO!=null) {
                    dailyTaskDao.update(dailyTaskDO); // ??????
                }
            }*/
            dailyArrangeDOS.stream().filter(Objects::nonNull).forEach(dailyArrangeDO -> {
                dailyArrangeDao.update(dailyArrangeDO);
            });
            // ??????????????????????????????
            /*for (DailyArrangeDO dailyArrangeDO:dailyArrangeDOS) {
                // ??????????????????????????????????????????
                if (dailyArrangeDO!=null) {
                    dailyArrangeDao.update(dailyArrangeDO);// ??????
                }
            }*/
            dailyProblemDOS.stream().filter(Objects::nonNull).forEach(dailyProblemDO -> {
                dailyProblemDao.update(dailyProblemDO);
            });
            // ???????????????????????????
           /* for (DailyProblemDO dailyProblemDO:dailyProblemDOS) {
                // ???????????????????????????????????????
                if (dailyProblemDO!=null) {
                    dailyProblemDao.update(dailyProblemDO);// ??????
                }
            }*/
            return dailyDao.update(dailyDO);// ??????????????????
        }

    }

    /**
     * ????????????
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
     * ????????????
     * @param id
     * @return
     */
    @Override
    public DailyDO get(String id) {
        return dailyDao.get(id);
    }

    /**
     * ?????????????????????
     * @param userName
     * @return
     */
    @Override
    public List<DailyDO> find(String userName){
        // ?????????????????????
        List<Task> taskList = taskService.createTaskQuery().taskAssignee(userName).list();
        List<DailyDO> resultList = taskList.stream().map(task -> dailyDao.findTask(task.getProcessInstanceId())).collect(Collectors.toList());
        /*for (Task task : taskList) {
            // ??????????????????????????????list?????????
            DailyDO dailyDO = dailyDao.findTask(task.getProcessInstanceId());
            dailyDO.setProcId(task.getProcessInstanceId());
            resultList.add(dailyDO);
        }*/
        return resultList;
    }

    /**
     *?????????????????????
     * @param userName
     * @return
     */
    @Override
    public List<DailyDO> finish(String userName){
        // ??????????????????
        HistoryService historyService = processEngine.getHistoryService();
        // ?????????????????????
        List<HistoricTaskInstance> taskList  = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(userName)
                .finished()
                .list();
        List<DailyDO> resultList = taskList.stream().map(task -> dailyDao.findTask(task.getProcessInstanceId())).collect(Collectors.toList());
        // ???????????????????????????
        /*for (HistoricTaskInstance historicTaskInstance : taskList) {
            // ???????????????????????????
            DailyDO dailyDO = dailyDao.findTask(historicTaskInstance.getProcessInstanceId());
            if(dailyDO!=null){
                // ??????????????????????????????list?????????
                resultList.add(dailyDO);
            }
        }*/
        return resultList;
    }

    /**
     * ????????????
     */
    @Override
    public int approve(int sign, String procId, String userId){
        // ??????????????????
        DailyInfoDO dailyInfoDO = new DailyInfoDO();
        // ????????????????????????
        MyUserDO myUserDO = myUserDao.findById(userId);
        // ?????????
        String name = myUserDO.getName();
        // ??????
        String job = myUserDO.getPosition();
        dailyInfoDO.setProcId(procId); // ??????????????????id
        dailyInfoDO.setJob(job); // ????????????
        // ???????????????????????????id
        Task task = taskService.createTaskQuery().processInstanceId(dailyInfoDO.getProcId()).singleResult();
        dailyInfoDO.setTaskId(task.getId()); // ??????
        Map<String, Object> map = new HashMap<>();
        // ????????????
        int num = 0;
        switch (job){
            case "????????????":
                dailyInfoDO.setApprover1(name); // ????????????
                map.put("dailyInfoDO",dailyInfoDO);
                map.put("sign",sign);
                // ????????????
                taskService.setAssignee(task.getId(),dailyInfoDO.getApprover1());
                // ????????????
                taskService.complete(dailyInfoDO.getTaskId(), map);
                if(sign==1){
                    num = 1;
                }else {
                    num = dailyDao.updateStatus(procId,"3");
                }
                break;
            case "????????????":
                dailyInfoDO.setApprover2(name); // ????????????
                map.put("flag",sign);
                // ????????????
                taskService.setAssignee(task.getId(),dailyInfoDO.getApprover2());
                // ????????????
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
     * ????????????????????????
     *
     * @param procInsId
     *            ????????????
     */
    @Override
    public List<DailyInfoDO> histoicFlowList(String procInsId) {
        // ????????????????????????
        List<DailyInfoDO> dailyInfoDOList = new ArrayList<>();
        // ??????????????????
        String remark = null;
        // ??????????????????
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(procInsId).finished().orderByHistoricActivityInstanceStartTime().asc() // ??????
                .list();
        // ??????list??????????????????
        if (list != null && list.size() > 0) {
            // ??????list
            for (HistoricActivityInstance activityInstance : list) {
                DailyInfoDO dailyInfoDO = new DailyInfoDO();
                // ????????????
                dailyInfoDO.setActivityName(activityInstance.getActivityName()); // ????????????
                dailyInfoDO.setAssigneeName(activityInstance.getAssignee()); // ?????????
                dailyInfoDO.setStartDate(activityInstance.getStartTime()); // ????????????
                dailyInfoDO.setEndDate(activityInstance.getEndTime()); // ????????????
                // ????????????
                dailyInfoDOList.add(dailyInfoDO);
            }
        }
        return dailyInfoDOList;
    }

    @Override
    public void export(HttpServletRequest request,HttpServletResponse response, Map<String, Object> params){

        //??????
        //????????????
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("utf-8");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        // ??????excel?????????
        String fileName = UserUtils.getUser().getName() + DateUtils.getDate("yyyy-MM-dd") + "_??????"+".xlsx";
        SXSSFWorkbook wb = new SXSSFWorkbook();
        Sheet sheet = wb.createSheet("?????????");
        // ??????????????????
        CellStyle style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        Font titleFont = wb.createFont();
        titleFont.setFontName("??????");
        titleFont.setFontHeightInPoints((short) 12);
        titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style.setFont(titleFont);
        // ???????????????
        Row row1 = sheet.createRow(0);
        // ?????????????????????
        row1.createCell(0).setCellValue("??????");
        row1.createCell(1).setCellValue("????????????");
        row1.createCell(2).setCellValue("????????????");
        row1.createCell(3).setCellValue("?????????");
        row1.createCell(4).setCellValue("????????????");
        row1.createCell(5).setCellValue("??????");
        // ???????????????
        row1.createCell(6).setCellValue("????????????");
        row1.createCell(11).setCellValue("?????????????????????");
        row1.createCell(14).setCellValue("????????????");
        // ???????????????
        Row row2 = sheet.createRow(1);
        // ???????????????
        row2.createCell(6).setCellValue("????????????");
        row2.createCell(7).setCellValue("????????????");
        row2.createCell(8).setCellValue("????????????(???)");
        row2.createCell(9).setCellValue("???????????????(%)");
        row2.createCell(10).setCellValue("??????????????????");
        // ??????????????????
        row2.createCell(11).setCellValue("????????????");
        row2.createCell(12).setCellValue("??????????????????");
        row2.createCell(13).setCellValue("?????????????????????");
        // ???????????????
        row2.createCell(14).setCellValue("????????????");
        row2.createCell(15).setCellValue("????????????");
        row2.createCell(16).setCellValue("??????????????????");
        row2.createCell(17).setCellValue("?????????????????????");
        row2.createCell(18).setCellValue("????????????");
        // ??????????????????
        row1.setRowStyle(style);
        row2.setRowStyle(style);
        // ???????????????????????????
        for(int i=0;i<=5;i++) {
            merger(0, 1, i, sheet);
        }
        // ?????????????????????
        CellRangeAddress region7 = new CellRangeAddress(0, 0, 6, 10);
        sheet.addMergedRegion(region7);
        // ????????????????????????
        CellRangeAddress region8 = new CellRangeAddress(0, 0, 11, 13);
        sheet.addMergedRegion(region8);
        // ?????????????????????
        CellRangeAddress region9 = new CellRangeAddress(0, 0, 14, 18);
        sheet.addMergedRegion(region9);

        // ????????????
        Query query = new Query(params);
        Page<DailyDO> page = new Page<DailyDO>(query.getPageNo(), query.getPageSize());
        // ????????????
        page = dailyDao.export(page,params);
        List<DailyDO> list = page.getRecords();
        int max = 0;
        int height = 2;
        // ?????????????????????list??????
        for(int i=0;i<list.size();i++) {
            // ??????????????????
            DailyDO dailyDO = list.get(i);
            // ?????????????????????????????????
            List<DailyArrangeDO> dailyArrangeDOList = dailyDO.getDailyArranges();
            // ??????????????????????????????
            List<DailyTaskDO> dailyTaskDOList = dailyDO.getDailyTasks();
            // ??????????????????????????????
            List<DailyProblemDO> dailyProblemDOList = dailyDO.getDailyProblems();
            // ?????????????????????
            max = dailyArrangeDOList.size() > dailyProblemDOList.size() ? dailyArrangeDOList.size() : dailyProblemDOList.size();
            max = max > dailyTaskDOList.size() ? max : dailyTaskDOList.size();
            // ??????????????????
            Row row3  = sheet.createRow(height);
            // ????????????
            row3.createCell(0).setCellValue(i+1); // ??????
            row3.createCell(1).setCellValue(dailyDO.getTitle()); // ????????????
            row3.createCell(2).setCellValue(dailyDO.getId()); // ????????????
            row3.createCell(3).setCellValue(dailyDO.getWriteUser()); // ?????????
            row3.createCell(4).setCellValue(dailyDO.getReportDate()); // ????????????
            row3.createCell(5).setCellValue(dailyDO.getWriteJob()); // ??????
            // ????????????
            row3.setRowStyle(style);
            // ???????????????
            for(int j=0;j<=5;j++) {
                merger(height, max, j, sheet);
            }
            // ?????????????????????
            Row row;
            // ?????????????????????
            for (int j=0;j<dailyTaskDOList.size();j++) {
                // ???????????????????????????
                DailyTaskDO dailyTaskDO = dailyTaskDOList.get(j);
                if(sheet.getRow(height+j)==null) { // ???????????????????????????????????????????????????
                    // ?????????????????????
                    row = sheet.createRow(height+j);
                }else {
                    row = row3;
                }
                row.createCell(6).setCellValue(dailyTaskDO.getDailyTaskType()); // ????????????
                row.createCell(7).setCellValue(dailyTaskDO.getProjectName()); // ????????????
                row.createCell(8).setCellValue(dailyTaskDO.getUsetime()); // ????????????
                row.createCell(9).setCellValue(dailyTaskDO.getPercentComplete()); // ???????????????
                row.createCell(10).setCellValue(dailyTaskDO.getRemarks()); // ??????????????????
                // ?????????????????????
                row.setRowStyle(style);
            }
            // ????????????????????????
            for (int j=0;j<dailyArrangeDOList.size();j++) {
                // ????????????????????????????????????
                DailyArrangeDO dailyArrangeDO = dailyArrangeDOList.get(j);
                if(sheet.getRow(height+j)!=null) { // ???????????????????????????????????????????????????
                    // ?????????????????????
                    row = sheet.createRow(height+j);
                }else {
                    row = row3;
                }
                row.createCell(11).setCellValue(dailyArrangeDO.getDailyTaskType()); // ????????????
                row.createCell(12).setCellValue(dailyArrangeDO.getFinishDate()); // ??????????????????
                row.createCell(13).setCellValue(dailyArrangeDO.getRemarks()); // ?????????????????????
                // ?????????????????????
                row.setRowStyle(style);
            }
            // ?????????????????????
            for (int j=0;j<dailyProblemDOList.size();j++) {
                // ???????????????????????????
                DailyProblemDO dailyProblemDO = dailyProblemDOList.get(j);
                if(sheet.getRow(height+j)==null) { // ???????????????????????????????????????????????????
                    // ?????????????????????
                    row = sheet.createRow(height+j);
                }else {
                    row = row3;
                }
                row.createCell(14).setCellValue(dailyProblemDO.getProblem()); // ????????????
                row.createCell(15).setCellValue(dailyProblemDO.getProjectName()); // ????????????
                row.createCell(16).setCellValue(dailyProblemDO.getExpectedTime()); // ??????????????????
                row.createCell(17).setCellValue(dailyProblemDO.getSupport()); // ?????????????????????
                row.createCell(18).setCellValue(dailyProblemDO.getRemarks()); // ????????????
                // ?????????????????????
                row.setRowStyle(style);
            }
            // ??????????????????
            height = height+max;
            max = 0;
        }
        // ?????????????????????
        ServletOutputStream sos = null;
        // ??????????????????
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            // ???????????????
            sos =  response.getOutputStream();
            // ?????????????????????
            // ???????????????
            response.setHeader("content-disposition", "attachment;filename="+new String(fileName.getBytes(),"GBK"));
            response.setContentType("application/octet-stream");
            // ?????????????????????
            wb.write(sos);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                // ?????????
                sos.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * ???????????????
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
