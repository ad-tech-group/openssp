package com.atg.openssp.common.core.connector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.BidProcessingException;

/**
 * 
 * Doing a HTTP Connection with ContentType JSON and transport method GET.
 * 
 * @author André Schmer
 *
 */
public class JsonGetConnector extends DefaultConnector {

	private static final Logger log = LoggerFactory.getLogger(JsonGetConnector.class);

	/**
	 * Creates a connection and reads the response. Return the response as string value.
	 * 
	 * @param uriBuilder
	 * @return String with the body of response if connection was succesful, <code>null</code> otherwise.
	 * @throws BidProcessingException
	 */
	public String connect(final URIBuilder uriBuilder) throws BidProcessingException {
		String result = null;
		CloseableHttpResponse httpResponse = null;
		HttpEntity respEntity = null;
		try {
			final HttpGet httpGet = new HttpGet(uriBuilder.build());
			httpResponse = httpClient.execute(httpGet);
			final int resultCode = httpResponse.getStatusLine().getStatusCode();

			if (HttpStatus.SC_OK == resultCode) {
				respEntity = httpResponse.getEntity();

				if (respEntity != null) {
					final StringBuilder stringBuilder = new StringBuilder();
					try (InputStream inputStream = respEntity.getContent(); BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
						final char[] charBuffer = new char[2048];
						int bytesRead = -1;
						while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
							stringBuilder.append(charBuffer, 0, bytesRead);
						}
					}
					result = stringBuilder.toString();
				}
			}
		} catch (final IOException | URISyntaxException e) {
			throw new BidProcessingException("IO " + e.getMessage());
		} finally {
			EntityUtils.consumeQuietly(respEntity);
			if (httpResponse != null) {
				try {
					httpResponse.close();
				} catch (final IOException e) {
					log.error(e.getMessage());
				}
			}
		}

		return result;
	}

}
