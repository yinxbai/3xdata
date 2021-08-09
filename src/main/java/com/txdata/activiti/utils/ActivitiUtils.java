package com.txdata.activiti.utils;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActivitiUtils {

    @Autowired
    TaskService taskService;
    
    @Autowired
    RuntimeService runtimeService;
    
    /**
     * 根据任务ID获取业务ID
     * @param taskId
     * @return
     */
    public String getBusinessKeyByTaskId(String taskId){
        Task task = taskService
                .createTaskQuery()
                .taskId(taskId)
                .singleResult();
        ProcessInstance pi = runtimeService
                .createProcessInstanceQuery()
                .processInstanceId(task.getProcessInstanceId())
                .singleResult();
        return pi.getBusinessKey();
    }

    /**
     * 根据任务ID获取任务实体
     * @param taskId
     * @return
     */
    public Task getTaskByTaskId(String taskId){
        Task task = taskService
                .createTaskQuery()
                .taskId(taskId)
                .singleResult();
        return task;
    }
}
