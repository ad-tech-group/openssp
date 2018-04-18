package com.atg.openssp.common.core.system;

import com.atg.openssp.common.buffer.AdservingLatencyBuffer;
import com.atg.openssp.common.configuration.ContextCache;
import com.atg.openssp.common.configuration.ContextProperties;
import com.atg.openssp.common.core.cache.broker.CacheController;
import com.atg.openssp.common.core.exchange.ExchangeExecutorServiceFacade;
import com.atg.openssp.common.core.exchange.channel.rtb.DemandExecutorServiceFacade;
import com.atg.openssp.common.core.system.job.*;
import com.atg.openssp.common.core.system.loader.ConfigLoader;
import com.atg.openssp.common.core.system.loader.GlobalContextLoader;
import com.atg.openssp.common.core.system.loader.LocalContextLoader;
import com.atg.openssp.common.core.system.properties.MavenProperties;
import com.atg.openssp.common.logadapter.LogFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.CatalinaUtil;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author André Schmer
 *
 */
public class ApplicationInit extends GenericServlet {

	private final Logger log = LoggerFactory.getLogger(ApplicationInit.class);

	private static final long serialVersionUID = 1L;

	@Override
	public void init() {
		InitLogging.setSystemProperties();
		log.info("**** Initing core application ****");
		LocalContext.setVersion(new MavenProperties().getVersion());
		log.info("**** SSP Version: " + LocalContext.getVersion() + " ****");

		final CountDownLatch cdl = new CountDownLatch(3);
		// loading static config
		new ConfigLoader(cdl).readValues();
		// initing watchdogs for global.runtime.xml and local.runtime.xml
		WatchdogService.instance.initLoaderWatchdog(new LocalContextLoader(cdl), true).initLoaderWatchdog(new GlobalContextLoader(cdl), true).startWatchdogs();
		try {
			cdl.await();
		} catch (final InterruptedException e) {
			log.error(e.getMessage());
		}

		// initing cache update job
		// every 5 minutes on every day
		JobService.instance.initJob(JobConfig.newBuilder().setJobClass(CoreCacheLoaderJob.class).setJobName("CoreCacheLoaderJob").setExpression(ContextCache.instance.get(
		        ContextProperties.TRIGGER_EXPESSION)).build());

		// initing heartbeat job
		// every 10seconds on every day
		JobService.instance.initJob(JobConfig.newBuilder().setJobClass(HeartBeatJob.class).setJobName("HeartBeatJob").setExpression("0/60 * * * * ?").build());

		// initing cachecleaner job
		// every day at midnight
		JobService.instance.initJob(JobConfig.newBuilder().setJobClass(ResetCounterJob.class).setJobName("CleanerJob").setExpression("0 0 0 1/1 * ?").build());

		// initing cache data
		CacheController.instance.update();


		if (LocalContext.isMetricsEnabled()) {
			MetricFactory.instance.initMetrics();
			MetricFactory.instance.startMetricReport();
		}

		// retrieving system informations
		log.info("**** Initial Loglevel: " + LogFacade.getLogLevel() + " ****");
		log.info("**** SSP instance name: " + CatalinaUtil.instanceName() + " ****");
		log.info("**** SSP pid: " + CatalinaUtil.pid() + " ****");
		log.info("**** SSP uptime: " + LocalContext.getUptime() + " ****");
	}

	@Override
	public void service(final ServletRequest req, final ServletResponse res) throws ServletException, IOException {}

	@Override
	public void destroy() {
		JobService.instance.shutdown();
		WatchdogService.instance.shutdown();
		DemandExecutorServiceFacade.instance.shutdown();
		ExchangeExecutorServiceFacade.instance.shutdown();
		AdservingLatencyBuffer.getBuffer().shutDown();
		super.destroy();
	}

}
