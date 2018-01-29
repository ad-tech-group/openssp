package com.atg.openssp.common.logadapter;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atg.openssp.common.core.entry.SessionAgent;
import com.atg.openssp.common.provider.AdProviderReader;

/**
 * Threadsafe logging of rtb provider data with a {@link BlockingQueue}
 * 
 * @author André Schmer
 */
public class ProviderLogProcessor extends Thread {

	private static final Logger log = LoggerFactory.getLogger(ProviderLogProcessor.class);

	public static final ProviderLogProcessor instance = new ProviderLogProcessor();
	private final BlockingQueue<String> logQueue = new ArrayBlockingQueue<>(10000, true);
	private boolean shuttingDown, loggerTerminated;

	private ProviderLogProcessor() {
		super.start();
	}

	@Override
	public void run() {
		try {
			while (shuttingDown == false) {
				final String item = logQueue.take();
				LogFacade.logProviderAsync(item);
			}
		} catch (final InterruptedException e) {
			log.error(e.getMessage());
			loggerTerminated = true;
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * Writes data to fil with provider informatione.
	 * 
	 * @param agent
	 *            {@link SessionAgent}
	 * @param winner
	 *            {@link AdProviderReader}
	 */
	public void setLogData(final SessionAgent agent, final AdProviderReader winner) {
		if (shuttingDown || loggerTerminated) {
			return;
		}
		final StringBuilder sb = new StringBuilder();
		sb.append(winner.getVendorId()).append(LogFacade.LOG_DELIMITER);
		sb.append(winner.getPrice()).append(LogFacade.LOG_DELIMITER);
		sb.append(winner.getCurrrency()).append(LogFacade.LOG_DELIMITER);
		sb.append(agent.getRequestid()).append(LogFacade.LOG_DELIMITER);
		sb.append(winner.getAdid()).append(LogFacade.LOG_DELIMITER);
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
