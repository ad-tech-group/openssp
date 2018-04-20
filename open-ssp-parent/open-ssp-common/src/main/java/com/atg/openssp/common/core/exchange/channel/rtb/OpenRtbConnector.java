package com.atg.openssp.common.core.exchange.channel.rtb;

import com.atg.openssp.common.core.connector.JsonPostConnector;
import com.atg.openssp.common.demand.Supplier;
import com.atg.openssp.common.exception.BidProcessingException;
import org.apache.http.Header;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

/**
 * A wrapper to a concrete connector using JSON notation and post method.
 * 
 * @author André Schmer
 *
 */
public class OpenRtbConnector {

	private final JsonPostConnector jsonPostConnector;

	private final String endpoint;

	private final Supplier supplier;

	public OpenRtbConnector(final Supplier supplier) {
		this.supplier = supplier;
		endpoint = supplier.getEndPoint();
		jsonPostConnector = new JsonPostConnector(supplier.isConnectionKeepAlive());
	}

	/**
	 * Prepares the post transport.
	 * 
	 * @param bidrequest
	 *            the request to send
	 * @param header
	 *            to fill in connection header
	 * @return the body result from the response
	 * @throws BidProcessingException
	 */
	String connect(final String bidrequest, final Header[] header) throws BidProcessingException {
		final HttpPost httpPost = new HttpPost(endpoint);
		httpPost.setHeaders(header);
		return jsonPostConnector.connect(new StringEntity(bidrequest, ContentType.APPLICATION_JSON), httpPost);
	}

	public Supplier getSupplier() {
		return supplier;
	}
}
