package com.atg.openssp.core.exchange;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.atg.openssp.common.core.entry.SessionAgent;
import com.atg.openssp.common.core.exchange.RequestSessionAgent;
import com.atg.openssp.common.core.exchange.channel.rtb.DemandService;
import com.atg.openssp.common.core.system.LocalContext;
import com.atg.openssp.common.provider.AdProviderReader;

import channel.adserving.AdservingService;

/**
 * @author André Schmer
 *
 */
class ChannelFactory {

	/**
	 * Factory method creates a list of potential {@link Callable} to perform biddings. The activation of a channel depends on a implementation of that service
	 * and the activation of the marker in the properties file.
	 * 
	 * @param {@link
	 *            SessionAgent}
	 * @return a List of {@link Callable}
	 * 
	 * @see Callable
	 * @see RequestSessionAgent
	 * @see DemandService
	 * @see AdservingService
	 * @see AdProviderReader
	 */
	static List<Callable<AdProviderReader>> createListOfChannels(final RequestSessionAgent agent) {
		final List<Callable<AdProviderReader>> callables = new ArrayList<>();

		/*
		 * activate if marker in properties file local.runtime.xml is set to true
		 * 
		 */
		if (LocalContext.isDSPChannelEnabled()) {
			callables.add(new DemandService(agent));
		}

		/*
		 * activate if corresponding jar is imported and marker in properties file local.runtime.xml is set to true
		 * 
		 */
		 if (LocalContext.isAdservingChannelEnabled()) {
		 	callables.add(new AdservingService(agent));
		 }

		/*
		 * activate if corresponding jar is imported and marker in properties file local.runtime.xml is set to true
		 * 
		 */
		 if (LocalContext.isSSPChannelEnabled()) {
//		     callables.add(new SSPService(agent));
		 }

		return callables;
	}

}
