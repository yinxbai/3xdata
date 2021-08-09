package com.txdata.common.utils;

import com.txdata.common.domain.JobDO;
import com.txdata.common.domain.ScheduleJob;

public class ScheduleJobUtils {
	public static ScheduleJob entityToData(JobDO jobEntity) {
		ScheduleJob scheduleJob = new ScheduleJob();
		scheduleJob.setBeanClass(jobEntity.getBeanClass());
		scheduleJob.setCronExpression(jobEntity.getCronExpression());
		scheduleJob.setDescription(jobEntity.getDescription());
		scheduleJob.setIsConcurrent(jobEntity.getIsConcurrent());
		scheduleJob.setJobName(jobEntity.getJobName());
		scheduleJob.setJobGroup(jobEntity.getJobGroup());
		scheduleJob.setJobStatus(jobEntity.getJobStatus());
		scheduleJob.setMethodName(jobEntity.getMethodName());
		scheduleJob.setSpringBean(jobEntity.getSpringBean());
		return scheduleJob;
	}
}