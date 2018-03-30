package channel.adserving;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import com.atg.openssp.common.configuration.ContextCache;
import com.atg.openssp.common.configuration.ContextProperties;
import com.atg.openssp.common.configuration.GlobalContext;
import org.apache.http.client.utils.URIBuilder;

import com.atg.openssp.common.core.broker.AbstractBroker;
import com.atg.openssp.common.core.connector.JsonGetConnector;
import com.atg.openssp.common.exception.BidProcessingException;
import com.atg.openssp.common.provider.AdProviderReader;
import com.google.common.base.Stopwatch;
import com.google.gson.Gson;

/**
 * This class acts as Broker to the adserver connector. It uses a get-connector to connect direct to the adserver and retrieves the result from it.
 * 
 * @author André Schmer
 *
 */
public class AdserverBroker extends AbstractBroker {

    private AdserverBrokerHandler handler;

    public AdserverBroker() {
		String handlerClassName = GlobalContext.getAdserverBrokerHandlerClass();
		if (handlerClassName != null && !"".equals(handlerClassName)) {
			try {
				Class c = Class.forName(handlerClassName);
				handler = (AdserverBrokerHandler) c.getConstructor(null).newInstance(null);
			} catch (Exception e) {
				handler = new TestAdserverBrokerHandler();
				e.printStackTrace();
			}
		} else {
			handler = new TestAdserverBrokerHandler();
		}
	}

	/**
	 * Connects to the Adserver.
	 * 
	 * @return Optional of {@link AdProviderReader}
	 * @throws BidProcessingException
	 */
	public Optional<AdProviderReader> call() throws BidProcessingException {
		final Stopwatch stopwatch = Stopwatch.createStarted();
		try {
			final String result = handler.getConnector().connect(handler.getUriBuilder());
			final AdProviderReader adProvider = handler.getGson().fromJson(result, handler.getProvider());
			stopwatch.stop();
			return Optional.ofNullable(adProvider);
		} finally {
			if (stopwatch.isRunning()) {
				stopwatch.stop();
			}
		}
	}

}
