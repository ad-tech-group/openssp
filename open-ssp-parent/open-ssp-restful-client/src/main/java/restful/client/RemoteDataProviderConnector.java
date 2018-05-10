package restful.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;

import restful.context.PathBuilder;
import restful.exception.RestException;

/**
 * Generic approach to connect to the ssp-services and load cache specific data transfer objects.
 * 
 * @author André Schmer
 *
 */
public class RemoteDataProviderConnector {

	/**
	 * Connects to a remote endpoint {@see PathBuilder#buildEndpointURI()} with a basic authentification and GET request method. The authentification
	 * credentials must be provided by the config param {@see PathBuilder#getRemoteUser()} and {@see PathBuilder#getRemotePW()}
	 * 
	 * @param config
	 * @return String with the answer body.
	 * @throws RestException
	 */
	public String connectWithBasicAuth(final PathBuilder config) throws RestException {
		final StringBuilder stringBuilder = new StringBuilder();
		try {
			final HttpURLConnection connection = buildStandardConnection(config.buildEndpointURI().toURL());
			connection.setRequestProperty("Authorization", getAuth(config.getMaster_user(), config.getMaster_pw()));
			try (InputStream inputStream = connection.getInputStream(); BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
				final char[] charBuffer = new char[2048];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			}
		} catch (final Exception e) {
			throw new RestException(this.getClass().getSimpleName() + " -> " + e.getMessage());
		}
		return stringBuilder.toString();
	}

	/**
	 * Connects to a remote endpoint {@see PathBuilder#buildEndpointURI()} with GET request method.
	 * 
	 * @return String with the answer body.
	 * @throws RestException
	 */
	public String connect(final PathBuilder config) throws RestException {
		final StringBuilder stringBuilder = new StringBuilder();
		try {
			final HttpURLConnection connection = buildStandardConnection(config.buildEndpointURI().toURL());
			try (InputStream inputStream = connection.getInputStream(); BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
				final char[] charBuffer = new char[2048];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			}
		} catch (final Exception e) {
			throw new RestException(this.getClass().getSimpleName() + " -> " + e.getMessage());
		}
		return stringBuilder.toString();
	}

	private String getAuth(final String user, final String pw) {
		final String credentials = new String(user + ":" + pw);
		final String encoding = Base64.encodeBase64String(credentials.getBytes());
		return "Basic " + encoding;
	}

	private HttpURLConnection buildStandardConnection(final URL url) throws IOException {
		final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setConnectTimeout(5000);
		connection.setReadTimeout(5000);
		return connection;
	}

}
