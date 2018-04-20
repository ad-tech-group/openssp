package com.atg.openssp.common.logadapter;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Threadsafe logging of rtb response with a {@link BlockingQueue}
 * 
 * @author André Schmer
 */
public class RtbResponseLogProcessor extends Thread {

	private static final Logger log = LoggerFactory.getLogger(RtbResponseLogProcessor.class);

	public static final RtbResponseLogProcessor instance = new RtbResponseLogProcessor();
	private final BlockingQueue<ParamMessage> logQueue = new ArrayBlockingQueue<>(1000, true);
	private boolean shuttingDown, loggerTerminated;

	private RtbResponseLogProcessor() {
		super.start();
	}

	@Override
	public void run() {
		try {
			while (shuttingDown == false) {
				final ParamMessage item = logQueue.take();
				LogFacade.logRtbResponse(item.getMessage(), item.getParams());
			}
		} catch (final InterruptedException e) {
			log.error(e.getMessage());
			loggerTerminated = true;
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * Writes data to file with rtb response information. Uses the serialized data from the object param.
	 * 
	 * @param object
	 *            a object from which data will be serialized
	 * @param params
	 *            params to log
	 */
	public void setLogData(final Object object, final String... params) {
		// shutdown initiated, do nothing
		if (shuttingDown || loggerTerminated) {
			return;
		}
		final ParamMessage paramMessage = new ParamMessage();
		if (object != null) {
			paramMessage.setMessage(object.toString());
		} else {
			paramMessage.setMessage("");
		}
		paramMessage.setParams(params);
		try {
			logQueue.put(paramMessage);
		} catch (final InterruptedException e) {
			try {
				// try again
				logQueue.put(paramMessage);
			} catch (final InterruptedException ignore) {
				log.error("interrupted again, giving up. {}", e.getMessage());
				Thread.currentThread().interrupt();
			}
		}
	}

	/**
	 * Sets an indicator to shutdown this thread.
	 */
	public void shutDown() throws InterruptedException {
		shuttingDown = true;
		log.info("shutDown request received");
	}
}
