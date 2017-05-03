package com.atg.openssp.common.logadapter;

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
	private static Logger systemRequestLogger;
	// private static Logger pidLogger;
	private static Logger providerLogger;
	// private static Logger adservingRequestLogger;
	// private static Logger adservingResponseLogger;

	private static String REQUEST_INFO = "request";
	// private static String DEBUGGING = "debugging";
	private static String PROVIDER = "provider";
	// private static String PID = "pid";

	private static String BID_RESPONSE = "bid-response";
	private static String BID_REQUEST = "bid-request";

	// private static String ADSERVING_REQUEST = "adserving-request";
	// private static String ADSERVING_RESPONSE = "adserving-response";

	public static final String LOG_DELIMITER = "#";

	private static Level loglevel = Level.INFO;

	static {
		systemRequestLogger = LogManager.getLogger(REQUEST_INFO);
		// pidLogger = LogManager.getLogger(PID);
		rtbResponseLogger = LogManager.getLogger(BID_RESPONSE);
		rtbRequestLogger = LogManager.getLogger(BID_REQUEST);
		providerLogger = LogManager.getLogger(PROVIDER);
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
		rtbResponseLogger.info("{} {}", params, msg);
	}

	public static void logRtbRequest(final String msg, final String... params) {
		rtbRequestLogger.info("{} {}", params, msg);
	}

	// public static void logAdservingRequest(final String msg, final String... params) {
	// adservingRequestLogger.info("{} {}", msg, params);
	// }

	// public static void logAdservingResponse(final String msg, final String... params) {
	// adservingResponseLogger.info("{} {}", msg, params);
	// }

	public static void logRequestAsync(final String msg) {
		systemRequestLogger.info(msg);
	}

	public static void logProviderAsync(final String msg) {
		providerLogger.info(msg);
	}

	public static String getLogLevel() {
		return loglevel.name();
	}

}
