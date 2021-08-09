package com.txdata.activiti.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.RuntimeServiceImpl;
import org.activiti.engine.impl.interceptor.CommandExecutor;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.txdata.activiti.dao.ActivitiDao;
import com.txdata.activiti.domain.ActivitiDO;
import com.txdata.activiti.service.ActTaskService;
import com.txdata.activiti.utils.ProcessDefCache;
import com.txdata.common.utils.ShiroUtils;
import com.txdata.common.utils.StringUtils;
import com.txdata.activiti.service.JumpTaskCmd;
import com.txdata.activiti.utils.ProcessDefUtils;
import com.txdata.system.domain.UserDO;
import com.txdata.system.utils.UserUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 工作流任务相关Service
 * 
 * @author huangmk
 * @since 2018-11-21
 *
 */
@Service
public class ActTaskService {
	
	@Autowired
	TaskService taskService;
	@Autowired
	IdentityService identityService;
	@Autowired
	RuntimeService runtimeService;
	@Autowired
	FormService formService;
	@Autowired
	RepositoryService repositoryService;
	@Autowired
	private ProcessEngineFactoryBean processEngineFactory;
	@Autowired
	private ProcessEngine processEngine;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private ActivitiDao activitiDao;

	/**
	 * 获取待办列表
	 * @return
	 */
	public List<ActivitiDO> listTodo(ActivitiDO act){
		String userId = UserUtils.getUser().getLoginName();//ObjectUtils.toString(UserUtils.getUser().getId());
		List<ActivitiDO> result = new ArrayList<ActivitiDO>();
		// =============== 已经签收的任务  ===============
		TaskQuery todoTaskQuery = taskService.createTaskQuery().taskAssignee(userId).active()
				.includeProcessVariables().orderByTaskCreateTime().desc();
		// 设置查询条件
		if (StringUtils.isNotBlank(act.getProcDefKey())){
			todoTaskQuery.processDefinitionKey(act.getProcDefKey());
		}
		if (act.getBeginDate() != null){
			todoTaskQuery.taskCreatedAfter(act.getBeginDate());
		}
		if (act.getEndDate() != null){
			todoTaskQuery.taskCreatedBefore(act.getEndDate());
		}
		// 查询列表
		List<Task> todoList = todoTaskQuery.list();
		for (Task task : todoList) {
			ActivitiDO e = new ActivitiDO();
			e.setTask(task);
			e.setVars(task.getProcessVariables());
			e.setProcDef(ProcessDefCache.get(task.getProcessDefinitionId()));
			e.setStatus("todo");
			result.add(e);
		}
		// =============== 等待签收的任务  ===============
		TaskQuery toClaimQuery = taskService.createTaskQuery().taskCandidateUser(userId)
				.includeProcessVariables().active().orderByTaskCreateTime().desc();
		// 设置查询条件
		if (StringUtils.isNotBlank(act.getProcDefKey())){
			toClaimQuery.processDefinitionKey(act.getProcDefKey());
		}
		if (act.getBeginDate() != null){
			toClaimQuery.taskCreatedAfter(act.getBeginDate());
		}
		if (act.getEndDate() != null){
			toClaimQuery.taskCreatedBefore(act.getEndDate());
		}
		// 查询列表
		List<Task> toClaimList = toClaimQuery.list();
		for (Task task : toClaimList) {
			ActivitiDO e = new ActivitiDO();
			e.setTask(task);
			e.setVars(task.getProcessVariables());
			e.setProcDef(ProcessDefCache.get(task.getProcessDefinitionId()));
			e.setStatus("claim");
			result.add(e);
		}
		return result;
	}

	/**
	 * 提交任务, 并保存意见
	 *
	 * @param taskId
	 *            任务ID
	 * @param procInsId
	 *            流程实例ID，如果为空，则不保存任务提交意见
	 * @param comment
	 *            任务提交意见的内容
	 * @param title
	 *            流程标题，显示在待办任务标题
	 * @param vars
	 *            任务变量
	 */
	@Transactional(readOnly = false)
	public void complete(String taskId, String procInsId, String comment, String title, Map<String, Object> vars) {
		// 添加意见
		if (StringUtils.isNotBlank(procInsId) && StringUtils.isNotBlank(comment)) {
			taskService.addComment(taskId, procInsId, comment);
		}
		// 设置流程变量
		if (vars == null) {
			vars = new HashMap<>();
		}
		// 设置流程标题
		if (StringUtils.isNotBlank(title)) {
			vars.put("title", title);
		}
		// 提交任务
		taskService.complete(taskId, vars);
	}

