package channel.adapter;

import com.atg.openssp.common.core.entry.SessionAgent;

/**
 * @author André Schmer
 *
 */
public class TrialAdapterBuilder implements AdapterBuilder {

	private final SessionAgent sessionAgent;

	public TrialAdapterBuilder(final SessionAgent sessionAgent) {
		this.sessionAgent = sessionAgent;
	}

	@Override
	public String build() {
		sessionAgent.getRequestid();
		return null;
	}

}
