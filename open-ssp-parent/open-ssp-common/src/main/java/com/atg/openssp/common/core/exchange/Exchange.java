package com.atg.openssp.common.core.exchange;

import com.atg.openssp.common.core.entry.SessionAgent;

/**
 * @author André Schmer
 *
 */
public interface Exchange<T extends SessionAgent> {

	boolean processExchange(T agent) throws Exception;

}
