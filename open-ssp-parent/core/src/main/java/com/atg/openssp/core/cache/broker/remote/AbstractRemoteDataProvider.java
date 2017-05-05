package com.atg.openssp.core.cache.broker.remote;

import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atg.openssp.common.cache.broker.DataBrokerObserver;
import com.atg.openssp.common.configuration.ContextCache;
import com.atg.openssp.common.configuration.ContextProperties;
import com.atg.openssp.common.exception.EmptyHostException;

import restful.client.RemoteDataProviderConnector;
import restful.context.PathBuilder;
import restful.exception.RestException;

/**
 * @author André Schmer
 *
 */
abstract class AbstractRemoteDataProvider extends DataBrokerObserver {

	private final Logger log = LoggerFactory.getLogger(AbstractRemoteDataProvider.class);

	protected String connect() throws RestException, EmptyHostException {
		return new RemoteDataProviderConnector().connect(getRestfulContext());
	}

	protected PathBuilder getDefaulPathBuilder() throws EmptyHostException {
		final PathBuilder pathBuilder = new PathBuilder();

		try {
			final URI remoteEndpintURI = new URI(ContextCache.instance.get(ContextProperties.DATA_PROVIDER_URL));
			if (remoteEndpintURI.getHost() == null) {
				throw new EmptyHostException("No Host is defined, see data-provider-url in global.runtime.xml");
			}
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
