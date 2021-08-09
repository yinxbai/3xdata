package com.txdata.activiti.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.activiti.engine.FormService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSON;
import com.txdata.activiti.domain.ActivitiDO;
import com.txdata.activiti.service.ActTaskService;
import com.txdata.activiti.vo.ProcessVO;
import com.txdata.activiti.vo.TaskVO;
import com.txdata.common.utils.PageUtils;
import com.txdata.common.utils.R;
import io.swagger.annotations.ApiOperation;

@RequestMapping("act/task")
@RestController
public class TaskController {

	@Autowired
	RepositoryService repositoryService;

	@Autowired
	FormService formService;

	@Autowired
	TaskService taskService;

	@Autowired
	ActTaskService actTaskService;

	@GetMapping("/gotoList")
	PageUtils list(int offset, int limit) {
		List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery().listPage(offset,
				limit);
		int count = (int) repositoryService.createProcessDefinitionQuery().count();
		List<Object> list = new ArrayList<>();
		for (ProcessDefinition processDefinition : processDefinitions) {
			list.add(new ProcessVO(processDefinition));
		}
		PageUtils pageUtils = new PageUtils(list, count);
		return pageUtils;
	}

	@GetMapping("/todoList")
	List<TaskVO> todoList() {
		List<Task> tasks = taskService.createTaskQuery().taskAssignee("admin").list();
		List<TaskVO> taskVOS = new ArrayList<>();
		for (Task task : tasks) {
			TaskVO taskVO = new TaskVO(task);
			taskVOS.add(taskVO);
		}
		return taskVOS;
	}

	/**
	 * 读取带跟踪的图片
	 */
	@RequestMapping(value = "/trace/photo/{procDefId}/{execId}")
	public void tracePhoto(@PathVariable("procDefId") String procDefId, @PathVariable("execId") String execId,
			HttpServletResponse response) throws Exception {
		InputStream imageStream = actTaskService.tracePhoto(procDefId, execId);
		// 输出资源内容到相应对象
		byte[] b = new byte[1024];
		int len;
		while ((len = imageStream.read(b, 0, 1024)) != -1) {
			response.getOutputStream().write(b, 0, len);
		}
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
	@RequestMapping(value = "histoicFlow")
	@ApiOperation(value = "获取流转历史列表", httpMethod = "POST")
	@ResponseBody
	public String histoicFlow(String procInsId, String startAct, String endAct, HttpServletResponse response) {
		R result = null;
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(procInsId)) {
			List<ActivitiDO> histoicFlowList = actTaskService.histoicFlowList(procInsId, startAct, endAct);
			for (ActivitiDO act : histoicFlowList) {
				String comment = act.getComment();
				if(StringUtils.isNotBlank(comment)) {
					String activityId = act.getHistIns().getActivityId();
					int indexNo = comment.indexOf("0");
					int indexYes = comment.indexOf("1");
					if("memberEdit".equals(activityId)) {
						if(indexNo == 0) {
							act.setComment("【取消】"+comment.substring(2));
							continue;
						}else if(indexYes == 0) {
							act.setComment("");
							continue;
						}
					}
					if("cashierAudit".equals(activityId)) {
						if(indexYes == 0) {
							act.setComment("【已付款】"+comment.substring(2));
							continue;
						}
					}
					if(indexNo == 0) {
						act.setComment("【拒绝】"+comment.substring(2));
					}else if(indexYes == 0) {
						act.setComment("【同意】"+comment.substring(2));
					}else {
						act.setComment(comment);
					}
				}else {
					act.setComment(comment);
				}
			}
			jsonMap.put("histoicFlowList", histoicFlowList);
		} else {
			jsonMap.put("histoicFlowList", null);
		}

		result = R.success(jsonMap);
		String jsonStr = JSON.toJSONStringWithDateFormat(result, "yyyy-MM-dd HH:mm:ss");
		return jsonStr;
	}

	@GetMapping("/form/{procDefId}/{taskId}")
	public void form(@PathVariable("procDefId") String procDefId, @PathVariable("taskId") String taskId,
			HttpServletResponse response) throws IOException {
		// 获取流程XML上的表单KEY
		String formKey = actTaskService.getFormKey(procDefId, taskId);
		response.sendRedirect(formKey + "/" + taskId);
	}

}
