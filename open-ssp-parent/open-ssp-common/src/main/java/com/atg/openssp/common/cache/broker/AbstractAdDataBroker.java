package com.atg.openssp.common.cache.broker;

import com.atg.openssp.common.configuration.ContextCache;
import com.atg.openssp.common.configuration.ContextProperties;
import com.atg.openssp.common.exception.EmptyHostException;
import restful.client.JsonDataProviderConnector;
import restful.context.PathBuilder;
import restful.exception.RestException;

/**
 * Generic Broker to connect to a remote webservice.
 * 
 * @author André Schmer
 *
 */
public abstract class AbstractAdDataBroker<T> extends DataBrokerObserver {

	protected T connect(final Class<T> clazz) throws RestException, EmptyHostException {
		return new JsonDataProviderConnector<>(clazz).connectDataProvider(getRestfulContext());
	}

	protected PathBuilder getDefaulPathBuilder() {
		final PathBuilder pathBuilder = new PathBuilder();
		pathBuilder.setMaster_pw(ContextCache.instance.get(ContextProperties.MASTER_PW));
		pathBuilder.setMaster_user(ContextCache.instance.get(ContextProperties.MASTER_USER));
		pathBuilder.setScheme(ContextCache.instance.get(ContextProperties.ADSERVER_PROVIDER_SCHEME));
		pathBuilder.setHost(ContextCache.instance.get(ContextProperties.ADSERVER_PROVIDER_HOST));
		pathBuilder.setPort(ContextCache.instance.get(ContextProperties.ADSERVER_PROVIDER_PORT));
		return pathBuilder;
	}

	/**
	 * @return the context of the restful service to connect with {see PathBuilder}.
	 * @throws EmptyHostException
	 */
	public abstract PathBuilder getRestfulContext() throws EmptyHostException;
}
