package com.atg.openssp.common.core.system.job;

import com.atg.openssp.common.core.system.loader.AbstractConfigurationLoader;
import com.atg.openssp.common.watchdog.Watchdog;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * @author André Schmer
 *
 */
public class WatchdogService {

	public static WatchdogService instance = new WatchdogService();

	private final List<Watchdog> watchdogs;

	private final ExecutorService executorService;

	private WatchdogService() {
		watchdogs = new ArrayList<>();
		final ThreadFactory watchdogThreadFactory = new ThreadFactoryBuilder().setNameFormat("CoreEngine-WatchDog-%d").setDaemon(true).build();
		executorService = Executors.newCachedThreadPool(watchdogThreadFactory);
	}

	public WatchdogService initLoaderWatchdog(final AbstractConfigurationLoader loader, final boolean startJobImmediately) {
		watchdogs.add(new Watchdog(loader, startJobImmediately));
		return this;
	}

	/**
	 * Terminal Method
	 */
	public void startWatchdogs() {
		for (final Runnable r : watchdogs) {
			executorService.execute(r);
		}
	}

	public void shutdown() {
		for (final Watchdog w : watchdogs) {
			w.shutDown();
		}

		executorService.shutdown();
	}

}
