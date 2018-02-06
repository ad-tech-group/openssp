package channel.cache;

import java.io.Serializable;

import channel.adapter.AdapterConnector;
import channel.adapter.SSPAdapter;

/**
 * @author André Schmer
 *
 */
public class BasicAdapter implements Serializable, SSPAdapter {

	private static final long serialVersionUID = -5648775770118011066L;

	private String name;

	private String currency;

	private String endpoint;

	private static final boolean connectionkeepAlive = true;

	private String contentType;

	private String connectorClass;

	private AdapterConnector connector;

	public BasicAdapter() {}

	@Override
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(final String currency) {
		this.currency = currency;
	}

	@Override
	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(final String endpoint) {
		this.endpoint = endpoint;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(final String contentType) {
		this.contentType = contentType;
	}

	public boolean isConnectionkeepAlive() {
		return connectionkeepAlive;
	}

	@Override
	public AdapterConnector getConnector() {
		return connector;
	}

	public void setConnector(final AdapterConnector connector) {
		this.connector = connector;
	}

	public String getConnectorClass() {
		return connectorClass;
	}

	public void setConnectorClass(final String connectorClass) {
		this.connectorClass = connectorClass;
	}

}
