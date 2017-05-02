package com.atg.openssp.core.system.job;

import com.atg.service.LogFacade;

import jobservice.CommonJobTrigger;

/**
 * @author André Schmer
 * 
 */
public class CacheTriggerController {

	/**
	 * Checks if the <code>cronExpression</code> for execution regarding
	 * <code>jobName</code> has changed. In case of true, the new expression
	 * will be updated.
	 * 
	 * @param cronExpression
	 */
	public static void checkForExpressionUpdate(final String cronExpression, final String jobName) {
		final CommonJobTrigger job = JobService.instance.getJobByName(jobName);

		if (job == null || job.getExpression().equals(cronExpression)) {
			// do nothing - up to date
			return;
		}
		LogFacade.logInfo("found expression changes [" + cronExpression + "] reiniting " + jobName);
		job.reinitJob(cronExpression);
	}
}
