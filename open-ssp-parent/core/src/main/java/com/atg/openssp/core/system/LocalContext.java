package com.atg.openssp.core.system;

import java.time.LocalDateTime;

import com.atg.openssp.common.configuration.Context;
import com.atg.openssp.common.configuration.ContextCache;
import com.atg.openssp.common.configuration.ContextProperties;

/**
 * @author André Schmer
 *
 */
public class LocalContext extends Context {

	// private static String dataProviderToken;

	private static String uptime;

	private static boolean isDspChannelEnabled = false;

	private static boolean isDebugEnabled = false;

	private static boolean isAdservingChannelEnabled = false;

	private static boolean isVerboseEnabled = false;

	// private static VastResolverBroker vastResolverBroker;

	private static boolean isMetricsEnabled;

	private static String sspVersion;

	private static boolean isSspChannelEnabled;

	static {
		initData();
	}

	public static void refreshContext() {
		isDebugEnabled = Boolean.parseBoolean(ContextCache.instance.get(ContextProperties.DEBUG));
		isDspChannelEnabled = Boolean.parseBoolean(ContextCache.instance.get(ContextProperties.RTB));
		isAdservingChannelEnabled = Boolean.parseBoolean(ContextCache.instance.get(ContextProperties.ADSERVING));
		isSspChannelEnabled = Boolean.parseBoolean(ContextCache.instance.get(ContextProperties.SSPCHANNEL));
		isVerboseEnabled = Boolean.parseBoolean(ContextCache.instance.get(ContextProperties.VERBOSE));
		isMetricsEnabled = Boolean.parseBoolean(ContextCache.instance.get(ContextProperties.METRICS));
	}

	private static void initData() {
		uptime = LocalDateTime.now().format(dateTimeFormatter);

		// vastResolverBroker = new VastResolverBroker();
	}

	public static boolean isSSPChannelEnabled() {
		return isSspChannelEnabled;
	}

	public static boolean isDSPChannelEnabled() {
		return isDspChannelEnabled;
	}

	public static boolean isAdservingChannelEnabled() {
		return isAdservingChannelEnabled;
	}

	public static boolean isVerboseEnabled() {
		return isVerboseEnabled;
	}

	static String getUptime() {
		return uptime;
	}

	// static void setToken(final String _token) {
	// dataProviderToken = _token;
	// }

	// static String getToken() {
	// return dataProviderToken;
	// }

	// static VastResolverBroker getVastResolverBroker() {
	// return vastResolverBroker;
	// }

	static boolean isDebugEnabled() {
		return isDebugEnabled;
	}

	// static String printContextData(final ContextProperties property) {
	// return property + "=" + ContextCache.instance.get(property);
	// }

	// static void printContextData() {
	// for (final Map.Entry<ContextProperties, String> context : ContextCache.instance.getAll().entrySet()) {
	// System.out.println(context.getKey() + "=" + context.getValue());
	// }
	// }

	static boolean isMetricsEnabled() {
		return isMetricsEnabled;
	}

	static void setVersion(final String version) {
		sspVersion = version;
	}

	static String getVersion() {
		return sspVersion;
	}

}
