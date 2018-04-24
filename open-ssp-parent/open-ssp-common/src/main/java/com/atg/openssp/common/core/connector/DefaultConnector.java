package com.atg.openssp.common.core.connector;

import java.util.concurrent.TimeUnit;

import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import com.atg.openssp.common.configuration.Context;

/**
 * @author André Schmer
 *
 */
public class DefaultConnector {

	protected CloseableHttpClient httpClient;

	/**
	 *
	 */
	public DefaultConnector() {
		this(true);
	}

	/**
	 * 
	 */
	public DefaultConnector(boolean keepAlive) {
		final PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		cm.closeIdleConnections(120, TimeUnit.SECONDS);

		// would be nice to set this from outside -> keep alive
		final SocketConfig sConfig = SocketConfig.custom().setSoKeepAlive(keepAlive).setSoTimeout(Context.SOCKET_TO).build();
		cm.setDefaultSocketConfig(sConfig);

		cm.setMaxTotal(150);
		cm.setDefaultMaxPerRoute(150);
		cm.setValidateAfterInactivity(0);

		final HttpRequestRetryHandler rh = new DefaultHttpRequestRetryHandler(3, true);
		httpClient = HttpClients.custom().setRetryHandler(rh).setConnectionManager(cm).build();
	}

}
