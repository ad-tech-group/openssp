package channel.adapter;

import com.atg.openssp.common.core.entry.SessionAgent;
import com.atg.openssp.common.exception.BidProcessingException;

/**
 * @author André Schmer
 *
 */
public interface AdapterConnector {

	String connect(SessionAgent sessionAgent) throws BidProcessingException;

}
