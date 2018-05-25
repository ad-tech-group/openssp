package com.atg.openssp.common.logadapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Threadsafe logging of cookie sync interaction with a {@link BlockingQueue}
 *
 * @author Brian Sorensen
 */
public class DspCookieSyncLogProcessor extends Thread {

    private static final Logger log = LoggerFactory.getLogger(DspCookieSyncLogProcessor.class);

    public static final DspCookieSyncLogProcessor instance = new DspCookieSyncLogProcessor();
    private final BlockingQueue<ParamMessage> logQueue = new ArrayBlockingQueue<>(1000, true);
    private boolean shuttingDown, loggerTerminated;

    private DspCookieSyncLogProcessor() {
        super.start();
    }

    @Override
    public void run() {
        try {
            while (shuttingDown == false) {
                final ParamMessage item = logQueue.take();
                LogFacade.logCookieSync(item.getMessage(), item.getParams());
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
     * @param object a object from which data will be serialized
     * @param params params to log
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
