package com.atg.openssp.common.logadapter;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atg.openssp.common.core.entry.SessionAgent;

/**
 * Threadsafe logging of rtb request data with a {@link BlockingQueue}
 * 
 * @author André Schmer
 */
public class RequestLogProcessor extends Thread {

	private static final Logger log = LoggerFactory.getLogger(RequestLogProcessor.class);

	public static final RequestLogProcessor instance = new RequestLogProcessor();
	private final BlockingQueue<String> logQueue = new ArrayBlockingQueue<>(1000, true);
	private boolean shuttingDown, loggerTerminated;

	private RequestLogProcessor() {
		super.start();
	}

	@Override
	public void run() {
		try {
			while (shuttingDown == false) {
				final String item = logQueue.take();
				LogFacade.logRequestAsync(item);
			}
		} catch (final InterruptedException e) {
			log.error(e.getMessage());
			loggerTerminated = true;
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * Writes data to file with request information.
	 * 
	 * @param agent
	 *            {@link SessionAgent}
	 */
	public void setLogData(final SessionAgent agent) {
		if (shuttingDown || loggerTerminated) {
			return;
		}
		final StringBuilder sb = new StringBuilder();
		sb.append(agent.getRequestid()).append(LogFacade.LOG_DELIMITER);

		// referrer
		sb.append(agent.getRemoteIP());
		try {
			logQueue.put(sb.toString());
		} catch (final InterruptedException e) {
			try {
				// try again
				logQueue.put(sb.toString());
			} catch (final InterruptedException ignore) {
				log.error("interrupted again, giving up.");
				Thread.currentThread().interrupt();
			}
		}
	}

	/**
	 * Sets an indicator to shutdown this thread.
	 */
	public void shutDown() {
		shuttingDown = true;
		log.info("shutDown request received");
	}
}
