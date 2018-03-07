package restful.context;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * Information holder about connection to a remote system.
 * 
 * @author André Schmer
 * 
 */
public class PathBuilder {

	private final Logger LOGGER = LoggerFactory.getLogger(PathBuilder.class);

	private final StringBuilder path;
	private String mediaType;
	private String host;
	private String port;
	private String master_user;
	private String master_pw;
	private String scheme;
	private final Map<String, String> parameter;

	public PathBuilder() {
		parameter = new HashMap<>();
		path = new StringBuilder();
	}

	/**
	 * Adds the path to the pathbuilder. If the Path does not contain a leading slash, it will added.
	 * 
	 * @param path
	 * @return this
	 */
	public PathBuilder addPath(final Path path) {
		final String value = path.getValue();
		if (!value.startsWith("/")) {
			this.path.append("/");
		}
		this.path.append(value);
		return this;
	}

	public String getPath() {
		return path.toString();
	}

	/**
	 * Adds the key and its value to the list of params.
	 * 
	 * @param key
	 * @param value
	 * @return this
	 */
	public PathBuilder addParam(final String key, final String value) {
		parameter.put(key, value);
		return this;
	}

	/**
	 * Adds a media type to this pathbuilder.
	 * 
	 * @param mediaType
	 * @return this
	 */
	public PathBuilder addMediaType(final String mediaType) {
		this.mediaType = mediaType;
		return this;
	}

	public String getMediaType() {
		return mediaType;
	}

	/**
	 * Sets the value for the Host of this path.
	 * 
	 * @param host
	 */
	public void setHost(final String host) {
		this.host = host;
	}

	/**
	 * Sets the value for the Host of this path.
	 *
	 * @param port
	 */
	public void setPort(final String port) {
		this.port = port;
	}

	public void setScheme(final String scheme) {
		this.scheme = scheme;
	}

	public String getScheme() {
		return scheme;
	}

	public String getHost() {
		return host;
	}

	public String getServer() {
		return (StringUtils.isEmpty(host) ? "" : host) + (StringUtils.isEmpty(port) ? "" : ":"+port);
	}

	public String getPort() {
		return port;
	}

	public String getMaster_user() {
		return master_user;
	}

	public void setMaster_user(final String master_user) {
		this.master_user = master_user;
	}

	public String getMaster_pw() {
		return master_pw;
	}

	public void setMaster_pw(final String master_pw) {
		this.master_pw = master_pw;
	}

	/**
	 * Build a complete {@see URI} of the endpoint including the {@code scheme}, {@code host}, {@code path} and {@code parameters}.
	 * 
	 * @return {@see URI}
	 */
	public URI buildEndpointURI() {
		try {
			final List<NameValuePair> nameValuePairs = new ArrayList<>();
			if (!parameter.isEmpty()) {
				parameter.forEach((k, v) -> nameValuePairs.add(new BasicNameValuePair(k, v)));
			}
			return new URIBuilder().setCharset(StandardCharsets.UTF_8).setScheme(getScheme().toLowerCase()).setHost(getServer()).setPath(path.toString()).addParameters(
			        nameValuePairs).build();
		} catch (final URISyntaxException e) {
			LOGGER.error(e.getMessage());
		}
		return null;
	}

}
