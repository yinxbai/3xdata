package com.txdata.common.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.txdata.common.config.Constant;
import com.txdata.common.domain.JobDO;
import com.txdata.common.service.JobService;
import com.txdata.common.utils.PageUtils;
import com.txdata.common.utils.Query;
import com.txdata.common.utils.R;
import com.txdata.common.utils.StringUtils;

/**
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-09-26 20:53:48
 */
@Controller
@RequestMapping("/common/job")
public class JobController extends BaseController {
	
	@Autowired
	private JobService jobService;

	@GetMapping()
	String taskScheduleJob() {
		return "common/job/job";
	}

	@ResponseBody
	@PostMapping("/list")
	public R list(@RequestParam Map<String, Object> params) {
		// 查询列表数据
		Query query = new Query(params);
		List<JobDO> jobList = jobService.list(query);
		int total = jobService.count(query);
		PageUtils pageUtils = new PageUtils(jobList, total);
		Map<String,Object> jsonMap = new HashMap<String,Object>();
        jsonMap.put("rows", pageUtils.getRows());
        jsonMap.put("pageSize", query.getPageSize());
        jsonMap.put("pageNo", query.getPageNo());
        jsonMap.put("count", pageUtils.getTotal());
        return R.success(jsonMap);
	}

	@GetMapping("/add")
	String add() {
		return "common/job/add";
	}

	@GetMapping("/edit/{id}")
	String edit(@PathVariable("id") String id, Model model) {
		JobDO job = jobService.get(id);
		model.addAttribute("job", job);
		return "common/job/edit";
	}

	/**
	 * 信息
	 */
	 @ResponseBody
	 @PostMapping("/form")
	public R info(@RequestParam(required = true)String id) {
		JobDO taskScheduleJob = jobService.get(id);
		Map<String, Object> map = new HashMap<>();
		map.put("formObject", taskScheduleJob);
		return R.success(map);
	}

	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	public R save(JobDO taskScheduleJob) {
		if (StringUtils.isNotBlank(taskScheduleJob.getId())) {
			JobDO Job = jobService.get(taskScheduleJob.getId());
			if (Constant.STATUS_RUNNING_START.equals(Job.getJobStatus())) {
				return R.error("请先关闭该定时任务");
			}
			if (jobService.update(taskScheduleJob) > 0) {
				return R.success();
			}
		} else {
			if (jobService.save(taskScheduleJob) > 0) {
				return R.success();
			}
		}
		return R.error();
	}

	/**
	 * 修改
	 */
	@ResponseBody
	@PostMapping("/update")
	public R update(JobDO taskScheduleJob) {
		if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
			return R.error("1", "演示系统不允许修改,完整体验请部署程序");
		}
		jobService.update(taskScheduleJob);
		return R.success();
	}

	/**
	 * 删除
	 */
	@PostMapping("/delete")
	@ResponseBody
	public R remove(String id) {
		if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
			return R.error("1", "演示系统不允许修改,完整体验请部署程序");
		}
		JobDO Job = jobService.get(id);
		if (Constant.STATUS_RUNNING_START.equals(Job.getJobStatus())) {
			return R.error("请先关闭该定时任务");
		}
		if (jobService.remove(id) > 0) {
			return R.success();
		}
		return R.error();
	}

	/**
	 * 删除
	 */
	@PostMapping("/batchRemove")
	@ResponseBody
	public R remove(@RequestParam("ids[]") String[] ids) {
		if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
			return R.error("1", "演示系统不允许修改,完整体验请部署程序");
		}
		jobService.batchRemove(ids);
		return R.success();
	}

	@PostMapping(value = "/changeJobStatus")
	@ResponseBody
	public R changeJobStatus(String id, String cmd) {
		if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
			return R.error("1", "演示系统不允许修改,完整体验请部署程序");
		}
		Map<String, Object> map = new HashMap<>();
		String label = "停止";
		if ("1".equals(cmd)) {
			label = "启动";
		} else {
			label = "停止";
		}
		try {
			jobService.changeStatus(id, cmd);
			map.put("message", "任务" + label + "成功");
			return R.success(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		map.put("message", "任务" + label + "失败");
		return R.success(map);
	}

}
