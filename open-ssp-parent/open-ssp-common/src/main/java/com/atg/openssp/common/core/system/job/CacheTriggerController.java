package com.atg.openssp.common.core.system.job;

import com.atg.openssp.common.jobservice.CommonJobTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author André Schmer
 * 
 */
public class CacheTriggerController {

	private static final Logger log = LoggerFactory.getLogger(CacheTriggerController.class);

	/**
	 * Checks if the <code>cronExpression</code> for execution regarding <code>jobName</code> has changed. In case of true, the new expression will be updated.
	 * 
	 * @param cronExpression
	 */
	public static void checkForExpressionUpdate(final String cronExpression, final String jobName) {
		final CommonJobTrigger job = JobService.instance.getJobByName(jobName);

		if (job == null || job.getExpression().equals(cronExpression)) {
			// do nothing - up to date
			return;
		}
		log.info("found expression changes [" + cronExpression + "] reiniting " + jobName);
		job.reinitJob(cronExpression);
	}
}