	/**
	 * 提交任务
	 */
	@Transactional(readOnly = false)
	public void complete(String taskId, Map<String, Object> vars) {
		taskService.complete(taskId, vars);
	}

	/**
	 * 启动流程
	 *
	 * @param procDefKey
	 *            流程定义KEY
	 * @param businessTable
	 *            业务表表名
	 * @param businessId
	 *            业务表编号
	 * @param title
	 *            流程标题，显示在待办任务标题
	 * @param vars
	 *            流程变量
	 * @return 流程实例ID
	 */
	@Transactional(readOnly = false)
	public String startProcess(String procDefKey, String businessTable, String businessId, String title,
			Map<String, Object> vars) {
		String userId = ShiroUtils.getUser().getUsername();// ObjectUtils.toString(UserUtils.getUser().getId())
		// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
		identityService.setAuthenticatedUserId(userId);
		// 设置流程变量
		if (vars == null) {
			vars = new HashMap<String, Object>();
		}
		// 设置流程标题
		if (StringUtils.isNotBlank(title)) {
			vars.put("title", title);
		}
		// 启动流程
		ProcessInstance procIns = runtimeService.startProcessInstanceByKey(procDefKey, businessTable+":"+businessId, vars);
		// 更新业务表流程ID
		ActivitiDO act = new ActivitiDO();
		act.setBusinessTable(businessTable);// 业务表名
		act.setBusinessId(businessId); // 业务表ID
		act.setProcInsId(procIns.getId());
		activitiDao.updateProcInsIdByBusinessId(act);
		return act.getProcInsId();
	}

	/**
	 * 获取流程表单（首先获取任务节点表单KEY，如果没有则取流程开始节点表单KEY）
	 *
	 * @return
	 */
	public String getFormKey(String procDefId, String taskDefKey) {
		String formKey = "";
		if (StringUtils.isNotBlank(procDefId)) {
			if (StringUtils.isNotBlank(taskDefKey)) {
				try {
					formKey = formService.getTaskFormKey(procDefId, taskDefKey);
				} catch (Exception e) {
					formKey = "";
				}
			}
			if (StringUtils.isBlank(formKey)) {
				formKey = formService.getStartFormKey(procDefId);
			}
			if (StringUtils.isBlank(formKey)) {
				formKey = "/404";
			}
		}
		return formKey;
	}

