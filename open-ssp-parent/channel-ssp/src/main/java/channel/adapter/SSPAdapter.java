package channel.adapter;

/**
 * @author André Schmer
 *
 */
public interface SSPAdapter {

	String getCurrency();

	String getEndpoint();

	String getName();

	AdapterConnector getConnector();

}
