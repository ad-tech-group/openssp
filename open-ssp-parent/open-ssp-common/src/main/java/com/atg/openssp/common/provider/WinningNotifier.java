package com.atg.openssp.common.provider;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atg.openssp.common.core.entry.SessionAgent;
import com.atg.openssp.common.exception.NotifyingException;

/**
 * @author André Schmer
 * 
 */
public class WinningNotifier implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(WinningNotifier.class);

	private final String nurl;
	private final float price;
	private final SessionAgent agent;

	private static final int ATTEMPTS = 6;

	public WinningNotifier(final String nurl, final float price, final SessionAgent agent) {
		this.nurl = nurl;
		this.price = price;
		this.agent = agent;
	}

	@Override
	public void run() {
		final String url = MarkupParser.parseNurl(nurl, price);
		if (!StringUtils.isEmpty(url)) {
			boolean failed = false;
			try {
				final String decoded = URLDecoder.decode(url, "UTF-8");
				for (int i = 1; i <= ATTEMPTS; i++) {
					try {
						notifyWinner(decoded);
						if (i > 1) {
							log.info("Notifying finally succeed. Tried {} times. {} [...]", i, decoded.substring(0, 80));
						}
					} catch (final NotifyingException e) {
						log.error("winning notify fails {} {}", i, e.getMessage());
						if (i == ATTEMPTS) {
							log.error("notifying the winner unsuccessful - giving up ...");
							failed = true;
							break;
						}
						try {
							final CountDownLatch latch = new CountDownLatch(1);
							latch.await(100 * (long) i, TimeUnit.MILLISECONDS);
						} catch (final InterruptedException ignore) {}
						continue;
					}
					break;
				}
			} catch (final UnsupportedEncodingException e) {
				log.error(e.getMessage());
			}
			if (!failed) {
				final StringBuilder sb = new StringBuilder();
				sb.append(agent.getRequestid()).append("#").append(price).append("#").append(url);
				log.info("{}", sb.toString());
			}
		}
	}

	private void notifyWinner(final String url) throws NotifyingException {
		final HttpPost httpPost = new HttpPost(url);
		final CloseableHttpClient httpclient = HttpClientBuilder.create().build();
		final RequestConfig config = RequestConfig.custom().setConnectTimeout(2000).setSocketTimeout(2000).setExpectContinueEnabled(false).build();
		httpPost.setConfig(config);

		CloseableHttpResponse httpResponse = null;
		int statusCode = -1;
		try {
			httpResponse = httpclient.execute(httpPost);
			statusCode = httpResponse.getStatusLine().getStatusCode();
		} catch (final IOException e) {
			throw new NotifyingException(url.substring(0, 80) + " [...] ," + e.getMessage() + ", status " + statusCode + ".");
		} finally {
			httpPost.releaseConnection();
			try {
				httpclient.close();
				if (httpResponse != null) {
					httpResponse.close();
				}
			} catch (final IOException e) {
				log.error(e.getMessage());
			}
		}
	}
}
