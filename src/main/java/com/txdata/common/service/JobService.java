package com.txdata.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.txdata.common.config.Constant;
import com.txdata.common.dao.JobDao;
import com.txdata.common.domain.JobDO;
import com.txdata.common.domain.ScheduleJob;
import com.txdata.common.domain.JobDO;
import com.txdata.common.persistence.CrudService;
import com.txdata.common.quartz.utils.QuartzManager;
import com.txdata.common.utils.ScheduleJobUtils;

/**
 * 定时任务相关Service
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-09-26 20:53:48
 */
@Service
public class JobService extends CrudService<JobDao, JobDO> {

	@Autowired
	QuartzManager quartzManager;

	@Override
	public JobDO get(String id) {
		return dao.get(id);
	}

	@Override
	public List<JobDO> list(Map<String, Object> map) {
		return dao.list(map);
	}

	@Override
	public int count(Map<String, Object> map) {
		return dao.count(map);
	}

	@Override
	@Transactional(readOnly = false)
	public int save(JobDO taskScheduleJob) {
		taskScheduleJob.preInsert();
		return dao.save(taskScheduleJob);
	}

	@Override
	@Transactional(readOnly = false)
	public int update(JobDO taskScheduleJob) {
		taskScheduleJob.preUpdate();
		return dao.update(taskScheduleJob);
	}

	@Override
	@Transactional(readOnly = false)
	public int remove(String id) {
		try {
			JobDO scheduleJob = get(id);
			quartzManager.deleteJob(ScheduleJobUtils.entityToData(scheduleJob));
			return dao.remove(id);
		} catch (SchedulerException e) {
			e.printStackTrace();
			return 0;
		}

	}
	@Transactional(readOnly = false)
	public int batchRemove(String[] ids) {
		for (String id : ids) {
			try {
				JobDO scheduleJob = get(id);
				quartzManager.deleteJob(ScheduleJobUtils.entityToData(scheduleJob));
			} catch (SchedulerException e) {
				e.printStackTrace();
				return 0;
			}
		}
		return dao.batchRemove(ids);
	}

	/**
	 * 初始化任务计划
	 * 
	 * @throws SchedulerException
	 */
	@Transactional(readOnly = false)
	public void initSchedule() throws SchedulerException {
		// 这里获取任务信息数据
		List<JobDO> jobList = dao.list(new HashMap<String, Object>(16));
		for (JobDO scheduleJob : jobList) {
			if ("1".equals(scheduleJob.getJobStatus())) {
				ScheduleJob job = ScheduleJobUtils.entityToData(scheduleJob);
				quartzManager.addJob(job);
			}

		}
	}

	/**
	 * 修改任务状态
	 * 
	 * @param jobId
	 * @param cmd
	 * @throws SchedulerException
	 */
	@Transactional(readOnly = false)
	public void changeStatus(String jobId, String cmd) throws SchedulerException {
		JobDO scheduleJob = get(jobId);
		if (scheduleJob == null) {
			return;
		}
		if (Constant.STATUS_RUNNING_STOP.equals(cmd)) {
			quartzManager.deleteJob(ScheduleJobUtils.entityToData(scheduleJob));
			scheduleJob.setJobStatus(ScheduleJob.STATUS_NOT_RUNNING);
		} else {
			if (!Constant.STATUS_RUNNING_START.equals(cmd)) {
			} else {
				scheduleJob.setJobStatus(ScheduleJob.STATUS_RUNNING);
				quartzManager.addJob(ScheduleJobUtils.entityToData(scheduleJob));
			}
		}
		update(scheduleJob);
	}

	/**
	 * 更新job时间表达式
	 * 
	 * @param jobId
	 * @throws SchedulerException
	 */
	@Transactional(readOnly = false)
	public void updateCron(String jobId) throws SchedulerException {
		JobDO scheduleJob = get(jobId);
		if (scheduleJob == null) {
			return;
		}
		if (ScheduleJob.STATUS_RUNNING.equals(scheduleJob.getJobStatus())) {
			quartzManager.updateJobCron(ScheduleJobUtils.entityToData(scheduleJob));
		}
		update(scheduleJob);
	}
}
