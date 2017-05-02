package com.atg.openssp.common.configuration;

/**
 * @author André Schmer
 *
 */
public class GlobalContext extends Context {

	private static long executionTimeout;
	private static float drawModeration;

	public static void refreshContext() {
		// default value is 500 milliseconds
		executionTimeout = Long.parseLong(ContextCache.instance.get(ContextProperties.EXECUTION_TIMEOUT) != null ? ContextCache.instance.get(
		        ContextProperties.EXECUTION_TIMEOUT) : "500");

		// default value is 0.3
		drawModeration = Float.parseFloat(ContextCache.instance.get(ContextProperties.DRAW_MODERATION) != null ? ContextCache.instance.get(
		        ContextProperties.DRAW_MODERATION) : "0.3");
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

}
