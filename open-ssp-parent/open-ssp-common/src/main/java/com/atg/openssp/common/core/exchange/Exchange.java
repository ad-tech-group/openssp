package com.atg.openssp.common.core.exchange;

import com.atg.openssp.common.core.entry.SessionAgent;

import java.util.concurrent.ExecutionException;

/**
 * @author André Schmer
 *
 */
public interface Exchange<T extends SessionAgent> {

	boolean processExchange(T agent) throws ExecutionException;

}
