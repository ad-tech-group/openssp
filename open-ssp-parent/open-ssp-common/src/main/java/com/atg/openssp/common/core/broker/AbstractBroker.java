package com.atg.openssp.common.core.broker;

import com.atg.openssp.common.core.entry.SessionAgent;

/**
 * @author André Schmer
 *
 */
public class AbstractBroker {

	protected SessionAgent sessionAgent;

	public SessionAgent getSessionAgent() {
		return sessionAgent;
	}

	public void setSessionAgent(final SessionAgent sessionAgent) {
		this.sessionAgent = sessionAgent;
	}

}
