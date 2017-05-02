package restful.client;

import restful.context.PathBuilder;
import restful.exception.RestException;

/**
 * @author André Schmer
 *
 */
public interface DataProviderConnector<T> {

	/**
	 * Doing the connection to the remote using the configuration data {@code config}.
	 * 
	 * @param config
	 * @return T {@link T}
	 * @throws RestException
	 */
	T connectDataProvider(PathBuilder config) throws RestException;

}
