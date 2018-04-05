package com.atg.openssp.common.logadapter;

import com.atg.openssp.common.core.entry.SessionAgent;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import openrtb.bidrequest.model.BidRequest;
import openrtb.bidrequest.model.Site;
import openrtb.bidrequest.model.User;
import openrtb.bidresponse.model.Bid;
import openrtb.bidresponse.model.BidResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Threadsafe logging of rtb request data with a {@link BlockingQueue}
 * 
 * @author André Schmer
 */
public class AuctionLogProcessor extends Thread {

	private static final Logger log = LoggerFactory.getLogger(AuctionLogProcessor.class);

	public static final AuctionLogProcessor instance = new AuctionLogProcessor();
	private final BlockingQueue<AuctionLogEntry> logQueue = new ArrayBlockingQueue<>(1000, true);
	private boolean shuttingDown, loggerTerminated;

	private AuctionLogProcessor() {
		super.start();
	}

	@Override
	public void run() {
		try {
			while (shuttingDown == false) {
				final AuctionLogEntry item = logQueue.take();
				if (item != null) {
                    LogFacade.logAuction(item);
                }
			}
		} catch (final InterruptedException e) {
			log.error(e.getMessage());
			loggerTerminated = true;
			Thread.currentThread().interrupt();
		}
	}

    /**
     * Writes data to file with request information.
     *  @param loggingId
     * @param requestId
     * @param user
     * @param supplierId
     * @param supplierName
     * @param site
     * @param o
     *            {@link SessionAgent}
     */
    public void setLogData(String loggingId, String requestId, User user, Long supplierId, String supplierName, Site site, List<Bid> o) {
        if (shuttingDown || loggerTerminated) {
            return;
        }
        AuctionLogEntry ale = new AuctionLogEntry();
        try {
            ale.setLogginId(loggingId);
            ale.setRequestId(requestId);
            if (user != null) {
				ale.setUserId(user.getId());
			}
            ale.setSupplierId(supplierId);
            ale.setSupplierName(supplierName);
            if (site != null) {
                ale.setPage(site.getPage());
                ale.setSite(site);
            }
            ale.setBids(o);
            if (ale.getLogginId() == null) {
                ale.setLogginId("UNKN");
            }
            logQueue.put(ale);
        } catch (final InterruptedException e) {
            try {
                // try again
                logQueue.put(ale);
            } catch (final InterruptedException ignore) {
                log.error("interrupted again, giving up.");
                Thread.currentThread().interrupt();
            }
        }
    }

    /*
	public void setLogData(String id, BidRequest bidRequest) {
		if (shuttingDown || loggerTerminated) {
			return;
		}
		StringBuilder sb = new StringBuilder(loggingId);
		sb.append("|");
		sb.append("N");
		try {
			logQueue.put(memo);
		} catch (final InterruptedException e) {
			try {
				// try again
				logQueue.put(memo);
			} catch (final InterruptedException ignore) {
				log.error("interrupted again, giving up.");
				Thread.currentThread().interrupt();
			}
		}
	}
	*/

	/**
	 * Sets an indicator to shutdown this thread.
	 */
	public void shutDown() {
		shuttingDown = true;
		log.info("shutDown request received");
	}

}
