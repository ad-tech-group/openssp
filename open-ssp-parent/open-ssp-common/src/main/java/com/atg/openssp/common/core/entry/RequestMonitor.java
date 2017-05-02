package com.atg.openssp.common.core.entry;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author André Schmer
 *
 */
public final class RequestMonitor {

	private static AtomicLong atomicIntermediateRequestCounter = new AtomicLong(0);

	private static AtomicLong atomicTotalRequest = new AtomicLong(0);

	public static void monitorRequests() {
		atomicIntermediateRequestCounter.incrementAndGet();
		atomicTotalRequest.incrementAndGet();
	}

	public static void resetCounter() {
		atomicIntermediateRequestCounter.set(0);
		atomicTotalRequest.set(0);
	}

	public static long getCurrentCounter() {
		return RequestMonitor.atomicTotalRequest.longValue();
	}

	public static long getIntermediateRequestCounter() {
		final long t = RequestMonitor.atomicIntermediateRequestCounter.longValue();
		RequestMonitor.atomicIntermediateRequestCounter.set(0);
		return t;
	}

}
