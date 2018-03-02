package com.atg.openssp.core.system;

import java.time.LocalDateTime;

import com.atg.openssp.common.configuration.Context;
import com.atg.openssp.common.configuration.ContextCache;
import com.atg.openssp.common.configuration.ContextProperties;
import com.atg.openssp.core.system.properties.MavenProperties;

import java.time.LocalDateTime;

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

	private static String sspVersion = new MavenProperties().getVersion();


    private static boolean isSspChannelEnabled;

	//#######################################
    private static boolean isAppDataServiceEnabled = false;

    private static boolean isCurrencyDataServiceEnabled = false;

    private static boolean isLoginServiceEnabled = false;

    private static boolean isPricelayerDataServiceEnabled = false;

    private static boolean isSiteDataServiceEnabled = false;

    private static boolean isSupplierDataServiceEnabled = false;

	private static String appDataHandlerClass;

	private static String currencyDataHandlerClass;

	private static String loginHandlerClass;

	private static String pricelayerDataHandlerClass;

	private static String siteDataHandlerClass;

	private static String supplierDataHandlerClass;
    //#######################################


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

		isAppDataServiceEnabled = Boolean.parseBoolean(ContextCache.instance.get(ContextProperties.APP_DATA_SERVICE_ENABLED));
		isCurrencyDataServiceEnabled = Boolean.parseBoolean(ContextCache.instance.get(ContextProperties.CURRENCY_DATA_SERVICE_ENABLED));
		isLoginServiceEnabled = Boolean.parseBoolean(ContextCache.instance.get(ContextProperties.LOGIN_SERVICE_ENABLED));
		isPricelayerDataServiceEnabled = Boolean.parseBoolean(ContextCache.instance.get(ContextProperties.PRICELAYER_DATA_SERVICE_ENABLED));
		isSiteDataServiceEnabled = Boolean.parseBoolean(ContextCache.instance.get(ContextProperties.SITE_DATA_SERVICE_ENABLED));
		isSupplierDataServiceEnabled = Boolean.parseBoolean(ContextCache.instance.get(ContextProperties.SUPPLIER_DATA_SERVICE_ENABLED));

		appDataHandlerClass = ContextCache.instance.get(ContextProperties.APP_DATA_HANDLER_CLASS);
		currencyDataHandlerClass = ContextCache.instance.get(ContextProperties.CURRENCY_DATA_HANDLER_CLASS);
		loginHandlerClass = ContextCache.instance.get(ContextProperties.LOGIN_HANDLER_CLASS);
		pricelayerDataHandlerClass = ContextCache.instance.get(ContextProperties.PRICELAYER_DATA_HANDLER_CLASS);
		siteDataHandlerClass = ContextCache.instance.get(ContextProperties.SITE_DATA_HANDLER_CLASS);
		supplierDataHandlerClass = ContextCache.instance.get(ContextProperties.SUPPLIER_DATA_HANDLER_CLASS);
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


	public static boolean isAppDataServiceEnabled() {
		return isAppDataServiceEnabled;
	}

	public static boolean isCurrencyDataServiceEnabled() {
		return isCurrencyDataServiceEnabled;
	}

	public static boolean isLoginServiceEnabled() {
		return isLoginServiceEnabled;
	}

	public static boolean isPricelayerDataServiceEnabled() {
		return isPricelayerDataServiceEnabled;
	}

	public static boolean isSiteDataServiceEnabled() {
		return isSiteDataServiceEnabled;
	}

	public static boolean isSupplierDataServiceEnabled() {
		return isSupplierDataServiceEnabled;
	}

	public static String getAppDataHandlerClass() {
		return appDataHandlerClass;
	}

	public static String getCurrencyDataHandlerClass() {
		return currencyDataHandlerClass;
	}

	public static String getLoginHandlerClass() {
		return loginHandlerClass;
	}

	public static String getPricelayerDataHandlerClass() {
		return pricelayerDataHandlerClass;
	}

	public static String getSiteDataHandlerClass() {
		return siteDataHandlerClass;
	}

	public static String getSupplierDataHandlerClass() {
		return supplierDataHandlerClass;
	}

}
