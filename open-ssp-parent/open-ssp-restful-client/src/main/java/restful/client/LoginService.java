package restful.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import org.apache.http.client.utils.URIBuilder;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import restful.context.PathBuilder;

/**
 * @author André Schmer
 *
 */
public final class LoginService {

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
		final RestTemplate restTemplate = new RestTemplate();
		final SimpleClientHttpRequestFactory rf = (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();
		rf.setReadTimeout(3000);
		rf.setConnectTimeout(30000);
		try {
			final URI uri = new URIBuilder().setScheme("https").setCharset(StandardCharsets.UTF_8).setHost(config
					.getServer()).setPath("/open-ssp-services/login/token").build();
			final TokenWrapper result = restTemplate.postForObject(uri, map, TokenWrapper.class);
			return result.getToken();
		} catch (final URISyntaxException | RestClientException e) {
			System.out.println("[WARNING] LoginService: " + e.getMessage());
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
