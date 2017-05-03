package com.atg.openssp.core.system.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atg.openssp.core.cache.broker.CacheController;
import com.atg.openssp.core.system.LocalContext;

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
