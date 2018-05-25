package com.atg.openssp.common.logadapter;

import com.google.gson.Gson;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

/**
 * Acts as a facade to log4j2 logging framework.
 * 
 * @author André Schmer
 *
 */
public class LogFacade {

	private static Logger rtbResponseLogger;
	private static Logger rtbRequestLogger;
    private static Logger cookieSyncLogger;
	private static Logger systemRequestLogger;
	// private static Logger pidLogger;
	private static Logger providerLogger;
	// private static Logger adservingRequestLogger;
	// private static Logger adservingResponseLogger;
    private static Logger timeInfoLogger;
	private static Logger dataBrokerLogger;
	private static Logger auctionLogger;

	private static String REQUEST_INFO = "request";
	// private static String DEBUGGING = "debugging";
	private static String PROVIDER = "provider";
	// private static String PID = "pid";

	private static String BID_RESPONSE = "bid-response";
	private static String BID_REQUEST = "bid-request";

	private static String COOKIE_SYNC = "cookie-sync";

	private static String TIME_INFO = "time-info";
	private static String DATA_BROKER = "data-broker";
	private static String AUCTION = "auction";

	// private static String ADSERVING_REQUEST = "adserving-request";
	// private static String ADSERVING_RESPONSE = "adserving-response";

	public static final String LOG_DELIMITER = "#";

	private static Level loglevel = Level.INFO;

	static {
		// pidLogger = LogManager.getLogger(PID);
		// adservingRequestLogger = LogManager.getLogger(ADSERVING_REQUEST);
		// adservingResponseLogger = LogManager.getLogger(ADSERVING_RESPONSE);
	}

	public static void initLogging(final Level level) {
		loglevel = level;
		final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		final Configuration config = ctx.getConfiguration();
		final LoggerConfig loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
		loggerConfig.setLevel(level);
		ctx.updateLoggers();
	}

	public static boolean isDebugEnabled() {
		return loglevel == Level.DEBUG;
	}

	// public static void logPid(final String msg) {
	// pidLogger.info(msg);
	// }

	public static void logRtbResponse(final String msg, final String... params) {
	    if (rtbResponseLogger == null) {
	        synchronized (LogFacade.class) {
                rtbResponseLogger = LogManager.getLogger(BID_RESPONSE);
            }
        }
        rtbResponseLogger.debug("{} {}", params, msg);
	}

	public static void logRtbRequest(final String msg, final String... params) {
        if (rtbRequestLogger == null) {
            synchronized (LogFacade.class) {
                rtbRequestLogger = LogManager.getLogger(BID_REQUEST);
            }
        }
        rtbRequestLogger.debug("{} {}", params, msg);
	}

    public static void logCookieSync(final String msg, final String... params) {
            if (cookieSyncLogger == null) {
            synchronized (LogFacade.class) {
                cookieSyncLogger = LogManager.getLogger(COOKIE_SYNC);
            }
        }
        cookieSyncLogger.debug("{} {}", params, msg);
    }

    // public static void logAdservingRequest(final String msg, final String... params) {
	// adservingRequestLogger.info("{} {}", msg, params);
	// }

	// public static void logAdservingResponse(final String msg, final String... params) {
	// adservingResponseLogger.info("{} {}", msg, params);
	// }

	public static void logRequestAsync(final String msg) {
        if (systemRequestLogger == null) {
            synchronized (LogFacade.class) {
                systemRequestLogger = LogManager.getLogger(REQUEST_INFO);
            }
        }
        systemRequestLogger.info(msg);
	}

	public static void logProviderAsync(final String msg) {
        if (providerLogger == null) {
            synchronized (LogFacade.class) {
                providerLogger = LogManager.getLogger(PROVIDER);
            }
        }
        providerLogger.info(msg);
	}

	public static void logTimeInfo(final String msg, final String... params) {
		if (timeInfoLogger == null) {
			synchronized (LogFacade.class) {
                timeInfoLogger = LogManager.getLogger(TIME_INFO);
			}
		}
        timeInfoLogger.info("{} {}", params, msg);
	}

	public static void logDataBroker(final String msg, final String... params) {
		if (dataBrokerLogger == null) {
			synchronized (LogFacade.class) {
				dataBrokerLogger = LogManager.getLogger(DATA_BROKER);
			}
		}
		dataBrokerLogger.info("{} {}", params, msg);
	}

	public static void logAuction(final AuctionLogEntry ale, final String... params) {
		if (auctionLogger == null) {
			synchronized (LogFacade.class) {
				auctionLogger = LogManager.getLogger(AUCTION);
			}
		}
		StringBuilder sb = new StringBuilder(ale.getLogginId());
        sb.append("|");
        sb.append(ale.getRequestId());
        sb.append("|");
        sb.append(ale.getUserId());
        sb.append("|");
        sb.append(ale.getSupplierId());
        sb.append("|");
        sb.append(ale.getSupplierName());
        sb.append("|");
        if (ale.getSite() == null) {
			sb.append("UNKN");
		} else {
			sb.append(ale.getSite().getId());
		}
        sb.append("|");
		sb.append(ale.getPage());
        sb.append("|");
		sb.append(new Gson().toJson(ale.getBids()));
		auctionLogger.info("{} {}", params, sb.toString());
	}

	public static String getLogLevel() {
		return loglevel.name();
	}

}
