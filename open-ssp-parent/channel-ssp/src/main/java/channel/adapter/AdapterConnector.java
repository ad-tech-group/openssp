package channel.adapter;

import java.io.Serializable;

import com.atg.openssp.common.core.entry.SessionAgent;
import com.atg.openssp.common.exception.BidProcessingException;

/**
 * @author André Schmer
 *
 */
public interface AdapterConnector extends Serializable {

	String connect(SessionAgent sessionAgent) throws BidProcessingException;

}
