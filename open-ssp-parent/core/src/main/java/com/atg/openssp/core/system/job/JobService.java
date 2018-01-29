package com.atg.openssp.core.system.job;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.atg.openssp.common.jobservice.CommonJobTrigger;

/**
 * @author André Schmer
 *
 */
public class JobService {

	public static final JobService instance = new JobService();

	private final Map<String, CommonJobTrigger> triggerList;

	private JobService() {
		triggerList = new HashMap<>();
		System.setProperty("org.quartz.scheduler.skipUpdateCheck", "true");
		System.setProperty("org.quartz.scheduler.interruptJobsOnShutdownWithWait", "true");
	}

	public void initJob(final JobConfig jobConfig) {
		final CommonJobTrigger trigger = new CommonJobTrigger();
		trigger.initJob(jobConfig.getJobClazz(), jobConfig.getJobName(), jobConfig.getExpression());
		trigger.startScheduling();
		triggerList.put(jobConfig.getJobName(), trigger);
	}

	public void shutdown() {
		for (final Entry<String, CommonJobTrigger> commonJobTrigger : triggerList.entrySet()) {
			commonJobTrigger.getValue().shutdown();
		}
	}

	CommonJobTrigger getJobByName(final String jobName) {
		return triggerList.get(jobName);
	}

}
