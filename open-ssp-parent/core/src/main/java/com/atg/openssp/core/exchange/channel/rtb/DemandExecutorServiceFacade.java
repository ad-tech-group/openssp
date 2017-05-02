package com.atg.openssp.core.exchange.channel.rtb;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import com.atg.openssp.common.configuration.GlobalContext;
import com.atg.openssp.common.demand.ResponseContainer;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * ServiceFacade to create a Executor Service {@link ExecutorService} and invoke a collection of callables which performing the rtb calls to the demand-side.
 * 
 * @author André Schmer
 */
public final class DemandExecutorServiceFacade {

	public static DemandExecutorServiceFacade instance = new DemandExecutorServiceFacade();

	private static ExecutorService service;

	private DemandExecutorServiceFacade() {
		final ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("DemandConnector-%d").setDaemon(true).build();
		service = Executors.newCachedThreadPool(namedThreadFactory);
	}

	/**
	 * This method invokes the given list of {@link DemandBroker} by using a {@link ExecutorService}.
	 * 
	 * @param {@code demandConnectors} a list of callable DemandBrokers which must implement the {@link Callable} interface.
	 * @return a List of {@link Future}s containing {@link ResponseContainer}
	 * @throws InterruptedException
	 */
	List<Future<ResponseContainer>> invokeAll(final List<DemandBroker> demandConnectors) throws InterruptedException {
		final long execTO = (long) (GlobalContext.getExecutionTimeout() - (GlobalContext.getExecutionTimeout() * 0.2));
		return service.invokeAll(demandConnectors, execTO, TimeUnit.MILLISECONDS);
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
