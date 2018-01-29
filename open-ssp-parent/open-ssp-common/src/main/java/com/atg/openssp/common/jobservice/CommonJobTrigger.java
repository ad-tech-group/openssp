package com.atg.openssp.common.jobservice;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a wrapper for the trigger and scheduler.
 * 
 * @author André Schmer
 * 
 */
public class CommonJobTrigger {

	private static final Logger log = LoggerFactory.getLogger(CommonJobTrigger.class);

	private String triggerIdentity;

	private Class<? extends org.quartz.Job> jobClazz;

	private Scheduler scheduler;

	private JobDetail jobDetail;

	private Trigger trigger;

	private boolean isStarted = false;

	private String expression;

	private String group;

	public CommonJobTrigger() {}

	public void initJob(final Class<? extends org.quartz.Job> jobClazz, final String jobIdentity, final String expression) {
		this.jobClazz = jobClazz;
		triggerIdentity = jobIdentity;
		group = jobIdentity + "_group";
		this.expression = expression;
		initJob();
	}

	private void initJob() {
		jobDetail = JobBuilder.newJob(jobClazz).withIdentity(triggerIdentity, group).build();
		trigger = TriggerBuilder.newTrigger().withIdentity(triggerIdentity).withSchedule(CronScheduleBuilder.cronSchedule(expression)).build();
	}

	public void startScheduling() {
		log.info("initing trigger for {} job ...", triggerIdentity);
		try {
			scheduler = new StdSchedulerFactory().getScheduler();
			if (!isStarted) {
				scheduler.scheduleJob(jobDetail, trigger);
				scheduler.start();
				isStarted = true;
			} else {
				log.info("trigger {} was already started.", triggerIdentity);
			}
		} catch (final SchedulerException e) {
			log.warn(e.getMessage());
		}
	}

	public void reinitJob(final String cronExpression) {
		if (scheduler != null) {
			expression = cronExpression;
			try {
				scheduler.deleteJob(jobDetail.getKey());
				initJob();
				scheduler.scheduleJob(jobDetail, trigger);
			} catch (final SchedulerException e) {
				log.error("{}", e.getMessage());
			}
		}
	}

	public void shutdown() {
		if (scheduler != null) {
			try {
				scheduler.shutdown(true);
			} catch (final SchedulerException e) {
				log.error("{}", e.getMessage());
			}
		}
	}

	public String getExpression() {
		return expression;
	}

}