	/**
	 * 读取带跟踪的图片
	 * @param xx
	 * @param pProcessInstanceId 环节ID
	 * 
	 */
	public InputStream tracePhoto(String xx, String pProcessInstanceId) {
		// 获取历史流程实例
		HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
				.processInstanceId(pProcessInstanceId).singleResult();
		if (historicProcessInstance != null) {
			// 获取流程定义
			ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
					.getDeployedProcessDefinition(historicProcessInstance.getProcessDefinitionId());
			// 获取流程历史中已执行节点，并按照节点在流程中执行先后顺序排序
			List<HistoricActivityInstance> historicActivityInstanceList = historyService
					.createHistoricActivityInstanceQuery().processInstanceId(pProcessInstanceId)
					.orderByHistoricActivityInstanceId().asc().list();
			// 已执行的节点ID集合
			List<String> executedActivityIdList = new ArrayList<String>();
			int index = 1;
			// 获取已经执行的节点ID
			for (HistoricActivityInstance activityInstance : historicActivityInstanceList) {
				executedActivityIdList.add(activityInstance.getActivityId());
				index++;
			}
			// 已执行的线集合
			List<String> flowIds = new ArrayList<String>();
			// 获取流程走过的线
			flowIds = getHighLightedFlows(processDefinition, historicActivityInstanceList);

			BpmnModel bpmnModel = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());
			// 获取流程图图像字符流
			ProcessDiagramGenerator pec = processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator();
			// 配置字体
			InputStream imageStream = pec.generateDiagram(bpmnModel, "png", executedActivityIdList, flowIds, "宋体",
					"微软雅黑", "黑体", null, 2.0);
			return imageStream;
		}
		return null;
	}

	/**
	 * 获取需要高亮的线
	 * 
	 * @param processDefinitionEntity
	 * @param historicActivityInstances
	 * @return
	 */
	private List<String> getHighLightedFlows(ProcessDefinitionEntity processDefinitionEntity,
			List<HistoricActivityInstance> historicActivityInstances) {
		List<String> highFlows = new ArrayList<String>();// 用以保存高亮的线flowId
		for (int i = 0; i < historicActivityInstances.size() - 1; i++) {// 对历史流程节点进行遍历
			ActivityImpl activityImpl = processDefinitionEntity
					.findActivity(historicActivityInstances.get(i).getActivityId());// 得到节点定义的详细信息
			List<ActivityImpl> sameStartTimeNodes = new ArrayList<ActivityImpl>();// 用以保存后需开始时间相同的节点
			ActivityImpl sameActivityImpl1 = processDefinitionEntity
					.findActivity(historicActivityInstances.get(i + 1).getActivityId());
			// 将后面第一个节点放在时间相同节点的集合里
			sameStartTimeNodes.add(sameActivityImpl1);
			for (int j = i + 1; j < historicActivityInstances.size() - 1; j++) {
				HistoricActivityInstance activityImpl1 = historicActivityInstances.get(j);// 后续第一个节点
				HistoricActivityInstance activityImpl2 = historicActivityInstances.get(j + 1);// 后续第二个节点
				if (activityImpl1.getStartTime().equals(activityImpl2.getStartTime())) {
					// 如果第一个节点和第二个节点开始时间相同保存
					ActivityImpl sameActivityImpl2 = processDefinitionEntity
							.findActivity(activityImpl2.getActivityId());
					sameStartTimeNodes.add(sameActivityImpl2);
				} else {
					// 有不相同跳出循环
					break;
				}
			}
			List<PvmTransition> pvmTransitions = activityImpl.getOutgoingTransitions();// 取出节点的所有出去的线
			for (PvmTransition pvmTransition : pvmTransitions) {
				// 对所有的线进行遍历
				ActivityImpl pvmActivityImpl = (ActivityImpl) pvmTransition.getDestination();
				// 如果取出的线的目标节点存在时间相同的节点里，保存该线的id，进行高亮显示
				if (sameStartTimeNodes.contains(pvmActivityImpl)) {
					highFlows.add(pvmTransition.getId());
				}
			}
		}
		return highFlows;
	}

	/**
	 * 签收任务
	 * 
	 * @param taskId
	 *            任务ID
	 * @param userId
	 *            签收用户ID（用户登录名）
	 */
	@Transactional(readOnly = false)
	public void claim(String taskId, String userId) {
		// TODO Auto-generated method stub
		taskService.claim(taskId, userId);
	}
	
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

	public TaskEntity getCurrentTask(String procInsId) {
		// TODO Auto-generated method stub
		return (TaskEntity) taskService.createTaskQuery().processInstanceId(procInsId).active().singleResult();
	}

	/**
	 * 跳转（包括回退和向前）至指定活动节点
	 */
	public void jumpTask(String procInsId, String targetTaskDefinitionKey, Map<String, Object> variables) {
		// TODO Auto-generated method stub
		jumpTask(getCurrentTask(procInsId), targetTaskDefinitionKey, variables);
	}

	/**
	 * 跳转（包括回退和向前）至指定活动节点
	 * 
	 * @param currentTaskEntity
	 *            当前任务节点
	 * @param targetTaskDefinitionKey
	 *            目标任务节点（在模型定义里面的节点名称）
	 * @throws Exception
	 */
	public void jumpTask(TaskEntity currentTaskEntity, String targetTaskDefinitionKey, Map<String, Object> variables) {
		ActivityImpl activity = ProcessDefUtils.getActivity(processEngine, currentTaskEntity.getProcessDefinitionId(),
				targetTaskDefinitionKey);
		jumpTask(currentTaskEntity, activity, variables);
	}

	/**
	 * 跳转（包括回退和向前）至指定活动节点
	 * 
	 * @param currentTaskEntity
	 *            当前任务节点
	 * @param targetActivity
	 *            目标任务节点（在模型定义里面的节点名称）
	 * @throws Exception
	 */
	private void jumpTask(TaskEntity currentTaskEntity, ActivityImpl targetActivity, Map<String, Object> variables) {
		CommandExecutor commandExecutor = ((RuntimeServiceImpl) runtimeService).getCommandExecutor();
		commandExecutor.execute(new JumpTaskCmd(currentTaskEntity, targetActivity, variables));
	}

	/**
	 * 获取流转历史列表
	 * 
	 * @param procInsId
	 *            流程实例
	 * @param startAct
	 *            开始活动节点名称
	 * @param endAct
	 *            结束活动节点名称
	 */
	public List<ActivitiDO> histoicFlowList(String procInsId, String startAct, String endAct) {
		List<ActivitiDO> actList = Lists.newArrayList();
		List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
				.processInstanceId(procInsId).finished().orderByHistoricActivityInstanceStartTime().asc()
				.orderByHistoricActivityInstanceEndTime().asc().list();

		boolean start = false;
		Map<String, Integer> actMap = Maps.newHashMap();
		for (int i = 0; i < list.size(); i++) {
			HistoricActivityInstance histIns = list.get(i);
			// 过滤开始节点前的节点
			if (StringUtils.isNotBlank(startAct) && startAct.equals(histIns.getActivityId())) {
				start = true;
			}
			if (StringUtils.isNotBlank(startAct) && !start) {
				continue;
			}
			// 只显示开始节点和结束节点，并且执行人不为空的任务
			if (StringUtils.isNotBlank(histIns.getAssignee()) || "startEvent".equals(histIns.getActivityType())
					|| "endEvent".equals(histIns.getActivityType())) {
				// 给节点增加一个序号
				Integer actNum = actMap.get(histIns.getActivityId());
				if (actNum == null) {
					actMap.put(histIns.getActivityId(), actMap.size());
				}
				ActivitiDO e = new ActivitiDO();
				e.setHistIns(histIns);
				// 获取流程发起人名称
				if ("startEvent".equals(histIns.getActivityType())) {
					List<HistoricProcessInstance> il = historyService.createHistoricProcessInstanceQuery()
							.processInstanceId(procInsId).orderByProcessInstanceStartTime().asc().list();
					if (il.size() > 0) {
						if (StringUtils.isNotBlank(il.get(0).getStartUserId())) {
							UserDO user = UserUtils.getByLoginName(il.get(0).getStartUserId());
							if (user != null) {
								e.setAssignee(histIns.getAssignee());
								e.setAssigneeName(user.getName());
								e.setActivityId(histIns.getActivityId());
							}
						}
					}
				}
				// 获取任务执行人名称
				if (StringUtils.isNotEmpty(histIns.getAssignee())) {
					UserDO user = UserUtils.getByLoginName(histIns.getAssignee());
					if (user != null) {
						e.setAssignee(histIns.getAssignee());
						e.setAssigneeName(user.getName());
						e.setActivityId(histIns.getActivityId());
					}
				}
				// 获取意见评论内容 以及 附件URL和名称
				if (StringUtils.isNotBlank(histIns.getTaskId())) {
					List<Comment> commentList = taskService.getTaskComments(histIns.getTaskId());
					if (commentList.size() > 0) {
						e.setComment(commentList.get(0).getFullMessage());
					}
					List<Attachment> attachmentList = taskService.getTaskAttachments(histIns.getTaskId());
					if (attachmentList.size() > 0) {
						Attachment attachment = attachmentList.get(0);
						e.setAttachmentURL(attachment.getUrl());
						e.setAttachmentName(attachment.getName());
					}
				}
				actList.add(e);
			}
			// 过滤结束节点后的节点
			if (StringUtils.isNotBlank(endAct) && endAct.equals(histIns.getActivityId())) {
				boolean bl = false;
				Integer actNum = actMap.get(histIns.getActivityId());
				// 该活动节点，后续节点是否在结束节点之前，在后续节点中是否存在
				for (int j = i + 1; j < list.size(); j++) {
					HistoricActivityInstance hi = list.get(j);
					Integer actNumA = actMap.get(hi.getActivityId());
					if ((actNumA != null && actNumA < actNum)
							|| StringUtils.equals(hi.getActivityId(), histIns.getActivityId())) {
						bl = true;
					}
				}
				if (!bl) {
					break;
				}
			}
		}
		for (ActivitiDO act : actList) {
			String filePath = act.getAttachmentURL();
			if (StringUtils.isNotBlank(filePath)) {
				filePath = filePath.replace("\\", "/");
				act.setAttachmentURL(filePath);
			}
		}
		return actList;
	}
}
