package com.atg.openssp.common.core.connector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atg.openssp.common.exception.BidProcessingException;

/**
 * 
 * Doing a HTTP Connection with ContentType JSON and transport method POST.
 * 
 * @author André Schmer
 *
 */
public class JsonPostConnector extends DefaultConnector {

	private static final Logger log = LoggerFactory.getLogger(JsonPostConnector.class);
	public static final String NO_CONTENT = "NO-CONTENT";

	public JsonPostConnector() {
		super();
	}

	/**
	 * @param keepAlive
	 */
	public JsonPostConnector(boolean keepAlive) {
		super(keepAlive);
	}

	/**
	 * Does a connection with POST method and sends the data in {@code entity}. Reads the response and returns it as string value.
	 * 
	 * @param entity
	 * @param httpPost
	 * @return String with the body of response if connection was succesful, <code>null</code> otherwise.
	 * @throws BidProcessingException
	 */
	public String connect(final StringEntity entity, HttpPost httpPost) throws BidProcessingException {
		CloseableHttpResponse httpResponse = null;
		HttpEntity respEntity = null;
		try {
			httpPost.setEntity(entity);
            log.debug("calling: "+httpPost.getURI().toASCIIString());
            System.out.println("calling: "+httpPost.getURI().toASCIIString());
			httpResponse = httpClient.execute(httpPost);
			final int statusCode = httpResponse.getStatusLine().getStatusCode();
            log.debug("status: "+statusCode);
            System.out.println("status: "+statusCode);
			if (HttpStatus.SC_OK == statusCode) {
				respEntity = httpResponse.getEntity();
				if (respEntity != null) {
					String content = null;
					if (httpResponse.containsHeader("Content-Encoding") && httpResponse.getFirstHeader("Content-Encoding").getValue().equalsIgnoreCase("gzip")) {
						content = readContentFromStream(new GZIPInputStream(respEntity.getContent()));
					} else {
						content = readContentFromStream(respEntity.getContent());
					}
					return content;
				} else {
					log.debug("bad result: {}", statusCode);
				}
			} else if (HttpStatus.SC_NO_CONTENT == statusCode) {
				return NO_CONTENT;
			}
		} catch (final Exception e) {
            System.err.println(e.getMessage());
		    e.printStackTrace();
			log.warn(e.getMessage(), e);
			throw new BidProcessingException(e.getMessage());
		} finally {
			EntityUtils.consumeQuietly(respEntity);
			httpPost = null;
			if (httpResponse != null) {
				try {
					httpResponse.close();
				} catch (final IOException e) {
					log.error(e.getMessage());
				}
			}
		}

		return null;
	}

	private String readContentFromStream(final InputStream _inputStream) {
		final StringBuilder stringBuilder = new StringBuilder();
		try (final InputStream inputStream = _inputStream; final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
			final char[] charBuffer = new char[2048];
			int bytesRead = -1;
			while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
				stringBuilder.append(charBuffer, 0, bytesRead);
			}
		} catch (final IOException e) {
			log.error(e.getMessage(), e);
		}
		return stringBuilder.toString();
	}

}
