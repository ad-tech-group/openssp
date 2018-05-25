package restful.client;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.TrustStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import restful.context.PathBuilder;
import restful.context.RestfulContext;
import restful.exception.RestException;

import javax.net.ssl.SSLContext;

/**
 * Generic approach to connect a remote data provider and load specific data transfer objects.
 * 
 * @author André Schmer
 *
 */
public class JsonDataProviderConnector<T> implements DataProviderConnector<T> {

	private final Logger LOGGER = LoggerFactory.getLogger(JsonDataProviderConnector.class);

	private final Class<T> dtoType;

	private final List<HttpMessageConverter<?>> httpMessageConverters;

	public JsonDataProviderConnector(final Class<T> dtoType) {
		this.dtoType = dtoType;
		final ObjectMapper objectMapper = createObjectMapper();

		final MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter = new MappingJackson2HttpMessageConverter();
		mappingJacksonHttpMessageConverter.setObjectMapper(objectMapper);

		// create message converters list
		httpMessageConverters = new ArrayList<>();
		httpMessageConverters.add(mappingJacksonHttpMessageConverter);

    }

	@Override
	public T connectDataProvider(final PathBuilder config) throws RestException {
		return doConnection(config, 3);
	}

	private T doConnection(final PathBuilder config, int attempts) throws RestException {
		if (StringUtils.isEmpty(RestfulContext.getToken())) {
			RestfulContext.setToken(LoginService.loginDataProvider(config));
		}
		try {
			return connect(config);
		} catch (final RestClientException e) {
			final HttpStatus status = evaluateStatusCode(e.getMessage());
			if (status == HttpStatus.UNAUTHORIZED) {
				if (attempts > 0) {
					attempts--;
					LOGGER.info(" retrying connect " + attempts);
					RestfulContext.setToken(null);
					return doConnection(config, attempts);
				}
			}
			LOGGER.warn("" + status);
			throw new RestException(LocalDateTime.now(ZoneId.of("Europe/Berlin")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + " " + dtoType.getSimpleName()
			        + " is empty " + e.getMessage());
		}
	}

	private HttpStatus evaluateStatusCode(final String message) {
		if (message.contains("401")) {
			return HttpStatus.UNAUTHORIZED;
		} // weitere?
		return HttpStatus.FORBIDDEN;
	}

	private T connect(final PathBuilder config) throws RestClientException {
		final RestTemplate restTemplate;
        config.addParam("t", RestfulContext.getToken());
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
				restTemplate.setMessageConverters(httpMessageConverters);
                final HttpComponentsClientHttpRequestFactory rf = (HttpComponentsClientHttpRequestFactory) restTemplate.getRequestFactory();
                rf.setReadTimeout(2000);
                rf.setConnectTimeout(2000);
			} catch (Exception e) {
				throw new RestClientException(e.getMessage());
			}
		} else {
			restTemplate = new RestTemplate(httpMessageConverters);
            final SimpleClientHttpRequestFactory rf = (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();
            rf.setReadTimeout(2000);
            rf.setConnectTimeout(2000);
		}

		final ResponseEntity<T> re = restTemplate.getForEntity(config.buildEndpointURI(), dtoType);
		return re.getBody();
	}

	private ObjectMapper createObjectMapper() {
		final ObjectMapper objectMapper = new ObjectMapper();
		// ignore failures
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
		objectMapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, false);
		// relax parsing
		objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		// use arrays
		objectMapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
		return objectMapper;
	}
}
