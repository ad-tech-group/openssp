package channel.adapter;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import com.atg.openssp.common.core.connector.JsonPostConnector;
import com.atg.openssp.common.core.entry.SessionAgent;
import com.atg.openssp.common.exception.BidProcessingException;

/**
 * @author André Schmer
 *
 */
public class TrialAdapterConnector implements AdapterConnector {

	private static final long serialVersionUID = -6978598309872993590L;

	private final JsonPostConnector jsonPostConnector;

	private final SSPAdapter sspAdapter;

	public TrialAdapterConnector(final SSPAdapter sspAdapter) {
		this.sspAdapter = sspAdapter;
		jsonPostConnector = new JsonPostConnector();
	}

	@Override
	public String connect(final SessionAgent sessionAgent) throws BidProcessingException {
		final String jsonRequest = new TrialAdapterBuilder(sessionAgent).build();

		final HttpPost httpPost = new HttpPost(sspAdapter.getEndpoint());
		return jsonPostConnector.connect(new StringEntity(jsonRequest, ContentType.APPLICATION_JSON), httpPost);
	}

}
