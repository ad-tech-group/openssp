package com.atg.openssp.core.system.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.atg.openssp.core.cache.broker.CacheController;
import com.atg.openssp.core.system.LocalContext;
import com.atg.service.LogFacade;

/**
 * @author André Schmer
 * 
 */
public class CoreCacheLoaderJob implements Job {

	@Override
	public void execute(final JobExecutionContext context) throws JobExecutionException {
		if (LocalContext.isVerboseEnabled()) {
			LogFacade.logInfo(this.getClass().getSimpleName() + " fired ... next fire time: " + context.getNextFireTime());
		}
		CacheController.instance.update();
	}

}
