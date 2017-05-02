package com.atg.openssp.core.cache.broker.remote;

import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atg.openssp.common.cache.broker.DataBrokerObserver;
import com.atg.openssp.common.configuration.ContextCache;
import com.atg.openssp.common.configuration.ContextProperties;

import restful.client.RemoteDataProviderConnector;
import restful.context.PathBuilder;
import restful.exception.RestException;

/**
 * @author André Schmer
 *
 */
abstract class AbstractRemoteDataProvider extends DataBrokerObserver {

	private final Logger log = LoggerFactory.getLogger(AbstractRemoteDataProvider.class);

	protected String connect() throws RestException {
		final String result = new RemoteDataProviderConnector().connect(getRestfulContext());
		return result;
	}

	protected PathBuilder getDefaulPathBuilder() {
		final PathBuilder pathBuilder = new PathBuilder();

		try {
			final URI remoteEndpintURI = new URI(ContextCache.instance.get(ContextProperties.DATA_PROVIDER_URL));
			pathBuilder.setServer(remoteEndpintURI.getHost());
			if (remoteEndpintURI.getScheme() == null) {
				pathBuilder.setScheme("http");
			} else {
				pathBuilder.setScheme(remoteEndpintURI.getScheme());
			}
		} catch (final URISyntaxException e) {
			log.error(e.getMessage());
		}

		return pathBuilder;
	}

}
