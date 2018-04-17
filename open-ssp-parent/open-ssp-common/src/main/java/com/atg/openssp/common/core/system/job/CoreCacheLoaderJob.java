package com.atg.openssp.common.core.system.job;

import com.atg.openssp.common.core.cache.broker.CacheController;
import com.atg.openssp.common.core.system.LocalContext;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author André Schmer
 * 
 */
public class CoreCacheLoaderJob implements Job {

	private static final Logger log = LoggerFactory.getLogger(CoreCacheLoaderJob.class);

	@Override
	public void execute(final JobExecutionContext context) throws JobExecutionException {
		if (LocalContext.isVerboseEnabled()) {
			log.info(this.getClass().getSimpleName() + " fired ... next fire time: " + context.getNextFireTime());
		}
		CacheController.instance.update();
	}

}
