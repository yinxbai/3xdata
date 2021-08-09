package com.txdata.modules.contract.activiti;

import com.txdata.modules.daily.domain.DailyInfoDO;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActivitiTest {
    @Autowired
    TaskService taskService;
    @Autowired
    IdentityService identityService;
    @Autowired
    RuntimeService runtimeService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private RepositoryService repositoryService;


    ProcessEngineConfiguration pec = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
    //获取ProcessEngine对象   默认配置文件名称：activiti.cfg.xml  并且configuration的Bean实例ID为processEngineConfiguration
    ProcessEngine processEngine = pec.buildProcessEngine();

    /**
     * 流程部署
     * `act_ge_bytearray` 流程定义的资源信息，包含bpmn和png流程文件信息
     * `act_re_deployment` 流程部署信息，包含流程名称,ID,Key等
     * `act_re_procdef` 流程定义信息
     */
    @Test
    public void deployment() {
        //进行部署,将对应的流程定义文件生成到数据库当中，作为记录进行保存
        Deployment deployment = processEngine.getRepositoryService()
                .createDeployment()
                .addClasspathResource("bpmn/test.bpmn")     //加载流程文件
                .addClasspathResource("bpmn/test.png")
                .name("测试")
                .deploy();              //部署
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //创建流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("test");
    }

    /**
     * 启动流程实例
     * `act_hi_actinst`  已开始和执行完毕的活动信息
     * `act_hi_identitylink`    历史参与者信息
     * `act_hi_procinst`        流程实例信息
     * `act_hi_taskinst`        历史任务实例
     * act_ru_execution        任务执行信息
     * act_ru_identitylink     当前任务参与者
     * `act_ru_task`            任务信息
     */
    public String startInstance(String processInstanceByKey, DailyInfoDO dailyInfoDO) {
        Map<String, Object> map = new HashMap<>();
        map.put("dailyInfoDO",dailyInfoDO);
        //获取到RuntimeService对象
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //创建流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processInstanceByKey,map);//红字是流程图的key值
        return processInstance.getId();
    }

    /**
     * 查询用户待办任务列表。
     * @return 任务列表
     */
    public List<Task> queryToDoTasks(String assignee ) {
        taskService = processEngine.getTaskService();
        // 查询待审批列表
        List<Task> taskList  = taskService.createTaskQuery().taskAssignee(assignee).list();
        return taskList;
    }

    /**
     * 查询已处理任务列表。
     *
     * @param assignee 用户
     * @return 已处理任务列表
     */
    public List<HistoricTaskInstance> queryDoneTasks(String assignee) {
        List<HistoricTaskInstance> taskList  = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(assignee)
                .finished()
                .list();
        return taskList;
    }

    public void complete(String taskId, Map<String, Object> vars) {
        taskService.complete(taskId, vars);
    }
}
