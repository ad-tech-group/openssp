package channel.adserving;

import java.nio.charset.StandardCharsets;

import org.apache.http.client.utils.URIBuilder;

import com.atg.openssp.common.core.broker.AbstractBroker;
import com.atg.openssp.common.core.connector.JsonGetConnector;
import com.atg.openssp.common.provider.AdProviderReader;
import com.google.common.base.Stopwatch;
import com.google.gson.Gson;

import common.BidProcessingException;

/**
 * This class acts as Broker to the adserver connector. It uses a get-connector to connect direct to the adserver and retrieves the result from it.
 * 
 * @author André Schmer
 *
 */
public class AdserverBroker extends AbstractBroker {

	private final Gson gson;

	private final JsonGetConnector jsonGetConnector;

	final String scheme = "http";
	final String host = "doamin.com";
	final String path = "/path/to/target";

	final URIBuilder uriBuilder;

	public AdserverBroker() {
		uriBuilder = new URIBuilder().setCharset(StandardCharsets.UTF_8).setScheme(scheme).setHost(host).setPath(path);

		jsonGetConnector = new JsonGetConnector();
		gson = new Gson();
	}

	/**
	 * Connects to the Adserver.
	 * 
	 * @return {@link AdProviderReader}
	 * @throws BidProcessingException
	 */
	public AdProviderReader call() /* throws BidProcessingException */ throws Exception {
		final Stopwatch stopwatch = Stopwatch.createStarted();
		try {
			uriBuilder.setParameter("zoneid", String.valueOf(sessionAgent.getParamValues().getZone().getZoneId()));

			// LogFacade.logAdservingRequest("Adserving", sessionAgent.getRequestid(), uriBuilder.toString());

			final String result = jsonGetConnector.connect(uriBuilder);
			final AdProviderReader adProvider = gson.fromJson(result, AdservingCampaignProvider.class);
			stopwatch.stop();
			// LogFacade.logAdservingResponse("Adserving", sessionAgent.getRequestid(), String.valueOf(stopwatch.elapsed(TimeUnit.MILLISECONDS)), " hasResult: "
			// + (adProvider
			// .buildResponse() != null));
			return adProvider;
		} catch (final Exception e) { // BidProcessingException
			// LogFacade.logAdservingResponse("Adserving Error", sessionAgent.getRequestid(), e.getMessage());
			throw e;
		} finally {
			if (stopwatch.isRunning()) {
				stopwatch.stop();
			}
		}
	}

}
