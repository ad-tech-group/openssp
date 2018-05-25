package channel.ssp;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atg.openssp.common.core.broker.AbstractBroker;
import com.atg.openssp.common.demand.ResponseContainer;
import com.atg.openssp.common.logadapter.RtbResponseLogProcessor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import channel.adapter.SSPAdapter;
import openrtb.bidresponse.model.BidResponse;

/**
 * This class acts as Broker to a adapter used ssp channel context.
 * 
 * TODO: not yet implemented
 * 
 * @author André Schmer
 *
 */
public final class SSPBroker extends AbstractBroker implements Callable<ResponseContainer> {

	private static final Logger log = LoggerFactory.getLogger(SSPBroker.class);

	private final SSPAdapter sspAdapter;

	public SSPBroker(final SSPAdapter sspAdapter) {
		this.sspAdapter = sspAdapter;
	}

	@Override
	public ResponseContainer call() throws Exception {
		final String result = sspAdapter.getConnector().connect(getSessionAgent());
		final BidResponse.Builder bidResponse = ResponseParser.parse(result, sspAdapter);

		log.debug(bidResponse.toString());
		return null;
	}

	public SSPAdapter getAdapter() {
		return sspAdapter;
	}

	static class ResponseParser {
		private static Gson gson = new GsonBuilder().create();

		public static BidResponse.Builder parse(final String result, final SSPAdapter adapter) {
			RtbResponseLogProcessor.instance.setLogData(result, "bidresponse", adapter.getName());
			try {
				// TODO: define new responsetype
				final BidResponse response = gson.fromJson(result, BidResponse.class);
				return response.getBuilder();
			} catch (final JsonIOException | JsonSyntaxException e) {
				log.error(e.getMessage());
			}
			return null;
		}
	}
}
