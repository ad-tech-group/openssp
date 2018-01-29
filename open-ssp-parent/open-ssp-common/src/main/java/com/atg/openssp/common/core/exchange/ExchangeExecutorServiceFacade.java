package com.atg.openssp.common.core.exchange;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import com.atg.openssp.common.configuration.GlobalContext;
import com.atg.openssp.common.provider.AdProviderReader;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * ServiceFacade to create a Executor Service {@link ExecutorService} and invoke a collection of callables which performing calls to a exchange-side or/and
 * calls to the DemandService which handles the rtb calls separately.
 * 
 * @author André Schmer
 *
 */
public final class ExchangeExecutorServiceFacade {

	public static final ExchangeExecutorServiceFacade instance = new ExchangeExecutorServiceFacade();

	private final ExecutorService service;

	private ExchangeExecutorServiceFacade() {
		final ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("Supply-%d").setDaemon(true).build();
		service = Executors.newCachedThreadPool(namedThreadFactory);
	}

	/**
	 * This method invokes the given list of {@link Callable} with {@link AdProviderReader} by using a {@link ExecutorService}.
	 * 
	 * @param {@code callables} a list of callables which must implement the {@link Callable} interface.
	 * @return a List of {@link Future}s containing {@link AdProviderReader}
	 * @throws InterruptedException
	 */
	public List<Future<AdProviderReader>> invokeAll(final List<Callable<AdProviderReader>> callables) throws InterruptedException {
		return service.invokeAll(callables, GlobalContext.getExecutionTimeout(), TimeUnit.MILLISECONDS);
	}

	/**
	 * ExecutorService shutdown.
	 */
	public void shutdown() {
		if (service != null && !service.isShutdown()) {
			service.shutdown();
		}
	}
}
