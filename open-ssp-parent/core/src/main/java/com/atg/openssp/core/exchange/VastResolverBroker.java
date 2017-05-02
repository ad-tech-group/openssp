package com.atg.openssp.core.exchange;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atg.openssp.common.core.connector.JsonPostConnector;

import common.BidProcessingException;

/**
 * @author André Schmer
 *
 */
public class VastResolverBroker {

	private final Logger log = LoggerFactory.getLogger(VastResolverBroker.class);

	private final JsonPostConnector jsonPostConnector;

	private final String endPoint;

	public VastResolverBroker() {
		// set up endpoint
		endPoint = "";
		jsonPostConnector = new JsonPostConnector();
	}

	public String call(final String vast, final String adid, final String zoneid) {
		final List<NameValuePair> nameValuePairs = new ArrayList<>();
		nameValuePairs.add(new BasicNameValuePair("adid", adid));
		nameValuePairs.add(new BasicNameValuePair("zoneid", zoneid));
		nameValuePairs.add(new BasicNameValuePair("vast", vast));

		try {
			return jsonPostConnector.connect(new UrlEncodedFormEntity(nameValuePairs, StandardCharsets.UTF_8), new HttpPost(endPoint));
		} catch (final BidProcessingException e) {
			log.error(e.getMessage());
		}
		return null;
	}

}
