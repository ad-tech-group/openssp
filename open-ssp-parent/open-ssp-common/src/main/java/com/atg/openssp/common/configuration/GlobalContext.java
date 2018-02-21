package com.atg.openssp.common.configuration;

/**
 * @author André Schmer
 *
 */
public class GlobalContext extends Context {

	private static long executionTimeout;
	private static float drawModeration;
	private static String entryValidatorHandlerClass;
	private static String adserverBrokerHandlerClass;

	public static void refreshContext() {
		// default value is 500 milliseconds
		executionTimeout = Long.parseLong(ContextCache.instance.get(ContextProperties.EXECUTION_TIMEOUT) != null ? ContextCache.instance.get(
		        ContextProperties.EXECUTION_TIMEOUT) : "500");

		// default value is 0.3
		drawModeration = Float.parseFloat(ContextCache.instance.get(ContextProperties.DRAW_MODERATION) != null ? ContextCache.instance.get(
		        ContextProperties.DRAW_MODERATION) : "0.3");

		entryValidatorHandlerClass = ContextCache.instance.get(ContextProperties.VALIDATOR_HANDLER_CLASS);
		adserverBrokerHandlerClass = ContextCache.instance.get(ContextProperties.ADSERVER_BROKER_HANDLER_CLASS);
	}

	/**
	 * The timeout in milliseconds.
	 * <p>
	 * The value can be changed outside in {@code local.runtime.xml} properties file.
	 * 
	 * @return timeout in milliseconds
	 */
	public static long getExecutionTimeout() {
		return executionTimeout;
	}

	/**
	 * The draw moderation.
	 * <p>
	 * The value can be changed outside in {@code local.runtime.xml} properties file.
	 * 
	 * @return draw moderation
	 */
	public static float getDrawModeration() {
		return drawModeration;
	}

    public static String getEntryValidatorHandlerClass() {
        return entryValidatorHandlerClass;
    }

    public static String getAdserverBrokerHandlerClass() {
        return adserverBrokerHandlerClass;
    }

}
