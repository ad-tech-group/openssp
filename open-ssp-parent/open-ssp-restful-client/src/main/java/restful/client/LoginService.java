package restful.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;

import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.TrustStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import restful.context.PathBuilder;

import javax.net.ssl.SSLContext;

/**
 * @author André Schmer
 *
 */
public final class LoginService {
    private static final Logger log = LoggerFactory.getLogger(LoginService.class);

	/**
	 * Creates a login call to remote webservice.
	 * 
	 * @param config
	 * @return token | RestClientException
	 */
	public static String loginDataProvider(final PathBuilder config) {
		final MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("u", config.getMaster_user());
		map.add("p", config.getMaster_pw());
		final RestTemplate restTemplate;
        if ("HTTPS".equalsIgnoreCase(config.getScheme())) {
			TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

			try {
				SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
						.loadTrustMaterial(null, acceptingTrustStrategy)
						.build();
				SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

				CloseableHttpClient httpClient = HttpClients.custom()
						.setSSLSocketFactory(csf)
						.build();

				HttpComponentsClientHttpRequestFactory requestFactory =
						new HttpComponentsClientHttpRequestFactory();

				requestFactory.setHttpClient(httpClient);
				restTemplate = new RestTemplate(requestFactory);
				final HttpComponentsClientHttpRequestFactory rf = (HttpComponentsClientHttpRequestFactory) restTemplate.getRequestFactory();
				rf.setReadTimeout(2000);
				rf.setConnectTimeout(2000);
			} catch (Exception e) {
				throw new RestClientException(e.getMessage());
			}
		} else {
			restTemplate = new RestTemplate();
			final SimpleClientHttpRequestFactory rf = (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();
			rf.setReadTimeout(3000);
			rf.setConnectTimeout(30000);
		}

		try {
			final URI uri = new URIBuilder().setScheme(config.getScheme()).setCharset(StandardCharsets.UTF_8).setHost(config
					.getServer()).setPath("/ssp-services/login/token").build();
			final TokenWrapper result = restTemplate.postForObject(uri, map, TokenWrapper.class);
			return result.getToken();
		} catch (final URISyntaxException | RestClientException e) {
            log.warn(e.getMessage(), e);
		}

		return null;
	}

	private static class TokenWrapper {

		private String token;

		public String getToken() {
			return token;
		}

		@SuppressWarnings("unused")
		public void setToken(final String token) {
			this.token = token;
		}

	}
}
