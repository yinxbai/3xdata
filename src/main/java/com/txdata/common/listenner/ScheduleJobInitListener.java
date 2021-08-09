package com.txdata.common.listenner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import com.txdata.common.quartz.utils.QuartzManager;
import com.txdata.common.service.JobService;

/**
 * Component注解表示把该类实例化到spring容器中 Order注解表示被spring加载的顺序，数值越小越先被加载
 * 实现CommandLineRunner接口用来预先数据的加载，重构run方法即可
 * 
 * @author huangmk
 * @since 2018-11-21
 *
 */
@Component
@Order(value = 1)
public class ScheduleJobInitListener implements CommandLineRunner {

	@Autowired
	JobService scheduleJobService;

	@Autowired
	QuartzManager quartzManager;

	@Override
	public void run(String... arg0) throws Exception {
		try {
			scheduleJobService.initSchedule();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}