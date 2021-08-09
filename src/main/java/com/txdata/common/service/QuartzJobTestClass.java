package com.txdata.common.service;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

/**
 * 测试定时任务类
 * @ClassName: QuartzJobTestClass
 * @Description: 这里用一句话描述这个类的作用
 * @author: wanjm
 * @date: 2019年8月8日 下午3:02:51
 */
@Component
public class QuartzJobTestClass implements Job{
	
	@Override
	public void execute(JobExecutionContext paramJobExecutionContext) throws JobExecutionException {
		System.out.println("------------测试定时任务类【com.txdata.common.service.QuartzJobTestClass】--------------");
	}
}
