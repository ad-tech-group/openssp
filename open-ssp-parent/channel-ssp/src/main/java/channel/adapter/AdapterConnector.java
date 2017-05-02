package channel.adapter;

import com.atg.openssp.common.core.entry.SessionAgent;

import common.BidProcessingException;

/**
 * @author André Schmer
 *
 */
public interface AdapterConnector {

	String connect(SessionAgent sessionAgent) throws BidProcessingException;

}
